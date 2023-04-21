package at.momberban.game.me.tests;

import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import at.momberban.game.me.BTClientNetworkManager;
import at.momberban.game.me.ClientNetworkManager;

/**
 * A simple client network manager test MIDlet.
 * @author nfk
 */
public final class BTClientNetworkManagerTester extends MIDlet implements CommandListener {

    private List list;

    private Command exit;

    private Command discoverCached;

    private Command discoverNew;

    private Command connect;

    private Display display;

    private ClientNetworkManager nm;

    public BTClientNetworkManagerTester() {
        this.list = new List("BTClientNetworkManagerTester", List.IMPLICIT);
        this.exit = new Command("Exit", Command.EXIT, 1);
        this.discoverCached = new Command("Cached discover", Command.OK, 1);
        this.discoverNew = new Command("New discover", Command.OK, 1);
        this.connect = new Command("Connect", Command.OK, 1);
        this.list.addCommand(this.exit);
        this.list.addCommand(this.discoverCached);
        this.list.addCommand(this.discoverNew);
        this.list.addCommand(this.connect);
        this.list.setCommandListener(this);
        this.display = Display.getDisplay(this);
        this.nm = BTClientNetworkManager.getManager();
    }

    /**
     * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
     */
    protected void destroyApp(final boolean arg0) throws MIDletStateChangeException {
        if (BTClientNetworkManager.supportsBluetooth()) {
            this.nm.disconnect();
        }
    }

    /**
     * @see javax.microedition.midlet.MIDlet#pauseApp()
     */
    protected void pauseApp() {
    }

    /**
     * @see javax.microedition.midlet.MIDlet#startApp()
     */
    protected void startApp() throws MIDletStateChangeException {
        if (!BTClientNetworkManager.supportsBluetooth()) {
            final Alert noBluetooth = new Alert("Error", "No bluetooth available", null, AlertType.ERROR);
            noBluetooth.addCommand(exit);
            noBluetooth.setCommandListener(this);
            noBluetooth.setTimeout(Alert.FOREVER);
            display.setCurrent(noBluetooth);
        } else {
            display.setCurrent(list);
        }
    }

    /**
     * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command,
     *      javax.microedition.lcdui.Displayable)
     */
    public void commandAction(final Command arg0, final Displayable arg1) {
        if (arg0 == this.exit) {
            try {
                destroyApp(false);
                notifyDestroyed();
            } catch (final MIDletStateChangeException ex) {
            }
        } else if (arg0 == discoverCached) {
            this.list.deleteAll();
            this.list.append("Discovering ...", null);
            String[] s = null;
            try {
                s = this.nm.discover(false);
            } catch (final IOException ex) {
            }
            fillList(s);
        } else if (arg0 == discoverNew) {
            this.list.deleteAll();
            this.list.append("Discovering ...", null);
            String[] s = null;
            try {
                s = this.nm.discover(true);
            } catch (final IOException ex) {
            }
            fillList(s);
        } else if (arg0 == connect) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        boolean connected = nm.connect(list.getSelectedIndex());
                        if (connected) {
                            Alert a = new Alert("Connection seem to be successful!");
                            a.setTimeout(Alert.FOREVER);
                            Display.getDisplay(BTClientNetworkManagerTester.this).setCurrent(a);
                        }
                    } catch (final IOException ex) {
                        ex.printStackTrace();
                    } finally {
                        nm.disconnect();
                    }
                }
            }).start();
        }
    }

    /** Helper method to fill list. */
    private void fillList(final String[] s) {
        this.list.deleteAll();
        if (s == null || s.length == 0) {
            this.list.append("No servers!", null);
        } else {
            for (int i = 0; i < s.length; i++) {
                this.list.append(s[i], null);
            }
        }
    }
}
