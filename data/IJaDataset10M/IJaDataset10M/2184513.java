package undoredo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultUndoRedoManagerTest {

    private Counter counter;

    private UndoRedoManager undoRedoManager;

    @Before
    public void setup() {
        counter = new Counter();
        undoRedoManager = new DefaultUndoRedoManager();
        undoRedoManager.addManageObject(counter);
        undoRedoManager.snapState();
    }

    @Test
    public void testCanUndo() {
        assertEquals(false, undoRedoManager.canUndo());
        counter.plusOne();
        undoRedoManager.snapState();
        assertEquals(true, undoRedoManager.canUndo());
        undoRedoManager.undo();
        assertEquals(false, undoRedoManager.canUndo());
    }

    @Test
    public void testCanRedo() {
        assertEquals(false, undoRedoManager.canRedo());
        counter.plusOne();
        undoRedoManager.snapState();
        undoRedoManager.undo();
        assertEquals(true, undoRedoManager.canRedo());
    }

    @Test
    public void testCanNotRedo() {
        counter.plusOne();
        undoRedoManager.snapState();
        counter.plusOne();
        undoRedoManager.snapState();
        undoRedoManager.undo();
        undoRedoManager.undo();
        counter.plusOne();
        undoRedoManager.snapState();
        assertEquals(false, undoRedoManager.canRedo());
        assertEquals(1, counter.getCounter());
        undoRedoManager.undo();
        assertEquals(false, undoRedoManager.canUndo());
    }

    @Test
    public void testUndo() {
        counter.plusOne();
        undoRedoManager.snapState();
        undoRedoManager.undo();
        assertEquals(0, counter.getCounter());
    }

    @Test
    public void testRedo() {
        counter.plusOne();
        undoRedoManager.snapState();
        undoRedoManager.undo();
        undoRedoManager.redo();
        assertEquals(1, counter.getCounter());
    }
}
