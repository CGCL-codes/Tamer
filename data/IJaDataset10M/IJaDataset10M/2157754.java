package jbced.citadellesspring.modele.quartiers.impl;

import static org.junit.Assert.*;
import jbced.citadellesspring.modele.quartiers.QuartierEnum;
import org.junit.Test;

public class QuartierTest {

    @Test
    public void tousLesQuartiersOntUneCouleurUnNomUnCoutDeConstructionEtUneValeur() throws Exception {
        for (final QuartierEnum q : QuartierEnum.values()) {
            assertNotNull(q.getCouleur());
            assertNotNull(q.name());
            assertTrue(q.getCoutConstruction() > 0);
            assertTrue(q.getValeur() > 0);
        }
    }
}
