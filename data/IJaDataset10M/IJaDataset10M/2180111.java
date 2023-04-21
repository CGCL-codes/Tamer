package test.com.gestioni.adoc.aps.system.services.azioni;

import java.util.List;
import test.com.gestioni.adoc.aps.JcrRepositoryBaseTestCase;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.gestioni.adoc.aps.system.services.azioni.Azione;
import com.gestioni.adoc.aps.system.services.azioni.IAzioneManager;

public class TestAzioneManager extends JcrRepositoryBaseTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.init();
        List<Azione> azioni = (List<Azione>) this._azioneManager.getList();
        assertEquals(15, azioni.size());
    }

    public void testInit() {
        assertNotNull(this._azioneManager);
    }

    public void testList() throws ApsSystemException {
        List<Azione> azioni = _azioneManager.getList();
        assertEquals(15, azioni.size());
        assertEquals(1, azioni.get(0).getId());
        assertEquals(IAzioneManager.AZIONE_INSERT, azioni.get(0).getDescr());
        assertEquals("fascicolo", azioni.get(0).getOggetto());
    }

    public void testGetAzione() throws ApsSystemException {
        Azione azione = this._azioneManager.getAzione(5);
        assertEquals("documento", azione.getOggetto());
        assertEquals(IAzioneManager.AZIONE_DELETE, azione.getDescr());
    }

    public void testCRUD() throws Throwable {
        Azione testAzione = new Azione();
        testAzione.setDescr("testDescr");
        testAzione.setOggetto("fascicolo");
        this._azioneManager.add(testAzione);
        List<Azione> azioni = (List<Azione>) this._azioneManager.getList();
        assertEquals(16, azioni.size());
        Azione azione = this._azioneManager.getAzione(testAzione.getId());
        assertNotNull(azione);
        assertEquals(testAzione.getDescr(), azione.getDescr());
        assertEquals(testAzione.getOggetto(), azione.getOggetto());
        assertTrue(testAzione.getId() > 0);
        azione.setDescr("MODIFICA");
        azione.setOggetto("MODIFICA");
        this._azioneManager.update(azione);
        azioni = (List<Azione>) this._azioneManager.getList();
        assertEquals(16, azioni.size());
        Azione updated = this._azioneManager.getAzione(azione.getId());
        assertNotNull(updated);
        assertEquals(azione.getDescr(), updated.getDescr());
        assertEquals(azione.getOggetto(), updated.getOggetto());
        this._azioneManager.delete(updated.getId());
        azioni = (List<Azione>) this._azioneManager.getList();
        assertEquals(15, azioni.size());
    }

    private void init() {
        _azioneManager = (IAzioneManager) this.getService(AdocSystemConstants.AZIONE_MANAGER);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private IAzioneManager _azioneManager;
}
