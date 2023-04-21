package client.cmd;

import java.util.StringTokenizer;
import client.CUser;
import client.MWClient;

/**
 * @author Imi (immanuel.scholz@gmx.de)
 */
public class UG extends Command {

    /**
	 * @param client
	 */
    public UG(MWClient mwclient) {
        super(mwclient);
    }

    /**
	 * @see client.cmd.Command#execute(java.lang.String)
	 */
    @Override
    public void execute(String input) {
        StringTokenizer st = decode(input);
        CUser mmci = new CUser((String) st.nextElement());
        CUser user = mwclient.getUser(mmci.getName());
        while (mwclient.getUsers().remove(user)) {
            user = mwclient.getUser(mmci.getName());
        }
        mwclient.refreshGUI(MWClient.REFRESH_USERLIST);
        if (mwclient.isDedicated()) {
            return;
        }
        if ((mmci.isInvis() && mmci.getUserlevel() > mwclient.getUserLevel()) || mmci.getName().startsWith("[Dedicated]")) {
            return;
        }
        if (st.hasMoreTokens()) {
            String toSend = "<font color=\"maroon\">>> Exit " + mmci.getName() + "</font>";
            if (mwclient.getConfig().isParam("TIMESTAMP")) toSend = mwclient.getShortTime() + toSend;
            if (mwclient.getConfig().isParam("SHOWENTERANDEXIT")) mwclient.addToChat(toSend);
            mwclient.doPlaySound(mwclient.getConfigParam("SOUNDONEXIT"));
        }
    }
}
