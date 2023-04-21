package main;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class clientMain {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        XMPPConnection.DEBUG_ENABLED = true;
        XMPPConnection connection = new XMPPConnection("141.30.203.90");
        try {
            connection.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        try {
            connection.login("beta", "Pslqt");
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
