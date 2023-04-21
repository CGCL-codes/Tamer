package lu.albert.ovum.config;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A simple handler for XML config files
 * @author Michel Albert <michel@albert.lu>
 * @todo Make more generic
 * @todo This is realised a bit quirky. It only checks for the name of the 
 * element and then reads out the first parameter. A better way would be nice. 
 */
public class ConfigParser extends DefaultHandler {

    /**
    * Call this when the start of an element <i>appears</i>
    * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
    */
    public void startElement(String nsName, String sName, String qName, Attributes attrs) {
        String eName = sName;
        if ("".equals(eName)) eName = qName;
        if (eName.equals("localeFile")) {
            Config.getInstance().setLocaleFile(attrs.getValue(0));
        }
    }
}
