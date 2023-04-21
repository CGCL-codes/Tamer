package gov.sandia.ccaffeine.dc.user_iface.gui.guicmd;

import java.util.*;
import gov.sandia.ccaffeine.cmd.*;
import gov.sandia.ccaffeine.dc.user_iface.gui.guicmd.CmdActionGUI;

/**
 * CmdActionGUISetProperty.java
 */
public class CmdActionGUISetPortProperty extends CmdActionGUI implements CmdAction {

    private static final String[] namelist = { "PortProperty" };

    public CmdActionGUISetPortProperty() {
    }

    /**
   * setPortProperty
   *   <component instance>
   *   <port instance>
   *   <key string>
   *   <datatype string>
   *   <value string>
   */
    public String argtype() {
        return "ISSSS";
    }

    public String[] names() {
        return namelist;
    }

    public String help() {
        return "Sets a key value pair onto the gui representation of " + "a port";
    }

    public void doIt(CmdContext cc, Vector args) {
        String componentInstanceName = (String) args.get(0);
        String portInstanceName = (String) args.get(1);
        String propertyName = (String) args.get(2);
        String dataType = (String) args.get(3);
        String propertyValue = (String) args.get(4);
        this.broadcastSetPortProperty(componentInstanceName, portInstanceName, propertyName, dataType, propertyValue);
    }
}
