package br.ita.trucocearense.server.core.teste;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import br.ita.trucocearense.server.core.Carta;
import br.ita.trucocearense.server.core.DeckCartas;

public class TesteDeckCartas {

    @Test
    public void TesteGetManilha() {
        DeckCartas deckcartas = new DeckCartas();
        Carta manilha = deckcartas.getManilha();
        Assert.assertNotNull(manilha);
        Assert.assertFalse(manilha.isEspiao());
    }

    @Test
    public void TesteGetCartas() {
        DeckCartas deckcartas = new DeckCartas();
        Carta manilha = deckcartas.getManilha();
        Set<Carta> cartas = new HashSet<Carta>();
        for (int nUsuario = 0; nUsuario < 4; nUsuario++) {
            Carta[] cartasUsuario = deckcartas.getCartas(manilha);
            Assert.assertEquals(3, cartasUsuario.length);
            for (int i = 0; i < 3; i++) {
                Assert.assertFalse(cartas.contains(cartasUsuario[i]));
                cartas.add(cartasUsuario[i]);
            }
        }
    }
}
