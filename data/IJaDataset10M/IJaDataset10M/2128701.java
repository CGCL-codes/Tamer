package org.spnt.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Setting")
public class Setting {

    @XmlElement(name = "config")
    private SpntServletConfig config;

    @XmlTransient
    public SpntServletConfig getConfig() {
        return config;
    }

    public void setConfig(SpntServletConfig config) {
        this.config = config;
    }
}
