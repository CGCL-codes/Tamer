package org.blueoxygen.cimande.gx.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.blueoxygen.cimande.commons.DefaultPersistence;

/**
 * @author leo
 * 
 */
@Entity
@Table(name = "gx_droplist_name", uniqueConstraints = { @UniqueConstraint(columnNames = "name") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class GxDroplistName extends DefaultPersistence {

    private String name;

    private GxDroplistName parent;

    private List<GxDroplistValue> values;

    private List<GxDroplistName> childs;

    private String description;

    @Column(name = "name", unique = true, nullable = false)
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

    @ManyToOne
    public GxDroplistName getParent() {
        return parent;
    }

    public void setParent(GxDroplistName parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "name")
    public List<GxDroplistValue> getValues() {
        return values;
    }

    public void setValues(List<GxDroplistValue> values) {
        this.values = values;
    }

    @OneToMany(mappedBy = "parent")
    public List<GxDroplistName> getChilds() {
        return childs;
    }

    public void setChilds(List<GxDroplistName> childs) {
        this.childs = childs;
    }
}
