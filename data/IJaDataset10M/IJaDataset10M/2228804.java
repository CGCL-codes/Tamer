package net.sourceforge.freecol.common;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class UnitTest extends TestCase {

    public static final String COPYRIGHT = "Copyright (C) 2003 The FreeCol Team";

    public static final String LICENSE = "http://www.gnu.org/licenses/gpl.html";

    public static final String REVISION = "$Revision: 611 $";

    public void testEquals() {
        Location location = new LocationStub();
        Player owner = new PlayerStub();
        Unit unit1 = new Unit(location, owner, Unit.FREE_COLONIST, 1, Unit.ACTIVE, 123, false, false, false, 0);
        Unit unit2 = new Unit(location, owner, Unit.ELDER_STATESMAN, 1, Unit.FORTIFY, 314, true, true, false, 0);
        assertTrue(unit2.equals(unit2));
        assertTrue(unit1.equals(unit1));
        assertFalse(unit1.equals(unit2));
    }

    public void testDelete() {
        Location location = new LocationStub();
        Player owner = new PlayerStub();
        Unit unit = new Unit(location, owner, Unit.FREE_COLONIST, 1, Unit.ACTIVE, 123, false, false, false, 0);
        assertFalse(owner.contains(unit) == 0);
        assertFalse(location.contains(unit) == 0);
        unit.delete();
        assertTrue(owner.contains(unit) == 0);
        assertTrue(location.contains(unit) == 0);
    }

    public void testMoveTo() {
        Location location1 = new LocationStub();
        Location location2 = new LocationStub();
        Player owner = new PlayerStub();
        Unit unit = new Unit(location1, owner, Unit.FREE_COLONIST, 1, Unit.ACTIVE, 123, false, false, false, 0);
        assertFalse(location1.contains(unit) == 0);
        assertTrue(location2.contains(unit) == 0);
        unit.moveTo(location2);
        assertTrue(location1.contains(unit) == 0);
        assertFalse(location2.contains(unit) == 0);
    }

    public UnitTest(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite s = new TestSuite(UnitTest.class);
        return s;
    }
}
