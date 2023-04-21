package net.sf.btw.ibtu;

import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.UUID;
import net.sf.btw.tools.Logger;
import net.sf.btw.ui.ConnectorCanvas;
import net.sf.btw.ui.ConnectorForm;
import net.sf.btw.ui.IConnectorListener;
import net.sf.btw.btlib.Client;
import net.sf.btw.btlib.Peer;
import net.sf.btw.btlib.Server;
import net.sf.btw.btlib.ServerInfo;
import net.sf.btw.ibtu.bluetooth.ClientListener;
import net.sf.btw.ibtu.bluetooth.ServerListener;
import net.sf.btw.ibtu.ui.SplashScreen;
import net.sf.btw.ibtu.ui.HistoryCanvas;

/**
 * This class represents the I BlueTooth You {@link MIDlet} application itself.
 * It contains all the {@link Screen}s for user interface, as well as the
 * classes performing the Bluetooth communication.
 * 
 * @author Jan Tomka
 * 
 * TODO Externalize string in the whole application.
 */
public class Ibtu extends MIDlet {

    /**
	 * IBTU application universally unique identifier. Incompatible versions of
	 * application should have different UUID.
	 */
    private static final UUID ibtuUuid = new UUID("35022954942d4738bf60eaa0ccf580b0", false);

    /**
	 * Splash screen to be displayed at the beginning of an application.
	 */
    private SplashScreen splashScreen;

    /**
	 * Displayable to be switched to after splash screen timer expires or the
	 * splash screen is OK'ed.
	 */
    private Displayable initialDisplayable;

    /**
	 * Bluetooth connector form, where user selects bluetooth connection
	 * properties. TODO Get why server names are truncated by two characters on
	 * my Nokia.
	 */
    private ConnectorCanvas conForm;

    /**
	 * Canvas displaying the communicatioon history.
	 */
    private HistoryCanvas history;

    /**
	 * Instance of {@link Client} class, representing an IBTU client the
	 * application acts as.
	 */
    private Client client;

    /**
	 * Instance of {@link Server} class, representing an IBTU server the
	 * application acts as.
	 */
    private Server server;

    /**
	 * Bluetooth configuration value of the maximum transmition unit size. TODO
	 * Increase value (???)
	 */
    private static final int MAX_MESSAGE_SIZE = 100;

    /**
	 * Creates new I BlueTooth You application object. This constructor creates
	 * all the {@link Form}s and {@link Screen}s the application may display
	 * during the execution.
	 */
    public Ibtu() {
        super();
        try {
            Peer.initializeBluetooth();
            conForm = new ConnectorCanvas(ibtuUuid, Display.getDisplay(this), new IbtuConnectorListener(), "Create new chat: ", "Join to: ");
            initialDisplayable = conForm;
        } catch (BluetoothStateException e) {
            e.printStackTrace();
            Alert connectorAlert = new Alert("Error", "Bluetooth device is " + "not active. Please, check the settings.", null, AlertType.ERROR);
            connectorAlert.setTimeout(Alert.FOREVER);
            initialDisplayable = connectorAlert;
        }
        history = new HistoryCanvas(this);
    }

    /**
	 * Returns reference to connector canvas.
	 * 
	 * @return Connector canvas reference.
	 */
    public ConnectorCanvas getConForm() {
        return conForm;
    }

    /**
	 * Disconnects device, be it a server or a client.
	 */
    public void disconnect() {
        if (server != null) {
            server.stopServer();
            server = null;
        }
        if (client != null) {
            client.disconnect();
            client = null;
        }
    }

    /**
	 * Signals the MIDlet to terminate and enter the Destroyed state.
	 * Disconnects Bluetooth device.
	 * 
	 * @param unconditional
	 *            If true when this method is called, the MIDlet must cleanup
	 *            and release all resources. If false the MIDlet may throw
	 *            MIDletStateChangeException to indicate it does not want to be
	 *            destroyed at this time.
	 * 
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        disconnect();
    }

    /**
	 * Signals the MIDlet to stop and enter the Paused state. Doesn't do
	 * anything.
	 * 
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
    protected void pauseApp() {
    }

    /**
	 * Starts the I BlueTooth You MIDlet application. Only sets the current
	 * {@link Screen} to be displayed to {@link Ibtu#history}.
	 * 
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
    protected void startApp() throws MIDletStateChangeException {
        final Display display = Display.getDisplay(this);
        splashScreen = new SplashScreen(display, initialDisplayable);
        display.setCurrent(splashScreen);
        Timer splashTimer = new Timer();
        splashTimer.schedule(new TimerTask() {

            public void run() {
                if (display.getCurrent() == splashScreen) display.setCurrent(initialDisplayable);
            }
        }, 5000);
    }

    /**
	 * Sends a message. Does the right action to send out the message, depending
	 * on the role the application actually acts as.
	 * 
	 * @param message
	 *            The message to send.
	 */
    public void sendMessage(final String message) {
        byte[] msg;
        if (client != null) {
            msg = new Message(Peer.getDeviceName(), message).toByteArray();
            client.send(msg);
        } else if (server != null) {
            msg = new Message(Peer.getDeviceName(), message).toByteArray();
            server.sendBuffer(server.getClientIDs().keys(), (byte) -1, msg);
        } else throw new IllegalStateException("Ibtu must be connected");
    }

    /**
	 * The {@link ConnectorForm} form listener. Handles application Bluetooth
	 * initialization events.
	 * 
	 * @author Martin Vysny
	 */
    private class IbtuConnectorListener implements IConnectorListener {

        /**
		 * Terminates application on connector form closure.
		 * 
		 * @see net.sf.btw.ui.IConnectorListener#closeConnector()
		 */
        public void closeConnector() {
            notifyDestroyed();
        }

        /**
		 * Intializes client when user decides to act as one. Creates new
		 * {@link Client} instance, attempts to connects to a server, and
		 * displays the {@link #history} canvas.
		 * 
		 * @see net.sf.btw.ui.IConnectorListener#connectToServer(net.sf.btw.btlib.ServerInfo)
		 */
        public void connectToServer(ServerInfo info) {
            try {
                Display.getDisplay(Ibtu.this).setCurrent(history);
                client = new Client(ibtuUuid, MAX_MESSAGE_SIZE, MAX_MESSAGE_SIZE, new ClientListener(Display.getDisplay(Ibtu.this), history));
                client.connect(info);
            } catch (Exception ex) {
                Logger.error("Error creating client", ex);
                Display.getDisplay(Ibtu.this).setCurrent(new Alert("Error", "Error creating client: " + ex.getMessage(), null, AlertType.ERROR), conForm);
            }
        }

        /**
		 * Initializes server when user decides to act as one. Creates
		 * {@link Server} instance, attempts to start it and displays the
		 * {@link #history} canvas.
		 */
        public void startServer() {
            try {
                Display.getDisplay(Ibtu.this).setCurrent(history);
                final ServerListener l = new ServerListener(history);
                server = new Server(MAX_MESSAGE_SIZE, MAX_MESSAGE_SIZE, ibtuUuid, l);
                l.server = server;
                server.startServer();
            } catch (Exception ex) {
                Logger.error("Error creating server", ex);
                Display.getDisplay(Ibtu.this).setCurrent(new Alert("Error", "Error creating server: " + ex.getMessage(), null, AlertType.ERROR), conForm);
            }
        }
    }
}
