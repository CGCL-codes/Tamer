package pcgen.cdom.facet;

import junit.framework.TestCase;
import org.junit.Test;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.PCTemplate;
import pcgen.core.Race;

public class HandsFacetTest extends TestCase {

    private CharID id;

    private CharID altid;

    private HandsFacet facet = new HandsFacet();

    private RaceFacet rfacet = new RaceFacet();

    private TemplateFacet tfacet = new TemplateFacet();

    @Override
    public void setUp() throws Exception {
        super.setUp();
        id = new CharID();
        altid = new CharID();
    }

    @Test
    public void testRaceTypeUnsetNull() {
        assertEquals(0, facet.getHands(id));
    }

    @Test
    public void testWithNothingInRaceDefault2() {
        rfacet.set(id, new Race());
        assertEquals(2, facet.getHands(id));
    }

    @Test
    public void testAvoidPollution() {
        Race r = new Race();
        r.put(IntegerKey.CREATURE_HANDS, 5);
        rfacet.set(id, r);
        assertEquals(0, facet.getHands(altid));
    }

    @Test
    public void testGetFromRace() {
        Race r = new Race();
        r.put(IntegerKey.CREATURE_HANDS, 5);
        rfacet.set(id, r);
        assertEquals(5, facet.getHands(id));
        rfacet.remove(id);
        assertEquals(0, facet.getHands(id));
    }

    @Test
    public void testGetFromTemplate() {
        rfacet.set(id, new Race());
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.CREATURE_HANDS, 5);
        tfacet.add(id, t);
        assertEquals(5, facet.getHands(id));
        tfacet.remove(id, t);
        assertEquals(2, facet.getHands(id));
    }

    @Test
    public void testGetFromTemplateSecondOverrides() {
        Race r = new Race();
        r.put(IntegerKey.CREATURE_HANDS, 5);
        rfacet.set(id, r);
        assertEquals(5, facet.getHands(id));
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.CREATURE_HANDS, 3);
        tfacet.add(id, t);
        assertEquals(3, facet.getHands(id));
        PCTemplate t5 = new PCTemplate();
        t5.put(IntegerKey.CREATURE_HANDS, 4);
        tfacet.add(id, t5);
        assertEquals(4, facet.getHands(id));
        tfacet.remove(id, t);
        assertEquals(4, facet.getHands(id));
        tfacet.add(id, t);
        assertEquals(3, facet.getHands(id));
        tfacet.remove(id, t);
        assertEquals(4, facet.getHands(id));
        tfacet.remove(id, t5);
        assertEquals(5, facet.getHands(id));
    }
}
