package net.etherstorm.jopenrpg.net;

import java.util.Iterator;
import net.etherstorm.jopenrpg.swing.nodehandlers.BaseNodehandler;
import org.jdom.Document;
import org.jdom.Element;

/**
 * 
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 1.7 $
 * $Date: 2003/07/29 05:46:31 $
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
    public void setNodehandler(BaseNodehandler nodehandler) {
        myElement.setText("");
        Element e = nodehandler.toXML();
        e.detach();
        myElement.addContent(e);
    }
}
