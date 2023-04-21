package gov.nasa.jpf.jvm.undo;

import gov.nasa.jpf.jvm.Area;
import gov.nasa.jpf.jvm.UndoRestorer.DEIState;
import java.util.Stack;

/**
 * An instance of this class keeps a stack of changes.
 *
 * @author Tihomir Gvero (tihomir.gvero@gmail.com)
 */
public class UndoArea {

    protected Stack<UndoObject> elements;

    protected DEIState[] states;

    public UndoArea(DEIState[] states) {
        this.setStates(states);
        elements = new Stack<UndoObject>();
    }

    public void store(UndoObject undoObject) {
        elements.push(undoObject);
    }

    public void restore(Area area) {
        while (!elements.isEmpty()) {
            UndoObject undoObj = elements.pop();
            undoObj.restore(area);
        }
    }

    public void setStates(DEIState[] states) {
        this.states = states;
    }

    public DEIState[] getStates() {
        return states;
    }
}
