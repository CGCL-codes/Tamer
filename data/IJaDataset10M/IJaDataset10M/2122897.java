package org.apache.crimson.parser;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.apache.crimson.util.XmlNames;

/**
 * This parser tests XML documents against the validity constraints
 * specified in the XML 1.0 specification as it parses them.  It
 * reports violations of those constraints using the standard SAX API.
 *
 * <P><em>This parser should be configured to use an <code>ErrorHandler</code>
 * that reject documents with validity errors, otherwise they will be accepted
 * despite errors.</em>  The default error handling, as specified by SAX,
 * ignores all validity errors.  The simplest way to have validity errors
 * have a useful effect is to pass a boolean <em>true</em> value to
 * the parser's constructor.
 *
 * <P> Note that most validity checks are performed during parsing by
 * the base class, for efficiency.  They're disabled by default in
 * that class, and enabled by the constructor in this class.
 *
 * @author David Brownell
 * @version $Revision: 1.1 $
 */
public class ValidatingParser extends Parser2 {

    private SimpleHashtable ids = new SimpleHashtable();

    /** Constructs a SAX parser object. */
    public ValidatingParser() {
        setIsValidating(true);
    }

    /**
     * Constructs a SAX parser object, optionally assigning the error
     * handler to report exceptions on recoverable errors (which include
     * all validity errors) as well as fatal errors.
     *
     * @param rejectValidityErrors When true, the parser will use an
     *	error handler which throws exceptions on recoverable errors.
     *	Otherwise it uses the default SAX error handler, which ignores
     *	such errors.
     */
    public ValidatingParser(boolean rejectValidityErrors) {
        this();
        if (rejectValidityErrors) setErrorHandler(new HandlerBase() {

            public void error(SAXParseException x) throws SAXException {
                throw x;
            }
        });
    }

    void afterRoot() throws SAXException {
        for (Enumeration e = ids.keys(); e.hasMoreElements(); ) {
            String id = (String) e.nextElement();
            Boolean value = (Boolean) ids.get(id);
            if (Boolean.FALSE == value) error("V-024", new Object[] { id });
        }
    }

    void afterDocument() {
        ids.clear();
    }

    void validateAttributeSyntax(AttributeDecl attr, String value) throws SAXException {
        if (AttributeDecl.ID == attr.type) {
            if (!XmlNames.isName(value)) error("V-025", new Object[] { value });
            Boolean b = (Boolean) ids.getNonInterned(value);
            if (b == null || b.equals(Boolean.FALSE)) ids.put(value.intern(), Boolean.TRUE); else error("V-026", new Object[] { value });
        } else if (AttributeDecl.IDREF == attr.type) {
            if (!XmlNames.isName(value)) error("V-027", new Object[] { value });
            Boolean b = (Boolean) ids.getNonInterned(value);
            if (b == null) ids.put(value.intern(), Boolean.FALSE);
        } else if (AttributeDecl.IDREFS == attr.type) {
            StringTokenizer tokenizer = new StringTokenizer(value);
            Boolean b;
            boolean sawValue = false;
            while (tokenizer.hasMoreTokens()) {
                value = tokenizer.nextToken();
                if (!XmlNames.isName(value)) error("V-027", new Object[] { value });
                b = (Boolean) ids.getNonInterned(value);
                if (b == null) ids.put(value.intern(), Boolean.FALSE);
                sawValue = true;
            }
            if (!sawValue) error("V-039", null);
        } else if (AttributeDecl.NMTOKEN == attr.type) {
            if (!XmlNames.isNmtoken(value)) error("V-028", new Object[] { value });
        } else if (AttributeDecl.NMTOKENS == attr.type) {
            StringTokenizer tokenizer = new StringTokenizer(value);
            boolean sawValue = false;
            while (tokenizer.hasMoreTokens()) {
                value = tokenizer.nextToken();
                if (!XmlNames.isNmtoken(value)) error("V-028", new Object[] { value });
                sawValue = true;
            }
            if (!sawValue) error("V-032", null);
        } else if (AttributeDecl.ENUMERATION == attr.type) {
            for (int i = 0; i < attr.values.length; i++) if (value.equals(attr.values[i])) return;
            error("V-029", new Object[] { value });
        } else if (AttributeDecl.NOTATION == attr.type) {
            for (int i = 0; i < attr.values.length; i++) if (value.equals(attr.values[i])) return;
            error("V-030", new Object[] { value });
        } else if (AttributeDecl.ENTITY == attr.type) {
            if (!isUnparsedEntity(value)) error("V-031", new Object[] { value });
        } else if (AttributeDecl.ENTITIES == attr.type) {
            StringTokenizer tokenizer = new StringTokenizer(value);
            boolean sawValue = false;
            while (tokenizer.hasMoreTokens()) {
                value = tokenizer.nextToken();
                if (!isUnparsedEntity(value)) error("V-031", new Object[] { value });
                sawValue = true;
            }
            if (!sawValue) error("V-040", null);
        } else if (AttributeDecl.CDATA != attr.type) throw new InternalError(attr.type);
    }

    ContentModel newContentModel(String tag) {
        return new ContentModel(tag);
    }

    ContentModel newContentModel(char type, ContentModel next) {
        return new ContentModel(type, next);
    }

    ElementValidator newValidator(ElementDecl element) {
        if (element.validator != null) return element.validator;
        if (element.model != null) return new ChildrenValidator(element);
        if (element.contentType == null || strANY == element.contentType) element.validator = ElementValidator.ANY; else if (strEMPTY == element.contentType) element.validator = EMPTY; else element.validator = new MixedValidator(element);
        return element.validator;
    }

    private final EmptyValidator EMPTY = new EmptyValidator();

    class EmptyValidator extends ElementValidator {

        public void consume(String token) throws SAXException {
            error("V-033", null);
        }

        public void text() throws SAXException {
            error("V-033", null);
        }
    }

    class MixedValidator extends ElementValidator {

        private ElementDecl element;

        MixedValidator(ElementDecl element) {
            this.element = element;
        }

        public void consume(String type) throws SAXException {
            String model = element.contentType;
            for (int index = 8; (index = model.indexOf(type, index + 1)) >= 9; ) {
                char c;
                if (model.charAt(index - 1) != '|') continue;
                c = model.charAt(index + type.length());
                if (c == '|' || c == ')') return;
            }
            error("V-034", new Object[] { element.name, type, model });
        }
    }

    class ChildrenValidator extends ElementValidator {

        private ContentModelState state;

        private String name;

        ChildrenValidator(ElementDecl element) {
            state = new ContentModelState(element.model);
            name = element.name;
        }

        public void consume(String token) throws SAXException {
            if (state == null) error("V-035", new Object[] { name, token }); else try {
                state = state.advance(token);
            } catch (EndOfInputException e) {
                error("V-036", new Object[] { name, token });
            }
        }

        public void text() throws SAXException {
            error("V-037", new Object[] { name });
        }

        public void done() throws SAXException {
            if (state != null && !state.terminate()) error("V-038", new Object[] { name });
        }
    }

    private boolean isUnparsedEntity(String name) {
        Object e = entities.getNonInterned(name);
        if (e == null || !(e instanceof ExternalEntity)) return false;
        return ((ExternalEntity) e).notation != null;
    }
}
