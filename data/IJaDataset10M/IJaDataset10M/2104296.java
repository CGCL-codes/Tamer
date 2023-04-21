package org.wwweeeportal.util.xml.sax;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import javax.xml.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.ext.*;
import org.xml.sax.helpers.*;
import org.springframework.core.convert.*;
import org.wwweeeportal.util.*;
import org.wwweeeportal.util.convert.*;
import org.wwweeeportal.util.logging.*;
import org.wwweeeportal.util.xml.*;

/**
 * <a href="http://www.saxproject.org/">SAX</a> Utilities.
 */
public abstract class SAXUtil {

    /**
   * The <a href="http://www.saxproject.org/">SAX</a> property used to
   * {@linkplain XMLReader#setProperty(String, Object) set} a {@link LexicalHandler} (such as {@link DOMBuildingHandler}
   * ) on the {@link XMLReader} which a {@link SAXParser} is {@linkplain SAXParser#getXMLReader() using}. Doing this
   * allows the {@link LexicalHandler} to receive events for {@linkplain LexicalHandler#comment(char[], int, int)
   * comments} and {@linkplain LexicalHandler#startCDATA() CDATA} sections, etc.
   */
    public static final String SAX_LEXICAL_HANDLER_PROP = "http://xml.org/sax/properties/lexical-handler";

    /**
   * The <a href="http://www.saxproject.org/">SAX</a> property used to
   * {@linkplain XMLReader#setProperty(String, Object) set} a {@link DeclHandler} (such as {@link DOMBuildingHandler})
   * on the {@link XMLReader} which a {@link SAXParser} is {@linkplain SAXParser#getXMLReader() using}. Doing this
   * allows the {@link DeclHandler} to receive events for DTD declaration elements.
   */
    public static final String SAX_DECL_HANDLER_PROP = "http://xml.org/sax/properties/declaration-handler";

    /**
   * A shared {@linkplain SAXParserFactory#isNamespaceAware() namespace-aware}
   * {@linkplain SAXParserFactory#isValidating() non-validating} {@link SAXParserFactory}.
   */
    protected static final SAXParserFactory SAX_PARSER_FACTORY = SAXParserFactory.newInstance();

    static {
        SAX_PARSER_FACTORY.setNamespaceAware(true);
        SAX_PARSER_FACTORY.setValidating(false);
        try {
            SAX_PARSER_FACTORY.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        } catch (java.lang.Exception e) {
        }
    }

    /**
   * Create a new {@link SAXParser}. The parser will be {@linkplain SAXParserFactory#setNamespaceAware(boolean)
   * namespace aware}, {@linkplain SAXParserFactory#setValidating(boolean) validating}, won't load external DTD's, and
   * will throw exceptions on parsing errors.
   * 
   * @return The new {@link SAXParser}.
   */
    public static final SAXParser newSAXParser() {
        final SAXParser saxParser;
        try {
            synchronized (SAX_PARSER_FACTORY) {
                saxParser = SAX_PARSER_FACTORY.newSAXParser();
            }
        } catch (org.xml.sax.SAXException saxe) {
            throw new RuntimeException(saxe);
        } catch (ParserConfigurationException pce) {
            throw new RuntimeException(pce);
        }
        return saxParser;
    }

    /**
   * Set the specified attribute <code>value</code>.
   * 
   * @param attributes The {@link Attributes} upon which the <code>value</code> should be
   * {@linkplain AttributesImpl#setAttribute(int, String, String, String, String, String) set}.
   * @param nsURI The <a href="http://www.w3.org/TR/xml-names/">namespace</a> {@link URI} to use for the created
   * attribute.
   * @param nsPrefix The namespace prefix to use for the created attribute.
   * @param localName The {@linkplain Attributes#getLocalName(int) local name} to use for the created attribute.
   * @param value The new value of the attribute. If <code>null</code> any existing attribute will be
   * {@linkplain AttributesImpl#removeAttribute(int) removed}.
   * @return The new {@link AttributesImpl} based on the old <code>attributes</code>, but with the new
   * <code>value</code> added.
   * @throws IllegalArgumentException If <code>localName</code> is <code>null</code>.
   * @see AttributesImpl#setAttribute(int, String, String, String, String, String)
   * @see AttributesImpl#removeAttribute(int)
   */
    public static final AttributesImpl setAttribute(final Attributes attributes, final Object nsURI, final String nsPrefix, final String localName, final Object value) throws IllegalArgumentException {
        if (localName == null) throw new IllegalArgumentException("null localName");
        final AttributesImpl newAttributes;
        final int existingIndex;
        if (attributes != null) {
            newAttributes = (attributes instanceof AttributesImpl) ? (AttributesImpl) attributes : new AttributesImpl(attributes);
            existingIndex = attributes.getIndex((nsURI != null) ? nsURI.toString() : "", localName);
        } else {
            newAttributes = new AttributesImpl();
            existingIndex = -1;
        }
        if (value != null) {
            if (existingIndex >= 0) {
                newAttributes.setAttribute(existingIndex, (nsURI != null) ? nsURI.toString() : "", localName, (nsPrefix != null) ? nsPrefix + ':' + localName : localName, "CDATA", value.toString());
            } else {
                newAttributes.addAttribute((nsURI != null) ? nsURI.toString() : "", localName, (nsPrefix != null) ? nsPrefix + ':' + localName : localName, "CDATA", value.toString());
            }
        } else {
            if (existingIndex >= 0) newAttributes.removeAttribute(existingIndex);
        }
        return newAttributes;
    }

    /**
   * Set the <code>xml:lang</code> Attr in the given <code>attributes</code> to the value of the given
   * <code>locale</code>.
   * 
   * @param attributes The {@link Attributes} in which to declare the language.
   * @param locale The {@link Locale} of the language to declare, if <code>null</code> an empty attribute will be
   * declared.
   * @return The new {@link AttributesImpl} based on the old <code>attributes</code>, but with the language attribute
   * added.
   * @throws ConversionException If there was a problem {@linkplain I18nUtil#LOCALE_LANGUAGE_TAG_CONVERTER converting}
   * the lang attribute.
   * @see I18nUtil#LOCALE_LANGUAGE_TAG_CONVERTER
   */
    public static final AttributesImpl setXMLLangAttr(final Attributes attributes, final Locale locale) throws ConversionException {
        return setAttribute(attributes, XMLUtil.XML_NS_URI, XMLConstants.XML_NS_PREFIX, "lang", (locale != null) ? ConversionUtil.invokeConverter(I18nUtil.LOCALE_LANGUAGE_TAG_CONVERTER, locale) : "");
    }

    /**
   * Retrieve the value of the <code>xml:lang</code> attribute from the supplied <code>attributes</code> list.
   * 
   * @param attributes The {@link Attributes} from which to retrieve the <code>xml:lang</code> value.
   * @param defaultLocale The {@link Locale} to return should the <code>attributes</code> not specify a language.
   * @return The value of the <code>xml:lang</code> attribute, parsed as a {@link Locale}.
   * @throws ConversionException If there was a problem {@linkplain I18nUtil#LANGUAGE_TAG_LOCALE_CONVERTER converting}
   * the lang attribute.
   * @see Attributes#getValue(String, String)
   * @see I18nUtil#LANGUAGE_TAG_LOCALE_CONVERTER
   */
    public static final Locale getXMLLangAttr(final Attributes attributes, final Locale defaultLocale) throws ConversionException {
        if ((attributes == null) || (attributes.getLength() == 0)) return defaultLocale;
        final String xmlLangAttrValue = attributes.getValue(XMLConstants.XML_NS_URI, "lang");
        return (xmlLangAttrValue != null) ? ConversionUtil.invokeConverter(I18nUtil.LANGUAGE_TAG_LOCALE_CONVERTER, xmlLangAttrValue) : defaultLocale;
    }

    /**
   * Fix bugs in {@link org.xml.sax.SAXException}.
   */
    public static class SAXException extends org.xml.sax.SAXException {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * {@link org.xml.sax.SAXException#getCause()} returns it's own exception member, but doesn't override
     * {@link Throwable#initCause(Throwable)} to update it, so we have to override both to fix it.
     */
        private Exception cause = this;

        /**
     * Construct a new <code>SAXException</code>.
     * 
     * @param message The exception {@linkplain org.xml.sax.SAXException#getMessage() message}.
     */
        public SAXException(final String message) {
            super(message);
            return;
        }

        /**
     * Construct a new <code>SAXException</code>.
     * 
     * @param cause The exception {@linkplain #getCause() cause}.
     */
        public SAXException(final Exception cause) {
            super(cause);
            if (cause != null) initCause(cause);
            return;
        }

        /**
     * Construct a new <code>SAXException</code>.
     * 
     * @param message The exception {@linkplain org.xml.sax.SAXException#getMessage() message}.
     * @param cause The exception {@linkplain #getCause() cause}.
     */
        public SAXException(final String message, final Exception cause) {
            super(message, cause);
            if (cause != null) initCause(cause);
            return;
        }

        @Override
        public Throwable getCause() {
            return (cause == this) ? null : cause;
        }

        @Override
        public synchronized Throwable initCause(final Throwable cause) throws IllegalStateException, IllegalArgumentException {
            super.initCause(cause);
            this.cause = (Exception) cause;
            return cause;
        }

        @Override
        public String toString() {
            final String message = getMessage();
            return (message != null) ? (getClass().getName() + ": " + message) : getClass().getName();
        }
    }

    /**
   * This class extends {@link Exception} by adding an {@link ErrorHandler} implementation which will store
   * {@linkplain ErrorHandler#warning(SAXParseException) warnings}, {@linkplain ErrorHandler#error(SAXParseException)
   * errors}, and {@linkplain ErrorHandler#fatalError(SAXParseException) fatalErrors}, and (optionally) throw itself
   * (wrapped in a {@link org.xml.sax.SAXException}) in those situations.
   */
    public static class SAXErrorHandlerException extends SAXException implements ErrorHandler, LogAnnotation.Message {

        /**
     * @see Serializable
     */
        private static final long serialVersionUID = 1L;

        /**
     * Should parsing be discontinued by throwing this {@link org.xml.sax.SAXException} in the case of
     * {@linkplain #warning(SAXParseException) warnings}?
     */
        protected final boolean throwWarnings;

        /**
     * Should parsing be discontinued by throwing this {@link org.xml.sax.SAXException} in the case of
     * {@linkplain #error(SAXParseException) errors}?
     */
        protected final boolean throwErrors;

        /**
     * Should parsing be discontinued by throwing this {@link org.xml.sax.SAXException} in the case of
     * {@linkplain #fatalError(SAXParseException) fatal errors}?
     */
        protected final boolean throwFatalErrors;

        /**
     * @see #getLogMessageAnnotations()
     */
        protected final SortedMap<Level, List<Map.Entry<String, Object>>> annotations = new TreeMap<Level, List<Map.Entry<String, Object>>>(LogAnnotation.LEVEL_COMPARATOR);

        /**
     * Construct a new <code>SAXErrorHandlerException</code>.
     * 
     * @param throwWarnings Should parsing be discontinued by throwing this {@link org.xml.sax.SAXException} in the case
     * of {@linkplain #warning(SAXParseException) warnings}?
     * @param throwErrors Should parsing be discontinued by throwing this {@link org.xml.sax.SAXException} in the case
     * of {@linkplain #error(SAXParseException) errors}?
     * @param throwFatalErrors Should parsing be discontinued by throwing this {@link org.xml.sax.SAXException} in the
     * case of {@linkplain #fatalError(SAXParseException) fatal errors}?
     * @param inputSource Error messages will be constructed using this {@link InputSource#getPublicId()} for
     * referencing the {@link InputSource} resource being parsed.
     */
        public SAXErrorHandlerException(final boolean throwWarnings, final boolean throwErrors, final boolean throwFatalErrors, final InputSource inputSource) {
            super(SAXErrorHandlerException.class.getSimpleName(), null);
            this.throwWarnings = throwWarnings;
            this.throwErrors = throwErrors;
            this.throwFatalErrors = throwFatalErrors;
            if (inputSource != null) LogAnnotation.annotate(this, "source", inputSource.getPublicId(), Level.OFF, false);
            return;
        }

        @Override
        public void warning(final SAXParseException exception) throws org.xml.sax.SAXException {
            final boolean exceptionCauseIsThis = MiscUtil.getCause(exception, SAXErrorHandlerException.class) == this;
            if (!exceptionCauseIsThis) LogAnnotation.annotate(this, "warning", exception, Level.WARNING, true);
            if (throwWarnings) {
                if (!exceptionCauseIsThis) initCause(exception);
                if (exceptionCauseIsThis) throw exception;
                throw new SAXException(null, this);
            }
            return;
        }

        @Override
        public void error(final SAXParseException exception) throws org.xml.sax.SAXException {
            final boolean exceptionCauseIsThis = MiscUtil.getCause(exception, SAXErrorHandlerException.class) == this;
            if (!exceptionCauseIsThis) LogAnnotation.annotate(this, "error", exception, Level.SEVERE, true);
            if (throwErrors) {
                if (!exceptionCauseIsThis) initCause(exception);
                if (exceptionCauseIsThis) throw exception;
                throw new SAXException(null, this);
            }
            return;
        }

        @Override
        public void fatalError(final SAXParseException exception) throws org.xml.sax.SAXException {
            final boolean exceptionCauseIsThis = MiscUtil.getCause(exception, SAXErrorHandlerException.class) == this;
            if (!exceptionCauseIsThis) LogAnnotation.annotate(this, "fatal", exception, Level.SEVERE, true);
            if (throwFatalErrors) {
                if (!exceptionCauseIsThis) initCause(exception);
                if (exceptionCauseIsThis) throw exception;
                throw new SAXException(null, this);
            }
            return;
        }

        @Override
        public String getMessage() {
            return LogAnnotation.toString(this, Level.INFO);
        }

        @Override
        public String getAnnotatedLogMessage() {
            return super.getMessage();
        }

        @Override
        public Level getAnnotatedLogMessageLevel() {
            return null;
        }

        @Override
        public SortedMap<Level, List<Map.Entry<String, Object>>> getLogMessageAnnotations() {
            return annotations;
        }
    }
}
