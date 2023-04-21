package ch.ethz.dcg.spamato.filter.earlgrey.common;

import java.io.*;
import java.security.PrivateKey;
import ch.ethz.dcg.spamato.base.common.util.msg.Message;
import ch.ethz.dcg.spamato.filter.earlgrey.common.msg.*;
import ch.ethz.dcg.spamato.filter.earlgrey.common.security.RSA;
import ch.ethz.dcg.spamato.filter.earlgrey.common.xml.*;

/**
 * @author simon
 */
public class SecurityUtil {

    private static final EarlgreyFilterXMLOutputStream xmlOut = new EarlgreyFilterXMLOutputStream();

    private static final EarlgreyFilterXMLInputStream xmlIn = new EarlgreyFilterXMLInputStream();

    public static SignedMessage signMessage(Message msg, PrivateKey privKey) throws SigningException {
        SignedMessage signedMessage = new SignedMessage();
        signedMessage.setMessage(msg);
        signedMessage.createSignature(privKey);
        return signedMessage;
    }

    public static String encryptMessage(Message msg, String rsaPublicKeyOfReceiver) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        synchronized (xmlOut) {
            xmlOut.setOutputStream(byteBuffer);
            try {
                xmlOut.writeObject(msg);
                xmlOut.closeUnderlyingStream();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        String message = null;
        try {
            message = byteBuffer.toString("ISO-8859-1");
        } catch (UnsupportedEncodingException use) {
            use.printStackTrace();
        }
        String encryptedMessage = RSA.encode(message, rsaPublicKeyOfReceiver);
        return encryptedMessage;
    }

    public static Message decryptMessage(String encryptedMessage, String rsaPrivateKey, String rsaPublicKey) {
        String decryptedMessage = RSA.decode(encryptedMessage, rsaPrivateKey, rsaPublicKey);
        ByteArrayInputStream byteIn = null;
        try {
            byteIn = new ByteArrayInputStream(decryptedMessage.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException use) {
            use.printStackTrace();
        }
        Message message;
        synchronized (xmlIn) {
            xmlIn.setInputStream(byteIn);
            try {
                message = (Message) xmlIn.readObject();
            } catch (IOException ioe) {
                ioe.printStackTrace();
                message = null;
            }
        }
        return message;
    }
}
