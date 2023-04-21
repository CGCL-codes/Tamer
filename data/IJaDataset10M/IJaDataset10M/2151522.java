package chat.client;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Timer;
import java.util.Vector;
import javax.swing.JOptionPane;
import chat.client.gui.Main;
import chat.client.gui.Settings;

/**
 * the {@link Connection}-class is the base of a instance of the client
 * 
 * @author Daniel
 */
public class Connection {

    final InetAddress address;

    /**
	 * the logger of the current runtime-session
	 */
    public Logger log;

    private ChatRoomSession ses = null;

    final RecieveThread receiveThread;

    private Timer t;

    private static volatile Connection con;

    /**
	 * 
	 */
    public Vector<InetAddress> localAddresses;

    /**
	 * 
	 */
    public Vector<ChatSession> openSessions;

    /**
	 * @param addr
	 *            NetworkAddress of local Computer
	 * @param log1
	 *            if this {@link Connection} should be logged
	 * @throws IOException
	 */
    public Connection(final InetAddress addr, final boolean log1) throws IOException {
        this.address = addr;
        if (log1) {
            this.log = new Logger((System.getProperty("user.dir") + File.separatorChar + "jdcc_log_" + DateFormat.getInstance().format(new Date()) + ".log").replaceAll(":", "."));
        }
        this.localAddresses = new Vector<InetAddress>();
        this.openSessions = new Vector<ChatSession>();
        this.receiveThread = new RecieveThread(Main.receivePort);
        this.receiveThread.start();
        this.startTimer();
    }

    /**
	 * @param forceNew
	 *            force to open a new {@link ChatRoomSession}
	 * @return {@link ChatRoomSession}
	 */
    public ChatRoomSession getChatRoomSession(final boolean forceNew) {
        if (this.ses == null | forceNew) {
            this.ses = new ChatRoomSession(Main.multicastAddr, Main.receivePort);
            this.openSessions.add(this.ses);
            Main.updateTrayPopup();
        }
        return this.ses;
    }

    /**
	 * @return NetworkAddress of local Computer
	 */
    public InetAddress getInetAddress() {
        return this.address;
    }

    private void startTimer() throws IOException {
        this.t = new Timer();
        this.t.schedule(new EchoTask(this.address), 1000, Settings.updateTime * 60000l);
    }

    /**
	 * @param address
	 * @param create
	 * @return ChatSession to the Client with this InetAddress
	 */
    public ChatSession getChatSession(final InetAddress address, final boolean create) {
        for (final ChatSession ses : this.openSessions) {
            if (ses.getAddress().equals(address)) {
                if (!(ses instanceof ChatRoomSession)) {
                    return ses;
                }
            }
        }
        if (create) {
            final ChatSession s = new ChatSession(address, Main.receivePort);
            s.frame.setVisible(true);
            s.frame.textField.grabFocus();
            this.openSessions.add(s);
            Main.updateTrayPopup();
            return s;
        }
        return null;
    }

    private static InetAddress getAddress() throws SocketException, UnknownHostException {
        final Vector<InetAddress> addresses = new Vector<InetAddress>();
        final Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        while (nis.hasMoreElements()) {
            final NetworkInterface ni = nis.nextElement();
            final Enumeration<InetAddress> i = ni.getInetAddresses();
            while (i.hasMoreElements()) {
                final InetAddress ia = i.nextElement();
                if (ia.isSiteLocalAddress()) {
                    addresses.add(ia);
                }
            }
        }
        InetAddress a;
        if (addresses.size() > 1) {
            final InetAddress[] adr = new InetAddress[addresses.size()];
            addresses.copyInto(adr);
            final int i = JOptionPane.showOptionDialog(null, "Choose an IP to use.", "IP", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, adr, null);
            a = adr[i];
        } else if (addresses.size() <= 0) {
            final String s = JOptionPane.showInputDialog(null, "Please insert your pc-name.", "IP", JOptionPane.PLAIN_MESSAGE);
            a = InetAddress.getByName(s);
        } else {
            a = addresses.firstElement();
        }
        System.out.println(a.getHostAddress());
        return a;
    }

    /**
	 * @param log
	 *            if this {@link Connection} should be logged
	 * @return Connection
	 */
    public static Connection getConnection(final boolean log) {
        if (Connection.con == null) {
            try {
                return Connection.con = new Connection(Connection.getAddress(), log);
            } catch (final IOException e) {
                e.printStackTrace();
                try {
                    Connection.con = new Connection(InetAddress.getByName(null), log);
                } catch (final IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return Connection.con;
    }

    /**
	 * 
	 */
    public void close() {
        for (final ChatSession ses : this.openSessions) {
            ses.close();
            this.openSessions.remove(ses);
        }
        this.receiveThread.kill();
        this.t.cancel();
        Main.updateTrayPopup();
        this.log.exit();
        System.gc();
    }
}
