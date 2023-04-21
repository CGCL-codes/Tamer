package de.javatt.testrunner.finder;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.NodeImpl;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import de.javatt.data.scenario.http.verification.HTMLTagAttribute;

/**
 * HTMLTagAtributeFinder is an HTMLTagFinder which finds
 * an HTML tag depending on the Attribute.
 * 
 * @author Uwe Katt
 *
 */
public class HTMLTagAttributeFinder implements HTMLTagFinder {

    /**
	 * The Name
	 */
    private HTMLTagAttribute myAttrib;

    private Logger myLogger;

    /**
	 * Constructor
	 * 
	 * @param name
	 */
    public HTMLTagAttributeFinder(HTMLTagAttribute attrib) {
        super();
        myAttrib = attrib;
        myLogger = Logger.getLogger(getClass());
    }

    /**
	 * Does the Node match the Criterion specified by this
	 * finder?
	 */
    public boolean matches(NodeImpl toFind) {
        boolean returnValue = false;
        NamedNodeMap listOfAttributes = toFind.getAttributes();
        Node namedItem = listOfAttributes.getNamedItem(myAttrib.getName());
        if (namedItem != null) {
            String attribValue = namedItem.getNodeValue();
            myLogger.debug("check attribute: name=" + myAttrib.getName() + "; value= " + attribValue);
            if (attribValue != null) {
                if (attribValue.equalsIgnoreCase(myAttrib.getValue())) {
                    returnValue = true;
                }
            }
        }
        return returnValue;
    }
}
