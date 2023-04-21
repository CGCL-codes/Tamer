package org.blueoxygen.cimande.domain;

import java.util.List;
import javax.persistence.Basic;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import static javax.persistence.InheritanceType.SINGLE_TABLE;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author leo
 */
@Entity
@Table(name = "gx_droplist_name")
@Inheritance(strategy = SINGLE_TABLE)
public class GxDroplistName extends DefaultPersistence {

    private static final long serialVersionUID = 1L;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private GxDroplistName parent;

    @OneToMany(mappedBy = "name", cascade = ALL)
    private List<GxDroplistValue> values;

    @OneToMany(mappedBy = "parent")
    private List<GxDroplistName> childs;

    public String getName() {
        return name;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String aDescription) {
        description = aDescription;
    }

    public GxDroplistName getParent() {
        return parent;
    }

    public void setParent(GxDroplistName aParent) {
        parent = aParent;
    }

    public List<GxDroplistValue> getValues() {
        return values;
    }

    public void setValues(List<GxDroplistValue> valuez) {
        values = valuez;
    }

    public List<GxDroplistName> getChilds() {
        return childs;
    }

    public void setChilds(List<GxDroplistName> childz) {
        childs = childz;
    }
}
