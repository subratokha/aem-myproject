/*
	*  Copyright 2015 Adobe Systems Incorporated
	*
	*  Licensed under the Apache License, Version 2.0 (the "License");
	*  you may not use this file except in compliance with the License.
	*  You may obtain a copy of the License at
	*
	*      http://www.apache.org/licenses/LICENSE-2.0
	*
	*  Unless required by applicable law or agreed to in writing, software
	*  distributed under the License is distributed on an "AS IS" BASIS,
	*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	*  See the License for the specific language governing permissions and
	*  limitations under the License.
	*/
package com.aem.myproject.core.servlets;

import com.aem.myproject.core.entity.WeatherReport;
import com.aem.myproject.core.services.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class,
				property = {
								"sling.servlet.methods=" + HttpConstants.METHOD_GET,
								"sling.servlet.resourceTypes=" + "myproject/components/content/weatherreport",
								"sling.servlet.extensions=" + "json"
				})
@ServiceDescription("Weather Servlet")
public class WeatherServlet extends SlingSafeMethodsServlet {
				
				private static final String CITY_ID = "cityId";
				@Reference
				private transient WeatherService weatherService;
				
				@Override
				protected void doGet(final SlingHttpServletRequest request,
																									final SlingHttpServletResponse response) throws IOException {
								final Resource resource = request.getResource();
								String cityId = resource.getValueMap().get(CITY_ID, String.class);
								if (cityId == null || cityId.equals("")) {
												response.sendError(HttpStatus.SC_BAD_REQUEST, "City Id Not Present");
												return;
								}
								WeatherReport weather = weatherService.getWeatherReport(cityId.trim());
								ObjectMapper objectMapper = new ObjectMapper();
								String result = objectMapper.writeValueAsString(weather);
								
								response.setContentType("application/json");
								response.getWriter().write(result);
				}
}
