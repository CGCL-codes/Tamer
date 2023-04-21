package capitulo6.gcf.net;

import java.io.InputStream;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Spacer;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class WebConnectionMIDlet extends MIDlet implements CommandListener {

    public static Display display;

    Form form;

    TextField field;

    StringItem item;

    Command cmdGo;

    public WebConnectionMIDlet() {
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }

    protected void pauseApp() {
    }

    protected void startApp() throws MIDletStateChangeException {
        form = new Form("Web Connection MIDlet");
        field = new TextField("URL", "http://www.yahoo.com", 50, TextField.ANY);
        item = new StringItem("HTML", "");
        cmdGo = new Command("Visit", Command.OK, 1);
        form.append(field);
        form.append(new Spacer(10, 1));
        form.append(item);
        form.addCommand(cmdGo);
        form.setCommandListener(this);
        display = Display.getDisplay(this);
        display.setCurrent(form);
    }

    public void commandAction(Command arg0, Displayable arg1) {
        if (arg0.equals(cmdGo)) {
            try {
                new Thread() {

                    public void run() {
                        try {
                            System.out.println("thread started . . .");
                            HttpConnection con = (HttpConnection) Connector.open(field.getString());
                            System.out.println(field.getString());
                            int status = con.getResponseCode();
                            StringBuffer sb = new StringBuffer();
                            if (status == HttpConnection.HTTP_OK) {
                                InputStream is = con.openInputStream();
                                int c = is.read();
                                while (c != -1) {
                                    System.out.print((char) (byte) c);
                                    sb.append((char) c);
                                    c = is.read();
                                }
                                System.out.println(sb.length());
                                item.setText(sb.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
