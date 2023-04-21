package de.schwarzrot.data.test.domain;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.schwarzrot.data.Entity;
import de.schwarzrot.data.NamedChildEntity;
import de.schwarzrot.data.support.AbstractEntity;

public class SimpleTestEntity extends AbstractEntity implements NamedChildEntity<String, Entity> {

    public static final String TEST_SCHEMA = "srtst";

    private static final long serialVersionUID = 713L;

    private Entity parent;

    private String name;

    private int value;

    private int weight;

    private int ordinal;

    @Override
    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        String ov = this.name;
        this.name = name;
        if (ov != null) {
            if (name == null || ov.compareTo(name) != 0) setDirty(true);
        } else if (ov != name) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "name", ov, name);
        this.firePropertyChange(pce);
    }

    public final int getValue() {
        return value;
    }

    public final void setValue(int value) {
        Integer ov = this.value;
        this.value = value;
        if (!ov.equals(value)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "value", ov, value);
        this.firePropertyChange(pce);
    }

    @Override
    public Map<String, String> getMappings() {
        Map<String, String> mappings = super.getMappings();
        mappings.put(PARENT_ATTR_NAME, "pid");
        return mappings;
    }

    @Override
    public Entity getParent() {
        return parent;
    }

    @Override
    public Class<Entity> getParentType() {
        return Entity.class;
    }

    @Override
    public void setParent(Entity parent) {
        this.parent = parent;
    }

    @Override
    public final void setDirty(boolean dirty) {
        super.setDirty(dirty);
        if (parent != null && dirty) parent.setDirty(true);
    }

    public final int getWeight() {
        return weight;
    }

    public final void setWeight(int weight) {
        Integer ov = this.weight;
        this.weight = weight;
        if (!ov.equals(weight)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "weight", ov, weight);
        this.firePropertyChange(pce);
    }

    public final int getOrdinal() {
        return ordinal;
    }

    public final void setOrdinal(int ordinal) {
        Integer ov = this.ordinal;
        this.ordinal = ordinal;
        if (!ov.equals(ordinal)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "ordinal", ov, ordinal);
        this.firePropertyChange(pce);
    }

    @Override
    public String getPersistenceName() {
        return "simple";
    }

    @Override
    public List<String> getUniqColumnNames() {
        List<String> rv = new ArrayList<String>();
        rv.add("name");
        rv.add("weight");
        return rv;
    }

    @Override
    public String getSchemaName() {
        return TEST_SCHEMA;
    }
}
