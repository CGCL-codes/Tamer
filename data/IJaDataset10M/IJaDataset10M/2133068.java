package com.ericsson.xsmp.service.alert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class AlertTask implements Serializable {

    String alertType;

    String message;

    Map<String, String> paramMap = new HashMap<String, String>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public void setParameter(String name, String value) {
        paramMap.put(name, value);
    }

    public String getParameter(String name) {
        return paramMap.get(name);
    }
}
