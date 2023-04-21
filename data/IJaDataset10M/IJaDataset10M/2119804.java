package graphlab.gui.plugins.main.core.actions.undo.undo;

import graphlab.gui.plugins.main.core.actions.undo.UndoableActionOccuredData;
import graphlab.main.core.BlackBoard.BlackBoard;
import graphlab.main.core.Event;
import graphlab.main.core.action.AbstractAction;

public class UndoLogManager extends AbstractAction {

    public static final String name = "Undo Log Manager";

    Event event = UndoableActionOccuredData.event;

    public UndoLogManager(BlackBoard bb) {
        super(bb);
        setEvent(event);
        enable();
        current = lastNode;
    }

    Node current;

    Node lastNode = new Node();

    /**Occurs when the undo log adds by an action*/
    public void performJob(String name) {
        UndoableActionOccuredData uaod = blackboard.get(UndoableActionOccuredData.name);
        Node first = new Node();
        first.val = uaod;
        first.setNext(current);
        this.current = first;
    }

    /**returns the data for the next undo action*/
    public UndoableActionOccuredData getNextUndoData() {
        if (current == lastNode) return null;
        UndoableActionOccuredData val = current.val;
        current = current.next;
        return val;
    }

    /**returns the data for the next undo action*/
    public UndoableActionOccuredData getNextRedoData() {
        if (current.prev == null) return null;
        current = current.prev;
        return current.val;
    }
}

class Node {

    Node next, prev;

    UndoableActionOccuredData val;

    /**sets the next node linked to this node and also updates the previous node of the next to this*/
    public void setNext(Node next) {
        this.next = next;
        if (next != null) next.prev = this;
    }
}
