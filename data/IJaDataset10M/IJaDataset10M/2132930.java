package org.nakedobjects.object.defaults.collection;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.Persistable;
import org.nakedobjects.object.TypedNakedCollection;
import org.nakedobjects.object.defaults.AbstractNakedReference;
import org.nakedobjects.object.defaults.InternalNakedObject;
import org.nakedobjects.object.persistence.Oid;
import org.nakedobjects.utility.Assert;
import java.util.Enumeration;
import java.util.Vector;

public class InstanceCollectionVector extends AbstractNakedReference implements TypedNakedCollection, InternalNakedObject {

    private String name;

    private Vector instances;

    private NakedObjectSpecification instanceSpecification;

    public InstanceCollectionVector(NakedObjectSpecification elementSpecification, NakedObject[] instances) {
        this.instanceSpecification = elementSpecification;
        name = elementSpecification.getPluralName();
        int size = instances.length;
        this.instances = new Vector(size);
        for (int i = 0; i < size; i++) {
            this.instances.addElement(instances[i]);
        }
    }

    public NakedObject elementAt(int i) {
        if (i < 0 || i >= size()) {
            throw new IllegalArgumentException("No such element: " + i);
        }
        return (NakedObject) instances.elementAt(i);
    }

    public String titleString() {
        return name + ", " + size();
    }

    public int size() {
        return instances.size();
    }

    public void setOid(Oid oid) {
    }

    public NakedObjectSpecification getElementSpecification() {
        return instanceSpecification;
    }

    public boolean contains(NakedObject object) {
        return false;
    }

    public void destroyed() {
    }

    public Enumeration elements() {
        return instances.elements();
    }

    public String getIconName() {
        return null;
    }

    public Object getObject() {
        return instances;
    }

    public void init(Object[] initElements) {
        Assert.assertEquals("Collection not empty", 0, this.instances.size());
        for (int i = 0; i < initElements.length; i++) {
            instances.addElement(initElements[i]);
        }
    }

    public void sort() {
        Vector sorted = new Vector(instances.size());
        outer: for (Enumeration e = instances.elements(); e.hasMoreElements(); ) {
            NakedObject element = (NakedObject) e.nextElement();
            String title = element.titleString();
            int i = 0;
            for (Enumeration f = sorted.elements(); f.hasMoreElements(); ) {
                NakedObject sortedElement = (NakedObject) f.nextElement();
                String sortedTitle = sortedElement.titleString();
                if (sortedTitle.compareTo(title) > 0) {
                    sorted.insertElementAt(element, i);
                    continue outer;
                }
                i++;
            }
            sorted.addElement(element);
        }
        instances = sorted;
    }

    public Persistable persistable() {
        return Persistable.TRANSIENT;
    }
}
