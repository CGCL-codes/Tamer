package org.slasoi.ord.framework.vo;

import java.util.Map;

public class PartyVO {

    private String role;

    private Map<String, String> properties;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    public PartyVO() {
    }
}
