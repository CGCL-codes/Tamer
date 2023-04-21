package wtanaka.praya.gui.inline;

import wtanaka.praya.Recipient;

/**
 * Fired when the recipient to send to should change.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2002/01/25 08:24:11 $
 **/
public class RecipientChangeEvent implements java.io.Serializable {

    private Recipient m_recipient;

    private boolean m_isAdjusting;

    public RecipientChangeEvent(Recipient recip) {
        this(recip, false);
    }

    public RecipientChangeEvent(Recipient recip, boolean isAdjusting) {
        m_recipient = recip;
        m_isAdjusting = isAdjusting;
    }

    public Recipient getRecipient() {
        return m_recipient;
    }

    public boolean getValueIsAdjusting() {
        return m_isAdjusting;
    }

    public String toString() {
        return "[RecipientChangeEvent: " + m_recipient + "]";
    }
}
