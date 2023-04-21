package com.miden2ena.mogeci.wfe;

import com.miden2ena.mogeci.exceptions.WFEConnectionException;
import com.miden2ena.mogeci.exceptions.WFEEventException;
import com.miden2ena.mogeci.exceptions.WFEWorklistException;
import com.miden2ena.mogeci.exceptions.WFEWorklistException;
import com.miden2ena.mogeci.util.ConfigUtil;
import com.miden2ena.mogeci.wfe.event.*;
import com.miden2ena.mogeci.exceptions.WFEWorklistException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.TreeMap;
import openwfe.org.OpenWfeException;
import openwfe.org.engine.workitem.AttributeException;
import openwfe.org.worklist.WorkListException;

/**
 * WFEController e' un interfaccia unificata per accedere a vari metodi di utilita nella gestione
 * di WorkItem .
 * WFEController e' un singleton , usare il metodo statico getInstance() per ottenere un istanza della classe.
 * WFEController è Thread Safe.
 * @author miden2ena
 */
public class WFEController {

    private static WFEController istanza;

    /**
     * Ritorna un unica istanza di WFEController . Singleton Pattern
     * @return un Oggetto WFEController.
     */
    public static WFEController getInstance() {
        if (istanza == null) istanza = new WFEController();
        return istanza;
    }

    /**
     * Crea una nuova instanza of WFEController , privato per garantire un unica istanza per programma.
     */
    private WFEController() {
    }

    /**
     * Esegue delle azioni nel WFE a seconda del tipo del WFEvent passato in parametro
     * <p>il WFEvent oltre a identificare quale insieme di azioni vengono svolte contiene i dati necessari alla sua gestione.
     * <p>nel caso sia necessario gestire nuovi eventi la classe WFEController va estesa ridefinendo il metodo execute(WFEvent p_e)
     * @param p_e un sotto oggetto di WFEvent .
     * @return il WorkItemID del workItem lanciato/modificato
     * @throws com.miden2ena.mogeci.exceptions.WFEConnectionException Lanciata in caso di probblemi di connettività con il workSessionServer
     * @throws com.miden2ena.mogeci.exceptions.WFEWorklistException Lanciata nel caso di un errore nello svolgere un azione su di un workItem.
     * @throws com.miden2ena.mogeci.exceptions.WFEEventException Lanciata nel caso il metodo WFEController non abbia un metodo per gestire l'evento passato come parametro.
     */
    public String execute(WFEvent p_e) throws WFEConnectionException, WFEWorklistException, WFEEventException {
        if (p_e instanceof WFEvent_CVMail) return this.doCVMail(p_e);
        if (p_e instanceof WFEvent_CVPrivacy) return doCVPrivacy(p_e);
        if (p_e instanceof WFEvent_CVInserito) return doCVInserito(p_e);
        if (p_e instanceof WFEvent_CVCertificato) return doCVCertificato(p_e);
        if (p_e instanceof WFEvent_CVSelf) return doCVSelf(p_e);
        if (p_e instanceof WFEvent_CVManuale) return doCVManuale(p_e);
        throw new WFEEventException("Evento non previsto.");
    }

    private String doCVMail(WFEvent e) throws WFEConnectionException, WFEWorklistException {
        WFEExecutor exec = new WFEExecutor(e.getUser(), e.getPassword(), e.getSessionServerUrl());
        TreeMap amap = new TreeMap();
        Long tmp = new Long(((WFEvent_CVMail) e).getIdCV());
        amap.put(ConfigUtil.FLOW_FIELD_CVID, tmp.toString());
        amap.put(ConfigUtil.FLOW_FIELD_DATAMAIL, ((WFEvent_CVMail) e).getMailDataArrivo());
        amap.put(ConfigUtil.FLOW_FIELD_INDMAIL, ((WFEvent_CVMail) e).getMailIndirizzo());
        amap.put(ConfigUtil.FLOW_FIELD_OBJECT, ((WFEvent_CVMail) e).getMailObject());
        String aux = null;
        try {
            aux = exec.launchItem(e.getEngine(), ((WFEvent_CVMail) e).getFlowDefinitionUrl(), amap);
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("url " + ((WFEvent_CVMail) e).getSessionServerUrl() + " errato.");
        } catch (AttributeException ex) {
            throw new WFEWorklistException("Errore attributi :" + ex.toString());
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella connessione con OpenWFE");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore di rete, impossibile la connessione con OpenWFE");
        } catch (OpenWfeException ex) {
            throw new WFEWorklistException("Errore interno di OpenWFE.");
        }
        return aux;
    }

    private String doCVPrivacy(WFEvent e) throws WFEConnectionException, WFEWorklistException {
        WFEvent_CVPrivacy em = (WFEvent_CVPrivacy) e;
        WFEExecutor exec = new WFEExecutor(em.getUser(), em.getPassword(), em.getSessionServerUrl());
        try {
            exec.waitForItem(e.getStore(), e.getWfInstanceId());
            if (em.isConfermata()) {
                exec.addField(em.getStore(), em.getWfInstanceId(), ConfigUtil.FLOW_FIELD_PRIVACY, "ok");
            }
            exec.addField(em.getStore(), em.getWfInstanceId(), ConfigUtil.FLOW_FIELD_DATAPRIVACY, em.getDataConfermaPrivacy().toString());
            exec.forward(em.getStore(), em.getWfInstanceId());
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("url " + ((WFEvent_CVPrivacy) e).getSessionServerUrl() + " errato.");
        } catch (AttributeException ex) {
            throw new WFEWorklistException("Errore attributi :" + ex.toString());
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella connessione con OpenWFE");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore di rete, impossibile la connessione con OpenWFE");
        } catch (OpenWfeException ex) {
            throw new WFEWorklistException("Errore interno di OpenWFE.");
        }
        return em.getWfInstanceId();
    }

    private String doCVInserito(WFEvent e) throws WFEConnectionException, WFEWorklistException {
        WFEvent_CVInserito em = (WFEvent_CVInserito) e;
        WFEExecutor exec = new WFEExecutor(em.getUser(), em.getPassword(), em.getSessionServerUrl());
        try {
            exec.connect();
            exec.waitForItem(e.getStore(), e.getWfInstanceId());
            exec.addField(em.getStore(), em.getWfInstanceId(), ConfigUtil.FLOW_FIELD_INSERITO, "ok");
            if (em.getCognomeNome() != null) {
                exec.addField(em.getStore(), em.getWfInstanceId(), ConfigUtil.FLOW_FIELD_COGNOMENOME, em.getCognomeNome());
            }
            if (em.getMail() != null) {
                exec.addField(em.getStore(), em.getWfInstanceId(), ConfigUtil.FLOW_FIELD_INDMAIL, em.getMail());
            }
            exec.forward(em.getStore(), em.getWfInstanceId());
            exec.close();
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("url " + ((WFEvent_CVPrivacy) e).getSessionServerUrl() + " errato.");
        } catch (AttributeException ex) {
            throw new WFEWorklistException("Errore attributi :" + ex.toString());
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella connessione con OpenWFE");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore di rete, impossibile la connessione con OpenWFE");
        } catch (OpenWfeException ex) {
            throw new WFEWorklistException("Errore interno di OpenWFE.");
        }
        return em.getWfInstanceId();
    }

    private String doCVManuale(WFEvent e) throws WFEConnectionException, WFEWorklistException {
        WFEExecutor exec = new WFEExecutor(e.getUser(), e.getPassword(), e.getSessionServerUrl());
        TreeMap amap = new TreeMap();
        WFEvent_CVManuale em = (WFEvent_CVManuale) e;
        amap.put(ConfigUtil.FLOW_FIELD_CVID, em.getIdCV());
        amap.put(ConfigUtil.FLOW_FIELD_PRIVACY, "ok");
        amap.put(ConfigUtil.FLOW_FIELD_COGNOMENOME, em.getCognomeNome());
        amap.put(ConfigUtil.FLOW_FIELD_DATAPRIVACY, em.getDataConfermaPrivacy().toString());
        if (em.getMail() != null) amap.put(ConfigUtil.FLOW_FIELD_INDMAIL, em.getMail());
        String aux = null;
        try {
            aux = exec.launchItem(em.getEngine(), ConfigUtil.FLOW_URL_CVMANUALE, amap);
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("url " + ((WFEvent_CVPrivacy) e).getSessionServerUrl() + " errato.");
        } catch (AttributeException ex) {
            throw new WFEWorklistException("Errore attributi :" + ex.toString());
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella connessione con OpenWFE");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore di rete, impossibile la connessione con OpenWFE");
        } catch (OpenWfeException ex) {
            throw new WFEWorklistException("Errore interno di OpenWFE." + ex.toString());
        }
        return aux;
    }

    private String doCVSelf(WFEvent e) throws WFEConnectionException, WFEWorklistException {
        WFEExecutor exec = new WFEExecutor(e.getUser(), e.getPassword(), e.getSessionServerUrl());
        TreeMap amap = new TreeMap();
        amap.put(ConfigUtil.FLOW_FIELD_CVID, ((WFEvent_CVSelf) e).getIdCV());
        amap.put(ConfigUtil.FLOW_FIELD_PRIVACY, "ok");
        amap.put(ConfigUtil.FLOW_FIELD_COGNOMENOME, ((WFEvent_CVSelf) e).getCognomeNome());
        java.util.Date date_tmp = new Date(System.currentTimeMillis());
        amap.put(ConfigUtil.FLOW_FIELD_DATAPRIVACY, date_tmp.toString());
        String aux = "";
        try {
            aux = exec.launchItem(e.getEngine(), ((WFEvent_CVSelf) e).getFlowDefinitionUrl(), amap);
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("url " + ((WFEvent_CVPrivacy) e).getSessionServerUrl() + " errato.");
        } catch (AttributeException ex) {
            throw new WFEWorklistException("Errore attributi :" + ex.toString());
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella connessione con OpenWFE");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore di rete, impossibile la connessione con OpenWFE");
        } catch (OpenWfeException ex) {
            throw new WFEWorklistException("Errore interno di OpenWFE." + ex.toString());
        }
        return aux;
    }

    private String doCVCertificato(WFEvent e) throws WFEConnectionException, WFEWorklistException {
        WFEvent_CVCertificato em = (WFEvent_CVCertificato) e;
        WFEExecutor exec = new WFEExecutor(em.getUser(), em.getPassword(), em.getSessionServerUrl());
        try {
            exec.waitForItem(e.getStore(), e.getWfInstanceId());
            if (em.isCertificato()) {
                exec.addField(em.getStore(), em.getWfInstanceId(), ConfigUtil.FLOW_FIELD_CERTIFICATO, "ok");
            }
            exec.forward(em.getStore(), em.getWfInstanceId());
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("url " + ((WFEvent_CVPrivacy) e).getSessionServerUrl() + " errato.");
        } catch (AttributeException ex) {
            throw new WFEWorklistException("Errore attributi :" + ex.toString());
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella connessione con OpenWFE");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore di rete, impossibile la connessione con OpenWFE");
        } catch (OpenWfeException ex) {
            throw new WFEWorklistException("Errore interno di OpenWFE.");
        }
        return em.getWfInstanceId();
    }

    /**
     * Ricerca nello store segnalato la presenza di un WorkItem contenente il field CVID con il valore passato in parametro
     *
     *
     * @param p_user user openwfe
     * @param p_password password dell user openwfe
     * @param p_workSessionServer indirizzo del worksessionserver
     * @param p_cvid id del CV nel Database
     * @param p_store Lo store dove cercare il WorkItem
     * @return il WorkItemID del WorkItem contenente il valore del field _CVID_ passato come parametro
     * @throws com.miden2ena.mogeci.exceptions.WFEWorklistException Lanciata se OpenWFE  segnala un errore.
     * @throws com.miden2ena.mogeci.exceptions.WFEConnectionException Lanciata nel caso di probblemi di connettivita con il WorkSessionServer
     */
    public String getIdFlowCV(String p_user, String p_password, String p_workSessionServer, String p_cvid, String p_store) throws WFEWorklistException, WFEConnectionException {
        WFEReader reader = new WFEReader(p_user, p_password, p_workSessionServer);
        try {
            return reader.getIdFlowCV(p_cvid, p_store);
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella comunicazione con il WorkSessionServer :" + ex.toString());
        } catch (WorkListException ex) {
            throw new WFEWorklistException("Errore interno workflow :" + ex.toString());
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("Indirizzo " + p_workSessionServer + " non valido.");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore nella comunicazione con il WorkSessionServer :" + ex.toString());
        }
    }

    /**
     * Ricerca e ritorna un oggetto WorkItemHeader rappresentante uno specifico WorkItem.
     *
     *
     * @param p_user username Openwfe
     * @param p_password Password OpenWFE
     * @param p_workSessionServer WorkSessionServer URI
     * @param p_wiID il WorkItemID del workitem da ritornare.
     * @return un WorkItemHeader rappresentazione del WorkItem con WorkItemID == p_wiid
     * @throws com.miden2ena.mogeci.exceptions.WFEConnectionException .
     * @throws com.miden2ena.mogeci.exceptions.WFEWorklistException .
     */
    public com.miden2ena.mogeci.util.FlowHeader getWorkItemHeader(String p_user, String p_password, String p_workSessionServer, String p_wiID) throws WFEConnectionException, WFEWorklistException {
        WFEReader reader = new WFEReader(p_user, p_password, p_workSessionServer);
        try {
            return reader.getWorkItemHeader(p_wiID);
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella comunicazione con il WorkSessionServer :" + ex.toString());
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("Indirizzo " + p_workSessionServer + " non valido.");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore nella comunicazione con il WorkSessionServer :" + ex.toString());
        }
    }

    /**
     * Crea una rappresentazione di tutti i WorkItem contenuti in una Store.
     * <p>Se p_store==null ritorna una rappresentazione di tutti i workItem in tutte le store in cui l'utente ha permessi di lettura
     * <p>Ritorna un java.util.ArrayList contenente dei com.miden2ena.mogeci.util.FlowHeader che rappresentano i workItem nello store
     * @param user Username Openwfe
     * @param password Password Openwfe
     * @param workSessionServer WorkSessionServer URI OpenWFE
     * @param store Lo store da cui prelevare i WorkItem
     * @return un java.util.ArrayList di com.miden2ena.mogeci.util.FlowHeader rappresentante i workItem nello Store
     * @throws com.miden2ena.mogeci.exceptions.WFEConnectionException .
     * @throws com.miden2ena.mogeci.exceptions.WFEWorklistException .
     */
    public java.util.List getInFlowList(String user, String password, String workSessionServer, String store) throws WFEConnectionException, WFEWorklistException {
        WFEReader read = new WFEReader(user, password, workSessionServer);
        if (store != null && store.compareTo("") != 0) return read.getInFlowList(store, ConfigUtil.MAX_STORE_VIEW);
        return read.getInFlowList(ConfigUtil.MAX_STORE_VIEW);
    }

    /**
     * Ritorna un java.util.TreeMap (come key una String rappresentante lo Store , come valore una ArrayList di com.miden2ena.mogeci.util.FlowHeader.
     * <p>vengono prelevati i WorkItem solo dagli store in cui l'utente user ha diritto di lettura
     * @param user Username utente OpenWFE
     * @param password Password utente OpenWFE
     * @param workSessionServer WorkSessionServer URI
     * @return java.util.TreeMap Ritorna un java.util.TreeMap (come key una String rappresentante lo Store , come valore una ArrayList di com.miden2ena.mogeci.util.FlowHeader.
     * Solo store con diritto di lettura da parte dell User.
     * @throws com.miden2ena.mogeci.exceptions.WFEWorklistException .
     * @throws com.miden2ena.mogeci.exceptions.WFEConnectionException .
     */
    public java.util.Map getStoreMap(String user, String password, String workSessionServer) throws WFEWorklistException, WFEConnectionException {
        WFEReader read = new WFEReader(user, password, workSessionServer);
        try {
            return read.getStoreMap();
        } catch (RemoteException ex) {
            throw new WFEConnectionException("Errore nella comunicazione con il WorkSessionServer :" + ex.toString());
        } catch (MalformedURLException ex) {
            throw new WFEConnectionException("Indirizzo " + workSessionServer + " non valido.");
        } catch (NotBoundException ex) {
            throw new WFEConnectionException("Errore nella comunicazione con il WorkSessionServer :" + ex.toString());
        }
    }
}
