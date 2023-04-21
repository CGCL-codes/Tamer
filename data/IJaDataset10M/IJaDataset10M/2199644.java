package net.etherstorm.jOpenRPG.commlib;

import org.jdom.Element;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 352 $
 * $Date: 2002-02-01 02:32:11 -0500 (Fri, 01 Feb 2002) $
 */
public class TreeMessage extends MsgMessage {

    /**
	 *
	 */
    public TreeMessage() {
        super("tree");
        clear();
    }

    /**
	 *
	 */
    public void sendLocal() {
        referenceManager.getCore().sendLocal(this);
    }

    /**
	 *
	 */
    public void sendRemote() {
        Iterator iter = referenceManager.selectPlayers().iterator();
        while (iter.hasNext()) addRecipient((String) iter.next());
        referenceManager.getCore().sendRemote(this);
    }

    /**
	 *
	 */
    public void fromXML(Document doc) {
        super.fromXML(doc);
        myElement.addContent(doc.getRootElement().getChild("tree").getChild("nodehandler").detach());
    }

    /**
	 *
	 */
    public Element getNodehandler() {
        return myElement.getChild("nodehandler");
    }

    /**
	 *
	 */
    public void setNodehandler(Element nodehandler) {
        myElement.setText("");
        myElement.addContent((Element) nodehandler.clone());
    }
}
