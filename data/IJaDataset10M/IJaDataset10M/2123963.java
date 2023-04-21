package org.fsm4j;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FSMTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testFSMNoArg() {
        FSM fsm = new FSM();
        assertFalse(fsm.getIsDebug());
        assertEquals("FSM", fsm.toString());
    }

    @Test
    public void testFSMNameNull() {
        FSM fsm = new FSM(null);
        assertFalse(fsm.getIsDebug());
        assertEquals("FSM", fsm.toString());
    }

    @Test
    public void testFSMNameEmpty() {
        FSM fsm = new FSM("");
        assertFalse(fsm.getIsDebug());
        assertEquals("", fsm.toString());
    }

    @Test
    public void testFSMName() {
        FSM fsm = new FSM("test-fsm");
        assertFalse(fsm.getIsDebug());
        assertEquals("test-fsm", fsm.toString());
    }

    @Test
    public void testFSMNoStates() {
        FSM fsm = new FSM();
        try {
            FSMContext fsmContext = fsm.createContext();
            fail("FSM has no states");
        } catch (FSMException e) {
        }
    }

    @Test
    public void testFSMOneState() {
        FSM fsm = new FSM();
        fsm.preloadState("startState");
        FSMContext fsmContext = fsm.createContext();
        assertEquals("startState", fsmContext.getCurrentState());
    }

    @Test
    public void testFSMTwoStates() {
        FSM fsm = new FSM();
        fsm.preloadState("startState");
        fsm.preloadState("endState");
        FSMContext fsmContext = fsm.createContext();
        assertEquals("startState", fsmContext.getCurrentState());
    }

    @Test
    public void testFSMTwoStatesTransition() {
        FSM fsm = new FSM();
        fsm.preloadState("startState");
        fsm.preloadState("endState");
        fsm.addTransition("startState", "endItAll", "endState");
        FSMContext fsmContext = fsm.createContext();
        assertEquals("startState", fsmContext.getCurrentState());
        fsmContext.dispatch("endItAll");
        assertEquals("endState", fsmContext.getCurrentState());
    }

    @Test
    public void testFSMCountToFive() {
        FSM fsm = new FSM();
        fsm.preloadStates("startState", "gotOne", "gotTwo", "gotThree", "gotFour", "gotFive");
        fsm.addTransition("startState", Integer.valueOf(1), "gotOne");
        fsm.addTransition("gotOne", Integer.valueOf(2), "gotTwo");
        fsm.addTransition("gotTwo", Integer.valueOf(3), "gotThree");
        fsm.addTransition("gotThree", Integer.valueOf(4), "gotFour");
        fsm.addTransition("gotFour", Integer.valueOf(5), "gotFive");
        FSMContext fsmContext = fsm.createContext();
        assertEquals("startState", fsmContext.getCurrentState());
        fsmContext.dispatch(1);
        assertEquals("gotOne", fsmContext.getCurrentState());
        fsmContext.dispatch(2);
        assertEquals("gotTwo", fsmContext.getCurrentState());
        fsmContext.dispatch(3);
        assertEquals("gotThree", fsmContext.getCurrentState());
        fsmContext.dispatch(4);
        assertEquals("gotFour", fsmContext.getCurrentState());
        fsmContext.dispatch(5);
        assertEquals("gotFive", fsmContext.getCurrentState());
    }

    @Test
    public void testFSMCountToFiveWithCommands() {
        final String[] array = new String[5];
        FSM fsm = new FSM();
        fsm.preloadStates("startState", "gotOne", "gotTwo", "gotThree", "gotFour", "gotFive");
        fsm.addTransition("startState", Integer.valueOf(1), "gotOne", new FSMCommand() {

            public void execute(FSMContext fsmContext, Object event) {
                array[0] = "tagged";
            }
        });
        fsm.addTransition("gotOne", Integer.valueOf(2), "gotTwo", new FSMCommand() {

            public void execute(FSMContext fsmContext, Object event) {
                array[1] = "tagged";
            }
        });
        fsm.addTransition("gotTwo", Integer.valueOf(3), "gotThree", new FSMCommand() {

            public void execute(FSMContext fsmContext, Object event) {
                array[2] = "tagged";
            }
        });
        fsm.addTransition("gotThree", Integer.valueOf(4), "gotFour", new FSMCommand() {

            public void execute(FSMContext fsmContext, Object event) {
                array[3] = "tagged";
            }
        });
        fsm.addTransition("gotFour", Integer.valueOf(5), "gotFive", new FSMCommand() {

            public void execute(FSMContext fsmContext, Object event) {
                array[4] = "tagged";
            }
        });
        FSMContext fsmContext = fsm.createContext();
        assertEquals("startState", fsmContext.getCurrentState());
        assertNull(array[0]);
        assertNull(array[1]);
        assertNull(array[2]);
        assertNull(array[3]);
        assertNull(array[4]);
        fsmContext.dispatch(1);
        assertEquals("gotOne", fsmContext.getCurrentState());
        assertEquals("tagged", array[0]);
        assertNull(array[1]);
        assertNull(array[2]);
        assertNull(array[3]);
        assertNull(array[4]);
        fsmContext.dispatch(2);
        assertEquals("gotTwo", fsmContext.getCurrentState());
        assertEquals("tagged", array[0]);
        assertEquals("tagged", array[1]);
        assertNull(array[2]);
        assertNull(array[3]);
        assertNull(array[4]);
        fsmContext.dispatch(3);
        assertEquals("gotThree", fsmContext.getCurrentState());
        assertEquals("tagged", array[0]);
        assertEquals("tagged", array[1]);
        assertEquals("tagged", array[2]);
        assertNull(array[3]);
        assertNull(array[4]);
        fsmContext.dispatch(4);
        assertEquals("gotFour", fsmContext.getCurrentState());
        assertEquals("tagged", array[0]);
        assertEquals("tagged", array[1]);
        assertEquals("tagged", array[2]);
        assertEquals("tagged", array[3]);
        assertNull(array[4]);
        fsmContext.dispatch(5);
        assertEquals("gotFive", fsmContext.getCurrentState());
        assertEquals("tagged", array[0]);
        assertEquals("tagged", array[1]);
        assertEquals("tagged", array[2]);
        assertEquals("tagged", array[3]);
        assertEquals("tagged", array[4]);
    }
}
