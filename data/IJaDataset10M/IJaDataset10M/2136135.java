package org.bibop.xml.xforge.components.examples;

import java.util.Date;
import java.text.SimpleDateFormat;
import org.xml.sax.SAXException;
import org.bibop.xml.xforge.components.AbstractXForgePoolableComponent;

/**
 * A simple component that writes the current time with a specific format
 */
public class TimeComponent extends AbstractXForgePoolableComponent {

    /**
     * the method that runs the component
     * @exception SAXException
     */
    public void toSax() throws SAXException {
        String paramValue = "yyyy-MM-dd";
        if (parameters.containsKey("format")) paramValue = (String) parameters.get("format");
        SimpleDateFormat sdf1 = new SimpleDateFormat(paramValue);
        String formattedDate = null;
        try {
            formattedDate = new String(sdf1.format(new Date()));
        } catch (Exception ex) {
            throw new SAXException("An error has occutred while formatting date : " + ex.getMessage());
        }
        this.contentHandler.characters(formattedDate.toCharArray(), 0, formattedDate.length());
    }
}
