package es.eucm.eadventure.editor.control.tools.generic;

import java.util.List;
import es.eucm.eadventure.editor.control.Controller;
import es.eucm.eadventure.editor.control.tools.Tool;

/**
 * Convenient edition tool for moving up or down an object in a list (one unit)
 * 
 * @author Javier
 * 
 */
public class MoveObjectTool extends Tool {

    public static final int MODE_UP = 0;

    public static final int MODE_DOWN = 1;

    private List list;

    private int index;

    private int newIndex;

    private int mode;

    /**
     * Constructor.
     * 
     * @param list
     *            The List which contains the object to be moved
     * @param index
     *            The index of the object in the list
     * @param mode
     *            MODE_UP if the object must be moved one position up MODE_DOWN
     *            if the object must be moved one position down
     */
    public MoveObjectTool(List list, int index, int mode) {
        this.list = list;
        this.index = index;
        this.mode = mode;
    }

    /**
     * Constructor.
     * 
     * @param list
     *            The List which contains the object to be moved
     * @param object
     *            The object in the list. It must be compulsorily in the list
     * @param mode
     *            MODE_UP if the object must be moved one position up MODE_DOWN
     *            if the object must be moved one position down
     */
    public MoveObjectTool(List list, Object object, int mode) {
        this(list, list.indexOf(object), mode);
    }

    @Override
    public boolean canRedo() {
        return true;
    }

    @Override
    public boolean canUndo() {
        return true;
    }

    @Override
    public boolean doTool() {
        if (mode == MODE_UP) newIndex = moveUp(); else if (mode == MODE_DOWN) newIndex = moveDown();
        return (newIndex != -1);
    }

    @Override
    public boolean redoTool() {
        boolean done = false;
        if (mode == MODE_UP) done = moveUp() != -1; else if (mode == MODE_DOWN) done = moveDown() != -1;
        if (done) Controller.getInstance().updatePanel();
        return done;
    }

    @Override
    public boolean undoTool() {
        boolean done = false;
        if (mode == MODE_UP) {
            int temp = index;
            index = newIndex;
            done = moveDown() != -1;
            index = temp;
        } else if (mode == MODE_DOWN) {
            int temp = index;
            index = newIndex;
            done = moveUp() != -1;
            index = temp;
        }
        if (done) Controller.getInstance().updatePanel();
        return done;
    }

    @Override
    public boolean combine(Tool other) {
        return false;
    }

    private int moveUp() {
        int moved = -1;
        if (index > 0) {
            list.add(index - 1, list.remove(index));
            moved = index - 1;
        }
        return moved;
    }

    private int moveDown() {
        int moved = -1;
        if (index < list.size() - 1) {
            list.add(index + 1, list.remove(index));
            moved = index + 1;
        }
        return moved;
    }
}
