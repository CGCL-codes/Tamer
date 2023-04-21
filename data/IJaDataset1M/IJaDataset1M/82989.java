package Testing;

import junit.framework.*;
import Domini.tipusEspai;

/**
 * Prova dels m�todes de tipus espai
 * <p>T�tol: Projecte PP</p>
 * <p>Descripci�: Projecte PP primavera 2002</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Cl�ster 3</p>
 * @author Frederic P�rez Ordeig
 * @version 0.01
 */
public class TesttipusEspai extends TestCase {

    public TesttipusEspai(String s) {
        super(s);
    }

    tipusEspai tipusespai;

    protected void setUp() {
        try {
            String val1 = "STRING0";
            tipusespai = new tipusEspai(val1);
        } catch (Exception e) {
            System.err.println("Exception thrown:  " + e);
            fail();
        }
    }

    protected void tearDown() {
    }

    public void testSetTipusEspai() {
        String p_szTipusEspai1 = "STRING0";
        try {
            tipusespai.setTipusEspai(p_szTipusEspai1);
            assertTrue(tipusespai.getTipusEspai().equals(p_szTipusEspai1));
        } catch (Exception e) {
            System.err.println("Exception thrown:  " + e);
            fail();
        }
    }
}
