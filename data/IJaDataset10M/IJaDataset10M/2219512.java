package ch.bbv.dynamicproperties.objecteditor;

import java.util.HashSet;
import java.util.Set;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import ch.bbv.dynamicproperties.PropertyInstanceManager;
import ch.bbv.dynamicproperties.core.DynamicProperty;
import ch.bbv.dynamicproperties.core.Property;

public class DataObjectTreeModel implements TreeModel {

    private PropertyInstanceManager propertyInstanceManager;

    private Set<TreeModelListener> treeModelListeners;

    public DataObjectTreeModel(PropertyInstanceManager propertyInstanceManager) {
        this.propertyInstanceManager = propertyInstanceManager;
        this.treeModelListeners = new HashSet<TreeModelListener>();
    }

    public void addTreeModelListener(TreeModelListener listener) {
        treeModelListeners.add(listener);
    }

    public Object getChild(Object dataObject, int index) {
        if (dataObject instanceof DynamicProperty) {
            return propertyInstanceManager.getProperties(dataObject.getClass().asSubclass(DynamicProperty.class)).get(index);
        } else if (dataObject instanceof RootElement) {
            return ((RootElement) dataObject).getProperty(index);
        }
        return null;
    }

    public int getChildCount(Object dataObject) {
        if (dataObject instanceof DynamicProperty) {
            return propertyInstanceManager.getProperties(dataObject.getClass().asSubclass(DynamicProperty.class)).size();
        } else if (dataObject instanceof RootElement) {
            return ((RootElement) dataObject).size();
        }
        return 0;
    }

    public int getIndexOfChild(Object dataObject, Object property) {
        int i = 0;
        for (Property tmpProperty : propertyInstanceManager.getProperties(dataObject.getClass().asSubclass(DynamicProperty.class))) {
            if (tmpProperty == property) {
                return i;
            }
        }
        return -1;
    }

    public Object getRoot() {
        return new RootElement();
    }

    public boolean isLeaf(Object dynamicProperty) {
        return dynamicProperty instanceof Property;
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        treeModelListeners.remove(listener);
    }

    public void valueForPathChanged(TreePath arg0, Object arg1) {
    }

    private class RootElement {

        public int size() {
            return DataObjectTreeModel.this.propertyInstanceManager.getDataObjects().size();
        }

        public Class<? extends DynamicProperty> getProperty(int index) {
            return DataObjectTreeModel.this.propertyInstanceManager.getDataObjects().get(index);
        }

        public String toString() {
            return "root";
        }
    }
}
