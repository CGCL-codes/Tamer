package test.idv;

import java.util.Calendar;
import java.util.TimeZone;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class FirstMIDlet extends MIDlet implements CommandListener {

    private Form form = null;

    private Command exitCmd = null;

    private String str = "Hello MIDlet";

    private String getCurrentTime() {
        StringBuffer timeStr = new StringBuffer();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Taipei"));
        timeStr.append(cal.get(Calendar.YEAR)).append("/").append(cal.get(Calendar.MONDAY) + 1).append("/").append(cal.get(Calendar.DAY_OF_MONTH)).append(" ").append(cal.get(Calendar.HOUR_OF_DAY)).append(":").append(cal.get(Calendar.MINUTE)).append(":").append(cal.get(Calendar.SECOND));
        return timeStr.toString();
    }

    public FirstMIDlet() {
        super();
        System.out.println("FirstMIDlet initial");
        form = new Form("Hello MIDlet");
        exitCmd = new Command("EXIT", Command.EXIT, 2);
        form.append(str + " : " + getCurrentTime());
        form.addCommand(exitCmd);
        form.setCommandListener(this);
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        System.out.println("FirstMIDlet destoryed");
    }

    protected void pauseApp() {
        System.out.println("FirstMIDlet paused");
    }

    protected void startApp() throws MIDletStateChangeException {
        System.out.println("FirstMIDlet started");
        Display.getDisplay(this).setCurrent(form);
    }

    public void commandAction(Command cmd, Displayable dis) {
        System.out.println(cmd.getLabel() + "," + cmd.getPriority());
        try {
            destroyApp(false);
            notifyDestroyed();
        } catch (MIDletStateChangeException e) {
            e.printStackTrace();
        }
    }
}
