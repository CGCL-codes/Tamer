package gov.sandia.ccaffeine.dc.user_iface.gui.guicmd;

import java.util.*;
import gov.sandia.ccaffeine.cmd.*;

/**
 * CmdActionGUIExit.java
 *
 * If the end-user clicks on the File->Exit menu item
 * then the cca server breaks its connection with this
 * client.  Before severing the connection, the cca
 * server sends a disconnect message to the client.
 * The client responds by closing the application.
 */
public class CmdActionGUIExit extends CmdActionGUI implements CmdAction {

    public CmdActionGUIExit() {
    }

    public String argtype() {
        return "";
    }

    public String[] names() {
        return namelist;
    }

    public String help() {
        return "quits the program.";
    }

    private static final String[] namelist = { "exit", "bye!" };

    /** It is not safe to assume the input to this is valid,
	as the action of the connection already done in the
        framework may invalidate the ports being connected.
    */
    public void doIt(CmdContext cc, Vector args) {
        this.broadcastExit();
    }
}
