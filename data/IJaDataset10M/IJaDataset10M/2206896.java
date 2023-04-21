package edu.xjtu.jadd.impl.runtime;

import edu.xjtu.jadd.runtime.RuntimeComponent;

public class BindingLocation {

    private String type;

    private String location;

    private String extradata;

    private RuntimeComponent bindingcp;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExtradata() {
        return extradata;
    }

    public void setExtradata(String extradata) {
        this.extradata = extradata;
    }

    public RuntimeComponent getBindingcp() {
        return bindingcp;
    }

    public void setBindingcp(RuntimeComponent bindingcp) {
        this.bindingcp = bindingcp;
    }
}
