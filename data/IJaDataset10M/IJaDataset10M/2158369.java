package nativ;

import java.util.TimerTask;
import nativ.NativeException;
import net.*;
import java.util.Vector;
import org.w3c.dom.*;

/**
 * A timeout magvalosit�st segito osztaly, k�ld�shez
 * 
 * @author Tajti Barnab�s
 * 
 */
public class RawSendTask extends TimerTask {

    private RawSocket own;

    private Xml2Data xml2d;

    Vector<Xml2Data> messages;

    Document doc;

    public RawSendTask(RawSocket own, Xml2Data xml2d, Vector<Xml2Data> messages, Document doc) {
        super();
        this.own = own;
        this.xml2d = xml2d;
        this.doc = doc;
        this.messages = messages;
    }

    public void run() {
        try {
            own.send(xml2d.getData(), xml2d.getDestAddr());
            if (xml2d.isLoopback()) {
                try {
                    xml2d.linkel(messages);
                    xml2d.chk();
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            }
        } catch (NativeException e) {
            e.printStackTrace();
        }
    }
}
