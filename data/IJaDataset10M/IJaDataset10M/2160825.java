package caraacara;

import junit.framework.*;

/**
 *
 * @author HP
 */
public class CartasSecretasTest extends TestCase {

    public CartasSecretasTest(String testName) {
        super(testName);
    }

    /**
     * Test of verificaPalpiteRecebido method, of class caraacara.CartasSecretas.
     */
    public void testVerificaPalpiteRecebido() {
        System.out.println("verificaPalpiteRecebido");
        CartasSecretas palpite = new CartasSecretas(3);
        palpite.addCarta(new Carta(12));
        palpite.addCarta(new Carta(7));
        palpite.addCarta(new Carta(23));
        CartasSecretas instance = new CartasSecretas(3);
        instance.addCarta(new Carta(23));
        instance.addCarta(new Carta(7));
        instance.addCarta(new Carta(12));
        boolean expResult = true;
        boolean result = instance.verificaPalpiteRecebido(palpite);
        assertEquals(expResult, result);
        palpite = new CartasSecretas(3);
        palpite.addCarta(new Carta(12));
        palpite.addCarta(new Carta(7));
        palpite.addCarta(new Carta(23));
        instance = new CartasSecretas(3);
        instance.addCarta(new Carta(23));
        instance.addCarta(new Carta(11));
        instance.addCarta(new Carta(12));
        expResult = false;
        result = instance.verificaPalpiteRecebido(palpite);
        assertEquals(expResult, result);
    }
}
