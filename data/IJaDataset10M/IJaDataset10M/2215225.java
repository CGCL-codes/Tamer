package org.nbrowse.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import org.jnet.modelset.ModelLoader;
import org.jnet.modelset.ModelSet;
import org.jnet.shape.Shape;
import org.jnet.util.Escape;
import org.jnet.viewer.JnetConstants;
import org.jnet.viewer.Viewer;
import org.nbrowse.dataset.GraphEltSet;
import org.nbrowse.dataset.MetaData;
import org.nbrowse.dataset.MetaDataManager;
import org.nbrowse.layouts.spring.vrmlgraph.GraphData;
import org.nbrowse.utils.ErrorUtil;
import org.nbrowse.utils.NBrowseConstants;
import org.nbrowse.views.ColorManager;
import org.nbrowse.views.EdgeTableView;
import org.nbrowse.views.GraphViewI;
import org.nbrowse.views.GraphViewManager;
import org.nbrowse.views.InfoNodeMan;
import org.nbrowse.views.Jnet;
import org.nbrowse.views.JnetManager;
import org.nbrowse.views.JnetPanel;
import org.nbrowse.views.NBSelectionManager;
import org.nbrowse.views.Navigation;
import org.nbrowse.views.StepManager;
import org.nbrowse.views.edgecontrol.EdgeFilterState;

/**
 * singleton? should this be in gui?
 * @author mgibson
 */
class FileDialog {

    boolean useFileFilters = true;

    private static final boolean useCustomTypesCheck = true;

    private static final boolean useRawData = false;

    private static final boolean useSerialBinary = true;

    private DataLoader dataLoader;

    private JDialog dlg;

    private JTextField fileField;

    private JTextField startPointField;

    private JCheckBox dbCoordCheckBox;

    private JCheckBox customTypesCheckBox;

    private boolean isSessionFile = false;

    private static boolean dontShowTabFileLoadWarningMsg = false;

    FileDialog(DataLoader dl, boolean isSave, boolean isSession) {
        dataLoader = dl;
        if (isSave) return;
        if (isSession) dlg = new JDialog(dl.getParentFrame(), "Open Nbrowse Session File"); else dlg = new JDialog(dl.getParentFrame(), "Open Nbrowse Text File");
        isSessionFile = isSession;
        if (useCustomTypesCheck) dlg.setSize(360, 130); else dlg.setSize(360, 120);
        dlg.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        dlg.add(new JLabel("File "), c);
        fileField = new JTextField();
        Dimension d = new Dimension(200, 18);
        fileField.setPreferredSize(d);
        fileField.setMinimumSize(d);
        Load loadActn = null;
        LoadSession loadSessionActn = null;
        if (isSession) {
            loadSessionActn = new LoadSession();
            fileField.addActionListener(loadSessionActn);
        } else {
            loadActn = new Load();
            fileField.addActionListener(loadActn);
        }
        dlg.add(fileField, c);
        JButton browse = new JButton("Browse");
        dlg.add(browse, c);
        browse.addActionListener(new Browse());
        c.gridy += 2;
        if (!isSession) {
            dlg.add(new JLabel("Starting Point "), c);
            startPointField = new JTextField();
            startPointField.setPreferredSize(d);
            startPointField.setMinimumSize(d);
            startPointField.addActionListener(loadActn);
            dlg.add(startPointField, c);
            ++c.gridy;
            c.gridx = 1;
            dbCoordCheckBox = new JCheckBox("Cooperate with DB");
            dbCoordCheckBox.setSelected(true);
            dlg.add(dbCoordCheckBox, c);
            if (useCustomTypesCheck) {
                ++c.gridy;
                customTypesCheckBox = new JCheckBox("Use custom edge types");
                customTypesCheckBox.setSelected(true);
                dlg.add(customTypesCheckBox, c);
            }
        }
        ++c.gridy;
        c.gridx = 1;
        JButton loadBut = new JButton("Load");
        if (isSession) loadBut.addActionListener(loadSessionActn); else loadBut.addActionListener(loadActn);
        dlg.add(loadBut, c);
        ++c.gridx;
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new Cancel());
        dlg.add(cancel, c);
        Point p = new Point(0, 0);
        if (dlg.getParent().isVisible()) p = dlg.getParent().getLocationOnScreen();
        dlg.setLocation(p.x + 60, p.y + 60);
        dlg.setVisible(true);
    }

    private class Browse implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                File file = getFileFromUser(false, isSessionFile);
                fileField.setText(file.getAbsolutePath());
            } catch (NoFile ex) {
            }
        }
    }

    private class LoadSession implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            loadSessionFile();
        }
    }

    private class Load implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (dbCoordCheckBox.isSelected()) {
                if (!dontShowTabFileLoadWarningMsg) {
                    JCheckBox checkbox = new JCheckBox("Do not show this message again.");
                    String message = "If Using Node Synonyms - Make Sure They Are Unique!";
                    Object[] params = { message, checkbox };
                    JOptionPane.showMessageDialog(dataLoader.getParentFrame(), params, "Warning: Using Node Synonyms", JOptionPane.WARNING_MESSAGE);
                    boolean dontShow = checkbox.isSelected();
                    if (dontShow) dontShowTabFileLoadWarningMsg = true;
                }
            }
            loadFile();
        }
    }

    private String getStartingPoint() {
        return startPointField.getText();
    }

    private boolean haveStartingPoint() {
        return getStartingPoint() != null && !getStartingPoint().trim().equals("");
    }

    private boolean getCoordWithDb() {
        return dbCoordCheckBox.isSelected();
    }

    private boolean getCustomTypes() {
        return customTypesCheckBox.isSelected();
    }

    private File getFile() throws NoFile {
        String input = fileField.getText();
        if (input == null || input.trim().equals("")) throw new NoFile();
        File f = new File(input);
        if (!f.canRead()) {
            ErrorUtil.urgent(this, "Cant read file " + input);
            throw new NoFile();
        }
        DataAdapterI d = getFileAdapter(null);
        if (d instanceof RamAdapter) {
            RamAdapter ram = (RamAdapter) d;
            if (input.toLowerCase().endsWith(".sif")) {
                ram.setSIFFile(true);
            } else ram.setSIFFile(false);
        }
        return f;
    }

    private void loadSessionFile() {
        if (useSerialBinary) {
            File sFile;
            try {
                sFile = getFile();
            } catch (NoFile n) {
                return;
            }
            try {
                FileInputStream f_in = new FileInputStream(sFile.getAbsolutePath());
                JnetManager.inst().clearSerialLoad();
                ObjectInputStream obj_in = new ObjectInputStream(f_in);
                Object ges_obj = obj_in.readObject();
                if (ges_obj instanceof GraphEltSet) {
                    GraphEltSet.getCurrentGraph().clearAll();
                    GraphEltSet.setCurrentGraph((GraphEltSet) ges_obj);
                }
                Object gd_obj = obj_in.readObject();
                if (gd_obj instanceof GraphData) {
                    GraphData.getCurrentGraphData().clear();
                    GraphData.setCurrentGraphData((GraphData) gd_obj);
                    GraphViewManager.refreshGraphData();
                }
                Object viewer = obj_in.readObject();
                if (viewer instanceof Viewer) {
                    JnetManager.inst().loadJnetViewer((Viewer) viewer);
                }
                Object edgeFilterState = obj_in.readObject();
                if (edgeFilterState instanceof EdgeFilterState) {
                    EdgeFilterState.inst().setEdgeFilterState((EdgeFilterState) edgeFilterState);
                    GraphEltSet.setFilter(EdgeFilterState.inst());
                }
                Object stepMan = obj_in.readObject();
                if (stepMan instanceof StepManager) {
                    StepManager stepper = (StepManager) stepMan;
                    int dispStep = stepper.getDisplayStep();
                    int dispDepth = stepper.getStepQueryDepth();
                    StepManager.inst().setDisplayStepDepth((short) dispStep, (short) dispDepth);
                }
                try {
                    Object map = obj_in.readObject();
                    if (map instanceof HashMap) {
                        HashMap<String, Color> colorMap = (HashMap) map;
                        HashMap<String, Color> newColorMap = ColorManager.inst().getTypeToColor();
                        newColorMap.clear();
                        Set<String> keySet = colorMap.keySet();
                        Iterator<String> it = keySet.iterator();
                        while (it.hasNext()) {
                            String key = it.next();
                            Color val = colorMap.get(key);
                            newColorMap.put(key, val);
                        }
                    }
                } catch (Exception ex2) {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                dlg.dispose();
                ErrorUtil.urgent(this, "Failed Loading Session File", ex);
                return;
            }
            dlg.dispose();
            int expandedStep = StepManager.inst().getStepQueryDepth();
            GraphViewI fullGraph = GraphViewManager.current();
            ((GraphData) fullGraph).doRefresh(expandedStep);
            StatusManager.inst().newSerialDataDoneLoading();
            StatusManager.inst().newSerialDataDoneLoadingOver();
            return;
        }
    }

    private void loadFile() {
        if (getCoordWithDb() && !haveStartingPoint()) {
            ErrorUtil.urgent(this, "Starting point required to coord with DB");
            return;
        }
        File file;
        try {
            file = getFile();
        } catch (NoFile n) {
            return;
        }
        DataAdapterI d = getFileAdapter(file);
        if (haveStartingPoint()) d.setStartingPoint(getStartingPoint()); else d.setStartingPoint(null);
        d.setMergeFileAndDb(getCoordWithDb());
        if (useCustomTypesCheck) d.useCustomTypes(getCustomTypes());
        StatusManager.inst().newMetaDataLoading();
        StatusManager.inst().loadingNewData();
        try {
            GraphEltSet g = d.load();
            MetaDataManager.inst().setHasUserDefinedTypes(true);
            if (g == null) {
                ErrorUtil.urgent(this, "Failed to load file - no graph found");
                return;
            }
            if (g.getAllNodes() == null || g.getAllNodes().isEmpty()) {
                ErrorUtil.urgent(this, "Failed to load file - graph has no gene interactions found");
                return;
            }
            dataLoader.setNewGraph(g);
            if (useRawData) {
                if (dbCoordCheckBox.isSelected()) StatusManager.inst().newDataDoneLoading(); else StatusManager.inst().newRawDataDoneLoading();
            } else StatusManager.inst().newDataDoneLoading();
            dlg.dispose();
        } catch (Exception ex) {
            ErrorUtil.urgent(this, "Load file failed", ex);
        }
    }

    private class Cancel implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            dlg.dispose();
        }
    }

    class NoFile extends Exception {
    }

    private final JFileChooser fileChooser = new JFileChooser();

    public class EdgeDataFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            return true;
        }

        public String getDescription() {
            return "Gene Interaction Data Text File";
        }
    }

    public class SessionDataFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            return true;
        }

        public String getDescription() {
            return "Session Data File";
        }
    }

    public class GeneListFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            return true;
        }

        public String getDescription() {
            return "Gene List Text File";
        }
    }

    public class SIFFileFilter extends javax.swing.filechooser.FileFilter {

        public boolean accept(File file) {
            return (file.getName().toLowerCase().endsWith(".sif") || file.isDirectory());
        }

        public String getDescription() {
            return "Cytoscape SIF Text File";
        }
    }

    File getFileFromUser(boolean save, boolean isSession) throws NoFile {
        if (useFileFilters) {
            fileChooser.resetChoosableFileFilters();
            if (save && !isSession) {
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new GeneListFileFilter());
                fileChooser.addChoosableFileFilter(new SIFFileFilter());
                fileChooser.addChoosableFileFilter(new EdgeDataFileFilter());
            } else if (!isSession) {
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.addChoosableFileFilter(new SIFFileFilter());
                fileChooser.addChoosableFileFilter(new EdgeDataFileFilter());
            } else {
            }
        }
        int returnVal = save ? fileChooser.showSaveDialog(prntFrm()) : fileChooser.showOpenDialog(prntFrm());
        if (returnVal != JFileChooser.APPROVE_OPTION) throw new NoFile();
        File selectedFile = fileChooser.getSelectedFile();
        DataAdapterI adapter = getFileAdapter(selectedFile);
        if (adapter instanceof RamAdapter) {
            boolean isGeneList = isGeneListFilter();
            RamAdapter ramad = (RamAdapter) adapter;
            ramad.setGeneList(isGeneList);
            boolean isSIFFile = isSIFFileFilter();
            ramad.setSIFFile(isSIFFile);
        }
        if ((isSIFFileFilter()) && (!fileChooser.getSelectedFile().getName().toLowerCase().endsWith(".sif"))) {
            File newFile = new File(selectedFile.getAbsoluteFile() + ".sif");
            fileChooser.setSelectedFile(newFile);
        }
        return fileChooser.getSelectedFile();
    }

    private Frame prntFrm() {
        return dataLoader.getParentFrame();
    }

    private boolean isSIFFileFilter() {
        boolean isSIFFile = false;
        FileFilter filter = fileChooser.getFileFilter();
        if (filter.getDescription().equals("Cytoscape SIF Text File")) isSIFFile = true;
        return isSIFFile;
    }

    private boolean isGeneListFilter() {
        boolean isGeneList = false;
        FileFilter filter = fileChooser.getFileFilter();
        if (filter.getDescription().equals("Gene List Text File")) isGeneList = true;
        return isGeneList;
    }

    public void saveFile(boolean isSession) {
        try {
            File file = getFileFromUser(true, isSession);
            if (useSerialBinary && isSession) {
                file.delete();
                InfoNodeMan.inst().resetSplitterSave();
                GraphEltSet currGraph = GraphEltSet.getCurrentGraph();
                GraphData graphData = GraphData.getCurrentGraphData();
                StepManager stepMan = StepManager.inst();
                ModelSet modelset = JnetManager.inst().getJnetViewer().getModelSet();
                Navigation navigate = Navigation.inst();
                NBSelectionManager nbselection = NBSelectionManager.inst();
                Viewer viewer = JnetManager.inst().getJnetViewer();
                EdgeFilterState edgeFilterState = EdgeFilterState.inst();
                ColorManager cmanager = ColorManager.inst();
                try {
                    FileOutputStream f_out = new FileOutputStream(file.getAbsoluteFile());
                    ObjectOutputStream obj_out = new ObjectOutputStream(f_out);
                    obj_out.writeObject(currGraph);
                    obj_out.writeObject(graphData);
                    obj_out.writeObject(viewer);
                    obj_out.writeObject(edgeFilterState);
                    obj_out.writeObject(stepMan);
                    HashMap<String, Color> colorMap = cmanager.getTypeToColor();
                    obj_out.writeObject(colorMap);
                    obj_out.flush();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    ErrorUtil.urgent(this, "Failed Saving Session File", ex);
                    return;
                }
                InfoNodeMan.inst().resetSplitterLoad();
                String msg = "Session File Is Saved";
                JOptionPane.showMessageDialog(JnetManager.inst().getJnetViewer().getAwtComponent(), "Session Saved", msg, JOptionPane.INFORMATION_MESSAGE, UrlManager.inst().getInformationIcon());
                return;
            }
            DataAdapterI adapter = getFileAdapter(file);
            adapter.save();
        } catch (NoFile ex) {
        }
    }

    private DataAdapterManager dataMan() {
        return DataAdapterManager.inst();
    }

    private DataAdapterI getFileAdapter(File file) {
        DataAdapterI adap = dataMan().getFileAdapter(file);
        adap.setFile(file);
        return adap;
    }
}
