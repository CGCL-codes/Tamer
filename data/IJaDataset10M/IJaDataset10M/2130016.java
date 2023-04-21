package com.safi.workshop.sqlexplorer.parsers;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public abstract class AbstractQuery implements Query {

    private HashMap<String, NamedParameter> parameters;

    private LinkedHashSet<QueryParameter> queryParameters;

    void setParameters(HashMap<String, NamedParameter> parameters) {
        this.parameters = parameters;
    }

    public Map<String, NamedParameter> getNamedParameters() {
        return parameters;
    }

    public LinkedHashSet<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    public void setQueryParameters(LinkedHashSet<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }
}
