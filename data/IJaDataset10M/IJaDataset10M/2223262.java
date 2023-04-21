package wtanaka.praya.bnet;

import java.io.IOException;
import java.util.Hashtable;
import wtanaka.praya.NotSentException;
import wtanaka.praya.Protocol;
import wtanaka.praya.Recipient;
import wtanaka.praya.ResolvedRecipient;

/**
 * An individual user (via private message).
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
public class BnetUser extends ResolvedRecipient {

    private String user;

    public BnetUser(Protocol parent, String user) {
        super(parent);
        this.user = user;
    }

    public Hashtable getFieldNames() {
        Hashtable h = new Hashtable();
        h.put("User", user);
        return h;
    }

    public Recipient withNewFields(Hashtable fields) {
        if (fields.get("User") != null) {
            return new BnetUser(m_protocol, (String) fields.get("User"));
        }
        return this;
    }

    public String getUsername() {
        return user;
    }

    public String getDescription() {
        return user;
    }

    public String getFullDescription() {
        return getDescription() + " (whisper via battle.net)";
    }

    public void sendReply(Object o) throws NotSentException {
        try {
            ((BnetClient) m_protocol).sendToUser(user, ((String) o));
        } catch (IOException e) {
            throw new NotSentException(e.getMessage());
        }
    }
}
