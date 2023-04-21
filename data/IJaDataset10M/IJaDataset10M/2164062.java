package com.cosylab.vdct.plugin;

import java.io.*;
import java.net.*;
import java.util.*;
import com.cosylab.vdct.xml.*;
import org.w3c.dom.*;

/**
 * Insert the class' description here.
 * Creation date: (7.12.2001 14:48:33)
 * @author Matej Sekoranja
 */
public class PluginXMLSerializer implements PluginSerializer {

    private static final String DTD_SYMBOL = "com.cosylab.vdct:plugins";

    private static final String DTD_URL = com.cosylab.vdct.Constants.DTD_DIR + "plugins.dtd";

    /**
 * Insert the method's description here.
 * Creation date: (7.12.2001 15:04:28)
 * @param 
 */
    public void exportPlugins(String fileName, PluginManager pluginManager) throws Exception {
        Document doc = XMLManager.newDocument();
        Element root = (Element) doc.createElement("plugins");
        doc.appendChild(root);
        Iterator plugins = pluginManager.getPlugins();
        while (plugins.hasNext()) {
            Element node = (Element) doc.createElement("plugin");
            ((PluginObject) plugins.next()).saveConfig(doc, node);
            root.appendChild(node);
        }
        root.normalize();
        XMLManager.writeDocument(fileName, doc, null, DTD_SYMBOL, null);
    }

    /**
 * Insert the method's description here.
 * Creation date: (7.12.2001 14:52:16)
 * @param
 */
    public void importPlugins(String fileName, PluginManager pluginManager) throws Exception {
        URL dtdURL = getClass().getResource("/" + DTD_URL);
        if (dtdURL == null) throw new Exception("Failed to locate DTD file: /" + DTD_URL);
        Document doc = null;
        try {
            if (fileName.indexOf('\\') >= 0) {
                try {
                    fileName = (new File(fileName).toURI()).toURL().getFile();
                } catch (MalformedURLException exception) {
                }
            }
            doc = XMLManager.readResourceDocument(fileName, DTD_SYMBOL, dtdURL);
        } catch (FileNotFoundException e) {
            com.cosylab.vdct.Console.getInstance().println("Plugins configuration file '" + fileName + "' not found. Using defaults.");
            return;
        }
        if (doc == null) {
            com.cosylab.vdct.Console.getInstance().println("Failed to read plugins configuration file '" + fileName + "'.");
            return;
        }
        Node node = XMLManager.findNode(doc, "plugins").getNextSibling().getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                if (node.getNodeName().equals("plugin")) {
                    try {
                        PluginObject plugin = new PluginObject((Element) node);
                        pluginManager.addPlugin(plugin);
                    } catch (Throwable t) {
                        com.cosylab.vdct.Console.getInstance().println("Failed to load/initialize plugin: " + t.getMessage());
                        System.err.println("Failed to load/initialize plugin: " + t.getMessage());
                    }
                }
            }
            node = node.getNextSibling();
        }
    }
}
