package moteur;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import org.junit.*;

public class CaseTest {

    private Case caseTest;

    @Before
    public void Init() {
        caseTest = new Case();
    }

    @Test
    public void EqualsTest() {
        Case c = new Case();
        assertTrue(c.equals(caseTest));
        Case c2 = new Case();
        c2.captureSubvention();
        assertFalse(c2.equals(caseTest));
        Case c3 = new Case();
        c3.setX(42);
        assertFalse(c3.equals(caseTest));
        Case c4 = new Case();
        c4.setY(42);
        assertFalse(c4.equals(caseTest));
        Case c5 = new Case(new CaseGeometryC());
        assertFalse(c5.equals(caseTest));
        Case c6 = new Case();
        caseTest.setX(42);
        c6.setX(42);
        caseTest.setY(23);
        c6.setY(23);
        caseTest.captureSubvention();
        c6.captureSubvention();
        assertTrue(c6.equals(caseTest));
        assertFalse(c.equals(caseTest));
        assertFalse(c2.equals(caseTest));
        assertFalse(c3.equals(caseTest));
        assertFalse(c4.equals(caseTest));
        assertFalse(c5.equals(caseTest));
    }

    @Test
    public void ouvertTest() {
        Case c = new Case(new CaseGeometry(true, false, false, true));
        Case c2 = new Case(new CaseGeometry(false, true, true, false));
        assertTrue(c.ouvertHaut());
        assertTrue(c.ouvertGauche());
        assertFalse(c.ouvertBas());
        assertFalse(c.ouvertDroite());
        assertFalse(c2.ouvertHaut());
        assertFalse(c2.ouvertGauche());
        assertTrue(c2.ouvertBas());
        assertTrue(c2.ouvertDroite());
    }

    @Test
    public void getCheminsTest() {
        Case ca = new Case(new CaseGeometry(true, false, false, true));
        assertNotNull(ca.getChemins());
        int j = 0;
        boolean c[] = new boolean[] { true, false, false, true };
        assertEquals(c.length, ca.getChemins().length);
        for (boolean i : c) assertEquals(i, ca.getChemins()[j++]);
    }

    @Test
    public void subventionTest() {
        assertTrue(caseTest.contientUneSubvention());
        caseTest.captureSubvention();
        assertFalse(caseTest.contientUneSubvention());
    }

    @Test(timeout = 80)
    public void tourneDroiteTest() {
        Case ca = new Case(new CaseGeometry(true, false, false, true));
        ca.tourneDroite();
        int j = 0;
        boolean c[] = new boolean[] { true, true, false, false };
        assertEquals(c.length, ca.getChemins().length);
        for (boolean i : c) assertEquals(i, ca.getChemins()[j++]);
    }

    @Test(timeout = 80)
    public void tourneGaucheTest() {
        Case ca = new Case(new CaseGeometry(true, false, false, true));
        ca.tourneGauche();
        int j = 0;
        boolean c[] = new boolean[] { false, false, true, true };
        assertEquals(c.length, ca.getChemins().length);
        for (boolean i : c) assertEquals(i, ca.getChemins()[j++]);
    }
}
