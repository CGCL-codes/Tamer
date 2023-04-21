package org.likken.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.1 $ $Date: 2000/12/07 22:45:27 $
 */
public class DistinguishedName implements java.io.Serializable {

    ArrayList elements;

    public DistinguishedName() {
        elements = new ArrayList();
    }

    public DistinguishedName(final String anUnparsedDN) throws IllegalArgumentException {
        elements = new Parser(anUnparsedDN).getDN();
        elements.trimToSize();
    }

    private DistinguishedName(final List aListOfRDN) {
        elements = new ArrayList(aListOfRDN);
    }

    public String getDisplayName() {
        if (elements.size() > 0) {
            return getRDN(0).get(0).getValue();
        }
        return "";
    }

    public int getSize() {
        return elements.size();
    }

    public RDN getRDN(final int anIndex) {
        return (RDN) elements.get(anIndex);
    }

    public final boolean hasSuffix(final DistinguishedName aSuffixDN) {
        int i = elements.size();
        int j = aSuffixDN.elements.size();
        if (j > i) {
            return false;
        }
        while (j > 0) {
            if (!elements.get(--i).equals(aSuffixDN.elements.get(--j))) {
                return false;
            }
        }
        return true;
    }

    public final DistinguishedName withoutSuffix(final DistinguishedName aSuffixDN) {
        if (!hasSuffix(aSuffixDN)) {
            return this;
        }
        return new DistinguishedName(elements.subList(0, elements.size() - aSuffixDN.elements.size()));
    }

    public final DistinguishedName withSuffix(final DistinguishedName aSuffixDN) {
        if (hasSuffix(aSuffixDN)) {
            return this;
        }
        ArrayList list = new ArrayList(elements.size() + aSuffixDN.elements.size());
        list.addAll(elements);
        list.addAll(aSuffixDN.elements);
        return new DistinguishedName(list);
    }

    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof DistinguishedName) {
            return elements.equals(((DistinguishedName) o).elements);
        }
        return false;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < elements.size(); ++i) {
            if (i != 0) sb.append(',');
            sb.append(elements.get(i));
        }
        return sb.toString();
    }

    public class RDN {

        ArrayList attributes = new ArrayList(2);

        public final void addAttribute(final Attribute anAttribute) {
            attributes.add(anAttribute);
        }

        public final int size() {
            return attributes.size();
        }

        public final Attribute get(final int anIndex) {
            return (Attribute) attributes.get(anIndex);
        }

        public final String toString() {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < attributes.size(); ++i) {
                if (i != 0) sb.append('+');
                sb.append((Attribute) attributes.get(i));
            }
            return sb.toString();
        }

        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            } else if (o instanceof RDN) {
                return attributes.equals(((RDN) o).attributes);
            }
            return false;
        }
    }

    public class Attribute {

        private String type;

        private String value;

        public Attribute(final String aType, final String aValue) {
            type = aType;
            value = aValue;
        }

        public final String getType() {
            return type;
        }

        public final String getValue() {
            return value;
        }

        public final String toString() {
            StringBuffer sb = new StringBuffer(type.length() + value.length() + 1);
            return sb.append(type).append('=').append(value).toString();
        }

        public final boolean equals(final Object o) {
            if (this == o) {
                return true;
            } else if (o instanceof Attribute) {
                Attribute that = (Attribute) o;
                return this.type.equalsIgnoreCase(that.type) && this.value.equals(that.value);
            }
            return false;
        }
    }

    protected class Parser {

        private char chars[];

        private int pos;

        protected Parser(final String anUnparsedDN) {
            chars = anUnparsedDN.toCharArray();
            pos = 0;
        }

        /**
	 *  name = name-component *("," name-component)
	 */
        public ArrayList getDN() throws IllegalArgumentException {
            ArrayList list = new ArrayList();
            while (pos < chars.length) {
                skipWhitespace();
                list.add(parseNameComponent());
                skipWhitespace();
                if (pos >= chars.length) {
                    break;
                }
                if (chars[pos] != ',') {
                    throw new IllegalArgumentException();
                }
                ++pos;
            }
            return list;
        }

        private final String makeString(final int theStart, final int theEnd) throws IllegalArgumentException {
            if ((pos > chars.length) || (theStart >= theEnd)) {
                throw new IllegalArgumentException();
            }
            return new String(chars, theStart, theEnd - theStart);
        }

        private final void skipWhitespace() {
            while ((pos < chars.length) && Character.isWhitespace(chars[pos])) ++pos;
        }

        /**
	 *  hexchar = DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
	 *            / "a" / "b" / "c" / "d" / "e" / "f"
	 */
        private final boolean isHexchar(final char chr) {
            return ((chr >= '0') && (chr <= '9')) || ((chr >= 'a') && (chr <= 'f')) || ((chr >= 'A') && (chr <= 'F'));
        }

        /**
	 *  keychar       = ALPHA / DIGIT / "-"
	 *  oid           = 1*DIGIT *("." 1*DIGIT)
	 */
        private final boolean isKeychar(final char chr) {
            return ((chr >= 'a') && (chr <= 'z')) || ((chr >= 'A') && (chr <= 'Z')) || ((chr >= '0') && (chr <= '9')) || (chr == '-') || (chr == '.');
        }

        private final boolean isSeparator(final char chr) {
            return (chr == ',') || (chr == '+');
        }

        /**
	 *  name-component = attributeTypeAndValue *("+" attributeTypeAndValue)
	 */
        private final RDN parseNameComponent() throws IllegalArgumentException {
            RDN rdn = new RDN();
            for (; ; ) {
                rdn.addAttribute(parseAttributeTypeAndValue());
                skipWhitespace();
                if ((pos >= chars.length) || (chars[pos] != '+')) {
                    break;
                }
                ++pos;
                skipWhitespace();
            }
            return rdn;
        }

        /**
	 *  attributeTypeAndValue = attributeType "=" attributeValue
	 */
        private final Attribute parseAttributeTypeAndValue() throws IllegalArgumentException {
            String type = parseAttributeType();
            skipWhitespace();
            if ((pos >= chars.length) || (chars[pos] != '=')) {
                throw new IllegalArgumentException();
            }
            ++pos;
            skipWhitespace();
            return new Attribute(type, parseAttributeValue());
        }

        /**
	 *  attributeType = (ALPHA 1*keychar) / oid
	 *  keychar       = ALPHA / DIGIT / "-"
	 *  oid           = 1*DIGIT *("." 1*DIGIT)
	 */
        private final String parseAttributeType() throws IllegalArgumentException {
            int start = pos;
            while ((pos < chars.length) && isKeychar(chars[pos])) {
                ++pos;
            }
            return makeString(start, pos);
        }

        /**
	 *  attributeValue = *( stringchar / pair )
	 *                   / "#" hexstring
         *                   / QUOTATION *( quotechar / pair ) QUOTATION ; only from v2
	 */
        private final String parseAttributeValue() throws IllegalArgumentException {
            if (pos < chars.length) {
                if (chars[pos] == '"') {
                    return parseQuotedString();
                } else if (chars[pos] == '#') {
                    return parseHexstring();
                } else {
                    return parseString();
                }
            }
            throw new IllegalArgumentException();
        }

        private final String parseString() throws IllegalArgumentException {
            int start = pos;
            while ((pos < chars.length) && !isSeparator(chars[pos])) {
                if (chars[pos] == '\\') {
                    skipPair();
                }
                ++pos;
            }
            while (Character.isWhitespace(chars[pos - 1])) {
                --pos;
            }
            return makeString(start, pos);
        }

        private final String parseQuotedString() throws IllegalArgumentException {
            int start = pos;
            for (++pos; (pos < chars.length) && (chars[pos] != '"'); ++pos) {
                if (chars[pos] == '\\') {
                    skipPair();
                }
            }
            ++pos;
            return makeString(start, pos);
        }

        private final void skipPair() {
            if (++pos < chars.length) {
                if (isHexchar(chars[pos])) {
                    if ((++pos >= chars.length) || !isHexchar(chars[pos])) {
                        throw new IllegalArgumentException();
                    }
                }
            }
        }

        /**
	 *  hexstring  = 1*hexpair
	 *  hexpair    = hexchar hexchar
	 *  hexchar    = DIGIT / "A" / "B" / "C" / "D" / "E" / "F"
	 *               / "a" / "b" / "c" / "d" / "e" / "f"
	 */
        private final String parseHexstring() throws IllegalArgumentException {
            int start = pos++;
            while ((pos < chars.length) && isHexchar(chars[pos])) {
                ++pos;
            }
            if (((pos - start) & 1) == 0) {
                throw new IllegalArgumentException();
            }
            return makeString(start, pos);
        }
    }
}
