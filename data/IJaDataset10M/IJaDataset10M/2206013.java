package de.denkselbst.niffler.examples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import de.denkselbst.niffler.SerialisationHelper;
import de.denkselbst.prologinterface.terms.PrologStructure;
import de.denkselbst.prologinterface.terms.PrologTermFactory;

public class ExamplesTest extends TestCase {

    public void testExamplesMustNotBeEmpty() {
        try {
            new Examples(new ArrayList<PrologStructure>());
            fail();
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testCovered() {
        List<PrologStructure> theExamples = createExamples();
        Examples examples = new Examples(theExamples);
        assertEquals(0.0, examples.getCoverage(), 0.0001);
        assertEquals(5, examples.getAllExamples().cardinality());
        assertEquals(5, examples.getUncoveredExamples().cardinality());
        assertEquals(0, examples.getCoverage(), 0.0001);
        examples.setCovered(2);
        assertEquals(100.0 * 1 / 5, examples.getCoverage(), 0.0001);
        assertEquals(4, examples.getUncoveredExamples().cardinality());
        assertEquals(1, examples.getCoveredExamples().cardinality());
        assertFalse(examples.getCoveredExamples().get(0));
        assertFalse(examples.getCoveredExamples().get(1));
        assertTrue(examples.getCoveredExamples().get(2));
        assertFalse(examples.getCoveredExamples().get(3));
        assertFalse(examples.getCoveredExamples().get(4));
        examples.setCovered(2);
        assertEquals(100.0 * 1 / 5, examples.getCoverage(), 0.0001);
        assertEquals(4, examples.getUncoveredExamples().cardinality());
        assertEquals(1, examples.getCoveredExamples().cardinality());
        assertFalse(examples.getCoveredExamples().get(0));
        assertFalse(examples.getCoveredExamples().get(1));
        assertTrue(examples.getCoveredExamples().get(2));
        assertFalse(examples.getCoveredExamples().get(3));
        assertFalse(examples.getCoveredExamples().get(4));
        examples.setCovered(0);
        examples.setCovered(1);
        examples.setCovered(3);
        assertEquals(100.0 * 4 / 5, examples.getCoverage(), 0.0001);
        assertEquals(1, examples.getUncoveredExamples().cardinality());
        assertEquals(4, examples.getCoveredExamples().cardinality());
        assertTrue(examples.getCoveredExamples().get(0));
        assertTrue(examples.getCoveredExamples().get(1));
        assertTrue(examples.getCoveredExamples().get(2));
        assertTrue(examples.getCoveredExamples().get(3));
        assertFalse(examples.getCoveredExamples().get(4));
    }

    public void testSubset() {
        List<PrologStructure> theExamples = createExamples();
        Examples examples = new Examples(theExamples);
        examples.setCovered(2);
        examples.setProcessed(1);
        examples.setProcessed(4);
        BitSet selection = new BitSet();
        try {
            examples.subset(selection);
            fail("Empty selection is illegal.");
        } catch (IllegalArgumentException expected) {
        }
        selection.set(2);
        selection.set(4);
        Examples subset = examples.subset(selection);
        assertNotNull(subset);
        assertEquals("Wrong size.", 2, subset.getAllExamples().cardinality());
        assertEquals("No examples must be covered, initially.", 0, subset.getCoveredExamples().cardinality());
        assertEquals("Initially, all examples must be marked as unprocessed and uncovered.", 2, subset.getUncoveredUnprocessedExamples().cardinality());
        try {
            selection.set(12345);
            examples.subset(selection);
            fail("Selection is out of bounds.");
        } catch (IllegalArgumentException expected) {
        }
    }

    public void testRandomSubset() {
        Examples all = new Examples(createExamples(1000));
        Examples max = all.randomSubset(1500);
        assertEquals("Subset must equal superset.", 1000, max.size());
        Examples subset = all.randomSubset(150);
        assertEquals("Subset has wrong size.", 150, subset.size());
    }

    public void testMerge() {
        Random r = new Random();
        List<PrologStructure> e = createExamples(100);
        List<PrologStructure> e1 = new ArrayList<PrologStructure>();
        List<PrologStructure> e2 = new ArrayList<PrologStructure>();
        for (PrologStructure s : e) if (r.nextBoolean()) e1.add(s); else e2.add(s);
        Examples ex1 = new Examples(e1);
        Examples ex2 = new Examples(e2);
        Examples ex12 = ex1.merge(ex2);
        assertEquals(e.size(), ex12.size());
        for (int i = 0; i < ex12.size(); i++) assertTrue("No example must be 'lost' when merging.", e.contains(ex12.getExampleAtIndex(i)));
        assertEquals("Initially, all examples must be marked as unprocessed and uncovered.", e.size(), ex12.getUncoveredUnprocessedExamples().cardinality());
    }

    public void testSerializable() throws IOException, ClassNotFoundException {
        Examples examples = new Examples(createExamples(99));
        Examples reloaded = (Examples) SerialisationHelper.deSerialise(examples);
        assertEquals(examples, reloaded);
    }

    static List<PrologStructure> createExamples() {
        return createExamples(5);
    }

    static List<PrologStructure> createExamples(int n) {
        return createExamples("e", n);
    }

    static List<PrologStructure> createExamples(String name, int n) {
        List<PrologStructure> theExamples = new ArrayList<PrologStructure>();
        for (int i = 0; i < n; i++) theExamples.add(PrologTermFactory.createString("" + name + (i + 1)));
        return theExamples;
    }
}
