package friendsUI;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.*;
import com.alsutton.xmlparser.objectmodel.Node;
import main.MainCore;
import ports.ServerHandler;
import preference.FriendsPreference;
import preference.Preference;
import alertUI.MessageAlert;
import java.io.*;
import java.util.Vector;

/**
 * This is the friendslist class which handles all the friends
 * list related matters. 
 * 
 * It is heavily used for displaying only. **
 * @author Raymond Lo, James Fung
 *
 */
public class FriendsForm extends Form implements CommandListener {

    private Command settings, loadPic, back, send;

    private TextField message, friendField;

    private FriendsPreference pref;

    private int imageRef = -1;

    private int msgRef = -1;

    private int friendRef = -1;

    private boolean showingSend;

    private Display display;

    private Displayable parent;

    private boolean changed;

    private Form myForm;

    private MainCore m;

    private String serverName;

    private ServerHandler sh;

    /**
	 * Constructor
	 * @param label the name of the form
	 * @param display the current display of it.
	 * @param parent the form or cavaus who call this. 
	 */
    public FriendsForm(String label, Display display, Displayable parent, MainCore m) {
        super(label);
        myForm = this;
        this.m = m;
        loadPic = new Command("Refresh", Command.CANCEL, 1);
        back = new Command("Back", Command.CANCEL, 2);
        settings = new Command("Settings", Command.OK, 99);
        send = new Command("Send", Command.CANCEL, 1);
        sh = new ServerHandler();
        pref = new FriendsPreference(display, this);
        message = new TextField("Message", "", 249, TextField.ANY);
        friendField = new TextField("User Id", "", 50, TextField.ANY);
        loadPref();
        changed = false;
        showingSend = false;
        new Thread(new Runnable() {

            public void run() {
                Node root = sh.getFriends();
                root.printTree();
                Vector friendsList = root.getChildrenByName("friend");
                for (int i = 0; i < friendsList.size(); i++) {
                    Node friend = (Node) friendsList.elementAt(i);
                    System.out.println(friend.getText());
                }
            }
        }).start();
        this.addCommand(loadPic);
        this.addCommand(back);
        this.addCommand(settings);
        friendRef = this.append(friendField);
        msgRef = this.append(message);
        this.display = display;
        this.parent = parent;
        this.setCommandListener(this);
    }

    /**
	 * Load the Preference (Settings)
	 */
    private void loadPref() {
        this.serverName = pref.getServerOption();
        friendField.setString(pref.getFriendOption());
    }

    /**
	 * Display the form.
	 */
    public void startForm() {
        display.setCurrent(this);
        if (changed) {
            loadPref();
        }
        if (imageRef == -1) {
            new Thread(new Runnable() {

                public void run() {
                    try {
                        downloadImage(DownloadURL(serverName, friendField.getString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
	 *`The command action. 
	 * @param c the command button pressed
	 * @param arg1 i've no clue... :( 
	 */
    public void commandAction(Command c, Displayable arg1) {
        if (c == loadPic) {
            if (changed) {
                loadPref();
            }
            new Thread(new Runnable() {

                public void run() {
                    try {
                        downloadImage(DownloadURL(serverName, friendField.getString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            new Thread(new Runnable() {

                public void run() {
                    getMessage();
                }
            }).start();
        }
        if (c == back) {
            if (m != null) m.loadApplication(MainCore.MAINMENU); else display.setCurrent(parent);
        } else if (c == settings) {
            changed = true;
            pref.startPrefScreen();
        } else if (c == send) {
            if (changed) {
                loadPref();
            }
            new Thread(new Runnable() {

                public void run() {
                    try {
                        sendMessage("http://www.glogger.mobi/cgi-bin/sendMessage.pl");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**Return the URL for downloading image
	 * @param sName the server name
	 * @param fName the user name.
	 * @return
	 */
    public String DownloadURL(String sName, String fName) {
        return "http://" + sName + "/users/" + fName + "/image/friends.jpg";
    }

    /**
	 * Download image from the server. We should use the Friends List
	 * Preferences to know who's images to download from.
	 * @param url
	 * @throws IOException
	 */
    void downloadImage(String url) throws IOException {
        HttpConnection c = null;
        InputStream is = null;
        setTitle("Downloading...");
        try {
            c = (HttpConnection) Connector.open(url);
            c.setRequestProperty("User-Agent", "Mozilla/4.0");
            int rc = c.getResponseCode();
            if (rc == HttpConnection.HTTP_OK) {
                byte[] data;
                int len = (int) c.getLength();
                setTitle("Downloading - " + len);
                is = c.openInputStream();
                if (len != -1) {
                    int total = 0;
                    data = new byte[len];
                    while (total < len) {
                        total += is.read(data, total, len - total);
                    }
                } else {
                    ByteArrayOutputStream tmp = new ByteArrayOutputStream();
                    int ch;
                    while ((ch = is.read()) != -1) {
                        tmp.write(ch);
                    }
                    data = tmp.toByteArray();
                }
                is.close();
                if (msgRef != -1) {
                    this.delete(msgRef);
                }
                if (friendRef != -1) {
                    this.delete(friendRef);
                }
                if (imageRef != -1) {
                    this.delete(imageRef);
                }
                Image friendsImage = Image.createImage(data, 0, data.length);
                imageRef = this.append(friendsImage);
                friendRef = this.append(friendField);
                msgRef = this.append(message);
                setTitle(friendField.getString() + "'s Latest Image");
            } else {
                setTitle("Failed :(");
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (is != null) {
                is.close();
            }
            if (c != null) {
                c.close();
            }
        }
    }

    /**
	 * This method will upload the image to the Web
	 */
    void sendMessage(String url) throws IOException {
        HttpConnection c = null;
        InputStream is = null;
        OutputStream dstream = null;
        setTitle("Sending Messages...");
        try {
            c = (HttpConnection) Connector.open(url);
            c.setRequestMethod(HttpConnection.POST);
            c.setRequestProperty("Connection", "Keep-Alive");
            c.setRequestProperty("Content-Type", "multipart/form-data; boundary=****4353");
            dstream = new DataOutputStream(c.openOutputStream());
            dstream.write("--****4353\r\n".getBytes());
            dstream.write(("Content-Disposition: form-data; name=\"username\" " + "\r\n\r\n" + friendField.getString() + "\r\n" + "--****4353\r\n").getBytes());
            Preference prefMain = new Preference(display, this);
            String uName = prefMain.getUserOption();
            dstream.write(("Content-Disposition: form-data; name=\"from\" " + "\r\n\r\n" + uName + "\r\n" + "--****4353\r\n").getBytes());
            prefMain = null;
            dstream.write(("Content-Disposition: form-data; name=\"message\" " + "\r\n\r\n" + message.getString() + "\r\n" + "--****4353\r\n").getBytes());
            dstream.flush();
            dstream.close();
            message.setString("");
            setTitle("Message Sent");
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Not an HTTP URL");
        } finally {
            if (is != null) is.close();
            if (dstream != null) dstream.close();
            if (c != null) c.close();
        }
    }

    public void getMessage() {
        setTitle("Checking Msg...");
        String myMsg = sh.getInbox();
        if (myMsg.length() > 2) {
            MessageAlert.showMessage(display, "New Message", myMsg);
        }
        setTitle("Message Checked");
        append(myMsg);
    }
}
