package test.org.magicbox.domain;

import junit.framework.TestCase;
import org.magicbox.domain.UtenteUltraLightImpl;
import org.magicbox.domain.UtenteUltraLight;

public class UtenteUltraLightTest extends TestCase {

    public void setUp() throws Exception {
    }

    public void tearDown() throws Exception {
    }

    public void testCostruttoreVuoto() {
        UtenteUltraLight utente = new UtenteUltraLightImpl();
        assertTrue(utente.getId() == -1);
        assertTrue(-1 == utente.getIdGruppo());
        assertNull(utente.getNominativo());
    }

    public void testCostruttorePieno() {
        UtenteUltraLight utente = new UtenteUltraLightImpl("paolino paperino", 1, 2);
        assertEquals(2, utente.getId());
        assertEquals(1, utente.getIdGruppo());
        assertEquals("paolino paperino", utente.getNominativo());
    }
}
