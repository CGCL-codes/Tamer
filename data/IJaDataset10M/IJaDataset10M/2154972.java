package vqwiki.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextListener;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import vqwiki.Constants;
import vqwiki.Environment;
import vqwiki.TopicListener;
import vqwiki.WikiBase;
import vqwiki.lex.LexExtender;
import vqwiki.svc.ActionManager;
import vqwiki.svc.PseudoTopicHandler;
import vqwiki.utils.Utilities;

/**
 * Manager for loading plugins from zip files. The plugins directory is a directory under
 * the Wiki home directory (where the topic contents live) named "plugins". A plugin occupies
 * one zip file and needs to include a file named the same as the zip file but with
 * and "xml" file extension in the WEB-INF/classes directory.
 * So if the plugin file is called test.zip, then the included file
 * should be called test.xml.
 * <p/>
 * The plugin zip gets extracted to the webapp work directory, and its layout should be like
 * a normal WAR with classes in WEB-INF/classes and jars in WEB-INF/lib but without any web.xml.
 * <p/>
 * The plugin xml file can contain various entries that will cause mappings to be inserted in the
 * different mapping repositories.
 */
public class PluginManager {

    public static final String PLUGIN_FILE_EXTENSION = ".zip";

    public static final String PLUGIN_DESCRIPTOR_FILE_NAME = "vqwiki-plugin.xml";

    /** Location of the Plugins-Directory within the wiki home */
    public static final String PLUGINS_DIR = "plugins";

    private static final Logger logger = Logger.getLogger(PluginManager.class.getName());

    private static final String TAG_PLUGIN = "plugin";

    private static final String TAG_EXTERNAL_LEX = "external-lex";

    private static final String TAG_TOPIC_LISTENER = "topic-listener";

    private static final String TAG_WIKI_LISTENER = "wiki-listener";

    private static final String TAG_ACTION = "action";

    private static final String ATTR_TAG = "tag";

    private static final String ATTR_CLASS = "class";

    private static final String ATTR_NAME = "name";

    private static final String ATTR_PSEUDOTOPIC = "pseudotopic";

    private static final FilenameFilter zipFilenameFilter = new SuffixFileFilter(PLUGIN_FILE_EXTENSION);

    private PluginClassLoader pluginClassLoader;

    public PluginManager(File pluginWorkDir) {
        this.pluginClassLoader = new PluginClassLoader(pluginWorkDir);
    }

    public PluginClassLoader getPluginClassLoader() {
        return pluginClassLoader;
    }

    /**
     * Look for plugins and install them as required
     */
    public void installAll() {
        File pluginDir = new File(Environment.dir(), PLUGINS_DIR);
        pluginDir.mkdir();
        logger.fine("Looking for plugins in " + pluginDir);
        File[] files = pluginDir.listFiles(zipFilenameFilter);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String name = file.getName();
                name = name.substring(0, name.length() - 4);
                logger.info("Plugin found: " + name);
                installPlugin(name, file);
            }
        }
    }

    /**
     * Install the plugin
     *
     * @param name plugin name
     * @param file zip file containing the plugin
     */
    private void installPlugin(String name, File file) {
        WikiBase wb = WikiBase.getInstance();
        Document doc;
        pluginClassLoader.addPlugin(file);
        try {
            doc = Utilities.parseDocumentFromInputStream(pluginClassLoader.getResourceAsStream(name + ".xml"));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error parsing plugin XML descriptor: " + name, e);
            return;
        }
        logger.fine("Reading plugin configuration");
        NodeList rootList = doc.getElementsByTagName(TAG_PLUGIN);
        if (rootList.getLength() == 0) {
            return;
        }
        Element root = (Element) rootList.item(0);
        NodeList externalLexEntries = root.getElementsByTagName(TAG_EXTERNAL_LEX);
        for (int i = 0; i < externalLexEntries.getLength(); i++) {
            logger.fine("Making plugin external lex entry");
            Element externalLexElement = (Element) externalLexEntries.item(i);
            String className = externalLexElement.getAttribute(ATTR_CLASS);
            String tagName = externalLexElement.getAttribute(ATTR_TAG);
            try {
                pluginClassLoader.loadClass(className);
                LexExtender lexExtender = wb.getLexExtender();
                lexExtender.addMapping(tagName, className);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "error adding lexer entry", e);
            }
        }
        NodeList actionEntries = root.getElementsByTagName(TAG_ACTION);
        for (int i = 0; i < actionEntries.getLength(); i++) {
            Element actionElement = (Element) actionEntries.item(i);
            String actionName = actionElement.getAttribute(ATTR_NAME);
            String className = actionElement.getAttribute(ATTR_CLASS);
            try {
                String pseudotopic = actionElement.getAttribute(ATTR_PSEUDOTOPIC);
                if (pseudotopic != null && !"".equals(pseudotopic)) {
                    if (actionName == null || "".equals(actionName)) actionName = pseudotopic.toLowerCase();
                    PseudoTopicHandler pseudoTopicHandler = wb.getPseudoTopicHandler();
                    pseudoTopicHandler.addMapping(pseudotopic, "/Wiki?" + Constants.ACTION_KEY + "=" + actionName);
                }
                ActionManager actionManager = wb.getActionManager();
                pluginClassLoader.loadClass(className);
                actionManager.addMapping(actionName, className);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "error adding action", e);
            }
        }
        wb.getTopicListeners().addAll(parseListeners(pluginClassLoader, root, TAG_TOPIC_LISTENER, TopicListener.class));
        wb.getWikiListeners().addAll(parseListeners(pluginClassLoader, root, TAG_WIKI_LISTENER, ServletContextListener.class));
    }

    private List parseListeners(ClassLoader cl, Element root, String tag, Class requiredClazz) {
        NodeList nodeList = root.getElementsByTagName(tag);
        List listeners = new ArrayList();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            String className = element.getAttribute(ATTR_CLASS);
            logger.fine("registering listener: " + className);
            try {
                Class clazz = cl.loadClass(className);
                if (requiredClazz.isAssignableFrom(clazz)) {
                    listeners.add(clazz.newInstance());
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "error creating listener and registering it", e);
            }
        }
        return listeners;
    }

    /**
     * Get the currently installed version of the plugin.
     *
     * @param name plugin name
     * @return version or null if not installed
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    public String getInstalledPluginVersion(String name) throws IOException, ParserConfigurationException, SAXException {
        InputStream in = pluginClassLoader.getResourceAsStream(PLUGIN_DESCRIPTOR_FILE_NAME);
        if (in == null) {
            return null;
        }
        Document doc = Utilities.parseDocumentFromInputStream(in);
        return getPluginAttributeFromDocument(doc, "version");
    }

    /**
     * Return a plugin attribute from a plugin descriptor document
     *
     * @param doc           document
     * @param attributeName attribute name
     * @return value or null if not found
     */
    public static String getPluginAttributeFromDocument(Document doc, String attributeName) {
        NodeList pluginElements = doc.getElementsByTagName(TAG_PLUGIN);
        if (pluginElements.getLength() != 1) {
            logger.log(Level.SEVERE, "there must be one and only one plugin element in descriptor");
            return null;
        }
        Element element = (Element) pluginElements.item(0);
        String attribute = element.getAttribute(attributeName);
        logger.fine("found attribute " + attributeName + " = " + attribute);
        return attribute;
    }
}
