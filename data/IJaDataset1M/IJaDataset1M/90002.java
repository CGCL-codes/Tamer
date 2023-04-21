package chsec.domain;

import java.util.List;
import junit.framework.TestCase;

public class ParishionerHouseholdTest extends TestCase {

    private Household hh;

    private String hhLastNm = "hhLastNm";

    private String hhFirstNm = "hhFirstNm";

    protected void setUp() throws Exception {
        super.setUp();
        Parishioner hhHead = new Parishioner();
        hhHead.setLastNm(hhLastNm);
        hhHead.setFirstNm(hhFirstNm);
        hh = new Household();
        hh.setHead(hhHead);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        hh = null;
    }

    public void testCreateHousehold() {
        Parishioner hhHead = hh.getHead();
        assertNotNull("HH Head", hhHead);
        assertEquals("HH LastNm", hhLastNm, hhHead.getLastNm());
        assertEquals("HH First Nm", hhFirstNm, hhHead.getFirstNm());
    }

    public void testHousehold1Member() {
        List<Parishioner> mL = hh.getChildren();
        assertNotNull("Null Result", mL);
        assertEquals("Result Size", 0, mL.size());
    }

    public void testHeadOfHouseholdHasHousehold() {
        Parishioner hhHead = hh.getHead();
        Household hh2 = hhHead.getHousehold();
        assertEquals("HH reference", hh, hh2);
    }

    public void testSetHeadHh() {
        Parishioner oldHead = hh.getHead();
        Parishioner p2 = new Parishioner();
        final String lastNm2 = "LastName2";
        final String firstNm2 = "FirstNm2";
        p2.setLastNm(lastNm2);
        p2.setFirstNm(firstNm2);
        hh.setHead(null);
        hh.setHead(p2);
        assertEquals("HH head", p2, hh.getHead());
        assertEquals("Result", hh, p2.getHousehold());
        assertNull("Old head hh ref", oldHead.getHousehold());
    }

    public void testAddRelativePerson() {
        Parishioner p2 = new Parishioner();
        final String lastNm2 = "LastName2";
        final String firstNm2 = "FirstNm2";
        p2.setLastNm(lastNm2);
        p2.setFirstNm(firstNm2);
        hh.addToMembers(p2, PersonRelation.Spouse);
        Parishioner spouse = hh.getSpouse();
        assertNotNull("Spouse is null", spouse);
        assertEquals("Spouse", p2, spouse);
        List<Parishioner> mL2 = hh.getChildren();
        assertEquals("# of Children", 0, mL2.size());
        assertEquals("New member's HH", hh, p2.getHousehold());
    }

    public void testAddToMembers3dSpouse() {
        Parishioner p2 = new Parishioner();
        final String firstNm2 = "FirstNm2";
        p2.setLastNm(hhLastNm);
        p2.setFirstNm(firstNm2);
        Parishioner p3 = new Parishioner();
        final String firstNm3 = "FirstNm3";
        p3.setLastNm(hhLastNm);
        p3.setFirstNm(firstNm3);
        assertEquals("Result 2 spouses", true, hh.addToMembers(p2, PersonRelation.Spouse));
        assertEquals("Result 3 spouses", false, hh.addToMembers(p3, PersonRelation.Spouse));
        Parishioner spouse = hh.getSpouse();
        assertEquals("2nd spouse", p2, spouse);
    }

    public void testAddToMembersChild() {
        Parishioner p2 = new Parishioner();
        final String firstNm2 = "FirstNm2";
        p2.setLastNm(hhLastNm);
        p2.setFirstNm(firstNm2);
        assertEquals("Result add child", true, hh.addToMembers(p2, PersonRelation.Child));
        List<Parishioner> mL = hh.getChildren();
        assertTrue("Child in hh", mL.contains(p2));
        assertEquals("Child's hh", hh, p2.getHousehold());
        assertEquals("HH size", 1, mL.size());
    }
}
