package mpc.pong;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

public class BluetoothOption {

    private List BlueMenu;

    private Command bckCmd;

    private Command extCmd;

    private PongMidlet midlet;

    private PongOptionGame OptionScreen;

    private PongServer BlueServer;

    private PongClient BlueClient;

    public BluetoothOption(PongMidlet midlet, PongOptionGame OptionScreen) {
        this.midlet = midlet;
        this.OptionScreen = OptionScreen;
        init();
    }

    private void init() {
        BlueMenu = new List("Bluetooth Menu", List.IMPLICIT);
        BlueMenu.append("Client", null);
        BlueMenu.append("Server", null);
        bckCmd = new Command("Back", Command.BACK, 1);
        BlueMenu.addCommand(bckCmd);
        extCmd = new Command("Exit", Command.EXIT, 2);
        BlueMenu.addCommand(extCmd);
        BlueMenu.setCommandListener(new CommandListener() {

            public void commandAction(Command arg0, Displayable arg1) {
                if (arg0 == List.SELECT_COMMAND) {
                    int pos = BlueMenu.getSelectedIndex();
                    switch(pos) {
                        case 0:
                            startClient();
                            break;
                        case 1:
                            startServer();
                            break;
                        default:
                            break;
                    }
                } else if (arg0 == bckCmd) {
                    previousMenu();
                } else if (arg0 == extCmd) {
                    try {
                        midlet.destroyApp(false);
                    } catch (MIDletStateChangeException e) {
                    }
                }
            }
        });
        BlueServer = new PongServer(midlet, this);
        BlueClient = new PongClient(midlet, this);
    }

    protected void previousMenu() {
        midlet.display().setCurrent(OptionScreen.Menu());
    }

    protected void startServer() {
        midlet.display().setCurrent(BlueServer);
        BlueServer.startGame();
    }

    protected void startClient() {
        midlet.display().setCurrent(BlueClient);
        BlueClient.startGame();
    }

    protected List Menu() {
        return BlueMenu;
    }
}
