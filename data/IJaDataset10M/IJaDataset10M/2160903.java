package vademecum.ui.project;

import java.awt.Dimension;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.help.HelpSetException;
import javax.help.JHelp;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.Plugin;
import org.java.plugin.PluginClassLoader;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import vademecum.Core;
import vademecum.advisor.Advisor;
import vademecum.core.experiment.ExperimentNode;
import vademecum.ui.VademecumWindow;

public class DocBrowser extends JFrame {

    /**
	 *
	 */
    private static Log log = LogFactory.getLog(DocBrowser.class);

    /**
	 * static html file that displays a short introduction / welcome
	 */
    private static final String welcome = "file://" + System.getProperty("user.dir") + "/docs/user/test.html";

    private static DocBrowser instance;

    private JMenuBar menubar;

    private HelpSet hs;

    private JHelp hv;

    private DocBrowser() {
        this.setFocusable(true);
        initComponents();
        mergePluginHelpSets();
        this.pack();
    }

    private void mergePluginHelpSets() {
        ExtensionPoint ext = Core.manager.getRegistry().getExtensionPoint("vademecum.core@DataNode");
        Iterator exts = ext.getAvailableExtensions().iterator();
        while (exts.hasNext()) {
            Extension e = (Extension) exts.next();
            PluginClassLoader pcl = Core.manager.getPluginClassLoader(e.getDeclaringPluginDescriptor());
            URL url = pcl.getResource("/resources/javahelp/jhelpset.hs");
            if (url != null) {
                try {
                    log.debug(url);
                    log.debug("merging help of " + pcl.getPluginDescriptor().getId());
                    HelpSet helpset = new HelpSet(pcl, url);
                    hs.add(helpset);
                    log.debug("OK");
                } catch (HelpSetException e1) {
                    log.warn(e1.getMessage());
                }
            } else {
                log.debug("no helpset found for " + e.getUniqueId());
            }
        }
        ext = Core.manager.getRegistry().getExtensionPoint("vademecum.core@Visualizer");
        exts = ext.getAvailableExtensions().iterator();
        while (exts.hasNext()) {
            Extension e = (Extension) exts.next();
            PluginClassLoader pcl = Core.manager.getPluginClassLoader(e.getDeclaringPluginDescriptor());
            URL url = pcl.getResource("/resources/javahelp/jhelpset.hs");
            if (url != null) {
                try {
                    log.debug(url);
                    log.debug("merging help of " + pcl.getPluginDescriptor().getId());
                    HelpSet helpset = new HelpSet(pcl, url);
                    hs.add(helpset);
                    log.debug("OK");
                } catch (HelpSetException e1) {
                    log.warn(e1.getMessage());
                }
            } else {
                log.debug("no helpset found for " + e.getUniqueId());
            }
        }
    }

    public void jumpTo(String anchor) {
        try {
            hv.setCurrentID(anchor);
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
    }

    private void initComponents() {
        menubar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem item = new JMenuItem("close");
        item.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                DocBrowser.getInstance().setVisible(false);
            }
        });
        menu.add(item);
        menubar.add(menu);
        this.add(menubar);
        try {
            URL hsURL = new URL("file://" + System.getProperty("user.dir") + "/docs/user/jhelpset.hs");
            hs = new HelpSet(null, hsURL);
            hv = new JHelp(hs);
            this.add(hv);
        } catch (Exception ee) {
            log.warn(ee.getMessage());
        }
    }

    public static DocBrowser getInstance() {
        if (instance == null) instance = new DocBrowser();
        return instance;
    }
}
