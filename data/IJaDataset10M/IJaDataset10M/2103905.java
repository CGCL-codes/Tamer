package test.com.gestioni.adoc.aps.system.services.rubrica.nazione;

import test.com.gestioni.adoc.aps.AdocBaseTestCase;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.gestioni.adoc.aps.system.services.rubrica.nazione.INazioneManager;
import com.gestioni.adoc.aps.system.services.rubrica.nazione.Nazione;

public class TestNazioneManager extends AdocBaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
        assertNotNull(_nazioneManager);
        assertEquals(2, this._nazioneManager.getNazioni().size());
    }

    public void testAddUpdateDelete() throws Throwable {
        Nazione nazione = new Nazione();
        try {
            nazione.setDescr("Saint Kitts and Nevis");
            nazione.setId("kn");
            this._nazioneManager.addNazione(nazione);
            assertEquals(3, _nazioneManager.getNazioni().size());
            assertNotNull(this._nazioneManager.getNazione("kn"));
            assertEquals("Saint Kitts and Nevis", this._nazioneManager.getNazione("kn").getDescr());
            Nazione stKitts = this._nazioneManager.getNazione("kn");
            stKitts.setDescr("Fiji");
            this._nazioneManager.updateNazione(stKitts);
            assertEquals(3, _nazioneManager.getNazioni().size());
            assertNotNull(this._nazioneManager.getNazione("kn"));
            assertEquals("Fiji", this._nazioneManager.getNazione("kn").getDescr());
            this._nazioneManager.deleteNazione("kn");
            assertEquals(2, _nazioneManager.getNazioni().size());
            assertNull(this._nazioneManager.getNazione("kn"));
        } finally {
            this._nazioneManager.deleteNazione("kn");
        }
    }

    private void init() {
        this._nazioneManager = (INazioneManager) this.getService(AdocSystemConstants.NAZIONE_MANAGER);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private INazioneManager _nazioneManager;
}
