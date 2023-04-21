package it.conte.tesi.dao.beans.managers;

import com.icesoft.faces.async.render.SessionRenderer;
import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import it.conte.tesi.dao.beans.Gruppo;
import it.conte.tesi.dao.beans.Interfaccia;
import it.conte.tesi.dao.beans.Permanenza;
import it.conte.tesi.dao.beans.Permesso;
import it.conte.tesi.dao.beans.Switc;
import it.conte.tesi.dao.beans.util.FacesUtil;
import it.conte.tesi.dao.beans.util.HibernateUtil;
import it.conte.tesi.snmp.SnmpIfDataBean;
import it.conte.tesi.snmp.beans.BridgeBean;
import it.conte.tesi.snmp.beans.InterfaceBean;
import it.conte.tesi.snmp.operations.SettingOperation;
import it.conte.tesi.snmp.pollers.InterfacePoller;
import it.conte.tesi.snmp.retrivers.BridgeRetriver;
import it.conte.tesi.snmp.retrivers.InterfaceRetriver;
import it.conte.tesi.snmp.utils.SnmpUtil;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.event.ValueChangeEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.User;
import snmp.SNMPBadValueException;
import snmp.SNMPGetException;
import snmp.SNMPSetException;
import snmp.SNMPv1CommunicationInterface;

/**
 *
 * @author conte
 */
public class RequestManager implements Observer {

    private String username;

    private String ruoli;

    private Session session;

    private List<Permanenza> permanenzeUtente;

    private List<Switc> switchInterrogabili;

    private HtmlDataTable tabellaSwitchs;

    private boolean admin = false;

    private Switc switchselected;

    private List<Interfaccia> interfacceSwitchSel;

    private HashMap<String, String> mappaPermesso;

    private InterfaceBean InterfacceSnmp;

    private Exception eccezzione;

    private boolean eccezzioneVerificata;

    private List<SnmpIfDataBean> interfacceInterrogabili;

    private HtmlDataTable tabellaInterfacce;

    private boolean showInterfaceTable = false;

    private SnmpIfDataBean interfacciaSelezionata;

    private boolean pannelloAdminAttivo;

    private boolean showGenericPopup;

    private String informazione;

    private InterfaceBean ifbean;

    private BridgeBean brbean;

    private String macCercato;

    private InterfacePoller iPuller;

    private SNMPv1CommunicationInterface connection;

    private Thread threadPuller;

    private int intervalloPolling = 2;

    private boolean threadAlive = false;

    public RequestManager() {
        mappaPermesso = new HashMap<String, String>();
        User utente = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        username = utente.getUsername();
        Iterator<Gruppo> i = (fetchGruppiUtente()).iterator();
        while (i.hasNext()) {
            Gruppo gruppo = i.next();
            if (gruppo.getNome().equalsIgnoreCase("administrators")) {
                this.admin = true;
            }
        }
        this.fetchSwitchInterrogabili();
        SessionRenderer.addCurrentSession("managerRichieste");
    }

    private synchronized void fetchSwitchInterrogabili() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            if (this.isAdmin()) {
                switchInterrogabili = (List<Switc>) session.createQuery("from Switc").list();
            } else {
                switchInterrogabili = (List<Switc>) session.createQuery("select p.switc from Permesso p where p.gruppo in (select pm.gruppo from Permanenza pm where pm.utente.username = ?) and p.flag = 1 group by p.switc").setString(0, username).list();
            }
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    private synchronized List<Gruppo> fetchGruppiUtente() {
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        List<Gruppo> gruppi;
        try {
            tx = session.beginTransaction();
            gruppi = (List<Gruppo>) session.createQuery("select p.gruppo from Permanenza p where p.utente.username = ?").setString(0, username).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        return gruppi;
    }

    public List<Permanenza> getPermanenzeUtente() {
        if (permanenzeUtente == null) {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                permanenzeUtente = (List<Permanenza>) session.createQuery("from Permanenza p where p.utente.username = ?").setString(0, username).list();
                tx.commit();
            } catch (HibernateException e) {
                if (tx != null) {
                    tx.rollback();
                }
                throw e;
            }
        }
        return permanenzeUtente;
    }

    public String getRuoli() {
        return ruoli;
    }

    public String getUsername() {
        return username;
    }

    public void rowSelectionListener(RowSelectorEvent event) {
        setSwitchselected(null);
        for (int i = 0, max = switchInterrogabili.size(); i < max; i++) {
            Switc sel = (Switc) switchInterrogabili.get(i);
            if (sel.isSelected()) {
                setSwitchselected(sel);
                this.fetchInterfaces(sel);
                this.recuperaPorte();
                break;
            } else {
                setSwitchselected(null);
                setShowInterfaceTable(false);
            }
        }
    }

    public String interrogaInterfaccia() {
        setSwitchselected((Switc) tabellaSwitchs.getRowData());
        fetchInterfaces(switchselected);
        recuperaPorte();
        return null;
    }

    private synchronized void fetchInterfaces(Switc selezionato) {
        List<Permesso> permessi = new ArrayList<Permesso>();
        session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            setInterfacceSwitchSel((List<Interfaccia>) session.createQuery("from Interfaccia i where i.switc = ?").setEntity(0, selezionato).list());
            permessi = (List<Permesso>) session.createQuery("from Permesso p where p.gruppo in (select pm.gruppo from Permanenza pm where pm.utente.username = ?) and p.switc.indirizzoIp = ? and p.flag = 1 order by p.switc").setString(0, username).setString(1, getSwitchselected().getIndirizzoIp()).list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        if (!interfacceSwitchSel.isEmpty() && !permessi.isEmpty()) {
            Iterator<Permesso> i = permessi.iterator();
            while (i.hasNext()) {
                Permesso permesso = i.next();
                if (mappaPermesso.containsKey(permesso.getAzione())) {
                    String porteAggiunte = mappaPermesso.get(permesso.getAzione());
                    porteAggiunte = porteAggiunte + "," + permesso.getListaPorte();
                    mappaPermesso.put(permesso.getAzione(), porteAggiunte);
                } else {
                    mappaPermesso.put(permesso.getAzione(), permesso.getListaPorte());
                }
            }
        }
    }

    public synchronized void recuperaPorte() {
        try {
            InterfaceRetriver ir = null;
            BridgeRetriver br = null;
            if (mappaPermesso.containsKey("RW") || isAdmin()) {
                connection = SnmpUtil.createV1Connection(SnmpUtil.v2c, InetAddress.getByName(switchselected.getIndirizzoIp()), switchselected.getCommunityPri());
                ir = new InterfaceRetriver(connection);
                br = new BridgeRetriver(connection);
            } else {
                connection = SnmpUtil.createV1Connection(SnmpUtil.v2c, InetAddress.getByName(switchselected.getIndirizzoIp()), switchselected.getCommunityPub());
                ir = new InterfaceRetriver(connection);
                br = new BridgeRetriver(connection);
            }
            ifbean = new InterfaceBean(ir.fetch());
            brbean = br.fetch();
            if (isAdmin()) {
                interfacceInterrogabili = ifbean.getData();
                brbean.setPortaPrioritaria(ifbean);
                for (Iterator<SnmpIfDataBean> it = interfacceInterrogabili.iterator(); it.hasNext(); ) {
                    it.next().setPermesso(SnmpIfDataBean.PERMESSO_RW);
                }
            } else {
                interfacceInterrogabili = ifbean.scorporaInterfacce(mappaPermesso);
                brbean.setPortaPrioritaria(ifbean);
            }
            setShowInterfaceTable(true);
        } catch (IOException ex) {
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
            this.setEccezzione(new Exception("Errore: problema di IO"));
            setEccezzioneVerificata(true);
        } catch (SNMPBadValueException ex) {
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
            this.setEccezzione(new Exception("Errore: problema setting SNMP"));
            setEccezzioneVerificata(true);
        } catch (SNMPGetException ex) {
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
            this.setEccezzione(new Exception("Errore: problema recupero SNMP "));
            setEccezzioneVerificata(true);
        }
    }

    public void amministraInterfaccia() {
        setInterfacciaSelezionata((SnmpIfDataBean) tabellaInterfacce.getRowData());
        setPannelloAdminAttivo(true);
    }

    public void closePopup() {
        setEccezzioneVerificata(false);
        setPannelloAdminAttivo(false);
        setShowGenericPopup(false);
        if (threadPuller != null && threadPuller.isAlive()) {
            threadPuller = null;
        }
    }

    public void cercaMac() {
        int porta = brbean.isMacAddressPresent(macCercato);
        if (porta != -1) {
            Iterator<SnmpIfDataBean> i = ifbean.getData().iterator();
            while (i.hasNext()) {
                SnmpIfDataBean interfaccia = i.next();
                if (interfaccia.getIfIndex() == porta) {
                    setInformazione("MacAddress trovato nell'interfaccia: " + interfaccia.getIfName());
                    break;
                }
            }
        } else {
            setInformazione("MacAddress non trovato!");
        }
        setShowGenericPopup(true);
    }

    public void SpeedSettingInterfaccia(ValueChangeEvent e) {
        Iterator<SnmpIfDataBean> i = interfacceInterrogabili.iterator();
        int count = 1;
        while (i.hasNext()) {
            SnmpIfDataBean porta = i.next();
            if (porta.equals(interfacciaSelezionata)) {
                try {
                    porta.setPortAdminSpeed(Long.parseLong(e.getNewValue().toString()));
                    SettingOperation.SpeedSettingInterface(connection, porta, count);
                    break;
                } catch (IOException ex) {
                    setPannelloAdminAttivo(false);
                    this.setEccezzione(ex);
                    setEccezzioneVerificata(true);
                    Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SNMPBadValueException ex) {
                    setPannelloAdminAttivo(false);
                    this.setEccezzione(ex);
                    setEccezzioneVerificata(true);
                    Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SNMPSetException ex) {
                    setPannelloAdminAttivo(false);
                    this.setEccezzione(ex);
                    setEccezzioneVerificata(true);
                    Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                count++;
            }
        }
    }

    public void AdminStatusSettingInterfaccia(ValueChangeEvent e) {
        try {
            Long nuovoStato = Long.parseLong(e.getNewValue().toString());
            SettingOperation.AdminStatusSettingInterface(connection, interfacciaSelezionata, nuovoStato);
        } catch (IOException ex) {
            setPannelloAdminAttivo(false);
            this.setEccezzione(ex);
            setEccezzioneVerificata(true);
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SNMPBadValueException ex) {
            setPannelloAdminAttivo(false);
            this.setEccezzione(ex);
            setEccezzioneVerificata(true);
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SNMPSetException ex) {
            setPannelloAdminAttivo(false);
            this.setEccezzione(ex);
            setEccezzioneVerificata(true);
            Logger.getLogger(RequestManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void MonitorInterfaccia(ValueChangeEvent e) {
        UIComponent component = FacesUtil.findComponentInRoot("settingGrid");
        if (e.getNewValue().equals("on")) {
            iPuller = new InterfacePoller(switchselected, interfacciaSelezionata, intervalloPolling);
            iPuller.addObserver(this);
            threadPuller = new Thread(iPuller);
            threadPuller.start();
            threadAlive = true;
            if (component != null) {
                component.setRendered(false);
            }
        } else if (e.getNewValue().equals("off")) {
            threadPuller.interrupt();
            threadAlive = false;
            if (component != null) {
                component.setRendered(true);
            }
        }
    }

    public List<Switc> getSwitchInterrogabili() {
        return switchInterrogabili;
    }

    public void setSwitchInterrogabili(List<Switc> switchInterrogabili) {
        this.switchInterrogabili = switchInterrogabili;
    }

    public HtmlDataTable getTabellaSwitchs() {
        return tabellaSwitchs;
    }

    public void setTabellaSwitchs(HtmlDataTable tabellaSwitchs) {
        this.tabellaSwitchs = tabellaSwitchs;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean Admin) {
        this.admin = Admin;
    }

    public Switc getSwitchselected() {
        return switchselected;
    }

    public void setSwitchselected(Switc switchselected) {
        this.switchselected = switchselected;
    }

    public List<Interfaccia> getInterfacceSwitchSel() {
        return interfacceSwitchSel;
    }

    public void setInterfacceSwitchSel(List<Interfaccia> interfacceSwitchSel) {
        this.interfacceSwitchSel = interfacceSwitchSel;
    }

    public HashMap<String, String> getMappaPermesso() {
        return mappaPermesso;
    }

    public void setMappaPermesso(HashMap<String, String> mappaPermesso) {
        this.mappaPermesso = mappaPermesso;
    }

    public InterfaceBean getInterfacceSnmp() {
        return InterfacceSnmp;
    }

    public void setInterfacceSnmp(InterfaceBean InterfacceSnmp) {
        this.InterfacceSnmp = InterfacceSnmp;
    }

    public Exception getEccezzione() {
        return eccezzione;
    }

    public void setEccezzione(Exception eccezzione) {
        this.eccezzione = eccezzione;
    }

    public boolean isEccezzioneVerificata() {
        return eccezzioneVerificata;
    }

    public void setEccezzioneVerificata(boolean eccezzioneVerificata) {
        this.eccezzioneVerificata = eccezzioneVerificata;
    }

    public List<SnmpIfDataBean> getInterfacceInterrogabili() {
        return interfacceInterrogabili;
    }

    public void setInterfacceInterrogabili(List<SnmpIfDataBean> interfacceInterrogabili) {
        this.interfacceInterrogabili = interfacceInterrogabili;
    }

    public boolean isShowInterfaceTable() {
        return showInterfaceTable;
    }

    public void setShowInterfaceTable(boolean showInterfaceTable) {
        this.showInterfaceTable = showInterfaceTable;
    }

    public HtmlDataTable getTabellaInterfacce() {
        return tabellaInterfacce;
    }

    public void setTabellaInterfacce(HtmlDataTable tabellaInterfacce) {
        this.tabellaInterfacce = tabellaInterfacce;
    }

    public SnmpIfDataBean getInterfacciaSelezionata() {
        return interfacciaSelezionata;
    }

    public void setInterfacciaSelezionata(SnmpIfDataBean interfacciaSelezionata) {
        this.interfacciaSelezionata = interfacciaSelezionata;
    }

    public boolean isPannelloAdminAttivo() {
        return pannelloAdminAttivo;
    }

    public void setPannelloAdminAttivo(boolean pannelloAdminAttivo) {
        this.pannelloAdminAttivo = pannelloAdminAttivo;
    }

    public SNMPv1CommunicationInterface getConnection() {
        return connection;
    }

    public void setConnection(SNMPv1CommunicationInterface connection) {
        this.connection = connection;
    }

    public String getMacCercato() {
        return macCercato;
    }

    public void setMacCercato(String macCercato) {
        this.macCercato = macCercato;
    }

    public boolean isShowGenericPopup() {
        return showGenericPopup;
    }

    public void setShowGenericPopup(boolean showGenericPopup) {
        this.showGenericPopup = showGenericPopup;
    }

    public String getInformazione() {
        return informazione;
    }

    public void setInformazione(String informazione) {
        this.informazione = informazione;
    }

    public InterfacePoller getiPuller() {
        return iPuller;
    }

    public void setiPuller(InterfacePoller iPuller) {
        this.iPuller = iPuller;
    }

    public Thread getThreadPuller() {
        return threadPuller;
    }

    public void setThreadPuller(Thread threadPuller) {
        this.threadPuller = threadPuller;
    }

    public int getIntervalloPolling() {
        return intervalloPolling;
    }

    public void setIntervalloPolling(int intervalloPolling) {
        this.intervalloPolling = intervalloPolling;
    }

    public boolean isThreadAlive() {
        return threadAlive;
    }

    public void setThreadAlive(boolean threadAlive) {
        this.threadAlive = threadAlive;
    }

    public void update(Observable o, Object arg) {
        interfacciaSelezionata = (SnmpIfDataBean) arg;
        SessionRenderer.render("managerRichieste");
    }
}
