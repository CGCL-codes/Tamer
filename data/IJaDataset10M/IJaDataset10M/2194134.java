package trucotdd;

import static org.junit.Assert.*;
import org.junit.Test;
import trucotdd.carta.Carta;
import trucotdd.carta.Naipe;
import trucotdd.carta.Valor;

public class TesteMao {

    @Test(expected = IllegalArgumentException.class)
    public void testeMaoConstrutorInvalido() {
        Carta c1 = new Carta(Naipe.OUROS, Valor.AS);
        Carta c2 = new Carta(Naipe.OUROS, Valor.TRES);
        Carta c3 = null;
        assertTrue(new Mao(c1, c2, c3).isValida());
    }

    @Test
    public void testeMaoConstrutorValido() {
        Carta c1 = new Carta(Naipe.OUROS, Valor.AS);
        Carta c2 = new Carta(Naipe.OUROS, Valor.TRES);
        Carta c3 = new Carta(Naipe.OUROS, Valor.QUATRO);
        assertTrue(new Mao(c1, c2, c3).isValida());
    }

    @Test
    public void testeMaoValida() {
        Carta c1 = new Carta(Naipe.OUROS, Valor.AS);
        Carta c2 = new Carta(Naipe.OUROS, Valor.DOIS);
        Carta c3 = new Carta(Naipe.OUROS, Valor.TRES);
        assertTrue(new Mao(c1, c2, c3).isValida());
    }

    @Test
    public void testeMaoInvalida() {
        Carta c1 = new Carta(Naipe.OUROS, Valor.AS);
        Carta c2 = new Carta(Naipe.OUROS, Valor.AS);
        Carta c3 = new Carta(Naipe.OUROS, Valor.TRES);
        assertFalse(new Mao(c1, c2, c3).isValida());
    }
}
