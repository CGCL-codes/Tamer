package q10.unittests;

import junit.framework.TestCase;
import q10.Arbori.*;
import q10.Functii.FunctiiElementare.*;

public class RadicalTest extends TestCase {

    Nod n;

    Nod nec = new Necunoscuta();

    protected void setUp() throws Exception {
        n = new Radical();
        n.setfStang(nec);
    }

    protected void tearDown() throws Exception {
        n = null;
    }

    public void testCalcul() {
        double rez = Math.sqrt(this.n.getfStang().calcul());
        assertEquals(rez, this.n.calcul());
    }

    public void testDerivare() {
        String rez = "1/2*sqrt(" + this.n.getfStang() + ")";
        assertEquals(rez, this.n.derivare());
    }

    public void testToString() {
        String rez = "sqrt(" + this.n.getfStang() + ")";
        assertEquals(rez, this.n.toString());
    }

    public void testConcatTermeni() {
        String rez = "sqrt(" + this.n.getfStang() + ")";
        assertEquals(rez, this.n.toString());
    }
}
