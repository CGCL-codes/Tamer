package com.google.code.linkedinapi.schema.impl;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.google.code.linkedinapi.schema.Adapter1;
import com.google.code.linkedinapi.schema.QuestionCategory;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "code", "name" })
@XmlRootElement(name = "question-category")
public class QuestionCategoryImpl implements Serializable, QuestionCategory {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(required = true)
    protected String code;

    @XmlElement(required = true)
    protected String name;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(Adapter1.class)
    protected Long key;

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

    public Long getKey() {
        return key;
    }

    public void setKey(Long value) {
        this.key = value;
    }
}
