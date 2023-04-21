package vqwiki;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import vqwiki.lex.LexExtender;
import vqwiki.utils.Utilities;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.net.URL;
import java.net.URLDecoder;
import java.security.AccessControlException;
import java.io.UnsupportedEncodingException;

/**
 * Manager for loading plugins from zip files. The plugins directory is a directory under
 * the Wiki home directory (where the topic contents live) named "plugins". A plugin occupies
 * one zip file and needs to include a file named the same as the zip file but with
 * and "xml" file extension in the WEB-INF/classes directory.
 * So if the plugin file is called test.zip, then the included file
 * should be called test.xml.
 * <p/>
 * The plugin zip is extracted directly to the wiki installation directory, so any class files
 * should be in the path WEB-INF/classes and any jars should be in WEB-INF/lib.
 * <p/>
 * The plugin xml file can contain various entries that will cause mappings to be inserted in the
 * different mapping repositories.
 */
public class PluginManager {

    private static PluginManager instance;

    public static final String PLUGINS_DIR = "plugins";

    private static final Logger logger = Logger.getLogger(PluginManager.class);

    private static final String TAG_PLUGIN = "plugin";

    private static final String TAG_EXTERNAL_LEX = "external-lex";

    private static final String TAG_TOPIC_LISTENER = "topic-listener";

    private static final String TAG_ACTION = "action";

    private static final String ATTR_TAG = "tag";

    private static final String ATTR_CLASS = "class";

    private static final String ATTR_NAME = "name";

    private static final String ATTR_REDIRECT = "redirect";

    private static final String ATTR_PSEUDOTOPIC = "pseudotopic";

    /** Listeners for the WikiBase to use */
    private List topicListeners = new ArrayList();

    /** Number of millis to wait after a plugin has been unzipped so that the class loader has a chance to load the classes */
    private static final long PAUSE = 3000;

    /**
     * Get an instance of the manager
     *
     * @return singleton instance
     */
    public static synchronized PluginManager getInstance() {
        if (instance == null) {
            instance = new PluginManager();
        }
        return instance;
    }

    /**
     * Hide default constructor
     */
    private PluginManager() {
    }

    /**
     * Look for plugins and install them as required
     */
    public void installAll() {
        File pluginDir = new File(Environment.dir(), PLUGINS_DIR);
        pluginDir.mkdir();
        logger.debug("Looking for plugins in " + pluginDir);
        File[] files = pluginDir.listFiles();
        if (files == null) {
            return;
        }
        if (files.length == 0) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            String name = file.getName();
            if (name.endsWith(".zip")) {
                name = name.substring(0, name.length() - 4);
                logger.debug("Plugin found: " + name);
                install(name, file);
            }
        }
    }

    /**
     * Return the name the plugin XML file will have
     *
     * @param name plugin name
     * @return name
     */
    private String getPluginPropertiesFilename(String name) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("/");
        buffer.append(name);
        buffer.append(".xml");
        String pluginPropertiesFilename = buffer.toString();
        return pluginPropertiesFilename;
    }

    /**
     * Install the plugin
     *
     * @param name plugin name
     * @param file zip file containing the plugin
     */
    private void install(String name, File file) {
        try {
            String installedVersion = getInstalledPluginVersion(name);
            String newVersion = readPluginAttributeFromPlugin(name, file, "version");
            logger.debug("installed plugin version: " + installedVersion + ", new version: " + newVersion);
            if (installedVersion == null || newVersion.compareTo(installedVersion) > 0) {
                logger.info("Installing plugin: " + name);
                String realPath = Environment.getInstance().getRealPath();
                if (realPath == null) {
                    logger.error("installation directory is null, plugin manager must be running without any requests having been made!");
                    return;
                }
                Utilities.unzip(file, new File(realPath));
                Thread.sleep(PAUSE);
            } else {
                logger.info("plugin of same version already installed: " + name);
            }
        } catch (Exception e) {
            logger.error("unable to determine current version of plugin: " + name, e);
        }
        Document doc;
        String pluginPropertiesFilename = null;
        String res = null;
        try {
            pluginPropertiesFilename = getPluginPropertiesFilename(name);
            URL resource = PluginManager.class.getResource(pluginPropertiesFilename);
            if (resource == null) {
                logger.error("plugin properties file not found: " + pluginPropertiesFilename);
                return;
            } else {
                String defaultencoding = null;
                try {
                    defaultencoding = System.getProperty("file.encoding");
                } catch (AccessControlException ae) {
                    logger.warn("This application server doesn't allow to access " + "file.encoding with System.getProperty. Set default " + "encoding for filename-URL to UTF-8");
                    defaultencoding = "UTF-8";
                }
                try {
                    res = URLDecoder.decode(resource.getFile(), defaultencoding);
                } catch (UnsupportedEncodingException e) {
                    logger.error("The platform's default encoding is not supported in the JDK.", e);
                    try {
                        res = URLDecoder.decode(resource.getFile(), "UTF-8");
                    } catch (UnsupportedEncodingException e1) {
                        logger.fatal("Even UTF-8 is not supported by this JDK!", e1);
                    }
                }
            }
            doc = Utilities.parseDocumentFromFile(res);
        } catch (Exception e) {
            logger.error("Error parsing plugin properties XML file: " + pluginPropertiesFilename, e);
            return;
        }
        logger.debug("Reading plugin configuration");
        NodeList rootList = doc.getElementsByTagName(TAG_PLUGIN);
        if (rootList.getLength() == 0) {
            return;
        }
        Element root = (Element) rootList.item(0);
        NodeList externalLexEntries = root.getElementsByTagName(TAG_EXTERNAL_LEX);
        for (int i = 0; i < externalLexEntries.getLength(); i++) {
            logger.debug("Making plugin external lex entry");
            Element externalLexElement = (Element) externalLexEntries.item(i);
            String className = externalLexElement.getAttribute(ATTR_CLASS);
            String tagName = externalLexElement.getAttribute(ATTR_TAG);
            try {
                LexExtender.getInstance().addLexerEntry(tagName, className);
            } catch (IOException e) {
                logger.error("error adding lexer entry", e);
            }
        }
        NodeList actionEntries = root.getElementsByTagName(TAG_ACTION);
        for (int i = 0; i < actionEntries.getLength(); i++) {
            Element actionElement = (Element) actionEntries.item(i);
            String actionName = actionElement.getAttribute(ATTR_NAME);
            String className = actionElement.getAttribute(ATTR_CLASS);
            try {
                ActionManager.getInstance().addMapping(actionName, className);
            } catch (IOException e) {
                logger.error("error adding action", e);
            }
            String pseudotopic = actionElement.getAttribute(ATTR_PSEUDOTOPIC);
            if (pseudotopic != null && !"".equals(pseudotopic)) {
                PseudoTopicHandler.getInstance().addMapping(pseudotopic, "Wiki?action=" + actionName);
            }
        }
        NodeList topicListeners = root.getElementsByTagName(TAG_TOPIC_LISTENER);
        for (int i = 0; i < topicListeners.getLength(); i++) {
            Element topicListenerElement = (Element) topicListeners.item(i);
            String className = topicListenerElement.getAttribute(ATTR_CLASS);
            logger.debug("registering topic listener: " + className);
            try {
                Class clazz = Class.forName(className);
                TopicListener listener = (TopicListener) clazz.newInstance();
                this.topicListeners.add(listener);
            } catch (Exception e) {
                logger.error("error creating topic listener and registering it", e);
            }
        }
    }

    /**
     * Get the version of the currently installed version of the plugin
     *
     * @param name plugin name
     * @return version or null if not installed
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private String getInstalledPluginVersion(String name) throws IOException, ParserConfigurationException, SAXException {
        InputStream in = getClass().getResourceAsStream("/" + name + ".xml");
        if (in == null) {
            return null;
        }
        Document doc = Utilities.parseDocumentFromInputStream(in);
        return getPluginAttributeFromDocument(doc, "version");
    }

    /**
     * Read an attribute from the top-level plugin element in the plugin's descriptor XML
     *
     * @param pluginName    name of the plugin
     * @param pluginZipFile the zip file containing the plugin
     * @param attributeName attribute name
     * @return attribute value or null if not found
     */
    private String readPluginAttributeFromPlugin(String pluginName, File pluginZipFile, String attributeName) {
        Enumeration entries;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(pluginZipFile);
            entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.getName().equals("WEB-INF/classes/" + pluginName + ".xml")) {
                    logger.debug("found descriptor");
                    InputStream xmlIn = null;
                    try {
                        xmlIn = zipFile.getInputStream(entry);
                        Document doc = Utilities.parseDocumentFromInputStream(xmlIn);
                        return getPluginAttributeFromDocument(doc, attributeName);
                    } catch (Exception e) {
                        logger.error("", e);
                    } finally {
                        if (xmlIn != null) {
                            xmlIn.close();
                        }
                    }
                }
            }
            zipFile.close();
        } catch (IOException ioe) {
            logger.error("Unzipping error: " + ioe);
        }
        return null;
    }

    /**
     * Return a plugin attribute from a plugin descriptor document
     *
     * @param doc           document
     * @param attributeName attribute name
     * @return value or null if not found
     */
    private String getPluginAttributeFromDocument(Document doc, String attributeName) {
        NodeList pluginElements = doc.getElementsByTagName(TAG_PLUGIN);
        if (pluginElements.getLength() != 1) {
            logger.error("there must be one and only one plugin element in descriptor");
            return null;
        }
        Element element = (Element) pluginElements.item(0);
        String attribute = element.getAttribute(attributeName);
        logger.debug("found attribute " + attributeName + " = " + attribute);
        return attribute;
    }

    /**
     * Topic listeners found in plugins
     *
     * @return listeners
     */
    public List getTopicListeners() {
        return topicListeners;
    }
}
