package net.tmro.mobile.txtapark;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import net.tmro.mobile.txtapark.forms.MenuForm;

public class HelloMIDlet extends MIDlet implements CommandListener {

    public static final String HISTORY_SEPARATOR = "s";

    public final Command exitCommand;

    public final Command menuCommand;

    public final MenuForm menuForm;

    private Display display;

    public HelloMIDlet() {
        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 0);
        menuCommand = new Command("Menu", Command.BACK, 1);
        menuForm = new MenuForm(this);
    }

    public void startApp() {
        menuForm.addCommand(exitCommand);
        menuForm.setCommandListener(this);
        menuForm.createUI();
        display.setCurrent(menuForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        } else if (c == menuCommand) {
            display.setCurrent(menuForm);
        }
    }
}
