package org.vikamine.swing.util.docking;

/**
 * @author vogele
 */
public interface DockModel {

    void addDockListener(DockDataListener lissi);

    void removeDockListener(DockDataListener lissi);

    DockedComponent getElementAt(int index);

    int getSize();

    void add(DockedComponent obj);

    void remove(DockedComponent obj);

    boolean contains(Object dock);
}
