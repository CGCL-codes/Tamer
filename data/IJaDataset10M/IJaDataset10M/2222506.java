package org.nvframe.entity;

import java.util.ArrayList;
import java.util.List;
import org.nvframe.component.Component;
import org.nvframe.manager.EntityManager;
import org.nvframe.util.settings.SettingsObj;

/**
 * 
 * @author Nik Van Looy
 */
public class EntityImpl implements Entity {

    private String id;

    private String name;

    private int instanceId;

    private Entity owner = null;

    private boolean enabled;

    private ArrayList<Component> components;

    public EntityImpl(String id) {
        this.id = id;
        enabled = true;
        components = new ArrayList<Component>();
    }

    public EntityImpl(String id, String name) {
        this(id);
        this.name = name;
    }

    public EntityImpl(String id, String name, SettingsObj settings) {
        this(id, name);
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public Component getComponent(Class<?> cls) {
        for (Component comp : components) if (cls.isInstance(comp)) return comp;
        return null;
    }

    public Component getComponent(String id) {
        if (components.size() == 0) return null;
        for (Component comp : components) if (comp.getId().equals(id)) return comp;
        return null;
    }

    public boolean hasComponent(Class<?> cls) {
        if (components.size() == 0) return false;
        for (Component comp : components) if (cls.isInstance(comp)) return true;
        return false;
    }

    public int getComponentCount() {
        return components.size();
    }

    @Override
    public void initialize() {
        for (Component comp : components) comp.init();
    }

    public void removed() {
        EntityManager.getInstance().sheduleForDeletion(this);
    }

    public void deleteAllComponents() {
        components.clear();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public int getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(int instanceId) {
        this.instanceId = instanceId;
    }

    @Override
    public List<Component> getComponents() {
        return components;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    @Override
    public Entity getOwner() {
        return owner;
    }

    public Entity getRoot() {
        Entity ent = this;
        while (ent.getOwner() != null) ent = ent.getOwner();
        return ent;
    }

    @Override
    public String toString() {
        return id;
    }

    public void printAllComponents() {
        for (Component component : components) System.out.println(component.getId());
    }

    @Override
    public void init() {
    }

    protected void finalize() {
        System.out.println("GARBAGE: " + id);
    }

    @Override
    public String getName() {
        return name;
    }
}
