package com.multimedia.seabattle.controllers;

import java.util.List;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.multimedia.seabattle.model.beans.Country;
import com.multimedia.seabattle.service.country.ICountryService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class CountryController {

    private static final Logger logger = LoggerFactory.getLogger(CountryController.class);

    private ICountryService country_service;

    /**
	 * get all countries starting with prefix
	 */
    @RequestMapping(value = "/json/countries.htm")
    @ResponseBody
    public List<Country> getConutries(@RequestParam(value = "mask") String prefix) {
        if (logger.isDebugEnabled()) {
            logger.debug("getting countries starting with:" + prefix);
        }
        return country_service.getCountries(prefix);
    }

    @Required
    @Resource(name = "CountryService")
    public void setCountryService(ICountryService value) {
        this.country_service = value;
    }
}
