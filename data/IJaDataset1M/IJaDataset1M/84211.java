package com.google.code.linkedinapi.schema.impl;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.google.code.linkedinapi.schema.ProductType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "product-type", propOrder = { "code", "name" })
public class ProductTypeImpl implements Serializable, ProductType {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(required = true)
    protected String code;

    @XmlElement(required = true)
    protected String name;

    public String getCode() {
        return code;
    }

    public void setCode(String value) {
        this.code = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }
}
