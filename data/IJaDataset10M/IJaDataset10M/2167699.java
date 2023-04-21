package wtanaka.praya.yahoo;

import java.io.Serializable;
import java.util.Date;
import wtanaka.praya.Protocol;
import wtanaka.praya.Recipient;
import wtanaka.praya.obj.Message;

/**
 * Represents a calendar notification.
 *
 * <p>
 * Return to <A href="http://sourceforge.net/projects/praya/">
 * <IMG src="http://sourceforge.net/sflogo.php?group_id=2302&type=1"
 *   alt="Sourceforge" width="88" height="31" border="0"></A>
 * or the <a href="http://praya.sourceforge.net/">Praya Homepage</a>
 *
 * @author $Author: wtanaka $
 * @version $Name:  $ $Date: 2003/12/17 01:30:15 $
 **/
public class CalendarMessage extends Message implements Serializable {

    String m_url, m_title, m_description;

    Date m_date;

    int m_eventType;

    public CalendarMessage(Protocol parent, String url, int eventType, Date date, String title, String description) {
        super(parent);
        m_url = url;
        m_title = title;
        m_description = description;
        m_date = date;
        m_eventType = eventType;
    }

    public String getSubject() {
        return "Calendar Alert";
    }

    public Recipient replyRecipient() {
        return ((YahooClient) generatedBy).getDefaultRecipient();
    }

    public String getFrom() {
        return "Yahoo Calendar";
    }

    public String getContents() {
        return m_title + "(" + m_date + ")\n" + m_url + "\n" + m_description;
    }

    public int getScore() {
        return 10;
    }
}
