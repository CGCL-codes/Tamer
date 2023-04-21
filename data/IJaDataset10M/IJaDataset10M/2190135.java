package org.systemsbiology.apps.corragui.client.data.project;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SetupOption implements Serializable, ISetupOption {

    private String name;

    private String value;

    public SetupOption() {
    }

    public SetupOption(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String toString() {
        return "Name: " + name + "; Value: " + value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String newVal) {
        value = newVal;
    }

    public String getOptionAsString(String separator) {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getName());
        buf.append(separator);
        buf.append(this.getValue());
        return buf.toString();
    }

    public ISetupOption copy() {
        return new SetupOption(name, value);
    }
}
