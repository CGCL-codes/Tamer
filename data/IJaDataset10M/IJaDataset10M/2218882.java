package org.usca.workshift.database.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;

/**
 * Created by IntelliJ IDEA.
 * User: dantonetti
 * Date: Jul 15, 2008
 * Time: 4:53:53 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityXmlBinder<T extends BaseModel> {

    @XmlElement
    private Class clazz;

    @XmlElement
    private Long id;

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
