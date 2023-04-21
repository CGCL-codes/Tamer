package de.mogwai.common.webservice.rest;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import de.mogwai.common.utils.XMLException;
import de.mogwai.common.utils.XMLUtils;

/**
 * DOM Request f�r einen Webservice. 
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 14:26:37 $
 */
public class DOMWebserviceRequest extends StringWebserviceRequest {

    public DOMWebserviceRequest(Document aDocument) throws XMLException, ParserConfigurationException {
        super(XMLUtils.getInstance().transformToString(aDocument));
    }
}
