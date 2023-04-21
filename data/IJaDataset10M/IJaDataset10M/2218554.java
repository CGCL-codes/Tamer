package main;

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/**
 * This form will suicide once it's shown it's message ;)
 * @author Raymond
 *
 */
public class SuicideForm extends Form implements CommandListener {

    private Glog m = null;

    private Command exit = new Command("Exit", Command.CANCEL, 1);

    public SuicideForm(Glog m) {
        super("Error Message");
        this.m = m;
        this.addCommand(exit);
        this.setCommandListener(this);
    }

    public void addMessage(String msg) {
        this.append(msg);
    }

    public void commandAction(Command c, Displayable arg1) {
        if (c == exit) {
            this.m.exitMIDlet();
        }
    }
}
