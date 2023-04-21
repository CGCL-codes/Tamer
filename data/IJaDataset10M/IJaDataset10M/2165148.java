package fi.hip.gb.client;

import java.io.IOException;
import java.util.Vector;
import javax.bluetooth.BluetoothStateException;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import fi.hip.gb.bluetooth.BTListener;
import fi.hip.gb.bluetooth.BTService;
import fi.hip.gb.bluetooth.EndPoint;
import fi.hip.gb.bluetooth.GPSResult;
import fi.hip.gb.bluetooth.Service;
import fi.hip.gb.midlet.core.LiteResult;
import fi.hip.gb.midlet.core.LiteStorage;

/**
 * User interface for browsing bluetooth network and
 * viewing bluetooth server activities.
 * 
 * @author Juho Karppinen
 */
public class BluetoothBrowser extends Form implements CommandListener, BTListener {

    /** send the message to the selected device */
    private Command cmdSend = new Command("Connect", Command.SCREEN, 1);

    /** send the message to all known devices */
    private Command cmdSendAll = new Command("Send all", Command.SCREEN, 2);

    /** command for canceling inquiry operation */
    private Command cmdCancel = new Command("Cancel", Command.SCREEN, 1);

    /** command for updating the list of devices */
    private Command cmdRefresh = new Command("Refresh", Command.SCREEN, 3);

    /** command for saving the default service */
    private Command cmdSave = new Command("Set as default", Command.SCREEN, 3);

    /** close the connection for selected device */
    private Command cmdClose = new Command("Close connections", Command.SCREEN, 3);

    /** label for the list when seaching devices */
    private static final String SEARCH_DEVICES = "Searching devices...";

    /** label for the list when seaching services */
    private static final String SEARCH_SERVICES = "Searching services...";

    /** list of available devices */
    private ChoiceGroup devices = new ChoiceGroup("[press refresh]", Choice.EXCLUSIVE);

    /** maps the device names to corresponding endpoints */
    private Vector endPoints = new Vector();

    /** item to be sended */
    private LiteStorage sendQueue;

    /** shows GPS data */
    private GPSForm gpsForm;

    /** are we entering the form first time */
    private boolean firstTime = true;

    /**
	 * Initializes the bluetooth network browser
	 * @param serverEnabled should the server be started
	 */
    public BluetoothBrowser(boolean serverEnabled) {
        super("Bluetooth network");
        append(devices);
        addCommand(new Command("Back", Command.BACK, 1));
        setCommandListener(this);
        addCommand(cmdRefresh);
        try {
            BTService btServer = BTService.getInstance();
            if (btServer != null) {
                setTitle("BT on " + BTService.localName);
                if (serverEnabled) {
                    btServer.startServer();
                }
            }
        } catch (Exception e) {
            removeCommand(cmdRefresh);
            devices.setLabel("not supported");
        }
    }

    /**
	 * Adds a item into the sending queue.
	 * <p>
	 * TODO: currently queue length is one
	 * @param item item to be sended
	 */
    public void addToQueue(LiteStorage item) {
        this.sendQueue = item;
    }

    /**
	 * Handle event/activity from Bluetooth Network layer. This class is an
	 * implementation of BTListener; therefore, it handles all the bluetooth
	 * network event that received by NetLayer. The list of possible event are
	 * defined in BTListener.EVENT_XXX.
	 * 
	 * @see fi.hip.gb.bluetooth.BTListener#handleAction(String,EndPoint,Object)
	 */
    public void handleAction(String action, EndPoint endpt, Object data) {
        log("action=" + action);
        boolean finished = false;
        if (action.equals(BTListener.EVENT_SENT)) {
        } else if (action.equals(BTListener.EVENT_RECEIVED)) {
            GPSResult res = (GPSResult) data;
            LiteResult lr = new LiteResult();
            lr.setName(res.getName());
            lr.setType(res.getType());
            lr.writeObject(res.getFlags());
            if (this.gpsForm == null) {
                this.gpsForm = new GPSForm(lr);
                MIDui.next(this.gpsForm);
            } else {
                this.gpsForm.update(lr);
            }
        } else if (action.equals(BTListener.EVENT_FINISHED)) {
            removeCommand(cmdCancel);
            addCommand(cmdRefresh);
            finished = true;
        } else if (action.equals(BTListener.EVENT_SERVICES)) {
            devices.setLabel(SEARCH_SERVICES);
        } else {
            setDevice(endpt, action);
        }
        if (finished || devices.getLabel() == null || !devices.getLabel().equals(SEARCH_DEVICES) && !devices.getLabel().equals(SEARCH_SERVICES)) {
            if (devices.size() > 0) {
                addCommand(cmdSend);
                addCommand(cmdSendAll);
                addCommand(cmdSave);
                addCommand(cmdClose);
                devices.setLabel("Found " + devices.size() + " devices");
            } else {
                removeCommand(cmdSend);
                removeCommand(cmdSendAll);
                removeCommand(cmdSave);
                removeCommand(cmdClose);
                devices.setLabel("No devices found");
            }
        }
    }

    /**
	 * Commands from menu
	 * @param c command where the event originates
	 * @param disp display where the event was created
	 */
    public void commandAction(final Command c, Displayable disp) {
        if (c == cmdSend || c == cmdSendAll) {
            new Thread(new Runnable() {

                public void run() {
                    EndPoint endpt = null;
                    endpt = (EndPoint) endPoints.elementAt(devices.getSelectedIndex());
                    if (endpt.getConnections().length > 0) {
                        Service[] srv = endpt.getConnections();
                        for (int i = 0; i < srv.length; i++) {
                            try {
                                srv[i].openConnection();
                            } catch (IOException e) {
                                log(e.getMessage());
                            }
                        }
                    }
                }
            }).start();
        } else if (c == cmdSave) {
            String deviceName = devices.getString(devices.getSelectedIndex());
            EndPoint endpt = (EndPoint) endPoints.elementAt(devices.getSelectedIndex());
            if (endpt.getConnections().length > 0) {
                Service[] srv = endpt.getConnections();
                for (int i = 0; i < srv.length; i++) {
                    String url = srv[i].getURL();
                    Configs.storeString(Configs.INDEX_BLUETOOTSERVICE, url);
                    log("saved " + deviceName + " url " + url);
                }
            }
        } else if (c == cmdRefresh) {
            if (this.firstTime) {
                BTService.getInstance().addListener(this);
                this.firstTime = false;
            }
            this.devices.setLabel(SEARCH_DEVICES);
            while (size() > 1) delete(1);
            for (int i = 0; i < endPoints.size(); ) {
                if (devices.getImage(i) == null) {
                    devices.delete(i);
                    endPoints.removeElementAt(i);
                } else {
                    i++;
                }
            }
            addCommand(cmdCancel);
            removeCommand(cmdRefresh);
            removeCommand(cmdSend);
            removeCommand(cmdSendAll);
            try {
                BTService.getInstance().query();
            } catch (BluetoothStateException e) {
                e.printStackTrace();
                log("Query failed: " + e.getMessage());
            }
        } else if (c == cmdCancel) {
            BTService.getInstance().cancel();
            addCommand(cmdRefresh);
            removeCommand(cmdCancel);
        } else if (c == cmdClose) {
            if (this.devices.getSelectedIndex() != -1) {
                EndPoint endpt = (EndPoint) endPoints.elementAt(devices.getSelectedIndex());
                Service[] conns = endpt.getConnections();
                for (int i = 0; i < conns.length; i++) {
                    if (conns[i].getConnection() != null) conns[i].closeConnection();
                }
            }
        } else if ((c.getCommandType() == Command.BACK)) {
            if (this.devices.getSelectedIndex() != -1) {
                EndPoint endpt = (EndPoint) endPoints.elementAt(devices.getSelectedIndex());
                Service[] conns = endpt.getConnections();
                for (int i = 0; i < conns.length; i++) {
                    if (conns[i].getConnection() != null) conns[i].closeConnection();
                }
            }
            BTService.getInstance().cancel();
            BTService.getInstance().removeListener(this);
            MIDui.back(true);
        }
    }

    /**
	 * Adds a device or changes the state of existing device 
	 * @param endpt remote endpoint
	 * @param eventType state of the connection, one of the BTListener.EVENT_XXX
	 * types
	 */
    private void setDevice(EndPoint endpt, String eventType) {
        Image icon = null;
        try {
            if (eventType.equals(EVENT_COMPATIBLE)) icon = Image.createImage("/resources/bt_compatible.png"); else if (eventType.equals(EVENT_JOIN)) icon = Image.createImage("/resources/bt_connected.png");
        } catch (IOException ioe) {
        }
        for (int i = 0; i < endPoints.size(); i++) {
            if (endpt.equals(endPoints.elementAt(i))) {
                if (eventType.equals(EVENT_LOST)) {
                    devices.delete(i);
                    endPoints.removeElementAt(i);
                } else {
                    devices.set(i, endpt.toString(), icon);
                    endPoints.setElementAt(endpt, i);
                }
                return;
            }
        }
        devices.append(endpt.toString(), icon);
        endPoints.addElement(endpt);
    }

    /**
	 * Appends a log message to the form 
	 * @see fi.hip.gb.bluetooth.BTListener#log(java.lang.String)
	 */
    public void log(String message) {
        append("\n" + message);
    }
}
