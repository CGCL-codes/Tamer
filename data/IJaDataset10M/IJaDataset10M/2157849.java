package net.ideaity.jxmenu;

import org.w3c.dom.Document;
import net.ideaity.jxmenu.JxMenuContext;

/**
* Interface for defining a JxMenuFactory.
*
* @author Sloan Seaman (sloan@sgi.net)
* @version .05b
*/
public interface JxMenuFactory {

    /**
	* Create a Menu using the provided org.w3c.dom.Document
	* and JxMenuContext object
	*
	* @param aDoc The parsed XML document to use
	* @param aContext The context object to use
	* @return Whatever the implementing class creates. This
	*	should usually be a JComponent
	*/
    public Object create(Document aDoc, JxMenuContext aContext);
}
