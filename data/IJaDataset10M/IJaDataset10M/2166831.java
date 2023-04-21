package com.example.faults;

import javax.xml.bind.annotation.AccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(AccessType.FIELD)
@XmlType(name = "InputMessageValidationFaultType")
public class InputMessageValidationFaultType {

    @XmlAttribute
    protected String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String value) {
        this.msg = value;
    }
}
