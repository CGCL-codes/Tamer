package evs.activation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.SocketException;

public class Marshaller extends evs.brp.Marshaller {

    public static void marshall(ObjectOutput objectOutput, Message message) {
        log.info("marshalling: ");
        log.info(message.toString());
        try {
            objectOutput.writeObject(message);
        } catch (SocketException se) {
            log.error(se);
            se.printStackTrace();
        } catch (IOException ioe) {
            log.error(ioe);
            ioe.printStackTrace();
        }
        log.info("marshalling complete");
    }

    public static Message demarshall(ObjectInput objectInput) {
        log.info("demarshalling:");
        Message message = new Message();
        try {
            message = (Message) objectInput.readObject();
            log.info(message.toString());
        } catch (IOException ioe) {
            log.error(ioe);
        } catch (ClassNotFoundException ioe) {
            log.error(ioe);
        }
        log.info("demarshalling complete");
        return message;
    }
}
