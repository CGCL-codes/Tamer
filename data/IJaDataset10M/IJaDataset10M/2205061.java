package com.antlersoft.query;

public interface CountPreservingValueContext extends ValueContext {

    public void inputObject(ValueObject value, DataSource source, Object input);
}
