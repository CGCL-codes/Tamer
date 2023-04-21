package org.jpf.pluginmanager;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.*;
import org.java.plugin.boot.*;
import org.java.plugin.registry.*;
import org.java.plugin.registry.Extension.Parameter;
import org.java.plugin.util.ExtendedProperties;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 *
 * @version $Id: CorePlugin.java, 2008/07/30
 */
public final class CorePlugin extends ApplicationPlugin implements Application {

    /**
     * This plug-in ID.
     */
    public static final String PLUGIN_ID = "org.jpf.pluginmanager";

    private File dataFolder;

    private JFrame frame;

    private JTabbedPane tabbedPane;

    private JDialog dialog;

    public static Vector<Connection> vCapsule = new Vector<Connection>();

    private JProgressBar statusBar;

    private Thread hilo;

    private Object objeto = new Object();

    /**
     * Returns folder where given plug-in can store it's data.
     * @param descr plug-in descriptor
     * @return plug-in data folder
     * @throws IOException if folder doesn't exist and can't be created
     */
    public File getDataFolder(final PluginDescriptor descr) throws IOException {
        File result = new File(dataFolder, descr.getId());
        if (!result.isDirectory() && !result.mkdirs()) {
            throw new IOException("can't create data folder " + result + " for plug-in " + descr.getId());
        }
        return result;
    }

    /**
     * @see org.java.plugin.Plugin#doStart()
     */
    @Override
    protected void doStart() throws Exception {
    }

    /**
     * @see org.java.plugin.Plugin#doStop()
     */
    @Override
    protected void doStop() throws Exception {
    }

    /**
     * @see org.java.plugin.boot.ApplicationPlugin#initApplication(
     *      ExtendedProperties, String[])
     */
    @Override
    protected Application initApplication(final ExtendedProperties config, final String[] args) throws Exception {
        dataFolder = new File(config.getProperty("dataFolder", "." + File.separator + "data"));
        dataFolder = dataFolder.getCanonicalFile();
        log.debug("data folder - " + dataFolder);
        if (!dataFolder.isDirectory() && !dataFolder.mkdirs()) {
            throw new Exception("data folder " + dataFolder + " not found");
        }
        return this;
    }

    /**
     * @see org.java.plugin.boot.Application#startApplication()
     */
    public void startApplication() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
    }

    protected void createAndShowGUI() {
        frame = new JFrame("Plugin Manager");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
        }
        tabbedPane = new JTabbedPane(SwingConstants.TOP);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setIconImage(new ImageIcon(getClass().getResource("app.gif")).getImage());
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosed(final WindowEvent e) {
                try {
                    saveState();
                    JOptionPane.getRootFrame().dispose();
                    Boot.stopApplication(CorePlugin.this);
                } catch (Exception ex) {
                }
                System.exit(0);
            }
        });
        ExtensionPoint toolExtPoint = getManager().getRegistry().getExtensionPoint(getDescriptor().getId(), "Tool");
        for (Extension ext : toolExtPoint.getConnectedExtensions()) {
            JPanel panel = new JPanel();
            panel.putClientProperty("extension", ext);
            Parameter descrParam = ext.getParameter("description");
            Parameter iconParam = ext.getParameter("icon");
            URL iconUrl = null;
            if (iconParam != null) {
                iconUrl = getManager().getPluginClassLoader(ext.getDeclaringPluginDescriptor()).getResource(iconParam.valueAsString());
            }
            tabbedPane.addTab(ext.getParameter("name").valueAsString(), (iconUrl != null) ? new ImageIcon(iconUrl) : null, panel, (descrParam != null) ? descrParam.valueAsString() : "");
        }
        tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent e) {
                JTabbedPane theTabbedPane = (JTabbedPane) e.getSource();
                JComponent toolComponent = (JComponent) theTabbedPane.getComponent(theTabbedPane.getSelectedIndex());
                activateTool(toolComponent);
            }
        });
        readState();
        frame.getContentPane().add(tabbedPane);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        dialog = new JDialog(frame, "Servidor a usar...", true);
        dialog.setUndecorated(true);
        dialog.setLocation(440, 340);
        JPanel controls = new JPanel();
        controls.setLayout(new BorderLayout());
        ImageIcon image = new ImageIcon(getClass().getResource("splash.png"));
        int x = image.getIconWidth();
        int y = image.getIconHeight();
        JLabel splash = new JLabel(image);
        controls.add(splash, BorderLayout.NORTH);
        statusBar = new JProgressBar();
        controls.add(statusBar, BorderLayout.CENTER);
        JLabel lStatus = new JLabel("Cargando...");
        controls.add(lStatus, BorderLayout.SOUTH);
        iniciaCuenta();
        y = y + 33;
        controls.setVisible(true);
        dialog.add(controls);
        dialog.setSize(x, y);
        dialog.setVisible(true);
    }

    protected void activateTool(final JComponent toolComponent) {
        Extension ext = (Extension) toolComponent.getClientProperty("extension");
        frame.setTitle("[CICESE] - " + ext.getParameter("name").valueAsString());
        Tool tool = (Tool) toolComponent.getClientProperty("toolInstance");
        if (tool == null) {
            try {
                getManager().activatePlugin(ext.getDeclaringPluginDescriptor().getId());
                ClassLoader classLoader = getManager().getPluginClassLoader(ext.getDeclaringPluginDescriptor());
                Class<?> toolCls = classLoader.loadClass(ext.getParameter("class").valueAsString());
                tool = (Tool) toolCls.newInstance();
                tool.init(toolComponent);
            } catch (Throwable t) {
                toolComponent.setLayout(new BorderLayout());
                toolComponent.add(new JLabel(t.toString()), BorderLayout.NORTH);
                JScrollPane scrollPane = new JScrollPane();
                toolComponent.add(scrollPane, BorderLayout.CENTER);
                StringBuffer sb = new StringBuffer();
                String nl = System.getProperty("line.separator");
                Throwable err = t;
                while (err != null) {
                    if (err != t) {
                        sb.append(nl).append("Caused by " + err).append(nl).append(nl);
                    }
                    StackTraceElement[] stackTrace = err.getStackTrace();
                    for (int i = 0; i < stackTrace.length; i++) {
                        sb.append(stackTrace[i].toString()).append(nl);
                    }
                    err = err.getCause();
                }
                JTextArea textArea = new JTextArea(sb.toString());
                textArea.setBackground(java.awt.SystemColor.control);
                textArea.setEditable(false);
                scrollPane.setViewportView(textArea);
                textArea.setCaretPosition(0);
                return;
            }
            toolComponent.putClientProperty("toolInstance", tool);
        }
    }

    void loadConfiguration() throws IOException {
        String fPath = null;
        SAXBuilder builder;
        Document docto;
        List capsule = null;
        Element root = new Element("activecloud");
        File conf = new File(getDataFolder(getDescriptor()), "configuration.xml");
        if (!conf.exists() && !conf.createNewFile()) {
            throw new IOException("can't create configuration file " + conf);
        }
        fPath = conf.getAbsolutePath();
        try {
            File xml = new File(fPath);
            if (xml.exists() == false) {
                xml.createNewFile();
            }
            builder = new SAXBuilder();
            docto = builder.build(xml);
            root = docto.getRootElement();
        } catch (JDOMException ex) {
            docto = new Document(root);
        }
        capsule = root.getChildren("capsule");
        Iterator i = capsule.iterator();
        while (i.hasNext()) {
            Element e = (Element) i.next();
            Connection objCon = new Connection();
            objCon.setNetID(e.getAttribute("id").getValue());
            objCon.setSubjectName(e.getChild("subject-name").getValue());
            objCon.setSubjectFile(e.getChild("subject-file").getValue());
            objCon.setObserverName(e.getChild("observer-name").getValue());
            objCon.setObserverFile(e.getChild("observer-file").getValue());
            objCon.setServer(e.getChild("server").getValue());
            try {
                objCon.setPort(Integer.parseInt(e.getChild("port").getValue()));
            } catch (Exception exc) {
                objCon.setPort(0);
            }
            objCon.setNetwork(e.getChild("network").getValue());
            vCapsule.addElement(objCon);
        }
    }

    private File getConfigFile() throws IOException {
        File result = new File(getDataFolder(getDescriptor()), "config.properties");
        if (!result.exists() && !result.createNewFile()) {
            throw new IOException("can't create configuration file " + result);
        }
        return result;
    }

    protected void saveState() {
        Properties props = new Properties();
        props.setProperty("window.X", "" + frame.getX());
        props.setProperty("window.Y", "" + frame.getY());
        props.setProperty("window.width", "" + frame.getWidth());
        props.setProperty("window.height", "" + frame.getHeight());
        JComponent toolComponent = (JComponent) tabbedPane.getComponent(tabbedPane.getSelectedIndex());
        Extension ext = (Extension) toolComponent.getClientProperty("extension");
        props.setProperty("window.activeTool", ext.getUniqueId());
        try {
            OutputStream strm = new FileOutputStream(getConfigFile(), false);
            try {
                props.store(strm, "This is automatically generated configuration file.");
            } finally {
                strm.close();
            }
        } catch (Exception e) {
            log.error("can't save program state", e);
        }
    }

    private void readState() {
        Properties props = new Properties();
        try {
            InputStream strm = new FileInputStream(getConfigFile());
            try {
                props.load(strm);
            } finally {
                strm.close();
            }
        } catch (Exception e) {
            log.error("can't load program state", e);
        }
        frame.setBounds(Integer.parseInt(props.getProperty("window.X", "10"), 10), Integer.parseInt(props.getProperty("window.Y", "10"), 10), Integer.parseInt(props.getProperty("window.width", "500"), 10), Integer.parseInt(props.getProperty("window.height", "500"), 10));
        String activeTool = props.getProperty("window.activeTool");
        if (activeTool == null) {
            activateTool((JComponent) tabbedPane.getComponent(tabbedPane.getSelectedIndex()));
        } else {
            boolean activated = false;
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                JComponent toolComponent = (JComponent) tabbedPane.getComponent(i);
                Extension ext = (Extension) toolComponent.getClientProperty("extension");
                if (activeTool.equals(ext.getUniqueId())) {
                    activateTool(toolComponent);
                    tabbedPane.setSelectedIndex(i);
                    activated = true;
                    break;
                }
            }
            if (!activated) {
                activateTool((JComponent) tabbedPane.getComponent(tabbedPane.getSelectedIndex()));
            }
        }
    }

    public void iniciaCuenta() {
        if (hilo == null) {
            hilo = new ThreadCarga();
            hilo.start();
        }
    }

    class ThreadCarga extends Thread {

        public void run() {
            final int min = 0;
            final int max = 50;
            statusBar.setValue(min);
            statusBar.setMinimum(min);
            statusBar.setMaximum(max);
            for (int i = min; i <= max; i++) {
                statusBar.setValue(i);
                synchronized (objeto) {
                    try {
                        objeto.wait(50);
                    } catch (final InterruptedException e) {
                    }
                }
            }
            try {
                loadConfiguration();
            } catch (final IOException e1) {
                System.out.println("Error al cargar configuración...");
                e1.printStackTrace();
            }
            hilo = null;
            dialog.setVisible(false);
        }
    }
}
