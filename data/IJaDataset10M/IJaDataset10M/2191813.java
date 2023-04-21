package edicolaClasses;

import java.io.File;
import java.util.Vector;
import java.util.concurrent.locks.*;
import recoveryClasses.*;
import structure.EdicolaControl;
import backupClasses.MasterAgent;
import catalogo.Articolo;
import catalogo.Catalogo;
import data.Role;
import data.edicola.*;
import data.messages.*;
import data.newsletter.ClientSubscription;
import edicolaClasses.agents.*;

/**
 * Classe di controllo e sincronizzazione delle comunicazioni con le edicole
 * @author Simona
 * @version 1.0
 */
public class EdicolaManager extends Thread {

    private final String rootPath = "edicole/";

    private Lock lock = new ReentrantLock();

    private Condition waitingForRequests = lock.newCondition();

    private boolean newEdicolaToSubscribe;

    private boolean newEdicolaToUnsubscribe;

    private boolean edicolaDown;

    private boolean edicolaUp;

    private int firstControllerTcpPort;

    private int edicolaAgentTcpPort;

    private int edicolaMulticastPort;

    private int udpPortForKeepAlive;

    private int recoveryTcpPort;

    private String edicolaMulticastGroup;

    private String firstControllerHostName;

    private String entityName;

    private EdicolaContainer edicolaList;

    private Vector<KeepAliveAgent> agentPool;

    private MasterAgent masterAgent;

    private RecoveryReceiverMonitor control;

    private main.Main newEdicola;

    private static EdicolaManager instance;

    /**
	 * Costruttore privato
	 */
    private EdicolaManager() {
        newEdicolaToSubscribe = false;
        newEdicolaToUnsubscribe = false;
        edicolaDown = false;
        edicolaUp = false;
        udpPortForKeepAlive = 6000;
        agentPool = new Vector<KeepAliveAgent>();
        control = RecoveryReceiverMonitor.getInstance();
        entityName = control.getEntityName();
        recoveryTcpPort = control.getTcpPort();
        firstControllerHostName = control.getFirstControllerHostName();
        firstControllerTcpPort = control.getFirstControllerTcpPort();
        edicolaList = EdicolaContainer.getInstance();
        File root = new File(rootPath);
        if (!root.exists()) root.mkdir();
    }

    /**
	 * Pattern singleton (deve esistere una sola istanza di questa classe)
	 * @return riferimento all'unica istanza di questa classe
	 */
    public static EdicolaManager getInstance() {
        if (instance == null) instance = new EdicolaManager();
        return instance;
    }

    /**
	 * Metodo invocato dal Master per aggiungere o aggiornare una nuova edicola al 
	 * servizio di recovery
	 * @param message messaggio ricevuto dall'edicola
	 * @return id dell'edicola presso il servizio di recovery
	 */
    public String subscribeEdicola(String message) {
        lock.lock();
        EdicolaMessage msg = new EdicolaMessage();
        Edicola edicola = msg.getEdicolaFromEdicolaFormat(message);
        if (edicola.getRecoveryId() != null && (!edicola.getRecoveryId().equals(""))) {
            edicolaAlreadySubscribed(edicola);
        } else {
            edicola.generateNewID();
            if (!edicolaList.isEdicolaPresent(edicola)) {
                while (isPortUsed(udpPortForKeepAlive)) {
                    udpPortForKeepAlive++;
                }
                edicola.setUdpPort(udpPortForKeepAlive);
                edicolaList.addEdicola(edicola);
            } else {
                edicolaList.updateEdicola(edicola);
                edicolaList.updateCategorieEdicola(edicola, edicola.getCategorie());
            }
            File folder = new File(rootPath + edicola.getIpAddress() + "/");
            if (!folder.exists()) folder.mkdir();
            EdicolaMessage mess = new EdicolaMessage();
            String content = mess.createNewMessage(edicolaList.getEdicola(edicola.getRecoveryId()));
            mess.saveAsFile(edicola.getPathFile());
            if (control.getCurrentEntityRole().compareTo(Role.MASTER) == 0) {
                masterAgent = new MasterAgent(firstControllerTcpPort, firstControllerHostName);
                masterAgent.sendEdicolaBusinessCard(content);
                masterAgent.start();
                KeepAliveAgent agent = new KeepAliveAgent(udpPortForKeepAlive, edicola.getIpAddress());
                agent.start();
                agentPool.add(agent);
            }
            newEdicolaToSubscribe = true;
            waitingForRequests.signal();
        }
        lock.unlock();
        return edicola.getRecoveryId();
    }

    /**
	 * Metodo invocato dal Master per eliminare un'edicola dal servizio di recovery
	 * @param idEdicola id dell'edicola da cancellare
	 */
    public void unsubscribeEdicola(String idEdicola) {
        lock.lock();
        Edicola edicola = edicolaList.getEdicola(idEdicola);
        edicolaList.removeEdicola(edicola);
        File folder = new File(rootPath + edicola.getIpAddress() + "/");
        if (folder.exists()) {
            String[] listFile = folder.list();
            for (String s : listFile) {
                File filename = new File(folder.getPath() + "/" + s);
                filename.delete();
            }
            folder.delete();
        }
        EdicolaMessage message = new EdicolaMessage();
        String msg = message.createNewMessage(edicola);
        masterAgent = new MasterAgent(firstControllerTcpPort, firstControllerHostName);
        masterAgent.sendEdicolaToDelete(msg);
        masterAgent.start();
        for (int i = 0; i < agentPool.size(); i++) {
            KeepAliveAgent agent = agentPool.get(i);
            if (agent.getEdicolaAddress().equals(edicola.getIpAddress())) {
                agent.setFlag(true);
                udpPortForKeepAlive = agent.getPort();
                agentPool.remove(agent);
            }
        }
        newEdicolaToUnsubscribe = true;
        waitingForRequests.signal();
        lock.unlock();
    }

    /**
	 * Metodo invocato dal First Controller per aggiornare i dati di un'edicola iscritta 
	 * al servizio
	 * @param edicola edicola da aggiornare
	 * @param businessCard nuovi dati dell'edicola; se questa stringa � vuota, il First
	 * Controller lo interpreta come una richiesta di cancellazione dell'edicola dal suo
	 * database
	 * @return esito dell'operazione
	 */
    public String updateEdicola(Edicola edicola, String businessCard) {
        lock.lock();
        if (edicolaList.isEdicolaPresent(edicola)) {
            if (businessCard.equals("")) {
                Edicola e = edicolaList.getEdicola(edicola.getRecoveryId());
                edicolaList.removeEdicola(e);
                File folder = new File(rootPath + e.getIpAddress() + "/");
                if (folder.exists()) {
                    String[] listFile = folder.list();
                    for (String s : listFile) {
                        File filename = new File(folder.getPath() + "/" + s);
                        filename.delete();
                    }
                    folder.delete();
                }
                lock.unlock();
                return "ok";
            } else {
                edicolaAlreadySubscribed(edicola);
            }
        } else {
            edicolaList.addEdicola(edicola);
            File folder = new File(rootPath + edicola.getIpAddress() + "/");
            if (!folder.exists()) folder.mkdir();
            EdicolaMessage msg = new EdicolaMessage();
            msg.createNewMessage(edicolaList.getEdicola(edicola.getRecoveryId()));
            msg.saveAsFile(edicola.getPathFile());
        }
        lock.unlock();
        return "ok";
    }

    /**
	 * Metodo invocato sia dal Master che dal First Controller per aggiornare il catalogo
	 * di un'edicola iscritta al servizio
	 * @param idEdicola id dell'edicola da aggiornare
	 * @param msg nuovo catalogo dell'edicola
	 * @return esito dell'operazione
	 */
    public String updateCatalogoEdicola(String idEdicola, String msg) {
        lock.lock();
        CatalogoMessage cm = new CatalogoMessage();
        Vector<Articolo> list = cm.getCatalogo(msg);
        Edicola edicola = edicolaList.getEdicola(idEdicola);
        Catalogo cat = new Catalogo(edicola, list);
        cat.setLastUpdate(cm.getLastUpdateDate());
        edicola.setCatalogo(cat);
        edicolaList.updateCatalogoEdicola(edicola, cat);
        String message = cm.createMessage(cat);
        cm.saveAsFile(cat.getPathFile());
        if (control.getCurrentEntityRole().compareTo(Role.MASTER) == 0) {
            masterAgent = new MasterAgent(firstControllerTcpPort, firstControllerHostName);
            masterAgent.sendEdicolaCatalogo(idEdicola, message);
            masterAgent.start();
        }
        lock.unlock();
        return "ok";
    }

    /**
	 * Metodo invocato sia dal Master che dal First Controller per aggiornare la newsletter
	 * di un'edicola iscritta al servizio
	 * @param idEdicola id dell'edicola da aggiornare
	 * @param msg ultima newsletter dell'edicola
	 * @return esito dell'operazione
	 */
    public String updateNewsletterEdicola(String idEdicola, String msg) {
        lock.lock();
        NewsletterMessage mess = new NewsletterMessage();
        Vector<ClientSubscription> list = mess.getNewsletter(msg);
        Edicola edicola = edicolaList.getEdicola(idEdicola);
        edicolaList.updateNewsletterEdicola(edicola, list);
        String message = mess.createNewMessage(list);
        mess.saveAsFile(rootPath + "/" + edicola.getIpAddress() + "/newsletter_subscription.xml");
        if (control.getCurrentEntityRole().compareTo(Role.MASTER) == 0) {
            masterAgent = new MasterAgent(firstControllerTcpPort, firstControllerHostName);
            masterAgent.sendEdicolaNewsletter(idEdicola, message);
            masterAgent.start();
        }
        lock.unlock();
        return "ok";
    }

    /**
	 * Metodo invocato dall'ex First controller diventato Master (o dal Master stesso) 
	 * per inviare il database in suo possesso al nuovo First Controller
	 */
    public void synchronizeDatabase() {
        lock.lock();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        firstControllerTcpPort = control.getFirstControllerTcpPort();
        firstControllerHostName = control.getFirstControllerHostName();
        File folder = new File(rootPath);
        String[] directories = folder.list();
        for (int i = 0; i < directories.length; i++) {
            String ipEdicola = directories[i];
            Edicola edicola = edicolaList.findEdicola(ipEdicola);
            File edicolaDirectory = new File(folder.getPath() + "/" + ipEdicola);
            File[] listEdicolaFiles = edicolaDirectory.listFiles();
            File edicolaFile = findEdicolaFile(listEdicolaFiles);
            if (edicolaFile != null) {
                EdicolaMessage msg = new EdicolaMessage();
                String businessCard = msg.loadFromFile(edicola);
                String catalogo = "";
                String newsletter = "";
                File catalogoFile = findCatalogoFile(listEdicolaFiles);
                if (catalogoFile != null) {
                    CatalogoMessage mess = new CatalogoMessage();
                    catalogo = mess.createMessage(edicola.getCatalogo());
                }
                File newsletterFile = findNewsletterFile(listEdicolaFiles);
                if (newsletterFile != null) {
                    NewsletterMessage mess = new NewsletterMessage();
                    newsletter = mess.createNewMessage(edicola.getClienti());
                }
                masterAgent = new MasterAgent(firstControllerTcpPort, firstControllerHostName);
                masterAgent.sendEdicolaAll(edicola.getRecoveryId(), businessCard, catalogo, newsletter);
            }
        }
        System.out.println("Sincronizzazione database completata con successo");
        lock.unlock();
    }

    /**
	 * Metodo invocato dal nuovo master per creare nuovi agenti di keepalive per
	 * le edicole attive
	 */
    public void createNewKeepAlive() {
        lock.lock();
        NotifyAgent notify = new NotifyAgent(edicolaMulticastGroup, edicolaMulticastPort);
        notify.start();
        System.out.println("Creazione di nuovi agenti di keepalive...");
        KeepAliveAgent agent;
        for (int i = 0; i < edicolaList.size(); i++) {
            Edicola edicola = edicolaList.getEdicolaAt(i);
            int udpPort = edicola.getUdpPort();
            agent = new KeepAliveAgent(udpPort, edicola.getIpAddress());
            agent.start();
            agentPool.add(agent);
        }
        lock.unlock();
    }

    /**
	 * Metodo invocato dai KeepAliveAgent per notificare il sistema che un'edicola �
	 * caduta
	 * @param agent agente cui � scattata la timeout
	 */
    public void alert(KeepAliveAgent agent) {
        lock.lock();
        agentPool.remove(agent);
        String ipAddress = agent.getEdicolaAddress();
        Edicola edicola = edicolaList.findEdicola(ipAddress);
        File edicolaPath = new File(rootPath + edicola.getIpAddress() + "/");
        File[] fileList = edicolaPath.listFiles();
        newEdicola = new main.Main();
        newEdicola.setFiles(fileList);
        System.out.println("L'edicola caduta � " + edicola.getName());
        for (int i = 0; i < agentPool.size(); i++) {
            KeepAliveAgent keepAliveagent = agentPool.get(i);
            keepAliveagent.setFlag(true);
            while (keepAliveagent.isAlive()) ;
        }
        agentPool.clear();
        control.newAlertReceived();
        control = null;
        NotifyReceiver notifyAgent = new NotifyReceiver(1713);
        notifyAgent.start();
        edicolaDown = true;
        waitingForRequests.signal();
        lock.unlock();
    }

    /**
	 * Metodo invocato dal NotifyReceiver non appena riceve l'avviso dal master di recovery
	 * che la vecchia edicola � risalita e pu� smettere di sostituirla
	 */
    public void stopAlert() {
        lock.lock();
        try {
            System.out.println("Stopping all edicola services...");
            EdicolaControl edicolaControl = structure.EdicolaControl.getInstance();
            edicolaControl.stopService();
            System.out.println("Restarting all recovery services...");
            RecoveryReceiverMonitor rrc = RecoveryReceiverMonitor.getInstance();
            rrc.setEntityName(entityName);
            rrc.setTcpPort(recoveryTcpPort);
            rrc.start();
            File folder = new File(rootPath);
            File[] folderList = folder.listFiles();
            for (int i = 0; i < folderList.length; i++) {
                File edicolaFolder = folderList[i];
                File[] edicolaFolderList = edicolaFolder.listFiles();
                for (int j = 0; j < edicolaFolderList.length; j++) {
                    File file = edicolaFolderList[j];
                    file.delete();
                }
                edicolaFolder.delete();
            }
            edicolaUp = true;
            waitingForRequests.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void run() {
        lock.lock();
        try {
            EdicolaAgentListener listener = new EdicolaAgentListener();
            listener.start();
            EdicolaMulticastSender agent = new EdicolaMulticastSender();
            agent.start();
            while (!newEdicolaToSubscribe || !newEdicolaToUnsubscribe || !edicolaDown || !edicolaUp) {
                System.out.println("Edicola Manager sospeso in attesa di eventi...");
                waitingForRequests.await();
                if (newEdicolaToSubscribe) {
                    System.out.println("Manager: Nuova edicola iscritta correttamente al servizio!");
                    newEdicolaToSubscribe = false;
                } else if (newEdicolaToUnsubscribe) {
                    System.out.println("Manager: edicola cancellata correttamente dal servizio!");
                    newEdicolaToUnsubscribe = false;
                } else if (edicolaDown) {
                    System.out.println("EdicolaManager: edicola out!");
                    listener.stopListening();
                    agent.receiveMessage(null);
                    newEdicola.start();
                    edicolaDown = false;
                } else if (edicolaUp) {
                    System.out.println("Edicola tornata: torno a fare il recovery!");
                    newEdicolaToSubscribe = true;
                    newEdicolaToUnsubscribe = true;
                    edicolaDown = true;
                }
            }
            System.out.println("EdicolaManager: termino...");
            instance = null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    /**
	 * @return porta tcp alla quale il first controller � in attesa di ricevere aggiornamenti
	 * al suo database
	 */
    public int getFirstControllerTcpPort() {
        return firstControllerTcpPort;
    }

    /**
	 * Imposta la porta tcp alla quale il first controller � in attesa di ricevere 
	 * aggiornamenti al suo database
	 * @param firstControllerTcpPort nuova porta tcp
	 */
    public void setFirstControllerTcpPort(int firstControllerTcpPort) {
        this.firstControllerTcpPort = firstControllerTcpPort;
    }

    /**
	 * @return porta multicast di ascolto delle richieste di discovery dei clienti
	 */
    public int getEdicolaMulticastPort() {
        return edicolaMulticastPort;
    }

    /**
	 * Imposta la porta multicast di ascolto delle richieste di discovery dei clienti
	 * @param edicolaTcpPort nuova porta multicast
	 */
    public void setEdicolaMulticastPort(int edicolaTcpPort) {
        this.edicolaMulticastPort = edicolaTcpPort;
    }

    /**
	 * @return indirizzo ip multicast in cui il Master riceve le richieste di discovery
	 * dei clienti
	 */
    public String getEdicolaMulticastGroup() {
        return edicolaMulticastGroup;
    }

    /**
	 * Imposta il nuovo indirizzo ip multicast in cui il Master riceve le richieste 
	 * di discovery dei clienti
	 * @param edicolaMulticastGroup nuovo indirizzo multicast
	 */
    public void setEdicolaMulticastGroup(String edicolaMulticastGroup) {
        this.edicolaMulticastGroup = edicolaMulticastGroup;
    }

    /**
	 * @return porta tcp di dialogo con le edicole
	 */
    public int getEdicolaAgentTcpPort() {
        return edicolaAgentTcpPort;
    }

    /**
	 * Imposta la porta tcp di dialogo con le edicole
	 * @param edicolaAgentTcpPort nuova porta tcp
	 */
    public void setEdicolaAgentTcpPort(int edicolaAgentTcpPort) {
        this.edicolaAgentTcpPort = edicolaAgentTcpPort;
    }

    /**
	 * @return ultima porta udp utilizzata per i messaggi di keepalive provenienti
	 * da una generica edicola
	 */
    public int getUdpPortForKeepAlive() {
        return udpPortForKeepAlive;
    }

    /**
	 * Metodo che rintraccia il file edicola.xml all'interno di una cartella
	 * @param fileList elenco di file presenti nella cartella
	 * @return file edicola.xml se presente, altrimenti null
	 */
    private File findEdicolaFile(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];
            if (file.getName().equals("edicola.xml")) return file;
        }
        return null;
    }

    /**
	 * Metodo che rintraccia il file catalogo.xml all'interno di una cartella
	 * @param fileList elenco di file presenti nella cartella
	 * @return file catalogo.xml se presente, altrimenti null
	 */
    private File findCatalogoFile(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];
            if (file.getName().equals("catalog.xml")) return file;
        }
        return null;
    }

    /**
	 * Metodo che rintraccia il file newsletter_subscription.xml all'interno di una cartella
	 * @param fileList elenco di file presenti nella cartella
	 * @return file newsletter_subscription.xml se presente, altrimenti null
	 */
    private File findNewsletterFile(File[] fileList) {
        for (int i = 0; i < fileList.length; i++) {
            File file = fileList[i];
            if (file.getName().equals("newsletter_subscription.xml")) return file;
        }
        return null;
    }

    /**
	 * Metodo che rintraccia lo specifico agente di keepalive in ascolto dei messaggi
	 * provenienti dall'edicola presente all'indirizzo ip specificato
	 * @param ipAddress indirizzo ip dell'edicola
	 * @return l'agente di keepalive in ascolto dei messaggi della specifica edicola se
	 * presente, altrimenti null
	 */
    private KeepAliveAgent findAgent(String ipAddress) {
        KeepAliveAgent agent;
        for (int i = 0; i < agentPool.size(); i++) {
            agent = agentPool.get(i);
            if (agent.getEdicolaAddress().equals(ipAddress)) return agent;
        }
        return null;
    }

    /**
	 * Metodo utilizzato per verificare se la specifica porta � gi� utilizzata da qualche
	 * agente di keepalive del sistema
	 * @param port porta da verificare
	 * @return true se la porta � libera, false altrimenti
	 */
    private boolean isPortUsed(int port) {
        KeepAliveAgent agent;
        for (int i = 0; i < agentPool.size(); i++) {
            agent = agentPool.get(i);
            if (agent.getPort() == port) return true;
        }
        return false;
    }

    /**
	 * Procedura eseguita sia dal master che dal first controller per aggiornare i dati
	 * di un'edicola gi� iscritta al servizio
	 * @param edicola edicola da aggiornare
	 */
    private void edicolaAlreadySubscribed(Edicola edicola) {
        lock.lock();
        Edicola e = edicolaList.getEdicola(edicola.getRecoveryId());
        if (!e.getIpAddress().equals(edicola.getIpAddress())) {
            File folder = new File(rootPath + e.getIpAddress() + "/");
            if (folder.exists()) folder.renameTo(new File(rootPath + edicola.getIpAddress() + "/"));
            if (control.getCurrentEntityRole().compareTo(Role.MASTER) == 0) {
                KeepAliveAgent agent = findAgent(e.getIpAddress());
                int port = 0;
                if (agent != null) {
                    agent.setFlag(true);
                    port = agent.getPort();
                    while (agent.isAlive()) ;
                    agentPool.remove(agent);
                }
                KeepAliveAgent keepAlive;
                if (port != 0) keepAlive = new KeepAliveAgent(port, edicola.getIpAddress()); else {
                    while (isPortUsed(udpPortForKeepAlive)) {
                        udpPortForKeepAlive++;
                    }
                    keepAlive = new KeepAliveAgent(udpPortForKeepAlive, edicola.getIpAddress());
                }
                keepAlive.start();
                agentPool.add(keepAlive);
                if (e.isReplica()) {
                    UpdateFileAgent updateAgent = new UpdateFileAgent(edicola.getIpAddress(), edicola.getUpdatePort());
                    updateAgent.setFiles(edicola);
                    updateAgent.start();
                    NotifyRecovery notifyAgent = new NotifyRecovery(e.getIpAddress(), 1713);
                    notifyAgent.start();
                }
            }
            e.setIpAddress(edicola.getIpAddress());
        }
        edicolaList.updateEdicola(edicola);
        edicolaList.updateCategorieEdicola(edicola, edicola.getCategorie());
        EdicolaMessage mess = new EdicolaMessage();
        String m = mess.createNewMessage(edicolaList.getEdicola(edicola.getRecoveryId()));
        mess.saveAsFile(edicola.getPathFile());
        if (control.getCurrentEntityRole().compareTo(Role.MASTER) == 0) {
            masterAgent = new MasterAgent(firstControllerTcpPort, firstControllerHostName);
            masterAgent.sendEdicolaBusinessCard(m);
            masterAgent.start();
        }
        lock.unlock();
    }
}
