package net.sf.xpontus.plugins.browser;

import java.awt.Dimension;
import java.awt.Frame;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.plugins.SimplePluginDescriptor;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import org.java.plugin.registry.Identity;
import org.java.plugin.registry.PluginRegistry;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.text.StrBuilder;
import org.java.plugin.registry.PluginDescriptor;

/**
 * @version 0..0.1
 * @author Yves Zoundi
 */
public class InstallDownloadedPluginsController {

    private JFileChooser chooser;

    private DownloadedPanel view;

    private PluginRegistry registry;

    public JFileChooser getFileChooser() {
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setDialogTitle("Select XPontus plugin archive");
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(new XPontusModuleFileFilter());
            chooser.setAcceptAllFileFilterUsed(false);
        }
        return chooser;
    }

    public InstallDownloadedPluginsController() {
    }

    public PluginRegistry getRegistry() {
        if (registry == null) {
            registry = XPontusPluginManager.getPluginManager().getRegistry();
        }
        return registry;
    }

    public DownloadedPanel getView() {
        return view;
    }

    public void setView(DownloadedPanel view) {
        this.view = view;
    }

    private void collectManifests(final File file, final Set manifests, final Set archives) throws Exception {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".jpa")) {
            archives.add(file.toURL());
            return;
        }
        URL url = PluginsUtils.getInstance().getManifestUrl(file);
        if (url != null) {
            manifests.add(url);
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                collectManifests(files[i], manifests, archives);
            }
        }
    }

    Map addManifests(final File[] files) throws Exception {
        if (files.length == 0) {
            return null;
        }
        try {
            Set manifests = new HashSet();
            Set archives = new HashSet();
            for (int i = 0; i < files.length; i++) {
                collectManifests(files[i], manifests, archives);
            }
            Map<String, Identity> m_map = getRegistry().register((URL[]) manifests.toArray(new URL[manifests.size()]));
            return m_map;
        } catch (Exception e) {
            throw e;
        }
    }

    public void findRowForPlugin(String id) {
        JTable table = view.getPluginsTable();
        int selected = table.getSelectedRow();
        DefaultTableModel m_model = (DefaultTableModel) table.getModel();
        m_model.removeRow(selected);
    }

    public Object licenseAccepted(String license) {
        JScrollPane sp = new JScrollPane();
        Dimension dimension = new Dimension(300, 300);
        sp.setMinimumSize(dimension);
        sp.setPreferredSize(dimension);
        JTextArea licenseField = new JTextArea();
        licenseField.setEditable(false);
        licenseField.setLineWrap(true);
        try {
            InputStream is = new URL(license).openStream();
            Reader reader = new InputStreamReader(is);
            licenseField.read(reader, null);
        } catch (Exception e) {
            licenseField.setText("Cannot get license text information");
        }
        sp.getViewport().add(licenseField);
        Frame frame = (Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent();
        JOptionPane pane = new JOptionPane(sp, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, new Object[] { "Yes", "No" });
        JDialog dialog = pane.createDialog(frame, "License agreement");
        dialog.setVisible(true);
        Object selectedValue = pane.getValue();
        return selectedValue;
    }

    public void installPlugin() {
        JTable table = view.getPluginsTable();
        int selected = table.getSelectedRow();
        if (selected == -1) {
            XPontusComponentsUtils.showErrorMessage("No plugin selected for installation");
            return;
        }
        DefaultTableModel m_model = (DefaultTableModel) table.getModel();
        Boolean toInstall = (Boolean) m_model.getValueAt(selected, 0);
        if (!toInstall.booleanValue()) {
            XPontusComponentsUtils.showErrorMessage("Please check the checbox to select the plugin for installation");
            return;
        }
        String id = (String) m_model.getValueAt(selected, 1);
        File pluginArchive = view.getFilesMap().get(id);
        try {
            Object answer = licenseAccepted(view.getPluginsMap().get(id).getLicense());
            if (answer == null || answer.equals("No")) {
                System.out.println("License not accepted");
                XPontusComponentsUtils.showErrorMessage("You must agree with the license terms");
            } else {
                XPontusComponentsUtils.showWarningMessage("The installation feature is just a demo. Too many bugs....");
                if (true) {
                    return;
                }
                Object o = addManifests(new File[] { pluginArchive });
                if (o == null) {
                    return;
                } else {
                    Map<String, Identity> m_map = (Map<String, Identity>) o;
                    if (m_map.size() < 1) {
                        XPontusComponentsUtils.showWarningMessage("No plugins added, maybe the plugin was already installed?");
                        return;
                    } else {
                        if (m_map.size() > 0) {
                            Iterator<String> it = m_map.keySet().iterator();
                            while (it.hasNext()) {
                                String cle = it.next();
                                Identity m_id = m_map.get(cle);
                                String pluginIdentifier = m_id.getId();
                                File archiveFile = view.getFilesMap().get(pluginIdentifier);
                                File destFolder = new File(XPontusConfigurationConstantsIF.XPONTUS_PLUGINS_DIR, pluginIdentifier);
                                if (!destFolder.exists()) {
                                    destFolder.mkdirs();
                                }
                                try {
                                    PluginsUtils.getInstance().unzip(archiveFile.getAbsolutePath(), destFolder.getAbsolutePath());
                                } catch (Exception err) {
                                    throw new Exception("Error extracting plugin archive");
                                }
                            }
                        }
                    }
                }
                PluginDescriptor pds = XPontusPluginManager.getPluginManager().getRegistry().getPluginDescriptor(id);
                SimplePluginDescriptor spd = PluginsUtils.toSimplePluginDescriptor(pds);
                PluginsUtils.getInstance().addToIndex(spd);
            }
        } catch (Exception e) {
            StrBuilder buf = new StrBuilder();
            if (e instanceof IllegalArgumentException) {
                buf.append("Please check if you are not missing");
                buf.appendNewLine();
                buf.append("a required plugin dependency");
                buf.appendNewLine();
                buf.append("XPontus cannot handle it for now!");
                buf.appendNewLine();
            }
            buf.append(e.getMessage());
            XPontusComponentsUtils.showErrorMessage(buf.toString());
            e.printStackTrace();
            try {
                getRegistry().unregister(new String[] { id });
            } catch (Exception oo) {
            }
        }
    }

    public void addPlugin() {
        int rep = getFileChooser().showOpenDialog(XPontusComponentsUtils.getTopComponent().getDisplayComponent());
        if (rep == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            DefaultTableModel tableModel = (DefaultTableModel) view.getPluginsTable().getModel();
            for (File selectedFile : selectedFiles) {
                try {
                    URL manifestURL = PluginsUtils.getInstance().getManifestUrl(selectedFile);
                    if (manifestURL != null) {
                        InputStream is = manifestURL.openStream();
                        SimplePluginDescriptor spd = net.sf.xpontus.utils.PluginResolverUtils.resolvePlugins(is);
                        if (spd.getId() != null) {
                            view.getFilesMap().put(spd.getId(), selectedFile);
                            view.getPluginsMap().put(spd.getId(), spd);
                            tableModel.addRow(new Object[] { new Boolean(false), spd.getId(), spd.getCategory(), spd.getBuiltin() });
                        }
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }
}
