package org.movino;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import javax.microedition.midlet.MIDletStateChangeException;
import org.movino.connection.ConnectionMenu;
import org.movino.connection.ConnectionThread;
import org.movino.connection.MovinoConnection;

public class MovinoMenu extends Returnable implements CommandListener {

    private static final String CONNECT = "Connect";

    private static final String DISCONNECT = "Disconnect";

    private static final String STREAM_VIDEO = "Record";

    private static final String OPTIONS = "Options";

    private static final String EXIT = "Exit";

    private static final String[] MAIN_MENU = new String[] { CONNECT, STREAM_VIDEO, OPTIONS, EXIT };

    private List mainMenu;

    private Command selectCommand;

    private ConnectionThread connectionThread;

    private MovinoMidlet movinoMidlet;

    public MovinoMenu(MovinoMidlet midlet) {
        movinoMidlet = midlet;
        connectionThread = new ConnectionThread();
        connectionThread.start();
        Options.loadOptions();
    }

    public void show(Display display) {
        selectCommand = new Command("Select", Command.ITEM, 1);
        mainMenu = new List("Movino", List.IMPLICIT, MAIN_MENU, null);
        mainMenu.setCommandListener(this);
        mainMenu.setSelectCommand(selectCommand);
        if (MovinoConnection.currentConnection != null) {
            mainMenu.set(0, DISCONNECT, null);
        }
        display.setCurrent(mainMenu);
    }

    public void reShow(Display display) {
        if (MovinoConnection.currentConnection != null) {
            mainMenu.set(0, DISCONNECT, null);
        }
        display.setCurrent(mainMenu);
    }

    public void simpleAlert(String mess) {
        Alert alert = new Alert("Message", mess, null, AlertType.INFO);
        alert.setTimeout(Alert.FOREVER);
        Display display = Display.getDisplay(movinoMidlet);
        display.setCurrent(alert);
        System.out.println(mess);
    }

    public void commandAction(Command cmd, Displayable disp) {
        if (disp == mainMenu) {
            if (cmd == selectCommand) {
                int sel_index = mainMenu.getSelectedIndex();
                if (sel_index >= 0) {
                    String sel = mainMenu.getString(sel_index);
                    if (sel == CONNECT) {
                        new ConnectionMenu().show(Display.getDisplay(movinoMidlet), this);
                    } else if (sel == DISCONNECT) {
                        MovinoConnection mc = MovinoConnection.currentConnection;
                        if (mc != null) {
                            mc.abort();
                        }
                        mainMenu.set(0, CONNECT, null);
                    } else if (sel == STREAM_VIDEO) {
                        new Capture().show(Display.getDisplay(movinoMidlet), this);
                    } else if (sel == OPTIONS) {
                        new Options().show(Display.getDisplay(movinoMidlet), this);
                    } else if (sel == EXIT) {
                        Options.saveOptions();
                        connectionThread.shutdownConnections();
                        try {
                            movinoMidlet.destroyApp(false);
                        } catch (MIDletStateChangeException e) {
                        }
                        movinoMidlet.notifyDestroyed();
                    }
                }
            }
        }
    }
}
