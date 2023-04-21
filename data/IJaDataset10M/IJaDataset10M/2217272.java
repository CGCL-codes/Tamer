package org.usca.workshift.database.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import java.util.List;
import java.io.Serializable;

@Entity
@Table(name = "house")
@XmlType
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class House extends BaseModel implements Serializable {

    @Column(name = "name", unique = true)
    @XmlElement
    private String name;

    @Column(name = "hours_requirement")
    @XmlElement
    private float hoursRequirement;

    @OneToMany(targetEntity = Member.class, cascade = CascadeType.ALL, mappedBy = "house")
    @XmlElement
    private List<Member> members;

    @OneToMany(targetEntity = Workshift.class, cascade = CascadeType.ALL, mappedBy = "house")
    @XmlElement
    private List<Workshift> workshift;

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getHoursRequirement() {
        return hoursRequirement;
    }

    public void setHoursRequirement(float hoursRequirement) {
        this.hoursRequirement = hoursRequirement;
    }

    public List<Workshift> getWorkshift() {
        return workshift;
    }

    public void setWorkshift(List<Workshift> workshift) {
        this.workshift = workshift;
    }
}
