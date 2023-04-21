package mobiledesktopclient;

import javax.microedition.lcdui.*;

/**
 * An extended version of <code>javax.microedition.lcdui.Form</code> that displays
 * the area to enter the GPRS connection information
 * @version 1.0 03/04/08
 * @author Anjan Nepal
 * @author Manish Modi
 */
public class BluetoothForm extends Form {

    /** The textfiled to enter the UUID of the server*/
    private TextField uuidField;

    /** javax.microedition.lcdui.Command instance for confirming the input*/
    public Command connectCommand;

    /** javax.microedition.lcdui.Command instance for canceling the input*/
    public Command exitCommand;

    /** Creates a new instance of ConnectionForm */
    public BluetoothForm() {
        super("Bluetooth Form");
        uuidField = new TextField("Server UUID", "11122233344455566677788899900000", 32, TextField.ANY);
        connectCommand = new Command("Connect", Command.SCREEN, 0);
        exitCommand = new Command("Exit", Command.EXIT, 0);
        append(uuidField);
        addCommand(exitCommand);
        addCommand(connectCommand);
    }

    /** returns the <code>String</code> of the IP address */
    public String getUUID() {
        return uuidField.getString();
    }
}
