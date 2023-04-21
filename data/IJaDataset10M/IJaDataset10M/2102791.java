package phex.test;

import junit.framework.TestCase;
import phex.msg.MsgQuery;

public class TestMsgQuery extends TestCase {

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testIsBitSet() throws Throwable {
        Boolean state;
        state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { new Short((short) 0x0001), new Integer(0) });
        assertTrue(state.booleanValue());
        state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { new Short((short) 0x0001), new Integer(15) });
        assertFalse(state.booleanValue());
        state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { new Short((short) 0x8000), new Integer(15) });
        assertTrue(state.booleanValue());
    }

    public void testSetBit() throws Throwable {
        Short result;
        result = (Short) AccessUtils.invokeMethod(MsgQuery.class, "setBit", new Object[] { new Short((short) 0x0000), new Integer(0) });
        assertEquals(result.shortValue(), 0x0001);
    }

    public void testSettingAndCheckingBits() throws Throwable {
        Short myShort = new Short((short) 0x0000);
        Boolean state;
        myShort = (Short) AccessUtils.invokeMethod(MsgQuery.class, "setBit", new Object[] { myShort, new Integer(0) });
        state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, new Integer(0) });
        assertTrue(state.booleanValue());
        for (int i = 1; i < 16; i++) {
            Integer myInt = new Integer(i);
            state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, myInt });
            assertFalse(state.booleanValue());
        }
        myShort = (Short) AccessUtils.invokeMethod(MsgQuery.class, "setBit", new Object[] { myShort, new Integer(1) });
        for (int i = 0; i < 2; i++) {
            Integer myInt = new Integer(i);
            state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, myInt });
            assertTrue(state.booleanValue());
        }
        for (int i = 2; i < 16; i++) {
            Integer myInt = new Integer(i);
            state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, myInt });
            assertFalse(state.booleanValue());
        }
        myShort = (Short) AccessUtils.invokeMethod(MsgQuery.class, "setBit", new Object[] { myShort, new Integer(15) });
        for (int i = 0; i < 2; i++) {
            Integer myInt = new Integer(i);
            state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, myInt });
            assertTrue(state.booleanValue());
        }
        state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, new Integer(15) });
        assertTrue(state.booleanValue());
        for (int i = 2; i < 15; i++) {
            Integer myInt = new Integer(i);
            state = (Boolean) AccessUtils.invokeMethod(MsgQuery.class, "isBitSet", new Object[] { myShort, myInt });
            assertFalse(state.booleanValue());
        }
    }
}
