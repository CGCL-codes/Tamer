package pcgen.cdom.facet;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;
import pcgen.cdom.enumeration.CharID;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.core.PCTemplate;
import pcgen.core.Race;

public class ReachFacetTest extends TestCase {

    private CharID id;

    private CharID altid;

    private ReachFacet facet;

    private RaceFacet rfacet = new RaceFacet();

    private TemplateFacet tfacet = new TemplateFacet();

    private Map<CharID, Double> bonusInfo;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        id = new CharID();
        altid = new CharID();
        facet = getMockFacet();
        bonusInfo = new HashMap<CharID, Double>();
    }

    @Test
    public void testReachUnsetZero() {
        assertEquals(0, facet.getReach(id));
    }

    @Test
    public void testWithNothingInRaceDefaultsTo5() {
        rfacet.set(id, new Race());
        assertEquals(5, facet.getReach(id));
        rfacet.remove(id);
        assertEquals(0, facet.getReach(id));
    }

    @Test
    public void testAvoidPollution() {
        Race r = new Race();
        r.put(IntegerKey.REACH, 5);
        rfacet.set(id, r);
        assertEquals(0, facet.getReach(altid));
    }

    @Test
    public void testGetFromRace() {
        Race r = new Race();
        r.put(IntegerKey.REACH, 5);
        rfacet.set(id, r);
        assertEquals(5, facet.getReach(id));
    }

    @Test
    public void testGetFromTemplateLowerOverridesDefault() {
        rfacet.set(id, new Race());
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.REACH, 3);
        tfacet.add(id, t);
        assertEquals(3, facet.getReach(id));
        tfacet.remove(id, t);
        assertEquals(5, facet.getReach(id));
    }

    @Test
    public void testGetFromTemplateHigherOverridesDefault() {
        rfacet.set(id, new Race());
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.REACH, 7);
        tfacet.add(id, t);
        assertEquals(7, facet.getReach(id));
        tfacet.remove(id, t);
        assertEquals(5, facet.getReach(id));
    }

    @Test
    public void testGetFromTemplateLowerOverridesRace() {
        Race r = new Race();
        r.put(IntegerKey.REACH, 5);
        rfacet.set(id, r);
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.REACH, 3);
        tfacet.add(id, t);
        assertEquals(3, facet.getReach(id));
        tfacet.remove(id, t);
        assertEquals(5, facet.getReach(id));
    }

    @Test
    public void testGetFromTemplateHigherOverridesRace() {
        Race r = new Race();
        r.put(IntegerKey.REACH, 5);
        rfacet.set(id, r);
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.REACH, 8);
        tfacet.add(id, t);
        assertEquals(8, facet.getReach(id));
        tfacet.remove(id, t);
        assertEquals(5, facet.getReach(id));
    }

    @Test
    public void testGetFromTemplateSecondOverrides() {
        Race r = new Race();
        r.put(IntegerKey.REACH, 5);
        rfacet.set(id, r);
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.REACH, 8);
        tfacet.add(id, t);
        PCTemplate t2 = new PCTemplate();
        t2.put(IntegerKey.REACH, 7);
        tfacet.add(id, t2);
        assertEquals(7, facet.getReach(id));
        tfacet.remove(id, t2);
        assertEquals(8, facet.getReach(id));
        tfacet.remove(id, t);
        assertEquals(5, facet.getReach(id));
    }

    @Test
    public void testGetWithBonus() {
        Race r = new Race();
        r.put(IntegerKey.REACH, 5);
        rfacet.set(id, r);
        assertEquals(5, facet.getReach(id));
        bonusInfo.put(altid, 4.0);
        assertEquals(5, facet.getReach(id));
        bonusInfo.put(id, 6.0);
        assertEquals(11, facet.getReach(id));
        PCTemplate t = new PCTemplate();
        t.put(IntegerKey.REACH, 8);
        tfacet.add(id, t);
        assertEquals(14, facet.getReach(id));
        PCTemplate t2 = new PCTemplate();
        t2.put(IntegerKey.REACH, 7);
        tfacet.add(id, t2);
        assertEquals(13, facet.getReach(id));
        tfacet.remove(id, t2);
        assertEquals(14, facet.getReach(id));
        bonusInfo.clear();
        assertEquals(8, facet.getReach(id));
    }

    public ReachFacet getMockFacet() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ReachFacet f = new ReachFacet();
        Field field = ReachFacet.class.getDeclaredField("bonusFacet");
        field.setAccessible(true);
        BonusCheckingFacet fakeFacet = new BonusCheckingFacet() {

            @Override
            public double getBonus(CharID cid, String bonusType, String bonusName) {
                if ("COMBAT".equals(bonusType) && "REACH".equals(bonusName)) {
                    Double d = bonusInfo.get(cid);
                    return d == null ? 0 : d;
                }
                return 0;
            }
        };
        field.set(f, fakeFacet);
        return f;
    }
}
