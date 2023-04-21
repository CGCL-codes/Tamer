package org.logicalcobwebs.proxool.configuration;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.component.Component;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.logicalcobwebs.proxool.ProxoolConstants;
import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Configurator for the <a href="http://jakarta.apache.org/avalon/framework/" target="_new">Avalon Framework</a>.
 * The configuration can contain any number of &lt;proxool&gt; elements. The &lt;proxool&gt; elements
 * are delegated to {@link XMLConfigurator},
 * and have exactly the same format as is documented in that class.
 * <p>
 * This is a "faceless" Avalon component. This means that it does not present an operational interface, it
 * simply configures Proxool when Avalon calls its <code>configure</code> method. You need to lookup this
 * component in your bootstrap code to make this happen.
 * </p>
 * <p>
 * The configuration takes one attribute: <code>close-on-dispose</code>
 * <br>
 * You can use this to let this configurator know
 * wether or not to close the pools it has created
 * when it is disposed.
 * <br>Legal values are <code>true</code> or <code>false</code>. Default: <code>true</code>.
 * </p>
 *
 * @version $Revision: 1.14 $, $Date: 2006/01/18 14:39:58 $
 * @author billhorsman
 * @author $Author: billhorsman $ (current maintainer)
 */
public class AvalonConfigurator implements Component, Configurable, ThreadSafe, Disposable {

    private static final Log LOG = LogFactory.getLog(AvalonConfigurator.class);

    /**
     * Avalon ROLE id for this component.
     */
    public static final String ROLE = AvalonConfigurator.class.getName();

    /**
     * Constant for the boolean "close-on-dispose" attribute that signifies
     * wether or not this configurator shall close the pools it has created
     * when it is disposed. Legal values are "true" or "false". Default: true.
     *
     */
    public static final String CLOSE_ON_DISPOSE_ATTRIBUTE = "close-on-dispose";

    private boolean closeOnDispose = true;

    private final List configuredPools = new ArrayList(3);

    /**
     * Check that all top level elements are named proxool and hand them to
     * {@link XMLConfigurator}.
     * @param configuration the configuration handed over by the Avalon Framework.
     * @throws ConfigurationException if the configuration fails.
     */
    public void configure(Configuration configuration) throws ConfigurationException {
        final XMLConfigurator xmlConfigurator = new XMLConfigurator();
        this.closeOnDispose = configuration.getAttributeAsBoolean(CLOSE_ON_DISPOSE_ATTRIBUTE, true);
        final Configuration[] children = configuration.getChildren();
        for (int i = 0; i < children.length; ++i) {
            if (!children[i].getName().equals(ProxoolConstants.PROXOOL)) {
                throw new ConfigurationException("Found element named " + children[i].getName() + ". Only " + ProxoolConstants.PROXOOL + " top level elements are alowed.");
            }
        }
        try {
            xmlConfigurator.startDocument();
            reportProperties(xmlConfigurator, configuration.getChildren());
            xmlConfigurator.endDocument();
        } catch (SAXException e) {
            throw new ConfigurationException("", e);
        }
    }

    /**
     * If {@link #CLOSE_ON_DISPOSE_ATTRIBUTE} is set: Close all connection pools that this configurator has configured.
     * <br>...else: do nothing.
     */
    public void dispose() {
        LOG.info("Disposing.");
        if (this.closeOnDispose) {
            Iterator configuredPools = this.configuredPools.iterator();
            String alias = null;
            while (configuredPools.hasNext()) {
                alias = (String) configuredPools.next();
                LOG.info("Closing connection pool '" + alias + "'.");
                try {
                    ProxoolFacade.removeConnectionPool(alias);
                } catch (ProxoolException e) {
                    LOG.error("Closing of connection pool '" + alias + "' failed.", e);
                }
            }
        } else {
            LOG.info(CLOSE_ON_DISPOSE_ATTRIBUTE + " attribute is not set, so configured pools will not be closed.");
        }
        LOG.info("Disposed.");
    }

    private void reportProperties(XMLConfigurator xmlConfigurator, Configuration[] properties) throws ConfigurationException, SAXException {
        Configuration[] children = null;
        String value = null;
        String namespace = null;
        for (int i = 0; i < properties.length; ++i) {
            Configuration configuration = properties[i];
            namespace = configuration.getNamespace();
            if (namespace == null) {
                namespace = "";
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Reporting element start for " + configuration.getName());
            }
            final String lName = namespace.length() == 0 ? "" : configuration.getName();
            final String qName = namespace.length() == 0 ? configuration.getName() : "";
            xmlConfigurator.startElement(namespace, lName, qName, getAttributes(configuration));
            children = configuration.getChildren();
            if (children == null || children.length < 1) {
                value = configuration.getValue(null);
                if (value != null) {
                    xmlConfigurator.characters(value.toCharArray(), 0, value.length());
                }
            } else {
                reportProperties(xmlConfigurator, children);
            }
            xmlConfigurator.endElement(namespace, lName, qName);
            if (lName.equals(ProxoolConstants.PROXOOL) || qName.equals(ProxoolConstants.PROXOOL)) {
                Configuration conf = configuration.getChild(ProxoolConstants.ALIAS, false);
                if (conf != null) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding to configured pools: " + conf.getValue());
                    }
                    this.configuredPools.add(conf.getValue());
                } else {
                    LOG.error("proxool element was missing required element 'alias'");
                }
            }
        }
    }

    private Attributes getAttributes(Configuration configuration) throws ConfigurationException {
        final AttributesImpl attributes = new AttributesImpl();
        final String[] avalonAttributeNames = configuration.getAttributeNames();
        if (avalonAttributeNames != null && avalonAttributeNames.length > 0) {
            for (int i = 0; i < avalonAttributeNames.length; ++i) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Adding attribute " + avalonAttributeNames[i] + " with value " + configuration.getAttribute(avalonAttributeNames[i]));
                }
                attributes.addAttribute("", avalonAttributeNames[i], avalonAttributeNames[i], "CDATA", configuration.getAttribute(avalonAttributeNames[i]));
                LOG.debug("In attributes: " + avalonAttributeNames[i] + " with value " + attributes.getValue(avalonAttributeNames[i]));
            }
        }
        return attributes;
    }
}
