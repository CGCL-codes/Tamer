package PatchConversion;

import junit.framework.*;

public class SynthParmTableTest extends TestCase {

    public SynthParmTableTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SynthParmTableTest.class);
    }

    public void testGetValue() {
        String s;
        SynthParmTable spt = new SynthParmTable("spt1", new String[] { "Off", "On" }, new SysexParmOffset(0, 0));
        spt.setValue(1);
        assertTrue(spt.isValid());
        assertTrue(spt.isDefined());
        s = spt.getValue();
        assertTrue(s.equals("On"));
        spt.setValue(2);
        assertTrue(spt.isValid());
        assertFalse(spt.isDefined());
        s = spt.getValue();
        assertTrue(s.equals("2"));
        spt.setValue(128);
        assertFalse(spt.isValid());
        assertFalse(spt.isDefined());
    }

    public void testSetValue() {
        int i;
        SynthParmTable spt = new SynthParmTable("spt2", new String[] { "Off", "On" }, new SysexParmOffset(0, 0));
        spt.setValue("On");
        assertTrue(spt.isValid());
        assertTrue(spt.isDefined());
        i = spt.getIntValue();
        assertTrue(i == 1);
        spt.setValue("5");
        assertTrue(spt.isValid());
        assertFalse(spt.isDefined());
        i = spt.getIntValue();
        assertTrue(i == 5);
        spt.setValue("128");
        assertFalse(spt.isValid());
        assertFalse(spt.isDefined());
        i = spt.getIntValue();
        assertTrue(i == 128);
    }
}
