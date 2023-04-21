package es.ulpgc.dis.heuristicide.rcp.ui.details;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import es.ulpgc.dis.heuriskein.model.solver.Heuristic;
import es.ulpgc.dis.heuriskein.model.solver.Operator;
import es.ulpgc.dis.heuriskein.model.solver.Selector;

public class HeuristicContentProvider implements ITreeContentProvider {

    public Object[] getChildren(Object item) {
        if (item instanceof Heuristic) {
            return new Object[] { ((Heuristic) item).getOperator(), ((Heuristic) item).getSelector() };
        }
        PropertyDescriptor descriptor[] = null;
        try {
            if (item instanceof Selector) {
                descriptor = java.beans.Introspector.getBeanInfo(item.getClass(), Selector.class).getPropertyDescriptors();
            }
            if (item instanceof Operator) {
                descriptor = java.beans.Introspector.getBeanInfo(item.getClass(), Selector.class).getPropertyDescriptors();
            }
            ArrayList<Property> properties = new ArrayList<Property>();
            for (PropertyDescriptor p : descriptor) {
                Property property = new Property();
                property.instance = item;
                property.reader = p.getReadMethod();
                property.name = p.getName();
            }
            return properties.toArray();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return new Object[0];
    }

    public Object getParent(Object arg0) {
        return null;
    }

    public boolean hasChildren(Object arg0) {
        if (getChildren(arg0).length > 0) {
            return true;
        }
        return false;
    }

    public Object[] getElements(Object arg0) {
        return null;
    }

    public void dispose() {
    }

    public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
    }
}
