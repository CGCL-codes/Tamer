package com.hp.hpl.guess.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class GActionManager {

    /**
	 * The action elements which can be undone 
	 */
    private static Stack<GAction> undoStack = new Stack<GAction>();

    /**
	 * The action elements which where already undone, but can
	 * be redone
	 */
    private static Stack<GAction> redoStack = new Stack<GAction>();

    /**
	 * The maximum count of actions that can be undone
	 */
    public static final int MAX_UNDO_ACTIONS = 4;

    /**
	 * The IDs to send to the Action Listeners in the
	 * Action Event
	 */
    public static final int EVENT_ID_ACTION_ADDED = 0;

    public static final int EVENT_ID_ACTION_REDONE = 1;

    public static final int EVENT_ID_ACTION_UNDONE = 2;

    /**
	 * Set of listeners to notify when the undo or redo
	 * stack changes
	 */
    private static Set<ActionListener> actionListeners = new HashSet<ActionListener>();

    /**
	 * Add an action to the undo list
	 * @param aAction
	 */
    private static void addUndoAction(GAction aAction) {
        undoStack.push(aAction);
        notifyActionListener(aAction, EVENT_ID_ACTION_ADDED);
    }

    /**
	 * Add an action to the redo list
	 * @param aAction
	 */
    private static void addRedoAction(GAction aAction) {
        redoStack.push(aAction);
        notifyActionListener(aAction, EVENT_ID_ACTION_ADDED);
    }

    /**
	 * Run the action and add it to the undo list
	 * @param aAction
	 */
    public static void runAction(GAction aAction) {
        runAction(aAction, null);
    }

    /**
	 * Run the action and add a description and add 
	 * it to the undo list
	 * @param aAction
	 * @param aDescription
	 */
    public static void runAction(GAction aAction, String aDescription) {
        if (aDescription != null) {
            aAction.setDescription(aDescription);
        }
        aAction.run();
        addUndoAction(aAction);
        redoStack.clear();
        disposeOldActions();
    }

    /**
	 * Undo the last action
	 */
    public static void undo() {
        GAction lastItem = undoStack.pop();
        GAction redoItem = lastItem.getUndoAction().getUndoAction();
        addRedoAction(redoItem);
        lastItem.getUndoAction().run();
        notifyActionListener(lastItem, EVENT_ID_ACTION_UNDONE);
    }

    /**
	 * Redo the last undone action
	 */
    public static void redo() {
        GAction lastItem = redoStack.pop();
        GAction undoItem = lastItem.getUndoAction();
        addUndoAction(undoItem);
        lastItem.run();
        notifyActionListener(lastItem, EVENT_ID_ACTION_REDONE);
    }

    /**
	 * Delete actions older than MAX_UNDO_ACTIONS
	 */
    private static void disposeOldActions() {
        if (undoStack.size() > MAX_UNDO_ACTIONS) {
            GAction oldestAction = undoStack.get(0);
            oldestAction.dispose();
            undoStack.removeElementAt(0);
        }
    }

    public static void addActionListener(ActionListener changeListener) {
        actionListeners.add(changeListener);
    }

    private static void notifyActionListener(Object source, int eventID) {
        ActionEvent changeEvent = new ActionEvent(source, eventID, null);
        Iterator<ActionListener> listenerIterator = actionListeners.iterator();
        while (listenerIterator.hasNext()) {
            listenerIterator.next().actionPerformed(changeEvent);
        }
    }

    /**
	 * Returns the description of the last action for undo, or
	 * null if no such action exists
	 * @return
	 */
    public static GAction getLastUndoAction() {
        if (undoStack.size() == 0) {
            return null;
        } else {
            return undoStack.peek();
        }
    }

    /**
	 * Returns the description of the last action for redo, or
	 * null if no such action exists
	 * @return
	 */
    public static GAction getLastRedoAction() {
        if (redoStack.size() == 0) {
            return null;
        } else {
            return redoStack.peek();
        }
    }
}
