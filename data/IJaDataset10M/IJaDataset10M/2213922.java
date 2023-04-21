package net.sf.btw.ui;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.bluetooth.UUID;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.sf.btw.btlib.ILookupListener;
import net.sf.btw.btlib.Peer;
import net.sf.btw.btlib.ServerInfo;
import net.sf.btw.btlib.ServerLookup;
import net.sf.btw.tools.Logger;

/**
 * The connector canvas that allows user to select whether it wants to be a
 * server or a client. It immediately starts scanning for servers in range.
 * 
 * @author Jan Tomka
 */
public final class ConnectorCanvas extends Canvas implements ILookupListener, CommandListener {

    /**
	 * The ConnectorCanvas line item representation. Can be a label common
	 * to the following selectable items, a selectable allowing user to
	 * choose, or a special scan item, displayed as the last item flashing
	 * dots while Bluetooth scan is in progress.
	 *
	 * @author Jan Tomka
	 */
    private final class ConnectorCanvasItem {

        /**
		 * Type value for selectable items.
		 */
        public static final int SELECTABLE = 0;

        /**
		 * Type value for label items.
		 */
        public static final int LABEL = 1;

        /**
		 * Type value for single special scan item.
		 */
        public static final int SCAN = 2;

        /**
		 * String displayed, representing an item.
		 */
        private String string;

        /**
		 * Type of item, one of SELECTABLE, LABEL or SCAN. This value
		 * cannot be changed during the item lifetime.
		 */
        private final int type;

        /**
		 * Creates new connector canvas item.
		 *
		 * @param string
		 * @param type
		 */
        public ConnectorCanvasItem(String string, int type) {
            this.string = string;
            this.type = type;
        }

        /**
		 * Sets connector canvas item string.
		 *
		 * @param string
		 *            New value of item string.
		 */
        public void setString(String string) {
            this.string = string;
        }

        /**
		 * Returns current item string.
		 *
		 * @return Current value of item string.
		 */
        public String getString() {
            return string;
        }

        /**
		 * Returns current item type.
		 *
		 * @return 
		 */
        public int getType() {
            return type;
        }

        /**
		 * Retuns <code>true</code> if item is selectable. Effectively
		 * identical to condition <code>type ==
		 * ConnectorCanvasItem.SELECTABLE</code>.
		 */
        public boolean isSelectable() {
            return type == ConnectorCanvasItem.SELECTABLE;
        }
    }

    /**
	 * Vector of ConnectorCanvasItem's currently present in
	 * ConnectorCanvas.
	 */
    private final Vector items = new Vector();

    /**
	 * Font used to draw item strings. Always the device's default font.
	 */
    private static final Font font = Font.getDefaultFont();

    /**
	 * Canvas background color. Default value is 0xffffff (white).
	 */
    private int bgColor = 0xffffff;

    /**
	 * Label item text color. Default value is 0x000000 (black).
	 */
    private int labelColor = 0x000000;

    /**
	 * Label item background color. Default value is 0xffffff (white).
	 */
    private int labelBgColor = 0xffffff;

    /**
	 * Selectable item text color. Default value is 0x0000ff (blue).
	 */
    private int selectableColor = 0x0000ff;

    /**
	 * Selectable item background color. Default value is 0xffffff
	 * (white).
	 */
    private int selectableBgColor = 0xffffff;

    /**
	 * Selected item text color. Default value is 0xffffff (white).
	 */
    private int selectedColor = 0xffffff;

    /**
	 * Selected item background color. Default value is 0x0000ff (blue).
	 */
    private int selectedBgColor = 0x0000ff;

    /**
	 * Label item left side offset. Label item is drawn this number of
	 * pixels from the canvas' left border.
	 */
    private int labelOffset = 2;

    /**
	 * Selectable item left side offset. Selectable item is drawn this
	 * number of pixels from the canvas' left border.
	 */
    private int selectableOffset = 12;

    /**
	 * Index of currently selected item. Only items with type SELECTABLE
	 * can be selected. When ConnectorCanvas is created, no item is
	 * selected before one is appended. From then on, the first item
	 * appended is selected by default.
	 */
    private int itemSelected = -1;

    /**
	 * Index of special scan item. There can be only one item present with
	 * type <code>SCAN</code>. It's position is always at the very end of
	 * the items vector. Thus, the two possible values of this variable at
	 * any time are <code>-1</code> (no scan item present) or
	 * <code>(items.size() - 1)</code>.
	 */
    private int scanItem = -1;

    /**
	 * Number of dots that flash at the end of a special scan line. Value
	 * of this constant is 3.
	 */
    private final int lastDotsMax = 3;

    /**
	 * Current number of dots drawn at the end of a special scan line.
	 */
    private int lastDots = 0;

    /**
	 * Timer used to periodically update number of dots drawn at the end
	 * of a special scan line.
	 */
    private Timer dotsTimer = new Timer();

    /**
	 * Task that performs updates of number of dots drawn at the end of a
	 * special scan line.
	 */
    private TimerTask dotsTimerTask = new DotsTimerTask();

    private class DotsTimerTask extends TimerTask {

        public void run() {
            lastDots = (lastDots + 1) % (lastDotsMax + 1);
            repaint();
        }
    }

    ;

    private final Command startCommand = new Command("Start", Command.OK, 1);

    /**
	 * Command that causes ConnectorCanvas perform Bluetooth rescan.
	 */
    private final Command refreshCommand = new Command("Refresh", Command.SCREEN, 1);

    private final Command logCommand = new Command("Show log", Command.SCREEN, 2);

    /**
	 * Command that closes the ConnectorCanvas itself.
	 */
    private final Command closeCommand = new Command("Close", Command.BACK, 1);

    /**
	 * The server UUID.
	 */
    public final UUID serverId;

    /**
	 * A list of nearby servers. List of {@link ServerInfo} instances.
	 */
    private final Vector servers = new Vector();

    /**
	 * Used for server scanning. If <code>null</code> then there is no running
	 * lookup.
	 */
    private volatile ServerLookup lookup;

    /**
	 * The display instance.
	 */
    private final Display display;

    /**
	 * Connector listener interface implemented on the application side.
	 * ConnectorCanvas events are sent to it, to make application respond
	 * accordingly to the user actions done in ConnectorCanvas.
	 */
    private final IConnectorListener listener;

    /**
	 * String to be assigned to the label item displayed above the server
	 * name.
	 */
    private final String serverLabel;

    /**
	 * String to be assigned to the label item displayed above the list of
	 * client names.
	 */
    private final String clientLabel;

    /**
	 * Creates new connector form.
	 * 
	 * @param uuid
	 *            UUID of the application.
	 * @param display
	 *            the display instance.
	 * @param listener
	 *            listens for connector events.
	 */
    public ConnectorCanvas(final UUID uuid, final Display display, final IConnectorListener listener, String serverLabel, String clientLabel) {
        super();
        this.listener = listener;
        this.display = display;
        this.serverId = uuid;
        this.serverLabel = serverLabel;
        this.clientLabel = clientLabel;
        addCommand(startCommand);
        addCommand(refreshCommand);
        addCommand(closeCommand);
        if (Logger.isLoggable(Logger.LEVEL_INFO)) addCommand(logCommand);
        initCanvas();
        startServerScan();
        setCommandListener(this);
    }

    /**
	 * Initializes ConnectorCanvas. Removes all items and appends label
	 * with serverLabel, selectable server name, label with clientLabel
	 * and special scan item.
	 */
    private void initCanvas() {
        items.removeAllElements();
        itemSelected = -1;
        scanItem = -1;
        final String deviceName = Peer.getDeviceName();
        appendItem(this.serverLabel, ConnectorCanvasItem.LABEL);
        appendItem(deviceName, ConnectorCanvasItem.SELECTABLE);
        appendItem(clientLabel, ConnectorCanvasItem.LABEL);
        appendItem("Scanning", ConnectorCanvasItem.SCAN);
    }

    /**
	 * Returns <code>true</code> if canvas items contain special scan
	 * item.
	 */
    private boolean hasScanLine() {
        return scanItem >= 0;
    }

    /**
	 * Sets canvas background color.
	 */
    public void setBgColor(int v) {
        bgColor = v;
    }

    /**
	 * Sets label item text color.
	 */
    public void setLabelColor(int v) {
        labelColor = v;
    }

    /**
	 * Sets label item background color.
	 */
    public void setLabelBgColor(int v) {
        labelBgColor = v;
    }

    /**
	 * Sets selectable item text color.
	 */
    public void setSelectableColor(int v) {
        selectableColor = v;
    }

    /**
	 * Sets selectable item background color.
	 */
    public void setSelectableBgColor(int v) {
        selectableBgColor = v;
    }

    /**
	 * Sets selected item text color.
	 */
    public void setSelectedColor(int v) {
        selectedColor = v;
    }

    /**
	 * Sets selected item background color.
	 */
    public void setSelectedBgColor(int v) {
        selectedBgColor = v;
    }

    /**
	 * Sets label item left side offset.
	 */
    public void setLabelOffset(int v) {
        labelOffset = v;
    }

    /**
	 * Sets selectable item left side offset.
	 */
    public void setSelectableOffset(int v) {
        selectableOffset = v;
    }

    /**
	 * Appends an item to the ConnectorCanvas. Updates internal canvas
	 * state accordingly and repaints canvas.
	 */
    public void appendItem(String s, int type) {
        ConnectorCanvasItem item = new ConnectorCanvasItem(s, type);
        int newIndex;
        if (hasScanLine()) {
            newIndex = scanItem;
            scanItem++;
        } else newIndex = items.size();
        if (item.isSelectable() && itemSelected < 0) itemSelected = newIndex; else if (type == ConnectorCanvasItem.SCAN) {
            if (hasScanLine()) return; else scanItem = newIndex;
        }
        if (newIndex >= items.size()) items.addElement(item); else items.insertElementAt(item, newIndex);
        repaint();
    }

    /**
	 * Paints connector canvas. Iterates through the items vector, sets
	 * visual properties of current item and draws its string.
	 */
    public void paint(Graphics g) {
        g.setColor(bgColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        for (int i = 0; i < items.size(); i++) {
            ConnectorCanvasItem item = (ConnectorCanvasItem) items.elementAt(i);
            int offset;
            int color;
            int bgcolor;
            if (item.getType() == ConnectorCanvasItem.LABEL) {
                offset = labelOffset;
                color = labelColor;
                bgcolor = labelBgColor;
            } else {
                offset = selectableOffset;
                if (i == itemSelected) {
                    color = selectedColor;
                    bgcolor = selectedBgColor;
                } else {
                    color = selectableColor;
                    bgcolor = selectableBgColor;
                }
            }
            String s = item.getString();
            String sw = s;
            if (item.getType() == ConnectorCanvasItem.SCAN) {
                for (int j = 0; j < lastDots; j++) s += ".";
                sw = s;
                for (int j = lastDots; i < lastDotsMax; j++) {
                    sw += ".";
                }
            }
            g.setColor(bgcolor);
            g.fillRect(offset, font.getHeight() * i, font.stringWidth(sw), font.getHeight());
            g.setColor(color);
            g.drawString(s, offset, font.getHeight() * i, Graphics.TOP | Graphics.LEFT);
        }
    }

    /**
	 * Starts a scan for nearby bluetooth servers. Reinitializes connector
	 * canvas, starts flashing dots at the end of special scan item and
	 * starts scanning.
	 */
    private void startServerScan() {
        stopServerScan();
        initCanvas();
        dotsTimer.schedule(dotsTimerTask, 300, 300);
        servers.removeAllElements();
        lookup = new ServerLookup(serverId, this);
        lookup.searchForServers();
    }

    /**
	 * Stops the scan process. Stops flashing dots at the end of special
	 * scan item, removes it from the items and interrupts the server
	 * lookup thread.
	 */
    private void stopServerScan() {
        if (hasScanLine()) {
            dotsTimerTask.cancel();
            dotsTimerTask = new DotsTimerTask();
            items.removeElementAt(scanItem);
            scanItem = -1;
            repaint();
        }
        final ServerLookup l = lookup;
        if (l != null) {
            l.interrupt();
            try {
                l.join();
            } catch (InterruptedException e) {
                Logger.warn(null, e);
            }
        }
        lookup = null;
    }

    /**
	 * Handles event of new server found. Appends new server information into
	 * servers vector and appends its displayable name to the list of
	 * canvas items.
	 * 
	 * @param server
	 *             Information about new server found.
	 *
	 * @see net.sf.btw.btlib.IClientListener#serverFound(net.sf.btw.btlib.ServerInfo)
	 */
    public void serverFound(ServerInfo server) {
        servers.addElement(server);
        appendItem(server.displayableName, ConnectorCanvasItem.SELECTABLE);
    }

    /**
	 * Handles an error occurance while scan is in progress. Displays an
	 * error alert for 3 seconds.
	 * 
	 * @param ex
	 *           Exception containing information about the error that
	 *           occured.
	 *
	 * @see net.sf.btw.btlib.IClientListener#serverScanError(java.lang.Exception)
	 */
    public void serverScanError(Exception ex) {
        final Alert alert = new Alert("Error", "Error occured. Please see log for details. " + ex.getMessage(), null, AlertType.ERROR);
        alert.setTimeout(3000);
        display.setCurrent(alert, this);
    }

    /**
	 * Handles the end of scanning process. Stops flashing dots after the
	 * special scan line and removes it from the canvas items.
	 * 
	 * @param servers
	 *            Unused.
	 * @param completely
	 *            Unused.
	 *
	 * @see net.sf.btw.btlib.IClientListener#serverScanFinished(java.util.Vector,
	 *      boolean)
	 */
    public void serverScanFinished(Vector servers, boolean completely) {
        if (scanItem >= 0) {
            dotsTimerTask.cancel();
            dotsTimerTask = new DotsTimerTask();
            items.removeElementAt(scanItem);
            scanItem = -1;
            repaint();
        }
    }

    /**
	 * Returns the sequence number of currently selected item, ignoring
	 * all items that are not selectable. The result is index of currently
	 * selected item it would have if only selectable items were present
	 * in connector canvas item vector.
	 */
    private int getSelectedItemIndex() {
        int result = 0;
        for (int i = 0; i < items.size(); i++) {
            ConnectorCanvasItem item = (ConnectorCanvasItem) items.elementAt(i);
            if (item.isSelectable()) if (i == itemSelected) return result; else result++;
        }
        return -1;
    }

    /**
	 * Handle events when user presses a keypad button. Performs item
	 * selecting by means of UP/DOWN user actions and starting a server,
	 * or connecting to one when FIRE user action is received.
	 *
	 * @param keyCode
	 *            Code of key the user pressed.
	 */
    public void keyPressed(int keyCode) {
        keyCode = getGameAction(keyCode);
        if (itemSelected < 0) return;
        int savItemSelected = itemSelected;
        if (keyCode == Canvas.DOWN) {
            for (int i = itemSelected + 1; i < items.size(); i++) {
                ConnectorCanvasItem item = (ConnectorCanvasItem) items.elementAt(i);
                if (item.isSelectable()) itemSelected = i;
            }
        } else if (keyCode == Canvas.UP) {
            for (int i = itemSelected - 1; i >= 0; i--) {
                ConnectorCanvasItem item = (ConnectorCanvasItem) items.elementAt(i);
                if (item.isSelectable()) itemSelected = i;
            }
        } else if (keyCode == Canvas.FIRE) {
            final int index = getSelectedItemIndex();
            if (index < 0) return;
            stopServerScan();
            if (index == 0) {
                listener.startServer();
                return;
            }
            final ServerInfo info = (ServerInfo) servers.elementAt(index - 1);
            listener.connectToServer(info);
        }
        if (savItemSelected != itemSelected) repaint();
    }

    /**
	 * Handle events when user fires a command. When refresh command is
	 * received, restart scan process. When close command is received,
	 * close the connector canvas.
	 * 
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
	 *      javax.microedition.lcdui.Displayable)
	 */
    public void commandAction(Command cmd, Displayable arg1) {
        try {
            if (cmd == startCommand) {
                keyPressed(getKeyCode(Canvas.FIRE));
            } else if (cmd == refreshCommand) {
                startServerScan();
            } else if (cmd == closeCommand) {
                stopServerScan();
                listener.closeConnector();
            } else if (cmd == logCommand) {
                new Logger(this, display);
            }
        } catch (Exception e) {
            Logger.error("ConnectorCanvas.commandAction()", e);
            display.setCurrent(new Alert("Error", "Error occured. Please see log for details. " + e.getMessage(), null, AlertType.ERROR), this);
        }
    }
}
