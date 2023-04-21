package jrelcal.labelledrelations;

import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class LatticeTest {

    private EntrySetLabelledRelation<Integer, String, LabelInteger, Integer> rel;

    private EntrySetLabelledRelation<String, String, LabelInteger, Integer> rel2;

    private Lattice<Integer, String, LabelInteger, Integer> lattice;

    private Lattice<String, String, LabelInteger, Integer> lattice2;

    @Before
    public void setUp() throws Exception {
        rel = new EntrySetLabelledRelation<Integer, String, LabelInteger, Integer>();
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(1), "odd", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(1), "square", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(2), "even", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(2), "prime", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(3), "odd", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(3), "prime", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(4), "composite", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(4), "even", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(4), "square", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(5), "odd", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(5), "prime", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(6), "composite", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(6), "even", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(7), "odd", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(7), "prime", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(8), "composite", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(8), "even", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(9), "composite", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(9), "odd", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(9), "square", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(10), "composite", new LabelInteger(1)));
        rel.add(new Entry<Integer, String, LabelInteger, Integer>(new Integer(10), "even", new LabelInteger(1)));
        lattice = new Lattice<Integer, String, LabelInteger, Integer>(rel, new LabelInteger(0));
        rel2 = new EntrySetLabelledRelation<String, String, LabelInteger, Integer>();
        rel2.add(new Entry<String, String, LabelInteger, Integer>("Class1", "Developer1", new LabelInteger(10)));
        rel2.add(new Entry<String, String, LabelInteger, Integer>("Class1", "Developer2", new LabelInteger(5)));
        rel2.add(new Entry<String, String, LabelInteger, Integer>("Class2", "Developer2", new LabelInteger(8)));
        rel2.add(new Entry<String, String, LabelInteger, Integer>("Class2", "Developer3", new LabelInteger(8)));
        rel2.add(new Entry<String, String, LabelInteger, Integer>("Class3", "Developer3", new LabelInteger(6)));
        rel2.add(new Entry<String, String, LabelInteger, Integer>("Class3", "Developer4", new LabelInteger(12)));
        lattice2 = new Lattice<String, String, LabelInteger, Integer>(rel2, new LabelInteger(0));
    }

    @Test
    public final void testLattice() {
        lattice.diagramDot("lattice1", 0);
        lattice2.diagramDot("lattice2", 0);
        assertFalse(lattice.equals(lattice2));
    }

    @Test
    public final void testGetAttributes() {
        Set<String> set = new HashSet<String>();
        set.add("Developer1");
        set.add("Developer2");
        set.add("Developer3");
        set.add("Developer4");
        assertEquals(set, lattice2.getAttributes());
    }

    @Test
    public final void testGetObjects() {
        Set<String> set = new HashSet<String>();
        set.add("Class1");
        set.add("Class2");
        set.add("Class3");
        assertEquals(set, lattice2.getObjects());
    }

    @Test
    public final void testToString() {
        assertFalse(lattice2.toString().equals(""));
    }
}
