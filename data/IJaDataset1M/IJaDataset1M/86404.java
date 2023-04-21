package at.ac.univie.zsu.aguataplan.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**
 * @author gerry
 * 
 */
@Entity
public class EventDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column
    private Long id;

    @Version
    @Column
    private Integer version;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    private DetectedEvent refDetectedEvent;

    @ManyToOne
    private Keyword refKeyword;

    public EventDescription() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Description: ").append(getDescription()).append(", ");
        sb.append("Id: ").append(getId()).append(", ");
        sb.append("Name: ").append(getName()).append(", ");
        sb.append("RefDetectedEvent: ").append(getRefDetectedEvent()).append(", ");
        sb.append("RefKeyword: ").append(getRefKeyword()).append(", ");
        sb.append("Version: ").append(getVersion());
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DetectedEvent getRefDetectedEvent() {
        return refDetectedEvent;
    }

    public void setRefDetectedEvent(DetectedEvent refDetectedEvent) {
        this.refDetectedEvent = refDetectedEvent;
    }

    public Keyword getRefKeyword() {
        return refKeyword;
    }

    public void setRefKeyword(Keyword refKeyword) {
        this.refKeyword = refKeyword;
    }
}
