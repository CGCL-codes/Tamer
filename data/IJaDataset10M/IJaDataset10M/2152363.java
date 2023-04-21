package org.wfp.rita.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.wfp.rita.pojo.base.IdentifiedByInteger;
import org.wfp.rita.pojo.face.DescribedObject;
import org.wfp.rita.pojo.face.Insecurable;

/**
 * BuildingType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "building_type")
public class BuildingType extends IdentifiedByInteger implements DescribedObject, Insecurable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "description", nullable = false)
    private String description;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
