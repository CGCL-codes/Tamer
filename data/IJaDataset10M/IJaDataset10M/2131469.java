package EmailReader.EmailIO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.event.MessageCountListener;

/**
 *
 * @author sahaqiel
 */
public class GmailGetter implements Runnable, Getable {

    private Getable.STATE curState;

    private String gmailServer;

    private String gmailUserName;

    private String gmailDomain;

    private String gmailPass;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private MessageTableModel model;

    private Folder folder;

    public GmailGetter(MessageTableModel model) {
        this.model = model;
        curState = Getable.STATE.Stopped;
        init();
    }

    private void init() {
        Properties props = new Properties();
        FileInputStream fileIn = null;
        try {
            File lf = new File(System.getProperty("user.home") + "/.messageReader");
            if (lf.exists() == false) {
                lf.createNewFile();
                out("created " + lf.getPath());
            }
            fileIn = new FileInputStream(lf);
            props.load(fileIn);
            fileIn.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        gmailServer = "imap.gmail.com";
        gmailUserName = "";
        gmailDomain = "gmail.com";
        gmailPass = "";
        if (props.containsKey("gmailServer")) {
            gmailServer = props.getProperty("gmailServer");
        }
        if (props.containsKey("gmailUserName")) {
            gmailUserName = props.getProperty("gmailUserName");
        }
        if (props.containsKey("gmailDomain")) {
            gmailDomain = props.getProperty("gmailDomain");
        }
        if (props.containsKey("gmailPass")) {
            gmailPass = props.getProperty("gmailPass");
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            pcs.addPropertyChangeListener(listener);
        }
    }

    @Override
    public Getable.STATE getEmailState() {
        return curState;
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (listener != null) {
            pcs.removePropertyChangeListener(listener);
        }
    }

    protected void addMessageCountListener(MessageCountListener listener) {
        if (folder != null) {
            folder.addMessageCountListener(listener);
        }
    }

    protected void removeMessageCountListener(MessageCountListener listener) {
        if (folder != null) {
            folder.removeMessageCountListener(listener);
        }
    }

    @Override
    public void run() {
        setEmailState(Getable.STATE.Started);
        out("Setting Properties");
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.imap.socketFactory.fallback", "false");
        if (gmailUserName.equals("")) {
            SAuthenticator auth = new SAuthenticator();
            gmailUserName = auth.getPasswordAuthentication().getUserName();
            out("gmailUserName " + gmailUserName);
            gmailPass = auth.getPasswordAuthentication().getPassword();
            out("gmailPass " + gmailPass);
            auth = null;
        }
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        out("Creating Session");
        try {
            Store store = session.getStore("imaps");
            setEmailState(Getable.STATE.Connecting);
            store.connect("imap.gmail.com", "andrew.waidler@gmail.com", gmailPass);
            out("Connecting to Server");
            URLName server = new URLName("imaps://:@imap.gmail.com/INBOX");
            folder = store.getFolder(server);
            folder.open(Folder.READ_ONLY);
            if (folder.exists() && folder.isOpen()) {
                out("Opening " + folder.getName() + " found " + folder.getMessageCount() + " messages");
                setEmailState(Getable.STATE.Downloading);
                int count = folder.getMessageCount();
                for (int i = 1; i < count; i++) {
                    MessageMT mt = new MessageMT(folder.getMessage(i), i - 1);
                    model.addMessage(mt);
                }
                out("Email Fetch Completed");
                setEmailState(Getable.STATE.Completed);
            } else {
                out("Could not open folder");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setEmailState(Getable.STATE newState) {
        switch(curState) {
            case Completed:
            case Connecting:
            case Downloading:
            case Stalled:
            case Started:
            case Stopped:
                curState = newState;
                pcs.firePropertyChange(curState.toString(), 0, 1);
                out("" + curState.toString());
                break;
        }
    }

    private void out(String msg) {
        System.out.println(getClass().getName() + " " + msg);
    }
}
