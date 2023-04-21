package net.sf.microinstall;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.MessagePart;
import javax.wireless.messaging.MultipartMessage;

/**
 * 
 * 
 * @author Johan Karlsson (johan.karlsson@jayway.se)
 */
public class MMSInstaller {

    public void install(final String phoneNo, final String mmsText, final String fileURL) throws IllegalArgumentException {
        System.out.println("MMSInstaller.install() ");
        if (phoneNo == null || fileURL == null) {
            throw new IllegalArgumentException("The telephoneNo nor the fileURL must be null.");
        }
        System.out.println("Creating & starting the sending thread.");
        new Thread(new Runnable() {

            public void run() {
                sendMMS(phoneNo, mmsText, fileURL);
            }
        }).start();
    }

    private void sendMMS(String phoneNo, String mmsText, String fileURL) {
        System.out.println("Send MMS");
        try {
            MessageConnection messageConnection = (MessageConnection) Connector.open("mms://" + phoneNo);
            MultipartMessage multipartMessage = (MultipartMessage) messageConnection.newMessage(MessageConnection.MULTIPART_MESSAGE);
            multipartMessage.setSubject("Microinstall");
            InputStream inputStream = this.getClass().getResourceAsStream(fileURL);
            if (inputStream != null) {
                System.out.println("InputStream is " + inputStream);
                MessagePart jarPart = new MessagePart(inputStream, "application/java-archive", "installer-jar", null, null);
                multipartMessage.addMessagePart(jarPart);
            } else {
                System.err.println("The " + fileURL + " was not found.");
            }
            messageConnection.send(multipartMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
