package com.sns2Life.common.persistence.vo;

import java.util.Map;

public class UpdateConditionVO {

    private Class clasz;

    private Map<String, Object> params;

    private Map<String, Object> filter;

    public Class getClasz() {
        return clasz;
    }

    public void setClasz(Class clasz) {
        this.clasz = clasz;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, Object> getFilter() {
        return filter;
    }

    public void setFilter(Map<String, Object> filter) {
        this.filter = filter;
    }
}
