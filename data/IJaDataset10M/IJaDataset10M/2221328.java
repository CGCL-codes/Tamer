package fm.radiostation.handler.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import fm.radiostation.RSFMUtils;
import fm.radiostation.Radio;

public class TuneResponseHandler extends RESTResponseHandler {

    private static String TYPE = "type";

    private static String NAME = "name";

    private static String URL = "url";

    private static String SUPPORT_DISCOVERY = "supportsdiscovery";

    private static String STATION = "station";

    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        if (STATION.equals(name)) {
            apiResponseObject = new Radio();
        }
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        Radio tune = (Radio) apiResponseObject;
        String attr = getAttribute();
        if (STATION.equals(name)) {
            tune.setSuccess(true);
        } else if (TYPE.equals(name)) {
            tune.setType(attr);
        } else if (NAME.equals(name)) {
            tune.setName(attr);
        } else if (URL.equals(name)) {
            tune.setUrl(attr);
        } else if (SUPPORT_DISCOVERY.equals(name)) {
            tune.setSupportdiscovery("1".equals(attr));
        } else {
            RSFMUtils.debug("Unprocessed tune response element name=" + name + " attr=" + attr, this);
        }
        setAttribute("");
    }
}
