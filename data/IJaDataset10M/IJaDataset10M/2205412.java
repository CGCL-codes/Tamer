package org.escapek.core.internal.model.cmdb.classes;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PROPERTY_TYPES")
public class PropertyType implements Serializable {

    private static final long serialVersionUID = -7274096486880362374L;

    private Long Id;

    private String typeName;

    private String dtoType;

    private String javaType;

    public PropertyType() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TYPE_ID")
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    @Column(name = "TYPE_NAME")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(name = "DTO_TYPE")
    public String getDtoType() {
        return dtoType;
    }

    public void setDtoType(String dtoType) {
        this.dtoType = dtoType;
    }

    @Column(name = "JAVA_TYPE")
    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }
}
