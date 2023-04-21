package com.icteam.fiji.persistence.conf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "hql-query-template")
public class VelHqlQueryTemplate extends VelQueryTemplate {

    public VelHqlQueryTemplate() {
    }
}
