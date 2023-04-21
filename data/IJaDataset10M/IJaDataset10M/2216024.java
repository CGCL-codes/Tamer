package pt.igeo.snig.mig.undoManager;

import java.util.LinkedList;
import pt.igeo.snig.mig.editor.record.RecordManager;

/**
 * this class manage the undo and redo feature 
 * 
 * 
 * @author David Duque
 * @since 3.0
 */
public class UndoManager {

    /**
	 * Undo, redo Stack of changes  
	 */
    LinkedList<UndoElement> undoList = new LinkedList<UndoElement>();

    /**
	 * Undo Singleton 
	 */
    static UndoManager undoManager;

    int currentValueIndex = 0;

    /**
	 * 
	 * Get the singleton Undo Manager
	 * 
	 * @return Instance of UndoManager
	 */
    public static UndoManager getInstance() {
        if (undoManager == null) {
            undoManager = new UndoManager();
        }
        return undoManager;
    }

    /**
	 * 
	 * Add a element to undo Stack
	 * 
	 * @param element Undo Element
	 */
    public void addUndoElement(UndoElement element) {
        while (undoList.size() > currentValueIndex) {
            undoList.removeLast();
        }
        element.setRecord(RecordManager.getInstance().getRecord());
        undoList.addLast(element);
        currentValueIndex++;
    }

    /**
	 * 
	 * Pop one element from the undo stack and revert changes 
	 * 
	 */
    public void undo() {
        if (currentValueIndex <= 0) return;
        currentValueIndex--;
        undoRedo();
    }

    private void undoRedo() {
        UndoElement undoElement = undoList.get(currentValueIndex);
        UndoListener undoListner = undoElement.getListener();
        undoElement.setOlderObject(undoListner.undo(undoElement.getOlderObject(), undoElement.getFieldName(), undoElement.getRecord()));
    }

    /**
	 * reset the undo Stack
	 */
    public void reset() {
        undoList = new LinkedList<UndoElement>();
        currentValueIndex = 0;
    }

    public void redo() {
        if (currentValueIndex >= undoList.size()) return;
        undoRedo();
        currentValueIndex++;
    }
}
