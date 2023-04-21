package mou.net2;

import java.awt.Point;
import java.io.DataInput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TimerTask;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import mou.Main;
import mou.Subsystem;
import mou.net2.dht.DhtModul;
import mou.net2.messaging.MessagingModul;
import mou.net2.presence.PresenceModul;
import mou.net2.rpc.RpcModul;
import mou.net2.starmap.StarmapApplication;
import mou.net2.subscribe.SubscribeApplication;
import burlov.ipproxy.IPProxy;
import burlov.ipproxy.IPProxyAnswer;
import burlov.net.UrlCommunicator;

/**
 * @author pbu
 */
public class NetSubsystem extends Subsystem {

    private static final File PREFERENCE_FILE = new File("NetSubsystem.cfg");

    private static final long REREGISTER_ADDRESS_INTERVAL = 60 * 60 * 1000;

    private static final int IPPROXY_VERSION = Main.VERSION;

    public static final int BASE_PORT_DEFAULT = 12700;

    public static final int SOCKET_READ_TIMEOUT = 30000;

    private static final Random random = new Random();

    private IPProxy ipProxy = new IPProxy(Main.GAME_SERVER_URL + "ipproxy/");

    private int baseLocalPort = BASE_PORT_DEFAULT;

    private InetSocketAddress extAddress;

    private DhtModul dhtModul;

    private MessagingModul messagingModul;

    private RpcModul rpcModul;

    private PresenceModul presenceModul;

    private SubscribeApplication subscribeModul;

    private StarmapApplication starmapModul;

    private PeerHandle localHandle;

    /**
	 * @param parent
	 */
    public NetSubsystem(Subsystem parent) {
        super(parent);
        messagingModul = new MessagingModul(this);
        dhtModul = new DhtModul(this);
        rpcModul = new RpcModul(this);
        presenceModul = new PresenceModul(this);
        subscribeModul = new SubscribeApplication(this);
        starmapModul = new StarmapApplication(this);
    }

    public int getPort() {
        return baseLocalPort;
    }

    protected Level getDefaultLoggerLevel() {
        return Level.ALL;
    }

    public String getModulName() {
        return "NetSubsystem";
    }

    protected void shutdownIntern() {
        if (Main.isOnlineMode()) {
            try {
                getLogger().info("Ziehe meine IP-Adresse aus IPProxy Service zur�ck");
                ipProxy.unregisterAddress(baseLocalPort);
            } catch (IOException e) {
                getLogger().log(Level.WARNING, "Keine meine IP-Adresse aus IPProxy Service nicht entfernen", e);
            }
        }
    }

    protected File getPreferencesFile() {
        return PREFERENCE_FILE;
    }

    protected void startModulIntern() throws Exception {
        if (Main.isDebugMode()) {
        }
        baseLocalPort = getPreferences().getAsInteger("BaseLocalPort", new Integer(BASE_PORT_DEFAULT)).intValue();
        extAddress = InetSocketAddress.createUnresolved("localhost", BASE_PORT_DEFAULT);
        IPProxyAnswer answer = null;
        if (Main.isOnlineMode()) {
            try {
                final ServerSocket sock = new ServerSocket(baseLocalPort);
                new Thread(new Runnable() {

                    public void run() {
                        try {
                            sock.accept();
                        } catch (IOException e) {
                            getLogger().log(Level.FINE, "Beachte die Exception nicht, ist nur zur Information.", e);
                        }
                    }
                }).start();
                UrlCommunicator com = new UrlCommunicator(Main.GAME_SERVER_URL + "/ipproxy/test-global-access.php");
                Map<String, String> param = new HashMap<String, String>(1);
                param.put("port", Integer.toString(baseLocalPort));
                com.sendCommand(param);
                if (com.getError() != null) {
                    String text = "\nIhr Rechner ist f�r andere Spieler nicht sichtbar. \n" + "Die Ursache daf�r kann ein auf diesem Rechner installierter\n" + "Personal Firewall oder ein DSL-Router sein.\n" + "Bei dem Personal Firewall m�ssen Sie den Server-Modus\n" + "f�r javaw.exe und java.exe erlauben. Und falls Sie\n" + "�ber einen DSL-Router ins internet gehen, dann auch\n" + "die Portweiterleitung f�r den Port " + NetSubsystem.BASE_PORT_DEFAULT + "\n und Protokolle TCP und UDP in Ihrem Router einrichten.\n" + "Die Portweiterleitung wird in manchen Routern auch als\n" + "'Port forwarding' oder 'Virtueller Server' genannt.\n" + "Bitte lesen Sie die Gebrauchsanleitung Ihres DSL-Routers\n" + "zu diesem Thema.";
                    JOptionPane.showMessageDialog(null, text, "Fehler", JOptionPane.ERROR_MESSAGE);
                    Main.instance().forceExit("Local host ist global auf dem Port " + NetSubsystem.BASE_PORT_DEFAULT + " unerreichbar.");
                }
                sock.close();
            } catch (Exception e) {
                Main.instance().severeErrorOccured(e, "Hosterreichbarkeitspr�fung ist fehlgeschlagen", false);
                Main.instance().forceExit("Local host ist global auf dem Port " + NetSubsystem.BASE_PORT_DEFAULT + " unerreichbar.");
            }
            answer = ipProxy.getAddresses("MOU", IPPROXY_VERSION);
            if (answer.getError() != null) {
                throw new Exception("Fehler vom IPProxy Service. " + answer.getError());
            }
            try {
                extAddress = new InetSocketAddress(ipProxy.resolveMyGlobalIP(), baseLocalPort);
            } catch (IOException e) {
                throw new Exception("Fehler vom IPProxy Service. " + e.getLocalizedMessage());
            }
        }
        localHandle = new PeerHandle(Main.instance().getClientSerNumber().longValue(), extAddress);
        getLogger().info("Local Peer: " + localHandle);
        if (Main.isOnlineMode()) {
            getLogger().info("Registriere IP-Adresse in IPProxy-Service");
            ipProxy.registerAddress("MOU", IPPROXY_VERSION, baseLocalPort);
            Main.instance().getGlobalTimer().schedule(new TimerTask() {

                public void run() {
                    try {
                        getLogger().info("Reregistriere IP-Adresse in IPProxy-Service");
                        ipProxy.registerAddress("MOU", IPPROXY_VERSION, baseLocalPort);
                    } catch (IOException e) {
                        Main.instance().severeErrorOccured(e, "Kann IPProxy Internetservice nicht erreichen.\n" + "Bitte pr�fen Sie ob Ihre Internetverbindung besteht\n" + "und starten Sie das Spiel neu.", true);
                    }
                }
            }, REREGISTER_ADDRESS_INTERVAL, REREGISTER_ADDRESS_INTERVAL);
        }
    }

    public static void writePoint(Point point, DataOutputStream out) throws IOException {
        out.writeInt(point.x);
        out.writeInt(point.y);
    }

    /**
	 * Bilded eine Id zu dem gegebenen Point
	 * 
	 * @param point
	 * @return
	 */
    public static final long buildId(Point point) {
        synchronized (random) {
            long seed = point.x;
            seed = (seed << 32) ^ (point.y);
            random.setSeed(seed);
            long key = random.nextLong();
            return key;
        }
    }

    public static Point readPoint(DataInput in) throws IOException {
        return new Point(in.readInt(), in.readInt());
    }

    public InetSocketAddress getExtAddress() {
        return extAddress;
    }

    public DhtModul getDhtModul() {
        return dhtModul;
    }

    public MessagingModul getMessagingModul() {
        return messagingModul;
    }

    public RpcModul getRpcModul() {
        return rpcModul;
    }

    public PresenceModul getPresenceModul() {
        return presenceModul;
    }

    public PeerHandle getLocalHandle() {
        return localHandle;
    }

    public SubscribeApplication getSubscribeModul() {
        return subscribeModul;
    }
}
