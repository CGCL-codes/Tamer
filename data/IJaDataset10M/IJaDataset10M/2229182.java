package nl.joppla.ejb.entity.utils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_PROPERTIES_VW")
@NamedQuery(name = "SystemProperty.findAll", query = "select o from SystemProperty o")
public class SystemProperty {

    @Id
    @Column(nullable = false, unique = true, updatable = false)
    private Long Id;

    @Column(name = "GROUP_NAME")
    private String group;

    @Column(name = "PROPERTY_NAME")
    private String name;

    @Column(name = "PROPERTY_VALUE")
    private String value;

    public SystemProperty() {
    }

    public SystemProperty(String group, String name, String value) {
        this.group = group;
        this.name = name;
        this.value = value;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Long getId() {
        return Id;
    }
}
