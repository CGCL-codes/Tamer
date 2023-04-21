package org.apache.xerces.impl;

import java.io.IOException;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XML11Char;
import org.apache.xerces.util.XMLChar;
import org.apache.xerces.util.XMLStringBuffer;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

/**
 * This class is responsible for scanning the declarations found
 * in the internal and external subsets of a DTD in an XML document.
 * The scanner acts as the sources for the DTD information which is 
 * communicated to the DTD handlers.
 * <p>
 * This component requires the following features and properties from the
 * component manager that uses it:
 * <ul>
 *  <li>http://xml.org/sax/features/validation</li>
 *  <li>http://apache.org/xml/features/scanner/notify-char-refs</li>
 *  <li>http://apache.org/xml/properties/internal/symbol-table</li>
 *  <li>http://apache.org/xml/properties/internal/error-reporter</li>
 *  <li>http://apache.org/xml/properties/internal/entity-manager</li>
 * </ul>
 * 
 * @xerces.internal
 *
 * @author Arnaud  Le Hors, IBM
 * @author Andy Clark, IBM
 * @author Glenn Marcy, IBM
 * @author Eric Ye, IBM
 *
 * @version $Id: XML11DTDScannerImpl.java 572055 2007-09-02 17:55:43Z mrglavas $
 */
public class XML11DTDScannerImpl extends XMLDTDScannerImpl {

    /** String buffer. */
    private final XMLStringBuffer fStringBuffer = new XMLStringBuffer();

    /** Default constructor. */
    public XML11DTDScannerImpl() {
        super();
    }

    /** Constructor for he use of non-XMLComponentManagers. */
    public XML11DTDScannerImpl(SymbolTable symbolTable, XMLErrorReporter errorReporter, XMLEntityManager entityManager) {
        super(symbolTable, errorReporter, entityManager);
    }

    /**
     * Scans public ID literal.
     *
     * [12] PubidLiteral ::= '"' PubidChar* '"' | "'" (PubidChar - "'")* "'" 
     * [13] PubidChar::= #x20 | #xD | #xA | [a-zA-Z0-9] | [-'()+,./:=?;!*#@$_%]
     *
     * The returned string is normalized according to the following rule,
     * from http://www.w3.org/TR/REC-xml#dt-pubid:
     *
     * Before a match is attempted, all strings of white space in the public
     * identifier must be normalized to single space characters (#x20), and
     * leading and trailing white space must be removed.
     *
     * @param literal The string to fill in with the public ID literal.
     * @return True on success.
     *
     * <strong>Note:</strong> This method uses fStringBuffer, anything in it at
     * the time of calling is lost.
     */
    protected boolean scanPubidLiteral(XMLString literal) throws IOException, XNIException {
        int quote = fEntityScanner.scanChar();
        if (quote != '\'' && quote != '"') {
            reportFatalError("QuoteRequiredInPublicID", null);
            return false;
        }
        fStringBuffer.clear();
        boolean skipSpace = true;
        boolean dataok = true;
        while (true) {
            int c = fEntityScanner.scanChar();
            if (c == ' ' || c == '\n' || c == '\r' || c == 0x85 || c == 0x2028) {
                if (!skipSpace) {
                    fStringBuffer.append(' ');
                    skipSpace = true;
                }
            } else if (c == quote) {
                if (skipSpace) {
                    fStringBuffer.length--;
                }
                literal.setValues(fStringBuffer);
                break;
            } else if (XMLChar.isPubid(c)) {
                fStringBuffer.append((char) c);
                skipSpace = false;
            } else if (c == -1) {
                reportFatalError("PublicIDUnterminated", null);
                return false;
            } else {
                dataok = false;
                reportFatalError("InvalidCharInPublicID", new Object[] { Integer.toHexString(c) });
            }
        }
        return dataok;
    }

    /**
     * Normalize whitespace in an XMLString converting all whitespace
     * characters to space characters.
     */
    protected void normalizeWhitespace(XMLString value) {
        int end = value.offset + value.length;
        for (int i = value.offset; i < end; ++i) {
            int c = value.ch[i];
            if (XMLChar.isSpace(c)) {
                value.ch[i] = ' ';
            }
        }
    }

    /**
     * Normalize whitespace in an XMLString converting all whitespace
     * characters to space characters.
     */
    protected void normalizeWhitespace(XMLString value, int fromIndex) {
        int end = value.offset + value.length;
        for (int i = value.offset + fromIndex; i < end; ++i) {
            int c = value.ch[i];
            if (XMLChar.isSpace(c)) {
                value.ch[i] = ' ';
            }
        }
    }

    /**
     * Checks whether this string would be unchanged by normalization.
     * 
     * @return -1 if the value would be unchanged by normalization,
     * otherwise the index of the first whitespace character which
     * would be transformed.
     */
    protected int isUnchangedByNormalization(XMLString value) {
        int end = value.offset + value.length;
        for (int i = value.offset; i < end; ++i) {
            int c = value.ch[i];
            if (XMLChar.isSpace(c)) {
                return i - value.offset;
            }
        }
        return -1;
    }

    protected boolean isInvalid(int value) {
        return (!XML11Char.isXML11Valid(value));
    }

    protected boolean isInvalidLiteral(int value) {
        return (!XML11Char.isXML11ValidLiteral(value));
    }

    protected boolean isValidNameChar(int value) {
        return (XML11Char.isXML11Name(value));
    }

    protected boolean isValidNameStartChar(int value) {
        return (XML11Char.isXML11NameStart(value));
    }

    protected boolean isValidNCName(int value) {
        return (XML11Char.isXML11NCName(value));
    }

    protected boolean isValidNameStartHighSurrogate(int value) {
        return XML11Char.isXML11NameHighSurrogate(value);
    }

    protected boolean versionSupported(String version) {
        return version.equals("1.1") || version.equals("1.0");
    }

    protected String getVersionNotSupportedKey() {
        return "VersionNotSupported11";
    }
}
