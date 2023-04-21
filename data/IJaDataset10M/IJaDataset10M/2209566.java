package gnu.xml.validation.datatype;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/**
 * The XML Schema ENTITIES type.
 *
 * @author <a href='mailto:dog@gnu.org'>Chris Burdess</a>
 */
final class EntitiesType extends AtomicSimpleType {

    static final int[] CONSTRAINING_FACETS = { Facet.LENGTH, Facet.MIN_LENGTH, Facet.MAX_LENGTH, Facet.PATTERN, Facet.ENUMERATION, Facet.WHITESPACE };

    EntitiesType() {
        super(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "ENTITIES"), TypeLibrary.ENTITY);
    }

    public int[] getConstrainingFacets() {
        return CONSTRAINING_FACETS;
    }

    public void checkValid(String value, ValidationContext context) throws DatatypeException {
        super.checkValid(value, context);
        StringBuffer buf = new StringBuffer();
        int len = value.length();
        for (int i = 0; i < len; i++) {
            char c = value.charAt(i);
            if (c == ' ') {
                String token = buf.toString();
                if (token.length() > 0) {
                    if (!context.isUnparsedEntity(token)) throw new DatatypeException(i, "invalid ENTITIES value");
                }
                buf.setLength(0);
            } else buf.append(c);
        }
        String token = buf.toString();
        if (token.length() == 0 || !context.isUnparsedEntity(token)) throw new DatatypeException("invalid ENTITIES value");
    }

    public boolean isContextDependent() {
        return true;
    }
}
