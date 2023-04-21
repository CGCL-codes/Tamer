package Shifu.Login;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.RMISecurityManager;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.sql.*;
import java.rmi.server.*;
import Shifu.MainServer.Net.Leecher;
import Shifu.MainServer.Net.Seeder;
import Shifu.MainServer.Net.SeederShellPeerServer;
import Shifu.MainServer.Net.LeecherShellPeerServer;
import Shifu.MainServer.Net.*;
import java.lang.Runnable;
import java.rmi.activation.*;
import java.rmi.MarshalledObject;
import java.io.IOException;
import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

/**
 * @see LoginInterface
 */
public class LoginServerImpl extends Activatable implements LoginInterface, Unreferenced {

    private DBInteraction db = null;

    private Leecher leecherstubmainserver = null;

    private Seeder seederstubmainserver = null;

    private ActivationID id = null;

    public LoginServerImpl(ActivationID id, MarshalledObject seederstubmainserver) throws RemoteException {
        super(id, 3513, new SslRMIClientSocketFactory(), new SslRMIServerSocketFactory());
        this.id = id;
        db = new DBInteraction();
        System.out.println("Sono nel costruttore del LoginServer");
        try {
            this.leecherstubmainserver = (Leecher) seederstubmainserver.get();
            this.seederstubmainserver = (Seeder) seederstubmainserver.get();
        } catch (IOException ioe) {
            System.out.println("Errore nella demarshalizzazione degli stub " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Errore classe non trovata " + cnfe.getMessage());
        }
    }

    /**
	 * @see LoginInterface#loginSeeder
	 */
    public SeederShellPeerServer loginSeeder(String username, String password) throws RemoteException, SQLException {
        System.out.println("Login di un peer di tipo Seeder");
        if (db.checkSeeder(username, password)) {
            SeederShell seedershell = new SeederShell(username);
            SeederShellPeerServer mobileserver = new PeerServerImpl(leecherstubmainserver, seedershell);
            return mobileserver;
        }
        return null;
    }

    /**
	 * @see LoginInterface#loginLeecher
	 */
    public LeecherShellPeerServer loginLeecher(String username, String password) throws RemoteException, SQLException {
        System.out.println("Login di un peer di tipo Leecher");
        if (db.checkLeecher(username, password)) {
            LeecherShell leechershell = new LeecherShell(username);
            LeecherShellPeerServer mobileserver = new PeerServerImpl(leecherstubmainserver, leechershell);
            return mobileserver;
        }
        return null;
    }

    /**
	 * @see LoginInterface#registrate
	 */
    public boolean registratePeer(String username, String password) throws RemoteException, SQLException {
        System.out.println("Registrazione del peer : " + username);
        return (db.addPeer(username, password));
    }

    /**
	 * @see LoginInterface#cleanPeer
	 */
    public boolean removePeer(String username, String password) throws RemoteException, SQLException {
        System.out.println("Rimozione di un peer : " + username);
        return (db.removePeer(username, password));
    }

    /**
	 * Elimina il server una volta che non ci sono referenze ad esso
	 */
    public void unreferenced() {
        System.out.println("LoginServerImpl: mi disattivo poichè non ho più referenze");
        try {
            if (!Activatable.inactive(this.id)) {
                System.out.println("LoginServerImpl: ci sono dei peers ancora attivi");
            }
        } catch (Exception e) {
            System.out.println("LoginServerImpl: Errore nella disattivazione del server");
        }
    }
}
