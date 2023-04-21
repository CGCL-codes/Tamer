package net.sf.rmoffice.meta;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class SpelllistTypeTest {

    @Test
    public void testEquals() {
        SpelllistType t1 = new SpelllistType(SpelllistType.OPEN, false, null);
        SpelllistType t2 = new SpelllistType(SpelllistType.OPEN, false, null);
        SpelllistType t3 = new SpelllistType(SpelllistType.CLOSED, false, null);
        assertTrue(t1.equals(t2));
        assertEquals(t1.hashCode(), t2.hashCode());
        assertFalse(t1.equals(t3));
        assertFalse(t2.equals(t3));
        List<Integer> prof1 = new ArrayList<Integer>();
        prof1.add(Integer.valueOf(4));
        SpelllistType tp1 = new SpelllistType(SpelllistType.PROFESSION, false, prof1);
        List<Integer> prof2 = new ArrayList<Integer>();
        prof2.add(Integer.valueOf(4));
        SpelllistType tp2 = new SpelllistType(SpelllistType.PROFESSION, false, prof2);
        assertTrue(tp1.equals(tp2));
        assertEquals(tp1.hashCode(), tp2.hashCode());
    }
}
