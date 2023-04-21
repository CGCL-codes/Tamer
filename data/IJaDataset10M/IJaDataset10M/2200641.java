package org.paninij.datastructures;

import static org.junit.Assert.*;
import org.junit.Test;

public class VersionTest {

    @Test
    public void testMajorVersiondComp() {
        Version v = new Version("1", "2", "3");
        Version match = new Version("1", "2", "3");
        Version majorMiss, minorMiss, buildMiss;
        majorMiss = new Version("2", "2", "3");
        minorMiss = new Version("1", "1", "3");
        buildMiss = new Version("1", "2", "6");
        assertTrue(v.equalsMajor(match));
        assertFalse(v.equalsMajor(majorMiss));
        assertTrue(v.equalsMajor(minorMiss));
        assertTrue(v.equalsMajor(buildMiss));
    }

    @Test
    public void testMinorVersionComp() {
        Version v = new Version("1", "2", "3");
        Version match = new Version("1", "2", "3");
        Version majorMiss, minorMiss, buildMiss;
        majorMiss = new Version("2", "2", "3");
        minorMiss = new Version("1", "1", "3");
        buildMiss = new Version("1", "2", "6");
        assertTrue(v.equalsMinor(match));
        assertFalse(v.equalsMinor(majorMiss));
        assertFalse(v.equalsMinor(minorMiss));
        assertTrue(v.equalsMinor(buildMiss));
    }

    @Test
    public void testFullVersionComp() {
        Version v = new Version("1", "2", "3");
        Version match = new Version("1", "2", "3");
        Version majorMiss, minorMiss, buildMiss;
        majorMiss = new Version("2", "2", "3");
        minorMiss = new Version("1", "1", "3");
        buildMiss = new Version("1", "2", "6");
        assertTrue(v.equalsVersion(match));
        assertFalse(v.equalsVersion(majorMiss));
        assertFalse(v.equalsVersion(minorMiss));
        assertFalse(v.equalsVersion(buildMiss));
    }

    @Test
    public void testHashCode() {
        int h1, h2, h3;
        h1 = "1".hashCode();
        h2 = "2".hashCode();
        h3 = "3".hashCode();
        Version v = new Version("1", "2", "3");
        int hashCode = v.hashCode();
        assertEquals(h1 & 0xFFF00000, hashCode & 0xFFF00000);
        assertEquals(h2 & 0x000FFF00, hashCode & 0x000FFF00);
        assertEquals(h3 & 0x000000FF, hashCode & 0x000000FF);
    }

    @Test
    public void testEquals() {
        Version v = new Version("1", "2", "3");
        assertTrue(v.equals(v));
        assertFalse(v.equals(new Object()));
        assertTrue(v.equals(new Version("1", "2", "3")));
        assertFalse(v.equals(new Version("1", "2", "4")));
    }
}
