package com.tristia.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "father")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class Father extends Human {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "father", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Child> childs = new HashSet<Child>();

    @OneToMany(mappedBy = "father", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<House> houses = new HashSet<House>();

    public Father() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Child> getChilds() {
        return childs;
    }

    public void setChilds(Set<Child> childs) {
        this.childs = childs;
    }

    public Set<House> getHouses() {
        return houses;
    }

    public void setHouses(Set<House> houses) {
        this.houses = houses;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((childs == null) ? 0 : childs.hashCode());
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Father other = (Father) obj;
        if (childs == null) {
            if (other.childs != null) return false;
        } else if (!childs.equals(other.childs)) return false;
        if (id != other.id) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Father [id=" + id + ", name=" + name + ", childs=" + childs + "]";
    }
}
