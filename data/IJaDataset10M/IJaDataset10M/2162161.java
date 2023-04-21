package org.tagbox.engine;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;
import org.tagbox.xml.XMLProxy;
import org.tagbox.xml.NodeFinder;
import org.tagbox.util.ResourceManager;
import org.tagbox.config.ConfigurationRegistry;
import org.tagbox.config.Configurable;
import org.tagbox.config.PropertyInfo;
import org.tagbox.config.ConfigurationException;
import org.tagbox.util.Log;
import org.tagbox.xpath.XPathProcessor;

public class ComponentCenter implements Configurable {

    private static Map configs = new HashMap();

    private static int instanceCount = 0;

    private String name;

    private Context context;

    private Map components;

    private NodeFinder finder;

    public static final String DEFAULT_CONFIG = "conf/TagBox.xml";

    private ComponentCenter(Context context) throws TagBoxException {
        name = "componentCenter" + instanceCount++;
        this.context = context;
        components = new HashMap();
        finder = new NodeFinder(null);
    }

    /**
     * initialise context and components
     */
    private void init() throws TagBoxException {
        String configFile = getConfigFile(context);
        Log.trace(name + ": " + configFile);
        Document cfg;
        try {
            cfg = XMLProxy.getInstance().getDocument(configFile);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new TagBoxConfigurationException("invalid configuration file: " + configFile, e);
        } catch (IOException e) {
            throw new TagBoxConfigurationException("invalid configuration file: " + configFile, e);
        }
        String basedir = context.getURL("/");
        XIncluder xinc = new XIncluder(basedir);
        xinc.process(cfg);
        context.init(cfg.getDocumentElement());
        init(cfg.getDocumentElement());
    }

    public static ComponentCenter getInstance(Context context) throws TagBoxException {
        Object key = makeKey(context);
        synchronized (configs) {
            ComponentCenter cc = (ComponentCenter) configs.get(key);
            if (cc == null) {
                cc = new ComponentCenter(context);
                configs.put(key, cc);
                cc.init();
            }
            return cc;
        }
    }

    protected static String getConfigFile(Context context) {
        String configfile = context.getParameter("configuration-file");
        if (configfile == null) configfile = DEFAULT_CONFIG;
        configfile = context.getURL(configfile);
        return configfile;
    }

    protected static Object makeKey(Context context) {
        return getConfigFile(context);
    }

    public Component getComponent(String name) throws TagBoxException {
        return (Component) components.get(name);
    }

    /**
     * load and initialise components
     */
    private void init(Node cfg) throws TagBoxException {
        NodeIterator it = finder.getElements(cfg, "component");
        for (Node n = it.nextNode(); n != null; n = it.nextNode()) buildComponent(n);
        try {
            context.register(this, name);
        } catch (ConfigurationException exc) {
            throw new TagBoxConfigurationException(exc);
        }
    }

    protected void buildComponent(Node cfg) throws TagBoxException {
        String classname = finder.getElementValue(cfg, "class-name");
        String name = finder.getElementValue(cfg, "name");
        try {
            Component component;
            if (classname == null) component = new Component(); else component = (Component) ResourceManager.getClassForName(classname).newInstance();
            component.init(context, cfg);
            components.put(name, component);
        } catch (ClassNotFoundException exc) {
            throw new TagBoxConfigurationException("invalid component: " + name, exc);
        } catch (InstantiationException exc) {
            throw new TagBoxConfigurationException("invalid component: " + name, exc);
        } catch (IllegalAccessException exc) {
            throw new TagBoxConfigurationException("invalid component: " + name, exc);
        }
    }

    public String getConfigurationFile() {
        return getConfigFile(context);
    }

    public void register(ConfigurationRegistry cr) throws ConfigurationException {
        Iterator it = components.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            Component component = (Component) components.get(name);
            cr.register(component, name + "Component");
        }
        cr.register(new PropertyInfo("file", "the configuration filename", String.class, "getConfigurationFile"));
        cr.register(XPathProcessor.getInstance(), "XPathProcessor");
    }
}
