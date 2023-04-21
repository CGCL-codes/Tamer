package fr.yahoo.smanciot.controller.xml;

import fr.yahoo.smanciot.xml.sax.SAXElement;

/**
 * @author smanciot
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface SaxErrors extends SAXElement {

    SaxError[] getErrors();
}
