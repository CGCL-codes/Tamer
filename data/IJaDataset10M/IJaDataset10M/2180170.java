package com.fastfly.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.convert.converters.StringToDate;
import org.springframework.binding.convert.service.DefaultConversionService;
import org.springframework.stereotype.Service;
import com.fastfly.spring.mvc.converter.CityConverter;
import com.fastfly.spring.mvc.converter.SecurityQuestionConverter;

@Service("customConversionService")
public class ConversionService extends DefaultConversionService {

    protected FlightService baseService;

    public FlightService getBaseService() {
        return baseService;
    }

    public void setBaseService(FlightService baseService) {
        this.baseService = baseService;
    }

    @Autowired
    public ConversionService(FlightService baseService) {
        super();
        this.baseService = baseService;
        addCustomConverters();
    }

    protected void addCustomConverters() {
        addConverter("cityConverter", new CityConverter(baseService));
        addConverter("securityQuestionConverter", new SecurityQuestionConverter(baseService));
        StringToDate stringToDate = new StringToDate();
        stringToDate.setPattern("MM-dd-yyyy");
        addConverter("shortDate", stringToDate);
    }
}
