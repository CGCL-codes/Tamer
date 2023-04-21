package eu.keep.gui.explorer;

import eu.keep.characteriser.Format;
import eu.keep.emulatorarchive.emulatorpackage.EmulatorPackage;
import eu.keep.gui.GUI;
import eu.keep.gui.common.InfoTableDialog;
import eu.keep.gui.util.RBLanguages;
import eu.keep.softwarearchive.pathway.ObjectFormatType;
import eu.keep.softwarearchive.pathway.Pathway;
import eu.keep.softwarearchive.softwarepackage.SoftwarePackage;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class FileExplorerPanel extends JPanel implements ActionListener {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public File selectedFile;

    private File clickedFile;

    private GUI parent;

    private JPanel explorerPanel;

    private JPanel noObjectPanel;

    private JButton autoStart;

    private JButton checkEnvironment;

    private JButton startWithoutObject;

    private JMenuItem info;

    private FileTree tree;

    public FileExplorerPanel(GUI p) {
        selectedFile = null;
        parent = p;
        initGUI();
    }

    private void initGUI() {
        super.setLayout(new BorderLayout(5, 5));
        noObjectPanel = initNoObjectPanel();
        explorerPanel = initExplorerPanel();
        super.add(explorerPanel, BorderLayout.SOUTH);
        super.add(noObjectPanel, BorderLayout.NORTH);
    }

    /**
     * Initialise the FileExplorer Panel
     * @return the FileExplorer Panel, containing a dropdown to select the root file-system,
     * 			a fileTree to browse to the desired file, and two buttons to start emulation
     * 			automatically or to select pathways, emulators and software manually.
     */
    private JPanel initExplorerPanel() {
        final File[] roots = File.listRoots();
        final JComboBox rootsCombo = new JComboBox(roots);
        tree = null;
        if (roots.length > 0) {
            File start = roots[0];
            for (File root : roots) {
                String name = root.getAbsolutePath().toUpperCase();
                if (!(name.startsWith("A:") || name.startsWith("B:"))) {
                    start = root;
                    break;
                }
            }
            rootsCombo.setSelectedItem(start);
            initFileTree(start);
        } else {
            logger.error("Could not read the file system.");
        }
        rootsCombo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                tree.setRoot(new FileTreeNode((File) rootsCombo.getSelectedItem()));
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 1));
        autoStart = new JButton();
        RBLanguages.set(autoStart, "autoStart");
        checkEnvironment = new JButton();
        RBLanguages.set(checkEnvironment, "checkEnvironment");
        autoStart.setEnabled(false);
        checkEnvironment.setEnabled(false);
        autoStart.addActionListener(this);
        checkEnvironment.addActionListener(this);
        buttonPanel.add(autoStart);
        buttonPanel.add(checkEnvironment);
        JPanel explorerPanel = new JPanel(new BorderLayout(5, 5));
        explorerPanel.setPreferredSize(new Dimension((GUI.WIDTH_UNIT * 40) - 30, GUI.HEIGHT - 255));
        Border border = new TitledBorder("");
        JPanel north = new JPanel(new BorderLayout(5, 5));
        JLabel explanation = new JLabel();
        explanation.setFont(new Font(null, Font.BOLD, 12));
        RBLanguages.set(explanation, "start_environment");
        north.add(explanation, BorderLayout.NORTH);
        explorerPanel.setBorder(border);
        north.add(rootsCombo, BorderLayout.SOUTH);
        explorerPanel.add(north, BorderLayout.NORTH);
        explorerPanel.add(new JScrollPane(tree), BorderLayout.CENTER);
        explorerPanel.add(buttonPanel, BorderLayout.SOUTH);
        return explorerPanel;
    }

    /**
     * Initialise the FileExplorer Panel
     * @return the FileExplorer Panel, containing a dropdown to select the root file-system,
     * 			a fileTree to browse to the desired file, and two buttons to start emulation
     * 			automatically or to select pathways, emulators and software manually.
     */
    private JPanel initNoObjectPanel() {
        startWithoutObject = new JButton();
        RBLanguages.set(startWithoutObject, "start_wo_object");
        startWithoutObject.setEnabled(true);
        startWithoutObject.addActionListener(this);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 1));
        buttonPanel.add(startWithoutObject);
        JPanel noObjectPanel = new JPanel();
        noObjectPanel.setLayout(new BorderLayout(5, 5));
        Border noObjectBorder = new TitledBorder("");
        noObjectPanel.setBorder(noObjectBorder);
        JLabel explanation = new JLabel();
        explanation.setFont(new Font(null, Font.BOLD, 12));
        RBLanguages.set(explanation, "start_environment_without");
        noObjectPanel.add(explanation, BorderLayout.NORTH);
        noObjectPanel.add(buttonPanel, BorderLayout.SOUTH);
        return noObjectPanel;
    }

    /**
     * Initialise the fileTree
     * @param start the root directory at the top of the tree
     */
    private void initFileTree(File start) {
        tree = new FileTree(new FileTreeNode(start));
        tree.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    try {
                        parent.getConfigPanel().clear();
                        File selected = tree.getSelectedFile();
                        if (selected != null) {
                            select(selected);
                            if (selected.isFile() && e.getClickCount() >= 2) {
                                doAutoStart();
                            }
                        }
                    } catch (Exception ex) {
                        logger.warn("warning: " + ExceptionUtils.getStackTrace(ex));
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                processPopupEvents(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                processPopupEvents(e);
            }

            private void processPopupEvents(MouseEvent e) throws HeadlessException {
                if (e.getButton() == MouseEvent.BUTTON3 && e.isPopupTrigger()) {
                    File clicked = tree.getClickedFile(e);
                    if (clicked.isFile()) {
                        clickedFile = clicked;
                        final JPopupMenu popUp = new JPopupMenu();
                        info = new JMenuItem();
                        RBLanguages.set(info, "properties");
                        info.addActionListener(FileExplorerPanel.this);
                        popUp.add(info);
                        popUp.show((Component) e.getSource(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == autoStart) {
            if (selectedFile != null) {
                doAutoStart();
            }
        } else if (e.getSource() == checkEnvironment) {
            parent.getConfigPanel().clear();
            parent.lock(RBLanguages.get("characterizing_file") + ": " + selectedFile + ", " + RBLanguages.get("log_please_wait") + "...");
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        java.util.List<Format> characteriserFormats = parent.model.characterise(selectedFile);
                        java.util.List<ObjectFormatType> allFormats = parent.model.getAllFileFormatsFromArchive();
                        if (characteriserFormats.isEmpty()) {
                            String warning = RBLanguages.get("could_not_determine_format") + ": " + selectedFile;
                            if (allFormats.isEmpty()) {
                                warning = warning + " " + RBLanguages.get("error_and_not_download_all_formats");
                                logger.error(warning);
                                parent.displayMessage(parent, warning, warning, JOptionPane.ERROR_MESSAGE);
                            } else {
                                warning = warning + ". " + RBLanguages.get("manually_select_file_fomat");
                                logger.warn(warning);
                                parent.loadFormats(characteriserFormats, allFormats);
                                parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                            }
                        } else {
                            if (allFormats.isEmpty()) {
                                logger.warn("Managed to characterise file " + selectedFile.getAbsolutePath() + ", but could not download the list of all supported file formats from the software archive. " + "This probably indicates a problem with the (connection to) the software archive.");
                                parent.unlock(RBLanguages.get("number_of_formats") + ": " + characteriserFormats.size() + ". " + RBLanguages.get("error_donwload_all_formats"));
                            } else {
                                logger.debug("Successfully characterised file " + selectedFile.getAbsolutePath() + " and downloaded list of all supported file formats.");
                                parent.unlock(RBLanguages.get("number_of_formats") + ": " + characteriserFormats.size() + ". " + RBLanguages.get("suggest_manually_select_file_fomat"));
                            }
                            parent.loadFormats(characteriserFormats, allFormats);
                        }
                    } catch (IOException ex) {
                        String error = RBLanguages.get("error") + ": " + ex.getMessage();
                        parent.displayMessage(parent, error, error, JOptionPane.ERROR_MESSAGE);
                    }
                }
            })).start();
        } else if (e.getSource() == startWithoutObject) {
            clearExplorerPanel();
            enableExplorerPanel(false);
            parent.lock(RBLanguages.get("downloading_all_pathways") + ". " + RBLanguages.get("log_please_wait"));
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    parent.getConfigPanel().loadNoObject();
                }
            })).start();
        } else if (e.getSource() == info) {
            parent.lock(RBLanguages.get("getting_meta_data") + ": " + clickedFile + ", " + RBLanguages.get("log_please_wait") + "...");
            (new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Map<String, java.util.List<String>> techMetaData = parent.model.getTechMetadata(clickedFile);
                        Map<String, java.util.List<String>> descMetaData = parent.model.getFileInfo(clickedFile);
                        String[][] data = new String[techMetaData.size() + descMetaData.size()][];
                        int index = 0;
                        for (Map.Entry<String, java.util.List<String>> entry : techMetaData.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue().toString();
                            data[index++] = new String[] { key, value.substring(1, value.length() - 1) };
                        }
                        for (Map.Entry<String, java.util.List<String>> entry : descMetaData.entrySet()) {
                            String key = entry.getKey();
                            String value = entry.getValue().toString();
                            data[index++] = new String[] { key, value.substring(1, value.length() - 1) };
                        }
                        new InfoTableDialog(parent, clickedFile, data);
                        parent.unlock(RBLanguages.get("done"));
                    } catch (IOException ex) {
                        String error = RBLanguages.get("error") + ": " + ex.getMessage();
                        parent.displayMessage(parent, error, error, JOptionPane.ERROR_MESSAGE);
                    }
                }
            })).start();
        }
    }

    /**
     * Start the automatic emulation process on the selected file.
     */
    private void doAutoStart() {
        parent.getConfigPanel().clear();
        checkEnvironment.setEnabled(false);
        parent.lock(RBLanguages.get("preparing_start_emulation") + ": " + selectedFile + ", " + RBLanguages.get("log_please_wait") + "...");
        (new Thread(new Runnable() {

            @Override
            public void run() {
                String warning = "";
                try {
                    java.util.List<Format> formats = parent.model.characterise(selectedFile);
                    if (formats.isEmpty()) {
                        warning = RBLanguages.get("could_not_determine_format") + ": " + selectedFile;
                        parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                    } else {
                        parent.loadFormats(formats);
                        parent.getConfigPanel().enableOptions(false);
                        Format frmt = formats.get(0);
                        java.util.List<Pathway> paths = parent.model.getPathways(frmt);
                        parent.getConfigPanel().enableOptions(false);
                        if (paths.isEmpty()) {
                            warning = RBLanguages.get("didnt_find_dependency") + ": " + frmt + ".";
                            parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                        } else {
                            parent.getConfigPanel().loadPathways(paths);
                            parent.getConfigPanel().enableOptions(false);
                            Pathway path = paths.get(0);
                            if (!parent.model.isPathwaySatisfiable(path)) {
                                warning = RBLanguages.get("not_satisfiable_path") + ": " + path;
                                parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                            } else {
                                Map<EmulatorPackage, List<SoftwarePackage>> emuMap = parent.model.matchEmulatorWithSoftware(path);
                                if (emuMap.isEmpty()) {
                                    warning = RBLanguages.get("didnt_find_emulator") + ": " + paths;
                                    parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                                } else {
                                    parent.getConfigPanel().loadEmus(emuMap);
                                    parent.getConfigPanel().enableOptions(false);
                                    List<SoftwarePackage> swList = null;
                                    EmulatorPackage emu = null;
                                    for (Map.Entry<EmulatorPackage, List<SoftwarePackage>> entry : emuMap.entrySet()) {
                                        emu = entry.getKey();
                                        swList = entry.getValue();
                                        if (!swList.isEmpty()) {
                                            break;
                                        }
                                    }
                                    if (swList == null || swList.isEmpty()) {
                                        warning = RBLanguages.get("no_software_package") + ": " + path;
                                        parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                                    } else {
                                        parent.getConfigPanel().loadSoftware(swList);
                                        parent.getConfigPanel().enableOptions(false);
                                        SoftwarePackage swPack = swList.get(0);
                                        int lastConfiguredID = parent.model.prepareConfiguration(selectedFile, emu, swPack, path);
                                        Map<String, List<Map<String, String>>> configMap = parent.model.getEmuConfig(lastConfiguredID);
                                        if (configMap.isEmpty()) {
                                            warning = RBLanguages.get("no_configuration") + ": " + swPack.getDescription();
                                            parent.displayMessage(parent, warning, warning, JOptionPane.WARNING_MESSAGE);
                                        } else {
                                            parent.getConfigPanel().loadConfiguration(configMap);
                                            parent.getConfigPanel().enableOptions(false);
                                            parent.model.setEmuConfig(configMap, lastConfiguredID);
                                            parent.model.runEmulationProcess(lastConfiguredID);
                                            parent.unlock(RBLanguages.get("emulation_started"));
                                            new InfoTableDialog(parent, selectedFile, emu, swPack);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (IOException ex) {
                    String error = RBLanguages.get("error") + ": " + ex.getMessage();
                    parent.displayMessage(parent, error, error, JOptionPane.ERROR_MESSAGE);
                }
                parent.getConfigPanel().enableOptions(true);
            }
        })).start();
    }

    private void select(File file) {
        if (file != null) {
            boolean isFile = file.isFile();
            autoStart.setEnabled(isFile);
            checkEnvironment.setEnabled(isFile);
            selectedFile = isFile ? file : null;
        }
    }

    /**
     * Enable/Disable this panel
     * @param enabled true to enable this panel, false to disable
     */
    @Override
    public void setEnabled(boolean enabled) {
        this.enableExplorerPanel(enabled);
        this.enableNoObjectPanel(enabled);
    }

    /**
     * Enable/Disable the FileExplorer panel
     * @param enabled true to enable the FileExplorer panel, false to disable
     */
    private void enableExplorerPanel(boolean enabled) {
        this.autoStart.setEnabled(enabled);
        this.checkEnvironment.setEnabled(enabled);
    }

    /**
     * Clear the FileExplorer panel
     */
    private void clearExplorerPanel() {
        tree.clearSelection();
        selectedFile = null;
        clickedFile = null;
    }

    /**
     * Enable/Disable the NoObject panel
     * @param enabled true to enable the NoObject panel, false to disable
     */
    private void enableNoObjectPanel(boolean enabled) {
        this.startWithoutObject.setEnabled(enabled);
    }
}
