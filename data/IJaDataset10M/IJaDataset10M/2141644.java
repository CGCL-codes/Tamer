package org.jtools.internal.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Collection;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.jpattern.logging.Level;
import org.jtools.util.SimpleText;
import org.jtools.util.Text;

public class SimpleItemSet extends AbstractBeanSupport implements ItemSet, PropertyChangeListener, VetoableChangeListener {

    private static final class KeyFinderEntry {

        Item.KeyFinder keyFinder = null;

        int requests = 0;

        KeyFinderEntry(Item.KeyFinder keyFinder) {
            this.keyFinder = keyFinder;
        }
    }

    private final Text comment;

    private final TreeMap<Object, Item> items;

    private final String key;

    private final IdentityHashMap<Entity, KeyFinderEntry> keyFinders = new IdentityHashMap<Entity, KeyFinderEntry>();

    private final String label;

    public SimpleItemSet(Item parent, String key, String label, String comment, Comparator<Object> comparator) {
        this(parent, key, label, new SimpleText(comment), comparator);
    }

    public SimpleItemSet(Item parent, String key, String label, Text comment, Comparator<Object> comparator) {
        super(parent, null);
        if (key == null) throw new NullPointerException("key");
        this.key = key;
        this.label = ((label == null) ? key : label);
        this.comment = ((comment == null) ? new SimpleText() : comment);
        this.items = new TreeMap<Object, Item>(comparator);
        parent.addItemSet(this);
    }

    public synchronized void addEntity(Entity entity, Item.KeyFinder keyFinder) {
        if (entity == null) throw new NullPointerException("entity");
        if (keyFinder == null) throw new NullPointerException("keyFinder");
        KeyFinderEntry entry = keyFinders.get(entity);
        if (entry == null) {
            entry = new KeyFinderEntry(keyFinder);
            keyFinders.put(entity, entry);
        }
        if (!entry.keyFinder.equals(keyFinder)) {
            throw new RuntimeException("entity " + entity + " already registered.");
        }
        entry.requests++;
        if (entry.requests > 1) {
            return;
        }
        String myName = "." + key;
        if ((getParent() != null) && (getParent().getEntity() != null)) myName = getParent().getEntity().getKey() + myName;
        getLogger().logp(Level.DEBUG, getClass(), "addEntity", null, "{0}: keyFinder for Entity {1} [{2}] installed. [{3}]", myName, entity.getKey(), entity, this);
        entity.addVetoableChangeListener(this);
        entity.addPropertyChangeListener(this);
    }

    public boolean containsKey(Object key) {
        return items.containsKey(key);
    }

    public Object firstKey() {
        return items.firstKey();
    }

    public Item get(Object key) {
        return items.get(key);
    }

    public Text getComment() {
        return comment;
    }

    public String getKey() {
        return key;
    }

    protected Item.KeyFinder getKeyFinder(Entity e) {
        KeyFinderEntry entry = keyFinders.get(e);
        if (entry == null) return null;
        return entry.keyFinder;
    }

    public String getLabel() {
        return label;
    }

    public Model getModel() {
        return getParent().getModel();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public Set<Object> keySet() {
        return items.keySet();
    }

    public Object lastKey() {
        return items.lastKey();
    }

    protected synchronized void propertyChange(PropertyChangeEvent evt, Item item) {
        Item.KeyFinder keyFinder = getKeyFinder(item.getEntity());
        if (keyFinder == null) {
            return;
        }
        Object oldKey = keyFinder.getKey(item, evt.getPropertyName(), evt.getOldValue());
        Object newKey = keyFinder.getKey(item, evt.getPropertyName(), evt.getNewValue());
        if ((oldKey == null) && (newKey == null)) return;
        if ((oldKey != null) && (newKey != null) && oldKey.equals(newKey)) return;
        if (oldKey != null) {
            items.remove(oldKey);
        }
        if (newKey != null) {
            if (items.put(newKey, item) != null) throw new RuntimeException("ERROR: newKey already exists. sequence?");
            getLogger().logp(Level.DEBUG, getClass(), "propertyChange", null, "{0} in {1}: item {2} added. [{3}]", key, getParent(), newKey, String.valueOf(size()));
        }
        if (changes != null) changes.firePropertyChange("items", oldKey, newKey);
    }

    protected synchronized void propertyChange_items(PropertyChangeEvent evt, Entity entity, Item oldValue, Item newValue) {
        Item.KeyFinder keyFinder = getKeyFinder(entity);
        if (keyFinder == null) {
            throw new NullPointerException("no keyfinder for entity " + entity);
        }
        Item item = oldValue;
        if (item != null) {
            item.removePropertyChangeListener(this);
            item.removeVetoableChangeListener(this);
            Object key = keyFinder.getKey(item);
            if (key != null) {
                items.remove(key);
                if (changes != null) changes.firePropertyChange("items", key, null);
            }
        }
        item = newValue;
        if (item != null) {
            item.addPropertyChangeListener(this);
            item.addVetoableChangeListener(this);
            Object key = keyFinder.getKey(item);
            if (key != null) {
                items.put(key, item);
                getLogger().logp(Level.DEBUG, getClass(), "propertyChange_items", null, "{0} in {1}: property='{2}'; key={3}; {4}, size={5}", getKey(), getParent(), evt.getPropertyName(), key, key.getClass(), String.valueOf(size()));
                if (changes != null) changes.firePropertyChange("items", null, key);
            }
        }
    }

    private void removeAll(Entity entity) throws PropertyVetoException {
        Map.Entry<Object, Item>[] all = items.entrySet().toArray(new Map.Entry[items.size()]);
        if (vetos != null) {
            for (int i = 0; i < all.length; i++) {
                if (all[i].getValue().getEntity() == entity) vetos.fireVetoableChange("items", all[i].getKey(), null);
            }
        }
        for (int i = 0; i < all.length; i++) {
            if (all[i].getValue().getEntity() == entity) {
                Object oldKey = all[i].getKey();
                items.remove(oldKey);
                if (changes != null) changes.firePropertyChange("items", oldKey, null);
            }
        }
    }

    public synchronized void removeEntity(Entity entity) throws PropertyVetoException {
        if (entity == null) throw new NullPointerException("entity");
        KeyFinderEntry entry = keyFinders.get(entity);
        if (entry == null) {
            return;
        }
        if (entry.requests > 1) {
            entry.requests--;
            return;
        }
        keyFinders.remove(entity);
        entity.removeVetoableChangeListener(this);
        entity.removePropertyChangeListener(this);
        try {
            removeAll(entity);
        } catch (PropertyVetoException pvex) {
            keyFinders.put(entity, entry);
            entity.addVetoableChangeListener(this);
            entity.addPropertyChangeListener(this);
            throw pvex;
        }
    }

    public int size() {
        return items.size();
    }

    public String toString() {
        return label;
    }

    public Collection<Item> values() {
        return items.values();
    }

    protected synchronized void vetoableChange(PropertyChangeEvent evt, Item item) throws PropertyVetoException {
        Item.KeyFinder keyFinder = getKeyFinder(item.getEntity());
        if (keyFinder == null) {
            return;
        }
        Object oldKey = keyFinder.getKey(item, evt.getPropertyName(), evt.getOldValue());
        Object newKey = keyFinder.getKey(item, evt.getPropertyName(), evt.getNewValue());
        if ((oldKey == null) && (newKey == null)) return;
        if ((oldKey != null) && oldKey.equals(newKey)) return;
        if ((newKey != null) && items.containsKey(newKey)) throw new java.beans.PropertyVetoException("an item with key '" + newKey + "' already exists.", evt);
        if (vetos != null) vetos.fireVetoableChange("items", oldKey, newKey);
    }

    protected synchronized void vetoableChange_items(PropertyChangeEvent evt, Entity entity, Item oldValue, Item newValue) throws PropertyVetoException {
        Item.KeyFinder keyFinder = getKeyFinder((Entity) evt.getSource());
        Item item = oldValue;
        if (item != null) {
            Object key = keyFinder.getKey(item);
            if (key != null) {
                if (vetos != null) vetos.fireVetoableChange("items", key, null);
            }
        }
        item = newValue;
        if (item != null) {
            Object key = keyFinder.getKey(item);
            if (key != null) {
                if (items.containsKey(key)) throw new PropertyVetoException("an item with key '" + key + "' already exists.", evt);
                if (vetos != null) vetos.fireVetoableChange("items", null, key);
            }
        }
    }
}
