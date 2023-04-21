package szene.display.photos;

import java.util.Vector;
import szene.display.AbstractDisplayThread;
import szene.display.Waitscreen;
import szene.main.AbstractSzene1Client;
import szene.main.Szene1Client;
import weblife.datamodel.DataAlbumDetails;
import weblife.object.ObjectUserfoto;
import weblife.section.SectionUserfoto;
import weblife.server.RequestInterface;
import weblife.xml.ErrorcodeException;
import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.Form;

/**
 * Klasse zum Ausw�hlen eines Fotoalbums f�r Upload
 * @author Knoll
 *
 */
public class ChooseAlbum extends AbstractDisplayThread {

    private Form choice;

    private Szene1Client client;

    private RequestInterface rq;

    private Displayable dsp;

    private ChoiceGroup choicebox;

    private SectionUserfoto userfotosection;

    private ObjectUserfoto userfotoobject;

    private Vector vector_albums;

    private String currDirName;

    private String currFile;

    private Command cmdAccept = new Command("Akzeptieren", Command.OK, 1);

    public ChooseAlbum(Szene1Client client, RequestInterface rq, Displayable dsp, String currDirName, String currFile) {
        super(client);
        this.client = client;
        this.rq = rq;
        this.dsp = dsp;
        this.currDirName = currDirName;
        this.currFile = currFile;
        this.userfotosection = new SectionUserfoto(rq);
        choice = new Form("Bitte w�hlen");
        choice.addCommand(AbstractSzene1Client.backCmd);
        new Waitscreen(this.client, this.choice);
        this.start();
        choicebox = new ChoiceGroup(null, ChoiceGroup.POPUP);
        choice.append(choicebox);
        choice.addCommand(cmdAccept);
        choice.setCommandListener(this);
    }

    public void run() {
        try {
            userfotoobject = this.userfotosection.Methode_Getalbums(rq.GetUser().getUsername());
            vector_albums = userfotoobject.getVector_albums();
            for (int i = 0; i < vector_albums.size(); i++) {
                DataAlbumDetails album = (DataAlbumDetails) vector_albums.elementAt(i);
                choicebox.append(album.getName(), null);
            }
            if (!isCancel()) client.setCurrent(choice);
        } catch (ErrorcodeException e) {
            if (!isCancel()) {
                client.setCurrent(choice);
                AlertError(e.getErrormassage());
            }
        } catch (Exception e) {
            client.setCurrent(choice);
            AlertError(e.getMessage());
            System.out.println(e);
            System.out.println(e.getClass());
        }
    }

    public void commandAction(Command cmd, Displayable arg1) {
        if (cmd.equals(AbstractSzene1Client.backCmd)) {
            Cancel();
            client.setCurrent(dsp);
        } else if (cmd.equals(cmdAccept)) {
            DataAlbumDetails album = (DataAlbumDetails) vector_albums.elementAt(choicebox.getSelectedIndex());
            try {
                ObjectUserfoto userfotoobject = rq.UploadFile(currDirName, currFile, album.getId());
                if (userfotoobject.getSuccess().equals("true")) {
                    client.setCurrent(dsp);
                    AlertMessage("Upload erfolgreich");
                } else {
                    AlertError("Upload fehlgeschlagen");
                }
            } catch (ErrorcodeException e) {
                AlertError(e.getErrormassage());
                e.printStackTrace();
            } catch (Exception e) {
                AlertError(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
