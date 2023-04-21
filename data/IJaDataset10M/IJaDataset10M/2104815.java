package net.sourceforge.mystopwatch.undo;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

/**
 * Adding a new object will reset all objects on the redo list.
 */
public class Test_U_MO_4_AddResetsRedo {

    /**
	 * If an undo is added and one redo exists in the redo list, the redo is
	 * removed from the redo list.
	 * <ol>
	 * <li>Create an undo manager.</li>
	 * <li>Create and add an undoable (with a specific name).</li>
	 * <li>Invoke <code>undo()</code>.</li>
	 * <li>Create and add a new undoable (with a specific name).</li>
	 * <li>Verify that the redo list is empty and the undo list contains an
	 * undoable with the name of the 2nd undoable created.</li>
	 * </ol>
	 * 
	 * @throws Exception failed
	 */
    @Test
    public void t1_addUndoWithOneRedoInList() throws Exception {
        String action1Name = RandomStringUtils.randomAlphanumeric(10);
        String action2Name = RandomStringUtils.randomAlphanumeric(10);
        UndoManager um = new UndoManager(new UndoManagerConfig());
        um.doAction(new AccountingUndoable(action1Name));
        um.undo();
        um.doAction(new AccountingUndoable(action2Name));
        List<UndoableInfo> undoList = um.getUndoList();
        List<UndoableInfo> redoList = um.getRedoList();
        assertEquals(1, undoList.size());
        assertEquals(0, redoList.size());
        assertEquals(action2Name, undoList.get(0).describe());
    }

    /**
	 * If an undo is added and several redos exist in the redo list, the redos
	 * are removed.
	 * <ol>
	 * <li>Create an undo manager.</li>
	 * <li>Create and add 10 randomly named empty undoables.</li>
	 * <li>Invoke <code>undo()</code> 10 times.</li>
	 * <li>Verify that the undo list is empty and the redo list contains 10
	 * items.</li>
	 * <li>Add a new empty undoable with a specific name.</li>
	 * <li>Verify that the redo list is empty and the undo list contains one
	 * single item with the same name as the one just added.</li>
	 * </ol>
	 * 
	 * @throws Exception failed
	 */
    @Test
    public void t2_addUndoWithSeveralRedosInList() throws Exception {
        String names[] = new String[10];
        for (int i = 0; i < names.length; i++) {
            names[i] = RandomStringUtils.randomAlphanumeric(10);
        }
        String action2Name = RandomStringUtils.randomAlphanumeric(10);
        UndoManager um = new UndoManager(new UndoManagerConfig());
        for (String name : names) {
            um.doAction(new AccountingUndoable(name));
        }
        for (@SuppressWarnings("unused") String name : names) {
            um.undo();
        }
        assertEquals(0, um.getUndoList().size());
        assertEquals(names.length, um.getRedoList().size());
        um.doAction(new AccountingUndoable(action2Name));
        assertEquals(1, um.getUndoList().size());
        assertEquals(0, um.getRedoList().size());
        assertEquals(action2Name, um.getUndoList().get(0).describe());
    }
}
