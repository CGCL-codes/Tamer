package net.sf.lti.wisdom.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Test;

public class CombinationGeneratorTest {

    private static String toString(int[] array) {
        Collection<Integer> collection = new ArrayList<Integer>();
        for (int e : array) {
            collection.add(e);
        }
        return collection.toString();
    }

    @Test
    public void testGenerateCombinations() {
        {
            CombinationGenerator generator = new CombinationGenerator(new int[0]);
            assertTrue(generator.hasNext());
            assertEquals("[]", toString(generator.next()));
            assertFalse(generator.hasNext());
        }
        {
            CombinationGenerator generator = new CombinationGenerator(new int[] { 0 });
            assertFalse(generator.hasNext());
        }
        {
            CombinationGenerator generator = new CombinationGenerator(new int[] { 0, 1 });
            assertFalse(generator.hasNext());
        }
        {
            CombinationGenerator generator = new CombinationGenerator(new int[] { 1, 2 });
            assertTrue(generator.hasNext());
            assertEquals("[0, 0]", toString(generator.next()));
            assertTrue(generator.hasNext());
            assertEquals("[0, 1]", toString(generator.next()));
            assertFalse(generator.hasNext());
        }
        {
            CombinationGenerator generator = new CombinationGenerator(new int[] { 1, 2, 3 });
            assertTrue(generator.hasNext());
            assertEquals("[0, 0, 0]", toString(generator.next()));
            assertTrue(generator.hasNext());
            assertEquals("[0, 0, 1]", toString(generator.next()));
            assertTrue(generator.hasNext());
            assertEquals("[0, 0, 2]", toString(generator.next()));
            assertTrue(generator.hasNext());
            assertEquals("[0, 1, 0]", toString(generator.next()));
            assertTrue(generator.hasNext());
            assertEquals("[0, 1, 1]", toString(generator.next()));
            assertTrue(generator.hasNext());
            assertEquals("[0, 1, 2]", toString(generator.next()));
            assertFalse(generator.hasNext());
        }
    }
}
