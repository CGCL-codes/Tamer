package test.com.gestioni.adoc.aps.system.services.fascicolo.inoltro;

import java.util.List;
import test.com.gestioni.adoc.aps.JcrRepositoryBaseTestCase;
import test.com.gestioni.adoc.aps.system.services.fascicolo.TestFascicoloSearchHelper;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.gestioni.adoc.aps.system.services.azioni.IAzioneManager;
import com.gestioni.adoc.aps.system.services.fascicolo.Fascicolo;
import com.gestioni.adoc.aps.system.services.fascicolo.inoltro.IInoltroFascicoloManager;
import com.gestioni.adoc.aps.system.services.fascicolo.inoltro.InoltroFascicolo;
import com.gestioni.adoc.aps.system.services.fascicolo.inoltro.InoltroFascicoloManager;
import com.gestioni.adoc.aps.system.services.inoltro.IInoltroManager;
import com.gestioni.adoc.aps.system.utils.AdocLogger;

public class TestInoltroFascicoloManager extends JcrRepositoryBaseTestCase {

    public void testCrudInoltroFascicolo() throws Throwable {
        Fascicolo fascicolo = this.createAndInsertFascicoloTest("2.03.02-F03-01-07", ADD_051_FORMAZ);
        assertNotNull(fascicolo);
        String fascicoloId = fascicolo.getCode();
        InoltroFascicolo inoltroFascicolo = this.createInoltroFascicolo(fascicoloId, ADD_GIALLI_FORMAZ, AdocSystemConstants.INOLTRO_TIPOLOGIA_COMPETENZA, IInoltroFascicoloManager.TIPO_DIRETTO, ADD_MERCURIO_SEG_DIR);
        this._inoltroManager.addInoltro(inoltroFascicolo, ADD_GIALLI_FORMAZ);
        List<Integer> listInoltri = this.loadFascicoliLavorazione(ADD_MERCURIO_SEG_DIR);
        assertEquals(1, listInoltri.size());
        int code = listInoltri.get(0);
        InoltroFascicolo loadedInoltro = this._inoltroFascicoloManager.getInoltro(code);
        assertEquals(inoltroFascicolo.getFascicoloId(), loadedInoltro.getFascicoloId());
        assertEquals(inoltroFascicolo.getNote(), loadedInoltro.getNote());
        assertEquals(inoltroFascicolo.getSender(), loadedInoltro.getSender());
        assertEquals(ADD_MERCURIO_SEG_DIR, loadedInoltro.getRecipient());
        assertEquals(inoltroFascicolo.getTipologia(), loadedInoltro.getTipologia());
        assertEquals(inoltroFascicolo.getDatainoltro(), loadedInoltro.getDatainoltro());
        this._inoltroFascicoloManager.updateStatus(loadedInoltro.getId(), IInoltroManager.STATUS_ARCHIVIATO, ADD_MERCURIO_SEG_DIR);
        ((InoltroFascicoloManager) this._inoltroFascicoloManager).deleteInoltro(code);
        this.checkAzioniFascicolo(fascicolo, new String[] { IAzioneManager.AZIONE_INSERT, IAzioneManager.AZIONE_INOLTRO_UTENTE });
        listInoltri = this._inoltroFascicoloManager.getInoltriByProfilo(ADD_MERCURIO_SEG_DIR, null, false);
        assertTrue(listInoltri.isEmpty());
    }

    public void testCrudInoltroFascicoloUo() throws Throwable {
        Fascicolo fascicolo = this.createAndInsertFascicoloTest("2.03.02-F03-01-07", ADD_051_FORMAZ);
        assertNotNull(fascicolo);
        String fascicoloId = fascicolo.getCode();
        InoltroFascicolo inoltroFascicolo = this.createInoltroFascicolo(fascicoloId, ADD_GIALLI_FORMAZ, AdocSystemConstants.INOLTRO_TIPOLOGIA_COMPETENZA, IInoltroFascicoloManager.TIPO_UO, UUOOO_SEGRETERIA_DIR);
        this._inoltroManager.addInoltro(inoltroFascicolo, ADD_GIALLI_FORMAZ);
        List<Integer> listInoltriUo = this._inoltroFascicoloManager.getInoltriByUo(UUOOO_SEGRETERIA_DIR, false);
        assertEquals(1, listInoltriUo.size());
        List<Integer> listInoltriUo2 = this._inoltroFascicoloManager.getInoltriByProfilo(ADD_MERCURIO_SEG_DIR, IInoltroFascicoloManager.TIPO_UO, false);
        assertEquals(1, listInoltriUo2.size());
        List<Integer> listInoltriUo3 = this._inoltroFascicoloManager.getInoltriByProfilo(ADD_MERCURIO_SEG_DIR, IInoltroFascicoloManager.TIPO_DIRETTO, false);
        assertEquals(0, listInoltriUo3.size());
        List<Integer> listInoltriUo4 = this._inoltroFascicoloManager.getInoltriByProfilo(ADD_MERCURIO_SEG_DIR, null, false);
        assertEquals(1, listInoltriUo4.size());
        int inoltroId = listInoltriUo.get(0);
        InoltroFascicolo loadedInoltro = this._inoltroFascicoloManager.getInoltro(inoltroId);
        assertEquals(inoltroFascicolo.getFascicoloId(), loadedInoltro.getFascicoloId());
        assertEquals(inoltroFascicolo.getNote(), loadedInoltro.getNote());
        assertEquals(inoltroFascicolo.getSender(), loadedInoltro.getSender());
        assertEquals(UUOOO_SEGRETERIA_DIR, loadedInoltro.getRecipientUo());
        assertEquals(inoltroFascicolo.getTipologia(), loadedInoltro.getTipologia());
        assertEquals(inoltroFascicolo.getDatainoltro(), loadedInoltro.getDatainoltro());
        this.checkAzioniFascicolo(fascicolo, new String[] { IAzioneManager.AZIONE_INSERT, IAzioneManager.AZIONE_INOLTRO_UO });
        ((InoltroFascicoloManager) this._inoltroFascicoloManager).deleteInoltro(inoltroId);
        listInoltriUo = this._inoltroFascicoloManager.getInoltriByUo(UUOOO_SEGRETERIA_DIR, false);
        assertTrue(listInoltriUo.isEmpty());
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            TestFascicoloSearchHelper searcherBean = new TestFascicoloSearchHelper();
            searcherBean.setDescrizione("DELETE_");
            List<String> fascicoli = this._fascicoloManager.searchFascicoli(searcherBean);
            AdocLogger.logDebug("TROVATI " + fascicoli.size() + " DA CANCELLARE", this);
            for (int i = 0; i < fascicoli.size(); i++) {
                Fascicolo fsc = this._fascicoloManager.getFascicolo(fascicoli.get(i));
                this._fascicoloManager.delete(fsc.getCode(), fsc.getProfilo());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            super.tearDown();
        }
    }
}
