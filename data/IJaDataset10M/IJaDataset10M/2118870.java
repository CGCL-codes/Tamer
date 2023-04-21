package com.openbravo.data.user;

import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.beans.*;

/**
 *
 * @author  adrian
 */
public class DirtyManager implements DocumentListener, ChangeListener, ActionListener, PropertyChangeListener {

    private boolean m_bDirty;

    protected Vector listeners = new Vector();

    /** Creates a new instance of DirtyManager */
    public DirtyManager() {
        m_bDirty = false;
    }

    public void addDirtyListener(DirtyListener l) {
        listeners.add(l);
    }

    public void removeDirtyListener(DirtyListener l) {
        listeners.remove(l);
    }

    protected void fireChangedDirty() {
        Enumeration e = listeners.elements();
        while (e.hasMoreElements()) {
            DirtyListener l = (DirtyListener) e.nextElement();
            l.changedDirty(m_bDirty);
        }
    }

    public void setDirty(boolean bValue) {
        if (m_bDirty != bValue) {
            m_bDirty = bValue;
            fireChangedDirty();
        }
    }

    public boolean isDirty() {
        return m_bDirty;
    }

    public void changedUpdate(DocumentEvent e) {
        setDirty(true);
    }

    public void insertUpdate(DocumentEvent e) {
        setDirty(true);
    }

    public void removeUpdate(DocumentEvent e) {
        setDirty(true);
    }

    public void stateChanged(ChangeEvent e) {
        setDirty(true);
    }

    public void actionPerformed(ActionEvent e) {
        setDirty(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        setDirty(true);
    }
}
