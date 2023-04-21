package flexserverlib.services;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Properties;

/**
 * @author Andrew Powell - Universal Mind - andrew.powell@universalmind.com
 */
public class POP3Service implements IPOP3Service {

    /**
     * The URL of the POP3 server to connect to. This is set in the constructor
     * to the value: "pop3://"+username+":"+password+"@"+hostname;
     */
    protected String url = "";

    /**
     * The hostname of the POP3 server to connect to.
     */
    protected String hostname = "";

    /**
     * The username to connect to the POP3 server with.
     */
    protected String username = "";

    /**
     * The password to connect to the POP3 server with.
     */
    protected String password = "";

    /**
     * The message store.
     */
    protected Store store = null;

    /**
     * The Folder representing the users inbox.
     */
    protected Folder inboxFolder = null;

    /**
     * Constructor.
     *
     * @param p_hostname the hostname of the pop3 server to connect to
     * @param p_username the username to login to the pop3 account as
     * @param p_password the password to use when logging into the pop3 account
     * @throws Exception If an error occurs.
     */
    public POP3Service(String p_hostname, String p_username, String p_password) throws Exception {
        hostname = p_hostname;
        username = p_username;
        password = p_password;
        url = "pop3s://" + username + ":" + password + "@" + hostname;
    }

    /**
     * Connect to the pop3 server.
     *
     * @throws Exception If an error occurs.
     */
    public void connect() throws Exception {
        boolean debug = false;
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties pop3Props = new Properties();
        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port", "995");
        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
        pop3Props.setProperty("mail.store.protocol", "pop3s");
        pop3Props.setProperty("mail.pop3.host", hostname);
        try {
            Session session = Session.getDefaultInstance(pop3Props);
            store = session.getStore();
            System.out.println("your ID is : " + username);
            System.out.println("Connecting...");
            store.connect(hostname, username, password);
            System.out.println("Connected...");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            inboxFolder = store.getDefaultFolder();
            if (inboxFolder == null) {
                throw new Exception("No default inbox folder found.");
            }
            inboxFolder = inboxFolder.getFolder("INBOX");
            if (inboxFolder == null) {
                throw new Exception("INBOX folder does not exist.");
            }
            inboxFolder.open(Folder.READ_WRITE);
        } catch (Exception e) {
            throw new Exception("Unable to open folder. " + e.toString());
        }
    }

    /**
     * Disconnect from the pop3 server.
     *
     * @throws Exception If an error occurs.
     */
    public void disconnect() throws Exception {
        try {
            inboxFolder.close(true);
        } catch (Exception e) {
            throw new Exception("Unable to close the POP3 Folder. " + e.toString());
        }
        try {
            store.close();
        } catch (Exception e) {
            throw new Exception("Unable to disconnect from POP3 server. " + e.toString());
        }
    }

    /**
     * Returns the number of messages in the users inbox.
     *
     * @return int The number of messages in the users inbox.
     * @throws Exception If an error occurs.
     */
    public int getMessageCount() throws Exception {
        return inboxFolder.getMessageCount();
    }

    /**
     * Returns the number of unread messages in the users folder.
     *
     * @return int The number of unread messages in the users folder.
     * @throws Exception If an error occurs.
     */
    public int getUnreadMessageCount() throws Exception {
        return inboxFolder.getUnreadMessageCount();
    }

    /**
     * Returns the number of new messages in the users folder.
     *
     * @return int The number of new messages in the users folder.
     * @throws Exception If an error occurs.
     */
    public int getNewMessageCount() throws Exception {
        return inboxFolder.getNewMessageCount();
    }

    /**
     * Get the Message object corresponding to the given message number.
     *
     * @param msgnum The index/message number of the message to get.
     * @return Message The message at the specified index.
     * @throws Exception If an error occurs.
     */
    public Message getMessage(int msgnum) throws Exception {
        return inboxFolder.getMessage(msgnum);
    }

    /**
     * Get all Message objects from this Folder.
     *
     * @return Message[] The array of messages.
     * @throws Exception If an error occurs.
     */
    public Message[] getMessages() throws Exception {
        return inboxFolder.getMessages();
    }

    /**
     * Get the Message objects for message numbers specified in the array.
     *
     * @param msgnums An integer array containing the index/message
     *                number of the messages to get.
     * @return Message[] The array of messages.
     * @throws Exception If an error occurs.
     */
    public Message[] getMessages(int[] msgnums) throws Exception {
        return inboxFolder.getMessages(msgnums);
    }

    /**
     * Get the Message objects for message numbers ranging from start through
     * end, both start and end index inclusive.
     *
     * @param start The index/message number to start getting messages at.
     * @param end   The index/message number to stop getting messages at.
     * @return Message[] The array of messages.
     * @throws Exception If an error occurs.
     */
    public Message[] getMessages(int start, int end) throws Exception {
        return inboxFolder.getMessages(start, end);
    }
}
