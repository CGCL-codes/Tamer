package julien.esme.org;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class KroakMessage {

    /**
	 * @param args
	 * @throws XMPPException
	 */
    public static void main(String[] args) throws XMPPException {
        XMPPConnection connection = new XMPPConnection("jabber.org");
        connection.connect();
        connection.login("panoramix9", "potion");
        MessageListener listener = new MessageListenerImpl();
        Chat chat = connection.getChatManager().createChat("jackiechan777@jabber.org", listener);
        chat.sendMessage("Ca marche... ou peut etre pas !");
        connection.disconnect();
    }
}
