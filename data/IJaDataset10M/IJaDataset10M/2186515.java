package wtanaka.praya.gale;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import wtanaka.praya.NotSentException;
import wtanaka.praya.Protocol;
import wtanaka.praya.Recipient;
import wtanaka.praya.ResolvedRecipient;

/**
 * An old world order private recipient.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:25:17 $
 **/
public class PrivateRecipient extends ResolvedRecipient {

    private String[] m_ids;

    private Hashtable fields = new Hashtable();

    private static final String ID_LABEL = "ID";

    public PrivateRecipient(Protocol galeClient, String[] ids) {
        super(galeClient);
        m_ids = new String[ids.length];
        System.arraycopy(ids, 0, m_ids, 0, ids.length);
        fields.put(ID_LABEL, arrayToColonSeparated(ids));
    }

    private static String[] colonSeparatedToArray(String colonSep) {
        int count = 0;
        for (int i = 0; i < colonSep.length(); ++i) {
            if (colonSep.charAt(i) == ':') {
                count++;
            }
        }
        String[] toReturn = new String[count + 1];
        int start = 0;
        for (int i = 0; i < toReturn.length; ++i) {
            int end = colonSep.indexOf(":", start);
            if (end < 0) toReturn[i] = colonSep.substring(start); else toReturn[i] = colonSep.substring(start, end);
            start = end + 1;
        }
        return toReturn;
    }

    private static String arrayToColonSeparated(String[] array) {
        StringBuffer sb = new StringBuffer();
        if (array.length > 0) sb.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            sb.append(":");
            sb.append(array[i]);
        }
        return sb.toString();
    }

    public String getDescription() {
        return arrayToColonSeparated(m_ids) + " (private)";
    }

    public String getFullDescription() {
        return arrayToColonSeparated(m_ids) + " (private) via " + m_protocol.getCurrentDescription();
    }

    /**
    * @exception NotSentException if the message is not sent
    **/
    public void sendReply(Object o) throws NotSentException {
        try {
            Vector categories = new Vector();
            for (int i = 0; i < m_ids.length; ++i) {
                if (m_ids[i].indexOf('@') >= 0) {
                    categories.addElement(new Category("@" + m_ids[i].substring(1 + m_ids[i].indexOf('@')) + "/user/" + m_ids[i].substring(0, m_ids[i].indexOf('@')) + "/"));
                } else {
                    throw new NotSentException("ID expected to contain a @");
                }
            }
            Category[] cats = new Category[categories.size()];
            categories.copyInto(cats);
            ((GaleClient) m_protocol).sendEncrypted(cats, (String) o, m_ids);
        } catch (IOException e) {
            throw new NotSentException(e.toString());
        }
    }

    public Hashtable getFieldNames() {
        return fields;
    }

    public Recipient withNewFields(Hashtable newFields) {
        String newID;
        if ((newID = (String) newFields.get(ID_LABEL)) != null) return new PrivateRecipient((GaleClient) m_protocol, colonSeparatedToArray(newID));
        return this;
    }
}
