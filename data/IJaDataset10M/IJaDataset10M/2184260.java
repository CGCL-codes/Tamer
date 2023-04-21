package peer;

import condivisi.Descrittore;
import condivisi.ErrorException;
import condivisi.InterfacciaCallback;
import condivisi.InterfacciaRMI;
import condivisi.NetRecord;
import gui.BitCreekGui;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Parte client del protocollo BitCreek
 * @author Bandettini Alberto
 * @author Lottarini Andrea
 * @version BitCreekPeer 1.0
 */
public class BitCreekPeer {

    /** Definisce la costanta NULL*/
    private final int NULL = -1;

    /** Definisce il tempo di attesa della risposta del server */
    private final int ATTESARISPSERVER = 3000;

    /** Definisce la porta di ascolto del server */
    private final int PORTASERVER = 9999;

    /** Definisce la porta di RMI */
    private final int PORTARMI = 10000;

    /** Definisce la costante FINITO */
    private final int FINITO = 100;

    /** Numero max di thread */
    private final int NUMTHREAD = 100;

    /** Definsce la dimensione di un blocco da trasferire */
    public static final int DIMBLOCCO = 4096;

    /** Definisce il numero max di connessioni */
    protected static final int MAXCONNESSIONI = 100;

    /** Definisce il tempo di attesa su una connessione */
    protected static final int TIMEOUTCONNESSIONE = 500;

    /** Mio ip */
    private InetAddress mioip;

    /** Ip del server, null se peer disconnesso */
    private InetAddress ipServer;

    /** Socket di benvenuto */
    private ServerSocket welcome;

    /** Porta della socket di benvenuto */
    private int portarichieste;

    /** Array di creek */
    private ArrayList<Creek> arraydescr;

    /** Array di descrittori risultanti dall'ultima ricerca */
    private ArrayList<Descrittore> cercati;

    /** booleano per sapere se il thread per i keepalive è avviato */
    private boolean keepalive;

    /** booleano per sapere se il thread per in ascolto su welcome e è avviato */
    private boolean ascolto;

    /** booleano per sapere se il peer è dietro NAT o firewall */
    private boolean bloccato;

    /** Interfaccia RMI */
    private InterfacciaRMI stub;

    /** Interfaccia per le callback */
    private InterfacciaCallback stubcb;

    /** Implementazione delle callback */
    private ImplementazioneCallback callback;

    /** ThreadPool per il p2p*/
    private ExecutorService TP;

    /** Numero di connessioni aperte */
    private int connessioni;

    /**
     * Costruttore
     * @exception ErrorException se no è possibile creare
     * la parte client del protocollo
     */
    public BitCreekPeer() throws ErrorException {
        mioip = null;
        portarichieste = NULL;
        ipServer = null;
        welcome = null;
        arraydescr = new ArrayList<Creek>();
        cercati = null;
        keepalive = false;
        ascolto = false;
        bloccato = true;
        stub = null;
        stubcb = null;
        callback = null;
        connessioni = 0;
        TP = Executors.newFixedThreadPool(NUMTHREAD);
        File dir = new File("./FileCondivisi");
        dir.mkdir();
        dir = new File("./MetaInfo");
        dir.mkdir();
        File[] array = dir.listFiles();
        ObjectInputStream in = null;
        Creek c = null;
        for (File f : array) {
            try {
                in = new ObjectInputStream(new FileInputStream(f));
                c = (Creek) in.readObject();
                arraydescr.add(c);
                in.close();
            } catch (FileNotFoundException ex) {
                throw new ErrorException("File not found");
            } catch (IOException ex) {
                throw new ErrorException("IO Problem");
            } catch (ClassNotFoundException ex) {
                throw new ErrorException("Class not found");
            }
        }
    }

    /**
     * Contatta i peer nella lista e, se raggiungibili, crea nuove connessioni
     * da aggiungere al creek passato come parametro
     * @param c creek
     * @param lista lista di peer
     */
    public synchronized void aggiungiLista(Creek c, ArrayList<NetRecord> lista) {
        for (NetRecord n : lista) {
            try {
                if (this.getConnessioni() >= BitCreekPeer.MAXCONNESSIONI) {
                    break;
                }
                if (n.getPorta() == this.portarichieste && n.getIp().getHostAddress().compareTo(this.mioip.getHostAddress()) == 0) {
                    continue;
                }
                Connessione conn;
                Connessione toModify = c.presenzaConnessione(n.getIp(), n.getPorta());
                if (toModify == null) {
                    SocketAddress sa = new InetSocketAddress(n.getIp(), n.getPorta());
                    Socket sock = new Socket();
                    sock.connect(sa, BitCreekPeer.TIMEOUTCONNESSIONE);
                    Bitfield b = new Bitfield(null);
                    ObjectOutputStream contactOUT = new ObjectOutputStream(sock.getOutputStream());
                    ObjectInputStream contactIN = new ObjectInputStream(sock.getInputStream());
                    conn = new Connessione();
                    conn.set(true, sock, contactIN, contactOUT, b.getBitfield(), n.getPorta());
                    c.addConnessione(conn);
                    contactOUT.writeObject(new Contact(this.mioip, this.portarichieste, c.getId()));
                    try {
                        b = (Bitfield) contactIN.readObject();
                        conn.setBitfield(b.getBitfield());
                        c.addRarita(b.getBitfield());
                    } catch (ClassNotFoundException ex) {
                        System.err.println("Avvia : Classnotfound");
                    }
                    this.addTask(new Downloader(c, conn, this));
                    this.incrConnessioni();
                    c.incrPeer();
                } else {
                    conn = toModify;
                    if (conn.DownNull()) {
                        SocketAddress sa = new InetSocketAddress(n.getIp(), n.getPorta());
                        Socket sock = new Socket();
                        sock.connect(sa, BitCreekPeer.TIMEOUTCONNESSIONE);
                        Bitfield b = new Bitfield(null);
                        ObjectOutputStream contactOUT = new ObjectOutputStream(sock.getOutputStream());
                        ObjectInputStream contactIN = new ObjectInputStream(sock.getInputStream());
                        conn.set(true, sock, contactIN, contactOUT, b.getBitfield(), n.getPorta());
                        contactOUT.writeObject(new Contact(this.mioip, this.portarichieste, c.getId()));
                        try {
                            b = (Bitfield) contactIN.readObject();
                            conn.setBitfield(b.getBitfield());
                            c.addRarita(b.getBitfield());
                        } catch (ClassNotFoundException ex) {
                            System.err.println("Avvia : Classnotfound");
                        }
                        this.addTask(new Downloader(c, conn, this));
                        this.incrConnessioni();
                        c.incrPeer();
                    }
                }
            } catch (IOException ex) {
                continue;
            }
        }
    }

    /**
     * Metodo che si occupa di contattare il tracker TCP di uno swarm
     * @param d descrittore dello swarm
     * @return lista di peer dello swarm
     */
    public ArrayList<NetRecord> contattaTracker(Descrittore d) {
        ArrayList<NetRecord> lista = new ArrayList<NetRecord>();
        SSLSocket s = null;
        ObjectInputStream oin = null;
        int portatracker = d.getTCP();
        try {
            s = (SSLSocket) SSLSocketFactory.getDefault().createSocket(this.ipServer, portatracker);
            oin = new ObjectInputStream(s.getInputStream());
            int dimlista = oin.readInt();
            for (int j = 0; j < dimlista; j++) {
                lista.add((NetRecord) oin.readObject());
            }
            s.close();
        } catch (ClassNotFoundException ex) {
            System.err.println("Avvia : Classnotfound");
        } catch (IOException ex) {
            System.err.println("Avvia : IOexception");
        }
        return lista;
    }

    /**
     * Restituisce il numero di connessioni
     * @return connessioni
     */
    public synchronized int getConnessioni() {
        return this.connessioni;
    }

    /**
     * Incrementa il numero di connessioni
     */
    public void incrConnessioni() {
        this.connessioni++;
    }

    /**
     * Decrementa il numero di connessioni
     */
    public void decrConnessioni() {
        this.connessioni--;
    }

    /**
     * Restituisce l' ip del peer
     * @return mioip
     */
    public InetAddress getMioIp() {
        return this.mioip;
    }

    /**
     * Aggiunge un task al pool
     * @param r task da eseguire
     */
    public synchronized void addTask(Runnable r) {
        this.TP.execute(r);
    }

    /**
     * Restituisce il numero della porta in ascolto del peer
     * @return portarichieste
     */
    public int getPortaRichieste() {
        return this.portarichieste;
    }

    /**
     * Restituisce l'ip del server
     * @return ipServer
     */
    public InetAddress getIpServer() {
        return this.ipServer;
    }

    /**
     * Restituisce true se il peer è sotto firewall o nat, false altrimenti
     * @return bloccato
     */
    public boolean getBloccato() {
        return this.bloccato;
    }

    /**
     * Restituisce lo stub per le callback
     * @return stubcb
     */
    public InterfacciaCallback getStubCb() {
        return this.stubcb;
    }

    /**
     * Restituisce la socket in ascolto
     * @return welcome
     */
    public ServerSocket getSS() {
        return this.welcome;
    }

    /**
     * Restituisce lo stub di RMI
     * @return stub
     */
    public InterfacciaRMI getStub() {
        return this.stub;
    }

    /**
     * Restituisce una copia dell' array dei file cercati
     * @return ris array di descrittori
     * @throws condivisi.ErrorException se la copia di un descrittore fallisce
     */
    public synchronized ArrayList<Descrittore> getCercati() throws ErrorException {
        ArrayList<Descrittore> ris = new ArrayList<Descrittore>();
        for (Descrittore d : cercati) {
            Descrittore nuovo = d.copia();
            ris.add(nuovo);
        }
        return ris;
    }

    /**
     * Restituisce tutti i creek presenti nel peer
     * @return arraydescr
     */
    public synchronized ArrayList<Creek> getDescr() {
        return this.arraydescr;
    }

    /**
     * Setta l' array dei file cercati al parametro results
     * @param results
     */
    public synchronized void setCercati(ArrayList<Descrittore> results) {
        this.cercati = results;
    }

    /**
     * Controlla se la porta specificata è libera e in caso
     * affermativa scrive il numero su file
     * @param porta da controllare
     * @return true se è libera e av tutto bene, false altrimenti
     */
    public boolean settaporta(int porta) {
        boolean ris = true;
        try {
            DatagramSocket s = new DatagramSocket(porta);
        } catch (BindException e) {
            ris = false;
        } catch (SocketException e) {
            ris = false;
        }
        if (ris) {
            File f = new File("./porta.conf");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                out.write(new byte[] { (byte) (porta >>> 24), (byte) (porta >>> 16), (byte) (porta >>> 8), (byte) porta });
                out.close();
            } catch (FileNotFoundException ex) {
                f.delete();
                ris = false;
            } catch (IOException ex) {
                f.delete();
                ris = false;
            }
        }
        return ris;
    }

    /**
     * Controlla se esiste un creek con il nome nome
     * @param nome
     * @return true se esiste già, false altrimenti
     */
    public synchronized boolean presenza(String nome) {
        boolean presenza = false;
        for (Creek c : arraydescr) {
            if (c.getName().compareTo(nome) == 0) {
                presenza = true;
                break;
            }
        }
        return presenza;
    }

    /**
     * Ricerca e ritorna un creek in base all'id univoco passato come parametro
     * se non lo trova ritorna null
     * @param id
     * @return il Creek cercato se c'è ; altrimenti null
     */
    public synchronized Creek getCreek(int id) {
        Creek ret = null;
        for (Creek c : arraydescr) {
            if (c.getId() == id) {
                return c;
            }
        }
        return ret;
    }

    /**
     * Avvia il download dei file selezionati nella ricerca
     * facendo partire un thread apposito
     * @param array indici dei file da scaricare
     */
    public synchronized void avviaDescr(int[] array) {
        Thread t = new Thread(new Avvia(this, array));
        t.start();
    }

    /**
     * Aggiunge ad arraydescr un creek se non è già presente
     * @param creek
     * @return true se tutto ok, false se è già presente
     * @throws condivisi.ErrorException se c è null
     */
    public synchronized boolean addCreek(Creek creek) throws ErrorException {
        if (creek == null) {
            throw new ErrorException("Param null");
        }
        boolean trovato = this.presenza(creek.getName());
        if (!trovato) {
            FileOutputStream c = null;
            ObjectOutputStream o = null;
            try {
                c = new FileOutputStream(new File("./MetaInfo/" + creek.getName() + ".creek"));
                Creek toBeWritten = creek.copia();
                toBeWritten.setClean();
                o = new ObjectOutputStream(c);
                o.writeObject(toBeWritten);
                c.close();
                o.close();
            } catch (FileNotFoundException ex) {
                File f = new File("./MetaInfo/" + creek.getName() + ".creek");
                f.delete();
                throw new ErrorException("Impossibile aggiungere il file: FILE NOT FOUND");
            } catch (IOException e) {
                File f = new File("./MetaInfo/" + creek.getName() + ".creek");
                f.delete();
                throw new ErrorException("Impossibile aggiungere il file: IOEXCEPTION");
            }
            arraydescr.add(creek);
        }
        return !trovato;
    }

    /**
     * Cancella un creek in arraydescr se presente
     * @param nome nome del creek da cancellare
     * @return true se tutto ok, false altrimenti
     * @throws condivisi.ErrorException se nome è null
     */
    public synchronized boolean deleteCreek(String nome) throws ErrorException {
        if (nome == null) {
            throw new ErrorException("Param null");
        }
        int pos = 0;
        boolean rimosso = false;
        File f = null;
        for (Creek c : arraydescr) {
            if (c.getName().compareTo(nome) == 0) {
                c.chiudi();
                arraydescr.remove(pos);
                if ((c.getStato() && c.getPercentuale() != FINITO) || !c.getStato()) {
                    f = new File("./FileCondivisi/" + nome);
                    f.delete();
                }
                rimosso = true;
                break;
            }
            pos++;
        }
        return rimosso;
    }

    /**
     * Metodo che viene invocato dall'implementazione callback per
     * aggiornare lo stato
     * @param ind IP da notificare
     * @param nome nome del creek da aggiornare
     */
    public synchronized void notifica(InetAddress ind, String nome) {
        for (Creek c : arraydescr) {
            if (c.getName().compareTo(nome) == 0) {
                c.settaPeerCerca();
                c.settaIdentita(ind);
                break;
            }
        }
    }

    /**
     * Handler di chiusura del protocollo
     */
    public void close() {
        disconnetti();
        File conf = new File("./avviato.on");
        conf.delete();
        for (Creek creek : arraydescr) {
            ObjectOutputStream o = null;
            try {
                o = new ObjectOutputStream(new FileOutputStream(new File("./MetaInfo/" + creek.getName() + ".creek")));
                o.writeObject(creek);
                o.close();
            } catch (FileNotFoundException ex) {
                File f = new File("./MetaInfo/" + creek.getName() + ".creek");
                f.delete();
            } catch (IOException e) {
                File f = new File("./MetaInfo/" + creek.getName() + ".creek");
                f.delete();
            }
        }
        System.exit(0);
    }

    /**
     * Chiude tutte le connessioni in upload e in download
     */
    private synchronized void terminaConn() {
        for (Creek creek : arraydescr) {
            creek.chiudi();
        }
        if (TP != null) {
            this.TP.shutdownNow();
            while (!TP.isTerminated()) {
                this.TP.shutdownNow();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.err.println("BitCreekPeer : sono stato interrotto");
                }
            }
        }
        this.TP = null;
    }

    /**
     * Disconnette il peer
     */
    public void disconnetti() {
        ipServer = null;
        portarichieste = NULL;
        stub = null;
        stubcb = null;
        callback = null;
        try {
            if (welcome != null) {
                welcome.close();
            }
        } catch (IOException ex) {
        }
        this.terminaConn();
    }

    /**
     * Handler che si occupa di far partire un task
     * che effettua la ricerca
     * @param nome del file da cercare
     * @param gui interfaccia grafica da aggiornare
     * @throws condivisi.ErrorException se almeno un parametro è null
     */
    public void cerca(String nome, BitCreekGui gui) throws ErrorException {
        if (nome == null || gui == null) {
            throw new ErrorException("Param null");
        }
        Thread t = new Thread(new Cerca(nome, this, gui));
        t.start();
    }

    /**
     * Fa partire un task che si occupa di creare e pubblicare un creek
     * @param sorgente file da pubblicare
     * @param gui interfaccia grafica da aggiornare
     * @exception condivisi.ErrorException se almeno un parametro è null
     */
    public void crea(File sorgente, BitCreekGui gui) throws ErrorException {
        if (sorgente == null || gui == null) {
            throw new ErrorException("Param null");
        }
        Thread t = new Thread(new Crea(sorgente, this, gui));
        t.start();
    }

    /**
     * Fa partire un task che si occupa ci aprire un .creek su disco
     * @param creek file .creek da aprire
     * @param gui interfaccia grafica da aggiornare
     * @throws condivisi.ErrorException se almeno un parametro è null
     */
    public void apri(File creek, BitCreekGui gui) throws ErrorException {
        if (creek == null || gui == null) {
            throw new ErrorException("Param null");
        }
        Thread t = new Thread(new Apri(creek, this, gui));
        t.start();
    }

    /**
     * Fa partitìre un task che si occupa di eliminare il
     * file con nome nome
     * @param nome nome del file da eliminare
     * @throws condivisi.ErrorException se nome è null
     */
    public void elimina(String nome) throws ErrorException {
        if (nome == null) {
            throw new ErrorException("Param null");
        }
        Thread t = new Thread(new Elimina(nome, this));
        t.start();
    }

    /**
     * Tenta di stabilire una connessione con il server
     * @param server ip del server
     * @param gui interfaccia grafica da aggiornare
     * @throws condivisi.ErrorException se almeno un parametro è null
     */
    public void connetti(InetAddress server, BitCreekGui gui) throws ErrorException {
        if (server == null || gui == null) {
            throw new ErrorException("Param null");
        }
        File f = new File("./porta.conf");
        byte[] b = new byte[20];
        try {
            FileInputStream in = new FileInputStream(f);
            in.read(b);
        } catch (FileNotFoundException ex) {
            throw new ErrorException("File porta.conf non trovato");
        } catch (IOException e) {
            throw new ErrorException("IO Problem");
        }
        int porta = (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8) + (b[3] & 0xFF);
        if (porta == 0) {
            throw new ErrorException("Porta non valida. Settarla dal menu Aiuto");
        }
        try {
            welcome = new ServerSocket(porta);
            welcome.setSoTimeout(ATTESARISPSERVER);
            portarichieste = welcome.getLocalPort();
            ipServer = server;
        } catch (UnknownHostException ex) {
            ipServer = null;
        } catch (IOException e) {
            ipServer = null;
        }
        if (ipServer != null) {
            try {
                Registry reg = LocateRegistry.getRegistry(ipServer.getHostAddress(), PORTARMI);
                stub = (InterfacciaRMI) reg.lookup("MetodiRMI");
                callback = new ImplementazioneCallback(this);
                stubcb = (InterfacciaCallback) UnicastRemoteObject.exportObject(callback, 0);
            } catch (NullPointerException e) {
                ipServer = null;
            } catch (RemoteException e) {
                ipServer = null;
            } catch (NotBoundException e) {
                ipServer = null;
            }
            if (!keepalive) {
                Thread t = null;
                try {
                    t = new Thread(new KeepAlive(this));
                } catch (ErrorException ex) {
                    ipServer = null;
                }
                if (ipServer != null) {
                    t.start();
                    keepalive = true;
                }
            }
        }
        if (ipServer == null) {
            try {
                if (welcome != null) {
                    welcome.close();
                }
            } catch (IOException ex) {
            }
            portarichieste = NULL;
            stub = null;
            stubcb = null;
            callback = null;
            throw new ErrorException("Porta non valida. Settarla dal menu Aiuto");
        } else {
            gui.connettiDone();
            if (this.TP == null) {
                this.TP = Executors.newFixedThreadPool(NUMTHREAD);
            }
            Thread t = new Thread(new Riavvia(this));
            t.start();
        }
    }

    /**
     * Fa partitìre un task che si occupa di salvare il
     * file con nome file nel path passato come parametro
     * @param path path dove salvare il file
     * @param file nome del file da salvare
     * @param cerca flag che indica se il creek da salvare è un descrittore
     * o un creek già presente
     * @throws condivisi.ErrorException se almeno un parametro è null
     */
    public void salva(String path, String file, boolean cerca) throws ErrorException {
        if (path == null || file == null) {
            throw new ErrorException("Param null");
        }
        File percorso = new File(path + file + ".creek");
        ObjectOutputStream output = null;
        try {
            output = new ObjectOutputStream(new FileOutputStream(percorso));
        } catch (FileNotFoundException ex) {
            percorso.delete();
            throw new ErrorException("Percorso non trovato");
        } catch (IOException e) {
            percorso.delete();
            throw new ErrorException("Impossibile creare creek");
        }
        if (cerca) {
            Descrittore d = this.getFileCerca(file);
            if (d == null) {
                percorso.delete();
                throw new ErrorException("File non trovato");
            } else {
                Creek c = new Creek(d, true, false).esporta();
                try {
                    output.writeObject(c);
                    output.close();
                } catch (IOException ex) {
                    Logger.getLogger(BitCreekPeer.class.getName()).log(Level.SEVERE, null, ex);
                    percorso.delete();
                    throw new ErrorException("Impossibile leggere metainfo");
                }
            }
        } else {
            ObjectInputStream in = null;
            Creek copia = null;
            try {
                in = new ObjectInputStream(new FileInputStream(new File("./MetaInfo/" + file + ".creek")));
                Creek c = (Creek) in.readObject();
                copia = c.esporta();
                output.writeObject(copia);
                output.close();
                in.close();
            } catch (IOException e) {
                percorso.delete();
                throw new ErrorException("Impossibile leggere metainfo");
            } catch (ClassNotFoundException e) {
                percorso.delete();
                throw new ErrorException("Impossibile leggere metainfo");
            }
        }
    }

    /**
     * Effettua una copia di un descrittore presente nell'array cercati
     * @param nome nome del creek da copiare
     * @return la copia del descrittore
     * @throws condivisi.ErrorException se nome è null
     */
    private synchronized Descrittore getFileCerca(String nome) throws ErrorException {
        if (nome == null) {
            throw new ErrorException("Param null");
        }
        Descrittore d = new Descrittore();
        for (Descrittore temp : cercati) {
            if (temp.getName().compareTo(nome) == 0) {
                d = temp.copia();
                break;
            }
        }
        return d;
    }

    /**
     * Effettua il test NAT - Firewall
     * @param gui interfaccia da aggiornare
     * @throws ErrorException se qualcosa non va
     */
    public void test(BitCreekGui gui) throws ErrorException {
        Socket s = null;
        boolean problema = false;
        if (ipServer != null) {
            try {
                SocketAddress sa = new InetSocketAddress(ipServer, PORTASERVER);
                s = new Socket();
                s.connect(sa, ATTESARISPSERVER);
            } catch (IOException e) {
                disconnetti();
                problema = true;
            }
        }
        if (ipServer != null && !problema) {
            mioip = s.getLocalAddress();
            try {
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
                out.writeInt(portarichieste);
                s.close();
            } catch (IOException e) {
                problema = true;
            }
            if (!problema) {
                bloccato = false;
                try {
                    Socket prova = welcome.accept();
                    prova.close();
                } catch (SocketTimeoutException ex) {
                    bloccato = true;
                } catch (IOException ex) {
                    bloccato = true;
                    problema = true;
                }
            }
        }
        if (ipServer != null && !problema) {
            if (!bloccato) {
                if (!ascolto) {
                    Thread t = null;
                    try {
                        t = new Thread(new Ascolto(this));
                    } catch (ErrorException ex) {
                        bloccato = true;
                        problema = true;
                    }
                    t.start();
                    ascolto = true;
                }
            }
        }
        if (problema) {
            throw new ErrorException("Test fallito");
        } else {
            gui.testDone(bloccato, portarichieste);
        }
    }
}
