package dc.server;

import dc.core.IServer;
import dc.core.IClient;
import dc.core.PropertiesManager;
import spread.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;
import java.util.*;

/**
 * 
 * Diese Klasse implementiert das Interface IServer.
 *   
 * @author Nicole
 * @author Berit
 * @author G�nther
 * 
 * @see IServer
 *
 **/
public class Server extends UnicastRemoteObject implements IServer, AdvancedMessageListener {

    private static final long serialVersionUID = -6141130408678876583L;

    private ArrayList<IClient> clients = new ArrayList<IClient>();

    private Timer timer = new Timer();

    protected int seconds = 60;

    private SpreadConnection connection = null;

    private SpreadGroup group = null;

    private String groupName = "registration";

    private String username = "";

    private boolean primary = false;

    private String primaryname = "";

    private String spreadGroupName = "";

    private int id = 0;

    private String gameName = "";

    private int anzahlMitglieder = 0;

    private ArrayList<Integer> idMessages = new ArrayList<Integer>();

    public Server() throws RemoteException {
        super();
        Random random = null;
        random = new Random();
        this.id = random.nextInt();
        this.username = "user" + this.id;
        this.gameName = "game" + this.id;
    }

    /**
	 * Diese Methode wird zum Registrieren der einzelnen Clients verwendet.
	 * Das Start des Spiels wir �ber zwei Bedingungen erreicht.
	 * 1. Anzahl der Spieler betr�gt 4
	 * 2. eingestellte Wartezeit ist abgelaufen und Mindestspielerzahl ist erreicht.
	 */
    public synchronized void register(IClient client) throws RemoteException {
        client.setGameName(this.gameName);
        this.getClients().add(client);
        if (this.getPrimary()) {
            this.sendClients();
        }
        Server.log(String.format("Client '%s' currently registered ...", client.getName()));
        if (this.getClients().size() == 1) {
            this.getTimer().schedule(new StartGameTask(this), this.getSeconds() * 1000);
        } else if (this.getClients().size() == 4) {
            this.start();
        }
    }

    public void setTimer() throws RemoteException {
        if (this.getClients().size() > 0 && this.getClients().size() < 4) {
            this.getTimer().schedule(new StartGameTask(this), this.getSeconds() * 1000);
            Server.log("%%% timer gestartet %%% ");
        } else if (this.getClients().size() == 4) {
            this.start();
        }
    }

    /**
	 * Anzahl Sekunden, die verstreichen d�rfen bis das Spiel startet.
	 * (Nur wenn Mindestanzahl der Spieler erreicht ist)
	 * @param seconds	Sekunden
	 */
    private void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    /**
	 * Anzahl Sekunden, die bis zum automatischen Start verstreichen.
	 * (Wartezeit, in der sich neue Spieler registrieren k�nnen.)
	 * @return	int, Anzahl Sekunden
	 */
    private int getSeconds() {
        PropertiesManager pm = null;
        try {
            pm = new PropertiesManager("cfg/server.properties");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.seconds = Integer.valueOf(pm.get("TimeToWait"));
        return this.seconds;
    }

    /**
	 * Flag, ob der Server, der Primary ist 
	 * @return
	 */
    public boolean getPrimary() {
        return this.primary;
    }

    /**
	 * Setzen des Primarys, wenn Server der erste ist
	 * bzw, wenn Primary ausgefallen ist
	 * @param primary
	 */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /**
	 * R�ckgabe des Benutzernamens
	 * @return
	 */
    public String getUsername() {
        return this.username;
    }

    /**
	 * Setzen des Benutzernamens
	 * @param username
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * Liste der zur Verf�gung stehenden Ports
	 * @return Liste der Ports als Integerwerte
	 */
    private ArrayList<Integer> getPorts() {
        ArrayList<Integer> ports = null;
        ports = new ArrayList<Integer>();
        ports.add(1999);
        ports.add(2000);
        ports.add(2001);
        ports.add(2002);
        return ports;
    }

    /** 
	 * Setzen der Clients von einem Server zum anderen
	 * @param clients
	 */
    private void setClients(ArrayList<IClient> clients) {
        this.clients = clients;
    }

    /**
	 * Einsprungspunkt f�r den Server. Auslesen der entsprechenden
	 * Konfigurationsdaten und registrierung des Services.
	 */
    public static void main(String[] args) {
        Server srv = null;
        Server.log("Starting up server....");
        try {
            srv = new Server();
            srv.connection = new SpreadConnection();
            srv.connection.connect(InetAddress.getByName(null), 0, srv.username, false, true);
            srv.group = new SpreadGroup();
            srv.group.join(srv.connection, srv.groupName);
            srv.connection.add(srv);
        } catch (Exception ex) {
            Server.log("Something terrible has happened...");
            ex.printStackTrace();
        }
    }

    /**
	 * Verteilung der Spieler an die Clients und Neuinitialisierung
	 * des Servers f�r weitere Games.
	 * @throws RemoteException
	 */
    private void start() throws RemoteException {
        for (IClient c : this.getClients()) {
            c.setClients(this.getClients());
        }
        this.clear();
    }

    /**
	 * Neuinitialisierung des Servers. Er wartet auf neue
	 * Spieler.
	 */
    private void clear() {
        this.getTimer().cancel();
        this.timer = new Timer();
        this.getClients().clear();
        this.resetGameName();
        Server.log("Server ready for new connections...");
    }

    private void resetGameName() {
        Random random = null;
        random = new Random();
        this.id = random.nextInt();
        this.gameName = "game" + this.id;
    }

    /**
	 *	Timer ist verantwortlich f�r die Wartezeit ab der ersten
	 *  Verbindung.
	 */
    private Timer getTimer() {
        return this.timer;
    }

    /**
	 * Die aktuelle Liste der angemeldeten Clients.
	 * @return	List<IClient>, Liste der Clients
	 */
    private ArrayList<IClient> getClients() {
        return this.clients;
    }

    /**
	 * Zentrales Logging auf der Serverseite.
	 * @param msg
	 */
    private static void log(String msg) {
        System.out.println(msg);
    }

    /**
	 * Klasse, die f�r das Management der Wartezeit verantwortlich
	 * ist.
	 * @author Nicole, Berit, G�nther
	 *
	 */
    class StartGameTask extends TimerTask {

        private Server server = null;

        protected StartGameTask(Server server) {
            this.server = server;
        }

        public void run() {
            try {
                if (this.getServer().getClients().size() > 1) {
                    this.getServer().start();
                }
            } catch (RemoteException ex) {
                Server.log("StartGameTask.run() > :" + ex.getMessage());
            }
        }

        private Server getServer() {
            return this.server;
        }
    }

    /**
	 * Erhalten einer Membershipnachricht
	 */
    @Override
    public void membershipMessageReceived(SpreadMessage message) {
        MembershipInfo info = null;
        info = message.getMembershipInfo();
        this.printMembershipInfo(info);
        System.out.println(message.toString());
        if (info.isCausedByJoin()) {
            this.someBodyJoined(info);
        }
        if (info.isCausedByLeave()) {
            this.someBodyLeft(info);
        }
        if (info.isCausedByDisconnect()) {
            this.someBodyLeft(info);
        }
    }

    /**
	 * Start der Registry mit angegebenen Port
	 * @param port, Integer
	 */
    private void startRegistry(int port) {
        Registry reg = null;
        PropertiesManager pm = null;
        String service = "";
        String seconds = "";
        Server.log("Starting up server....");
        try {
            pm = new PropertiesManager("cfg/server.properties");
            service = pm.get("service");
            seconds = pm.get("TimeToWait");
            this.setSeconds(Integer.parseInt(seconds));
            reg = LocateRegistry.createRegistry(port);
            reg.rebind(service, this);
            Server.log(String.format("Service '%s' is up and running on port '%s'", service, port));
        } catch (Exception ex) {
            Server.log("Something terrible has happened...");
            ex.printStackTrace();
        }
    }

    /**
	 * Ausgabe der MembershipInfo
	 * @param info
	 */
    private void printMembershipInfo(MembershipInfo info) {
        SpreadGroup group = info.getGroup();
        if (info.isRegularMembership()) {
            SpreadGroup members[] = info.getMembers();
            MembershipInfo.VirtualSynchronySet virtual_synchrony_sets[] = info.getVirtualSynchronySets();
            MembershipInfo.VirtualSynchronySet my_virtual_synchrony_set = info.getMyVirtualSynchronySet();
            System.out.println("REGULAR membership for group " + group + " with " + members.length + " members:");
            for (int i = 0; i < members.length; ++i) {
                System.out.println("\t\t" + members[i]);
            }
            System.out.println("Group ID is " + info.getGroupID());
            System.out.print("\tDue to ");
            if (info.isCausedByJoin()) {
                System.out.println("the JOIN of " + info.getJoined());
            } else if (info.isCausedByLeave()) {
                System.out.println("the LEAVE of " + info.getLeft());
            } else if (info.isCausedByDisconnect()) {
                System.out.println("the DISCONNECT of " + info.getDisconnected());
            } else if (info.isCausedByNetwork()) {
                System.out.println("NETWORK change");
                for (int i = 0; i < virtual_synchrony_sets.length; ++i) {
                    MembershipInfo.VirtualSynchronySet set = virtual_synchrony_sets[i];
                    SpreadGroup setMembers[] = set.getMembers();
                    System.out.print("\t\t");
                    if (set == my_virtual_synchrony_set) {
                        System.out.print("(LOCAL) ");
                    } else {
                        System.out.print("(OTHER) ");
                    }
                    System.out.println("Virtual Synchrony Set " + i + " has " + set.getSize() + " members:");
                    for (int j = 0; j < set.getSize(); ++j) {
                        System.out.println("\t\t\t" + setMembers[j]);
                    }
                }
            }
        } else if (info.isTransition()) {
            System.out.println("TRANSITIONAL membership for group " + group);
        } else if (info.isSelfLeave()) {
            System.out.println("SELF-LEAVE message for group " + group);
        }
    }

    /**
	 * Ein neuer Server hat sich in der Gruppe angemeldet
	 * @param info
	 */
    private void someBodyJoined(MembershipInfo info) {
        SpreadGroup members[] = null;
        SpreadMessage message = null;
        members = info.getMembers();
        if (members.length == 1) {
            this.setPrimary(true);
            this.primaryname = members[0].toString();
            this.startRegistry(this.getPorts().get(0));
        } else {
            try {
                if (this.getPrimary()) {
                    this.sendClients();
                    message = new SpreadMessage();
                    message.setData(this.primaryname.getBytes());
                    message.addGroup(this.groupName);
                    message.setType((short) 1);
                    this.connection.multicast(message);
                }
            } catch (SpreadException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
	 * verschicken der Clients an die restlichen Server
	 * zur Gew�hrung der Ausfallssicherheit
	 */
    private void sendClients() {
        SpreadMessage message = null;
        try {
            message = new SpreadMessage();
            message.setObject(this.getClients());
            message.addGroup(this.groupName);
            message.setType((short) 0);
            message.setFifo();
            this.connection.multicast(message);
        } catch (SpreadException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Ein Server hat sich verabschiedet
	 * �berpr�fung, ob's der Primary war oder nicht
	 * @param info
	 */
    private void someBodyLeft(MembershipInfo info) {
        System.out.println("Hier wird �berpr�ft, ob Primary noch da ist ");
        String left = "";
        SpreadMessage message = null;
        left = info.getLeft().toString();
        if (left.compareTo(this.primaryname) == 0) {
            System.out.println("Primary hat uns verlassen");
            try {
                message = new SpreadMessage();
                try {
                    message.setData(this.intToByteArray(this.id));
                } catch (Exception e) {
                    System.out.println(" ha, ich bins!");
                }
                this.anzahlMitglieder = info.getMembers().length;
                message.addGroup(this.groupName);
                message.setType((short) 2);
                message.setSafe();
                this.connection.multicast(message);
            } catch (SpreadException ex) {
                System.out.println(ex.toString());
            }
        } else {
            System.out.println("irgendwer ist weg ");
        }
    }

    private int byteArrayToInt(final byte[] array) throws IOException {
        ByteArrayInputStream bos = new ByteArrayInputStream(array);
        DataInputStream dos = new DataInputStream(bos);
        return dos.readInt();
    }

    private byte[] intToByteArray(final int integer) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        dos.writeInt(integer);
        dos.flush();
        return bos.toByteArray();
    }

    /** 
	 * regul�re Nachricht erhalten
	 * dies k�nnte sein: Typ 0: Clients
	 * 					 Typ 1: Primaryname
	 * 					 Typ 2: verschicken aller Ids der noch vorhandenen Servern
	 */
    @Override
    public synchronized void regularMessageReceived(SpreadMessage message) {
        Object o = null;
        byte[] primaryname = null;
        int id = 0;
        try {
            if (message.getType() == (short) 0) {
                o = message.getObject();
                if (o != null && o instanceof ArrayList) {
                    this.setClients((ArrayList<IClient>) o);
                }
            } else if (message.getType() == (short) 1) {
                primaryname = message.getData();
                this.primaryname = new String(primaryname);
                System.out.println("%%%%%%%%%%% Primary ist: " + this.primaryname);
            } else if (message.getType() == (short) 2) {
                try {
                    id = this.byteArrayToInt(message.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                this.idMessages.add(id);
                System.out.println("//////id: " + id);
                if (this.id == id) {
                    this.spreadGroupName = message.getSender().toString();
                    System.out.println("//////spreadGroupName: " + this.spreadGroupName);
                }
                if (this.idMessages.size() == this.anzahlMitglieder) {
                    this.setNewPrimary();
                    this.idMessages = new ArrayList<Integer>();
                }
            }
        } catch (SpreadException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void setNewPrimary() {
        Collections.sort(this.idMessages);
        SpreadMessage message = null;
        Random random = null;
        int index = 0;
        int newport = 0;
        message = new SpreadMessage();
        random = new Random();
        if (this.idMessages.get(0) == this.id) {
            this.setPrimary(true);
            message = new SpreadMessage();
            message.setData(this.spreadGroupName.getBytes());
            message.addGroup(this.groupName);
            message.setType((short) 1);
            try {
                this.connection.multicast(message);
            } catch (SpreadException e) {
                e.printStackTrace();
            }
            index = random.nextInt(3);
            newport = this.getPorts().get(index);
            this.startRegistry(newport);
            Server.log(" Anzahl der clients: " + this.getClients().size());
            for (IClient client : this.getClients()) {
                try {
                    this.gameName = client.getGameName();
                    client.reconnect(newport);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            try {
                this.setTimer();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
