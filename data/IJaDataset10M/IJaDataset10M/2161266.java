package com.hyper9.simdk.stubs.dao;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import com.hyper9.simdk.stubs.JAXBAnnotated;
import com.hyper9.simdk.stubs.SerializableObjectWrapper;

/**
 * A dynamic property.
 * 
 * @author akutz
 * 
 */
@XmlType(name = "DynamicProperty", propOrder = { "name", "val" })
@Entity
@JAXBAnnotated
public class DynamicProperty implements Serializable {

    /**
     *The generated serial version UID.
     */
    private static final long serialVersionUID = -4186968128874685857L;

    private String name;

    private SerializableObjectWrapper valWrapper;

    private Object val;

    public void setVal(Object toSet) {
        this.val = toSet;
        this.valWrapper = SerializableObjectWrapper.wrap(toSet);
    }

    @Transient
    public Object getVal() {
        if (this.val != null) {
            return this.val;
        } else if (this.valWrapper == null) {
            return null;
        } else {
            this.val = this.valWrapper.unwrap();
            return this.val;
        }
    }

    public void setValWrapper(SerializableObjectWrapper toSet) {
        this.valWrapper = toSet;
    }

    @XmlTransient
    @Column(name = "C6b08258f1152a4ec6d98a688493b54be")
    public SerializableObjectWrapper getValWrapper() {
        if (this.valWrapper == null) {
            return null;
        } else {
            return this.valWrapper;
        }
    }

    @Column(name = "C21e121b9a9faab50ac866a3a6cb4d3db")
    public String getName() {
        return name;
    }

    public void setName(String toSet) {
        this.name = toSet;
    }

    private String jpaId = java.util.UUID.randomUUID().toString();

    @Id
    @Column(name = "jpaId")
    @XmlTransient
    public String getJpaId() {
        return this.jpaId;
    }

    public void setJpaId(String toSet) {
        this.jpaId = toSet;
    }
}
