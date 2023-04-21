package app_files.images;

import app_files.MediaNet;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

/**
 *
 * @author Christopher Standley
 */
public class MenuSelectionMethod implements CommandListener {

    /** This command goes to the main screen. */
    private final Command backCommand = new Command("Back", Command.BACK, 2);

    /** The list control to configure items to share. */
    private final List list = new List("Images", List.IMPLICIT);

    /** Keeps the parent MIDlet reference to process specific actions. */
    private MediaNet parent;

    /** Constructs server GUI. */
    public MenuSelectionMethod(MediaNet parent) {
        this.parent = parent;
        setupList();
        list.addCommand(backCommand);
        list.setCommandListener(this);
        Display.getDisplay(parent).setCurrent(list);
    }

    /**
     * Process the command event.
     *
     * @param c - the issued command.
     * @param d - the screen object the command was issued for.
     */
    public void commandAction(Command c, Displayable d) {
        if ((c == backCommand) && (d == list)) {
            destroy();
            parent.show();
            return;
        }
        switch(list.getSelectedIndex()) {
            case 0:
                new app_files.bluetooth.GUIImageClient(parent);
                break;
            default:
                System.err.println("Unexpected choice...");
                break;
        }
    }

    /** Destroys this component. */
    public void destroy() {
    }

    /**
     * Reads in a list of files from the xml file useing the ReadFile class.
     */
    private void setupList() {
        list.setCommandListener(this);
        list.append("Networked", null);
    }
}
