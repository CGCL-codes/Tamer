package com.express.service.mapping.converters;

import com.express.domain.Methodology;
import com.googlecode.simpleobjectassembler.converter.AbstractObjectConverter;
import org.springframework.stereotype.Component;

@Component
public class StringToMethodologyConverter extends AbstractObjectConverter<String, Methodology> {

    @Override
    public Methodology createDestinationObject(String methodologyName) {
        return Methodology.getMethodology(methodologyName);
    }
}
