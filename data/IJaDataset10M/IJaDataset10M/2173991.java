package com.legstar.xsdc.test.cases.cultureinfo.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "CultureInfoException", namespace = "http://cultureinfo.cases.test.xsdc.legstar.com/")
@XmlType(name = "CultureInfoException", namespace = "http://cultureinfo.cases.test.xsdc.legstar.com/", propOrder = { "message" })
@XmlAccessorType(XmlAccessType.FIELD)
public class CultureInfoExceptionBean {

    @XmlElement(name = "message", namespace = "")
    private String message;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
