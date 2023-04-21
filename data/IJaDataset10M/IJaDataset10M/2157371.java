package com.organic.maynard.outliner.util.undo;

import com.organic.maynard.outliner.*;
import java.util.*;
import java.awt.*;

/**
 * @author  $Author: maynardd $
 * @version $Revision: 1.3 $, $Date: 2002/08/27 09:42:13 $
 */
public class PrimitiveUndoableEditableChange extends AbstractUndoable implements Undoable, PrimitiveUndoablePropertyChange {

    private Node node = null;

    private int oldState = 0;

    private int newState = 0;

    public PrimitiveUndoableEditableChange(Node node, int oldState, int newState) {
        this.node = node;
        this.oldState = oldState;
        this.newState = newState;
    }

    public void destroy() {
        node = null;
    }

    public Node getNode() {
        return node;
    }

    public void undo() {
        node.setEditableState(oldState);
    }

    public void redo() {
        node.setEditableState(newState);
    }
}
