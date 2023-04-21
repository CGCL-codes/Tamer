package uk.ac.ebi.intact.confidence.util;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.intact.confidence.model.BinaryInteraction;
import uk.ac.ebi.intact.confidence.model.Confidence;
import uk.ac.ebi.intact.confidence.model.Identifier;
import uk.ac.ebi.intact.confidence.model.UniprotIdentifierImpl;
import uk.ac.ebi.intact.core.unit.IntactBasicTestCase;
import java.util.HashSet;
import java.util.Set;

/**
 * Test class for InteractionGenerator.
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version
 * @since <pre>14-Aug-2007</pre>
 */
public class InteractionGeneratorTest extends IntactBasicTestCase {

    @Test
    public void testGenerateNewObjectSt() throws Exception {
        InteractionGenerator intGen = new InteractionGenerator();
        Set<Identifier> yeastProts = new HashSet<Identifier>();
        Identifier id1 = new UniprotIdentifierImpl("P12345");
        Identifier id2 = new UniprotIdentifierImpl("P12346");
        Identifier id3 = new UniprotIdentifierImpl("P12347");
        Identifier id4 = new UniprotIdentifierImpl("P12348");
        Identifier id5 = new UniprotIdentifierImpl("P12349");
        Identifier id6 = new UniprotIdentifierImpl("P12340");
        yeastProts.add(id1);
        yeastProts.add(id2);
        yeastProts.add(id3);
        yeastProts.add(id4);
        yeastProts.add(id5);
        yeastProts.add(id6);
        Set<BinaryInteraction> forbidden = new HashSet<BinaryInteraction>();
        forbidden.add(new BinaryInteraction(id1, id2, Confidence.HIGH));
        forbidden.add(new BinaryInteraction(id1, id3, Confidence.HIGH));
        forbidden.add(new BinaryInteraction(id1, id4, Confidence.UNKNOWN));
        forbidden.add(new BinaryInteraction(id1, id5, Confidence.LOW));
        forbidden.add(new BinaryInteraction(id1, id6, Confidence.LOW));
        Set<BinaryInteraction> generated = intGen.generate(yeastProts, forbidden, 2);
        Assert.assertNotNull(generated);
        Assert.assertEquals(2, generated.size());
        for (BinaryInteraction bi : generated) {
            Assert.assertFalse(forbidden.contains(bi));
            Assert.assertFalse(bi.getFirstId().equals(id1));
        }
    }
}
