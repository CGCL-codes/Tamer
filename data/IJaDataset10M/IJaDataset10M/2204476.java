package szene.display.postbox;

import java.io.IOException;
import javax.microedition.lcdui.Image;
import szene.display.AbstractDisplay;
import szene.main.AbstractSzene1Client;
import szene.main.Szene1Client;
import weblife.object.ObjectPn;
import weblife.section.SectionPn;
import weblife.server.RequestInterface;
import weblife.xml.ErrorcodeException;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.Form;
import de.enough.polish.ui.ImageItem;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.StringItem;
import de.enough.polish.ui.TextField;

/**
 * Display f�r erstellen einer Neuen Nachricht, bzw um einer PN zu antworten
 * @author Knoll
 *
 */
public class NewMessage extends AbstractDisplay {

    private RequestInterface rq;

    private Szene1Client client;

    private Form newmessage;

    private TextField receiver;

    private TextField message;

    private Displayable display;

    private final Command cmdSend = new Command("Senden", Command.ITEM, 1);

    public NewMessage(Szene1Client client, RequestInterface rq, Displayable display) {
        super(client);
        this.rq = rq;
        this.display = display;
        this.client = client;
        newmessage = new Form("Neue PN");
        this.receiver = new TextField("an: ", "", 24, TextField.UNEDITABLE);
        this.message = new TextField(null, "", 512, TextField.ANY);
        this.newmessage.append(receiver);
        this.newmessage.append(message);
        this.newmessage.addCommand(AbstractSzene1Client.backCmd);
        this.newmessage.addCommand(this.cmdSend);
        this.newmessage.setCommandListener(this);
        try {
            Image updateimg = Image.createImage(AbstractSzene1Client.STATUS_MESSAGE);
            ImageItem update = new ImageItem(null, updateimg, Item.LAYOUT_CENTER, "Statusupdate", ImageItem.BUTTON);
            newmessage.append(update);
            update.addCommand(this.cmdSend);
            update.setDefaultCommand(this.cmdSend);
        } catch (IOException e) {
            StringItem update = new StringItem("", "Statusupdate", StringItem.BUTTON);
            update.addCommand(this.cmdSend);
            newmessage.append(update);
        }
        client.setCurrent(newmessage);
    }

    public void SetReceiver(String Username) {
        this.receiver.setString(Username);
    }

    public void MessageSuccesfullSended() {
        this.message.setString("");
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd.equals(AbstractSzene1Client.backCmd)) {
            this.client.setCurrent(display);
        } else if (cmd.equals(this.cmdSend)) {
            this.newmessage.removeCommand(cmdSend);
            new Pnsender(this.rq, this, this.receiver.getString(), this.message.getString());
        }
    }

    class Pnsender extends Thread implements Runnable {

        private String username;

        private String message;

        private SectionPn pnsection;

        private NewMessage instance;

        Pnsender(RequestInterface rq, NewMessage instance, String username, String message) {
            super();
            this.message = message;
            this.username = username;
            this.instance = instance;
            pnsection = new SectionPn(rq);
            this.start();
        }

        public void run() {
            if (this.message == null | message.length() < 1) {
                AlertMessage("Bitte alle Felder ausf�llen");
            } else {
                try {
                    System.out.println("Zu sendende Nachricht:" + message);
                    ObjectPn pnobject = this.pnsection.Methode_SendPn(username, message);
                    if (!pnobject.Error() && pnobject.getSuccess().equals("true")) {
                        this.instance.AlertMessage("PN gesendet");
                        this.instance.MessageSuccesfullSended();
                    } else {
                        this.instance.AlertError(pnobject.getErrormessage());
                    }
                } catch (ErrorcodeException e) {
                    instance.AlertError(e.getErrormassage());
                }
            }
            newmessage.addCommand(cmdSend);
        }
    }
}
