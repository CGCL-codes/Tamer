package de.jlab.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "board-channel-definition")
public class BoardChannelDefinitionConfig {

    @XmlAttribute(required = true, name = "type")
    LabModuleEnum type;

    @XmlAttribute(name = "subtype")
    String subtype = null;

    @XmlAttribute(required = true, name = "subchannel")
    int subchannel;

    @XmlAttribute(required = true, name = "readonly")
    boolean readonly;

    @XmlAttribute(required = true, name = "name")
    String name;

    public LabModuleEnum getType() {
        return type;
    }

    public void setType(LabModuleEnum type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public int getSubchannel() {
        return subchannel;
    }

    public void setSubchannel(int subchannel) {
        this.subchannel = subchannel;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
