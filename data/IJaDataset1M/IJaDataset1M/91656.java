package de.fzi.injectj.ui.model;

/**
 * 
 * @author <a href="mailto:sebastian.mies@gmx.net">Sebastian Mies</a>
 */
public final class ListObject {

    private IListModel m;

    private Object o;

    public ListObject(IListModel m, Object o) {
        this.m = m;
        this.o = o;
    }

    public final Object get() {
        return o;
    }

    public final String getIconResource() {
        return m.getIconResource(o);
    }

    public final String getVisibleString() {
        return m.getVisibleString(o);
    }

    public final Object getReturnObject() {
        return m.getReturnObject(o);
    }

    public final boolean isEditable() {
        return m.isEditable(o);
    }

    public final boolean setString(String str) {
        return m.setString(o, str);
    }

    public final IListModel getParent() {
        return m.getParent();
    }

    public final IListModel getChildren() {
        return m.getChildren(o);
    }

    public final boolean isLeaf() {
        return m.isLeaf(o);
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof ListObject)) return false;
        ListObject l = (ListObject) obj;
        return l.m == m && l.o.equals(o);
    }

    public int hashCode() {
        return m.hashCode() ^ o.hashCode();
    }

    public final String toString() {
        return getVisibleString();
    }
}
