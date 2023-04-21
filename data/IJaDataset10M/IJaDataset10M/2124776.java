package org.jcvi.assembly.ace;

import java.util.Date;
import org.jcvi.testUtil.TestUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestDefaultPhdInfo {

    Date date = new Date(123456789L);

    Date differentDate = new Date(0L);

    String phdName = "phdName";

    String traceName = "traceName";

    DefaultPhdInfo sut = new DefaultPhdInfo(traceName, phdName, date);

    @Test
    public void constructor() {
        assertEquals(phdName, sut.getPhdName());
        assertEquals(traceName, sut.getTraceName());
        assertEquals(date, sut.getPhdDate());
    }

    @Test
    public void notEqualsNull() {
        assertFalse(sut.equals(null));
    }

    @Test
    public void notEqualsDifferentClass() {
        assertFalse(sut.equals("not a phdinfo"));
    }

    @Test
    public void equalsSameRef() {
        TestUtil.assertEqualAndHashcodeSame(sut, sut);
    }

    @Test
    public void equalsSameValues() {
        DefaultPhdInfo sameValues = new DefaultPhdInfo(traceName, phdName, date);
        TestUtil.assertEqualAndHashcodeSame(sut, sameValues);
    }

    @Test
    public void differentTraceNameShouldNotBeEqual() {
        DefaultPhdInfo differentTraceName = new DefaultPhdInfo("different" + traceName, phdName, date);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, differentTraceName);
    }

    @Test
    public void nullTraceNameShouldNotBeEqual() {
        DefaultPhdInfo nullTraceName = new DefaultPhdInfo(null, phdName, date);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, nullTraceName);
    }

    @Test
    public void differentPhdNameShouldNotBeEqual() {
        DefaultPhdInfo differentPhdName = new DefaultPhdInfo(traceName, "different" + phdName, date);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, differentPhdName);
    }

    @Test
    public void nullPhdNameShouldNotBeEqual() {
        DefaultPhdInfo nullPhdName = new DefaultPhdInfo(traceName, null, date);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, nullPhdName);
    }

    @Test
    public void differentDateShouldNotBeEqual() {
        DefaultPhdInfo hasDifferentDate = new DefaultPhdInfo(traceName, phdName, differentDate);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, hasDifferentDate);
    }

    @Test(expected = NullPointerException.class)
    public void nullDateShouldThrowNPE() {
        new DefaultPhdInfo(traceName, phdName, null);
    }
}
