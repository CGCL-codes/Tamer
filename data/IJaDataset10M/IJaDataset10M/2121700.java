package framework.core;

import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import framework.core.fs.Node;
import framework.core.fs.UserNode;
import framework.core.logic.Game;
import framework.core.logic.LogHandler;
import framework.core.logic.Match;
import framework.core.logic.ServerPong;
import framework.core.logic.Tournament;
import framework.core.messaging.MessageService;
import framework.core.messaging.ServerMessage;
import framework.core.usermanager.Account;
import framework.core.usermanager.LoginServer;
import framework.core.usermanager.User;
import framework.core.usermanager.UserClient;
import framework.core.usermanager.UserServer;
import framework.core.usermanager.db.DbServer;

/**
 * 
 * @author Vincenzo Frascino
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GameServer extends UnicastRemoteObject implements LoginServer {

    private Node server;

    private Node accounts;

    private Node users;

    private Node tournaments;

    private Node games;

    private Node home;

    private Node etc;

    private Node fileserver;

    private LogHandler log;

    private GameConfiguration configuration;

    private Node root;

    private static GameServer instance = null;

    /**
	 * Costruttore della classe GameServer.
	 */
    private GameServer() throws InstantiationException, RemoteException {
        super();
        if (instance == null) {
            instance = this;
            root = new Node("");
            server = root.addChild(new Node("server"));
            accounts = root.addChild(new Node("accounts"));
            users = root.addChild(new Node("users"));
            tournaments = root.addChild(new Node("tournaments"));
            games = root.addChild(new Node("games"));
            etc = server.addChild(new Node("etc"));
            home = server.addChild(new Node("home"));
            fileserver = server.addChild(new Node("fileserver"));
        } else throw new InstantiationException();
    }

    /**
	 * Restituisce l'istanza sigleton di GameServer per il processo corrente.
	 * Se un'istanza di GameServer non � ancora stata creata, getInstance genera e
	 * restituisce una nuova istanza singleton di GameServer.
	 * @return Un'istanza singleton di GameServer.
	 */
    public static GameServer getInstance() {
        if (instance == null) {
            try {
                return new GameServer();
            } catch (Exception e) {
            }
        }
        return instance;
    }

    /**
	 * Installa un log handler nel server.
	 * @param hnd Istanza del log handler da installare.
	 */
    public void attachLogHandler(LogHandler hnd) {
        log = hnd;
    }

    /**
	 * Invia un messaggio al log handler installato nel server. Se non esistono
	 * log handler installati, il metodo scarta il messaggio.
	 * @param msg
	 */
    public void log(String msg) {
        if (log != null) {
            log.onLogMessage(":: " + msg + "\n");
        }
    }

    /**
	 * Effettua l'inizializzazione di Arena, avvia il server del database e carica
	 * tutti i dati nell'albero dei nodi.
	 * @param cfg Opzioni di configurazione del server.
	 * @return true se L'inizializzazione del server ha avuto successo, 
	 * false altrimenti.
	 */
    public boolean init(GameConfiguration cfg) {
        try {
            log(":: GameServer init()");
            configuration = cfg;
            if (DbServer.getInstance().init(cfg.dbUrl, cfg.dbUser, cfg.dbPassword)) {
                restoreAccounts();
                restoreTournaments();
                restoreMatches();
                System.setSecurityManager(new RMISecurityManager());
                String name = "//localhost:" + cfg.port + "/GameServer";
                Naming.rebind(name, getInstance());
                log("GameServer: in ascolto su " + name);
                String message = "//localhost:" + cfg.port + "/MessageServer";
                Naming.rebind(message, MessageServer.init());
                log("MessageServer: in ascolto su " + message);
                String pingpong = "//localhost:" + cfg.port + "/PingPongServer";
                Naming.rebind(pingpong, PingPongServer.init());
                log("PingPongServer: in ascolto su " + pingpong);
                String fileserver = "//localhost:" + cfg.port + "/FileServer";
                Naming.rebind(fileserver, FileServer.init());
                log("FileServer: in ascolto su " + fileserver);
                log(":: GameServer init() ok.");
                return true;
            } else {
                log(":: GameServer init() fallito!");
            }
        } catch (java.net.MalformedURLException ex) {
            log(":: GameServer init() fallito: " + ex.toString());
        } catch (Exception e) {
            log(":: GameServer init() fallito: " + e.toString());
        }
        return false;
    }

    /**
	 * Termina la sessione del server.
	 */
    public void fini() {
        log(":: GameServer fini()");
        DbServer.getInstance().fini();
        kickAllUsers();
        log(":: GameServer fini() ok");
    }

    /**
	 * Espelle tutti gli utenti dal server.
	 *
	 */
    public void kickAllUsers() {
        Enumeration us = users.children();
        while (us.hasMoreElements()) {
            UserNode u = (UserNode) us.nextElement();
            u.getUser().kick();
        }
    }

    /**
         * Invia un messaggio a tutti gli utenti connessi.
         * @param msg Il messaggio da inviare.
         */
    public void broadcastMessage(ServerMessage msg) {
        Enumeration us = users.children();
        while (us.hasMoreElements()) {
            UserNode u = (UserNode) us.nextElement();
            u.getUser().send(msg);
        }
    }

    /**
	 * Restituisce il root node dell'albero degli oggetti
	 * @return
	 */
    public Node getRoot() {
        return root;
    }

    /**
	 * Carica i dati relativi agli account dal database.
	 */
    private void restoreAccounts() {
        Dictionary data = DbServer.getInstance().getTableData("accounts");
        Enumeration keys = data.keys();
        while (keys.hasMoreElements()) {
            String accountName = (String) keys.nextElement();
            Account acc = new Account();
            acc.setName(accountName);
            acc.setObjectData((Dictionary) data.get(accountName));
            accounts.addChild(acc);
        }
    }

    /**
	 * Carica i dati relativi agli account dal database.
	 */
    private void restoreTournaments() {
        Dictionary data = DbServer.getInstance().getTableData("tournaments");
        Enumeration keys = data.keys();
        while (keys.hasMoreElements()) {
            String tName = (String) keys.nextElement();
            Dictionary tData = (Dictionary) data.get(tName);
            int type = ((Integer) tData.get("Type")).intValue();
            String description = (String) tData.get("Description");
            Game game = (Game) root.find("/games/" + tData.get("Game"));
            Account owner = (Account) root.find("/accounts/" + tData.get("Owner"));
            Tournament t = new Tournament(tName, owner, type, game);
            tournaments.addChild(t);
            t.setObjectData(tData);
        }
    }

    /**
	 * Metodo che ricarica le partite all'avvio del server.
	 *
	 */
    private void restoreMatches() {
        Dictionary data = DbServer.getInstance().getTableData("matches");
        Enumeration keys = data.keys();
        while (keys.hasMoreElements()) {
            String name = (String) keys.nextElement();
            Dictionary tData = (Dictionary) data.get(name);
            Match m = new Match();
            String tournamentName = (String) tData.get("Tournament");
            log("Carico le partite per '" + tournamentName + "'");
            Node matches = root.find("/tournaments/" + tournamentName + "/matches");
            matches.addChild(m);
            m.setObjectData(tData);
        }
    }

    /**
	 * Effettua il login di un utente.
	 * @param client Interfaccia UserClient del client che st� effettuando la
	 * richiesta di login.
	 * @param username Il nome dell'utente che st� effetuando il login.
	 * @param password La password dell'utente che st� effettuando il login.
	 * @return UN'istanza della classe UserServer che rappresenta l'utente
	 * collegato al server in caso di successo, null non � stato possibile stabilire
	 * la connessione.
	 */
    public UserServer login(UserClient client, String username, String password) throws RemoteException {
        Account acc = (Account) accounts.findChild(username);
        User user = null;
        if (acc != null) {
            if (acc.getPassword().equals(password)) {
                try {
                    log("Utente connesso: " + username);
                    broadcastMessage(new ServerMessage("login", username));
                    user = new User(client, acc);
                    home.addChild(new UserNode(user));
                    users.addChild(new UserNode(user));
                    MessageService ms = MessageService.getInstance();
                    ms.append(username + ".Benvenuto su Gaming Service!");
                } catch (Exception e) {
                    log(e.toString());
                }
                return user;
            } else acc = null;
        }
        return null;
    }

    /**
	 * Registra un nuovo account sul server e registra uno UserServer 
	 * associato al nuovo account.
	 * @param client Istanza del client che ha effettuato la richiesta 
	 * di registrazione.
	 * @param username Nome dell'utente
	 * @param password Password associata all'utente.
	 */
    public UserServer register(UserClient client, String username, String password) {
        Account acc = null;
        log("Richiesta di registrazione: " + username + " " + password);
        try {
            if (accounts.find(username) == null) {
                acc = new Account();
                acc.setName(username);
                acc.setPassword(password);
                acc.setAdmin(false);
                accounts.addChild(acc);
                acc.save();
                return login(client, username, password);
            }
        } catch (Exception e) {
            log(e.toString());
        }
        return null;
    }
}
