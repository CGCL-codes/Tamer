package org.jcvi.fluValidator2.flan.errors;

import static org.junit.Assert.*;
import org.jcvi.common.core.Range;
import org.jcvi.common.core.testUtil.TestUtil;
import org.junit.Test;

public class TestDeletionError {

    private final int numBases = 3;

    private final Range location = Range.create(10);

    DeletionError sut = new DeletionError(numBases, location);

    @Test
    public void constructor() {
        assertEquals(numBases, sut.getNumberOfBasesDeleted());
        assertEquals(location, sut.getLocation());
        assertEquals(String.format("deletion of %d bases near %s", numBases, location), sut.getMessage());
    }

    @Test
    public void notEqualToNull() {
        assertFalse(sut.equals(null));
    }

    @Test
    public void notEqualToDifferentClass() {
        assertFalse(sut.equals("not a deletion error"));
    }

    @Test
    public void sameReferenceShouldBeEqual() {
        TestUtil.assertEqualAndHashcodeSame(sut, sut);
    }

    @Test
    public void sameValuesShouldBeEqual() {
        DeletionError same = new DeletionError(numBases, location);
        TestUtil.assertEqualAndHashcodeSame(sut, same);
    }

    @Test
    public void differentLocationShouldNotBeEqual() {
        DeletionError different = new DeletionError(numBases, location.shiftLeft(1));
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, different);
    }

    @Test
    public void differentNumberOfBasesShouldNotBeEqual() {
        DeletionError different = new DeletionError(numBases + 2, location);
        TestUtil.assertNotEqualAndHashcodeDifferent(sut, different);
    }

    @Test(expected = NullPointerException.class)
    public void nullLocationShouldThrowNullPointerException() {
        new DeletionError(numBases, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroBasesDeletedShouldThrowIllegalArgumentException() {
        new DeletionError(0, location);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeBasesDeletedShouldThrowIllegalArgumentException() {
        new DeletionError(-1, location);
    }
}
