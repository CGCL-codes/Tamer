package szene.display.friendslist;

import java.util.Vector;
import szene.display.AbstractDisplayThread;
import szene.display.Waitscreen;
import szene.main.AbstractSzene1Client;
import szene.main.Szene1Client;
import weblife.object.ObjectFriendslist;
import weblife.section.SectionFriendslist;
import weblife.section.SectionRequests;
import weblife.server.RequestInterface;
import weblife.xml.ErrorcodeException;
import de.enough.polish.ui.ChoiceGroup;
import de.enough.polish.ui.Command;
import de.enough.polish.ui.Displayable;
import de.enough.polish.ui.Form;

/**
 * Klasse zum Ausw�hlen einer Freundesgruppe
 * M�gliche Optionen:
 *  + User als Freund hinzuf�gen
 *  + Freundschaftsanfrage annehmen
 * @author Knoll
 *
 */
public class ChooseFriendgroup extends AbstractDisplayThread {

    private Form choice;

    private Szene1Client client;

    private RequestInterface rq;

    private Displayable dsp;

    private ChoiceGroup choicebox;

    private String userid;

    private Vector vgroupid;

    private SectionRequests requestsection;

    private SectionFriendslist friendslistsection;

    public static int CHOOSE_ADD = 1;

    public static int CHOOSE_REQUEST = 2;

    private Command cmdAccept = new Command("Akzeptieren", Command.OK, 1);

    private Command cmdAdd = new Command("Hinzuf�gen", Command.OK, 1);

    public ChooseFriendgroup(Szene1Client client, RequestInterface rq, String userid, Displayable dsp, int choose) {
        super(client);
        this.userid = userid;
        this.client = client;
        this.rq = rq;
        this.dsp = dsp;
        if (choose == CHOOSE_REQUEST) this.requestsection = new SectionRequests(rq); else if (choose == CHOOSE_ADD) this.friendslistsection = new SectionFriendslist(rq);
        choice = new Form("Bitte w�hlen");
        choice.addCommand(AbstractSzene1Client.backCmd);
        new Waitscreen(this.client, this.choice);
        this.start();
        choicebox = new ChoiceGroup(null, ChoiceGroup.POPUP);
        choice.append(choicebox);
        if (choose == CHOOSE_REQUEST) choice.addCommand(cmdAccept); else if (choose == CHOOSE_ADD) choice.addCommand(cmdAdd);
        choice.setCommandListener(this);
    }

    public void run() {
        try {
            SectionFriendslist friendlistsection = new SectionFriendslist(rq);
            ObjectFriendslist friendslistobject = friendlistsection.Methode_Getfriendsgroup(rq.GetUser().getUsername());
            Vector vgroupname = friendslistobject.getVector_friendgroupname();
            vgroupid = friendslistobject.getVector_friendgroupsid();
            for (int i = 0; i < vgroupname.size(); i++) {
                choicebox.append((String) vgroupname.elementAt(i), null);
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
            try {
                requestsection.Methode_Accept(userid, vgroupid.elementAt(choicebox.getSelectedIndex()).toString());
                AlertMessage("Freundschaftsanfrage akzeptiert");
            } catch (ErrorcodeException e) {
                AlertError(e.getErrormassage());
                e.printStackTrace();
            }
        } else if (cmd.equals(cmdAdd)) {
            try {
                friendslistsection.Methode_Addfriend(userid, vgroupid.elementAt(choicebox.getSelectedIndex()).toString());
                AlertMessage("Freundschaftsanfrage gesendet");
            } catch (ErrorcodeException e) {
                AlertError(e.getErrormassage());
                e.printStackTrace();
            }
        }
    }
}
