package jsynoptic.base;

import java.util.LinkedHashMap;
import javax.swing.undo.CompoundEdit;
import simtools.diagram.DiagramSelection;

/**
 * Implementing objects can provide actions on an selection of objects.
 * These actions can depend on context information (position and target object)
 * 
 * @author zxpletran007
 *
 */
public interface SelectionContextualActionProvider {

    /**
     * Return list of possible collective actions
     * @param sel           the current selection
     * @param x             Coordinate, mouse position in the same unit as contains(x,y)
     * @param y             Coordinate, mouse position in the same unit as contains(x,y)
     * @param o             Object the collective actions should work on. Possibly null => default or all actions
     * @param context       one of the context defined in the ContextualActionProvider class
     * @return           The list of possible actions, possibly empty.The map values contains when applicable parent sub menus and icons
     */
    LinkedHashMap getCollectiveActions(DiagramSelection sel, double x, double y, Object o, int context);

    /**
     * Do one of the collective actions previously declared by getCollectiveActions.
     * @param sel           the current selection
     * @param x             Coordinate, for example mouse position
     * @param y             Coordinate, for example mouse position
     * @param o             Object the action should work on.
     * @param action        action An action returned by a previous getCollectiveActions call with the same x, y, o parameters
     * @param undoableEdit  undoableEdit The current compound edit for undo/redo operation. 
     *                      If the action is undoable, the required edits shall be added to this current compound edit.
     * @return              true if the collectiveaction could be performed
     */
    boolean doCollectiveAction(DiagramSelection sel, double x, double y, Object o, String action, CompoundEdit undoableEdit);
}
