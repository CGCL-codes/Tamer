package org.xfc.undo;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Stack;
import javax.swing.Action;
import org.xfc.components.XDefaultAction;
import org.xfc.util.XJavaBean;

/**
 *
 *
 * @author Devon Carew
 */
public class XUndoManager implements XJavaBean {

    private Stack undoActions = new Stack();

    private Stack redoActions = new Stack();

    private boolean isUndoing;

    private boolean isRedoing;

    private int disabled;

    private PropertyChangeSupport eventSupport = new PropertyChangeSupport(this);

    private Action undoAction = new XDefaultAction("edit-undo") {

        {
            XUndoManager.this.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent event) {
                    setEnabled(canUndo());
                }
            });
        }

        public void actionPerformed(ActionEvent arg0) {
            undo();
        }
    };

    private Action redoAction = new XDefaultAction("edit-redo") {

        {
            XUndoManager.this.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent event) {
                    setEnabled(canRedo());
                }
            });
        }

        public void actionPerformed(ActionEvent arg0) {
            redo();
        }
    };

    /**
	 * 
	 */
    public XUndoManager() {
    }

    /**
	 * @param action
	 */
    public void addAction(XUndoableAction action) {
        if (!isUndoRegistrationEnabled()) return;
        undoActions.push(action);
        redoActions.clear();
        fireEvents();
    }

    /**
	 * 
	 */
    public void clearAllActions() {
        undoActions.clear();
        redoActions.clear();
        fireEvents();
    }

    /**
	 * @return Returns the isRedoing.
	 */
    public boolean isRedoing() {
        return isRedoing;
    }

    /**
	 * @return Returns the isUndoing.
	 */
    public boolean isUndoing() {
        return isUndoing;
    }

    /**
	 * 
	 */
    public void undo() {
        if (undoActions.size() == 0) return;
        isUndoing = true;
        try {
            XUndoableAction action = (XUndoableAction) undoActions.peek();
            action.undo();
            undoActions.pop();
            redoActions.push(action);
        } finally {
            isUndoing = false;
        }
        fireEvents();
    }

    /**
	 * 
	 */
    public void redo() {
        if (redoActions.size() == 0) return;
        isRedoing = true;
        try {
            XUndoableAction action = (XUndoableAction) redoActions.peek();
            action.redo();
            redoActions.pop();
            undoActions.push(action);
        } finally {
            isRedoing = false;
        }
        fireEvents();
    }

    private void fireEvents() {
        firePropertyChange("canUndo", !canUndo(), canUndo());
        firePropertyChange("canRedo", !canRedo(), canRedo());
    }

    /**
	 * @return whether there are any actions to undo
	 */
    public boolean canUndo() {
        return undoActions.size() > 0;
    }

    /**
	 * @return whether there are any actions to redo
	 */
    public boolean canRedo() {
        return redoActions.size() > 0;
    }

    /**
	 * @param enabled
	 */
    public void setUndoRegistrationEnabled(boolean enabled) {
        boolean oldValue = isUndoRegistrationEnabled();
        disabled += (enabled ? -1 : 1);
        if (oldValue != isUndoRegistrationEnabled()) firePropertyChange("undoRegistrationEnabled", oldValue, isUndoRegistrationEnabled());
    }

    /**
	 * @return whether registration of undo actions is enabled
	 */
    public boolean isUndoRegistrationEnabled() {
        return disabled == 0;
    }

    /**
	 * Returns an Action which will invoke undo on the XUndoManager. The action will be
	 * enabled / disabled as undo is available on this manager.
	 * 
	 * @return an Action which will invoke undo on the XUndoManager
	 */
    public Action getUndoAction() {
        return undoAction;
    }

    /**
	 * Returns an Action which will invoke redo on the XUndoManager. The action will be
	 * enabled / disabled as redo is available on this manager.
	 * 
	 * @return an Action which will invoke redo on the XUndoManager
	 */
    public Action getRedoAction() {
        return redoAction;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        eventSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        eventSupport.removePropertyChangeListener(listener);
    }

    /**
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
    protected void firePropertyChange(String property, boolean oldValue, boolean newValue) {
        firePropertyChange(property, Boolean.valueOf(oldValue), Boolean.valueOf(newValue));
    }

    /**
	 * @param property
	 * @param oldValue
	 * @param newValue
	 */
    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        eventSupport.firePropertyChange(property, oldValue, newValue);
    }
}
