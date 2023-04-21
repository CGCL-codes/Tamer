package controller;

import java.util.Enumeration;
import java.util.Vector;
import figures.WBShape;

public class SelectedVector implements Cloneable {

    private Vector vSelected;

    public SelectedVector() {
        vSelected = new Vector();
    }

    public synchronized void add(WBShape s) {
        vSelected.add(s);
    }

    public int size() {
        return vSelected.size();
    }

    public synchronized void remove(WBShape s) {
        vSelected.remove(s);
    }

    public Object clone() {
        SelectedVector sv = new SelectedVector();
        Enumeration e = this.elements();
        while (e.hasMoreElements()) sv.add((WBShape) ((WBShape) e.nextElement()).clone());
        return sv;
    }

    public boolean contains(WBShape s) {
        return vSelected.contains(s);
    }

    public Enumeration elements() {
        return vSelected.elements();
    }

    public synchronized void removeAllElements() {
        vSelected.removeAllElements();
    }
}
