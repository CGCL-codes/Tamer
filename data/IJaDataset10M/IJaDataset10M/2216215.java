package org.micthemodel.gui.listModels;

import org.micthemodel.factory.Parameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 *
 * @author bishnoi
 */
public class SetModel<E> extends javax.swing.AbstractListModel implements javax.swing.ComboBoxModel, Iterable<E> {

    static final long serialVersionUID = 8382920428325739539L;

    private ArrayList<E> objects;

    private E selectedObject;

    public SetModel() {
        this.objects = new ArrayList<E>();
    }

    @Override
    public int getSize() {
        return this.objects.size();
    }

    @Override
    public E getElementAt(int index) {
        return this.objects.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        try {
            this.selectedObject = (E) anItem;
        } catch (ClassCastException ex) {
            return;
        }
    }

    @Override
    public E getSelectedItem() {
        return this.selectedObject;
    }

    @Override
    public Iterator<E> iterator() {
        return this.objects.iterator();
    }

    public void addAll(Collection<E> c) {
        for (E o : c) {
            this.add(o);
        }
        this.fireContentsChanged(this, 0, this.objects.size());
    }

    public void addAll(E[] c) {
        for (E o : c) {
            this.add(o);
        }
        this.fireContentsChanged(this, 0, this.objects.size());
    }

    public void add(E s) {
        for (E object : this.objects) {
            if (s == object) {
                Parameters.getOut().println(s.toString() + " equals " + object.toString());
                return;
            }
        }
        this.objects.add(s);
        this.fireContentsChanged(this, 0, this.objects.size());
    }

    public void remove(int index) {
        this.objects.remove(index);
        this.fireContentsChanged(this, 0, this.objects.size() - 1);
    }

    public void remove(E object) {
        Iterator<E> onObjects = this.objects.iterator();
        while (onObjects.hasNext()) {
            if (onObjects.equals(object)) {
                onObjects.remove();
            }
        }
        this.fireContentsChanged(this, 0, this.objects.size() - 1);
    }

    public void moveUp(int index) {
        if (index == 0 || index >= this.objects.size()) {
            return;
        }
        E last = this.objects.get(index - 1);
        this.objects.set(index - 1, this.objects.get(index));
        this.objects.set(index, last);
        this.fireContentsChanged(this, index - 1, index);
    }

    public void moveDown(int index) {
        if (index > this.objects.size() - 2) {
            return;
        }
        E last = this.objects.get(index + 1);
        this.objects.set(index + 1, this.objects.get(index));
        this.objects.set(index, last);
        this.fireContentsChanged(this, index, index + 1);
    }

    public void clear() {
        this.objects.clear();
        this.fireContentsChanged(this, 0, this.objects.size());
    }
}
