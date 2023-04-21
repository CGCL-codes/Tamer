package panels;

import core.COINCOMOComponent;
import core.COINCOMOSubComponent;
import core.COINCOMOSubSystem;
import core.COINCOMOUnit;
import database.COINCOMOComponentManager;
import database.COINCOMOSubComponentManager;
import database.COINCOMOComponentManager;
import database.COINCOMOSubSystemManager;
import dialogs.ComponentScaleFactorsDialog;
import dialogs.ComponentScheduleDriverDialog;
import dialogs.CotAssesCodeEAFTabDialog;
import dialogs.CotClassDialog;
import dialogs.CotGlueCodeEAFTabDialog;
import dialogs.CotTailoringCodeEAFTabDialog;
import dialogs.SubComponentEAFDialog;
import dialogs.SubComponentSizeDialog;
import dialogs.ViewCotsProjectsDialog;
import extensions.COINCOMOCheckBoxCellEditor;
import extensions.COINCOMOCheckBoxTableCellRenderer;
import extensions.COINCOMOClefTableHeaderRenderer;
import extensions.COINCOMOFixedTable;
import extensions.COINCOMOClefTableCellRenderer;
import extensions.COINCOMOComboBoxCellEditor;
import extensions.COINCOMOTreeNode;
import extensions.COINCOMOVector;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import main.COINCOMO;
import main.GlobalMethods;
import main.Icons;

/**
 *
 * @author Raed Shomali
 */
public class COCOTSComponentPanel extends JPanel implements ActionListener, ItemListener, MouseListener {

    public static COINCOMOComponent component = null;

    public JComboBox comboBox = new JComboBox();

    public static DefaultTableModel clefTableModel = new DefaultTableModel();

    public static COINCOMOFixedTable clefTable = new COINCOMOFixedTable(clefTableModel);

    public static JTextPane estimationTextPane = new JTextPane();

    JScrollPane estimationScroller = new JScrollPane(estimationTextPane);

    JSplitPane splitPane = new JSplitPane();

    static DecimalFormat format1Decimal = new DecimalFormat("0.0");

    static DecimalFormat format2Decimals = new DecimalFormat("0.00");

    static UpdateEstimationReport reportUpdater = null;

    static RefreshSubComponents refreshThread = null;

    static boolean isLoading = true;

    public static COINCOMOTreeNode selectedNode = null;

    /**
     * 
     * @param component is used to view its content on the panel
     */
    public COCOTSComponentPanel(COINCOMOComponent component) {
        COCOTSComponentPanel.component = component;
        COINCOMO.viewCotsProjectsMenuItem.addActionListener(this);
        comboBox.addItemListener(this);
        comboBox.setPreferredSize(new Dimension(125, 25));
        comboBox.setBackground(Color.WHITE);
        comboBox.setRenderer(new COINCOMOComboBoxCellEditor());
        comboBox.addItem(" More Actions ...");
        comboBox.addItem("SEPARATOR");
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(240);
        TitledBorder dictionaryTitleBorder = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED), "Estimation");
        dictionaryTitleBorder.setTitleColor(Color.BLUE);
        dictionaryTitleBorder.setTitlePosition(TitledBorder.BELOW_TOP);
        dictionaryTitleBorder.setTitleJustification(TitledBorder.CENTER);
        estimationTextPane.setEditable(false);
        estimationScroller.setBorder(dictionaryTitleBorder);
        estimationTextPane.setContentType("text/html");
        estimationTextPane.setMargin(new Insets(10, 10, 10, 10));
        isLoading = true;
        updateEstimationTextPane(false);
        isLoading = false;
        clefTableModel = new DefaultTableModel();
        clefTable = new COINCOMOFixedTable(clefTableModel);
        clefTable.setRowSelectionAllowed(false);
        clefTable.addMouseListener(this);
        clefTableModel.addColumn("X");
        clefTableModel.addColumn("<html><body style='text-align:center'>Project<br />Name<br /></body></html>");
        clefTableModel.addColumn("KSLOC");
        clefTableModel.addColumn("CREVOL");
        clefTableModel.addColumn("<html><body style='text-align:center'>COT's<br />Class<br /></body></html>");
        clefTableModel.addColumn("<html><body style='text-align:center'>Glue<br />Code<br /></body></html>");
        clefTableModel.addColumn("<html><body style='text-align:center'>Tailor<br />Code<br /></body></html>");
        clefTableModel.addColumn("<html><body style='text-align:center'>Assess<br />Code<br /></body></html>");
        clefTableModel.addColumn("<html><body style='text-align:center'>Rev<br />No<br /></body></html>");
        clefTableModel.addColumn("<html><body style='text-align:center'>Project<br />Result<br /></body></html>");
        COINCOMOClefTableHeaderRenderer multiLineTableHeaderRenderer = new COINCOMOClefTableHeaderRenderer();
        COINCOMOClefTableCellRenderer colorfulTableCellRenderer = new COINCOMOClefTableCellRenderer();
        Enumeration<TableColumn> columns = clefTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            column.setHeaderRenderer(multiLineTableHeaderRenderer);
            column.setCellRenderer(colorfulTableCellRenderer);
        }
        clefTable.getColumnModel().getColumn(0).setPreferredWidth(15);
        clefTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        clefTable.getColumnModel().getColumn(6).setPreferredWidth(50);
        clefTable.getColumnModel().getColumn(7).setPreferredWidth(50);
        clefTable.getColumnModel().getColumn(8).setPreferredWidth(50);
        clefTable.getColumnModel().getColumn(9).setPreferredWidth(50);
        clefTable.getColumnModel().getColumn(0).setCellRenderer(new COINCOMOCheckBoxTableCellRenderer());
        clefTable.getColumnModel().getColumn(0).setCellEditor(new COINCOMOCheckBoxCellEditor());
        FlowLayout rightFlow = new FlowLayout();
        FlowLayout leftFlow = new FlowLayout();
        rightFlow.setAlignment(FlowLayout.RIGHT);
        leftFlow.setAlignment(FlowLayout.LEFT);
        JPanel upperNorthPanel = new JPanel(rightFlow);
        JPanel lowerNorthPanel = new JPanel(leftFlow);
        lowerNorthPanel.add(new JLabel("  "));
        lowerNorthPanel.add(new JLabel(" Select Subcomponent: "));
        lowerNorthPanel.add(comboBox);
        JPanel northPanel = new JPanel(new GridLayout(2, 1));
        northPanel.add(upperNorthPanel);
        northPanel.add(lowerNorthPanel);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(estimationScroller);
        southPanel.add(new JLabel("    "), BorderLayout.NORTH);
        southPanel.add(new JLabel("    "), BorderLayout.SOUTH);
        southPanel.add(new JLabel("    "), BorderLayout.EAST);
        southPanel.add(new JLabel("    "), BorderLayout.WEST);
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(new JScrollPane(clefTable));
        centerPanel.add(new JLabel("    "), BorderLayout.NORTH);
        centerPanel.add(new JLabel("    "), BorderLayout.SOUTH);
        centerPanel.add(new JLabel("    "), BorderLayout.EAST);
        centerPanel.add(new JLabel("    "), BorderLayout.WEST);
        loadSubComponents();
        splitPane.setTopComponent(centerPanel);
        splitPane.setBottomComponent(southPanel);
        this.setLayout(new BorderLayout());
        this.add(northPanel, BorderLayout.NORTH);
        this.add(splitPane);
    }

    public static void updateEstimationTextPane(final boolean needToUpdateComponent) {
        estimationTextPane.setText("Loading ...");
        if (!isLoading) {
            if (refreshThread != null) {
                refreshThread.interrupt();
            }
            refreshThread = new RefreshSubComponents();
            refreshThread.start();
            if (reportUpdater != null) {
                reportUpdater.interrupt();
            }
        }
        reportUpdater = new UpdateEstimationReport(needToUpdateComponent);
        reportUpdater.start();
    }

    private void loadSubComponents() {
        COINCOMOVector<String> tableRowVector = new COINCOMOVector<String>();
        tableRowVector.add("false");
        tableRowVector.add(component.getName());
        tableRowVector.add("0.00");
        tableRowVector.add("0.00");
        tableRowVector.add("First");
        tableRowVector.add("Second");
        tableRowVector.add("Third");
        tableRowVector.add("Fourth");
        tableRowVector.add("0.00");
        tableRowVector.add("0.00");
        tableRowVector.add("0.0");
        tableRowVector.setRowID(component.getUnitID());
        clefTableModel.addRow(tableRowVector);
        GlobalMethods.updateStatusBar("Subcomponents have been loaded.");
    }

    public void loadcomboBoxSubComponents() {
        comboBox.removeAllItems();
        comboBox.addItem(" More Actions ...");
        comboBox.addItem("SEPARATOR");
        Vector<COINCOMOUnit> orderedVector = GlobalMethods.getOrderedVector(component.getListOfSubUnits());
        ArrayList<String> loadedList = new ArrayList<String>();
        for (int i = 0; i < orderedVector.size(); i++) {
            COINCOMOSubComponent tempSubComponent = (COINCOMOSubComponent) orderedVector.get(i);
            loadedList.add(tempSubComponent.getName());
        }
        for (String s : loadedList) comboBox.addItem(s);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == COINCOMO.viewCotsProjectsMenuItem) {
            new ViewCotsProjectsDialog(COINCOMO.application, component);
            if (clefTable.getRowCount() == 0) COINCOMOComponentManager.loadCocotProject(component.getProjectName(), component);
            COINCOMOComponentManager.loadCocotGlue(component);
            COINCOMOComponentManager.loadCocotTail(component);
            COINCOMOComponentManager.loadCocotAssess(component);
            COINCOMOComponentManager.deleteCocotsTablesFinal();
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(100);
                        clefTable.setValueAt(component.getProjectName(), 0, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>Project<br />Name<br /></body></html>", clefTable));
                        clefTable.setValueAt(component.getKSLOC(), 0, GlobalMethods.findColumnInTable("KSLOC", clefTable));
                        clefTable.setValueAt(component.getCREVOL(), 0, GlobalMethods.findColumnInTable("CREVOL", clefTable));
                        clefTable.setValueAt("First", 0, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>COT's<br />Class<br /></body></html>", clefTable));
                        clefTable.setValueAt("Second", 0, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>Glue<br />Code<br /></body></html>", clefTable));
                        clefTable.setValueAt("Third", 0, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>Tailor<br />Code<br /></body></html>", clefTable));
                        clefTable.setValueAt("Fourth", 0, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>Assess<br />Code<br /></body></html>", clefTable));
                        clefTable.setValueAt(component.getProjectResult(), 0, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>Project<br />Result<br /></body></html>", clefTable));
                        GlobalMethods.updateStatusBar("New revision number has been saved.");
                    } catch (InterruptedException ex) {
                    }
                }
            });
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            System.out.println(comboBox.getSelectedIndex());
            System.out.println(comboBox.getSelectedItem());
            Vector<COINCOMOUnit> orderedVector = GlobalMethods.getOrderedVector(component.getListOfSubUnits());
            boolean set = false;
            for (int i = 0; i < orderedVector.size(); i++) {
                COINCOMOSubComponent tempSubComponent = (COINCOMOSubComponent) orderedVector.get(i);
                if (tempSubComponent.getName().equals(comboBox.getSelectedItem()) && !set) {
                    set = true;
                    COINCOMOComponentManager.insertSubCompTemp(tempSubComponent.getUnitID());
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        final int rowNumber = clefTable.rowAtPoint(e.getPoint());
        final int columnNumber = clefTable.columnAtPoint(e.getPoint());
        Vector<COINCOMOVector> vectors = clefTableModel.getDataVector();
        COINCOMOVector clickedRow = vectors.get(rowNumber);
        if (columnNumber == 1) {
            Vector<String> loadedProjects = COINCOMOComponentManager.loadCocotsProjects();
            String result = JOptionPane.showInputDialog(COINCOMO.application, "Please enter the new name:");
            if (result != null && !result.trim().equals("") && !loadedProjects.contains(result)) {
                component.setName(result);
                GlobalMethods.updateStatusBar("Saving New Name...");
                COINCOMOComponentManager.updateCompCocotProject(component);
                clefTable.setValueAt(component.getName(), rowNumber, columnNumber);
            } else {
                JOptionPane.showMessageDialog(COINCOMO.application, "Invalid project name: " + result, "Notice", JOptionPane.WARNING_MESSAGE);
            }
        } else if (columnNumber == 2) {
            String result = JOptionPane.showInputDialog(COINCOMO.application, "Please enter the project KSLOC:");
            if (result != null && !result.trim().equals("")) {
                component.setKSLOC(Double.parseDouble(result));
                GlobalMethods.updateStatusBar("Saving New KSLOC...");
                clefTable.setValueAt(component.getKSLOC(), rowNumber, columnNumber);
            }
        } else if (columnNumber == 3) {
            String result = JOptionPane.showInputDialog(COINCOMO.application, "Please enter the project CREVOL:");
            if (result != null && !result.trim().equals("")) {
                component.setCREVOL(Double.parseDouble(result));
                GlobalMethods.updateStatusBar("Saving New CREVOL...");
                clefTable.setValueAt(component.getCREVOL(), rowNumber, columnNumber);
            }
        } else if (columnNumber == 4) {
            new CotClassDialog(COINCOMO.application, component, rowNumber);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(100);
                        COINCOMOComponentManager.insertCocotsCots(component);
                        clefTable.setValueAt("Done", rowNumber, columnNumber);
                        GlobalMethods.updateStatusBar("Cots Selected has been saved.");
                    } catch (InterruptedException ex) {
                    }
                }
            });
        } else if (columnNumber == 5) {
            new CotGlueCodeEAFTabDialog(COINCOMO.application, component, rowNumber);
            clefTable.setValueAt("Done", rowNumber, columnNumber);
        } else if (columnNumber == 6) {
            new CotTailoringCodeEAFTabDialog(COINCOMO.application, component, rowNumber);
            clefTable.setValueAt("Done", rowNumber, columnNumber);
        } else if (columnNumber == 7) {
            new CotAssesCodeEAFTabDialog(COINCOMO.application, component, rowNumber);
            clefTable.setValueAt("Done", rowNumber, columnNumber);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        Thread.sleep(100);
                        COINCOMOComponentManager.updateProjectResult(component);
                        clefTable.setValueAt(component.getProjectResult(), rowNumber, GlobalMethods.findColumnInTable("<html><body style='text-align:center'>Project<br />Result<br /></body></html>", clefTable));
                        GlobalMethods.updateStatusBar("Result Calculated");
                    } catch (InterruptedException ex) {
                    }
                }
            });
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

    static class RefreshSubComponents extends Thread {

        public RefreshSubComponents() {
            this.setPriority(3);
        }

        @Override
        public void run() {
            GlobalMethods.updateStatusBar("Refreshing Subcomponents ...");
            try {
                Vector<COINCOMOUnit> orderedVector = GlobalMethods.getOrderedVector(component.getListOfSubUnits());
                COINCOMOSubComponentManager.updateSubComponent(orderedVector);
                for (int i = 0; i < orderedVector.size(); i++) {
                    COINCOMOSubComponent tempSubComponent = (COINCOMOSubComponent) orderedVector.get(i);
                    clefTable.setValueAt(format2Decimals.format(GlobalMethods.roundOff(tempSubComponent.getNomEffort(), 2)), i, 6);
                    clefTable.setValueAt(format2Decimals.format(GlobalMethods.roundOff(tempSubComponent.getEstEffort(), 2)), i, 7);
                    clefTable.setValueAt(format2Decimals.format(GlobalMethods.roundOff(tempSubComponent.getProd(), 2)), i, 8);
                    clefTable.setValueAt(format2Decimals.format(GlobalMethods.roundOff(tempSubComponent.getCost(), 2)), i, 9);
                    clefTable.setValueAt(format2Decimals.format(GlobalMethods.roundOff(tempSubComponent.getInstCost(), 2)), i, 10);
                    clefTable.setValueAt(format1Decimal.format(GlobalMethods.roundOff(tempSubComponent.getStaff(), 1)), i, 11);
                }
            } catch (Exception e) {
            }
            GlobalMethods.updateStatusBar("Subcomponents have been refreshed.");
        }
    }

    static class UpdateEstimationReport extends Thread {

        boolean needToUpdateComponent;

        public UpdateEstimationReport(boolean needToUpdate) {
            this.needToUpdateComponent = needToUpdate;
            this.setPriority(1);
        }

        @Override
        public void run() {
            GlobalMethods.updateStatusBar("Updating Report ...");
            if (needToUpdateComponent) {
                COINCOMOComponentManager.updateComponent(component);
            }
            OverviewsAndGraphsPanel.copsemo.updateCOPSEMO(component);
            StringBuilder output = new StringBuilder();
            output.append("<table border = '0' cellpadding = '1'  cellspacing = '1' width = '50%' >");
            output.append("<tr>");
            output.append("<td> <b>Total Lines Of Code:</b> " + component.getFinalSLOC() + " </td>");
            output.append("<td> <b>Hours/PM:</b> 152 </td>");
            output.append("</tr>");
            output.append("</table>");
            String color = "DDDDDD";
            output.append("<table border = '1' cellpadding = '1'  cellspacing = '1' width = '100%' align = 'center'>");
            output.append("<tr>");
            output.append("<th bgcolor = " + color + "> Estimated </th>");
            output.append("<th bgcolor = " + color + "> Effort </th>");
            output.append("<th bgcolor = " + color + "> Schedule </th>");
            output.append("<th bgcolor = " + color + "> PROD </th>");
            output.append("<th bgcolor = " + color + "> COST </th>");
            output.append("<th bgcolor = " + color + "> INST </th>");
            output.append("<th bgcolor = " + color + "> Staff </th>");
            output.append("<th bgcolor = " + color + "> Risk </th>");
            output.append("</tr>");
            output.append("<tr>");
            output.append("<th bgcolor = " + color + "> Most Likely </th>");
            output.append("<th> " + format2Decimals.format(GlobalMethods.roundOff(component.getTotalEffort(), 2)) + " </th>");
            output.append("<th> " + format2Decimals.format(GlobalMethods.roundOff(component.getTotalSchedule(), 2)) + " </th>");
            output.append("<th> " + format2Decimals.format(GlobalMethods.roundOff(component.getTotalProd(), 2)) + " </th>");
            output.append("<th> " + format2Decimals.format(GlobalMethods.roundOff(component.getTotalCost(), 2)) + " </th>");
            output.append("<th> " + format2Decimals.format(GlobalMethods.roundOff(component.getTotalInstCost(), 2)) + " </th>");
            output.append("<th> " + format1Decimal.format(GlobalMethods.roundOff(component.getTotalStaff(), 1)) + " </th>");
            output.append("<th> 0.0 </th>");
            output.append("</tr>");
            output.append("</table>");
            estimationTextPane.setText(output.toString());
            GlobalMethods.updateStatusBar("Report Updated.");
        }
    }
}
