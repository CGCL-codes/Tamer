package org.jcvi.vics.server.jaxb.reference_record.impl.runtime;

import com.sun.xml.bind.Messages;
import com.sun.xml.bind.serializer.AbortSerializationException;
import com.sun.xml.bind.util.ValidationEventLocatorExImpl;
import org.xml.sax.SAXException;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.helpers.PrintConversionEventImpl;
import javax.xml.bind.helpers.ValidationEventImpl;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;

/**
 * @author Kohsuke Kawaguchi (kohsuke.kawaguchi@sun.com)
 */
public class Util {

    /**
     * Reports a print conversion error while marshalling.
     */
    public static void handlePrintConversionException(Object caller, Exception e, XMLSerializer serializer) throws SAXException {
        if (e instanceof SAXException) throw (SAXException) e;
        String message = e.getMessage();
        if (message == null) {
            message = e.toString();
        }
        ValidationEvent ve = new PrintConversionEventImpl(ValidationEvent.ERROR, message, new ValidationEventLocatorImpl(caller), e);
        serializer.reportError(ve);
    }

    /**
     * Reports that the type of an object in a property is unexpected.
     */
    public static void handleTypeMismatchError(XMLSerializer serializer, Object parentObject, String fieldName, Object childObject) throws AbortSerializationException {
        ValidationEvent ve = new ValidationEventImpl(ValidationEvent.ERROR, Messages.format(Messages.ERR_TYPE_MISMATCH, getUserFriendlyTypeName(parentObject), fieldName, getUserFriendlyTypeName(childObject)), new ValidationEventLocatorExImpl(parentObject, fieldName));
        serializer.reportError(ve);
    }

    private static String getUserFriendlyTypeName(Object o) {
        if (o instanceof ValidatableObject) return ((ValidatableObject) o).getPrimaryInterface().getName(); else return o.getClass().getName();
    }
}
