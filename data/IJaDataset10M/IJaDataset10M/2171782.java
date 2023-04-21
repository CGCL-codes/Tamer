package org.merlotxml.merlot.plugin;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.merlotxml.merlot.MerlotConstants;
import org.merlotxml.merlot.MerlotDebug;
import org.merlotxml.merlot.MerlotOptionPane;
import org.merlotxml.merlot.MerlotResource;
import org.merlotxml.merlot.MerlotUtils;
import org.merlotxml.merlot.XMLEditorFrame;
import org.merlotxml.merlot.XMLEditorSettings;
import org.merlotxml.util.xml.XPathUtil;
import org.tolven.logging.TolvenLogger;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Merlot Plugin Configuration
 * 
 * @author Tim McCune
 */
public abstract class PluginConfig {

    protected static final String XPATH_TEXT = "/text()";

    protected static final String XPATH_PLUGIN = "/*";

    protected static final String XPATH_LONG_NAME = XPATH_PLUGIN + "/longName" + XPATH_TEXT;

    protected static final String XPATH_NAME = XPATH_PLUGIN + "/name" + XPATH_TEXT;

    protected static final String XPATH_VERSION = XPATH_PLUGIN + "/version" + XPATH_TEXT;

    protected static final String XPATH_AUTHOR = XPATH_PLUGIN + "/author" + XPATH_TEXT;

    protected static final String XPATH_URL = XPATH_PLUGIN + "/url" + XPATH_TEXT;

    protected static final String XPATH_DEPENDENCIES = XPATH_PLUGIN + "/dependency" + XPATH_TEXT;

    protected File source;

    protected String longName;

    protected String name;

    protected String version;

    protected String author;

    protected URL url;

    private Node _node;

    /** the classloader which this plugin uses to find its classes */
    protected ClassLoader classLoader;

    /** The plugin manager instance */
    private PluginManager _pluginManager;

    /** List of other PluginConfigs this plugin requires */
    private List _dependencies;

    protected PluginConfig(PluginManager manager, ClassLoader loader, File source) {
        this.classLoader = loader;
        this.source = source;
        _pluginManager = manager;
    }

    protected void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
	 * Parse the default elements common to all plugins.
	 *
	 * @exception MalformedURLException Thrown if a URL value was supplied that
	 *		is not a valid URL
	 * @exception SAXException Thrown if the configuration XML is incorrect
	 * @exception PluginConfigException Not thrown here, but declared in case
	 *		a subclass needs to throw it
	 */
    protected void parse(Node node) throws MalformedURLException, SAXException, PluginConfigException {
        String s;
        _node = node;
        name = XPathUtil.getValue(node, XPATH_NAME);
        longName = XPathUtil.getValue(node, XPATH_LONG_NAME);
        version = XPathUtil.getValue(node, XPATH_VERSION);
        author = XPathUtil.getValue(node, XPATH_AUTHOR);
        if ((s = XPathUtil.getValue(node, XPATH_URL)) != null) {
            url = new URL(s);
        }
        _dependencies = XPathUtil.getValueList(node, XPATH_DEPENDENCIES);
        String msg = MerlotResource.getString(MerlotConstants.UI, "splash.loadingPlugins.msg");
        XMLEditorSettings.getSharedInstance().showSplashStatus(msg + " " + name);
    }

    protected void resolveDependencies() throws PluginConfigException {
        if (_dependencies != null) {
            ArrayList tmpDependencies = new ArrayList();
            Iterator it = _dependencies.iterator();
            while (it.hasNext()) {
                String depName = (String) it.next();
                PluginConfig config = _pluginManager.getPlugin(depName);
                if (config == null) {
                    TolvenLogger.info(name + " depends on " + depName + " but the dependency couldn't be found.", PluginConfig.class);
                    _dependencies = null;
                    throw new PluginConfigException("Plugin '" + name + "' depends on '" + depName + "' but it was not found.");
                } else {
                    tmpDependencies.add(config);
                    ((PluginClassLoader) classLoader).addClassLoader(config.classLoader);
                    MerlotDebug.msg(name + " depends on " + depName);
                }
            }
            _dependencies = tmpDependencies;
        }
    }

    protected void init() throws PluginConfigException {
    }

    protected void setSource(File source) {
        this.source = source;
    }

    public File getSource() {
        return source;
    }

    public AbstractAction getAboutAction() {
        return new AboutAction();
    }

    public String getName() {
        return name;
    }

    public String toString() {
        StringBuffer rtn = new StringBuffer();
        rtn.append("name: " + name + "\n");
        rtn.append("long name: " + longName + "\n");
        rtn.append("version: " + version + "\n");
        rtn.append("author: " + author + "\n");
        rtn.append("url: " + url + "\n");
        rtn.append("myPluginManager: " + _pluginManager + "\n");
        return rtn.toString();
    }

    private class AboutAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        private static final int ABOUT_COLS = 50;

        public AboutAction() {
            putValue(MerlotConstants.ACTION_NAME, name);
        }

        public void actionPerformed(ActionEvent event) {
            JPanel panel;
            Object[] formatArgs = { name };
            String titleFormat = MerlotResource.getString(MerlotConstants.UI, "plugin.about.title");
            String title = MessageFormat.format(titleFormat, formatArgs);
            StringBuffer about = new StringBuffer();
            if (longName != null) {
                about.append(longName + " ");
            } else if (name != null) {
                about.append(name + " ");
            }
            if (version != null) {
                about.append(version + "\n");
            }
            if (author != null) {
                about.append(author + "\n");
            }
            if (url != null) {
                about.append(url + "\n");
            }
            panel = MerlotUtils.createMultiLineLabel(about.toString(), ABOUT_COLS);
            MerlotOptionPane.showInternalConfirmDialog(XMLEditorFrame.getSharedInstance().getDesktopPane(), panel, title, JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Node getNode() {
        return _node;
    }
}
