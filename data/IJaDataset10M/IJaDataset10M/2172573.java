package com.icteam.fiji.persistence.template.handlers;

import org.hibernate.Query;

public class BooleanParameterHandler extends AbstractParameterHandler<Boolean> {

    public BooleanParameterHandler(String name, Boolean value) {
        super(name, value);
    }

    public void handleParameter(Query query) {
        query.setBoolean(this.getName(), this.getValue());
    }
}
