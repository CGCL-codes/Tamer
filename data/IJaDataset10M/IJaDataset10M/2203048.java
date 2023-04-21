package locusts.server.admin;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import locusts.common.entities.Entity;
import locusts.common.entities.EntityList;
import locusts.common.entities.EntityType;

/**
 * A  combo box model for handling the "type" property of an entity. It
 * allows the user to select a type from a list, rather than type in the id.
 *
 * @author Hamish Morgan
 */
public class EntityComboBoxModel implements ComboBoxModel {

    private final EntityList allEntities;

    private final EntityType type;

    private List<Entity> entities;

    private Entity selected;

    public EntityComboBoxModel(EntityList allEntities, EntityType type) {
        this.allEntities = allEntities;
        this.allEntities.addListDataListener(new ListDataHandler());
        this.type = type;
        entities = new ArrayList<Entity>();
        updateEntities();
    }

    private void updateEntities() {
        List<Entity> newEntities = allEntities.getAllByType(type);
        newEntities.add(null);
        boolean changed = newEntities.equals(entities);
        entities = newEntities;
        if (changed) {
            if (selected != null && !entities.contains(selected)) selected = null;
            if (selected == null && !entities.isEmpty()) selected = entities.get(0);
            fireContentsChanged(0, entities.size() - 1);
        }
    }

    private class ListDataHandler implements ListDataListener {

        public void intervalAdded(ListDataEvent e) {
            updateEntities();
        }

        public void intervalRemoved(ListDataEvent e) {
            updateEntities();
        }

        public void contentsChanged(ListDataEvent e) {
            updateEntities();
        }
    }

    public void setSelectedItem(Object o) {
        if (o instanceof Entity && entities.contains((Entity) o)) selected = (Entity) o; else {
            selected = null;
        }
    }

    public Object getSelectedItem() {
        return selected;
    }

    public int getSize() {
        return entities.size();
    }

    public Object getElementAt(int i) {
        return entities.get(i);
    }

    private final transient EventListenerList listeners = new EventListenerList();

    protected void fireIntervalAdded(int index0, int index1) {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);
        for (ListDataListener l : listeners.getListeners(ListDataListener.class)) l.intervalAdded(e);
    }

    protected void fireIntervalRemoved(int index0, int index1) {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);
        for (ListDataListener l : listeners.getListeners(ListDataListener.class)) l.intervalRemoved(e);
    }

    protected void fireContentsChanged(int index0, int index1) {
        final ListDataEvent e = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
        for (ListDataListener l : listeners.getListeners(ListDataListener.class)) l.contentsChanged(e);
    }

    public void addListDataListener(ListDataListener listener) {
        listeners.add(ListDataListener.class, listener);
    }

    public void removeListDataListener(ListDataListener listener) {
        listeners.remove(ListDataListener.class, listener);
    }
}
