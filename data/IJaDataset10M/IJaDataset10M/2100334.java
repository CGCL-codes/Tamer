package org.npsnet.v.kernel;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import javax.naming.*;
import org.npsnet.v.services.configuration.ConfigurationElementInterpreter;
import org.npsnet.v.services.configuration.ConfigurationElementInterpretationProvider;
import org.npsnet.v.services.configuration.InvalidConfigurationElementException;
import org.npsnet.v.services.persistence.SchemaLocationProvider;
import org.npsnet.v.services.resource.ArchiveDescriptor;
import org.npsnet.v.services.resource.ModuleClassDescriptor;
import org.npsnet.v.services.resource.ResourceArchiveDescriptor;
import org.npsnet.v.services.resource.ResourceManager;
import org.npsnet.v.services.resource.ResourceName;
import org.w3c.dom.*;

/**
 * A configuration element interpreter for the fundamental configuration
 * elements: <code>Module</code>, <code>Container</code>, 
 * <code>Include</code>, <code>Prototype</code>, and <code>Debug</code>.
 *
 * @author Andrzej Kapolka
 */
public class FundamentalConfigurationElementInterpreter implements ConfigurationElementInterpreter {

    /**
     * The XML namespace associated with the fundamental element interpreter.
     */
    private static final String NAMESPACE_URI = "resource:///org/npsnet/v/kernel/FundamentalConfigurationElementInterpreter.class";

    /**
     * The location of the namespace's schema.
     */
    private static final String SCHEMA_URL = "resource:///org/npsnet/v/kernel/FundamentalConfigurationElementInterpreter.xsd";

    /**
     * The module tag.
     */
    private static final String MODULE = "Module";

    /**
     * The generic container tag.
     */
    private static final String CONTAINER = "Container";

    /**
     * The inclusion tag.
     */
    private static final String INCLUDE = "Include";

    /**
     * The prototype tag.
     */
    private static final String PROTOTYPE = "Prototype";

    /**
     * The debug tag.
     */
    private static final String DEBUG = "Debug";

    /**
     * The archive attribute for the module tag.
     */
    private static final String ARCHIVE = "archive";

    /**
     * The class attribute for the module tag.
     */
    private static final String CLASS = "class";

    /**
     * The version attribute for the module tag.
     */
    private static final String VERSION = "version";

    /**
     * The name attribute for the module tag.
     */
    private static final String NAME = "name";

    /**
     * The replace attribute for the module tag.
     */
    private static final String REPLACE = "replace";

    /**
     * The URL attribute for the inclusion tag.
     */
    private static final String URL_ATTRIBUTE = "url";

    /**
     * The module name attribute for the prototype tag.
     */
    private static final String MODULE_NAME = "moduleName";

    /**
     * The state attribute for the debug tag.
     */
    private static final String ENABLED = "enabled";

    /**
     * The file attribute for the debug tag.
     */
    private static final String FILE = "file";

    /**
     * The true value string.
     */
    private static final String TRUE = "true";

    /**
     * The false value string.
     */
    private static final String FALSE = "false";

    /**
     * Registers this interpreter with the kernel. 
     */
    public void registerWithKernel() {
        Kernel kernel = Kernel.getKernel();
        try {
            kernel.addSchemaLocationMapping(NAMESPACE_URI, new URL(SCHEMA_URL));
        } catch (MalformedURLException mue) {
        }
        kernel.registerConfigurationElementInterpreter(NAMESPACE_URI, MODULE, this);
        kernel.registerConfigurationElementInterpreter(NAMESPACE_URI, CONTAINER, this);
        kernel.registerConfigurationElementInterpreter(NAMESPACE_URI, INCLUDE, this);
        kernel.registerConfigurationElementInterpreter(NAMESPACE_URI, PROTOTYPE, this);
        kernel.registerConfigurationElementInterpreter(NAMESPACE_URI, DEBUG, this);
    }

    /**
     * Interprets a configuration element, applying the changes
     * to a module.
     *
     * @param url the <code>URL</code> of the document that contains the element,
     * or <code>null</code> for none
     * @param e the configuration element to interpret
     * @param target the module to receive the changes
     * @exception InvalidConfigurationElementException if the configuration element
     * is invalid
     */
    public void interpretConfigurationElement(URL url, Element e, Module target) throws InvalidConfigurationElementException {
        e = (Element) e.cloneNode(true);
        preprocessElement(e);
        try {
            if (e.getLocalName().equals(INCLUDE)) interpretInclusionElement(url, e, target); else if (e.getLocalName().equals(PROTOTYPE)) interpretPrototypeElement(url, e, target); else if (e.getLocalName().equals(MODULE)) interpretModuleElement(url, e, target); else if (e.getLocalName().equals(CONTAINER)) interpretContainerElement(url, e, target); else if (e.getLocalName().equals(DEBUG)) interpretDebugElement(url, e, target);
        } catch (ClassCastException cce) {
            throw new InvalidConfigurationElementException("<" + e.getTagName() + "> element is not applicable to " + target);
        }
    }

    /**
     * Preprocesses an element, replacing instances of <code>${property.name}</code>
     * with the value of the named system property.
     *
     * @param e the element to preprocess
     */
    private void preprocessElement(Element e) {
        NamedNodeMap nnm = e.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Attr a = (Attr) nnm.item(i);
            String value = a.getValue();
            int beginIndex, endIndex;
            while ((beginIndex = value.indexOf("${")) != -1) {
                endIndex = value.indexOf("}", beginIndex + 2);
                String property = value.substring(beginIndex + 2, endIndex);
                value = value.substring(0, beginIndex) + System.getProperty(property, "") + value.substring(endIndex + 1);
            }
            a.setValue(value);
        }
    }

    /**
     * Interprets an inclusion element.
     *
     * @param url the document containing the element
     * @param e the element to interpret
     * @param target the module to affect
     * @exception InvalidConfigurationElementException if the configuration element
     * is invalid
     */
    protected void interpretInclusionElement(URL url, Element e, Module target) throws InvalidConfigurationElementException {
        if (!e.hasAttribute(URL_ATTRIBUTE) && !e.hasAttribute(NAME)) {
            throw new InvalidConfigurationElementException("<" + INCLUDE + "> element must provide either '" + URL_ATTRIBUTE + "' or '" + NAME + "' attribute");
        }
        if (e.hasAttribute(URL_ATTRIBUTE)) {
            try {
                target.applyConfiguration(new URL(url, e.getAttribute(URL_ATTRIBUTE)));
            } catch (MalformedURLException mue) {
                invalidAttributeError(INCLUDE, URL_ATTRIBUTE, e.getAttribute(URL_ATTRIBUTE));
            } catch (Exception ex) {
                throw new InvalidConfigurationElementException("Exception occurred while processing <" + INCLUDE + "> element: " + ex);
            }
        } else {
            try {
                if (e.hasAttribute(VERSION)) {
                    target.applyConfiguration(new URL("resource:///" + e.getAttribute(NAME) + "?version=" + e.getAttribute(VERSION)));
                } else {
                    target.applyConfiguration(new URL("resource:///" + e.getAttribute(NAME)));
                }
            } catch (MalformedURLException mue) {
                invalidAttributeError(INCLUDE, NAME, e.getAttribute(NAME));
            } catch (Exception ex) {
                throw new InvalidConfigurationElementException("Exception occurred while processing <" + INCLUDE + "> element: " + ex);
            }
        }
    }

    /**
     * Interprets a prototype element.
     *
     * @param url the document containing the element
     * @param e the element to interpret
     * @param target the module to affect
     * @exception InvalidConfigurationElementException if the configuration element
     * is invalid
     */
    protected void interpretPrototypeElement(URL url, Element e, Module target) throws InvalidConfigurationElementException {
        ModuleContainer mc = (ModuleContainer) target;
        URL prototypeURL = null;
        Module namedModule = null;
        String moduleName = null;
        boolean replace = false;
        if (!e.hasAttribute(URL_ATTRIBUTE) && !e.hasAttribute(NAME)) {
            throw new InvalidConfigurationElementException("<" + PROTOTYPE + "> element must provide either '" + URL_ATTRIBUTE + "' or '" + NAME + "' attribute");
        }
        if (e.hasAttribute(URL_ATTRIBUTE)) {
            try {
                prototypeURL = new URL(url, e.getAttribute(URL_ATTRIBUTE));
            } catch (MalformedURLException mue) {
                invalidAttributeError(PROTOTYPE, URL_ATTRIBUTE, e.getAttribute(URL_ATTRIBUTE));
            }
        } else {
            try {
                if (e.hasAttribute(VERSION)) {
                    prototypeURL = new URL("resource:///" + e.getAttribute(NAME) + "?version=" + e.getAttribute(VERSION));
                } else {
                    prototypeURL = new URL("resource:///" + e.getAttribute(NAME));
                }
            } catch (MalformedURLException mue) {
                invalidAttributeError(PROTOTYPE, NAME, e.getAttribute(NAME));
            }
        }
        if (e.hasAttribute(MODULE_NAME)) {
            moduleName = e.getAttribute(MODULE_NAME);
            if (e.hasAttribute(REPLACE)) {
                if (e.getAttribute(REPLACE).equals(FALSE)) {
                    replace = false;
                } else if (!e.getAttribute(REPLACE).equals(TRUE)) {
                    invalidAttributeError(PROTOTYPE, REPLACE, e.getAttribute(REPLACE));
                }
            }
            namedModule = mc.getModuleNamed(moduleName);
        }
        try {
            Module configurationTarget;
            if (namedModule == null || replace) {
                if (moduleName != null) {
                    configurationTarget = mc.createModule(prototypeURL, moduleName);
                } else {
                    configurationTarget = mc.createModule(prototypeURL);
                }
            } else {
                namedModule.applyPrototype(prototypeURL);
                configurationTarget = namedModule;
            }
            configurationTarget.applyConfigurationBody(url, e);
        } catch (Exception ex) {
            throw new InvalidConfigurationElementException("Exception occurred while processing <" + PROTOTYPE + "> element: " + ex);
        }
    }

    /**
     * Interprets a debug element.
     *
     * @param url the document containing the element
     * @param e the element to interpret
     * @param target the module to affect
     * @exception InvalidConfigurationElementException if the configuration element
     * is invalid
     */
    protected void interpretDebugElement(URL url, Element e, Module target) throws InvalidConfigurationElementException {
        if (e.hasAttribute(ENABLED)) {
            if (e.getAttribute(ENABLED).equals(TRUE)) {
                target.setDebugEnabled(true);
            } else if (e.getAttribute(ENABLED).equals(FALSE)) {
                target.setDebugEnabled(false);
            } else {
                invalidAttributeError(DEBUG, ENABLED, e.getAttribute(ENABLED));
            }
        } else {
            target.setDebugEnabled(true);
        }
        if (e.hasAttribute(FILE)) {
            try {
                FileOutputStream fos = new FileOutputStream(e.getAttribute(FILE));
                target.setDebugStream(new PrintStream(fos, true));
            } catch (FileNotFoundException fnfe) {
                invalidAttributeError(DEBUG, FILE, e.getAttribute(FILE));
            }
        }
    }

    /**
     * Interprets a module element.
     *
     * @param url the document containing the element
     * @param e the element to interpret
     * @param target the module to affect
     * @exception InvalidConfigurationElementException if the configuration element
     * is invalid
     */
    protected void interpretModuleElement(URL url, Element e, Module target) throws InvalidConfigurationElementException {
        ModuleContainer mc = (ModuleContainer) target;
        Module namedModule = null;
        String name = null;
        boolean replace = false;
        if (e.hasAttribute(NAME)) {
            name = e.getAttribute(NAME);
            if (e.hasAttribute(REPLACE)) {
                if (e.getAttribute(REPLACE).equals(FALSE)) {
                    replace = false;
                } else if (!e.getAttribute(REPLACE).equals(TRUE)) {
                    invalidAttributeError(MODULE, REPLACE, e.getAttribute(REPLACE));
                }
            }
            namedModule = mc.getModuleNamed(name);
        }
        try {
            if (namedModule == null || replace) {
                if (name != null) {
                    mc.createModule(url, e, name);
                } else {
                    mc.createModule(url, e);
                }
            } else {
                namedModule.applyPrototypeBody(url, e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Interprets a container element.
     *
     * @param url the document containing the element
     * @param e the element to interpret
     * @param target the module to affect
     * @exception InvalidConfigurationElementException if the configuration element
     * is invalid
     */
    protected void interpretContainerElement(URL url, Element e, Module target) throws InvalidConfigurationElementException {
        interpretModuleElement(url, e, target);
    }

    /**
     * Generates an exception with a formulaic error message.
     *
     * @param tagName the name of the element containing the error
     * @param attrName the name of the missing attribute
     * @exception InvalidConfigurationElementException always thrown
     */
    private void missingAttributeError(String tagName, String attrName) throws InvalidConfigurationElementException {
        throw new InvalidConfigurationElementException("<" + tagName + "> element missing '" + attrName + "' attribute");
    }

    /**
     * Generates an exception with a formulaic error message.
     *
     * @param tagName the name of the element containing the error
     * @param attrName the name of the attribute containing the error
     * @param attrValue the invalid attribute value
     * @exception InvalidConfigurationElementException always thrown
     */
    private void invalidAttributeError(String tagName, String attrName, String attrValue) throws InvalidConfigurationElementException {
        throw new InvalidConfigurationElementException("<" + tagName + "> element contains invalid value for '" + attrName + "' attribute: '" + attrValue + "'");
    }
}
