package org.jsf2jpa.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Class implements Hierarhy entity
 *
 * <br/>$LastChangedRevision:$
 * <br/>$LastChangedDate:$
 *
 * @author ASementsov
 */
@Entity
@Table(name = "HIER")
public class Hierarhy extends BaseEntity implements Serializable {

    /**
     * Subversion revision number it will be changed automatically when commited
     */
    private static final String REV_NUMBER = "$Revision:$";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIER_SEQ", strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Hierarhy parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID")
    private List<Hierarhy> children = new ArrayList<Hierarhy>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<HierarhyAttribute> attributes = new ArrayList<HierarhyAttribute>();

    public List<Hierarhy> getChildren() {
        return children;
    }

    public void setChildren(List<Hierarhy> children) {
        this.children = children;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Hierarhy getParent() {
        return parent;
    }

    public void setParent(Hierarhy parent) {
        this.parent = parent;
    }

    public List<HierarhyAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<HierarhyAttribute> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(HierarhyAttribute attr) {
        attr.setParent(this);
        attributes.add(attr);
    }

    public void addAttributes(List<HierarhyAttribute> attrs) {
        for (HierarhyAttribute attr : attrs) {
            addAttribute(attr);
        }
    }

    public void removeAttribute(HierarhyAttribute attr) {
        int index = attributes.indexOf(attr);
        if (index != -1) {
            HierarhyAttribute a = attributes.get(index);
            attributes.remove(index);
            attr.setParent(null);
            a.setParent(null);
        }
    }
}
