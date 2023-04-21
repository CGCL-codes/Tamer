package net.etherstorm.jOpenRPG.commlib;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import java.util.*;

/**
 * 
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 352 $
 * $Date: 2002-02-01 02:32:11 -0500 (Fri, 01 Feb 2002) $
 */
public abstract class MsgMessage extends AbstractMessage {

    /**
	 *
	 */
    Element data;

    /**
	 *
	 */
    public MsgMessage(String name) {
        super(name);
        setGroup(referenceManager.getCore().getGroupId());
    }

    /**
	 *
	 */
    public void fromXML(Document doc) {
        Element root = doc.getRootElement();
        Element msg = root.getChild("msg");
        setFrom(msg.getAttributeValue("from"));
        setGroup(msg.getAttributeValue("group_id"));
        StringTokenizer tok = new StringTokenizer(msg.getAttributeValue("to"), ", ");
        while (tok.hasMoreTokens()) addRecipient(tok.nextToken());
    }

    /**
	 *
	 */
    private Element getData() {
        if (data == null) data = new Element("data");
        return data;
    }

    /**
	 *
	 */
    protected String get(String key) {
        String foo = getData().getAttributeValue(key);
        if (key == null) {
            set(key, "");
            foo = "";
        }
        return foo;
    }

    /**
	 *
	 */
    protected void set(String key, String value) {
        getData().setAttribute(key, value);
    }

    /**
	 *
	 */
    public String toString() {
        org.jdom.output.XMLOutputter xout = new org.jdom.output.XMLOutputter();
        return getClass() + " " + xout.outputString(data) + xout.outputString(myElement);
    }
}
