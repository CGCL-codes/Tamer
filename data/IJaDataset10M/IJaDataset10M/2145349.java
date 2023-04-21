package Shifu.MainServer.Net;

import java.io.*;
import java.sql.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.server.Unreferenced;
import java.util.ArrayList;
import Shifu.MainServer.Management.*;
import Shifu.MainServer.Tools.*;
import Shifu.Login.DBInteraction;
import Shifu.Login.LoginServerImpl;
import Shifu.Login.LoginInterface;
import java.rmi.activation.*;
import java.rmi.MarshalledObject;

/**
 * Server centrale, gestisce tutta la distribuzione del file	
 * @author vr069316
 */
public class MainServer extends Activatable implements Seeder, Leecher, Unreferenced {

    private FileTable filetable;

    private PeersRing ring;

    private Obreros obreros;

    private DBInteraction db;

    private int peernumber = 0;

    private ActivationID id = null;

    /**
	 * Istanzia ed esporta il Main server
	 * @param filetable la tabella dei file
	 * @param ring l'anello dei peers
	 * @param obreros obreros 
	 * @exception RemoteException nel caso ci siano problemi di connessioni
	 */
    public MainServer(ActivationID id, MarshalledObject data) throws RemoteException, ActivationException {
        super(id, 3511);
        if (data == null) {
            this.filetable = new FileTable();
            this.ring = new PeersRing();
            this.obreros = new Obreros(filetable, ring);
            this.db = new DBInteraction();
            this.id = id;
        } else {
            try {
                ReactivationDataMainServer redata = (ReactivationDataMainServer) data.get();
                this.filetable = (FileTable) redata.getFileTable();
                this.ring = (PeersRing) redata.getPeersRing();
                this.obreros = (Obreros) redata.getObreros();
                this.db = (DBInteraction) redata.getDBInteraction();
                this.peernumber = (int) redata.getpeernumber();
                this.id = id;
                obreros.startSonar();
            } catch (IOException ioe) {
                System.out.println("Errore nella demarshalizzazione dai MainServer" + ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.out.println("Errore classe non trovata " + cnfe.getMessage());
            }
        }
        System.out.println("Sono nel costruttore del MainServer");
    }

    /**
	 * Aggiunge un peer al sistema
	 * @param stub lo stub del peer
	 * @param username dell'utente
	 * @return l'id che il client usera
	 * @exception RemoteException nel caso ci siano problemi di connessioni
	 */
    public int add(Remote stub, String username) throws RemoteException {
        System.out.println("MainServer: Chiamato add peer del main server");
        PeerServer stubpeer = (PeerServer) stub;
        System.out.println(stubpeer.areYouAlive());
        Peer newpeer = new Peer(Integer.toString(peernumber), stubpeer, username);
        obreros.insertPeer(newpeer);
        int oldpeernum = peernumber;
        peernumber++;
        return oldpeernum;
    }

    /**
	 * @see MainServer.Net.Seeder#promovePeer
         */
    public int promovePeer(int i) throws RemoteException {
        System.out.println("MainServer: Chiamato promove del main server\nTentativo di promuovere a Seeder il peer con id : " + i);
        Peer peer = ring.getPeerbyId(i);
        if (peer == null) {
            System.out.println("Impossibile trovate il peer");
            return -1;
        }
        String username = peer.getUsername();
        boolean checkSeeder = false;
        try {
            checkSeeder = db.isSeeder(username);
        } catch (SQLException sqle) {
            System.out.println("Errore SQL durante promozione file !");
        }
        if (checkSeeder) {
            System.out.println("MainServer: Impossibile promuovere peer, è gia un Seeder");
            return -1;
        }
        System.out.println("MainServer: Peer verificato, procedo con la promozione!");
        PromotionSet ps = new PromotionSet((Seeder) this, new SeederShell(username));
        boolean check = false;
        try {
            check = db.promovePeer(username);
            System.out.println("MainServer: Database aggiornato!");
        } catch (SQLException sqle) {
            System.out.println("Errore SQL durante promozione file !");
        }
        if (check) {
            System.out.println("MainServer: Invio richiesta al promosso!");
            PeerServer peerstub = peer.getStub();
            int ok = peerstub.getPromotion(ps);
            if (ok > 0) {
                System.out.println("Promozione peer : " + username + " avvenuta");
                return 1;
            }
        }
        System.out.println("Promozione peer : " + username + " fallita");
        return -1;
    }

    /**
	 * Rimuove un file dal sistema
	 * @param index indice del file da rimuovere
	 * @return 0 se ha avuto successo -1 altrimenti
	 * @exception RemoteException nel caso ci siano problemi di connessioni
	 */
    public int removeFile(int index) throws RemoteException {
        System.out.println("MainServer: Richiesta di rimozione file di indice: " + index);
        return (obreros.removeFile(index));
    }

    /**
	 * Salva un file sul sistema
	 * @param fildes descrittore del file da salvare
	 * @param buffer il file da salvare
	 * @return 1 se lo storage è andato a buon fine -1 altrimenti	
	 * @exception RemoteException nel caso ci siano problemi di connessioni
	 */
    public int storageFile(SFileDescriptor filedes, byte[] buffer) throws RemoteException {
        System.out.println("MainServer: Richiesta di archiviazione del file: " + filedes.getFileName());
        int result = obreros.storageFile(filedes, buffer);
        filetable.printAll();
        return result;
    }

    /**
	 * Restituisce un file del sistema richiesto
	 * @param index indice del file richiesto
	 * @return il file più il suo descrittore	
	 * @exception RemoteException nel caso ci siano problemi di connessioni	
	 */
    public MergedFile getFile(int index) throws RemoteException {
        System.out.println("MainServer: Richiesta di caricamento file di indice: " + index);
        return (obreros.loadFile(index));
    }

    /**
	 * Restituisce la lista dei file presenti sul sistema
	 * @return il file richiesto
	 * @exception RemoteException nel caso ci siano problemi di connessioni
	 */
    public String getFileList() throws RemoteException {
        System.out.println("MainServer: Richiesta lista dei file online");
        return obreros.getFileList();
    }

    /**
	 * Mostra tutti i peer presenti sul sistema
	 * @return lista dei peers connessi.
	 */
    public String getPeerList() {
        return obreros.getPeerList();
    }

    /**
	 * Elimina il server una volta che non ci sono referenze al server
	 */
    public void unreferenced() {
        System.out.println("MainServer: mi disattivo poichè non ho più referenze");
        try {
            if (!Activatable.inactive(this.id)) {
                System.out.println("MainServer: ci sono dei peers ancora attivi");
            }
        } catch (Exception e) {
            System.out.println("MainServer: Errore nella disattivazione del server");
            e.printStackTrace();
        }
        System.out.println("MainServer: Salvattaggio dello stato !");
        MarshalledObject data = null;
        ActivationSystem as = null;
        try {
            as = ActivationGroup.getSystem();
            data = new MarshalledObject(new ReactivationDataMainServer(this.filetable, this.ring, this.obreros, this.db, this.peernumber));
            ActivationDesc actD = as.getActivationDesc(this.id);
            ActivationDesc nad = new ActivationDesc(actD.getGroupID(), actD.getClassName(), actD.getLocation(), data);
            as.setActivationDesc(this.id, nad);
        } catch (RemoteException e) {
            System.out.println("non riesco a contattare il sistema di attivazione");
        } catch (IOException e) {
            System.out.println("non riesco a serializzare il mio stato, quindi mi deregistro dal sistema di attivazione");
            e.printStackTrace();
            try {
                as.unregisterObject(this.id);
            } catch (ActivationException g) {
                System.out.println("non riesco a farmi deregistrare dal sistema di attivazione");
            } catch (RemoteException g) {
                System.out.println("non riesco a contattare il sistema di attivazione");
            }
        } catch (ActivationException e) {
            System.out.println("se anche il sistema di attivazione sta girando ha comunque problemi seri");
            e.printStackTrace();
        }
    }
}
