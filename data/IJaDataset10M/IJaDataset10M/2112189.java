package szene.display.photos;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import szene.Filebrowser.FileBrowser;
import szene.display.AbstractDisplay;
import szene.display.AbstractDisplayThread;
import szene.display.Waitscreen;
import szene.main.AbstractSzene1Client;
import szene.main.Szene1Client;
import weblife.datamodel.DataAlbumDetails;
import weblife.object.ObjectUserfoto;
import weblife.section.SectionUserfoto;
import weblife.server.RequestInterface;
import weblife.xml.ErrorcodeException;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.Form;
import de.enough.polish.ui.IconItem;
import de.enough.polish.ui.TextField;

/**
 * Klasse repr�sentiert ein Photoalbum
 * @author Knoll
 *
 */
public class Photoalbum extends AbstractDisplayThread {

    private Szene1Client client;

    private RequestInterface rq;

    private Form Userphotos;

    private String username;

    private Displayable display;

    private Vector vector_albums = new Vector();

    private SectionUserfoto userfotosection;

    private ObjectUserfoto userfotoobject;

    private Command cmdOpen = new Command("�ffnen", Command.OK, 1);

    private Command cmdAccept = new Command("Best�tigen", Command.OK, 1);

    private Command cmdUpload = new Command("Upload", Command.OK, 1);

    private Command cmdDetails = new Command("Details", Command.OK, 2);

    /**
	 * 
	 * @param client Instanz des Client
	 * @param rq Requestinterface
	 * @param username Username des Fotoalbums
	 * @param display Zuletzt angezeigte Oberfl�che
	 */
    public Photoalbum(Szene1Client client, RequestInterface rq, String username, Displayable display) {
        super(client);
        this.client = client;
        this.rq = rq;
        this.userfotosection = new SectionUserfoto(rq);
        this.username = username;
        this.display = display;
        Userphotos = new Form("Userphotos");
        Userphotos.addCommand(AbstractSzene1Client.backCmd);
        Userphotos.setCommandListener(this);
        new Waitscreen(this.client, this.Userphotos);
        this.start();
    }

    public void run() {
        try {
            userfotoobject = this.userfotosection.Methode_Getalbums(username);
            Vector vector_albums = userfotoobject.getVector_albums();
            for (int i = 0; i < vector_albums.size(); i++) {
                DataAlbumDetails album = (DataAlbumDetails) vector_albums.elementAt(i);
                IconItem AlbumItem = null;
                if (album.getPermission().equals("0")) {
                    AlbumItem = new IconItem(null, album.getName(), null);
                    this.vector_albums.addElement(album);
                    try {
                        Image img;
                        img = Image.createImage(AbstractSzene1Client.PHOTO_PUBLIC);
                        AlbumItem.setImage(img);
                    } catch (IOException e) {
                    }
                } else if (album.getPermission().equals("1")) {
                    AlbumItem = new IconItem(null, album.getName() + " ", null);
                    this.vector_albums.addElement(album);
                    try {
                        Image img;
                        img = Image.createImage(AbstractSzene1Client.PHOTO_FRIENDS);
                        AlbumItem.setImage(img);
                    } catch (Exception e) {
                    }
                } else if ((album.getPermission().equals("2")) && (album.getOwnerid().equals(rq.GetUser().getUserid()))) {
                    AlbumItem = new IconItem(null, album.getName() + " ", null);
                    this.vector_albums.addElement(album);
                    try {
                        Image img;
                        img = Image.createImage(AbstractSzene1Client.PHOTO_HIDDEN);
                        AlbumItem.setImage(img);
                    } catch (Exception e) {
                    }
                } else if (album.getPermission().equals("3")) {
                    AlbumItem = new IconItem(null, album.getName() + " ", null);
                    Image img;
                    this.vector_albums.addElement(album);
                    try {
                        img = Image.createImage(AbstractSzene1Client.PHOTO_PASSWORD);
                        AlbumItem.setImage(img);
                    } catch (IOException e) {
                    }
                } else if (album.getPermission().equals("5")) {
                    AlbumItem = new IconItem(null, album.getName(), null);
                    this.vector_albums.addElement(album);
                    try {
                        Image img;
                        img = Image.createImage(AbstractSzene1Client.PHOTO_PUBLIC);
                        AlbumItem.setImage(img);
                    } catch (IOException e) {
                    }
                } else {
                }
                if (AlbumItem != null) {
                    AlbumItem.setDefaultCommand(this.cmdOpen);
                    AlbumItem.addCommand(this.cmdDetails);
                    AlbumItem.addCommand(this.cmdUpload);
                    this.Userphotos.append(AlbumItem);
                }
            }
            if (!isCancel()) client.setCurrent(Userphotos);
        } catch (ErrorcodeException e) {
            if (!isCancel()) {
                client.setCurrent(Userphotos);
                this.AlertError(e.getErrormassage());
            }
        }
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd.equals(AbstractSzene1Client.backCmd)) {
            Cancel();
            this.client.setCurrent(display);
        } else if (cmd.equals(this.cmdOpen)) {
            DataAlbumDetails album = (DataAlbumDetails) this.vector_albums.elementAt(this.Userphotos.getCurrentIndex());
            if ((album.getPermission().equals("3")) && (!album.getOwnerid().equals(rq.GetUser().getUserid()))) {
                new EnterPassword(client, rq, album, this.Userphotos);
            } else {
                new AlbumDetails(client, rq, album, this.Userphotos);
            }
        } else if (cmd.equals(this.cmdDetails)) {
            DataAlbumDetails album = ((DataAlbumDetails) this.vector_albums.elementAt(this.Userphotos.getCurrentIndex()));
            AlertMessage(album.getDescription());
        } else if (cmd.equals(this.cmdUpload)) {
            DataAlbumDetails album = (DataAlbumDetails) this.vector_albums.elementAt(this.Userphotos.getCurrentIndex());
            new FileBrowser(client, rq, album.getId(), this.Userphotos).start();
        }
    }

    /**
	 * Passworteingabe f�r gesch�tztes Photoalbum
	 * @author Knoll
	 *
	 */
    class EnterPassword extends AbstractDisplay {

        private Form password;

        private Szene1Client client;

        private RequestInterface rq;

        private Displayable display;

        private DataAlbumDetails album;

        private TextField T_password;

        EnterPassword(Szene1Client client, RequestInterface rq, DataAlbumDetails album, Displayable display) {
            super(client);
            this.album = album;
            this.client = client;
            this.rq = rq;
            this.display = display;
            password = new Form("Bitte Passwort eingeben");
            password.addCommand(AbstractSzene1Client.backCmd);
            password.addCommand(cmdAccept);
            password.setCommandListener(this);
            T_password = new TextField("Password: ", "", 24, TextField.PASSWORD);
            password.append(T_password);
            client.setCurrent(password);
        }

        public void commandAction(Command cmd, Displayable disp) {
            if (cmd.equals(AbstractSzene1Client.backCmd)) {
                Cancel();
                this.client.setCurrent(display);
            } else if (cmd.equals(cmdAccept)) {
                new AlbumDetails(client, rq, album, Userphotos, T_password.getString());
            }
        }
    }
}
