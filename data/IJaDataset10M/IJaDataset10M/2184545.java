package dc.client;

import dc.core.*;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import md.gui.*;
import at.falb.games.alcatraz.api.*;
import dc.core.IClient;

/**
 * 
 * @author Nicole, Berit, G�nther
 * 
 * Diese Klasse implementiert das Interface IClient, und das 
 * Interface MoveListener des Games Alcatraz.
 *
 */
public class Client extends UnicastRemoteObject implements IClient, MoveListener {

    private static final long serialVersionUID = 124141244260674591L;

    private List<IClient> clients = null;

    private String name = "John Doe";

    private String gameName = "";

    private Alcatraz game = null;

    private int playerId = -1;

    private Timer timer = new Timer();

    private int timeToWait = 1;

    private int numberOfTrials = 5;

    /**
	 * Konstruktor, in dem die eigentliche Instanz von Alcatraz instanziiert
	 * wird, und der MoveListener des Games an den Client delegiert wird.
	 * 
	 * @throws RemoteException
	 */
    public Client() throws RemoteException {
        super();
        this.game = new Alcatraz();
        this.getGame().addMoveListener(this);
    }

    /**
	 * Zugriff auf den Timer, der die Gesundheit der verbundenen Clients
	 * regelm�ssig durchf�hrt.
	 * @return Timerinstanz
	 */
    private Timer getTimer() {
        return this.timer;
    }

    /**
	 * Anzahl Sekunden, die zwischen dem ersten Erkennen eines unverbundenen
	 * Clients und dem Ausscheiden desselben gewartet werden soll.
	 * @param seconds	Sekunden, die gewartet werden sollen.
	 */
    private void setTimeToWait(int seconds) {
        this.timeToWait = (seconds * 1000);
    }

    /**
	 * Zugriff auf die Anzahl der Millisekunden zwischen dem ersten Erkennen
	 * eines unverbundenen Clients und dessen Ausscheidung.
	 * @return	int, Anzahl Millisekunden
	 */
    private int getTimeToWait() {
        return this.timeToWait;
    }

    public void setGameName(String gameName) throws RemoteException {
        this.gameName = gameName;
    }

    public String getGameName() throws RemoteException {
        return this.gameName;
    }

    private static ArrayList<Integer> getPorts() {
        ArrayList<Integer> ports = null;
        ports = new ArrayList<Integer>();
        ports.add(1999);
        ports.add(2000);
        ports.add(2001);
        ports.add(2002);
        return ports;
    }

    /**
	 * Anzahl der Versuche, die bis zum endg�ltigen Ausscheiden eines nicht
	 * mehr verbundenen Clients durchgef�hrt werden sollen.
	 * @param trials	Anzahl der Versuche
	 */
    private void setNumberOfTrials(int trials) {
        this.numberOfTrials = trials;
    }

    /**
	 * Anzahl der Versuche bis zum endg�ltigen Ausscheiden eines unverbundenen
	 * Clients.
	 * @return	Anzahl der Versuche
	 */
    private int getNumberOfTrials() {
        return this.numberOfTrials;
    }

    /**
	 * Implementierung der Methode, die f�r den Gesundheitscheck
	 * ben�tigt wird. Jede andere Methode w�re gleich gut geeignet.
	 */
    @Override
    public void checkAlive() throws RemoteException {
    }

    /**
	 * Interne Id des Spielers
	 * @return	int
	 */
    private int getPlayerId() {
        return this.playerId;
    }

    /**
	 * Interne Id des Spielers
	 * @param id	int (Ordinal des Spielers);
	 */
    private void setPlayerId(int id) {
        this.playerId = id;
        Client.log("Player mit Id '" + Integer.toString(id) + "' erstellt!");
    }

    /**
	 * Implementierung des Interfaces
	 * @see IClient
	 */
    @Override
    public String getName() throws RemoteException {
        return name;
    }

    /**
	 * Name des Spielers
	 * @param name	Name des Spielers (Nickname)
	 */
    protected void setName(String name) {
        this.name = name;
    }

    /**
	 * Implementierung des Interfaces
	 * Hier wird jedem Spieler eine eindeutige Id gegeben. Diese ist
	 * abh�ngig von der Position innerhalb der vom Server �bergebenen
	 * Liste.
	 * Anschlie�end wird das Spiel initialisiert, der aktuelle Player
	 * �bermittelt und dem Spielefenster ein aussagekr�ftiger Titel
	 * zugewisen.
	 * @see IClient
	 */
    @Override
    public void setClients(List<IClient> list) throws RemoteException {
        int ordinal = -1;
        this.clients = list;
        for (IClient c : list) {
            ordinal++;
            if (c.getName().equals(this.getName())) {
                this.setPlayerId(ordinal);
                break;
            }
        }
        this.getGame().init(list.size(), this.getPlayerId());
        this.getGame().getWindow().setTitle(this.getTitle());
        this.getGame().getPlayer(this.getPlayerId()).setName(this.getName());
        if (!this.getGame().getWindow().isVisible()) {
            this.getGame().showWindow();
        }
        this.getGame().start();
        this.initializeTimer();
    }

    /**
	 * aussagekr�ftiger Titel im Spielefenster.
	 * @return	String, Titel der Alcatraz-Instanz
	 * @throws RemoteException
	 */
    private String getTitle() throws RemoteException {
        return "Alcatraz - [" + this.getName() + "] " + " - Player " + this.getPlayerId();
    }

    /**
	 * Die aktuellen Mitspieler, die vom Server �bermittelt worden sind.
	 * @return	List<IClient>, Liste der Clients
	 */
    private List<IClient> getClients() {
        return this.clients;
    }

    /**
	 * Das eigentliche Game 'Alcatraz', das von diesem Client nur aufgerufen
	 * wird.
	 * @return	Alcatraz, das eigentliche Game
	 */
    protected Alcatraz getGame() {
        return this.game;
    }

    /**
	 * Implementierung des Interfaces
	 * @see IClient
	 */
    @Override
    public void move(Player player, Prisoner prisoner, int rowOrCol, int row, int col) throws RemoteException {
        this.getGame().doMove(player, prisoner, rowOrCol, row, col);
    }

    /**
	 * Einsprungspunkt des Clients. Im wesentlichen
	 * ist die Konsole das UI f�r den Client. Die Konfiguration
	 * f�r den aktuellen Client wird aus einem Konfigurationsfile
	 * gelesen.
	 */
    public static void main(String[] args) {
        IServer srv = null;
        String name = "John Doe";
        PropertiesManager pm = null;
        String service = "";
        String server = "";
        String trials = "";
        String seconds = "";
        String uri = "";
        ChatDialog dlg = null;
        try {
            pm = new PropertiesManager("cfg/client.properties");
            name = pm.get("nickname");
            server = pm.get("server");
            service = pm.get("service");
            trials = pm.get("NumberOfTrials");
            seconds = pm.get("TimeToWait");
            if (trials.length() == 0) {
                trials = "5";
            }
            if (seconds.length() == 0) {
                seconds = "1";
            }
            Client client = null;
            Client.log("Starting up client...");
            client = new Client();
            if (args.length > 0) {
                name = args[0];
            }
            client.setName(name);
            client.setTimeToWait(Integer.parseInt(seconds));
            client.setNumberOfTrials(Integer.parseInt(trials));
            srv = Client.findRightPort(0, server, service);
            srv.register(client);
            Client.log(String.format("Client '%s' ist mit Service '%s' verbunden - Viel Spass!", client.getName(), uri));
            dlg = new ChatDialog(client.getName(), client.gameName);
            dlg.setVisible(true);
        } catch (Exception ex) {
            Client.log("Something terrible has happend... >: " + ex.getMessage());
        }
    }

    private static IServer findRightPort(int i, String server, String service) {
        int portId = 0;
        String port = "";
        IServer srv = null;
        String uri = "";
        portId = Client.getPorts().get(i);
        port = String.valueOf(portId);
        Client.log(port);
        uri = "rmi://" + server + ":" + port + "/" + service;
        try {
            srv = (IServer) Naming.lookup(uri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            Client.log("NotBoundException...");
            e.printStackTrace();
        } catch (Exception ex) {
        }
        i++;
        if (srv == null) {
            if (i < Client.getPorts().size()) {
                srv = Client.findRightPort(i, server, service);
            } else {
                Client.log("Da passt noch was gewaltig nicht...!");
            }
        } else {
            Client.log(String.format("Client ist mit Service '%s' verbunden - Viel Spass!", uri));
        }
        return srv;
    }

    /**
	 * Zentrales Logging. Die Ausgabe auf die Konsole ist sicherlich nicht die 
	 * einzig m�gliche Variante f�r Ausgaben dieser Art :).
	 * @param msg	String, der gelogged werden soll.
	 */
    private static void log(String msg) {
        System.out.println(msg);
    }

    /**
	 * Abfrage, ob alle Mitspieler noch 'gesund' bzw. verbunden sind!
	 * @return true/false
	 */
    private boolean getAllAlive() {
        boolean alive = false;
        try {
            for (IClient c : this.getClients()) {
                c.checkAlive();
            }
            alive = true;
            Client.log("Gesundheitscheck: OK ");
        } catch (RemoteException ex) {
            Client.log("Gesundheitscheck: Einem Mitspieler gehts schlecht. Hoffen wir, dass es bald wieder besser geht...");
        }
        return alive;
    }

    /**
	 * Die Weitergabe des Zuges an alle Mitspieler. Vorher wird gepr�ft, ob alle
	 * Mitspieler auch 'gesund' sind. Dies wird durch den Aufruf der definierten
	 * Methode 'checkAlive' gepr�ft.
	 * Sind alle gesund, wird der Move weitergeben.
	 * Gibt es ein Problem, wird eine definierte Zeitspanne gewartet, bis der
	 * 'kranke' Client zur�ckkommt. Nach Ablauf dieser Zeitspanne wird der Client
	 * f�r 'tot' erkl�rt und ausgeschieden. Das Spiel wird mit den �briggebliebenen
	 * Spielern neu gestartet und weitergef�hrt. Ist nach dem Ausscheiden der 'kranken'
	 * Clients die Mindestanzahl der zur�ckgebliebenen Spielern unterschritten, wird
	 * der �briggebliebene Spieler zum Sieger erkl�rt.
	 * Wird der 'kranke' Client innerhalb der Zeit wieder 'gesund', werden die Z�ge
	 * weitergereicht.
	 * Eine Einzelabfrage der Gesundheitszust�nde w�re ebenso m�glich und einfach
	 * implementierbar - der dabei zu gewinnende Vorteil war aber aus unserer Sicht
	 * zu gering.
	 */
    public void doMove(Player player, Prisoner prisoner, int rowOrCol, int row, int col) {
        if (this.getAllAlive()) {
            for (IClient c : this.getClients()) {
                try {
                    if (!c.getName().equals(this.getName())) {
                        c.move(player, prisoner, rowOrCol, row, col);
                        Client.log("Move wurde an Player " + c.getName() + " weitergegeben...");
                    }
                } catch (RemoteException ex) {
                    Client.log("Innerhalb von doMove >: " + ex.getMessage());
                }
            }
        } else {
            if (this.playerIsBack()) {
                this.doMove(player, prisoner, rowOrCol, row, col);
            } else {
                this.restart();
            }
        }
    }

    /**
	 * Initialisierung des Timers zur Pr�fung des Gesundheitszustandes
	 * der Spieler.
	 * @see MoveTask
	 */
    private void initializeTimer() {
        if (this.getTimer() != null) {
            this.getTimer().cancel();
        }
        this.timer = new Timer();
        this.getTimer().schedule(new MoveTask(this), 10000, 10000);
    }

    /**
	 * Sind die 'kranken' Spieler wieder 'gesund' geworden?
	 * 'Krank' bedeutet in diesem Kontext, da� es zu Netzwerkproblemen
	 * kommt und nicht, da� der Spieler seine Spieleinstanz schlie�t, sich
	 * neu anmeldet und mit den Mitspielern 'nachtr�glich' weiterspielen kann.
	 * 'gesund' bedeutet, da� die Netzwerkprobleme gel�st wurden (z.B. Kabel
	 * wieder eingesteckt).
	 * @return true/false
	 */
    private boolean playerIsBack() {
        boolean allPlayerAlive = false;
        int retry = 0;
        this.getTimer().cancel();
        while (true) {
            try {
                retry++;
                java.lang.Thread.sleep(this.getTimeToWait());
                if (this.getAllAlive()) {
                    allPlayerAlive = true;
                    break;
                }
                if (retry == this.getNumberOfTrials()) {
                    Client.log("Player ist nicht zur�ckgekommen -> restart()");
                    break;
                }
            } catch (java.lang.InterruptedException ex) {
                Client.log("Something terrible has happend: > " + ex.getMessage());
            }
        }
        this.initializeTimer();
        return allPlayerAlive;
    }

    /**
	 * Offensichtlich hat das Netz Opfer gefordert. Diese werden hier
	 * ausgeschieden und das Spiel wird mit den verbliebenen Spielern
	 * weitergef�hrt.
	 */
    private void restart() {
        List<IClient> list = new ArrayList<IClient>();
        for (IClient c : this.getClients()) {
            try {
                c.checkAlive();
                list.add(c);
            } catch (RemoteException ex) {
                Client.log("Client bei restart ausgeschieden...");
            }
        }
        if (list.size() < 2) {
            this.getTimer().cancel();
            Client.log("Zu wenig Spieler vorhanden. Du hast gewonnen :) - Spiel wird beendet...");
            try {
                java.lang.Thread.sleep(4000);
            } catch (java.lang.InterruptedException ex) {
                Client.log("Something terrible has happend: > " + ex.getMessage());
            }
            System.exit(0);
        } else {
            for (IClient c : list) {
                try {
                    c.setClients(list);
                } catch (RemoteException e) {
                    Client.log("Client.restart() > " + e.getMessage());
                }
            }
        }
    }

    /**
	 * Delegation der Funktionalit�t des Spiels Alcatraz.
	 */
    @Override
    public void gameWon(Player p) {
        Client.log("Spieler " + p.getName() + "hat gewonnen!");
    }

    /**
	 * M�glichkeit einen Zug zur�ckzunehmen. Auch diese Funktionalit�t
	 * wird vom System delegiert. Wir haben im Spiel nicht herausgefunden
	 * wie das geht und implementieren es deshalb nicht.
	 */
    @Override
    public void undoMove() {
        this.getGame().undoMove();
    }

    public void reconnect(int port) {
        IServer srv = null;
        PropertiesManager pm = null;
        String service = "";
        String server = "";
        String trials = "";
        String seconds = "";
        String uri = "";
        try {
            pm = new PropertiesManager("cfg/client.properties");
            server = pm.get("server");
            service = pm.get("service");
            trials = pm.get("NumberOfTrials");
            seconds = pm.get("TimeToWait");
            if (trials.length() == 0) {
                trials = "5";
            }
            if (seconds.length() == 0) {
                seconds = "1";
            }
            uri = "rmi://" + server + ":" + port + "/" + service;
            srv = (IServer) Naming.lookup(uri);
            Client.log(String.format("Reconnect des Clients: Client '%s' ist mit Service '%s' verbunden - Viel Spass!", this.getName(), uri));
        } catch (Exception ex) {
            Client.log("Something terrible has happend... >: " + ex.getMessage());
        }
    }

    /**
	 * Implementierungsklasse f�r den Timer
	 * 
	 * @author Nicole, Berit, G�nther
	 *
	 */
    class MoveTask extends TimerTask {

        private Client client = null;

        public MoveTask(Client c) {
            this.client = c;
        }

        public void run() {
            if (!this.getClient().getAllAlive()) {
                if (!this.getClient().playerIsBack()) {
                    this.getClient().restart();
                }
            }
        }

        private Client getClient() {
            return this.client;
        }
    }
}
