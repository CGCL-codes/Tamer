package wtanaka.praya;

import java.util.EventObject;

/**
 * Fired when the description of a protocol changes.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 *  @author $Author: wtanaka $
 *  @version $Name:  $ $Date: 2001/10/14 18:07:13 $
 **/
public class ProtocolDescriptionEvent extends EventObject {

    private String m_description;

    public ProtocolDescriptionEvent(Protocol source, String newDescription) {
        super(source);
        m_description = newDescription;
    }

    public Protocol getSourceProtocol() {
        return (Protocol) getSource();
    }

    public String getDescription() {
        return m_description;
    }

    public String toString() {
        return "[ProtocolDescriptionEvent: " + getSourceProtocol() + " is now: " + getDescription() + "]";
    }
}
