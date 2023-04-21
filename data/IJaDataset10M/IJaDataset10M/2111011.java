package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/**
 * The XML Schema long type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class LongType extends AtomicSimpleType {

    static final int[] CONSTRAINING_FACETS = { Facet.TOTAL_DIGITS, Facet.FRACTION_DIGITS, Facet.PATTERN, Facet.WHITESPACE, Facet.ENUMERATION, Facet.MAX_INCLUSIVE, Facet.MAX_EXCLUSIVE, Facet.MIN_INCLUSIVE, Facet.MIN_EXCLUSIVE };

    static final String MAX_VALUE = "9223372036854775807";

    static final String MIN_VALUE = "9223372036854775808";

    static final int LENGTH = MAX_VALUE.length();

    LongType() {
        super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long"), TypeLibrary.INTEGER);
    }

    public int[] getConstrainingFacets() {
        return CONSTRAINING_FACETS;
    }

    public void checkValid(String value, ValidationContext context) throws DatatypeException {
        super.checkValid(value, context);
        int len = value.length();
        if (len == 0) throw new DatatypeException(0, "invalid long value");
        int i = 0, off = 0;
        boolean compare = false;
        String compareTo = MAX_VALUE;
        char c = value.charAt(0);
        if (c == '+') i++; else if (c == '-') {
            compareTo = MIN_VALUE;
            i++;
        }
        if (len - i > LENGTH) throw new DatatypeException(i, "invalid long value"); else if (len - i == LENGTH) compare = true;
        for (; i < len; i++) {
            c = value.charAt(i);
            if (c >= 0x30 && c <= 0x39) {
                if (compare) {
                    char d = compareTo.charAt(off);
                    if (Character.digit(c, 10) > Character.digit(d, 10)) throw new DatatypeException(i, "invalid long value");
                }
                off++;
                continue;
            }
            throw new DatatypeException(i, "invalid long value");
        }
    }

    public Object createValue(String literal, ValidationContext context) {
        try {
            return new Long(literal);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
