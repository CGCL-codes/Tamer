package hu.scytha.plugin;

import hu.scytha.common.MessageSystem;
import hu.scytha.main.Settings;
import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

/**
 * @author Bertalan Lacza
 *
 */
class PluginXmlParser {

    private static final String PLUGIN_XML_FILE = Settings.getPluginXml();

    private static final String VERSION = "0.1";

    private Document doc;

    private List<PluginDescriptor> plugins;

    private boolean changedPluginList = false;

    public PluginXmlParser() {
        plugins = new ArrayList<PluginDescriptor>();
        try {
            ensureXmlIsParsed();
            NodeList nodeList = doc.getElementsByTagName("plugin");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element elem = (Element) nodeList.item(i);
                plugins.add(createPluginDescriptor(elem));
            }
        } catch (Exception e) {
            MessageSystem.logException("Unable to parse plugin xml", PluginXmlParser.class.getName(), "getDefaultPlugins", null, e);
        }
    }

    public List<PluginDescriptor> getPluginDescriptors() {
        return plugins;
    }

    public void reset() {
        changedPluginList = true;
        plugins.clear();
    }

    public void addPlugin(PluginDescriptor pd) {
        if (!contains(pd)) {
            changedPluginList = true;
            plugins.add(pd);
        }
    }

    public void removePlugin(PluginDescriptor pd) {
        int index = -1;
        for (PluginDescriptor installedPlugin : plugins) {
            index++;
            if (installedPlugin.getId().equals(pd.getId())) {
                break;
            }
        }
        if (index >= 0) {
            changedPluginList = true;
            plugins.remove(index);
        }
    }

    public boolean contains(PluginDescriptor pd) {
        for (PluginDescriptor installedPlugin : plugins) {
            if (installedPlugin.getId().equals(pd.getId())) {
                return true;
            }
        }
        return false;
    }

    private static PluginDescriptor createPluginDescriptor(Element element) {
        PluginDescriptor pd = new PluginDescriptor();
        NodeList elements = element.getElementsByTagName("extensions");
        for (int i = 0; i < elements.getLength(); i++) {
            Node item = elements.item(i);
            if (item != null) {
                pd.addExtension(((Element) item).getAttribute("value"), Boolean.valueOf(((Element) item).getAttribute("isDeafult")));
            }
        }
        pd.setId(element.getAttribute("id"));
        pd.setJar(getNodeValue(element, "jar"));
        pd.setPluginClass(getNodeValue(element, "class"));
        pd.setDefault(Boolean.valueOf(getNodeValue(element, "default")));
        pd.setType(Integer.valueOf(getNodeValue(element, "type")));
        return pd;
    }

    private static String getNodeValue(Element element, String nodeName) {
        NodeList elements = element.getElementsByTagName(nodeName);
        if (elements.getLength() > 0) {
            Node item = elements.item(0);
            if (item != null) {
                return item.getTextContent();
            }
        }
        return null;
    }

    private void ensureXmlIsParsed() throws Exception {
        if (doc == null) {
            checkFile();
            try {
                DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
                doc = docBuilder.parse(PLUGIN_XML_FILE);
            } catch (Exception e) {
            }
        }
    }

    /**
    * Checks the existence of file,
    * and creates if failed.
    *
    */
    private void checkFile() throws IOException {
        File f = new File(PLUGIN_XML_FILE);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                MessageSystem.logException("f.createNewFile() failed.", "hu.scytha.frw.common.PluginXmlParser", "checkFile", null, e);
                throw e;
            }
            try {
                FileOutputStream fos = new FileOutputStream(f, true);
                fos.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n").getBytes());
                fos.write(("<scytha-plugins version=\"").getBytes());
                fos.write(VERSION.getBytes());
                fos.write(("\">\n").getBytes());
                fos.write(("</scytha-plugins>").getBytes());
                fos.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }

    /**
    * This method writes a DOM document to a file.
    */
    public void store() {
        if (!changedPluginList) {
            return;
        }
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element pluginsNode = document.createElement("plugins");
            document.appendChild(pluginsNode);
            for (PluginDescriptor pd : getPluginDescriptors()) {
                createPluginNode(document, pluginsNode, pd);
            }
            Source source = new DOMSource(document);
            File fileFile = new File(PLUGIN_XML_FILE);
            Result result = new StreamResult(fileFile);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            xformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void print() {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element pluginsNode = document.createElement("plugins");
            document.appendChild(pluginsNode);
            for (PluginDescriptor pd : getPluginDescriptors()) {
                createPluginNode(document, pluginsNode, pd);
            }
            Source source = new DOMSource(document);
            Result result = new StreamResult(System.out);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            xformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createPluginNode(Document document, Element pluginsNode, PluginDescriptor pd) {
        Element element = document.createElement("plugin");
        element.setAttribute("id", pd.getId());
        pluginsNode.appendChild(element);
        Element jar = document.createElement("jar");
        jar.setTextContent(pd.getJar());
        element.appendChild(jar);
        Element pluginClass = document.createElement("class");
        pluginClass.setTextContent(pd.getPluginClass());
        element.appendChild(pluginClass);
        Element extensions = document.createElement("extensions");
        element.appendChild(extensions);
        if (pd.getExtensions() != null) {
            for (PluginExtension pe : pd.getExtensions()) {
                Element extension = document.createElement("extension");
                extension.setAttribute("value", pe.getExtension());
                extension.setAttribute("isDefault", pe.getIsDefault() + "");
                extensions.appendChild(extension);
            }
        }
        Element type = document.createElement("type");
        type.setTextContent(pd.getType().toString());
        element.appendChild(type);
    }
}
