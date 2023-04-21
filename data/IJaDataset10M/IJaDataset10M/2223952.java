package net.sf.jtmt.concurrent.hadoop.querycocanalyzer;

import java.util.HashSet;
import java.util.Set;
import net.sf.jtmt.concurrent.hadoop.querycocanalyzer.QueryTermCoOccurrenceAnalyzer.Pair;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for combination generator.
 * @author Sujit Pal
 * @version $Revision: 51 $
 */
public class CombinationGeneratorTest {

    @Test
    public void testOneWord() throws Exception {
        try {
            String[] terms = new String[] { "breast" };
            System.out.println("combinations for " + StringUtils.join(terms, " ") + " => " + generateCombinations(terms));
            Assert.fail("Did not catch IAE");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testTwoWords() throws Exception {
        String[] terms = new String[] { "breast", "cancer" };
        System.out.println("combinations for " + StringUtils.join(terms, " ") + " => " + generateCombinations(terms));
    }

    @Test
    public void testThreeWords() throws Exception {
        String[] terms = new String[] { "type", "ii", "diabetes" };
        System.out.println("combinations for " + StringUtils.join(terms, " ") + " => " + generateCombinations(terms));
    }

    @Test
    public void testFourWords() throws Exception {
        String[] terms = new String[] { "type", "ii", "diabetes", "symptoms" };
        System.out.println("combinations for " + StringUtils.join(terms, " ") + " => " + generateCombinations(terms));
    }

    private String generateCombinations(String[] terms) {
        CombinationGenerator generator = new CombinationGenerator(terms.length, 2);
        Set<Pair> combinations = new HashSet<Pair>();
        while (generator.hasMore()) {
            int[] indices = generator.getNext();
            combinations.add(new Pair(terms[indices[0]], terms[indices[1]]));
        }
        return combinations.toString();
    }
}
