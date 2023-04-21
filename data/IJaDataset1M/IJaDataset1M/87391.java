package net.sf.balm.security.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * 
 * @author dz
 */
@Entity
@Table(name = "s_class_resource")
public class ClassResource extends Resource {

    private String targetClassName;

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String entityClassName) {
        this.targetClassName = entityClassName;
    }
}
