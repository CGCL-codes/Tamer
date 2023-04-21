package moteur;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.*;

public class CaseGeometryCTest {

    private CaseGeometryC caseGeom;

    @Before
    public void Init() {
        caseGeom = new CaseGeometryC();
    }

    @Test
    public void CreateTest() {
        CaseGeometry c = new CaseGeometry(true, true, false, false);
        assertTrue(c.equals(caseGeom));
    }

    @Test
    public void getCheminsTest() {
        int j = 0;
        boolean c[] = new boolean[] { true, true, false, false };
        assertEquals(c.length, caseGeom.getChemins().length);
        for (boolean i : c) assertEquals(i, caseGeom.getChemins()[j++]);
    }

    @Test
    public void rotate1Test() {
        int j = 0;
        boolean c[] = new boolean[] { true, true, false, false };
        assertEquals(c.length, caseGeom.getChemins().length);
        for (boolean i : c) assertEquals(i, caseGeom.getChemins()[j++]);
    }

    public void rotate2Test() {
        int j = 0;
        boolean c[] = new boolean[] { false, true, true, false };
        assertEquals(c.length, caseGeom.getChemins().length);
        for (boolean i : c) assertEquals(i, caseGeom.getChemins()[j++]);
    }

    public void rotate3Test() {
        int j = 0;
        boolean c[] = new boolean[] { false, false, true, true };
        assertEquals(c.length, caseGeom.getChemins().length);
        for (boolean i : c) assertEquals(i, caseGeom.getChemins()[j++]);
    }

    public void rotate4Test() {
        int j = 0;
        boolean c[] = new boolean[] { true, false, false, true };
        assertEquals(c.length, caseGeom.getChemins().length);
        for (boolean i : c) assertEquals(i, caseGeom.getChemins()[j++]);
    }

    @Test
    public void rotate0Test() {
        caseGeom.rotate(0);
        rotate1Test();
    }

    @Test
    public void rotatePositive1Test() {
        caseGeom.rotate(1);
        rotate2Test();
    }

    @Test
    public void rotatePositive2Test() {
        caseGeom.rotate(2);
        rotate3Test();
    }

    @Test
    public void rotatePositive3Test() {
        caseGeom.rotate(3);
        rotate4Test();
    }

    @Test
    public void rotatePositive4Test() {
        caseGeom.rotate(4);
        rotate1Test();
    }

    @Test
    public void rotateNegative1Test() {
        caseGeom.rotate(-1);
        rotate4Test();
    }

    @Test
    public void rotateNegative2Test() {
        caseGeom.rotate(-2);
        rotate3Test();
    }

    @Test
    public void rotateNegative3Test() {
        caseGeom.rotate(-3);
        rotate2Test();
    }

    @Test
    public void rotateNegative4Test() {
        caseGeom.rotate(-4);
        rotate0Test();
    }
}
