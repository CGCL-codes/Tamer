package de.jlab.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "connection-channel")
public class ConnectionChannelConfig {

    @XmlAttribute(required = true, name = "classname")
    String classname = null;

    @XmlAttribute(required = true, name = "channelname")
    String channelname = null;

    @XmlElement(name = "parameter")
    List<ParameterConfig> params = null;

    public ConnectionChannelConfig(String classname, String channelname) {
        this.classname = classname;
        this.channelname = channelname;
    }

    public ConnectionChannelConfig() {
    }

    public String getChannelname() {
        return channelname;
    }

    public void setChannelname(String channelname) {
        this.channelname = channelname;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public List<ParameterConfig> getParams() {
        return params;
    }

    public void setParams(List<ParameterConfig> params) {
        this.params = params;
    }

    public void addParam(String name, String value) {
        if (params == null) params = new ArrayList<ParameterConfig>();
        params.add(new ParameterConfig(name, value));
    }

    public HashMap<String, String> getParametersAsHashMap() {
        if (params == null) return null;
        HashMap<String, String> paramMap = new HashMap<String, String>();
        for (ParameterConfig currParam : params) {
            paramMap.put(currParam.getName(), currParam.getValue());
        }
        return paramMap;
    }
}
