package com.sporthenon.db.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.sporthenon.db.entity.meta.Metadata;

@Entity
@Table(name = "\"TYPE\"")
public class Type {

    public static final transient String alias = "TP";

    @Id
    @SequenceGenerator(name = "sq_type", sequenceName = "\"SQ_TYPE\"")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_type")
    private Integer id;

    @Column(name = "label", length = 25, nullable = false)
    private String label;

    @Column(name = "label_fr", length = 25, nullable = false)
    private String labelFr;

    @Column(name = "\"number\"", nullable = false)
    private Integer number;

    @Embedded
    private Metadata metadata;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        this.labelFr = label;
    }

    public String getLabelFr() {
        return labelFr;
    }

    public void setLabelFr(String labelFr) {
        this.labelFr = labelFr;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
