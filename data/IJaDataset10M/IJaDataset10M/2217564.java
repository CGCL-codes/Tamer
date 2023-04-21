package org.columba.addressbook.gui.focus;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.columba.core.gui.action.AbstractColumbaAction;

/**
 * 
 * Every {@link FocusOwner}should register at the <code>FocusManager</code>.
 * <p>
 * FocusManager enables and disables the following Actions:
 * <ul>
 * <li>CutAction</li>
 * <li>CopyAction</li>
 * <li>PasteAction</li>
 * <li>DeleteAction</li>
 * <li>SelectAllAction</li>
 * </ul>
 * 
 * 
 * @author fdietz
 *  
 */
public class FocusManager implements FocusListener {

    /**
	 * list of focus owners
	 */
    List list;

    /**
	 * map associating focus listener ui with focus owner
	 */
    Map map;

    /**
	 * all actions
	 */
    AbstractColumbaAction cutAction;

    AbstractColumbaAction copyAction;

    AbstractColumbaAction pasteAction;

    AbstractColumbaAction deleteAction;

    AbstractColumbaAction selectAllAction;

    AbstractColumbaAction undoAction;

    AbstractColumbaAction redoAction;

    /**
	 * current focus owner
	 */
    FocusOwner current = null;

    /**
	 * last available focus owner
	 */
    FocusOwner last = null;

    private static FocusManager instance = new FocusManager();

    public FocusManager() {
        list = new Vector();
        map = new HashMap();
    }

    public static FocusManager getInstance() {
        return instance;
    }

    /**
	 * register FocusOwner and add FocusListener
	 * 
	 * @param c
	 *            focus owner
	 */
    public void registerComponent(FocusOwner c) {
        list.add(c);
        map.put(c.getComponent(), c);
        c.getComponent().addFocusListener(this);
    }

    /**
	 * Get current focus owner
	 * 
	 * Try first current owner. If this fails, try the last available one.
	 * 
	 * @return current focus owner
	 */
    public FocusOwner getCurrentOwner() {
        if (current != null) {
            return current;
        }
        if (last != null) {
            return last;
        }
        return null;
    }

    /**
	 * 
	 * FocusOwner objects should call this method on selection changes in their
	 * view component to enable/disable the actions
	 *  
	 */
    public void updateActions() {
    }

    /**
	 * enable/disable actions
	 * 
	 * @param o
	 *            current focus owner
	 */
    protected void enableActions(FocusOwner o) {
        if (o == null) {
            cutAction.setEnabled(false);
            copyAction.setEnabled(false);
            pasteAction.setEnabled(false);
            deleteAction.setEnabled(false);
            undoAction.setEnabled(false);
            redoAction.setEnabled(false);
            selectAllAction.setEnabled(false);
            return;
        }
        cutAction.setEnabled(o.isCutActionEnabled());
        copyAction.setEnabled(o.isCopyActionEnabled());
        pasteAction.setEnabled(o.isPasteActionEnabled());
        deleteAction.setEnabled(o.isDeleteActionEnabled());
        undoAction.setEnabled(o.isUndoActionEnabled());
        redoAction.setEnabled(o.isRedoActionEnabled());
        selectAllAction.setEnabled(o.isSelectAllActionEnabled());
    }

    /**
	 * Component gained focus
	 *  
	 */
    public void focusGained(FocusEvent event) {
        current = (FocusOwner) map.get(event.getSource());
        updateActions();
    }

    /**
	 * Component lost focus
	 */
    public void focusLost(FocusEvent event) {
        last = current;
        current = null;
        updateActions();
    }

    /**
	 * execute cut action of currently available focus owner
	 *  
	 */
    public void cut() {
        getCurrentOwner().cut();
        enableActions(getCurrentOwner());
    }

    /**
	 * execute copy action of currently available focus owner
	 *  
	 */
    public void copy() {
        getCurrentOwner().copy();
        enableActions(getCurrentOwner());
    }

    /**
	 * execute paste action of currently available focus owner
	 *  
	 */
    public void paste() {
        getCurrentOwner().paste();
        enableActions(getCurrentOwner());
    }

    /**
	 * execute delete action of currently available focus owner
	 *  
	 */
    public void delete() {
        getCurrentOwner().delete();
        enableActions(getCurrentOwner());
    }

    /**
	 * execute redo action of currentyl available focus owner
	 *  
	 */
    public void redo() {
        getCurrentOwner().redo();
        enableActions(getCurrentOwner());
    }

    /**
	 * execute undo action of currentyl available focus owner
	 *  
	 */
    public void undo() {
        getCurrentOwner().undo();
        enableActions(getCurrentOwner());
    }

    /**
	 * execute selectAll action of currentyl available focus owner
	 *  
	 */
    public void selectAll() {
        getCurrentOwner().selectAll();
        enableActions(getCurrentOwner());
    }

    /**
	 * @param action
	 */
    public void setCopyAction(AbstractColumbaAction action) {
        copyAction = action;
    }

    /**
	 * @param action
	 */
    public void setCutAction(AbstractColumbaAction action) {
        cutAction = action;
    }

    /**
	 * @param action
	 */
    public void setDeleteAction(AbstractColumbaAction action) {
        deleteAction = action;
    }

    /**
	 * @param action
	 */
    public void setPasteAction(AbstractColumbaAction action) {
        pasteAction = action;
    }

    /**
	 * @param action
	 */
    public void setRedoAction(AbstractColumbaAction action) {
        redoAction = action;
    }

    /**
	 * @param action
	 */
    public void setSelectAllAction(AbstractColumbaAction action) {
        selectAllAction = action;
    }

    /**
	 * @param action
	 */
    public void setUndoAction(AbstractColumbaAction action) {
        undoAction = action;
    }
}
