package org.vqwiki.lex;

import java.io.IOException;
import java.util.Properties;
import java.security.AccessControlException;
import org.apache.log4j.Logger;
import org.vqwiki.Environment;

/**
 * Manager for lex extensions. Lex extensions are persisted as entries in a file at
 * WEB-INF/classes/externallex.properties. They are triggered by the special markup:
 * [&lt;tagname&gt;][&lt;/tagname&gt;] where tagname is the name of the external lex
 * to use process the content between the tags. The entry in this manager maps this name
 * to an implementation class of {@link ExternalLex} that is used to process the text.
 *
 * @author garethc
 *         Date: Jan 6, 2003
 */
public class LexExtender {

    /** Logger */
    private static final Logger logger = Logger.getLogger(LexExtender.class);

    /** Properties file to persist in */
    public static final String LEX_EXTENDER_PROPERTIES_FILE = "/externallex.properties";

    /** Singleon instance of the extender */
    public static LexExtender instance;

    /** The pairs of name to class mappings */
    private Properties properties;

    static {
        instance = new LexExtender();
    }

    /**
     * Hide constructor - singleton
     */
    private LexExtender() {
        this.properties = Environment.loadProperties(LEX_EXTENDER_PROPERTIES_FILE);
    }

    /**
     * Get instance of the extender
     *
     * @return singleton instance
     */
    public static synchronized LexExtender getInstance() {
        return instance;
    }

    /**
     * Process the given contents using the lex class that matches with the tag name
     *
     * @param tagName  tag name == lexer name
     * @param contents contents to transform
     * @return transformed contents
     */
    public String lexify(String tagName, String contents) {
        ExternalLex lexer = null;
        try {
            lexer = getLexerInstance(tagName);
        } catch (ClassNotFoundException e) {
            logger.error("", e);
        } catch (IllegalAccessException e) {
            logger.error("", e);
        } catch (InstantiationException e) {
            logger.error("", e);
        }
        if (lexer == null) {
            return "???" + tagName + "???";
        }
        return lexer.process(contents);
    }

    /**
     * Return the current entries, loading from the persisted file if necessary
     *
     * @return entries
     */
    private Properties getProperties() {
        return this.properties;
    }

    /**
     * Return an instance of the mapped lexer class for the given name
     * @param lexName lexer name
     * @return instance or null if there is no mapping for that name
     * @throws ClassNotFoundException if the class defined for the name can not be found
     * @throws IllegalAccessException on security problem
     * @throws InstantiationException if the lexer can not be instantiated
     */
    public ExternalLex getLexerInstance(String lexName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        String className = getProperties().getProperty(lexName);
        if (className == null) {
            return null;
        }
        Class clazz = Class.forName(className);
        return (ExternalLex) clazz.newInstance();
    }

    /**
     * Add a new lexer entry and persist the mapping
     * @param lexName lexer name
     * @param className class name
     * @throws IOException if the mapping can not be persisted
     */
    public void addLexerEntry(String lexName, String className) throws IOException {
        getProperties().setProperty(lexName, className);
        Environment.saveProperties(LEX_EXTENDER_PROPERTIES_FILE, this.properties, "externallex");
    }
}
