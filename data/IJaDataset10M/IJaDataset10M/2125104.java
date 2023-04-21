package il.co.gadiworks.tutorial;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlingXMLStuff extends DefaultHandler {

    XMLDataCollected info = new XMLDataCollected();

    public String getInformation() {
        return info.dataToString();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals("city")) {
            String city = atts.getValue("data");
            info.setCity(city);
        } else if (localName.equals("temp_c")) {
            String temp = atts.getValue("data");
            info.setTemp(Integer.parseInt(temp));
        }
    }
}
