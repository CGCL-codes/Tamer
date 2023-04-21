package serene.dtdcompatibility;

import org.xml.sax.Locator;

public class AttributeDefaultValueException extends DTDCompatibilityException {

    public AttributeDefaultValueException(String message, Locator locator) {
        super(message, locator);
    }

    public AttributeDefaultValueException(String message, Locator locator, Exception e) {
        super(message, locator, e);
    }

    public AttributeDefaultValueException(String message, String publicId, String systemId, int lineNumber, int columnNumber) {
        super(message, publicId, systemId, lineNumber, columnNumber);
    }

    public AttributeDefaultValueException(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e) {
        super(message, publicId, systemId, lineNumber, columnNumber, e);
    }
}
