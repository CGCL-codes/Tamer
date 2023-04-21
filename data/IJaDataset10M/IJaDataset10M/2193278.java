package org.jnav.coreui;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.GridLayout;
import java.io.IOException;
import org.jnav.core.FlowController;

/**
 *
 * @author Matthias Lohr <mlohr@jnav.org>
 */
public class SettingsMenu extends Form {

    GridLayout grid = new GridLayout(5, 1);

    Command cmdDeviceSelection;

    Command cmdReturn;

    Button btnDeviceSelection;

    Button btnReturn;

    public SettingsMenu(ActionListener commandListener) {
        super("Settings");
        addCommandListener(commandListener);
        setLayout(grid);
        try {
            cmdDeviceSelection = new Command("GPS device", Image.createImage("/icons/placeholder.png"), FlowController.CMD_DEVICESELECTION);
            cmdReturn = new Command("Back", Image.createImage("/icons/placeholder.png"), FlowController.CMD_RETURN);
            btnDeviceSelection = new Button(cmdDeviceSelection);
            btnReturn = new Button(cmdReturn);
            addComponent(btnDeviceSelection);
            addCommand(cmdReturn);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
