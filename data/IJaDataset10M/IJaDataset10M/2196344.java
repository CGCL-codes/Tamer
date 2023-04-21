package org.identifylife.taxonomy.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author dbarnier
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Person extends Model {

    @XmlValue
    private String name;

    public Person() {
    }

    public Person(String uuid) {
        setUuid(uuid);
    }

    public Person(String uuid, String name) {
        this(uuid);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("name", getName()).toString();
    }
}
