package com.objectsql.introspection;

public class UserMappingFieldDefinition extends ParameterMappingDefinition {

    @Override
    public Object getColumnValue() {
        return "DUMMY_USER";
    }

    @Override
    public boolean hasNotNullValue() {
        return true;
    }
}
