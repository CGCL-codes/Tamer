package uk.co.zenly.jllama.gui;

import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Properties;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.log4j.Logger;
import uk.co.zenly.jllama.LocalProperties;
import uk.co.zenly.jllama.data.CollectorTemplate;
import uk.co.zenly.jllama.data.Device;
import uk.co.zenly.jllama.gui.tree.DeviceTree;
import uk.co.zenly.jllama.httpLoad.HttpLoadGraphFrame;
import uk.co.zenly.jllama.httpLoad.HttpTestFrame;
import uk.co.zenly.jllama.httpLoad.PlotThreads;
import uk.co.zenly.jllama.httpLoad.PlotVelocity;
import uk.co.zenly.jllama.sql.CollectorTemplateDAO;
import uk.co.zenly.jllama.sql.DataPointDAO;
import uk.co.zenly.jllama.sql.DeviceDAO;
import uk.co.zenly.jllama.sql.HttpLoadResultDAO;
import uk.co.zenly.jllama.workers.Runner;

/**
 * Main Application listener for application events
 * 
 * @author dougg
 *
 */
public class AppListener implements ActionListener {

    static AppListener appListener = new AppListener();

    public static JDesktopPane jDesktopPane;

    static JFrame parentJFrame;

    static MenuBar menuBar;

    static DeviceTree deviceTree;

    static Logger logger = Logger.getLogger(ApplicationWindow.class.getName());

    protected static Properties props = new LocalProperties();

    static AppListener getInstance() {
        return appListener;
    }

    /**
	 * React to menu selections.
	 */
    public void actionPerformed(ActionEvent e) {
        if ("refreshMenu".equals(e.getActionCommand())) {
        } else if ("newDevice".equals(e.getActionCommand())) {
            newDevice();
        } else if ("newCollectorTemplate".equals(e.getActionCommand())) {
            newCollectorTemplate();
        } else if ("deviceTree".equals(e.getActionCommand())) {
            deviceTree();
        } else if ("quit".equals(e.getActionCommand())) {
            quit();
        } else if (e.getActionCommand().startsWith("editDevice_")) {
            String ids = e.getActionCommand().replace("editDevice_", "");
            Integer id = new Integer(ids);
            editDevice(id);
            refreshDeviceTree();
        } else if (e.getActionCommand().startsWith("deleteDevice_")) {
            String ids = e.getActionCommand().replace("deleteDevice_", "");
            Integer id = new Integer(ids);
            deleteDevice(id);
        } else if (e.getActionCommand().startsWith("configureDevice_")) {
            String ids = e.getActionCommand().replace("configureDevice_", "");
            Integer id = new Integer(ids);
            configureDevice(id);
        } else if (e.getActionCommand().startsWith("editCollectorTemplate_")) {
            String ids = e.getActionCommand().replace("editCollectorTemplate_", "");
            Integer id = new Integer(ids);
            editCollectorTemplate(id);
        } else if (e.getActionCommand().startsWith("deleteCollectorTemplate_")) {
            String ids = e.getActionCommand().replace("deleteCollectorTemplate_", "");
            Integer id = new Integer(ids);
            deleteCollectorTemplate(id);
        } else if (e.getActionCommand().equals("startCollectors")) {
            Runner.startAllRunners();
            JOptionPane.showMessageDialog(jDesktopPane, "Collectors started");
            resetMenu();
        } else if (e.getActionCommand().equals("stopCollectors")) {
            Runner.stopAllRunners();
            JOptionPane.showMessageDialog(jDesktopPane, "Collectors stopped");
            resetMenu();
        } else if (e.getActionCommand().equals("eraseAllData")) {
            eraseAllData();
        } else if (e.getActionCommand().equals("import")) {
            importCollectorTemplate();
        } else if (e.getActionCommand().startsWith("export_")) {
            String ids = e.getActionCommand().replace("export_", "");
            Integer id = new Integer(ids);
            exportCollectorTemplate(id);
        } else if (e.getActionCommand().startsWith("about")) {
            AboutFrame a = new AboutFrame();
            a.setVisible(true);
            jDesktopPane.add(a);
            try {
                a.setSelected(true);
            } catch (java.beans.PropertyVetoException ex) {
            }
        } else if (e.getActionCommand().equals("httpTest")) {
            httpTest();
        } else if (e.getActionCommand().equals("plotVelocity")) {
            plotVelocity();
        } else if (e.getActionCommand().equals("plotThreads")) {
            plotThreads();
        } else if (e.getActionCommand().equals("help")) {
            help();
        } else {
            logger.warn("Slipped through action performed list and left with nothing.");
        }
    }

    public void importCollectorTemplate() {
        String path = System.getProperty("user.dir") + "/" + props.getProperty("template.directory") + "/.";
        logger.debug("Open file dialog starting in " + path);
        final JFileChooser fc = new JFileChooser();
        File f = new File(path);
        fc.setSelectedFile(f);
        int returnVal = fc.showOpenDialog(jDesktopPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            logger.debug("Opening template " + file.getName());
            CollectorTemplate ct = CollectorTemplateDAO.importFromPath(file);
            if (ct == null) {
                JOptionPane.showMessageDialog(jDesktopPane, "Could not import template from file", "Import error", JOptionPane.ERROR_MESSAGE);
            } else {
                CollectorTemplateFrame frame = new CollectorTemplateFrame(ct);
                frame.setVisible(true);
                jDesktopPane.add(frame);
                try {
                    frame.setSelected(true);
                } catch (java.beans.PropertyVetoException e) {
                }
            }
        } else {
            logger.debug("Open cancelled");
        }
    }

    /**
	 * Exports a collector template
	 * 
	 * @param ctId ID of the collector template
	 */
    public void exportCollectorTemplate(int ctId) {
        CollectorTemplate ct = CollectorTemplateDAO.getCollectorTemplate(ctId);
        String filename = ct.toString().replaceAll("\\s+", "_");
        filename = filename.replaceAll("[\\W\\s]", "");
        String path = System.getProperty("user.dir") + "/" + props.getProperty("template.directory") + "/" + filename + ".obj";
        final JFileChooser fc = new JFileChooser();
        File f = new File(path);
        fc.setSelectedFile(f);
        int returnVal = fc.showSaveDialog(jDesktopPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            logger.debug("Saving template \"" + ct + "\" to " + file.getName());
            CollectorTemplateDAO.exportToPath(ct, file);
        } else {
            logger.debug("Save cancelled");
        }
    }

    /**
	 * Create a New Device Input form.
	 */
    protected void newDevice() {
        Device d = new Device();
        DeviceFrame dFrame = new DeviceFrame(d);
        dFrame.setVisible(true);
        jDesktopPane.add(dFrame);
        try {
            dFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    /**
	 * Edit a Device
	 */
    protected void editDevice(int deviceId) {
        Device d = DeviceDAO.getDevice(deviceId);
        DeviceFrame dFrame = new DeviceFrame(d);
        dFrame.setVisible(true);
        jDesktopPane.add(dFrame);
        try {
            dFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    /**
	 * Configure a device with CollectorTemplates
	 */
    public void configureDevice(int deviceId) {
        Device d = DeviceDAO.getDevice(deviceId);
        configureDevice(d);
    }

    /**
	 * Configure a device with CollectorTemplates
	 */
    public void configureDevice(Device d) {
        ConfigureDeviceFrame dFrame = new ConfigureDeviceFrame(d);
        dFrame.setVisible(true);
        jDesktopPane.add(dFrame);
        try {
            dFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    /**
	 * Delete a Device
	 */
    protected void deleteDevice(int deviceId) {
        Device d = DeviceDAO.getDevice(deviceId);
        Object[] options = { "OK", "CANCEL" };
        int del = JOptionPane.showOptionDialog(null, "Are you sure you want to delete " + d + "?", "Delete", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        if (del == JOptionPane.YES_OPTION) {
            logger.debug("Deleteing " + d);
            DeviceDAO.deleteDevice(deviceId);
            resetMenu();
            refreshDeviceTree();
        }
    }

    protected void eraseAllData() {
        Object[] options = { "OK", "CANCEL" };
        int del = JOptionPane.showOptionDialog(null, "Are you sure you want to erase all data?", "Erase all data", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        if (del == JOptionPane.YES_OPTION) {
            logger.debug("Erasing all data upon user request");
            DataPointDAO.deleteAll();
            HttpLoadResultDAO.deleteAll();
            JOptionPane.showMessageDialog(jDesktopPane, "Removed all data");
        }
    }

    protected void newCollectorTemplate() {
        CollectorTemplate ct = new CollectorTemplate();
        CollectorTemplateFrame ctFrame = new CollectorTemplateFrame(ct);
        ctFrame.setVisible(true);
        jDesktopPane.add(ctFrame);
        try {
            ctFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    protected void editCollectorTemplate(int ctId) {
        CollectorTemplate ct = CollectorTemplateDAO.getCollectorTemplate(ctId);
        CollectorTemplateFrame ctFrame = new CollectorTemplateFrame(ct);
        ctFrame.setVisible(true);
        jDesktopPane.add(ctFrame);
        try {
            ctFrame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    protected void deleteCollectorTemplate(int ctId) {
        CollectorTemplate ct = CollectorTemplateDAO.getCollectorTemplate(ctId);
        Object[] options = { "OK", "CANCEL" };
        int del = JOptionPane.showOptionDialog(null, "Are you sure you want to delete " + ct + "?", "Delete", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
        if (del == JOptionPane.YES_OPTION) {
            logger.debug("Deleteing " + ct);
            CollectorTemplateDAO.deleteCollectorTemplate(ctId);
            resetMenu();
            refreshDeviceTree();
        }
    }

    protected void quit() {
        logger.debug("Quiting the application");
        System.exit(0);
    }

    /**
	 * Create a New Device Tree, or use the one we have already!
	 */
    protected void deviceTree() {
        if (deviceTree == null) {
            deviceTree = new DeviceTree(jDesktopPane);
            deviceTree.setVisible(true);
            jDesktopPane.add(deviceTree);
        } else {
            if (deviceTree.isClosed()) {
                logger.debug("DeviceTree is closed so re-add it to the jDesktopPane");
                jDesktopPane.add(deviceTree);
            }
            deviceTree.setVisible(true);
        }
        try {
            deviceTree.setClosed(false);
            deviceTree.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            logger.warn("Could not set Selected on the device tree");
        }
    }

    public static void resetMenu() {
        menuBar.removeAll();
        menuBar.removeNotify();
        menuBar = new MenuBar(jDesktopPane);
        parentJFrame.setJMenuBar(menuBar);
        menuBar.updateUI();
    }

    /**
	 * Refresh the device tree for changed devices and templates etc
	 */
    protected static void refreshDeviceTree() {
        if (deviceTree != null) {
            deviceTree.refresh();
        }
    }

    public static void setParentJFrame(JFrame parentJFrame) {
        AppListener.parentJFrame = parentJFrame;
    }

    public static void setMenuBar(MenuBar menuBar) {
        AppListener.menuBar = menuBar;
    }

    public static void setJDesktopPane(JDesktopPane desktopPane) {
        jDesktopPane = desktopPane;
    }

    /**
	 * Opens Help page
	 */
    public void help() {
        if (Desktop.isDesktopSupported()) {
            URI uri = URI.create(props.getProperty("help.url"));
            try {
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                logger.error(ex, ex);
            }
        }
    }

    /**
	 * Create a HTTP Test form.
	 */
    protected void httpTest() {
        HttpTestFrame frame = new HttpTestFrame();
        frame.setVisible(true);
        jDesktopPane.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }

    protected void plotVelocity() {
        new PlotVelocity();
    }

    protected void plotThreads() {
        new PlotThreads();
    }
}
