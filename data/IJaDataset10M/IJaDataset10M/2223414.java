package com.nokia.ats4.appmodel.perspective.tester.swing;

import com.nokia.ats4.appmodel.perspective.tester.event.StopTestSetExecutionEvent;
import com.nokia.ats4.appmodel.event.EventQueue;
import com.nokia.ats4.appmodel.main.event.ExportTestScriptEvent;
import com.nokia.ats4.appmodel.main.swing.ModelTreePanel;
import com.nokia.ats4.appmodel.main.swing.UseCaseTreePanel;
import com.nokia.ats4.appmodel.main.swing.UseCaseTreePopup;
import com.nokia.ats4.appmodel.model.KendoModel;
import com.nokia.ats4.appmodel.model.KendoSubModel;
import com.nokia.ats4.appmodel.model.domain.SubModel;
import com.nokia.ats4.appmodel.model.domain.testset.TestSet;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCase;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCasePath;
import com.nokia.ats4.appmodel.model.swing.TestSetTableModel;
import com.nokia.ats4.appmodel.model.swing.TestStatusTableModel;
import com.nokia.ats4.appmodel.perspective.tester.event.CreateTestSetEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.CreateTestSetItemEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.DeleteTestSetEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.ExecuteTestSetEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.MoveTestSetItemsEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.RemoveTestSetItemsEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.SaveTestSetAsEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.SaveTestSetEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.SelectTestSetEvent;
import com.nokia.ats4.appmodel.perspective.tester.event.TestSetDescriptionUpdateEvent;
import com.nokia.ats4.appmodel.util.Settings;
import com.nokia.ats4.appmodel.util.KendoResources;
import com.nokia.ats4.appmodel.util.TreeDragAndDropHandler;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.ComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import org.apache.log4j.Logger;

/**
 * TesterPanel is a Swing implementation of the GUI for constructing batch
 * test sets to be executed in test engine (aka. "Test runs" or "Test Sets").
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 16 $
 */
public class TesterPanel extends javax.swing.JPanel implements java.awt.event.ActionListener, java.awt.event.FocusListener, javax.swing.event.PopupMenuListener, java.awt.event.MouseListener {

    private static final Logger log = Logger.getLogger(TesterPanel.class);

    private ModelTreePanel modelTreePanel;

    private UseCaseTreePanel caseTreePanel;

    /**
     * Creates new form TestingPanel
     */
    public TesterPanel() {
        initComponents();
        this.modelTreePanel = new ModelTreePanel();
        this.modelTreePanel.setTreeDragEnabled(true);
        this.modelTreePanel.addTreeListener(new ModelTreeListener());
        this.modelTreePanel.setTreeTransferHandler(new TreeDragAndDropHandler());
        this.caseTreePanel = new UseCaseTreePanel();
        this.caseTreePanel.setRootVisible(false);
        this.caseTreePanel.setTreeDragEnabled(true);
        this.caseTreePanel.setTreeTransferHandler(new TreeDragAndDropHandler());
        this.caseTreePanel.addTreeMouseListener(new UseCaseTreeMouseListener());
        this.sidePanel.add(this.modelTreePanel);
        this.sidePanel.add(this.caseTreePanel);
        this.testSetTable.setTransferHandler(new TestSetTableTransferHandler());
        this.testSetTable.setDropMode(DropMode.ON);
        this.testSetTable.setDragEnabled(true);
        this.testSetCombo.addActionListener(new TestSetsComboBoxListener());
        this.testResultTable.setAutoCreateRowSorter(true);
    }

    public void setTestResultTableModel(TableModel model) {
        this.testResultTable.setModel(model);
        setResultTableRenderers();
    }

    private void setResultTableRenderers() {
        TableColumn fileCol = this.testResultTable.getColumnModel().getColumn(0);
        TableColumn testerCol = this.testResultTable.getColumnModel().getColumn(1);
        TableColumn startTimeCol = this.testResultTable.getColumnModel().getColumn(2);
        TableColumn endTimeCol = this.testResultTable.getColumnModel().getColumn(3);
        TableColumn elapsedTimeCol = this.testResultTable.getColumnModel().getColumn(4);
        TableColumn statusCol = this.testResultTable.getColumnModel().getColumn(5);
        TableCellRenderer fileRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                File file = (File) value;
                setToolTipText(file.getName());
                return super.getTableCellRendererComponent(table, file.getName(), isSelected, hasFocus, row, column);
            }
        };
        TableCellRenderer dateRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value == null) {
                    return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                Date date = (Date) value;
                String dateString = formatter.format(date);
                setToolTipText(dateString);
                return super.getTableCellRendererComponent(table, dateString, isSelected, hasFocus, row, column);
            }
        };
        TableCellRenderer elapsedTimeRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value == null) {
                    return super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
                }
                long milliseconds = ((Long) value).longValue();
                long hours = milliseconds / (1000 * 60 * 60);
                long remainder = milliseconds % (1000 * 60 * 60);
                long minutes = remainder / (1000 * 60);
                remainder = remainder % (1000 * 60);
                long seconds = remainder / (1000);
                remainder = remainder % 1000;
                if (remainder >= 500) {
                    seconds += 1;
                }
                String hourString = "" + hours;
                String minuteString;
                if (minutes > 9) {
                    minuteString = "" + minutes;
                } else {
                    minuteString = "0" + minutes;
                }
                String secondString;
                if (seconds > 9) {
                    secondString = "" + seconds;
                } else {
                    secondString = "0" + seconds;
                }
                String timeString = hourString + ":" + minuteString + ":" + secondString;
                setToolTipText(timeString);
                return super.getTableCellRendererComponent(table, timeString, isSelected, hasFocus, row, column);
            }
        };
        TableCellRenderer stringRenderer = new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value == null) {
                    value = "";
                }
                setToolTipText(value.toString());
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        };
        fileCol.setCellRenderer(fileRenderer);
        testerCol.setCellRenderer(stringRenderer);
        startTimeCol.setCellRenderer(dateRenderer);
        endTimeCol.setCellRenderer(dateRenderer);
        elapsedTimeCol.setCellRenderer(elapsedTimeRenderer);
        statusCol.setCellRenderer(new ProgRenderer());
    }

    public void setTestStatusTableModel(TestStatusTableModel model) {
        this.testStatusTable.setModel(model);
        this.testStatusTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.testStatusTable.getColumnModel().getColumn(model.getProgressBarColumnNumber()).setCellRenderer(new ProgRenderer());
    }

    private void initComponents() {
        testSetTablePopup = new javax.swing.JPopupMenu();
        moveRowUpItem = new javax.swing.JMenuItem();
        moveRowDownItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        removeRowItem = new javax.swing.JMenuItem();
        TestStatusPopUp = new javax.swing.JPopupMenu();
        abortTestSetItem = new javax.swing.JMenuItem();
        testResultsTablePopup = new javax.swing.JPopupMenu();
        viewSummaryItem = new javax.swing.JMenuItem();
        viewResultsItem = new javax.swing.JMenuItem();
        viewHtmlItem = new javax.swing.JMenuItem();
        verticalSplitter = new javax.swing.JSplitPane();
        sidePanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();
        testSetsPanel = new javax.swing.JPanel();
        testSetLabel = new javax.swing.JLabel();
        testSetCombo = new javax.swing.JComboBox();
        newTestSetButton = new javax.swing.JButton();
        deleteTestSetButton = new javax.swing.JButton();
        descriptionLabel = new javax.swing.JLabel();
        descriptionTextField = new javax.swing.JTextField();
        testSetPanel = new javax.swing.JPanel();
        testSetScrollPane = new javax.swing.JScrollPane();
        testSetTable = new javax.swing.JTable();
        itemsLabel = new javax.swing.JLabel();
        modeComboBox = new javax.swing.JComboBox();
        modeLabel = new javax.swing.JLabel();
        executeButton = new javax.swing.JButton();
        saveAsButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        testSetPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        testResultTable = new javax.swing.JTable();
        testStatusPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        testStatusTable = new javax.swing.JTable();
        moveRowUpItem.setText(KendoResources.getString("testRun.testSet.moveRowUp"));
        moveRowUpItem.addActionListener(this);
        testSetTablePopup.add(moveRowUpItem);
        moveRowDownItem.setText(KendoResources.getString("testRun.testSet.moveDown"));
        moveRowDownItem.addActionListener(this);
        testSetTablePopup.add(moveRowDownItem);
        testSetTablePopup.add(jSeparator1);
        removeRowItem.setText(KendoResources.getString("testRun.testSet.removeRow"));
        removeRowItem.addActionListener(this);
        testSetTablePopup.add(removeRowItem);
        TestStatusPopUp.addPopupMenuListener(this);
        abortTestSetItem.setText(KendoResources.getString("tester.teststatus.popup.terminateItem"));
        abortTestSetItem.addActionListener(this);
        TestStatusPopUp.add(abortTestSetItem);
        viewSummaryItem.setText(KendoResources.getString("menu.popup.testResultsTable.viewSummary"));
        viewSummaryItem.addActionListener(this);
        testResultsTablePopup.add(viewSummaryItem);
        viewResultsItem.setText(KendoResources.getString("menu.popup.testResultsTable.viewResults"));
        viewResultsItem.addActionListener(this);
        testResultsTablePopup.add(viewResultsItem);
        viewHtmlItem.setText(KendoResources.getString("menu.popup.testResultsTable.viewHtmlLog"));
        viewHtmlItem.addActionListener(this);
        testResultsTablePopup.add(viewHtmlItem);
        setPreferredSize(new java.awt.Dimension(800, 600));
        verticalSplitter.setDividerLocation(200);
        sidePanel.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
        verticalSplitter.setLeftComponent(sidePanel);
        testSetsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(KendoResources.getString("testRun.testSets.title")));
        testSetLabel.setText(KendoResources.getString("testRun.testSets.testSet"));
        testSetCombo.setEnabled(false);
        testSetCombo.addActionListener(this);
        newTestSetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/nokia/ats4/appmodel/icons/new.gif")));
        newTestSetButton.setToolTipText(KendoResources.getString("testRun.testSets.new"));
        newTestSetButton.setBorder(null);
        newTestSetButton.setContentAreaFilled(false);
        newTestSetButton.addActionListener(this);
        newTestSetButton.addMouseListener(this);
        deleteTestSetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/nokia/ats4/appmodel/icons/delete.gif")));
        deleteTestSetButton.setToolTipText(KendoResources.getString("testRun.testSets.delete"));
        deleteTestSetButton.setBorder(null);
        deleteTestSetButton.setContentAreaFilled(false);
        deleteTestSetButton.setEnabled(false);
        deleteTestSetButton.addActionListener(this);
        deleteTestSetButton.addMouseListener(this);
        descriptionLabel.setText(KendoResources.getString("testRun.testSet.comment"));
        descriptionTextField.setEnabled(false);
        descriptionTextField.addActionListener(this);
        descriptionTextField.addFocusListener(this);
        javax.swing.GroupLayout testSetsPanelLayout = new javax.swing.GroupLayout(testSetsPanel);
        testSetsPanel.setLayout(testSetsPanelLayout);
        testSetsPanelLayout.setHorizontalGroup(testSetsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testSetsPanelLayout.createSequentialGroup().addContainerGap().addGroup(testSetsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testSetsPanelLayout.createSequentialGroup().addComponent(descriptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(descriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(deleteTestSetButton)).addGroup(testSetsPanelLayout.createSequentialGroup().addComponent(testSetLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(testSetCombo, 0, 448, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(newTestSetButton))).addContainerGap()));
        testSetsPanelLayout.setVerticalGroup(testSetsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testSetsPanelLayout.createSequentialGroup().addContainerGap().addGroup(testSetsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(testSetLabel).addComponent(newTestSetButton).addComponent(testSetCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(testSetsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(descriptionLabel).addComponent(descriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(deleteTestSetButton)).addContainerGap(14, Short.MAX_VALUE)));
        testSetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(KendoResources.getString("testRun.testSet.testSet")));
        testSetTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { {}, {}, {}, {}, {}, {} }, new String[] {}));
        testSetTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);
        testSetTable.setComponentPopupMenu(testSetTablePopup);
        testSetTable.setDragEnabled(true);
        testSetScrollPane.setViewportView(testSetTable);
        itemsLabel.setText(KendoResources.getString("testRun.testSet.items"));
        modeComboBox.setEnabled(false);
        modeComboBox.addActionListener(this);
        modeLabel.setText(KendoResources.getString("testRun.testSet.mode"));
        executeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/nokia/ats4/appmodel/icons/execute.gif")));
        executeButton.setToolTipText(KendoResources.getString("testRun.testSet.execute"));
        executeButton.setBorder(null);
        executeButton.setContentAreaFilled(false);
        executeButton.setEnabled(false);
        executeButton.addActionListener(this);
        executeButton.addMouseListener(this);
        saveAsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/nokia/ats4/appmodel/icons/save_as.gif")));
        saveAsButton.setToolTipText(KendoResources.getString("testRun.testSet.saveAs"));
        saveAsButton.setBorder(null);
        saveAsButton.setContentAreaFilled(false);
        saveAsButton.setEnabled(false);
        saveAsButton.addActionListener(this);
        saveAsButton.addMouseListener(this);
        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/nokia/ats4/appmodel/icons/save.gif")));
        saveButton.setToolTipText(KendoResources.getString("testRun.testSet.save"));
        saveButton.setBorder(null);
        saveButton.setContentAreaFilled(false);
        saveButton.setEnabled(false);
        saveButton.addActionListener(this);
        saveButton.addMouseListener(this);
        javax.swing.GroupLayout testSetPanelLayout = new javax.swing.GroupLayout(testSetPanel);
        testSetPanel.setLayout(testSetPanelLayout);
        testSetPanelLayout.setHorizontalGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testSetPanelLayout.createSequentialGroup().addContainerGap().addGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(modeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(itemsLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, testSetPanelLayout.createSequentialGroup().addComponent(saveButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(saveAsButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 414, Short.MAX_VALUE).addComponent(executeButton)).addComponent(testSetScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 471, Short.MAX_VALUE).addComponent(modeComboBox, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        testSetPanelLayout.setVerticalGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, testSetPanelLayout.createSequentialGroup().addContainerGap().addGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(modeLabel).addComponent(modeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(itemsLabel).addComponent(testSetScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(testSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(saveButton).addComponent(saveAsButton).addComponent(executeButton)).addContainerGap()));
        testSetPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(KendoResources.getString("testRun.testResults.title")));
        testResultTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        testResultTable.addMouseListener(this);
        jScrollPane1.setViewportView(testResultTable);
        javax.swing.GroupLayout testSetPanel1Layout = new javax.swing.GroupLayout(testSetPanel1);
        testSetPanel1.setLayout(testSetPanel1Layout);
        testSetPanel1Layout.setHorizontalGroup(testSetPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testSetPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE).addContainerGap()));
        testSetPanel1Layout.setVerticalGroup(testSetPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testSetPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE).addContainerGap()));
        testStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(KendoResources.getString("statuspanel.label")));
        testStatusTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Title 1", "Title 2", "Title 3", "Title 4" }));
        testStatusTable.setComponentPopupMenu(TestStatusPopUp);
        jScrollPane2.setViewportView(testStatusTable);
        javax.swing.GroupLayout testStatusPanelLayout = new javax.swing.GroupLayout(testStatusPanel);
        testStatusPanel.setLayout(testStatusPanelLayout);
        testStatusPanelLayout.setHorizontalGroup(testStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testStatusPanelLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 538, Short.MAX_VALUE).addContainerGap()));
        testStatusPanelLayout.setVerticalGroup(testStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(testStatusPanelLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE).addContainerGap()));
        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup().addContainerGap().addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(testSetPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(testSetPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(testSetsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(testStatusPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(mainPanelLayout.createSequentialGroup().addContainerGap().addComponent(testSetsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(testSetPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(testStatusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(testSetPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { testSetPanel1, testStatusPanel });
        jScrollPane3.setViewportView(mainPanel);
        verticalSplitter.setRightComponent(jScrollPane3);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(verticalSplitter, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(verticalSplitter, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE));
    }

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == testSetCombo) {
            TesterPanel.this.testSetComboActionPerformed(evt);
        } else if (evt.getSource() == newTestSetButton) {
            TesterPanel.this.newTestSetButtonActionPerformed(evt);
        } else if (evt.getSource() == deleteTestSetButton) {
            TesterPanel.this.deleteTestSetButtonActionPerformed(evt);
        } else if (evt.getSource() == descriptionTextField) {
            TesterPanel.this.descriptionTextFieldActionPerformed(evt);
        } else if (evt.getSource() == modeComboBox) {
            TesterPanel.this.modeComboBoxActionPerformed(evt);
        } else if (evt.getSource() == executeButton) {
            TesterPanel.this.executeButtonActionPerformed(evt);
        } else if (evt.getSource() == saveAsButton) {
            TesterPanel.this.saveAsButtonActionPerformed(evt);
        } else if (evt.getSource() == saveButton) {
            TesterPanel.this.saveButtonActionPerformed(evt);
        } else if (evt.getSource() == moveRowUpItem) {
            TesterPanel.this.moveRowUpItemActionPerformed(evt);
        } else if (evt.getSource() == moveRowDownItem) {
            TesterPanel.this.moveRowDownItemActionPerformed(evt);
        } else if (evt.getSource() == removeRowItem) {
            TesterPanel.this.removeRowItemActionPerformed(evt);
        } else if (evt.getSource() == abortTestSetItem) {
            TesterPanel.this.abortTestSetItemActionPerformed(evt);
        } else if (evt.getSource() == viewSummaryItem) {
            TesterPanel.this.viewSummaryItemActionPerformed(evt);
        } else if (evt.getSource() == viewResultsItem) {
            TesterPanel.this.viewResultsItemActionPerformed(evt);
        } else if (evt.getSource() == viewHtmlItem) {
            TesterPanel.this.viewHtmlItemActionPerformed(evt);
        }
    }

    public void focusGained(java.awt.event.FocusEvent evt) {
    }

    public void focusLost(java.awt.event.FocusEvent evt) {
        if (evt.getSource() == descriptionTextField) {
            TesterPanel.this.descriptionTextFieldFocusLost(evt);
        }
    }

    public void mouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getSource() == testResultTable) {
            TesterPanel.this.handleMouseClicked(evt);
        }
    }

    public void mouseEntered(java.awt.event.MouseEvent evt) {
        if (evt.getSource() == newTestSetButton) {
            TesterPanel.this.newTestSetButtonMouseEntered(evt);
        } else if (evt.getSource() == deleteTestSetButton) {
            TesterPanel.this.deleteTestSetButtonMouseEntered(evt);
        } else if (evt.getSource() == executeButton) {
            TesterPanel.this.executeButtonMouseEntered(evt);
        } else if (evt.getSource() == saveAsButton) {
            TesterPanel.this.saveAsButtonMouseEntered(evt);
        } else if (evt.getSource() == saveButton) {
            TesterPanel.this.saveButtonMouseEntered(evt);
        }
    }

    public void mouseExited(java.awt.event.MouseEvent evt) {
        if (evt.getSource() == newTestSetButton) {
            TesterPanel.this.newTestSetButtonMouseExited(evt);
        } else if (evt.getSource() == deleteTestSetButton) {
            TesterPanel.this.deleteTestSetButtonMouseExited(evt);
        } else if (evt.getSource() == executeButton) {
            TesterPanel.this.executeButtonMouseExited(evt);
        } else if (evt.getSource() == saveAsButton) {
            TesterPanel.this.saveAsButtonMouseExited(evt);
        } else if (evt.getSource() == saveButton) {
            TesterPanel.this.saveButtonMouseExited(evt);
        }
    }

    public void mousePressed(java.awt.event.MouseEvent evt) {
    }

    public void mouseReleased(java.awt.event.MouseEvent evt) {
    }

    public void popupMenuCanceled(javax.swing.event.PopupMenuEvent evt) {
    }

    public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent evt) {
    }

    public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        if (evt.getSource() == TestStatusPopUp) {
            TesterPanel.this.TestStatusPopUpPopupMenuWillBecomeVisible(evt);
        }
    }

    private void executeButtonMouseExited(java.awt.event.MouseEvent evt) {
        handleMouseExited((Component) evt.getSource());
    }

    private void executeButtonMouseEntered(java.awt.event.MouseEvent evt) {
        handleMouseEntered((Component) evt.getSource());
    }

    private void saveAsButtonMouseExited(java.awt.event.MouseEvent evt) {
        handleMouseExited((Component) evt.getSource());
    }

    private void saveAsButtonMouseEntered(java.awt.event.MouseEvent evt) {
        handleMouseEntered((Component) evt.getSource());
    }

    private void saveButtonMouseExited(java.awt.event.MouseEvent evt) {
        handleMouseExited((Component) evt.getSource());
    }

    private void saveButtonMouseEntered(java.awt.event.MouseEvent evt) {
        handleMouseEntered((Component) evt.getSource());
    }

    private void deleteTestSetButtonMouseExited(java.awt.event.MouseEvent evt) {
        handleMouseExited((Component) evt.getSource());
    }

    private void deleteTestSetButtonMouseEntered(java.awt.event.MouseEvent evt) {
        handleMouseEntered((Component) evt.getSource());
    }

    private void newTestSetButtonMouseExited(java.awt.event.MouseEvent evt) {
        handleMouseExited((Component) evt.getSource());
    }

    private void newTestSetButtonMouseEntered(java.awt.event.MouseEvent evt) {
        handleMouseEntered((Component) evt.getSource());
    }

    private void TestStatusPopUpPopupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent evt) {
        if (this.testStatusTable.getSelectedRow() != -1) {
            this.abortTestSetItem.setEnabled(true);
        } else {
            this.abortTestSetItem.setEnabled(false);
        }
    }

    private void abortTestSetItemActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = this.testStatusTable.getSelectedRow();
        if (selectedRow != -1) {
            EventQueue.dispatchEvent(new StopTestSetExecutionEvent(evt.getSource(), ((TestStatusTableModel) testStatusTable.getModel()).getEventForRow(selectedRow)));
        }
    }

    private void viewResultsItemActionPerformed(java.awt.event.ActionEvent evt) {
        int i = testResultTable.getSelectedRow();
        if (i > -1) {
            File file = (File) testResultTable.getModel().getValueAt(i, 0);
            String name = file.getName().replace("_Summary_", "_Result_");
            String path = Settings.getProperty("aste.install.dir") + Settings.getProperty("aste.result.dir");
            this.launchFile(new File(path, name));
        }
    }

    private void viewSummaryItemActionPerformed(java.awt.event.ActionEvent evt) {
        int i = testResultTable.getSelectedRow();
        if (i > -1) {
            File file = (File) testResultTable.getModel().getValueAt(i, 0);
            this.launchFile(file);
        }
    }

    private void handleMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getButton() == MouseEvent.BUTTON3 && testResultTable.getSelectedRowCount() == 1) {
            this.testResultsTablePopup.show((Component) evt.getSource(), evt.getX(), evt.getY());
        }
    }

    private void viewHtmlItemActionPerformed(java.awt.event.ActionEvent evt) {
        int i = testResultTable.getSelectedRow();
        if (i > -1) {
            File file = (File) testResultTable.getModel().getValueAt(i, 0);
            String name = file.getName().replace(".xls", ".html");
            String path = Settings.getProperty("aste.install.dir") + Settings.getProperty("aste.logger.dir");
            this.launchFile(new File(path, name));
        }
    }

    private void descriptionTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        String desc = descriptionTextField.getText();
        EventQueue.postEvent(new TestSetDescriptionUpdateEvent(evt.getSource(), testSet, desc));
    }

    private void modeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        TestSet.Mode mode = (TestSet.Mode) modeComboBox.getSelectedItem();
        if (testSet != null && mode != null && evt.getModifiers() == KeyEvent.BUTTON1_MASK) {
            testSet.setMode(mode);
        }
    }

    private void saveAsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        EventQueue.postEvent(new SaveTestSetAsEvent(evt.getSource(), testSet));
    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        EventQueue.postEvent(new SaveTestSetEvent(evt.getSource(), testSet));
    }

    private void descriptionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        String desc = descriptionTextField.getText();
        EventQueue.postEvent(new TestSetDescriptionUpdateEvent(evt.getSource(), testSet, desc));
    }

    private void moveRowDownItemActionPerformed(java.awt.event.ActionEvent evt) {
        int targetRow = testSetTable.getSelectedRow() + 1;
        int[] rows = testSetTable.getSelectedRows();
        if (rows != null) {
            EventQueue.postEvent(new MoveTestSetItemsEvent(evt.getSource(), rows, targetRow));
        }
    }

    private void moveRowUpItemActionPerformed(java.awt.event.ActionEvent evt) {
        int targetRow = testSetTable.getSelectedRow() - 1;
        int[] rows = testSetTable.getSelectedRows();
        if (rows != null) {
            EventQueue.postEvent(new MoveTestSetItemsEvent(evt.getSource(), rows, targetRow));
        }
    }

    private void executeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        EventQueue.postEvent(new ExecuteTestSetEvent(evt.getSource(), testSet));
    }

    private void removeRowItemActionPerformed(java.awt.event.ActionEvent evt) {
        int[] rows = testSetTable.getSelectedRows();
        EventQueue.postEvent(new RemoveTestSetItemsEvent(evt.getSource(), rows));
    }

    private void deleteTestSetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
        EventQueue.postEvent(new DeleteTestSetEvent(evt.getSource(), testSet));
    }

    private void testSetComboActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getModifiers() == KeyEvent.BUTTON1_MASK) {
            TestSet testSet = (TestSet) testSetCombo.getSelectedItem();
            EventQueue.dispatchEvent(new SelectTestSetEvent(evt.getSource(), testSet));
        }
    }

    private void newTestSetButtonActionPerformed(java.awt.event.ActionEvent evt) {
        EventQueue.postEvent(new CreateTestSetEvent(evt.getSource()));
    }

    private javax.swing.JPopupMenu TestStatusPopUp;

    private javax.swing.JMenuItem abortTestSetItem;

    private javax.swing.JButton deleteTestSetButton;

    private javax.swing.JLabel descriptionLabel;

    private javax.swing.JTextField descriptionTextField;

    private javax.swing.JButton executeButton;

    private javax.swing.JLabel itemsLabel;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JComboBox modeComboBox;

    private javax.swing.JLabel modeLabel;

    private javax.swing.JMenuItem moveRowDownItem;

    private javax.swing.JMenuItem moveRowUpItem;

    private javax.swing.JButton newTestSetButton;

    private javax.swing.JMenuItem removeRowItem;

    private javax.swing.JButton saveAsButton;

    private javax.swing.JButton saveButton;

    private javax.swing.JPanel sidePanel;

    private javax.swing.JTable testResultTable;

    private javax.swing.JPopupMenu testResultsTablePopup;

    private javax.swing.JComboBox testSetCombo;

    private javax.swing.JLabel testSetLabel;

    private javax.swing.JPanel testSetPanel;

    private javax.swing.JPanel testSetPanel1;

    private javax.swing.JScrollPane testSetScrollPane;

    private javax.swing.JTable testSetTable;

    private javax.swing.JPopupMenu testSetTablePopup;

    private javax.swing.JPanel testSetsPanel;

    private javax.swing.JPanel testStatusPanel;

    private javax.swing.JTable testStatusTable;

    private javax.swing.JSplitPane verticalSplitter;

    private javax.swing.JMenuItem viewHtmlItem;

    private javax.swing.JMenuItem viewResultsItem;

    private javax.swing.JMenuItem viewSummaryItem;

    /**
     * Handles the component highlighting (when mouse enters the component).
     */
    private void handleMouseEntered(Component c) {
        if (c.isEnabled()) {
            c.setBackground(new Color(225, 225, 225));
        }
    }

    /**
     * Removes the highlighting from specified component (when mouse exits).
     */
    private void handleMouseExited(Component c) {
        c.setBackground(getBackground());
    }

    /**
     * Set the tree model for displaying the project models.
     */
    public void setProjectTreeModel(TreeModel model) {
        this.modelTreePanel.setTreeModel(model);
    }

    /**
     * Set the tree model for three that displays the UseCases and TestCases.
     */
    public void setUseAndTestCaseTreeModel(TreeModel model) {
        this.caseTreePanel.setUseCaseTreeModel(model);
    }

    /**
     * Set the tree model for the use/test case tree.
     */
    public void setCaseTreeModel(TreeModel model) {
        this.caseTreePanel.setUseCaseTreeModel(model);
    }

    /**
     * Set the table model for the test set table component.
     */
    public void setTestSetTableModel(TableModel model) {
        this.testSetTable.setModel(model);
        TableColumnModel columns = this.testSetTable.getColumnModel();
        DefaultTableCellRenderer rend = new DefaultTableCellRenderer();
        rend.setHorizontalAlignment(SwingConstants.CENTER);
        TableColumn index = columns.getColumn(TestSetTableModel.COL_INDEX);
        index.setCellRenderer(rend);
        index.setPreferredWidth(50);
        index.setMinWidth(50);
        index.setMaxWidth(50);
        TableColumn name = columns.getColumn(TestSetTableModel.COL_NAME);
        name.setPreferredWidth(400);
        name.setMinWidth(50);
        TableColumn type = columns.getColumn(TestSetTableModel.COL_TYPE);
        type.setPreferredWidth(400);
        type.setMinWidth(50);
        TableColumn rep = columns.getColumn(TestSetTableModel.COL_REPEATS);
        rep.setCellRenderer(rend);
        rep.setPreferredWidth(50);
        rep.setMinWidth(50);
        rep.setMaxWidth(50);
        TableColumn skip = columns.getColumn(TestSetTableModel.COL_SKIP);
        skip.setMinWidth(50);
        skip.setMaxWidth(50);
        skip.setPreferredWidth(50);
        model.addTableModelListener(new TestSetTableModelListener());
    }

    /**
     * Enable or disable panel controls.
     */
    public void setControlsEnabled(boolean isEnabled) {
        for (Component c : this.getComponents()) {
            c.setEnabled(isEnabled);
        }
    }

    /**
     * Set the model for JComboBox component that displays the test sets.
     *
     * @param model ComboBoxModel to set
     */
    public void setTestSetComboBoxModel(ComboBoxModel model) {
        this.testSetCombo.setModel(model);
    }

    /**
     * Set the model for JComboBox component that displays the test set mode.
     *
     * @param model ComboBoxModel to set
     */
    public void setTestSetModeComboBoxModel(ComboBoxModel model) {
        this.modeComboBox.setModel(model);
    }

    /**
     * Set the model (Document) for JTextField that displays the test set's
     * description.
     *
     * @param doc Document to set
     */
    public void setTestSetDescriptionDocument(Document doc) {
        this.descriptionTextField.setDocument(doc);
    }

    /**
     * Launches the specified file in the default application.
     *
     * @param file File to launch
     */
    private void launchFile(final File file) {
        log.debug("file=" + file.getAbsolutePath());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    if (Desktop.isDesktopSupported()) {
                        String name = file.getName().toLowerCase();
                        if (name.endsWith(".html") || name.endsWith(".htm")) {
                            Desktop.getDesktop().browse(file.toURI());
                        } else {
                            Desktop.getDesktop().open(file);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error while launching file", e);
                }
            }
        });
    }

    /**
     * Listener that monitors the test set table state and enables/disables the
     * test set action buttons according to number of test set items.
     */
    private class TestSetTableModelListener implements TableModelListener {

        public void tableChanged(TableModelEvent e) {
            int rowCount = ((TableModel) e.getSource()).getRowCount();
            for (Component c : testSetTablePopup.getComponents()) {
                c.setEnabled(rowCount > 1);
            }
            executeButton.setEnabled(rowCount > 1);
        }
    }

    /**
     * TransferHandler for listening drags from project tree to test set table and
     * handling drag and drops inside test set table
     */
    class TestSetTableTransferHandler extends TransferHandler {

        /** This determines if the drop can be executed over the given component **/
        public boolean canImport(TransferHandler.TransferSupport support) {
            for (DataFlavor f : support.getDataFlavors()) {
                if (f.getHumanPresentableName().equalsIgnoreCase("TreeNode")) {
                    return true;
                } else if (f.getHumanPresentableName().equalsIgnoreCase("Integer Array")) {
                    return true;
                }
            }
            return false;
        }

        public int getSourceActions(JComponent c) {
            return COPY_OR_MOVE;
        }

        /** This greates the transferobject that holds the tree node as data **/
        protected Transferable createTransferable(JComponent c) {
            if (c instanceof JTable) {
                int[] transferable = ((JTable) c).getSelectedRows();
                if (transferable != null && transferable.length > 0) {
                    return new TransferableListItemNode(transferable);
                }
            }
            return null;
        }

        public void exportDone(JComponent c, Transferable t, int action) {
        }

        /** This handles the drop action of drag and drop **/
        public boolean importData(TransferHandler.TransferSupport support) {
            if (support.getComponent() instanceof JTable && ((JTable) support.getComponent()).getModel() instanceof TestSetTableModel) {
                if (support.getDataFlavors()[0].getHumanPresentableName().equalsIgnoreCase("TreeNode")) {
                    DefaultMutableTreeNode dnd = null;
                    try {
                        dnd = (DefaultMutableTreeNode) support.getTransferable().getTransferData(support.getDataFlavors()[0]);
                    } catch (UnsupportedFlavorException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    JTable.DropLocation dropPoint = (javax.swing.JTable.DropLocation) support.getDropLocation();
                    if (!(dnd.getUserObject() instanceof SubModel)) {
                        if (dropPoint != null) {
                            EventQueue.dispatchEvent(new CreateTestSetItemEvent(support.getComponent(), dnd.getUserObject(), dropPoint.getRow()));
                        } else {
                            EventQueue.dispatchEvent(new CreateTestSetItemEvent(support.getComponent(), dnd.getUserObject(), -1));
                        }
                        return true;
                    }
                } else if (support.getDataFlavors()[0].getHumanPresentableName().equalsIgnoreCase("Integer Array")) {
                    int[] rowsToBeMoved = null;
                    try {
                        rowsToBeMoved = (int[]) support.getTransferable().getTransferData(support.getDataFlavors()[0]);
                    } catch (UnsupportedFlavorException ex) {
                        ex.printStackTrace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    JTable.DropLocation dropPoint = (javax.swing.JTable.DropLocation) support.getDropLocation();
                    EventQueue.dispatchEvent(new MoveTestSetItemsEvent(support.getComponent(), rowsToBeMoved, dropPoint.getRow()));
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Listener for use/test case tree mouse events.
     */
    private class UseCaseTreeMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            JTree tree = (JTree) e.getSource();
            if (tree != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node != null) {
                    Object obj = node.getUserObject();
                    if (e.getClickCount() == 2) {
                        if (node != null && node.getUserObject() != null) {
                            if (obj instanceof UseCase || obj instanceof UseCasePath) {
                                EventQueue.postEvent(new CreateTestSetItemEvent(e.getSource(), obj, -1));
                            } else {
                                Toolkit.getDefaultToolkit().beep();
                            }
                        }
                    } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON3) {
                        UseCaseTreePopup pop = new UseCaseTreePopup(tree, obj, false, false);
                        if (pop.getComponents().length > 0) {
                            pop.show(tree, e.getX(), e.getY());
                        }
                    }
                }
            }
        }
    }

    /**
     * Listener for model tree mouse events.
     */
    private class ModelTreeListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            JTree tree = (JTree) e.getSource();
            if (tree != null && e.getClickCount() == 2) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (node != null && node.getUserObject() != null) {
                    Object obj = node.getUserObject();
                    if (obj instanceof KendoModel && !(obj instanceof KendoSubModel)) {
                        EventQueue.postEvent(new CreateTestSetItemEvent(e.getSource(), obj, -1));
                    } else {
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        }

        public void mousePressed(MouseEvent e) {
            JTree tree = (JTree) e.getSource();
            if (e.getButton() == e.BUTTON3) {
            }
        }
    }

    /**
     * Action listener for test sets combobox. Enables/disables certai
     * components according to selected test set.
     */
    private class TestSetsComboBoxListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            testSetCombo.setEnabled(testSetCombo.getItemCount() > 0);
            deleteTestSetButton.setEnabled(testSetCombo.getSelectedItem() != null);
            descriptionTextField.setEnabled(testSetCombo.getSelectedItem() != null);
            saveButton.setEnabled(testSetCombo.getSelectedItem() != null);
            saveAsButton.setEnabled(testSetCombo.getSelectedItem() != null);
            modeComboBox.setEnabled(testSetCombo.getSelectedItem() != null);
        }
    }

    /**
     * Transfer object for moving list items in the list
     *
     */
    class TransferableListItemNode implements Transferable {

        /** This table holds all the flavors that are used to indentify the type of transfer data **/
        private DataFlavor[] flavors = { new DataFlavor(int[].class, "Integer Array") };

        /** This is the transferred data **/
        private int[] items = null;

        /**
         * The constructor
         * @param items Indices array
         */
        public TransferableListItemNode(int[] items) {
            this.items = items;
        }

        /**
         * @return the flavors assosisiated whit this transrerable
         */
        public DataFlavor[] getTransferDataFlavors() {
            return (DataFlavor[]) flavors.clone();
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (flavor.getHumanPresentableName().equalsIgnoreCase("Integer Array")) {
                return true;
            } else {
                return false;
            }
        }

        /**
         * This mehtod is getter for the transferred data
         * @param flavor the type of data that want to be imported
         * @return the transferred data
         */
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (flavor.getHumanPresentableName().equalsIgnoreCase(flavors[0].getHumanPresentableName())) {
                return this.items;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
}
