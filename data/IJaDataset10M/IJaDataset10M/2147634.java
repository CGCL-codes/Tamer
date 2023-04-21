package org.jato.transform;

import java.io.IOException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.TemplatesHandler;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.SAXException;
import org.jdom.JDOMException;
import org.jdom.input.SAXHandler;
import org.jdom.transform.JDOMSource;
import org.jdom.transform.JDOMResult;
import org.jato.ScriptBuilder;
import org.jato.JatoException;

/**
 * A TransformerFactory instance is used to create Transformer and
 * Templates objects.
 * <p>
 * The <code>JatoTransformerFactory</code> implements the JAXP TrAX
 * {@link TransformerFactory} API to allow considering Jato as just
 * another XML transformation tool.</p>
 * <p>
 * Please note that as Jato does not aim at performing "pure" XSLT
 * transformations, this implementation does not support some of
 * the TrAX API features too specific to XSLT.</p>
 * <p>
 * Refer to the <a href="package-summary.html#usage">user manual</a>
 * for a detailed description of how to use this JAXP TrAX
 * implementation.</p>
 *
 * @author Laurent Bihanic
 * @author Andy Krumel
 */
public class JatoTransformerFactory extends SAXTransformerFactory {

    /**
    * If {@link TransformerFactory#getFeature} returns true when
    * passed this value as an argument, the TransformerFactory
    * returned from {@link TransformerFactory#newInstance} may
    * be safely cast to a JatoTransformerFactory.
    */
    public static final String FEATURE = "http://org.jato.transform.JatoTransformerFactory/feature";

    /**
    * The object that will be used to resolve URIs used in
    * xsl:import, etc.  This will be used as the default for the
    * transformations.
    */
    private URIResolver uriResolver = null;

    /**
    * The error listener.
    */
    private ErrorListener errorListener = new DefaultErrorListener();

    /**
    * Default constructor.
    *
    * @throws JatoException   if the loading of Jato default
    *                         definitions failed.
    */
    public JatoTransformerFactory() throws JatoException {
        super();
        try {
            ScriptBuilder.loadDefaultJatoDefs();
        } catch (Exception ex1) {
            throw (new JatoException(ex1.getMessage(), ex1));
        }
    }

    /**
    * Returns a new instance of a <code>TransformerFactory</code>.
    * This static method creates a new Jato factory instance.
    * <p>
    * Once an application has obtained a reference to a
    * <code>TransformerFactory</code> it can use the factory to
    * configure and obtain transformer and templates instances.</p>
    *
    * @return a new TransformerFactory instance, never
    *         <code>null</code>.
    *
    * @throws TransformerFactoryConfigurationError   if the
    *         implementation cannot be instantiated.
    */
    public static TransformerFactory newInstance() throws TransformerFactoryConfigurationError {
        try {
            return (new JatoTransformerFactory());
        } catch (Exception ex2) {
            throw (new TransformerFactoryConfigurationError(ex2, "Failed to initialize JatoTransformerFactory"));
        }
    }

    /**
    * Gets the stylesheet specification(s) associated via the
    * <a href="http://www.w3.org/TR/xml-stylesheet/">xml-stylesheet</a>
    * processing instruction with the document specified in the
    * source parameter, and that match the given criteria.
    * <p>
    * <strong>Warning</strong>: As Jato is not aimed at performing
    * pure XSLT transformations, this implementation always throws
    * a {@link TransformerConfigurationException} signalling that
    * this feature is not supported.</p>
    *
    * @param  source    the XML source document.
    * @param  media     the media attribute to be matched.  May be
    *                   <code>null</code>, in which case the
    *                   preferred templates will be used.
    * @param  title     the value of the title attribute to
    *                   match.  May be <code>null</code>.
    * @param  charset   the value of the charset attribute to
    *                   match.  May be <code>null</code>.
    *
    * @return <i>Not applicable: see below</i>.
    *
    * @throws TransformerConfigurationException   always: this feature
    *                                             is not supported.
    */
    public Source getAssociatedStylesheet(Source source, String media, String title, String charset) throws TransformerConfigurationException {
        throw (new TransformerConfigurationException("Feature not supported"));
    }

    /**
    * Looks up the value of a feature.
    * <p>
    * The feature name is any fully-qualified URI.  It is
    * possible for an TransformerFactory to recognize a feature name
    * but to be unable to return its value.</p>
    *
    * @param  name   the feature name, which is a fully-qualified URI.
    *
    * @return the current state of the feature (<code>true</code> or
    *         <code>false</code>).
    */
    public boolean getFeature(String name) {
        if ((DOMResult.FEATURE == name) || (DOMSource.FEATURE == name) || (SAXResult.FEATURE == name) || (SAXSource.FEATURE == name) || (StreamResult.FEATURE == name) || (StreamSource.FEATURE == name) || (JDOMResult.FEATURE == name) || (JDOMSource.FEATURE == name) || (AbstractJatoResult.FEATURE == name) || (AbstractJatoSource.FEATURE == name) || (JatoTransformerFactory.FEATURE == name) || (SAXTransformerFactory.FEATURE == name) || (SAXTransformerFactory.FEATURE_XMLFILTER == name)) {
            return (true);
        } else {
            if ((DOMResult.FEATURE.equals(name)) || (DOMSource.FEATURE.equals(name)) || (SAXResult.FEATURE.equals(name)) || (SAXSource.FEATURE.equals(name)) || (StreamResult.FEATURE.equals(name)) || (StreamSource.FEATURE.equals(name)) || (JDOMResult.FEATURE.equals(name)) || (JDOMSource.FEATURE.equals(name)) || (AbstractJatoResult.FEATURE.equals(name)) || (AbstractJatoSource.FEATURE.equals(name)) || (JatoTransformerFactory.FEATURE.equals(name)) || (SAXTransformerFactory.FEATURE.equals(name)) || (SAXTransformerFactory.FEATURE_XMLFILTER.equals(name))) {
                return (true);
            } else {
                return (false);
            }
        }
    }

    /**
    * Allows the user to set specific attributes on the underlying
    * implementation.
    * <p>
    * <strong>Warning</strong>: this implementation always throws
    * an {@link java.lang.IllegalArgumentException} as it does not
    * support any implementation-specific attribute.</p>
    *
    * @param  name    the name of the attribute.
    * @param  value   the value of the attribute.
    *
    * @throws IllegalArgumentException   always: this implementation
    *         does not support any implementation-specific attribute.
    */
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
        throw (new IllegalArgumentException("Feature not supported"));
    }

    /**
    * Allows the user to retrieve specific attributes on the
    * underlying implementation.
    * <p>
    * <strong>Warning</strong>: this implementation always throws
    * an {@link java.lang.IllegalArgumentException} as it does not
    * support any implementation-specific attribute.</p>
    *
    * @param  name    the name of the attribute.
    *
    * @return <i>Not applicable: see below</i>.
    *
    * @throws IllegalArgumentException   always: this implementation
    *         does not support any implementation-specific attribute.
    */
    public Object getAttribute(String name) throws IllegalArgumentException {
        throw (new IllegalArgumentException("Feature not supported"));
    }

    /**
    * Processes the source into a Transformer object.  Care must
    * be given to know that this object can not be used concurrently
    * in multiple threads when performing Jato debugging.
    *
    * @param  source   an object that holds a URL, input stream, etc.
    *
    * @return a Transformer object capable of being used for
    *         transformation purposes in a single thread.
    *
    * @throws TransformerConfigurationException   if the construction
    *         of the Transformer failed.
    */
    public Transformer newTransformer(Source source) throws TransformerConfigurationException {
        Transformer transformer = null;
        Templates templates = newTemplates(source);
        if (templates != null) {
            try {
                transformer = templates.newTransformer();
            } catch (Exception ex1) {
                this.handleFatalError(ex1);
            }
        }
        return (transformer);
    }

    /**
    * Creates a new Transformer object that performs a copy
    * of the source to the result.
    *
    * @return a Transformer object that may be used to perform a
    *         transformation in a single thread.
    *
    * @throws TransformerConfigurationException   if the construction
    *         of the Transformer failed.
    */
    public Transformer newTransformer() throws TransformerConfigurationException {
        return (new JatoTransformer(null, this.getURIResolver()));
    }

    /**
    * Processes the source into a Templates object, which is likely
    * a compiled representation of the source.  This Templates
    * object may then be used concurrently across multiple
    * threads.  Creating a Templates object allows the
    * TransformerFactory to do detailed performance optimization of
    * transformation instructions, without penalizing runtime
    * transformation.
    *
    * @param  source   an object that holds a URL, input stream, etc.
    *
    * @return a Templates object capable of being used for
    *         transformation purposes.
    *
    * @throws TransformerConfigurationException   if the construction
    *         of the Templates failed.
    */
    public Templates newTemplates(Source source) throws TransformerConfigurationException {
        Templates templates = null;
        try {
            templates = new JatoTemplates(source, this.getURIResolver());
        } catch (Exception ex1) {
            this.handleFatalError(ex1);
        }
        return (templates);
    }

    /**
    * Sets an object that will be used to resolve URIs used in
    * xsl:import, etc.  This will be used as the default for the
    * transformation.
    *
    * @param  resolver   an object that implements the URIResolver
    *                    interface, or <code>null</code>.
    */
    public void setURIResolver(URIResolver resolver) {
        this.uriResolver = resolver;
    }

    /**
    * Returns the object that will be used to resolve URIs used in
    * xsl:import, etc.  This will be used as the default for the
    * transformation.
    *
    * @return the URIResolver that was set with
    *         {@link #setURIResolver} or <code>null</code>.
    */
    public URIResolver getURIResolver() {
        return (this.uriResolver);
    }

    /**
    * Returns the error listener in effect for the
    * TransformerFactory.
    *
    * @return a non-null reference to an error listener.
    */
    public ErrorListener getErrorListener() {
        return (this.errorListener);
    }

    /**
    * Sets an error listener for the TransformerFactory.
    *
    * @param  listener  a <i>non-null</i> reference to an
    *                   ErrorListener.
    *
    * @throws IllegalArgumentException   if the listener argument
    *                                    is <code>null</code>.
    */
    public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {
        if (listener == null) {
            throw (new IllegalArgumentException("listener"));
        }
        this.errorListener = listener;
        return;
    }

    /**
    * Returns a TransformerHandler object that can process SAX
    * ContentHandler events into a Result, based on the
    * transformation instructions specified by the argument.
    * <p>
    * The returned TransformerHandler can safely be typecasted to
    * {@link JatoTransformerHandler} for the developer to access
    * the Jato-specific methods offered by this implementation.</p>
    *
    * @param  src   the Source of the transformation instructions.
    *
    * @return a JatoTransformerHandler instance ready to transform
    *         SAX events.
    *
    * @throws TransformerConfigurationException   if for some reason
    *         the TransformerHandler can not be created.
    */
    public TransformerHandler newTransformerHandler(Source src) throws TransformerConfigurationException {
        TransformerHandler handler = null;
        Templates templates = newTemplates(src);
        if (templates != null) {
            handler = this.newTransformerHandler(templates);
        }
        return (handler);
    }

    /**
    * Returns a TransformerHandler object that can process SAX
    * ContentHandler events into a Result, based on the Templates
    * argument.
    * <p>
    * The returned TransformerHandler can safely be typecasted to
    * {@link JatoTransformerHandler} for the developer to access
    * the Jato-specific methods offered by this implementation.</p>
    *
    * @param  templates   the compiled transformation instructions.
    *
    * @return a JatoTransformerHandler instance ready to transform
    *         SAX events.
    *
    * @throws TransformerConfigurationException   if for some reason
    *         the TransformerHandler can not be created.
    */
    public TransformerHandler newTransformerHandler(Templates templates) throws TransformerConfigurationException {
        TransformerHandler handler = null;
        if ((templates == null) || ((templates instanceof JatoTemplates) == false)) {
            throw (new IllegalArgumentException("Invalid templates: Jato templates expected"));
        }
        try {
            handler = new JatoTransformerHandler(templates.newTransformer());
        } catch (Exception ex1) {
            this.handleFatalError(ex1);
        }
        return (handler);
    }

    /**
    * Returns a TransformerHandler object that can process SAX
    * ContentHandler events into a Result.  The transformation
    * is defined as an identity (or copy) transformation, for
    * example to copy a series of SAX parse events into a DOM tree.
    * <p>
    * As Jato is not aimed at performing pure XSLT transformations,
    * this implementation always throws a
    * {@link TransformerConfigurationException} signalling that this
    * feature is not supported.</p>
    *
    * @return a JatoTransformerHandler instance ready to transform
    *         SAX events.
    *
    * @throws TransformerConfigurationException   if for some reason
    *         the TransformerHandler can not be created.
    */
    public TransformerHandler newTransformerHandler() throws TransformerConfigurationException {
        TransformerHandler handler = null;
        try {
            handler = new JatoTransformerHandler(this.newTransformer());
        } catch (Exception ex1) {
            this.handleFatalError(ex1);
        }
        return (handler);
    }

    /**
    * Returns a TemplatesHandler object that can process SAX
    * ContentHandler events into a Templates object.
    *
    * @return a non-null reference to a TransformerHandler, that may
    *         be used as a ContentHandler for SAX parse events.
    *
    * @throws TransformerConfigurationException   if for some reason
    *         the TemplatesHandler cannot be created.
    */
    public TemplatesHandler newTemplatesHandler() throws TransformerConfigurationException {
        TemplatesHandler handler = null;
        try {
            handler = new TemplatesHandlerImpl();
        } catch (Exception ex1) {
            this.handleFatalError(ex1);
        }
        return (handler);
    }

    /**
    * Creates an XMLFilter that uses the given Source as the
    * transformation instructions.
    * <p>
    * The returned XMLFilter can safely be typecasted to
    * {@link JatoTransformFilter} for the developer to access
    * the Jato-specific methods offered by this implementation.</p>
    *
    * @param  src   the Source of the transformation instructions.
    *
    * @return a JatoTransformFilter instance.
    *
    * @throws TransformerConfigurationException   if for some reason
    *         the filter cannot be created.
    */
    public XMLFilter newXMLFilter(Source src) throws TransformerConfigurationException {
        XMLFilter filter = null;
        Templates templates = newTemplates(src);
        if (templates != null) {
            filter = newXMLFilter(templates);
        }
        return (filter);
    }

    /**
    * Create an XMLFilter, based on the Templates argument.
    * <p>
    * The returned XMLFilter can safely be typecasted to
    * {@link JatoTransformFilter} for the developer to access
    * the Jato-specific methods offered by this implementation.</p>
    *
    * @param  templates   the compiled transformation instructions.
    *
    * @return a JatoTransformFilter instance.
    *
    * @throws TransformerConfigurationException   if for some reason
    *         the filter cannot be created.
    */
    public XMLFilter newXMLFilter(Templates templates) throws TransformerConfigurationException {
        XMLFilter filter = null;
        if ((templates == null) || ((templates instanceof JatoTemplates) == false)) {
            throw (new IllegalArgumentException("Invalid templates: Jato templates expected"));
        }
        try {
            filter = new JatoTransformFilter(templates.newTransformer());
        } catch (Exception ex1) {
            this.handleFatalError(ex1);
        }
        return (filter);
    }

    /**
    * Handles the exceptions thrown during the contruction of
    * Templates and Transformers.
    * <p>
    * The error is wrapped into a <code>TransformerException</code>
    * prior notifying the error listener.  If the error listener
    * throws an exception, it is wrapped into a
    * <code>TransformerConfigurationException</code> before being
    * re-thrown.  Otherwise, the original exception is wrapped and
    * re-thrown (this implementation does not allow error listeners
    * to absorb fatal errors).</p>
    *
    * @param  error   an exception caught during a build process.
    *
    * @throws TransformerConfigurationException   to report to the
    *                                             caller application.
    */
    private void handleFatalError(Exception error) throws TransformerConfigurationException {
        try {
            if (error instanceof TransformerException) {
                this.errorListener.fatalError((TransformerException) error);
            } else {
                this.errorListener.fatalError(new TransformerException(error));
            }
            if (error instanceof TransformerConfigurationException) {
                throw (error);
            } else {
                throw (new TransformerConfigurationException(error));
            }
        } catch (TransformerConfigurationException ex1) {
            throw (ex1);
        } catch (Exception ex2) {
            throw (new TransformerConfigurationException(ex2));
        }
    }

    /**
    * An implementation of the TemplatesHandler interface based on
    * JDOM's SAXHandler implementation of ContentHandler,
    * LexicalHandler...
    */
    private class TemplatesHandlerImpl extends SAXHandler implements TemplatesHandler {

        /**
       * The built Templates object or <code>null</code> is no
       * templates is available yet (parsing no begun or ongoing).
       */
        private Templates templates = null;

        /**
       * The base ID (URI or system ID) for resolving relative URIs.
       * <p>
       * <b>Unused.</b></p>
       */
        private String baseSystemId = null;

        /**
       * Default constructor.
       *
       * @throws IOEXception   if thrown by the superclass
       *                       constructor.
       */
        public TemplatesHandlerImpl() throws IOException {
            super();
        }

        /**
       * When a TemplatesHandler object is used as a ContentHandler
       * for the parsing of transformation instructions, it creates
       * a Templates object, which the caller can get once the SAX
       * events have been completed.
       *
       * @return the Templates object that was created during the
       *         SAX event process, or <code>null</code> if no
       *         Templates object has been created.
       */
        public Templates getTemplates() {
            return (this.templates);
        }

        /**
       * Sets the base ID (URI or system ID) for the Templates
       * object created by this builder.  This must be set in order
       * to resolve relative URIs in the stylesheet.  This must be
       * called before the startDocument event.
       *
       * @param  baseID   base URI for this stylesheet.
       */
        public void setSystemId(String systemID) {
            this.baseSystemId = systemID;
        }

        /**
       * Returns the base ID (URI or system ID) from where relative
       * URLs will be resolved.
       *
       * @return the systemID that was set with {@link #setSystemId}.
       */
        public String getSystemId() {
            return (this.baseSystemId);
        }

        /**
       * Receives notification of the beginning of a document.
       * <p>
       * This implementation resets the current Templates object.</p>
       *
       * @throws SAXException   any SAX exception, possibly wrapping
       *                        another exception.
       */
        public void startDocument() throws SAXException {
            this.templates = null;
            super.startDocument();
        }

        /**
       * Receives notification of the end of a document.
       * <p>
       * This implementation builds a new Templates object from the
       * parsed XML document.</p>
       *
       * @throws SAXException   any SAX exception, possibly wrapping
       *                        another exception.
       */
        public void endDocument() throws SAXException {
            super.endDocument();
            try {
                this.templates = newTemplates(new JDOMSource(this.getDocument()));
            } catch (Exception ex1) {
                throw (new SAXException("Failed to create Templates", ex1));
            }
            return;
        }
    }
}
