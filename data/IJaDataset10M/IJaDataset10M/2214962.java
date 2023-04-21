package net.sourceforge.obexftpfrontend.gui.action;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import net.sourceforge.obexftpfrontend.command.OBEXFTPCommandQueue;
import net.sourceforge.obexftpfrontend.gui.DeviceInfoDialog;
import net.sourceforge.obexftpfrontend.model.ConfigurationHolder;
import net.sourceforge.obexftpfrontend.obexftp.DevInfoFileParser;

/**
 * Action that is responsible to create and show the 'show device info' dialog.
 * @author Daniel F. Martins
 */
public class ShowDeviceInfoAction extends DefaultAction {

    /**
     * Create a new instance of ShowDeviceInfoAction.
     * @param parent Parent frame.
     * @param configHolder Object that manages the user' preferences.
     * @param queue Command queue to be used.
     * @param devInfoParser Object used to parse the device information file.
     */
    public ShowDeviceInfoAction(Window parent, ConfigurationHolder configHolder, OBEXFTPCommandQueue queue, DevInfoFileParser devInfoParser) {
        super(new DeviceInfoDialog(parent, configHolder, queue, devInfoParser));
        putValue(NAME, "Device info");
        putValue(SHORT_DESCRIPTION, "Show some info about the connected device");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_MASK));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                getParentWindow().setVisible(true);
            }
        });
    }
}
