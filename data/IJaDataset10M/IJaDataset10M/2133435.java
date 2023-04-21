package net.sf.metarbe.eclipse.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.swt.graphics.Image;

public class ModelElement {

    public static final String PROPERTY_NAME = "name";

    private String name;

    private List<ModelElement> children = new LinkedList<ModelElement>();

    private ModelElement parent;

    private List<ModelElementListener> listeners = new LinkedList<ModelElementListener>();

    public ModelElement(String name) {
        super();
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
        fireUpdatedEvent(PROPERTY_NAME);
    }

    public String getName() {
        return name;
    }

    public Object[] getChildren() {
        return children.toArray();
    }

    public void setParent(ModelElement parent) {
        this.parent = parent;
    }

    public ModelElement getParent() {
        return parent;
    }

    public void addChild(ModelElement aModelElement) {
        synchronized (children) {
            children.add(aModelElement);
        }
        aModelElement.setParent(this);
    }

    public void removeChild(ModelElement aModelElement) {
        aModelElement.setParent(null);
        synchronized (children) {
            children.remove(aModelElement);
        }
    }

    public void removeAllChildren() {
        for (Iterator childrenIt = children.iterator(); childrenIt.hasNext(); ) {
            ModelElement el = (ModelElement) childrenIt.next();
            el.setParent(null);
        }
        children.clear();
    }

    public Image getIcon() {
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

    public void addModelElementListener(ModelElementListener l) {
        listeners.add(l);
    }

    public void removeModelElementListener(ModelElementListener l) {
        listeners.remove(l);
    }

    protected void fireUpdatedEvent(String aProperty) {
        for (Iterator iter = listeners.iterator(); iter.hasNext(); ) {
            ModelElementListener l = (ModelElementListener) iter.next();
            l.elementUpdated(this, aProperty);
        }
    }
}
