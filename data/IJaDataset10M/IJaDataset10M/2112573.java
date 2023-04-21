package org.systemsbiology.libs.apmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
* DataProcessingHandler is APMLSAXHandler for parse apml and create DataProcessing object.
* @author Mi-Youn Brusniak
* @version version 1.0
* @since December 4, 2006
* @see APMLSAXHandler 
* @see DataProcessing 
* @see APMLElementName
*/
public class DataProcessingHandler extends APMLSAXHandler {

    private final DataProcessing dataP;

    private final APMLElementName apmlName = APMLElementName.getObject();

    public DataProcessingHandler() {
        dataP = new DataProcessing();
    }

    public String getClassName() {
        return DataProcessing.class.getName();
    }

    public Object getObject() {
        return dataP;
    }

    public void startElement(String uri, String localName, String elem, Attributes attributes) throws SAXException {
        if (apmlName.isSOFTWARE(elem)) {
            parseSoftware(attributes);
        }
    }

    public void endElement(String uri, String localName, String elem) throws SAXException {
        if (apmlName.isSOFTWARE(elem)) {
        }
    }

    private void parseSoftware(Attributes attributes) {
        dataP.setSoftware(attributes.getValue("name"));
        dataP.setType(ProcessStatus.valueOf(attributes.getValue("type").toUpperCase()));
        dataP.setVersion(attributes.getValue("version"));
    }
}
