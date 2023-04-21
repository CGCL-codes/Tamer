package p1.aplic.cartastestes;

import junit.framework.*;
import p1.aplic.cartas.*;

/**
 * Testes da classe CartaPoquer.
 *
 */
public class TestaCartaPoquer extends TestCase {

    protected CartaPoquer asPaus;

    protected CartaPoquer seisPaus;

    protected CartaPoquer reiPaus;

    protected CartaPoquer menorCarta;

    protected CartaPoquer maiorCarta;

    public TestaCartaPoquer(String name) {
        super(name);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
        asPaus = new CartaPoquer(CartaPoquer.AS, CartaPoquer.PAUS);
        seisPaus = new CartaPoquer(6, CartaPoquer.PAUS);
        reiPaus = new CartaPoquer(CartaPoquer.REI, CartaPoquer.PAUS);
        menorCarta = new CartaPoquer(CartaPoquer.menorValor(), Carta.PAUS);
        maiorCarta = new CartaPoquer(CartaPoquer.maiorValor(), Carta.PAUS);
    }

    public static Test suite() {
        return new TestSuite(TestaCartaPoquer.class);
    }

    public void testMenor() {
        assertEquals(seisPaus, menorCarta);
    }

    public void testMaior() {
        assertEquals(asPaus, maiorCarta);
    }

    public void testCompareTo() {
        assertTrue("1", asPaus.compareTo(reiPaus) > 0);
        assertTrue("2", reiPaus.compareTo(asPaus) < 0);
    }

    public void testToString() {
        assertEquals("AS de PAUS", asPaus.toString());
    }
}
