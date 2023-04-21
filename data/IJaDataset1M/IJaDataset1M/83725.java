package com.jhyle.sce.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@javax.persistence.Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Type extends Entity {

    private String name;

    private String label;

    private List<Field> fields;

    private String originalName = null;

    public static final String ENTITY_TYPE = "type";

    public Type() {
    }

    public Type(String name) {
        this(name, "");
    }

    public Type(String name, String label) {
        this.name = name;
        this.label = label;
    }

    @Transient
    public String getType() {
        return ENTITY_TYPE;
    }

    public boolean equals(Object o) {
        if (o instanceof Type) {
            Type type = (Type) o;
            return StringUtils.equals(type.getName(), getName());
        }
        return false;
    }

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (originalName == null) {
            originalName = name;
        }
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Transient
    public String getOriginalName() {
        return originalName;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Field> getFields() {
        if (fields == null) {
            return Collections.emptyList();
        } else {
            return fields;
        }
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public synchronized void addField(Field field) {
        if (fields == null) {
            fields = new ArrayList<Field>();
        }
        fields.add(field);
    }

    public synchronized void moveFieldUp(Field field) {
        if (fields == null) return;
        int position = fields.indexOf(field);
        if (position > 0) {
            Field tmp = fields.get(position - 1);
            fields.set(position - 1, field);
            fields.set(position, tmp);
        }
    }

    public synchronized void moveFieldDown(Field field) {
        if (fields == null) return;
        int position = fields.indexOf(field);
        if (position > -1 && position < fields.size() - 1) {
            Field tmp = fields.get(position + 1);
            fields.set(position + 1, field);
            fields.set(position, tmp);
        }
    }

    public synchronized void removeField(Field field) {
        if (fields != null) {
            fields.remove(field);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(name).append(";");
        sb.append("originalName=").append(originalName).append(";");
        if (fields != null) for (Field field : fields) {
            sb.append(field);
        }
        return sb.toString();
    }
}
