package com.sun.org.apache.xerces.internal.parsers;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager;
import com.sun.org.apache.xerces.internal.impl.XMLErrorReporter;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarLoader;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarPool;
import com.sun.org.apache.xerces.internal.xni.parser.XMLEntityResolver;
import com.sun.org.apache.xerces.internal.xni.parser.XMLErrorHandler;
import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;

/**
 * <p> This class provides an easy way for a user to preparse grammars
 * of various types.  By default, it knows how to preparse external
 * DTD's and schemas; it provides an easy way for user applications to
 * register classes that know how to parse additional grammar types.
 * By default, it does no grammar caching; but it provides ways for
 * user applications to do so.
 *
 * @author Neil Graham, IBM
 *
 * @version $Id: XMLGrammarPreparser.java,v 1.2.6.1 2005/09/08 04:03:52 sunithareddy Exp $
 */
public class XMLGrammarPreparser {

    private static final String CONTINUE_AFTER_FATAL_ERROR = Constants.XERCES_FEATURE_PREFIX + Constants.CONTINUE_AFTER_FATAL_ERROR_FEATURE;

    /** Property identifier: symbol table. */
    protected static final String SYMBOL_TABLE = Constants.XERCES_PROPERTY_PREFIX + Constants.SYMBOL_TABLE_PROPERTY;

    /** Property identifier: error reporter. */
    protected static final String ERROR_REPORTER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_REPORTER_PROPERTY;

    /** Property identifier: error handler. */
    protected static final String ERROR_HANDLER = Constants.XERCES_PROPERTY_PREFIX + Constants.ERROR_HANDLER_PROPERTY;

    /** Property identifier: entity resolver. */
    protected static final String ENTITY_RESOLVER = Constants.XERCES_PROPERTY_PREFIX + Constants.ENTITY_RESOLVER_PROPERTY;

    /** Property identifier: grammar pool . */
    protected static final String GRAMMAR_POOL = Constants.XERCES_PROPERTY_PREFIX + Constants.XMLGRAMMAR_POOL_PROPERTY;

    private static final Hashtable KNOWN_LOADERS = new Hashtable();

    static {
        KNOWN_LOADERS.put(XMLGrammarDescription.XML_SCHEMA, "com.sun.org.apache.xerces.internal.impl.xs.XMLSchemaLoader");
        KNOWN_LOADERS.put(XMLGrammarDescription.XML_DTD, "com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDLoader");
    }

    /** Recognized properties. */
    private static final String[] RECOGNIZED_PROPERTIES = { SYMBOL_TABLE, ERROR_REPORTER, ERROR_HANDLER, ENTITY_RESOLVER, GRAMMAR_POOL };

    protected SymbolTable fSymbolTable;

    protected XMLErrorReporter fErrorReporter;

    protected XMLEntityResolver fEntityResolver;

    protected XMLGrammarPool fGrammarPool;

    protected Locale fLocale;

    private Hashtable fLoaders;

    /** Default constructor. */
    public XMLGrammarPreparser() {
        this(new SymbolTable());
    }

    /**
     * Constructs a preparser using the specified symbol table.
     *
     * @param symbolTable The symbol table to use.
     */
    public XMLGrammarPreparser(SymbolTable symbolTable) {
        fSymbolTable = symbolTable;
        fLoaders = new Hashtable();
        setLocale(Locale.getDefault());
        fErrorReporter = new XMLErrorReporter();
        fErrorReporter.setLocale(fLocale);
        fEntityResolver = new XMLEntityManager();
    }

    public boolean registerPreparser(String grammarType, XMLGrammarLoader loader) {
        if (loader == null) {
            if (KNOWN_LOADERS.containsKey(grammarType)) {
                String loaderName = (String) KNOWN_LOADERS.get(grammarType);
                try {
                    ClassLoader cl = ObjectFactory.findClassLoader();
                    XMLGrammarLoader gl = (XMLGrammarLoader) (ObjectFactory.newInstance(loaderName, cl, true));
                    fLoaders.put(grammarType, gl);
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
            return false;
        }
        fLoaders.put(grammarType, loader);
        return true;
    }

    /**
     * Parse a grammar from a location identified by an
     * XMLInputSource.
     * This method also adds this grammar to the XMLGrammarPool
     *
     * @param type The type of the grammar to be constructed
     * @param is The XMLInputSource containing this grammar's
     * information
     * <strong>If a URI is included in the systemId field, the parser will not expand this URI or make it
     * available to the EntityResolver</strong>
     * @return The newly created <code>Grammar</code>.
     * @exception XNIException thrown on an error in grammar
     * construction
     * @exception IOException thrown if an error is encountered
     * in reading the file
     */
    public Grammar preparseGrammar(String type, XMLInputSource is) throws XNIException, IOException {
        if (fLoaders.containsKey(type)) {
            XMLGrammarLoader gl = (XMLGrammarLoader) fLoaders.get(type);
            gl.setProperty(SYMBOL_TABLE, fSymbolTable);
            gl.setProperty(ENTITY_RESOLVER, fEntityResolver);
            gl.setProperty(ERROR_REPORTER, fErrorReporter);
            if (fGrammarPool != null) {
                try {
                    gl.setProperty(GRAMMAR_POOL, fGrammarPool);
                } catch (Exception e) {
                }
            }
            return gl.loadGrammar(is);
        }
        return null;
    }

    /**
     * Set the locale to use for messages.
     *
     * @param locale The locale object to use for localization of messages.
     *
     * @exception XNIException Thrown if the parser does not support the
     *                         specified locale.
     */
    public void setLocale(Locale locale) {
        fLocale = locale;
    }

    /** Return the Locale the XMLGrammarLoader is using. */
    public Locale getLocale() {
        return fLocale;
    }

    /**
     * Sets the error handler.
     *
     * @param errorHandler The error handler.
     */
    public void setErrorHandler(XMLErrorHandler errorHandler) {
        fErrorReporter.setProperty(ERROR_HANDLER, errorHandler);
    }

    /** Returns the registered error handler.  */
    public XMLErrorHandler getErrorHandler() {
        return fErrorReporter.getErrorHandler();
    }

    /**
     * Sets the entity resolver.
     *
     * @param entityResolver The new entity resolver.
     */
    public void setEntityResolver(XMLEntityResolver entityResolver) {
        fEntityResolver = entityResolver;
    }

    /** Returns the registered entity resolver.  */
    public XMLEntityResolver getEntityResolver() {
        return fEntityResolver;
    }

    /**
     * Sets the grammar pool.
     *
     * @param grammarPool The new grammar pool.
     */
    public void setGrammarPool(XMLGrammarPool grammarPool) {
        fGrammarPool = grammarPool;
    }

    /** Returns the registered grammar pool.  */
    public XMLGrammarPool getGrammarPool() {
        return fGrammarPool;
    }

    public XMLGrammarLoader getLoader(String type) {
        return (XMLGrammarLoader) fLoaders.get(type);
    }

    public void setFeature(String featureId, boolean value) {
        Enumeration loaders = fLoaders.elements();
        while (loaders.hasMoreElements()) {
            XMLGrammarLoader gl = (XMLGrammarLoader) loaders.nextElement();
            try {
                gl.setFeature(featureId, value);
            } catch (Exception e) {
            }
        }
        if (featureId.equals(CONTINUE_AFTER_FATAL_ERROR)) {
            fErrorReporter.setFeature(CONTINUE_AFTER_FATAL_ERROR, value);
        }
    }

    public void setProperty(String propId, Object value) {
        Enumeration loaders = fLoaders.elements();
        while (loaders.hasMoreElements()) {
            XMLGrammarLoader gl = (XMLGrammarLoader) loaders.nextElement();
            try {
                gl.setProperty(propId, value);
            } catch (Exception e) {
            }
        }
    }

    public boolean getFeature(String type, String featureId) {
        XMLGrammarLoader gl = (XMLGrammarLoader) fLoaders.get(type);
        return gl.getFeature(featureId);
    }

    public Object getProperty(String type, String propertyId) {
        XMLGrammarLoader gl = (XMLGrammarLoader) fLoaders.get(type);
        return gl.getProperty(propertyId);
    }
}
