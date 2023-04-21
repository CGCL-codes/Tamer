package gov.sandia.ccaffeine.dc.user_iface.gui.guicmd;

import java.util.*;
import gov.sandia.ccaffeine.cmd.*;
import gov.sandia.ccaffeine.dc.user_iface.gui.guicmd.CmdActionGUI;

/**
 * CmdActionGUIParamDialog.java
 *
 * Cca components contain ports.
 * Some of the ports contain data fields.
 * This event can be used to notify components
 * that the cca server wants the client to
 * display a dialog box that contains the values
 * of all the data fields in the port.  A view
 * entity might respond by displaying an empty
 * dialog box.
 * <p>
 * Possible Scenario: <br>
 * An end-user clicks on a blue port inside of a component <br>
 * client sends "parameters" to server <br>
 * serer- sends "ParamDialog" to client <br>
 * client responds by creating an empty dialog box <br>
 * server sends "ParamTab" to client <br>
 * client responds by inserting a new tab in the dialog box <br>
 * server sends "ParamField" to client <br>
 * client responds by inserting a blank data line into the dialog box <br>
 * server sends "ParamCurrent" to client <br>
 * client responds by inserting the data's value into the dialog box <br>
 * server sends "ParamHelp" to client <br>
 * client responds by setting the text that is displayed if the help button is clicked <br>
 * server sends "ParamPrompt" to client <br>
 * client responds by displaying a prompt to the left of the data's value <br>
 * server sends "ParamDefault" to client <br>
 * client responds by setting the data's default value <br>
 * server sends "ParamStringChoice" to client <br>
 * client responds by setting an item in the value's choice box <br>
 * server sends "ParamNumberRange" to client <br>
 * client responds by setting the data value's range of allowed values <br>
 * server sends  "ParamEndDialog" to client <br>
 * client responds by displaying the dialog box on the screen <br> *
 */
public class CmdActionGUIParamDialog extends CmdActionGUI implements CmdAction {

    public CmdActionGUIParamDialog() {
    }

    public String argtype() {
        return "ISA";
    }

    public String[] names() {
        return namelist;
    }

    public String help() {
        return "creates a new ConfigureDialog to be filled with forthcoming data.";
    }

    private static final String[] namelist = { "dialog", "newParamDialog" };

    public void doIt(CmdContext cc, Vector args) {
        String componentInstanceName = (String) args.get(0);
        String portInstanceName = (String) args.get(1);
        String titleOfDialogBox = (String) args.get(2);
        this.broadcastParamDialog(componentInstanceName, portInstanceName, titleOfDialogBox);
    }
}
