package test.com.gestioni.adoc.apsadmin;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.ArrayUtils;
import test.com.agiletec.ConfigTestUtils;
import test.com.gestioni.adoc.AdocRepositoryConfigTestUtils;
import test.com.gestioni.adoc.aps.AdocBaseTestCase;
import test.com.gestioni.adoc.aps.system.services.documento.DocumentoTestHelper;
import test.com.gestioni.adoc.aps.system.services.documento.personale.helper.DocumentoPersonaleTestHelper;
import test.com.gestioni.adoc.aps.system.services.fascicolo.TestFascicoloSearchHelper;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.util.DateConverter;
import com.gestioni.adoc.aps.system.AdocSystemConstants;
import com.gestioni.adoc.aps.system.services.aoo.IAooManager;
import com.gestioni.adoc.aps.system.services.azioni.Azione;
import com.gestioni.adoc.aps.system.services.azioni.IAzioneManager;
import com.gestioni.adoc.aps.system.services.azioni.IAzioneOggettoManager;
import com.gestioni.adoc.aps.system.services.documento.IDocumentoManager;
import com.gestioni.adoc.aps.system.services.documento.authorization.IDocumentoAuthorizationManager;
import com.gestioni.adoc.aps.system.services.documento.azioni.AzioneDocumento;
import com.gestioni.adoc.aps.system.services.documento.inoltro.AbstractInoltroDocumento;
import com.gestioni.adoc.aps.system.services.documento.inoltro.IInoltroDocumentoManager;
import com.gestioni.adoc.aps.system.services.documento.inoltro.InoltroDocumento;
import com.gestioni.adoc.aps.system.services.documento.inoltro.InoltroDocumentoUo;
import com.gestioni.adoc.aps.system.services.documento.model.Documento;
import com.gestioni.adoc.aps.system.services.documento.personale.DocumentoPersonale;
import com.gestioni.adoc.aps.system.services.documento.personale.IDocumentoPersonaleManager;
import com.gestioni.adoc.aps.system.services.ente.IEnteManager;
import com.gestioni.adoc.aps.system.services.fascicolo.Fascicolo;
import com.gestioni.adoc.aps.system.services.fascicolo.IFascicoloManager;
import com.gestioni.adoc.aps.system.services.fascicolo.azioni.AzioneFascicolo;
import com.gestioni.adoc.aps.system.services.fascicolo.inoltro.IInoltroFascicoloManager;
import com.gestioni.adoc.aps.system.services.fascicolo.inoltro.InoltroFascicolo;
import com.gestioni.adoc.aps.system.services.inoltro.IInoltroManager;
import com.gestioni.adoc.aps.system.services.inoltro.InoltroInterface;
import com.gestioni.adoc.aps.system.services.pec.IPecManagerOUT;
import com.gestioni.adoc.aps.system.services.profilo.IProfiloManager;
import com.gestioni.adoc.aps.system.services.profilo.Profilo;
import com.gestioni.adoc.aps.system.services.protocollo.IProtocolloManager;
import com.gestioni.adoc.aps.system.services.protocollo.IProtocolloUscitaManager;
import com.gestioni.adoc.aps.system.services.protocollo.ProtocolloManager;
import com.gestioni.adoc.aps.system.services.protocollo.azioni.AzioneProtocollo;
import com.gestioni.adoc.aps.system.services.protocollo.model.Protocollo;
import com.gestioni.adoc.aps.system.services.protocollo.model.SmallContatto;
import com.gestioni.adoc.aps.system.services.protocollo.numeratore.Numeratore;
import com.gestioni.adoc.aps.system.services.repository.JcrRepositoryBaseConnection;
import com.gestioni.adoc.aps.system.services.titolario.ITitolarioManager;
import com.gestioni.adoc.aps.system.services.titolario.NodoTitolario;
import com.gestioni.adoc.aps.system.services.titolario.TitolarioManager;
import com.gestioni.adoc.aps.system.services.uo.IUnitaOrganizzativaManager;
import com.gestioni.adoc.aps.system.utils.AdocLogger;

public class JcrRepositoryAdocApsAdminBaseTestCase extends AdocApsAdminBaseTestCase {

    @Override
    protected ConfigTestUtils getConfigUtils() {
        return new AdocRepositoryConfigTestUtils();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        _repositoryImpl = (JcrRepositoryBaseConnection) this.getApplicationContext().getBean("JcrRepositoryBaseConnection");
        _documentoManager = (IDocumentoManager) this.getService(AdocSystemConstants.DOCUMENTO_MANAGER);
        _userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
        _profiloManager = (IProfiloManager) this.getService(AdocSystemConstants.PROFILO_MANAGER);
        _fascicoloManager = (IFascicoloManager) this.getService(AdocSystemConstants.FASCICOLO_MANAGER);
        _inoltroDocumentoManager = (IInoltroDocumentoManager) this.getService(AdocSystemConstants.INOLTRO_DOCUMENTO_MANAGER);
        _documentoAuthorizationManager = (IDocumentoAuthorizationManager) this.getService(AdocSystemConstants.DOCUMENTO_AUTH_MANAGER);
        _protocolloManager = (IProtocolloManager) this.getService(AdocSystemConstants.PROTOCOLLO_MANAGER);
        _helper = new DocumentoTestHelper(_documentoManager);
        _enteManager = (IEnteManager) this.getService(AdocSystemConstants.ENTE_MANAGER);
        _aooManager = (IAooManager) this.getService(AdocSystemConstants.AOOMANAGER);
        _azioneDocumentoManager = (IAzioneOggettoManager) this.getService(AdocSystemConstants.AZIONE_DOCUMENTO_MANAGER);
        _azioneFascicoloManager = (IAzioneOggettoManager) this.getService(AdocSystemConstants.AZIONE_FASCICOLO_MANAGER);
        _azioneProtocolloManager = (IAzioneOggettoManager) this.getService(AdocSystemConstants.AZIONE_PROTOCOLLO_MANAGER);
        _azioneManager = (IAzioneManager) this.getService(AdocSystemConstants.AZIONE_MANAGER);
        _inoltroManager = (IInoltroManager) this.getService(AdocSystemConstants.INOLTRO_MANAGER);
        _unitaOrganizzativaManager = (IUnitaOrganizzativaManager) this.getService(AdocSystemConstants.UNITA_ORGANIZZATIVA_MANAGER);
        _inoltroFascicoloManager = (IInoltroFascicoloManager) this.getService(AdocSystemConstants.INOLTRO_FASCICOLO_MANAGER);
        _titolarioManager = (ITitolarioManager) this.getService(AdocSystemConstants.TITOLARIO_MANAGER);
        _pecManagerOUT = (IPecManagerOUT) this.getService(AdocSystemConstants.PEC_MANAGER_OUT);
        _documentoPersonaleManager = (IDocumentoPersonaleManager) this.getService(AdocSystemConstants.DOCUMENTO_PERSONALE_MANAGER);
        assertTrue("NUMERATORE", this.checkAndCreateNumeratore());
    }

    private boolean checkAndCreateNumeratore() throws Exception {
        try {
            String anno = "2010";
            String ente = this._enteManager.getCurrentEnte().getCode();
            String aoo = this._aooManager.getCurrentAoo().getCode();
            Numeratore numeratore = ((ProtocolloManager) this._protocolloManager).getNumeratore(anno, ente, aoo);
            assertNull(numeratore);
            ((ProtocolloManager) this._protocolloManager).createNumeratore(anno, ente, aoo);
            numeratore = ((ProtocolloManager) this._protocolloManager).getNumeratore(anno, ente, aoo);
            this._numeratore = numeratore;
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * Crea un fascicolo nello stesso livello di quello identificato da code
	 * @param code codice di un fascicolo di riferimento,
	 * @param idProfilo
	 * @return
	 * @throws Throwable
	 */
    protected Fascicolo createAndInsertFascicoloTest(String code, int idProfilo) throws Throwable {
        Fascicolo fascicolo = null;
        try {
            Fascicolo ref = this._fascicoloManager.getFascicolo(code);
            fascicolo = new Fascicolo();
            fascicolo.setAutore(this._profiloManager.getProfilo(idProfilo).getUsername());
            fascicolo.setDescrizione("DELETE_" + code);
            fascicolo.setIdNodoPadre(ref.getIdNodoPadre());
            fascicolo.setIdNodoTitolario(ref.getIdNodoTitolario());
            fascicolo.setOggetto(ref.getOggetto());
            fascicolo.setProfilo(idProfilo);
            fascicolo.setStartDate(ref.getStartDate());
            this._fascicoloManager.add(fascicolo, idProfilo);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CREAZIONE FASCICOLO DI TEST", this);
            throw new Throwable();
        }
        return fascicolo;
    }

    /**
	 * Crea un protocollo in uscita
	 * @param oggetto required
	 * @param riservato
	 * @param documento il documento principale
	 * @param idProfilo identificativo del protocollatore
	 * @param modalitaSpedizione la modalita spedizione
	 * @return TRUE SE VA TUTTO OK
	 * @throws ApsSystemException
	 */
    protected boolean createAndInsertProtocolloUscita(String oggetto, boolean riservato, Documento documento, int idProfilo, int modalitaSpedizione) throws ApsSystemException {
        try {
            SmallContatto mittente = new SmallContatto();
            mittente.setCodiceFiscale("Mittente CF " + oggetto);
            mittente.setCognome("Mittente Cognome");
            mittente.setNome("Mittente Nome");
            mittente.setIndirizzo("Mittente indirizzo");
            mittente.setNazione("Mittente nazione");
            mittente.setPiva("Mittente piva");
            mittente.setRagioneSociale("Mittente ragioneSociale");
            mittente.setEmail("stefanopuddu@gmail.com");
            mittente.setModalitaSpedizione(modalitaSpedizione);
            Protocollo protocollo = new Protocollo();
            protocollo = this._protocolloManager.getProtocolloType();
            String anno = DateConverter.getFormattedDate(new Date(), "yyyy");
            String aooId = this._aooManager.getCurrentAoo().getCode();
            String enteId = this._enteManager.getCurrentEnte().getCode();
            protocollo.setSenso(Protocollo.SENSO_OUT);
            protocollo.setAnno(anno);
            protocollo.setEnteId(enteId);
            protocollo.setAooId(aooId);
            protocollo.setOggetto(oggetto);
            protocollo.setRiservato(riservato);
            protocollo.setMittente(mittente);
            protocollo.setDocumento(documento);
            protocollo.setIdProtocollatore(idProfilo);
            ((IProtocolloUscitaManager) this._protocolloManager).addProtocolloUscita(protocollo, idProfilo);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CREAZIONE PROTOCOLLO IN USCITA: " + oggetto, this);
            return false;
        }
        return true;
    }

    protected SmallContatto createProtDestinatario(String protId, int modalitaSpedizione) {
        SmallContatto destintatario = new SmallContatto();
        destintatario.setCodiceFiscale("Destinatario CF " + protId);
        destintatario.setCognome("Destinatario Cognome " + protId);
        destintatario.setNome("Destinatario Nome " + protId);
        destintatario.setIndirizzo("Destinatario indirizzo " + protId);
        destintatario.setNazione("Destinatario nazione " + protId);
        destintatario.setPiva("Destinatario piva " + protId);
        destintatario.setRagioneSociale("Destinatario ragioneSociale " + protId);
        destintatario.setEmail("stefanopuddu@gmail.com");
        destintatario.setModalitaSpedizione(modalitaSpedizione);
        return destintatario;
    }

    /**
	 * Crea un protocollo in ingresso SENZA IL DOC PRINCIPALE, senza inserirlo
	 * @param oggetto
	 * @param riservato
	 * @param documento
	 * @param idProfilo
	 * @param modalitaSpedizione valida sia per il mittente che per il destinatario
	 * @return
	 * @throws ApsSystemException
	 */
    protected Protocollo createProtocolloIngressoNoDoc(String oggetto, boolean riservato, int idProfilo, int modalitaSpedizione) throws ApsSystemException {
        Protocollo protocollo = null;
        try {
            SmallContatto mittente = new SmallContatto();
            mittente.setCodiceFiscale("Mittente CF " + oggetto);
            mittente.setCognome("Mittente Cognome " + oggetto);
            mittente.setNome("Mittente Nome " + oggetto);
            mittente.setIndirizzo("Mittente indirizzo " + oggetto);
            mittente.setNazione("Mittente nazione " + oggetto);
            mittente.setPiva("Mittente piva " + oggetto);
            mittente.setRagioneSociale("Mittente ragioneSociale " + oggetto);
            mittente.setEmail("stefanopuddu@gmail.com " + oggetto);
            SmallContatto destinatario = new SmallContatto();
            destinatario.setCodiceFiscale("Destinatario CF " + oggetto);
            destinatario.setCognome("Destinatario Cognome " + oggetto);
            destinatario.setNome("Destinatario Nome " + oggetto);
            destinatario.setIndirizzo("Destinatarioindirizzo " + oggetto);
            destinatario.setNazione("Destinatario nazione " + oggetto);
            destinatario.setPiva("Destinatario piva " + oggetto);
            destinatario.setRagioneSociale("Destinatario ragioneSociale " + oggetto);
            destinatario.setEmail("stefanopuddu@gmail.com");
            destinatario.setModalitaSpedizione(modalitaSpedizione);
            protocollo = new Protocollo();
            protocollo = this._protocolloManager.getProtocolloType();
            String anno = DateConverter.getFormattedDate(new Date(), "yyyy");
            String aooId = this._aooManager.getCurrentAoo().getCode();
            String enteId = this._enteManager.getCurrentEnte().getCode();
            protocollo.setSenso(Protocollo.SENSO_IN);
            protocollo.setAnno(anno);
            protocollo.setEnteId(enteId);
            protocollo.setAooId(aooId);
            protocollo.setOggetto(oggetto);
            protocollo.setRiservato(riservato);
            protocollo.setMittente(mittente);
            protocollo.setDestinatari(new ArrayList<SmallContatto>());
            protocollo.getDestinatari().add(destinatario);
            protocollo.setIdProtocollatore(idProfilo);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CREAZIONE PROTOCOLLO IN INGRESSO: " + oggetto, this);
            return null;
        }
        return protocollo;
    }

    protected Protocollo createProtocolloIngressoWithDOC(String oggetto, boolean riservato, int idProfilo, int modalitaSpedizione, Documento doc) throws ApsSystemException {
        Protocollo protocollo = null;
        try {
            protocollo = this.createProtocolloIngressoNoDoc(oggetto, riservato, idProfilo, modalitaSpedizione);
            protocollo.setDocumento(doc);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CREAZIONE PROTOCOLLO IN INGRESSO: " + oggetto, this);
            return null;
        }
        return protocollo;
    }

    /**
	 * Aggiunge un documento di TIPO TES
	 * @param titolo required
	 * @param fascicolo required
	 * @param riservato opzionale
	 * @param idProfilo required
	 * @param filePathName TODO
	 * @return
	 */
    protected boolean createAndInsertDocument(String titolo, String fascicolo, boolean riservato, int idProfilo, String filePathName) {
        String fileName = FILE_DOCUMENT_PATHNAME;
        if (filePathName != null) {
            fileName = filePathName;
        }
        try {
            Documento documento = this.createDocument(titolo, riservato, idProfilo);
            Fascicolo fascicoloObj = this._fascicoloManager.getFascicolo(fascicolo);
            if (null == fascicoloObj) {
                AdocLogger.logDebug("NESSUN FASCICOLO PER IL DOC " + titolo, this);
                return false;
            }
            List<String> fascicoli = new ArrayList<String>();
            fascicoli.add(fascicolo);
            documento.setFascicoli(fascicoli);
            File file = new File(fileName);
            Profilo profilo = this._profiloManager.getProfilo(idProfilo);
            this._documentoManager.addDocument(documento, file, file.getName(), profilo);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CREAZIONE DOCUMENTO " + titolo, this);
            return false;
        }
        return true;
    }

    /**
	 * Crea un documento di tipo TES, SENZA FASCICOLO E SENZA FILE...
	 * @param titolo
	 * @param riservato
	 * @param idProfilo
	 * @return
	 */
    protected Documento createDocument(String titolo, boolean riservato, int idProfilo) {
        Documento doc = null;
        try {
            AdocLogger.logDebug("Creazione documento " + titolo, this);
            String[] vociTitolario = new String[0];
            String[] fascicoli = new String[0];
            String[] docCollegati = new String[0];
            Map<String, Object> metadata = new HashMap<String, Object>();
            metadata.put("titolo", titolo);
            metadata.put("sunto", titolo);
            metadata.put("dettaglio", titolo);
            Map<String, Object> attrubutes = new HashMap<String, Object>();
            attrubutes.put("Titolo", "titolo " + titolo);
            attrubutes.put("Numero", new BigDecimal(idProfilo));
            fascicoli = new String[0];
            Profilo profilo = this._profiloManager.getProfilo(idProfilo);
            doc = (Documento) _helper.createTestDocument(AdocBaseTestCase.DOCUMENT_TYPE_TES, profilo, metadata, attrubutes, vociTitolario, fascicoli, docCollegati);
            doc.setRiservato(riservato);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CREAZIONE DOCUMENTO " + titolo, this);
            return null;
        }
        return doc;
    }

    protected void fascicolaDocumento(String documentId, int profiloId, String fascicolo) throws Throwable {
        try {
            Documento documento = (Documento) this._documentoManager.getDocument(documentId);
            this._fascicoloManager.addDocumentToFascicolo(documento, fascicolo, profiloId);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN FASCICOLAZIONE DOCUMENTO " + documentId, this);
            throw new Throwable();
        }
    }

    protected void rimuoviFascicoloDocumento(String documentId, int profiloId, String fascicolo) throws Throwable {
        try {
            Documento documento = (Documento) this._documentoManager.getDocument(documentId);
            this._fascicoloManager.removeDocumentFromFascicolo(documento, fascicolo, profiloId);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN RIMOZIONE FASCICOLAZIONE DOCUMENTO " + documentId, this);
            throw new Throwable();
        }
    }

    /**
	 *
	 * @param fascicoloId
	 * @param sender
	 * @param tipologia
	 * @param type required, diretto o uo
	 * @param recipientCode
	 */
    protected void inoltraFascicolo(String fascicoloId, Integer sender, String tipologia, String type, int recipientCode) {
        try {
            InoltroFascicolo inoltro = this.createInoltroFascicolo(fascicoloId, sender, tipologia, type, recipientCode);
            List<InoltroInterface> listaInoltri = new ArrayList<InoltroInterface>();
            listaInoltri.add(inoltro);
            this._inoltroManager.addMassiveInoltri(listaInoltri, sender);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN INOLTRO FASCICOLO " + fascicoloId, this);
        }
    }

    /**
	 * Effettua un inoltro
	 * @param documenntId required
	 * @param sender identificativo del profilo mittente
	 * @param tipologia required, competenza o conoscenza
	 * @param type required, diretto o uo
	 * @param recipientCode identificativo del destinatario (id uo o codice profilo)
	 * @return
	 */
    protected void inoltraDocument(String documentId, Integer sender, String tipologia, String type, int recipientCode) {
        try {
            AbstractInoltroDocumento inoltro = this.createInoltro(documentId, sender, tipologia, type, recipientCode);
            List<InoltroInterface> listaInoltri = new ArrayList<InoltroInterface>();
            listaInoltri.add(inoltro);
            this._inoltroManager.addMassiveInoltri(listaInoltri, sender);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN INOLTRO DOCUMENTO " + documentId, this);
        }
    }

    protected void changeInoltroStatus(int idInoltro, int idProfilo, int newStatus) throws Throwable {
        try {
            InoltroDocumento inoltroDocumento = this._inoltroDocumentoManager.getInoltro(idInoltro);
            this._inoltroDocumentoManager.updateStatusInoltro(inoltroDocumento, newStatus, idProfilo);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CAMBIO STATO INOLTRO PER INOLTRO/PROFILO:" + +idInoltro + "/" + idProfilo, this);
            throw new Throwable();
        }
    }

    /**
	 * Carica i documenti in lavorazione per un profilo
	 * @param idProfilo
	 * @return
	 * @throws Throwable
	 */
    protected List<Integer> loadDocumentiLavorazione(int idProfilo) throws Throwable {
        AdocLogger.logDebug("Ricerca documenti in lavorazione, INOLTRI, per profilo " + idProfilo, this);
        List<Integer> inoltriId = null;
        try {
            inoltriId = this._inoltroDocumentoManager.searchInoltriId(idProfilo, IInoltroManager.STATUS_RICEVUTO, IInoltroManager.STATUS_ARCHIVIATO, this.getYesterday(), null);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CARICAMENTO DOCUMENTI IN LAVORAZIONE PER IL PROFILO " + idProfilo, this);
            throw new Throwable();
        }
        return inoltriId;
    }

    protected List<Integer> loadFascicoliLavorazione(int idProfilo) throws Throwable {
        AdocLogger.logDebug("Ricerca fascicolo in lavorazione, INOLTRI, per profilo " + idProfilo, this);
        List<Integer> inoltriId = null;
        try {
            inoltriId = this._inoltroFascicoloManager.getInoltriByProfilo(idProfilo, null, true);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN CARICAMENTO FASCICOLI IN LAVORAZIONE PER IL PROFILO " + idProfilo, this);
            throw new Throwable();
        }
        return inoltriId;
    }

    /**
	 * Crea un inoltro
	 * @param documentId
	 * @param sender
	 * @param tipologia
	 * @param type
	 * @param recipientCode
	 * @return
	 */
    protected AbstractInoltroDocumento createInoltro(String documentId, int sender, String tipologia, String type, int recipientCode) {
        String note = "inoltro da " + sender + " a " + recipientCode + " per il documento " + documentId;
        if (type.equalsIgnoreCase("diretto")) {
            InoltroDocumento inoltroDocumento = new InoltroDocumento();
            inoltroDocumento.setSender(sender);
            inoltroDocumento.setDocumentId(documentId);
            inoltroDocumento.setNote(note);
            inoltroDocumento.setRecipient(recipientCode);
            inoltroDocumento.setTipologia(tipologia);
            return inoltroDocumento;
        } else if (type.equalsIgnoreCase("uo")) {
            InoltroDocumentoUo inoltroDocumentoUo = new InoltroDocumentoUo();
            inoltroDocumentoUo.setDocumentId(documentId);
            inoltroDocumentoUo.setNote(note);
            inoltroDocumentoUo.setSender(sender);
            inoltroDocumentoUo.setTipologia(tipologia);
            inoltroDocumentoUo.setUo(recipientCode);
            return inoltroDocumentoUo;
        } else {
            AdocLogger.logDebug("ERRORE IN CREAZIONE INOLTRO: TIPO INOLTRO NON RICONOSCIUTO", this);
            throw new UnsupportedOperationException("TIPO INOLTRO NON RICONOSCIUTO");
        }
    }

    /**
	 *
	 * @param fascicoloId
	 * @param sender
	 * @param tipologia
	 * @param tipo
	 * @param recipientCode
	 * @return
	 */
    protected InoltroFascicolo createInoltroFascicolo(String fascicoloId, int sender, String tipologia, String tipo, int recipientCode) {
        String note = "inoltro da " + sender + " a " + recipientCode + " per il fascicolo " + fascicoloId;
        InoltroFascicolo inoltro = new InoltroFascicolo();
        inoltro.setSender(sender);
        inoltro.setFascicoloId(fascicoloId);
        inoltro.setNote(note);
        inoltro.setRecipient(recipientCode);
        inoltro.setTipologia(tipologia);
        inoltro.setTipo(tipo);
        if (tipo.equalsIgnoreCase(IInoltroFascicoloManager.TIPO_DIRETTO)) {
            inoltro.setRecipient(recipientCode);
        } else if (tipo.equalsIgnoreCase(IInoltroFascicoloManager.TIPO_UO)) {
            inoltro.setRecipientUo(recipientCode);
        } else {
            AdocLogger.logDebug("ERRORE IN CREAZIONE INOLTRO FASCICOLO: TIPO INOLTRO NON RICONOSCIUTO", this);
            throw new UnsupportedOperationException("TIPO INOLTRO NON RICONOSCIUTO");
        }
        return inoltro;
    }

    @SuppressWarnings("unchecked")
    protected void compareInoltri(InoltroDocumento expected, InoltroDocumento actual) throws Throwable, InvocationTargetException, NoSuchMethodException {
        Map expectedMap = PropertyUtils.describe(expected);
        Map actualMap = PropertyUtils.describe(actual);
        Iterator it = expectedMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String expectedKey = (String) pairs.getKey();
            Object expectedValue = pairs.getValue();
            Object actualValue = actualMap.get(expectedKey);
            if (expectedKey.equals("qualcosa_da_gestire")) {
                assertEquals(ArrayUtils.toString(expectedValue), ArrayUtils.toString(actualValue));
            } else if (expectedKey.equals("datainoltro")) {
            } else if (expectedKey.equals("titoloDocumento")) {
            } else if (expectedKey.equals("statoDocumento")) {
            } else {
                assertEquals(expectedKey, actualValue, expectedValue);
            }
        }
    }

    protected void compareDocuments(Documento expected, Documento actual, InputStream inputStreamExpected, InputStream inputStreamActual) throws Throwable, InvocationTargetException, NoSuchMethodException {
        this._helper.compareDocuments(expected, actual, inputStreamExpected, inputStreamActual);
    }

    /**
	 * Ricerca documenti per titolo
	 * @param titolo
	 * @param useLikeOption
	 * @param idProfilo null per ricercare tutto
	 * @return
	 */
    protected List<String> searchDocumentByTitolo(String titolo, boolean useLikeOption, Integer idProfilo) {
        List<String> docsId = null;
        Profilo profilo = null;
        try {
            EntitySearchFilter titoloFilter = new EntitySearchFilter(IDocumentoManager.TITOLO_FILTER_KEY, false, titolo, useLikeOption);
            EntitySearchFilter[] filters = { titoloFilter };
            if (null != idProfilo) {
                profilo = this._profiloManager.getProfilo(idProfilo.intValue());
            }
            docsId = this._documentoManager.loadDocumentiId(filters, null, null, profilo);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN RICERCA DOCUMENTI PER TITOLO: " + titolo, this);
        }
        return docsId;
    }

    /**
	 * Ricerca protocolli per titolo
	 * @param titolo
	 * @param useLikeOption
	 * @param idProfilo null per ricercare tutto
	 * @return
	 */
    protected List<String> searchProtocolliByOggetto(String oggetto, boolean useLikeOption) {
        List<String> protocolliId = null;
        try {
            EntitySearchFilter titoloFilter = new EntitySearchFilter(IProtocolloManager.OGGETTO_FILTER_KEY, false, oggetto, useLikeOption);
            EntitySearchFilter[] filters = { titoloFilter };
            protocolliId = this._protocolloManager.loadProtocolliId(filters);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN RICERCA PROTOCOLLI PER OGGETTO: " + oggetto, this);
        }
        return protocolliId;
    }

    protected List<String> searchDocumentiPersonaliByTitolo(String titolo, boolean useLikeOption) {
        List<String> documentiPersonali = null;
        try {
            EntitySearchFilter titoloFilter = new EntitySearchFilter("titolo", false, titolo, useLikeOption);
            EntitySearchFilter[] filters = { titoloFilter };
            documentiPersonali = this._documentoPersonaleManager.loadDocumentiId(filters);
        } catch (Throwable t) {
            AdocLogger.logDebug("ERRORE IN RICERCA DOCUMENTI PERSONALI PER TTIOLO: " + titolo, this);
        }
        return documentiPersonali;
    }

    /**
	 * Aggiunge un documento personale
	 * @param idProfilo identificativo del profilo che lo crea
	 * @param titolo Il titolo
	 * @return true se va tutto bene
	 * @throws Throwable
	 */
    protected boolean createAndInsertDocumentoPersonale(int idProfilo, String titolo) throws Throwable {
        boolean ok = false;
        try {
            DocumentoPersonale doc = null;
            DocumentoPersonaleTestHelper helper;
            helper = new DocumentoPersonaleTestHelper(_documentoPersonaleManager);
            Profilo profilo = _profiloManager.getProfilo(idProfilo);
            doc = helper.createDocumentoPersonale(titolo, profilo, null);
            File file = new File(FILE_DOCUMENT_PATHNAME);
            this._documentoPersonaleManager.addDocument(idProfilo, doc, file, file.getName());
            ok = true;
        } catch (Throwable t) {
            throw new Throwable("ERRORE IN CREAZIONE DOCUMENTO PERSONALE");
        }
        return ok;
    }

    /**
	 *
	 * @param titolo oggetto del protocollo, stringa esatta
	 * @return
	 * @throws ApsSystemException
	 */
    protected Documento loadDocByTitolo(String titolo) throws ApsSystemException {
        Documento doc = null;
        try {
            List<String> ids = this.searchDocumentByTitolo(titolo, false, null);
            if (null != ids && ids.size() == 1) {
                doc = (Documento) this._documentoManager.getDocument(ids.get(0));
            } else {
                AdocLogger.logDebug("Errore in recupero documento per titolo. Dimensione lista: " + ids.size(), this);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return doc;
    }

    /**
	 *
	 * @param oggetto oggetto del protocollo, stringa esatta
	 * @return
	 * @throws ApsSystemException
	 */
    protected Protocollo loadProtByOggetto(String oggetto) throws ApsSystemException {
        Protocollo prot = null;
        try {
            List<String> ids = this.searchProtocolliByOggetto(oggetto, false);
            if (null != ids && ids.size() == 1) {
                prot = this._protocolloManager.getProtocollo(ids.get(0));
            } else {
                AdocLogger.logDebug("Errore in recupero protocollo per oggetto. Dimensione lista: " + ids.size(), this);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return prot;
    }

    /**
	 *
	 * @param titolo oggetto del protocollo, stringa esatta
	 * @return
	 * @throws ApsSystemException
	 */
    protected DocumentoPersonale loadDocumentoPersonaleByTitolo(String titolo) throws ApsSystemException {
        DocumentoPersonale dp = null;
        try {
            List<String> docPersonali = this.searchDocumentiPersonaliByTitolo(titolo, false);
            if (null != docPersonali && docPersonali.size() == 1) {
                dp = this._documentoPersonaleManager.getDocument(docPersonali.get(0));
            } else {
                AdocLogger.logDebug("Errore in recupero documenti personali per titolo. Dimensione lista: " + docPersonali.size(), this);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return dp;
    }

    /**
	 * Estrae le azioni relative al protocollo e le confronta con l'array expected.<br>
	 * @param protocollo
	 * @param expected Array di stringhe contenenti le costanti delle azioni, in ordine cronologico
	 * @throws Throwable
	 */
    protected void checkAzioniProtocollo(Protocollo protocollo, String[] expected) throws Throwable {
        Thread.sleep(1500);
        List<Integer> azioni = this._azioneProtocolloManager.getIdsAzioni(protocollo.getId(), ADD_091_PROTOCOLLO, UUOOO_PROTOCOLLO, this.getYesterday(), null);
        ArrayUtils.reverse(expected);
        assertEquals(expected.length, azioni.size());
        for (int i = 0; i < azioni.size(); i++) {
            int idAzione = azioni.get(i);
            AzioneProtocollo azioneProtocollo = (AzioneProtocollo) this._azioneProtocolloManager.getAzione(idAzione);
            Azione azione = this._azioneManager.getAzione(IAzioneManager.OGGETTO_PROTOCOLLO, expected[i]);
            assertEquals("TIPO AZIONE PROTOCOLLO NON CORRISPONDENTE: ", azioneProtocollo.getIdTipoazione(), azione.getId());
        }
    }

    /**
	 * Estrae le azioni relative al documento e le confronta con l'array expected.<br>
	 * @param documento
	 * @param expected Array di stringhe contenenti le costanti delle azioni, in ordine cronologico
	 * @throws Throwable
	 */
    protected void checkAzioniDocumento(Documento documento, String[] expected) throws Throwable {
        Thread.sleep(1500);
        List<Integer> azioni = this._azioneDocumentoManager.getIdsAzioni(documento.getId(), 0, 0, null, null);
        ArrayUtils.reverse(expected);
        assertEquals(expected.length, azioni.size());
        for (int i = 0; i < azioni.size(); i++) {
            int idAzione = azioni.get(i);
            AzioneDocumento azioneDocumento = (AzioneDocumento) this._azioneDocumentoManager.getAzione(idAzione);
            Azione azione = this._azioneManager.getAzione(IAzioneManager.OGGETTO_DOCUMENTO, expected[i]);
            assertEquals("TIPO AZIONE DOCUMENTO NON CORRISPONDENTE: ", azioneDocumento.getIdTipoazione(), azione.getId());
        }
    }

    /**
	 * Estrae le azioni relative al fascicolo e le confronta con l'array expected.<br>
	 * @param fascicolo
	 * @param expected Array di stringhe contenenti le costanti delle azioni, in ordine cronologico
	 * @throws Throwable
	 */
    protected void checkAzioniFascicolo(Fascicolo fascicolo, String[] expected) throws Throwable {
        Thread.sleep(1500);
        List<Integer> azioni = this._azioneFascicoloManager.getIdsAzioni(fascicolo.getCode(), 0, 0, null, null);
        ArrayUtils.reverse(expected);
        assertEquals(expected.length, azioni.size());
        for (int i = 0; i < azioni.size(); i++) {
            int idAzione = azioni.get(i);
            AzioneFascicolo azioneFascicolo = (AzioneFascicolo) this._azioneFascicoloManager.getAzione(idAzione);
            Azione azione = this._azioneManager.getAzione(IAzioneManager.OGGETTO_FASCICOLO, expected[i]);
            assertEquals("TIPO AZIONE FASCICOLO NON CORRISPONDENTE: ", azioneFascicolo.getIdTipoazione(), azione.getId());
        }
    }

    @SuppressWarnings("unchecked")
    public void compareProtocolli(Protocollo expected, Protocollo actual) throws Throwable {
        PropertyUtilsBean pubExp = new PropertyUtilsBean();
        Map expectedFields = pubExp.describe(expected);
        PropertyUtilsBean pubAct = new PropertyUtilsBean();
        Map actualFields = pubAct.describe(actual);
        Iterator it = expectedFields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String expectedKey = (String) pairs.getKey();
            Object expectedValue = pairs.getValue();
            Object actualValue = actualFields.get(expectedKey);
            if (expectedKey.equals("attributeMap")) {
            } else if (expectedKey.equals("entityPrototype") || expectedKey.equals("attributeList")) {
            } else if (expectedKey.equals("mittente")) {
                this.compareContatti(expectedValue, actualValue);
            } else if (expectedKey.equals("destinatari")) {
                this.compareDestinatari(expectedValue, actualValue);
            } else if (expectedKey.equals("documento")) {
                if (actualValue != null && expectedValue != null) {
                    this._helper.compareDocuments((Documento) actualValue, (Documento) expectedValue, null, null);
                }
            } else {
                assertEquals("ERROR ON " + expectedKey, actualValue, expectedValue);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void compareDestinatari(Object expectedValue, Object actualValue) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        List<SmallContatto> destinatariExpected = (List<SmallContatto>) expectedValue;
        List<SmallContatto> destinatariactual = (List<SmallContatto>) actualValue;
        assertEquals(destinatariExpected.size(), destinatariactual.size());
        for (int i = 0; i < destinatariExpected.size(); i++) {
            this.compareContatti(destinatariExpected.get(i), destinatariactual.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    private void compareContatti(Object expected, Object actual) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PropertyUtilsBean pubExp = new PropertyUtilsBean();
        Map expectedFields = pubExp.describe(expected);
        PropertyUtilsBean pubAct = new PropertyUtilsBean();
        Map actualFields = pubAct.describe(actual);
        Iterator it = expectedFields.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            String expectedKey = (String) pairs.getKey();
            Object expectedValue = pairs.getValue();
            Object actualValue = actualFields.get(expectedKey);
            if (expectedKey.equals("something.to.handle")) {
            } else {
                assertEquals("compareContatti_" + expectedKey, actualValue, expectedValue);
            }
        }
    }

    protected Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = cal.getTime();
        return yesterday;
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
            NodoTitolario testVoceTitolario = (NodoTitolario) this._titolarioManager.getNode("4");
            if (null != testVoceTitolario) {
                ((TitolarioManager) this._titolarioManager).deleteNodoTitolario("userName", testVoceTitolario.getCode());
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            ((ProtocolloManager) this._protocolloManager).deleteNumeratore(_numeratore);
            _repositoryImpl.shutdown();
            super.tearDown();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    private JcrRepositoryBaseConnection _repositoryImpl;

    protected DocumentoTestHelper _helper;

    protected IDocumentoManager _documentoManager;

    protected IProfiloManager _profiloManager;

    protected IUserManager _userManager;

    protected IFascicoloManager _fascicoloManager;

    protected IInoltroDocumentoManager _inoltroDocumentoManager;

    protected IInoltroManager _inoltroManager;

    protected IDocumentoAuthorizationManager _documentoAuthorizationManager;

    protected IProtocolloManager _protocolloManager;

    protected IEnteManager _enteManager;

    protected IAooManager _aooManager;

    protected IAzioneOggettoManager _azioneProtocolloManager;

    protected IAzioneOggettoManager _azioneFascicoloManager;

    protected IAzioneOggettoManager _azioneDocumentoManager;

    protected IAzioneManager _azioneManager;

    protected IUnitaOrganizzativaManager _unitaOrganizzativaManager;

    protected IInoltroFascicoloManager _inoltroFascicoloManager;

    protected ITitolarioManager _titolarioManager;

    protected IPecManagerOUT _pecManagerOUT;

    protected IDocumentoPersonaleManager _documentoPersonaleManager;

    private Numeratore _numeratore;

    public static final String FILE_DOCUMENT_PATHNAME = "./admin/test/documentsFile/test.txt";

    public static final int ADD_051_FORMAZ = 126;

    public static final int ADD_101_STIP = 65;

    public static final int ADD_MERCURIO_SEG_DIR = 83;

    public static final int ADD_VERDI_RIS_UMANE = 56;

    public static final int ADD_GIALLI_FORMAZ = 59;

    public static final int ADD_VIOLA_GEST_FORN = 92;

    public static final int ADD_091_PROTOCOLLO = 71;

    public static final int UUOOO_RISORSE_UMANE = 4;

    public static final int UUOOO_SEGRETERIA_DIR = 11;

    public static final int UUOOO_STIPENDI = 10;

    public static final int UUOOO_FORMAZIONE = 5;

    public static final int UUOOO_PROTOCOLLO = 13;

    public static final int UUOOO_GEST_FORN = 8;

    public static final int MODALITA_SPEDIZIONE_MAIL = 1;

    public static final int MODALITA_SPEDIZIONE_OTHER = 2;
}
