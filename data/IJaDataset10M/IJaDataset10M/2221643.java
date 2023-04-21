package cx.ath.contribs.internal.xerces.impl.xs.identity;

import cx.ath.contribs.internal.xerces.impl.xpath.XPathException;
import cx.ath.contribs.internal.xerces.impl.xs.util.ShortListImpl;
import cx.ath.contribs.internal.xerces.util.SymbolTable;
import cx.ath.contribs.internal.xerces.util.XMLChar;
import cx.ath.contribs.internal.xerces.xni.NamespaceContext;
import cx.ath.contribs.internal.xerces.xs.ShortList;
import cx.ath.contribs.internal.xerces.xs.XSComplexTypeDefinition;
import cx.ath.contribs.internal.xerces.xs.XSConstants;
import cx.ath.contribs.internal.xerces.xs.XSTypeDefinition;

/**
 * Schema identity constraint field.
 *
 * @xerces.internal 
 *
 * @author Andy Clark, IBM
 * @version $Id: Field.java,v 1.1 2007/06/02 09:58:59 paul Exp $
 */
public class Field {

    /** Field XPath. */
    protected Field.XPath fXPath;

    /** Identity constraint. */
    protected IdentityConstraint fIdentityConstraint;

    /** Constructs a field. */
    public Field(Field.XPath xpath, IdentityConstraint identityConstraint) {
        fXPath = xpath;
        fIdentityConstraint = identityConstraint;
    }

    /** Returns the field XPath. */
    public cx.ath.contribs.internal.xerces.impl.xpath.XPath getXPath() {
        return fXPath;
    }

    /** Returns the identity constraint. */
    public IdentityConstraint getIdentityConstraint() {
        return fIdentityConstraint;
    }

    /** Creates a field matcher. */
    public XPathMatcher createMatcher(FieldActivator activator, ValueStore store) {
        return new Field.Matcher(fXPath, activator, store);
    }

    /** Returns a string representation of this object. */
    public String toString() {
        return fXPath.toString();
    }

    /**
     * Field XPath.
     *
     * @author Andy Clark, IBM
     */
    public static class XPath extends cx.ath.contribs.internal.xerces.impl.xpath.XPath {

        /** Constructs a field XPath expression. */
        public XPath(String xpath, SymbolTable symbolTable, NamespaceContext context) throws XPathException {
            super(fixupXPath(xpath), symbolTable, context);
            for (int i = 0; i < fLocationPaths.length; i++) {
                for (int j = 0; j < fLocationPaths[i].steps.length; j++) {
                    cx.ath.contribs.internal.xerces.impl.xpath.XPath.Axis axis = fLocationPaths[i].steps[j].axis;
                    if (axis.type == XPath.Axis.ATTRIBUTE && (j < fLocationPaths[i].steps.length - 1)) {
                        throw new XPathException("c-fields-xpaths");
                    }
                }
            }
        }

        /** Fixup XPath expression. Avoid creating a new String if possible. */
        private static String fixupXPath(String xpath) {
            final int end = xpath.length();
            int offset = 0;
            boolean whitespace = true;
            char c;
            for (; offset < end; ++offset) {
                c = xpath.charAt(offset);
                if (whitespace) {
                    if (!XMLChar.isSpace(c)) {
                        if (c == '.' || c == '/') {
                            whitespace = false;
                        } else if (c != '|') {
                            return fixupXPath2(xpath, offset, end);
                        }
                    }
                } else if (c == '|') {
                    whitespace = true;
                }
            }
            return xpath;
        }

        private static String fixupXPath2(String xpath, int offset, final int end) {
            StringBuffer buffer = new StringBuffer(end + 2);
            for (int i = 0; i < offset; ++i) {
                buffer.append(xpath.charAt(i));
            }
            buffer.append("./");
            boolean whitespace = false;
            char c;
            for (; offset < end; ++offset) {
                c = xpath.charAt(offset);
                if (whitespace) {
                    if (!XMLChar.isSpace(c)) {
                        if (c == '.' || c == '/') {
                            whitespace = false;
                        } else if (c != '|') {
                            buffer.append("./");
                            whitespace = false;
                        }
                    }
                } else if (c == '|') {
                    whitespace = true;
                }
                buffer.append(c);
            }
            return buffer.toString();
        }
    }

    /**
     * Field matcher.
     *
     * @author Andy Clark, IBM
     */
    protected class Matcher extends XPathMatcher {

        /** Field activator. */
        protected FieldActivator fFieldActivator;

        /** Value store for data values. */
        protected ValueStore fStore;

        /** Constructs a field matcher. */
        public Matcher(Field.XPath xpath, FieldActivator activator, ValueStore store) {
            super(xpath);
            fFieldActivator = activator;
            fStore = store;
        }

        /**
         * This method is called when the XPath handler matches the
         * XPath expression.
         */
        protected void matched(Object actualValue, short valueType, ShortList itemValueType, boolean isNil) {
            super.matched(actualValue, valueType, itemValueType, isNil);
            if (isNil && (fIdentityConstraint.getCategory() == IdentityConstraint.IC_KEY)) {
                String code = "KeyMatchesNillable";
                fStore.reportError(code, new Object[] { fIdentityConstraint.getElementName(), fIdentityConstraint.getIdentityConstraintName() });
            }
            fStore.addValue(Field.this, actualValue, convertToPrimitiveKind(valueType), convertToPrimitiveKind(itemValueType));
            fFieldActivator.setMayMatch(Field.this, Boolean.FALSE);
        }

        private short convertToPrimitiveKind(short valueType) {
            if (valueType <= XSConstants.NOTATION_DT) {
                return valueType;
            }
            if (valueType <= XSConstants.ENTITY_DT) {
                return XSConstants.STRING_DT;
            }
            if (valueType <= XSConstants.POSITIVEINTEGER_DT) {
                return XSConstants.DECIMAL_DT;
            }
            return valueType;
        }

        private ShortList convertToPrimitiveKind(ShortList itemValueType) {
            if (itemValueType != null) {
                int i;
                final int length = itemValueType.getLength();
                for (i = 0; i < length; ++i) {
                    short type = itemValueType.item(i);
                    if (type != convertToPrimitiveKind(type)) {
                        break;
                    }
                }
                if (i != length) {
                    final short[] arr = new short[length];
                    for (int j = 0; j < i; ++j) {
                        arr[j] = itemValueType.item(j);
                    }
                    for (; i < length; ++i) {
                        arr[i] = convertToPrimitiveKind(itemValueType.item(i));
                    }
                    return new ShortListImpl(arr, arr.length);
                }
            }
            return itemValueType;
        }

        protected void handleContent(XSTypeDefinition type, boolean nillable, Object actualValue, short valueType, ShortList itemValueType) {
            if (type == null || type.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE && ((XSComplexTypeDefinition) type).getContentType() != XSComplexTypeDefinition.CONTENTTYPE_SIMPLE) {
                fStore.reportError("cvc-id.3", new Object[] { fIdentityConstraint.getName(), fIdentityConstraint.getElementName() });
            }
            fMatchedString = actualValue;
            matched(fMatchedString, valueType, itemValueType, nillable);
        }
    }
}
