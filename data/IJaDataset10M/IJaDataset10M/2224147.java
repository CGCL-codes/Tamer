package org.merlotxml.merlot;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;

public class MerlotUndoableEdit implements UndoableEdit, MerlotConstants {

    public static final int DELETE = 1;

    public static final int INSERT = 2;

    public static final int MOVE = 3;

    /**
     * For deletes this is the only node,
     * For moves, this is the node being moved. 
     * For inserts this is the node being inserted.
     */
    protected MerlotDOMNode _node;

    /**
     * this is basically a TreePath with child indices instead of object refs of where the
     * node used to be before being deleted or moved. We don't care about location when
     * undoing an insert. We just erase it.
     */
    protected int[] _nodeLocation;

    protected DOMTreeTableAdapter _tableModel;

    /**
     * Presentation name
     */
    protected String _presName;

    protected int _action;

    public MerlotUndoableEdit(String pname, int action, DOMTreeTableAdapter model, MerlotDOMNode nd, int[] where) {
        _presName = pname;
        _action = action;
        _node = nd;
        _nodeLocation = where;
        _tableModel = model;
    }

    /**
     * Undo the edit that was made.
     */
    public void undo() throws CannotUndoException {
        MerlotDebug.msg("Undo: " + toString());
        switch(_action) {
            case DELETE:
                undoDelete();
                break;
            case INSERT:
                undoInsert();
                break;
            case MOVE:
                undoMove();
                break;
        }
    }

    protected void undoDelete() throws CannotUndoException {
        MerlotDOMNode parent = getParentFromLocationPath();
        int index = _nodeLocation[_nodeLocation.length - 1];
        MerlotDebug.msg("undoDelete: parent = " + parent + " index = " + index);
        parent.insertChildAt(_node, index);
    }

    protected void undoInsert() throws CannotUndoException {
        MerlotDebug.msg("undoInsert:  node =" + _node);
        _node.delete();
    }

    protected void undoMove() throws CannotUndoException {
        MerlotDebug.msg("undo Move: not implemented");
        throw new CannotUndoException();
    }

    /**
     * This gets the parent node from the location path
     */
    protected MerlotDOMNode getParentFromLocationPath() throws CannotUndoException {
        MerlotDOMNode root = (MerlotDOMNode) _tableModel.getRoot();
        int len = _nodeLocation.length - 1;
        MerlotDOMNode nd = root;
        MerlotDOMNode[] children;
        for (int i = 1; i < len; i++) {
            children = nd.getChildNodes();
            if (children.length > _nodeLocation[i]) {
                nd = children[_nodeLocation[i]];
            } else {
                MerlotDebug.msg("can't undo: node location problem");
                throw new CannotUndoException();
            }
            MerlotDebug.msg(" getParentFromLocationPath: loc[" + i + "] = " + _nodeLocation[i] + "  node = " + nd);
        }
        return nd;
    }

    /**
     * True if it is still possible to undo this operation
     */
    public boolean canUndo() {
        return true;
    }

    /**
     * Re-apply the edit, assuming that it has been undone.
     */
    public void redo() throws CannotRedoException {
    }

    /**
     * True if it is still possible to redo this operation
     */
    public boolean canRedo() {
        return false;
    }

    /**
     * May be sent to inform an edit that it should no longer be
     * used. This is a useful hook for cleaning up state no longer
     * needed once undoing or redoing is impossible--for example,
     * deleting file resources used by objects that can no longer be
     * undeleted. UndoManager calls this before it dequeues edits.
     *
     * Note that this is a one-way operation. There is no "undie"
     * method.
     *
     * @see CompoundEdit#die
     */
    public void die() {
    }

    /**
     * This UndoableEdit should absorb anEdit if it can. Return true
     * if anEdit has been incoporated, false if it has not.
     *
     * <p>Typically the receiver is already in the queue of a
     * UndoManager (or other UndoableEditListener), and is being
     * given a chance to incorporate anEdit rather than letting it be
     * added to the queue in turn.</p>
     *
     * <p>If true is returned, from now on anEdit must return false from
     * canUndo() and canRedo(), and must throw the appropriate
     * exception on undo() or redo().</p>
     */
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    /**
     * Return true if this UndoableEdit should replace anEdit. The
     * receiver should incorporate anEdit's state before returning true.
     *
     * <p>This message is the opposite of addEdit--anEdit has typically
     * already been queued in a UndoManager (or other
     * UndoableEditListener), and the receiver is being given a chance
     * to take its place.</p>
     *
     * <p>If true is returned, from now on anEdit must return false from
     * canUndo() and canRedo(), and must throw the appropriate
     * exception on undo() or redo().</p>
     */
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    /**
     * Return false if this edit is insignificant--for example one
     * that maintains the user's selection, but does not change any
     * model state. This status can be used by an UndoableEditListener
     * (like UndoManager) when deciding which UndoableEdits to present
     * to the user as Undo/Redo options, and which to perform as side
     * effects of undoing or redoing other events.
     */
    public boolean isSignificant() {
        return true;
    }

    /**
     * Provide a localized, human readable description of this edit
     * suitable for use in, say, a change log.
     */
    public String getPresentationName() {
        return _presName;
    }

    public void setPresentationName(String s) {
        _presName = s;
    }

    /**
     * Provide a localized, human readable description of the undoable
     * form of this edit, e.g. for use as an Undo menu item. Typically
     * derived from getDescription();
     */
    public String getUndoPresentationName() {
        return _presName;
    }

    /**
     * Provide a localized, human readable description of the redoable
     * form of this edit, e.g. for use as a Redo menu item. Typically
     * derived from getPresentationName();
     */
    public String getRedoPresentationName() {
        return _presName;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("MerlotUndoableEdit [");
        sb.append(_presName + ", ");
        sb.append("action=" + _action + ", ");
        sb.append("node=" + _node);
        sb.append("location=" + array2String(_nodeLocation) + "]");
        return sb.toString();
    }

    public String array2String(int[] array) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i + 1 < array.length) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
