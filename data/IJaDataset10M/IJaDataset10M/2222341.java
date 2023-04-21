package com.flagstone.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import com.flagstone.transform.datatype.Bounds;

public final class ScalingGridTest {

    private final transient int identifier = 1;

    private final transient Bounds bounds = new Bounds(1, 2, 3, 4);

    private transient ScalingGrid fixture;

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForIdentifierWithLowerBound() {
        fixture = new ScalingGrid(0, bounds);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForIdentifierWithUpperBound() {
        fixture = new ScalingGrid(65536, bounds);
        fixture.setIdentifier(65536);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkAccessorForDataWithNull() {
        fixture = new ScalingGrid(identifier, null);
    }

    @Test
    public void checkCopy() {
        fixture = new ScalingGrid(identifier, bounds);
        assertEquals(fixture.getIdentifier(), fixture.copy().getIdentifier());
        assertSame(fixture.getBounds(), fixture.copy().getBounds());
        assertEquals(fixture.toString(), fixture.toString());
    }
}
