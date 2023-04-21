package com.express.service.mapping.converters;

import com.express.domain.Status;
import com.googlecode.simpleobjectassembler.converter.AbstractObjectConverter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusConverter extends AbstractObjectConverter<String, Status> {

    @Override
    public Status createDestinationObject(String source) {
        return Status.getStatus(source);
    }

    @Override
    protected boolean disableAutoMapping() {
        return true;
    }
}
