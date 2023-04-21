package capitulo6.gcf.net.time;

import javax.microedition.midlet.*;
import javax.microedition.io.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class TimeMIDlet extends MIDlet implements CommandListener {

    private static Display display;

    private Form f;

    private boolean isPaused;

    private StringItem si;

    private TimeClient client;

    private Command exitCommand = new Command("Exit", Command.EXIT, 1);

    private Command startCommand = new Command("GetTime", Command.ITEM, 1);

    public TimeMIDlet() {
        display = Display.getDisplay(this);
        f = new Form("Time Demo");
        si = new StringItem("Select GetTime to get the current Time! ", " ");
        f.append(si);
        f.addCommand(exitCommand);
        f.addCommand(startCommand);
        f.setCommandListener(this);
        display.setCurrent(f);
    }

    public void startApp() {
        isPaused = false;
    }

    public void pauseApp() {
        isPaused = true;
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(true);
            notifyDestroyed();
        } else if (c == startCommand) {
            client = new TimeClient(this);
            client.start();
        }
    }
}
