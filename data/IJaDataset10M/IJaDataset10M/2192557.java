package com.weebill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.validator.NotNull;
import org.trails.descriptor.annotation.PropertyDescriptor;

@Entity
public class ActionPathElement implements Comparable<ActionPathElement> {

    private Integer id;

    private Integer order;

    private ActionPath actionPath;

    @ManyToOne
    @PropertyDescriptor(index = 1)
    @JoinColumn(name = "actionPathId")
    @NotNull
    public ActionPath getActionPath() {
        return actionPath;
    }

    public void setActionPath(ActionPath actionPath) {
        this.actionPath = actionPath;
    }

    @Id
    @GeneratedValue
    @PropertyDescriptor(index = 0)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @PropertyDescriptor(index = 2)
    @Column(name = "sequenceNo")
    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public int compareTo(ActionPathElement that) {
        if (this.getOrder() == null) return -1;
        if (that == null) return 1;
        int o = this.getOrder().compareTo(that.getOrder());
        return o;
    }

    @Override
    public boolean equals(Object object) {
        if (this.getId() == null) return false;
        if (object == null || !(object instanceof ActionPathElement)) return false;
        return getId().equals(((ActionPathElement) object).getId());
    }
}
