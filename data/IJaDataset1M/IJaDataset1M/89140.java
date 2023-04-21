package com.jettmarks.bkthn.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Tag implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @OneToMany
    private Set<LogTagMap> logTagMaps = new HashSet<LogTagMap>(0);

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Tag(String name, Set<LogTagMap> logTagMaps) {
        this.name = name;
        this.logTagMaps = logTagMaps;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<LogTagMap> getLogTagMaps() {
        return this.logTagMaps;
    }

    public void setLogTagMaps(Set<LogTagMap> logTagMaps) {
        this.logTagMaps = logTagMaps;
    }
}
