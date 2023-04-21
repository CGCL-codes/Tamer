package org.docflower.objectserializer.params;

import java.util.*;

public abstract class AbstractParamHolder {

    private Map<String, Object> params = new HashMap<String, Object>();

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void addParam(String paramName, Object param) {
        if (!(getParams().containsKey(paramName)) && !(getParams().containsValue(param))) {
            getParams().put(paramName, param);
        }
    }

    public void replaceParam(String paramName, Object param) {
        getParams().put(paramName, param);
    }

    public Object getParamByName(String paramName) {
        return getParams().get(paramName);
    }
}
