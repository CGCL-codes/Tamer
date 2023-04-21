package calclipse.core.disp;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import calclipse.lib.lcd.display.Direction;
import calclipse.lib.lcd.display.Display;
import calclipse.lib.lcd.display.DisplayModel;

public class TrapModelTest {

    public static final List<InputMethod> INPUT_STACK = new ArrayList<InputMethod>();

    private static final long SHORT_SLEEP = 100;

    private static final long MEDIUM_SLEEP = 1000;

    private static final List<Integer> SYNC_LIST = new ArrayList<Integer>();

    private static final Logger LOGGER = Logger.getLogger(TrapModelTest.class.getName());

    public enum InputMethod {

        CLEAR, SET_POSITION, SET_SELECTION, SET_SELECTING, INSERT, MOVE_LEFT, MOVE_RIGHT, MOVE_UP, MOVE_DOWN, DELETE_NEXT, DELETE_PREVIOUS, REMOVE_SELECTION, TYPE, MOVE, DELETE, BACKSPACE, REMOVE_SELECTION_2, BYPASS_CLEAR, BYPASS_SET_POSITION, BYPASS_SET_SELECTION, BYPASS_SET_SELECTING, BYPASS_INSERT, BYPASS_MOVE_LEFT, BYPASS_MOVE_RIGHT, BYPASS_MOVE_UP, BYPASS_MOVE_DOWN, BYPASS_DELETE_NEXT, BYPASS_DELETE_PREVIOUS, BYPASS_REMOVE_SELECTION, BYPASS_TYPE, BYPASS_MOVE, BYPASS_DELETE, BYPASS_BACKSPACE, BYPASS_REMOVE_SELECTION_2, TRAP_CLEAR, TRAP_SET_POSITION, TRAP_SET_SELECTION, TRAP_SET_SELECTING, TRAP_INSERT, TRAP_MOVE_LEFT, TRAP_MOVE_RIGHT, TRAP_MOVE_UP, TRAP_MOVE_DOWN, TRAP_DELETE_NEXT, TRAP_DELETE_PREVIOUS, TRAP_REMOVE_SELECTION, TRAP_TYPE, TRAP_MOVE, TRAP_DELETE, TRAP_BACKSPACE, TRAP_REMOVE_SELECTION_2
    }

    public static class TestTrap extends AbstractInputTrap {

        public TestTrap(final InputTrap next) {
            super(next);
        }

        @Override
        public int backspace(final TrapModel model, final int n) {
            INPUT_STACK.add(InputMethod.TRAP_BACKSPACE);
            return super.backspace(model, n);
        }

        @Override
        public void clear(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_CLEAR);
            super.clear(model, fireEvent);
        }

        @Override
        public int delete(final TrapModel model, final int n) {
            INPUT_STACK.add(InputMethod.TRAP_DELETE);
            return super.delete(model, n);
        }

        @Override
        public boolean deleteNext(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_DELETE_NEXT);
            return super.deleteNext(model, fireEvent);
        }

        @Override
        public boolean deletePrevious(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_DELETE_PREVIOUS);
            return super.deletePrevious(model, fireEvent);
        }

        @Override
        public void insert(final TrapModel model, final char c, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_INSERT);
            super.insert(model, c, fireEvent);
        }

        @Override
        public int move(final TrapModel model, final int n, final Direction d) {
            INPUT_STACK.add(InputMethod.TRAP_MOVE);
            return super.move(model, n, d);
        }

        @Override
        public boolean moveDown(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_MOVE_DOWN);
            return super.moveDown(model, fireEvent);
        }

        @Override
        public boolean moveLeft(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_MOVE_LEFT);
            return super.moveLeft(model, fireEvent);
        }

        @Override
        public boolean moveRight(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_MOVE_RIGHT);
            return super.moveRight(model, fireEvent);
        }

        @Override
        public boolean moveUp(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_MOVE_UP);
            return super.moveUp(model, fireEvent);
        }

        @Override
        public boolean removeSelection(final TrapModel model, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_REMOVE_SELECTION);
            return super.removeSelection(model, fireEvent);
        }

        @Override
        public boolean removeSelection(final TrapModel model) {
            INPUT_STACK.add(InputMethod.TRAP_REMOVE_SELECTION_2);
            return super.removeSelection(model);
        }

        @Override
        public void setPosition(final TrapModel model, final int x, final int y, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_SET_POSITION);
            super.setPosition(model, x, y, fireEvent);
        }

        @Override
        public void setSelecting(final TrapModel model, final boolean b, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_SET_SELECTING);
            super.setSelecting(model, b, fireEvent);
        }

        @Override
        public void setSelection(final TrapModel model, final int x, final int y, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.TRAP_SET_SELECTION);
            super.setSelection(model, x, y, fireEvent);
        }

        @Override
        public void type(final TrapModel model, final String text) {
            INPUT_STACK.add(InputMethod.TRAP_TYPE);
            super.type(model, text);
        }

        @Override
        public void close(final TrapModel model, final boolean wasInterrupted) {
        }

        @Override
        public void open(final TrapModel model) {
        }
    }

    public static class TestModel extends TrapModel {

        private static final long serialVersionUID = 1L;

        public TestModel(final Display display) {
            super(display);
        }

        @Override
        public int backspace(final int n) {
            INPUT_STACK.add(InputMethod.BACKSPACE);
            return super.backspace(n);
        }

        @Override
        public int bypassBackspace(final int n) {
            INPUT_STACK.add(InputMethod.BYPASS_BACKSPACE);
            return super.bypassBackspace(n);
        }

        @Override
        public void bypassClear(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_CLEAR);
            super.bypassClear(fireEvent);
        }

        @Override
        public int bypassDelete(final int n) {
            INPUT_STACK.add(InputMethod.BYPASS_DELETE);
            return super.bypassDelete(n);
        }

        @Override
        public boolean bypassDeleteNext(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_DELETE_NEXT);
            return super.bypassDeleteNext(fireEvent);
        }

        @Override
        public boolean bypassDeletePrevious(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_DELETE_PREVIOUS);
            return super.bypassDeletePrevious(fireEvent);
        }

        @Override
        public void bypassInsert(final char c, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_INSERT);
            super.bypassInsert(c, fireEvent);
        }

        @Override
        public int bypassMove(final int n, final Direction d) {
            INPUT_STACK.add(InputMethod.BYPASS_MOVE);
            return super.bypassMove(n, d);
        }

        @Override
        public boolean bypassMoveDown(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_MOVE_DOWN);
            return super.bypassMoveDown(fireEvent);
        }

        @Override
        public boolean bypassMoveLeft(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_MOVE_LEFT);
            return super.bypassMoveLeft(fireEvent);
        }

        @Override
        public boolean bypassMoveRight(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_MOVE_RIGHT);
            return super.bypassMoveRight(fireEvent);
        }

        @Override
        public boolean bypassMoveUp(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_MOVE_UP);
            return super.bypassMoveUp(fireEvent);
        }

        @Override
        public boolean bypassRemoveSelection() {
            INPUT_STACK.add(InputMethod.BYPASS_REMOVE_SELECTION_2);
            return super.bypassRemoveSelection();
        }

        @Override
        public boolean bypassRemoveSelection(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_REMOVE_SELECTION);
            return super.bypassRemoveSelection(fireEvent);
        }

        @Override
        public void bypassSetPosition(final int x, final int y, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_SET_POSITION);
            super.bypassSetPosition(x, y, fireEvent);
        }

        @Override
        public void bypassSetSelecting(final boolean b, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_SET_SELECTING);
            super.bypassSetSelecting(b, fireEvent);
        }

        @Override
        public void bypassSetSelection(final int x, final int y, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.BYPASS_SET_SELECTION);
            super.bypassSetSelection(x, y, fireEvent);
        }

        @Override
        public void bypassType(final String text) {
            INPUT_STACK.add(InputMethod.BYPASS_TYPE);
            super.bypassType(text);
        }

        @Override
        public void clear(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.CLEAR);
            super.clear(fireEvent);
        }

        @Override
        public int delete(final int n) {
            INPUT_STACK.add(InputMethod.DELETE);
            return super.delete(n);
        }

        @Override
        public boolean deleteNext(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.DELETE_NEXT);
            return super.deleteNext(fireEvent);
        }

        @Override
        public boolean deletePrevious(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.DELETE_PREVIOUS);
            return super.deletePrevious(fireEvent);
        }

        @Override
        public void insert(final char c, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.INSERT);
            super.insert(c, fireEvent);
        }

        @Override
        public int move(final int n, final Direction d) {
            INPUT_STACK.add(InputMethod.MOVE);
            return super.move(n, d);
        }

        @Override
        public boolean moveDown(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.MOVE_DOWN);
            return super.moveDown(fireEvent);
        }

        @Override
        public boolean moveLeft(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.MOVE_LEFT);
            return super.moveLeft(fireEvent);
        }

        @Override
        public boolean moveRight(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.MOVE_RIGHT);
            return super.moveRight(fireEvent);
        }

        @Override
        public boolean moveUp(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.MOVE_UP);
            return super.moveUp(fireEvent);
        }

        @Override
        public boolean removeSelection() {
            INPUT_STACK.add(InputMethod.REMOVE_SELECTION_2);
            return super.removeSelection();
        }

        @Override
        public boolean removeSelection(final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.REMOVE_SELECTION);
            return super.removeSelection(fireEvent);
        }

        @Override
        public void setPosition(final int x, final int y, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.SET_POSITION);
            super.setPosition(x, y, fireEvent);
        }

        @Override
        public void setSelecting(final boolean b, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.SET_SELECTING);
            super.setSelecting(b, fireEvent);
        }

        @Override
        public void setSelection(final int x, final int y, final boolean fireEvent) {
            INPUT_STACK.add(InputMethod.SET_SELECTION);
            super.setSelection(x, y, fireEvent);
        }

        @Override
        public void type(final String text) {
            INPUT_STACK.add(InputMethod.TYPE);
            super.type(text);
        }
    }

    public static class MockWaitForInput implements Runnable {

        private final TrapModel model;

        private final InputTrap trap;

        public MockWaitForInput(final TrapModel model, final InputTrap trap) {
            this.model = model;
            this.trap = trap;
        }

        @Override
        public void run() {
            try {
                model.waitForInput(trap);
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private TestModel model;

    public TrapModelTest() {
    }

    @Before
    public void setUp() {
        model = new TestModel(new Display());
        INPUT_STACK.clear();
        SYNC_LIST.clear();
    }

    @After
    public void tearDown() {
        model = null;
    }

    private void startSession(final InputTrap next) {
        new Thread(new MockWaitForInput(model, new TestTrap(next))).start();
        try {
            Thread.sleep(SHORT_SLEEP);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private void stopSession() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    model.inputReceived();
                }
            });
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (final InvocationTargetException ex) {
            LOGGER.warning(ex.toString());
        }
    }

    public static void assertOnInputStack(final InputMethod... methods) {
        assertEquals("assertOnInputStack: 1: " + INPUT_STACK, methods.length, INPUT_STACK.size());
        for (int i = 0; i < methods.length; i++) {
            assertEquals("assertOnInputStack: 2: " + INPUT_STACK.get(i), methods[i], INPUT_STACK.get(i));
        }
    }

    @Test
    public void testClear() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.clear(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.CLEAR, InputMethod.TRAP_CLEAR, InputMethod.BYPASS_CLEAR);
    }

    @Test
    public void testSetPosition() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.setPosition(0, 0, false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.SET_POSITION, InputMethod.TRAP_SET_POSITION, InputMethod.BYPASS_SET_POSITION);
    }

    @Test
    public void testSetSelection() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.setSelection(0, 0, false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.SET_SELECTION, InputMethod.TRAP_SET_SELECTION, InputMethod.BYPASS_SET_SELECTION);
    }

    @Test
    public void testSetSelecting() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.setSelecting(false, false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING);
    }

    @Test
    public void testInsert() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.insert('a', false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.INSERT, InputMethod.TRAP_INSERT, InputMethod.BYPASS_INSERT);
    }

    @Test
    public void testMoveLeft() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.moveLeft(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.MOVE_LEFT, InputMethod.TRAP_MOVE_LEFT, InputMethod.BYPASS_MOVE_LEFT);
    }

    @Test
    public void testMoveRight() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.moveRight(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.MOVE_RIGHT, InputMethod.TRAP_MOVE_RIGHT, InputMethod.BYPASS_MOVE_RIGHT);
    }

    @Test
    public void testMoveUp() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.moveUp(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.MOVE_UP, InputMethod.TRAP_MOVE_UP, InputMethod.BYPASS_MOVE_UP);
    }

    @Test
    public void testMoveDown() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.moveDown(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.MOVE_DOWN, InputMethod.TRAP_MOVE_DOWN, InputMethod.BYPASS_MOVE_DOWN);
    }

    @Test
    public void testDeleteNext() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.deleteNext(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.DELETE_NEXT, InputMethod.TRAP_DELETE_NEXT, InputMethod.BYPASS_DELETE_NEXT);
    }

    @Test
    public void testDeletePrevious() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.deletePrevious(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.DELETE_PREVIOUS, InputMethod.TRAP_DELETE_PREVIOUS, InputMethod.BYPASS_DELETE_PREVIOUS);
    }

    @Test
    public void testRemoveSelectionBoolean() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.removeSelection(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION);
    }

    @Test
    public void testType() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.type("ab");
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.TYPE, InputMethod.TRAP_TYPE, InputMethod.BYPASS_TYPE, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING, InputMethod.INSERT, InputMethod.TRAP_INSERT, InputMethod.BYPASS_INSERT, InputMethod.INSERT, InputMethod.TRAP_INSERT, InputMethod.BYPASS_INSERT);
    }

    @Test
    public void testMove() {
        model.type(DisplayModel.EOL);
        INPUT_STACK.clear();
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.move(1, Direction.UP);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.MOVE, InputMethod.TRAP_MOVE, InputMethod.BYPASS_MOVE, InputMethod.MOVE_UP, InputMethod.TRAP_MOVE_UP, InputMethod.BYPASS_MOVE_UP);
    }

    @Test
    public void testDelete() {
        model.type("a");
        model.setPosition(0, 0, false);
        INPUT_STACK.clear();
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.delete(1);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.DELETE, InputMethod.TRAP_DELETE, InputMethod.BYPASS_DELETE, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING, InputMethod.DELETE_NEXT, InputMethod.TRAP_DELETE_NEXT, InputMethod.BYPASS_DELETE_NEXT);
    }

    @Test
    public void testBackspace() {
        model.type("a");
        INPUT_STACK.clear();
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.backspace(1);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BACKSPACE, InputMethod.TRAP_BACKSPACE, InputMethod.BYPASS_BACKSPACE, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING, InputMethod.DELETE_PREVIOUS, InputMethod.TRAP_DELETE_PREVIOUS, InputMethod.BYPASS_DELETE_PREVIOUS);
    }

    @Test
    public void testRemoveSelection() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.removeSelection();
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.REMOVE_SELECTION_2, InputMethod.TRAP_REMOVE_SELECTION_2, InputMethod.BYPASS_REMOVE_SELECTION_2, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING);
    }

    @Test
    public void testBypassClear() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassClear(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_CLEAR);
    }

    @Test
    public void testBypassSetPosition() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassSetPosition(0, 0, false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_SET_POSITION);
    }

    @Test
    public void testBypassSetSelection() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassSetSelection(0, 0, false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_SET_SELECTION);
    }

    @Test
    public void testBypassSetSelecting() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassSetSelecting(false, false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_SET_SELECTING);
    }

    @Test
    public void testBypassInsert() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassInsert('a', false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_INSERT);
    }

    @Test
    public void testBypassMoveLeft() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassMoveLeft(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_MOVE_LEFT);
    }

    @Test
    public void testBypassMoveRight() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassMoveRight(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_MOVE_RIGHT);
    }

    @Test
    public void testBypassMoveUp() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassMoveUp(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_MOVE_UP);
    }

    @Test
    public void testBypassMoveDown() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassMoveDown(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_MOVE_DOWN);
    }

    @Test
    public void testBypassDeleteNext() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassDeleteNext(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_DELETE_NEXT);
    }

    @Test
    public void testBypassDeletePrevious() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassDeletePrevious(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_DELETE_PREVIOUS);
    }

    @Test
    public void testBypassRemoveSelectionBoolean() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassRemoveSelection(false);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_REMOVE_SELECTION);
    }

    @Test
    public void testBypassType() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassType("a");
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_TYPE, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING, InputMethod.INSERT, InputMethod.TRAP_INSERT, InputMethod.BYPASS_INSERT);
    }

    @Test
    public void testBypassMove() {
        model.type(DisplayModel.EOL);
        INPUT_STACK.clear();
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassMove(1, Direction.UP);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_MOVE, InputMethod.MOVE_UP, InputMethod.TRAP_MOVE_UP, InputMethod.BYPASS_MOVE_UP);
    }

    @Test
    public void testBypassDelete() {
        model.type("a");
        model.setPosition(0, 0, false);
        INPUT_STACK.clear();
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassDelete(1);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_DELETE, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING, InputMethod.DELETE_NEXT, InputMethod.TRAP_DELETE_NEXT, InputMethod.BYPASS_DELETE_NEXT);
    }

    @Test
    public void testBypassBackspace() {
        model.type("a");
        INPUT_STACK.clear();
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassBackspace(1);
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_BACKSPACE, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING, InputMethod.DELETE_PREVIOUS, InputMethod.TRAP_DELETE_PREVIOUS, InputMethod.BYPASS_DELETE_PREVIOUS);
    }

    @Test
    public void testBypassRemoveSelection() {
        startSession(null);
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                model.bypassRemoveSelection();
            }
        });
        stopSession();
        assertOnInputStack(InputMethod.BYPASS_REMOVE_SELECTION_2, InputMethod.REMOVE_SELECTION, InputMethod.TRAP_REMOVE_SELECTION, InputMethod.BYPASS_REMOVE_SELECTION, InputMethod.SET_SELECTING, InputMethod.TRAP_SET_SELECTING, InputMethod.BYPASS_SET_SELECTING);
    }

    private static final class ReceiveOnOpenTrap extends AbstractInputTrap {

        private final String name;

        private final int iOpen;

        private final int iClose;

        public ReceiveOnOpenTrap(final String n, final int io, final int ic) {
            super(null);
            name = n;
            iOpen = io;
            iClose = ic;
        }

        @Override
        public void close(final TrapModel model, final boolean wasInterrupted) {
            LOGGER.info(name + ": Closing, adding " + iClose + " to SYNC_LIST");
            SYNC_LIST.add(iClose);
        }

        @Override
        public void open(final TrapModel model) {
            LOGGER.info(name + ": Opening, adding " + iOpen + " to SYNC_LIST");
            SYNC_LIST.add(iOpen);
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    LOGGER.info(name + ":run(): calling inputReceived()");
                    model.inputReceived();
                }
            });
        }
    }

    private static final class WaitThread implements Runnable {

        private final String name;

        private final TrapModel model;

        private final InputTrap trap;

        private final CyclicBarrier barrier;

        public WaitThread(final String name, final TrapModel model, final InputTrap trap, final CyclicBarrier barrier) {
            this.name = name;
            this.model = model;
            this.trap = trap;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                LOGGER.info(name + ": awating...");
                barrier.await();
                LOGGER.info(name + ": Over the barrier, waiting for input...");
                model.waitForInput(trap);
                LOGGER.info(name + ": returned from waitForInput. DONE!");
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
            } catch (final BrokenBarrierException ex) {
                LOGGER.warning(ex.toString());
            }
        }
    }

    private static void assertSync() {
        assertEquals("SYNC_LIST.size() % 2 != 0: " + SYNC_LIST, 0, SYNC_LIST.size() % 2);
        for (int i = 0; i < SYNC_LIST.size(); i += 2) {
            assertEquals(SYNC_LIST.toString(), SYNC_LIST.get(i).intValue() + 1, SYNC_LIST.get(i + 1).intValue());
        }
    }

    @Test
    public void testWaitForInput() {
        final TrapModel mdl = new TrapModel(new Display());
        final CyclicBarrier barrier = new CyclicBarrier(7);
        final InputTrap trap1 = new ReceiveOnOpenTrap("trap1", 1, 2);
        final InputTrap trap2 = new ReceiveOnOpenTrap("trap2", 10, 11);
        final InputTrap trap3 = new ReceiveOnOpenTrap("trap3", 100, 101);
        final InputTrap trap4 = new ReceiveOnOpenTrap("trap4", 1000, 1001);
        final InputTrap trap5 = new ReceiveOnOpenTrap("trap5", 10000, 10001);
        final InputTrap trap6 = new ReceiveOnOpenTrap("trap6", 100000, 100001);
        final InputTrap trap7 = new ReceiveOnOpenTrap("trap7", 500000, 500001);
        final Runnable wait1 = new WaitThread("wait1", mdl, trap1, barrier);
        final Runnable wait2 = new WaitThread("wait2", mdl, trap2, barrier);
        final Runnable wait3 = new WaitThread("wait3", mdl, trap3, barrier);
        final Runnable wait4 = new WaitThread("wait4", mdl, trap4, barrier);
        final Runnable wait5 = new WaitThread("wait5", mdl, trap5, barrier);
        final Runnable wait6 = new WaitThread("wait6", mdl, trap6, barrier);
        final Runnable wait7 = new WaitThread("wait7", mdl, trap7, barrier);
        new Thread(wait1).start();
        new Thread(wait2).start();
        new Thread(wait3).start();
        new Thread(wait4).start();
        new Thread(wait5).start();
        new Thread(wait6).start();
        new Thread(wait7).start();
        try {
            Thread.sleep(MEDIUM_SLEEP);
        } catch (final InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        assertSync();
    }
}
