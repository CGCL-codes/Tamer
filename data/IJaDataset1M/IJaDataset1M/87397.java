package client.cmd;

import java.util.StringTokenizer;
import client.MWClient;
import client.gui.CCommPanel;

/**
 * @author Imi (immanuel.scholz@gmx.de)
 */
public class SP extends Command {

    /**
	 * @see Command#Command(MMClient)
	 */
    public SP(MWClient mwclient) {
        super(mwclient);
    }

    /**
	 * @see client.cmd.Command#execute(java.lang.String)
	 */
    @Override
    public void execute(String input) {
        StringTokenizer st = decode(input);
        if (mwclient.getConfig().isParam("POPUPONMESSAGE")) mwclient.showInfoWindow(st.nextToken()); else mwclient.addToChat(st.nextToken(), CCommPanel.CHANNEL_MISC);
    }
}
