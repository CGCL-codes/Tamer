package br.com.qotsa.j2me.btsoftcontrol.remotecontrol.screen;

import br.com.qotsa.btsoftcontrolprotocol.handler.BTSoftControlProtocolClientSynchronization;
import br.com.qotsa.j2me.btsoftcontrol.bluetooth.BluetoothDiscoveryController;
import br.com.qotsa.j2me.btsoftcontrol.language.Resource;
import br.com.qotsa.j2me.btsoftcontrol.language.ResourceFactory;
import br.com.qotsa.j2me.btsoftcontrol.main.screen.ApplicationScreen;
import br.com.qotsa.j2me.btsoftcontrol.main.DisplayController;
import br.com.qotsa.j2me.btsoftcontrol.remotecontrol.WinampPlayerController;
import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

/**
 *
 * @author Francisco Guimar�es
 */
public abstract class MainRemoteControlScreen implements ApplicationScreen, CommandListener {

    private static final int WINAMP = 0;

    private static final int COMPUTER = 1;

    private static Resource resource = ResourceFactory.getResource();

    final Command COMMAND_SELECT = new Command(resource.getSelectMessage(), Command.OK, 0);

    final Command COMMAND_DISCONNECT = new Command(resource.getDisconnectMessage(), Command.BACK, 0);

    public MainRemoteControlScreen() {
        paintScreen();
        getScreen().addCommand(COMMAND_SELECT);
        getScreen().addCommand(COMMAND_DISCONNECT);
        getScreen().setCommandListener(this);
    }

    abstract int getSelectedIndex();

    public void commandAction(Command c, Displayable d) {
        if (c == COMMAND_SELECT) {
            if (getSelectedIndex() == WINAMP) {
                WinampPlayerController.initInstance(getScreen());
            } else if (getSelectedIndex() == COMPUTER) {
            }
        } else if (c == COMMAND_DISCONNECT) {
            try {
                BTSoftControlProtocolClientSynchronization.disconnect();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                DisplayController.showErrorAndBackToMainMenu(ResourceFactory.getResource().getDisconnectedFromMessage().concat(" ").concat(BluetoothDiscoveryController.getServerName()));
            }
        }
    }
}
