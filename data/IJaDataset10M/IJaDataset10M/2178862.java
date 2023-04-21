package xbrowser.util;

import java.awt.*;
import java.beans.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import org.w3c.dom.*;
import xbrowser.XProjectConstants;
import xbrowser.XRepository;
import xbrowser.options.XOptionPage;
import xbrowser.plugin.*;

public class XPluginObject extends XProxyObject {

    public XPluginObject(String resource) {
        super("", "", resource);
        propChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propChangeSupport.removePropertyChangeListener(listener);
    }

    public XPlugin getPlugin() {
        return plugin;
    }

    public void init() {
        if (plugin != null) return;
        try {
            plugin = loadPluginImpl();
            setStatus(XProjectConstants.PLUGIN_LOADED);
        } catch (Throwable t) {
            XRepository.getLogger().error(this, "Plugin can not be loaded!");
            XRepository.getLogger().error(this, t);
            plugin = null;
            setStatus(XProjectConstants.PLUGIN_INVALID);
            return;
        }
        plugin.setContext(new XPluginContext(plugin.getClass().getClassLoader()));
        plugin.init();
        setStatus(XProjectConstants.PLUGIN_INITIATED);
    }

    public void start() {
        if (plugin == null) return;
        plugin.start();
        setStatus(XProjectConstants.PLUGIN_STARTED);
    }

    public void stop() {
        if (plugin == null) return;
        plugin.stop();
        setStatus(XProjectConstants.PLUGIN_STOPPED);
    }

    public void destroy() {
        if (plugin == null) return;
        plugin.destroy();
    }

    public XOptionPage getOptionPage() {
        if (plugin == null) return null;
        if (options == null) options = plugin.getOptionPage();
        return options;
    }

    private XPlugin loadPluginImpl() throws Exception {
        if (pluginClassLoader == null) {
            StringTokenizer tokenizer = new StringTokenizer(resource, ",;");
            String temp_rsrc = "";
            while (tokenizer.hasMoreTokens()) temp_rsrc += XProjectConstants.PLUGINS_DIR + tokenizer.nextToken();
            pluginClassLoader = XRepository.getResourceManager().createClassLoaderForResource(temp_rsrc);
        }
        URL xml = pluginClassLoader.getResource(XProjectConstants.PLUGIN_DESCRIPTOR_FILE);
        if (xml == null) throw new Exception("Plugin descriptor not found!!");
        URL dtd_url = XRepository.getResourceManager().getResourceURL(DTD_URL);
        Document doc = XMLManager.readResourceDocument(xml.toString(), DTD_SYMBOL, dtd_url);
        Node node = XMLManager.findNode(doc, "xplugin").getNextSibling();
        loadDescriptor(node);
        checkForExtraLibraries();
        return ((XPlugin) XRepository.getResourceManager().loadObject(packageName, pluginClassLoader));
    }

    private void checkForExtraLibraries() throws Exception {
        StringTokenizer tokenizer = new StringTokenizer(extraResource, ",;");
        String plugin_lib_dir = XProjectConstants.PLUGINS_DIR + name + "_lib/";
        String extra_resource_urls = "";
        String temp_rsrc;
        InputStream is;
        OutputStream os;
        File file;
        URL url;
        int b;
        while (tokenizer.hasMoreTokens()) {
            temp_rsrc = tokenizer.nextToken();
            file = new File(plugin_lib_dir + temp_rsrc);
            if (!file.exists()) {
                url = pluginClassLoader.getResource(temp_rsrc);
                is = url.openStream();
                file.getParentFile().mkdirs();
                os = new FileOutputStream(file);
                while ((b = is.read()) != -1) os.write(b);
                is.close();
                os.close();
            }
            if (!extra_resource_urls.equals("")) extra_resource_urls += ";";
            extra_resource_urls += plugin_lib_dir + temp_rsrc;
        }
        pluginClassLoader = XRepository.getResourceManager().createBuiltInClassLoader(extra_resource_urls, pluginClassLoader);
    }

    private void loadDescriptor(Node root) throws DOMException {
        Node node = root.getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                if (node.getNodeName().equals("name")) name = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("icon")) iconPath = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("classname")) packageName = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("resource")) extraResource = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("version")) version = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("releasedate")) releaseDate = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("description")) description = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("documentation")) documentationFile = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("author")) loadAuthorInfo(node);
            }
            node = node.getNextSibling();
        }
    }

    private void loadAuthorInfo(Node node) throws DOMException {
        node = node.getFirstChild();
        while (node != null) {
            if (node instanceof Element) {
                if (node.getNodeName().equals("authorname")) authorName = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("authoremail")) authorEMail = XMLManager.getNodeValue(node); else if (node.getNodeName().equals("authorhomepage")) authorHomePage = XMLManager.getNodeValue(node);
            }
            node = node.getNextSibling();
        }
    }

    private void buildIcon() {
        if (icon != null) return;
        if ((iconPath == null) || (iconPath.trim().equals("")) || (pluginClassLoader == null)) return;
        try {
            icon = new ImageIcon(pluginClassLoader.getResource(iconPath));
        } catch (MissingResourceException e) {
        }
    }

    public Component getComponent() {
        if ((myComponent == null) && (plugin instanceof XGUIPlugin)) myComponent = ((XGUIPlugin) plugin).getComponent();
        SwingUtilities.updateComponentTreeUI(myComponent);
        return myComponent;
    }

    public String getTitle() {
        if (plugin != null) return plugin.getTitle(); else return "";
    }

    public ImageIcon getIcon() {
        buildIcon();
        return icon;
    }

    public String getVersion() {
        return version;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getDescription() {
        return description;
    }

    public String getDocumentationFile() {
        return documentationFile;
    }

    public URL getDocumentationURL() {
        return pluginClassLoader.getResource(documentationFile);
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEMail() {
        return authorEMail;
    }

    public String getAuthorHomePage() {
        return authorHomePage;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusString() {
        if (status == XProjectConstants.PLUGIN_NOT_LOADED) return notLoadedPlugin; else if (status == XProjectConstants.PLUGIN_LOADED) return loadedPlugin; else if (status == XProjectConstants.PLUGIN_INVALID) return invalidPlugin; else if (status == XProjectConstants.PLUGIN_INITIATED) return initiatedPlugin; else if (status == XProjectConstants.PLUGIN_STARTED) return startedPlugin; else if (status == XProjectConstants.PLUGIN_STOPPED) return stoppedPlugin; else return "";
    }

    public int getDock() {
        return dock;
    }

    public void setDock(int new_dock) {
        int old_dock = dock;
        dock = new_dock;
        propChangeSupport.firePropertyChange("Dock", dock, old_dock);
    }

    public void setDockString(String new_dock) {
        if (new_dock.equalsIgnoreCase(floatablePlugin)) setDock(XProjectConstants.PLUGIN_DOCK_FLOATABLE); else if (new_dock.equalsIgnoreCase(dockablePlugin)) setDock(XProjectConstants.PLUGIN_DOCK_DOCKABLE);
    }

    public String getDockString() {
        if (dock == XProjectConstants.PLUGIN_DOCK_FLOATABLE) return floatablePlugin; else if (dock == XProjectConstants.PLUGIN_DOCK_DOCKABLE) return dockablePlugin; else return "";
    }

    public static Iterator getPluginDocks() {
        return pluginDocks.iterator();
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean auto_start) {
        boolean old_auto_start = autoStart;
        autoStart = auto_start;
        propChangeSupport.firePropertyChange("AutoStart", autoStart, old_auto_start);
    }

    public String getAutoStartString() {
        if (autoStart) return autoPlugin; else return nonAutoPlugin;
    }

    private void setStatus(int new_status) {
        int old_status = status;
        status = new_status;
        propChangeSupport.firePropertyChange("Status", status, old_status);
    }

    private Component myComponent = null;

    private XOptionPage options = null;

    private ImageIcon icon = null;

    private String iconPath = "";

    private String version = "";

    private String releaseDate = "";

    private String description = "";

    private String documentationFile = "";

    private String extraResource = "";

    private String authorName = "";

    private String authorEMail = "";

    private String authorHomePage = "";

    private final String DTD_SYMBOL = "xbrowser:plugin-jar";

    private final String DTD_URL = XProjectConstants.DTD_DIR + "XPlugin-jar.dtd";

    private int status = XProjectConstants.PLUGIN_NOT_LOADED;

    private int dock = XProjectConstants.PLUGIN_DOCK_FLOATABLE;

    private boolean autoStart = false;

    private ClassLoader pluginClassLoader = null;

    private PropertyChangeSupport propChangeSupport = null;

    private XPlugin plugin = null;

    private static final String notLoadedPlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Status.NotLoadedPlugin");

    private static final String loadedPlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Status.LoadedPlugin");

    private static final String invalidPlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Status.InvalidPlugin");

    private static final String initiatedPlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Status.InitiatedPlugin");

    private static final String startedPlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Status.StartedPlugin");

    private static final String stoppedPlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Status.StoppedPlugin");

    private static final String dockablePlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Dock.DockablePlugin");

    private static final String floatablePlugin = XRepository.getResourceManager().getProperty(XPluginObject.class, "Dock.FloatablePlugin");

    private static final LinkedList pluginDocks = new LinkedList();

    static {
        pluginDocks.add(dockablePlugin);
        pluginDocks.add(floatablePlugin);
    }

    private final String autoPlugin = XRepository.getResourceManager().getProperty(this, "AutoStart.Yes");

    private final String nonAutoPlugin = XRepository.getResourceManager().getProperty(this, "AutoStart.No");
}
