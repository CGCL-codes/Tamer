package org.apache.catalina.startup;

import java.net.URL;
import org.apache.catalina.util.SchemaResolver;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.RuleSet;

/**
 * Wrapper class around the Digester that hide Digester's initialization details
 *
 * @author Jean-Francois Arcand
 */
public class DigesterFactory {

    /**
     * The log.
     */
    protected static org.apache.juli.logging.Log log = org.apache.juli.logging.LogFactory.getLog(DigesterFactory.class);

    /**
     * The XML entiry resolver used by the Digester.
     */
    private static SchemaResolver schemaResolver;

    /**
     * Create a <code>Digester</code> parser with no <code>Rule</code>
     * associated and XML validation turned off.
     */
    public static Digester newDigester() {
        return newDigester(false, false, null);
    }

    /**
     * Create a <code>Digester</code> parser with XML validation turned off.
     * @param rule an instance of <code>RuleSet</code> used for parsing the xml.
     */
    public static Digester newDigester(RuleSet rule) {
        return newDigester(false, false, rule);
    }

    /**
     * Create a <code>Digester</code> parser.
     * @param xmlValidation turn on/off xml validation
     * @param xmlNamespaceAware turn on/off namespace validation
     * @param rule an instance of <code>RuleSet</code> used for parsing the xml.
     */
    public static Digester newDigester(boolean xmlValidation, boolean xmlNamespaceAware, RuleSet rule) {
        Digester digester = new Digester();
        digester.setNamespaceAware(xmlNamespaceAware);
        digester.setValidating(xmlValidation);
        digester.setUseContextClassLoader(true);
        if (xmlValidation || xmlNamespaceAware) {
            configureSchema(digester);
        }
        schemaResolver = new SchemaResolver(digester);
        registerLocalSchema();
        digester.setEntityResolver(schemaResolver);
        if (rule != null) {
            digester.addRuleSet(rule);
        }
        return (digester);
    }

    /**
     * Utilities used to force the parser to use local schema, when available,
     * instead of the <code>schemaLocation</code> XML element.
     */
    protected static void registerLocalSchema() {
        register(Constants.J2eeSchemaResourcePath_14, Constants.J2eeSchemaPublicId_14);
        register(Constants.W3cSchemaResourcePath_10, Constants.W3cSchemaPublicId_10);
        register(Constants.JspSchemaResourcePath_20, Constants.JspSchemaPublicId_20);
        register(Constants.JspSchemaResourcePath_21, Constants.JspSchemaPublicId_21);
        register(Constants.TldDtdResourcePath_11, Constants.TldDtdPublicId_11);
        register(Constants.TldDtdResourcePath_12, Constants.TldDtdPublicId_12);
        register(Constants.TldSchemaResourcePath_20, Constants.TldSchemaPublicId_20);
        register(Constants.TldSchemaResourcePath_21, Constants.TldSchemaPublicId_21);
        register(Constants.WebDtdResourcePath_22, Constants.WebDtdPublicId_22);
        register(Constants.WebDtdResourcePath_23, Constants.WebDtdPublicId_23);
        register(Constants.WebSchemaResourcePath_24, Constants.WebSchemaPublicId_24);
        register(Constants.WebSchemaResourcePath_25, Constants.WebSchemaPublicId_25);
        register(Constants.J2eeWebServiceSchemaResourcePath_11, Constants.J2eeWebServiceSchemaPublicId_11);
        register(Constants.J2eeWebServiceClientSchemaResourcePath_11, Constants.J2eeWebServiceClientSchemaPublicId_11);
    }

    /**
     * Load the resource and add it to the resolver.
     */
    protected static void register(String resourceURL, String resourcePublicId) {
        URL url = DigesterFactory.class.getResource(resourceURL);
        if (url == null) {
            log.warn("Could not get url for " + resourceURL);
        } else {
            schemaResolver.register(resourcePublicId, url.toString());
        }
    }

    /**
     * Turn on DTD and/or validation (based on the parser implementation)
     */
    protected static void configureSchema(Digester digester) {
        URL url = DigesterFactory.class.getResource(Constants.WebSchemaResourcePath_24);
        if (url == null) {
            log.error("Could not get url for " + Constants.WebSchemaResourcePath_24);
        } else {
            digester.setSchema(url.toString());
        }
    }
}
