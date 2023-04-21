package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/**
 * The XML Schema unsignedInt type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class UnsignedIntType extends AtomicSimpleType {

    static final int[] CONSTRAINING_FACETS = { Facet.TOTAL_DIGITS, Facet.FRACTION_DIGITS, Facet.PATTERN, Facet.WHITESPACE, Facet.ENUMERATION, Facet.MAX_INCLUSIVE, Facet.MAX_EXCLUSIVE, Facet.MIN_INCLUSIVE, Facet.MIN_EXCLUSIVE };

    static final String MAX_VALUE = "4294967295";

    static final int LENGTH = MAX_VALUE.length();

    UnsignedIntType() {
        super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "unsignedInt"), TypeLibrary.UNSIGNED_LONG);
    }

    public int[] getConstrainingFacets() {
        return CONSTRAINING_FACETS;
    }

    public void checkValid(String value, ValidationContext context) throws DatatypeException {
        super.checkValid(value, context);
        int len = value.length();
        if (len == 0) throw new DatatypeException(0, "invalid unsigned int value");
        boolean compare = false;
        for (int i = 0; i < len; i++) {
            if (len - i > LENGTH) throw new DatatypeException(i, "invalid unsigned int value"); else if (len - i == LENGTH) compare = true;
            char c = value.charAt(i);
            if (c >= 0x30 && c <= 0x39) {
                if (compare) {
                    char d = MAX_VALUE.charAt(i);
                    if (Character.digit(c, 10) > Character.digit(d, 10)) throw new DatatypeException(i, "invalid unsigned int value");
                }
                continue;
            }
            throw new DatatypeException(i, "invalid unsigned int value");
        }
    }

    public Object createValue(String literal, ValidationContext context) {
        try {
            return new Integer(literal);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
