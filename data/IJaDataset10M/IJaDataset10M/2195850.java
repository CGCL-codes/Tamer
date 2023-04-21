package wtanaka.praya.bnet;

import wtanaka.praya.Protocol;
import wtanaka.praya.Recipient;
import wtanaka.praya.obj.Message;

/**
 * Message generated when someone leaves a channel.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:27:21 $
 **/
public class BnetLeaveMessage extends Message {

    String user;

    public BnetLeaveMessage(Protocol parent, String user) {
        super(parent);
        this.user = user;
    }

    public String getContents() {
        return user + " has left the channel";
    }

    public Recipient replyRecipient() {
        return new BnetUser(generatedBy, user);
    }
}
