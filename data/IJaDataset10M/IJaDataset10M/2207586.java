package preprocessing.newGUI;

import game.utils.CrossRoad;
import game.utils.DebugInfo;
import game.utils.FileChooserDialog;
import game.utils.FilePathLocator;
import preprocessing.MainPreprocessingDialogInterface;
import preprocessing.automatic.Population.IndividualSerializerFactory;
import preprocessing.storage.PreprocessingStorage;
import preprocessing.storage.SimplePreprocessingWarehouse;
import javax.management.InstanceNotFoundException;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: lagon
 * Date: May 4, 2008
 * Time: 9:55:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class PreprocessingSpreadsheet extends PreprocessingPanelSkeleton implements ActionListener, WindowListener, MouseListener {

    private JButton newButton;

    private JButton loadButton;

    private JButton saveButton;

    private JButton addInstButton;

    private JButton remInstButton;

    private JButton addAttrButton;

    private JButton remAttrButton;

    private JButton runButton;

    private JButton configureButton;

    private JButton desciptionButton;

    private JButton applyAutomaticSequence;

    private JTable table1;

    private PreprocessingTableModel model;

    public PreprocessingSpreadsheet(MainPreprocessingDialogInterface callbackPtr) {
        super(callbackPtr);
        tabName = "Data Preprocessing";
        createPanel();
        model = new PreprocessingTableModel();
        createUIComponents(model);
        model.setPreprocessingStore(CrossRoad.getInstance().getPreprocessingStorage(false));
    }

    private ImageIcon loadIcon(String iconName) throws FileNotFoundException {
        File f = new File(iconName);
        if (!f.exists()) {
            DebugInfo.putDebugMessage("Icon file " + iconName + " was not found. Do something about it!", DebugInfo.DebugLevel.D_ERROR);
            throw new FileNotFoundException("Icon file " + iconName + " was not found.");
        }
        return new ImageIcon(iconName);
    }

    void createUIComponents(PreprocessingTableModel model) {
        FilePathLocator locator = FilePathLocator.getInstance();
        try {
            newButton.setIcon(loadIcon(locator.getUIDataDirectory() + "new.png"));
            newButton.setText("");
            loadButton.setIcon(loadIcon(locator.getUIDataDirectory() + "open.png"));
            loadButton.setText("");
            runButton.setIcon(loadIcon(locator.getUIDataDirectory() + "play.png"));
            runButton.setText("");
            configureButton.setIcon(loadIcon(locator.getUIDataDirectory() + "config.png"));
            configureButton.setText("");
            addInstButton.setIcon(loadIcon(locator.getUIDataDirectory() + "addrow.png"));
            addInstButton.setText("");
            remInstButton.setIcon(loadIcon(locator.getUIDataDirectory() + "delrow.png"));
            remInstButton.setText("");
            addAttrButton.setIcon(loadIcon(locator.getUIDataDirectory() + "addcol.png"));
            addAttrButton.setText("");
            remAttrButton.setIcon(loadIcon(locator.getUIDataDirectory() + "delcol.png"));
            remAttrButton.setText("");
        } catch (FileNotFoundException e) {
        }
        addInstButton.setEnabled(false);
        remInstButton.setEnabled(false);
        addAttrButton.setEnabled(false);
        remAttrButton.setEnabled(false);
        newButton.addActionListener(this);
        loadButton.addActionListener(this);
        saveButton.addActionListener(this);
        runButton.addActionListener(this);
        configureButton.addActionListener(this);
        desciptionButton.addActionListener(this);
        applyAutomaticSequence.addActionListener(this);
        addInstButton.addActionListener(this);
        remInstButton.addActionListener(this);
        addAttrButton.addActionListener(this);
        remAttrButton.addActionListener(this);
        JTableHeader tableHead = table1.getTableHeader();
        tableHead.addMouseListener(this);
        PreprocessingTableEditor editor = new PreprocessingTableEditor();
        PreprocessingTableRenderer prepTabRndr = new PreprocessingTableRenderer();
        ListSelectionModel selectModel = new DefaultListSelectionModel();
        table1.setSelectionModel(selectModel);
        selectModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        model.setAttributeChangesAllowed(true);
        model.setEditor(editor);
        table1.setModel(model);
        table1.setDefaultRenderer(Object.class, prepTabRndr);
        table1.setDefaultEditor(Object.class, editor);
        table1.setCellSelectionEnabled(true);
        table1.setRowSelectionAllowed(true);
        table1.setColumnSelectionAllowed(true);
    }

    public void setPreprocessingStorage(PreprocessingStorage store) {
        model.setPreprocessingStore(store);
        model.fireTableDataChanged();
        model.fireTableStructureChanged();
    }

    public void preprocessingDataChange() {
        table1.repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newButton) {
            int ans = JOptionPane.showConfirmDialog(this, "Clean all stored game.data?", "Clean game.data?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (ans == 0) {
                DebugInfo.putDebugMessage("PreprocessingSpreadsheet: Wiping game.data", DebugInfo.DebugLevel.D_INFO);
                CrossRoad.getInstance().getPreprocessingStorage(false).wipeAllData();
                model.fireTableStructureChanged();
            }
            return;
        }
        if (e.getSource() == loadButton) {
            return;
        }
        if (e.getSource() == runButton) {
            int[] cols = table1.getSelectedColumns();
            runSelectedMethod(cols);
            model.dataChanged();
            model.fireTableStructureChanged();
            return;
        }
        if (e.getSource() == configureButton) {
            configureSelectedMethod();
            return;
        }
        if (e.getSource() == applyAutomaticSequence) {
            applyAutoMethods();
            return;
        }
        if (e.getSource() == desciptionButton) {
        }
    }

    private void applyAutoMethods() {
        FileChooserDialog fcd = FileChooserDialog.getInstance();
        fcd.removeAllFilters();
        JFileChooser fc = fcd.getFileChooser();
        fc.setDialogTitle("Select file with individual ...");
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                IndividualSerializerFactory.applySerializedIndividualToData(fc.getSelectedFile(), SimplePreprocessingWarehouse.getInstance().getMasterStorage());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error opening file with individual: " + e.getMessage(), "Error loading individual", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (InstanceNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error " + e.getMessage(), "Error loading individual", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
            model.fireTableStructureChanged();
        }
    }

    private void createPanel() {
        setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        add(panel2, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JToolBar toolBar1 = new JToolBar();
        panel2.add(toolBar1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(-1, 20), null, 0, false));
        newButton = new JButton();
        newButton.setText("New");
        newButton.setToolTipText("New dataset - erases all game.data.");
        toolBar1.add(newButton);
        loadButton = new JButton();
        loadButton.setText("Load");
        loadButton.setToolTipText("Import new dataset into GAME");
        toolBar1.add(loadButton);
        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.setToolTipText("Saves current dataset to file");
        toolBar1.add(saveButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator1);
        runButton = new JButton();
        runButton.setText("Run");
        runButton.setToolTipText("Executes selected preprocessing method");
        toolBar1.add(runButton);
        configureButton = new JButton();
        configureButton.setText("Configure");
        configureButton.setToolTipText("Configure selected preprocessing method");
        toolBar1.add(configureButton);
        desciptionButton = new JButton();
        desciptionButton.setText("Desciption");
        desciptionButton.setToolTipText("Show description of selected preprocessing method");
        toolBar1.add(desciptionButton);
        final JToolBar.Separator toolBar$Separator2 = new JToolBar.Separator();
        toolBar1.add(toolBar$Separator2);
        addInstButton = new JButton();
        addInstButton.setText("Add Inst");
        addInstButton.setToolTipText("Add new instance to dataset");
        toolBar1.add(addInstButton);
        remInstButton = new JButton();
        remInstButton.setText("Rem Inst");
        remInstButton.setToolTipText("Remove instance from given dataset.");
        toolBar1.add(remInstButton);
        addAttrButton = new JButton();
        addAttrButton.setText("Add attr");
        addAttrButton.setToolTipText("Add new empty attribute");
        toolBar1.add(addAttrButton);
        remAttrButton = new JButton();
        remAttrButton.setText("Rem attr");
        remAttrButton.setToolTipText("Remove given attribute");
        toolBar1.add(remAttrButton);
        applyAutomaticSequence = new JButton();
        applyAutomaticSequence.setText("Apply auto seq");
        applyAutomaticSequence.setToolTipText("Apply qutomatic sequence");
        toolBar1.add(applyAutomaticSequence);
        final JSplitPane splitPane1 = new JSplitPane();
        panel2.add(splitPane1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel3);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel3.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        methodTree = new JTree();
        scrollPane1.setViewportView(methodTree);
        final JScrollPane scrollPane2 = new JScrollPane();
        splitPane1.setRightComponent(scrollPane2);
        table1 = new JTable();
        scrollPane2.setViewportView(table1);
    }

    public void windowOpened(WindowEvent e) {
        callback.WindowOpened();
        model.fireTableStructureChanged();
        model.fireTableDataChanged();
    }

    public void windowClosing(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            int selCol = table1.getTableHeader().columnAtPoint(e.getPoint());
            if (selCol <= 0) return;
            System.out.printf("Selecting %s\n", selCol);
            table1.getSelectionModel().setLeadSelectionIndex(0);
            table1.getSelectionModel().addSelectionInterval(0, table1.getRowCount());
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
