package main;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class MainMIDlet extends MIDlet {

    private Displayable welcomeScreen;

    private Displayable gameScreen;

    private final Command exitCommand = new Command("Exit", Command.EXIT, 2);

    private final Command startCommand = new Command("Start", "Start game", Command.SCREEN, 1);

    private final Command endCommand = new Command("End", "End game", Command.BACK, 1);

    private Display display;

    private void createWelcomeScreen() {
        StringItem label = new StringItem(null, "\nA simple Tic-Tac-Toe over bluetooth.\nEnjoy!");
        Form form = new Form("Tic-Tac-Toe");
        form.append(label);
        form.addCommand(exitCommand);
        form.addCommand(startCommand);
        welcomeScreen = form;
        welcomeScreen.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == exitCommand) {
                    destroyApp(false);
                    notifyDestroyed();
                } else if (c == startCommand) display.setCurrent(gameScreen);
            }
        });
    }

    private void createGameScreen() {
        gameScreen.addCommand(exitCommand);
        gameScreen.addCommand(endCommand);
        gameScreen.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == exitCommand) {
                    destroyApp(false);
                    notifyDestroyed();
                } else if (c == endCommand) {
                    createGameScreen();
                    display.setCurrent(welcomeScreen);
                }
            }
        });
    }

    public MainMIDlet() {
        display = Display.getDisplay(this);
        createWelcomeScreen();
        createGameScreen();
    }

    public void startApp() {
        display.setCurrent(welcomeScreen);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }
}
