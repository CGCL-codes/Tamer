package com.google.code.linkedinapi.schema.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import com.google.code.linkedinapi.schema.Adapter1;
import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Educations;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "educationList" })
@XmlRootElement(name = "educations")
public class EducationsImpl implements Serializable, Educations {

    private static final long serialVersionUID = 2461660169443089969L;

    @XmlElement(name = "education", required = true, type = EducationImpl.class)
    protected List<Education> educationList;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(Adapter1.class)
    protected Long total;

    public List<Education> getEducationList() {
        if (educationList == null) {
            educationList = new ArrayList<Education>();
        }
        return this.educationList;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long value) {
        this.total = value;
    }
}
