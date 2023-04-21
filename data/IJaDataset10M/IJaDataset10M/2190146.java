package com.apelon.beans.dts.kb;

import com.apelon.beans.apelapp.ApelApp;
import com.apelon.beans.apelconfig.ApelConfig;
import com.apelon.beans.apeldlg.ApelDlg;
import com.apelon.beans.apelpanel.ApelPanel;
import com.apelon.beans.dts.BeanContext;
import com.apelon.beans.dts.plugin.DTSAppManager;
import com.apelon.beans.dts.plugin.connection.DtsConnectionEvent;
import com.apelon.beans.dts.querymanager.NamespaceManager;
import com.apelon.beans.dts.querymanager.Sorter;
import com.apelon.beans.dts.querymanager.WriteManager;
import com.apelon.beans.dts.uielements.DTSBasePanel;
import com.apelon.beans.dts.uielements.MaxLengthTextField;
import com.apelon.common.log4j.Categories;
import com.apelon.common.table.ApelTableModel;
import com.apelon.common.table.ApelTableSorter2;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.client.DTSQuery;
import com.apelon.dts.client.attribute.QualifierType;
import com.apelon.dts.client.attribute.QualifiesItemType;
import com.apelon.dts.client.events.NamespaceEvent;
import com.apelon.dts.client.events.NamespaceListener;
import com.apelon.dts.client.namespace.Namespace;
import com.apelon.dts.common.DTSDataLimits;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author jszinger
 * @version 1.0
 */
public class QualTypeEditor extends DTSBasePanel implements NamespaceListener {

    private boolean useCodeIdFillIn = true;

    private final String QUALIFIER_STRING = "Qualifier";

    private DTSQuery fQueryMgr = DTSAppManager.getQuery();

    private NamespaceManager fNamespaceMgr = null;

    private Namespace[] namespaces = null;

    private Namespace[] writableNamespaces = null;

    private QualifierType[] qualTypes = null;

    private QualifierType currentQualType = null;

    private QualifierType editQualType = null;

    private int qualTypeEditMode = 0;

    private static final int QUAL_TYPE_NEW = 1;

    private static final int QUAL_TYPE_MODIFY = 2;

    private JPanel qualTypeTopPanel;

    private JButton bQualTypeNew;

    private JButton bQualTypeDel;

    private JPanel qualTypeBtmPanel;

    private JButton bQualTypeApply;

    private JButton bQualTypeClose;

    private JTable qualTypeTable;

    private ApelTableSorter2 tableSorter;

    private JTextField txfQualTypeName;

    private JTextField txfQualTypeID;

    private JTextField txfQualTypeCode;

    private JComboBox cboQualTypeNmsp;

    private JComboBox cboQualTypeQuals;

    private JCheckBox cbxHasValueList;

    private JButton btnEditValueList;

    public QualTypeEditor() {
        try {
            initialize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void initialize() throws Exception {
        Font myBoldFont = new java.awt.Font("Dialog", 1, 12);
        Insets gbInsetL1 = new Insets(0, 14, 0, 0);
        Insets gbInsetR1 = new Insets(4, 2, 2, 12);
        Insets gbInsetR2 = new Insets(2, 2, 2, 12);
        JPanel qualTypeInnerPanel = new JPanel();
        JLabel lblQualType = new JLabel("Qualifier Type:");
        JLabel lblQualTypeName = new JLabel("Name:");
        JLabel lblQualTypeID = new JLabel("ID:");
        JLabel lblQualTypeCode = new JLabel("Code:");
        JLabel lblQualTypeNmsp = new JLabel("Namespace:");
        JLabel lblQualTypeQuals = new JLabel("Qualifies:");
        lblQualType.setFont(myBoldFont);
        lblQualTypeName.setFont(myBoldFont);
        lblQualTypeID.setFont(myBoldFont);
        lblQualTypeCode.setFont(myBoldFont);
        lblQualTypeNmsp.setFont(myBoldFont);
        lblQualTypeQuals.setFont(myBoldFont);
        this.setPreferredSize(new Dimension(500, 450));
        this.setLayout(new BorderLayout());
        this.add(getQualTypeTopPanel(), BorderLayout.NORTH);
        this.add(qualTypeInnerPanel, BorderLayout.CENTER);
        qualTypeInnerPanel.setLayout(new GridBagLayout());
        qualTypeInnerPanel.setBorder(BorderFactory.createEtchedBorder());
        this.add(getQualTypeBtmPanel(), BorderLayout.SOUTH);
        JScrollPane tablePane = new JScrollPane(getQualTypeTable());
        tablePane.getViewport().setBackground(Color.WHITE);
        qualTypeInnerPanel.add(tablePane, new GridBagConstraints(0, 1, 3, 1, 1.0, 1.0, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(12, 12, 12, 12), 0, 0));
        qualTypeInnerPanel.add(lblQualTypeName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, gbInsetL1, 4, 4));
        qualTypeInnerPanel.add(getNameTxf(), new GridBagConstraints(1, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, gbInsetR1, 0, 0));
        qualTypeInnerPanel.add(lblQualTypeID, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, gbInsetL1, 4, 4));
        qualTypeInnerPanel.add(getIdTxf(), new GridBagConstraints(1, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, gbInsetR2, 0, 0));
        qualTypeInnerPanel.add(lblQualTypeCode, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, gbInsetL1, 4, 4));
        qualTypeInnerPanel.add(getCodeTxf(), new GridBagConstraints(1, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, gbInsetR2, 0, 0));
        qualTypeInnerPanel.add(lblQualTypeNmsp, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, gbInsetL1, 4, 4));
        qualTypeInnerPanel.add(getNmspCbo(), new GridBagConstraints(1, 5, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, gbInsetR2, 0, 0));
        qualTypeInnerPanel.add(lblQualTypeQuals, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 14, 12, 0), 4, 4));
        qualTypeInnerPanel.add(getQualifiesCbo(), new GridBagConstraints(1, 6, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(2, 2, 12, 12), 0, 0));
    }

    public JTable getQualTypeTable() {
        if (qualTypeTable == null) {
            qualTypeTable = new JTable();
            qualTypeTable.setShowGrid(false);
            qualTypeTable.setRowSelectionAllowed(true);
            qualTypeTable.setColumnSelectionAllowed(false);
            qualTypeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            qualTypeTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent e) {
                    qualTypeTableSelectionChanged();
                }
            });
            TableModelListener tableModelListener = new TableModelListener() {

                public void tableChanged(TableModelEvent e) {
                    qualTypeTableChanged(e);
                }
            };
            qualTypeTable.getModel().addTableModelListener(tableModelListener);
        }
        return qualTypeTable;
    }

    TableModel makeTableModel(QualifierType[] qualTypeArray) {
        String[] colNames = new String[] { "Qualifier Type", "Namespace", "Qualifies" };
        TableModel qualTableModel = new ApelTableModel(colNames, qualTypeArray.length);
        for (int j = 0; j < qualTypeArray.length; j++) {
            qualTableModel.setValueAt(qualTypeArray[j].getName(), j, 0);
            int nmspId = (qualTypeArray[j]).getNamespaceId();
            Namespace nmsp = NamespaceManager.findNamespaceById(nmspId);
            String nmspNm;
            if (nmsp != null) {
                nmspNm = nmsp.getName();
            } else {
                nmspNm = "";
                ApelApp.showWarningMsg("Qualifier Type Editor: Error getting " + "Namespace from Knowldedge Base", BeanContext.getInstance().getMainAppFrame());
            }
            qualTableModel.setValueAt(nmspNm, j, 1);
            qualTableModel.setValueAt(qualTypeArray[j].getQualifies(), j, 2);
        }
        return qualTableModel;
    }

    ApelTableSorter2 getTableSorter() {
        if (tableSorter == null) {
            tableSorter = new ApelTableSorter2(makeTableModel(qualTypes));
            tableSorter.setTableHeader(getQualTypeTable().getTableHeader());
        }
        return tableSorter;
    }

    void setTableWids() {
        int[] colWids = new int[] { 150, 150, 100 };
        TableColumnModel cm = getQualTypeTable().getColumnModel();
        for (int i = 0; i < colWids.length; i++) {
            cm.getColumn(i).setMinWidth(colWids[i]);
        }
    }

    public JCheckBox getHasValueListCbx() {
        if (cbxHasValueList == null) {
            cbxHasValueList = new JCheckBox("Constrain to Eumerated Values");
            cbxHasValueList.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_cbxHasValueList(e);
                }
            });
        }
        return cbxHasValueList;
    }

    public JButton getEditValueListBtn() {
        if (btnEditValueList == null) {
            btnEditValueList = new JButton("Edit Qualifier Values");
            btnEditValueList.setEnabled(getHasValueListCbx().isSelected());
            btnEditValueList.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_btnEditValueList(e);
                }
            });
        }
        return btnEditValueList;
    }

    protected JPanel getQualTypeTopPanel() {
        if (qualTypeTopPanel == null) {
            qualTypeTopPanel = new JPanel();
            qualTypeTopPanel.setLayout(new FlowLayout());
            qualTypeTopPanel.add(getNewButton());
            qualTypeTopPanel.add(getDeleteButton());
        }
        return qualTypeTopPanel;
    }

    public JButton getNewButton() {
        if (bQualTypeNew == null) {
            bQualTypeNew = new JButton("New");
            bQualTypeNew.setMargin(new Insets(1, 20, 1, 20));
            bQualTypeNew.setEnabled(false);
            bQualTypeNew.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_bQualTypeNew(e);
                }
            });
        }
        return bQualTypeNew;
    }

    public JButton getDeleteButton() {
        if (bQualTypeDel == null) {
            bQualTypeDel = new JButton("Delete");
            bQualTypeDel.setMargin(new Insets(1, 14, 1, 14));
            bQualTypeDel.setEnabled(false);
            bQualTypeDel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_bQualTypeDelete(e);
                }
            });
        }
        return bQualTypeDel;
    }

    protected JPanel getQualTypeBtmPanel() {
        if (qualTypeBtmPanel == null) {
            qualTypeBtmPanel = new JPanel();
            qualTypeBtmPanel.setLayout(new FlowLayout());
            qualTypeBtmPanel.add(getApplyButton());
            qualTypeBtmPanel.add(getCloseButton());
        }
        return qualTypeBtmPanel;
    }

    public JButton getApplyButton() {
        if (bQualTypeApply == null) {
            bQualTypeApply = new JButton("Apply");
            bQualTypeApply.setMargin(new Insets(1, 16, 1, 16));
            bQualTypeApply.setEnabled(false);
            bQualTypeApply.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_bQualTypeApply(e);
                }
            });
        }
        return bQualTypeApply;
    }

    public JButton getCloseButton() {
        if (bQualTypeClose == null) {
            bQualTypeClose = new JButton("Close");
            bQualTypeClose.setMargin(new Insets(1, 14, 1, 14));
            bQualTypeClose.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    actionPerformed_bQualTypeClose(e);
                }
            });
        }
        return bQualTypeClose;
    }

    public JTextField getNameTxf() {
        if (txfQualTypeName == null) {
            txfQualTypeName = new MaxLengthTextField(DTSDataLimits.LEN_NAME);
            txfQualTypeName.setEnabled(false);
            txfQualTypeName.setOpaque(false);
            txfQualTypeName.addKeyListener(new KeyListener() {

                public void keyPressed(KeyEvent e) {
                }

                public void keyReleased(KeyEvent e) {
                }

                public void keyTyped(KeyEvent e) {
                    getApplyButton().setEnabled(true);
                }
            });
        }
        return txfQualTypeName;
    }

    public JTextField getIdTxf() {
        if (txfQualTypeID == null) {
            LimitedDocument ld = new LimitedDocument(10);
            txfQualTypeID = new JTextField(ld, "", 0);
            txfQualTypeID.setEnabled(false);
            txfQualTypeID.setOpaque(false);
        }
        return txfQualTypeID;
    }

    public JTextField getCodeTxf() {
        if (txfQualTypeCode == null) {
            LimitedDocument ld = new LimitedDocument(32);
            txfQualTypeCode = new JTextField(ld, "", 0);
            txfQualTypeCode.setEnabled(false);
            txfQualTypeCode.setOpaque(false);
        }
        return txfQualTypeCode;
    }

    public JComboBox getNmspCbo() {
        if (cboQualTypeNmsp == null) {
            cboQualTypeNmsp = new JComboBox();
            cboQualTypeNmsp.setEnabled(false);
        }
        return cboQualTypeNmsp;
    }

    public JComboBox getQualifiesCbo() {
        if (cboQualTypeQuals == null) {
            cboQualTypeQuals = new JComboBox();
            cboQualTypeQuals.addItem("Concept Association");
            cboQualTypeQuals.addItem("Concept Property");
            cboQualTypeQuals.addItem("Term Association");
            cboQualTypeQuals.addItem("Term Property");
            cboQualTypeQuals.setEnabled(false);
        }
        return cboQualTypeQuals;
    }

    public void configure() {
        if (fQueryMgr.isOpen()) {
            configNamespMgr(fQueryMgr);
            loadQualTypes();
            loadNamespaces();
            getQualTypeTable().clearSelection();
            clearFields();
            if (getNmspCbo().getItemCount() == 0) {
                getNewButton().setEnabled(false);
            } else {
                getNewButton().setEnabled(true);
                this.setDefaultButton(getNewButton());
            }
            getApplyButton().setEnabled(false);
        }
    }

    public void clear() {
        if (fQueryMgr.isOpen()) {
            clearQualTypes();
            clearNamespaces();
            getQualTypeTable().clearSelection();
            clearFields();
            getNewButton().setEnabled(false);
            getDeleteButton().setEnabled(false);
            fNamespaceMgr = null;
            currentQualType = null;
        }
    }

    private void clearQualTypes() {
        qualTypes = null;
    }

    private void loadQualTypes() {
        clearQualTypes();
        if (fQueryMgr.isOpen()) {
            try {
                qualTypes = fQueryMgr.getAssociationQuery().getAllQualifierTypes();
                Sorter.sort(qualTypes);
                tableSorter = null;
                getQualTypeTable().setModel(getTableSorter());
                setTableWids();
                getTableSorter().setSortingStatus(0, ApelTableSorter2.ASCENDING);
            } catch (DTSException ex) {
                String s = "Error while loading the Qualifier Types. ";
                BeanContext.handleException(s, ex);
            }
        }
    }

    private void clearNamespaces() {
        getNmspCbo().removeAllItems();
        namespaces = null;
    }

    private void loadNamespaces() {
        clearNamespaces();
        if (fNamespaceMgr != null) {
            try {
                namespaces = fNamespaceMgr.getNamespaces();
                Sorter.sort(namespaces);
                loadWritableNamespaces();
                Namespace currentLocalNmsp = fNamespaceMgr.getCurrentLocalNamespace();
                for (int i = 0; i < writableNamespaces.length; i++) {
                    getNmspCbo().addItem(writableNamespaces[i].getName());
                    if (currentLocalNmsp != null) {
                        if (currentLocalNmsp.equals(writableNamespaces[i])) {
                            getNmspCbo().setSelectedIndex(i);
                        }
                    }
                }
            } catch (DTSException ex) {
                String s = "Error while loading the namespaces.";
                BeanContext.handleException(s, ex);
            }
        }
    }

    private void loadWritableNamespaces() {
        if (namespaces != null) {
            int len = 0;
            for (int i = 0; i < namespaces.length; i++) {
                if (namespaces[i].isWritable()) {
                    len++;
                }
            }
            writableNamespaces = new Namespace[len];
            int j = 0;
            for (int i = 0; i < namespaces.length; i++) {
                if (namespaces[i].isWritable()) {
                    writableNamespaces[j] = namespaces[i];
                    j++;
                }
            }
        }
    }

    private void configNamespMgr(DTSQuery manager) {
        if (!manager.isOpen()) {
            throw new RuntimeException("Cannot configure Namespace without Server Connection");
        }
        if (fNamespaceMgr == null) {
            fNamespaceMgr = NamespaceManager.getInstance();
        }
    }

    private void enableEditFieldsforNew() {
        getNameTxf().setText("");
        getNameTxf().setEditable(true);
        getNameTxf().setEnabled(true);
        getNameTxf().setOpaque(true);
        getNameTxf().requestFocusInWindow();
        if (useCodeIdFillIn) {
            getIdTxf().setText("");
            getIdTxf().setEditable(false);
            getIdTxf().setEnabled(false);
            getIdTxf().setOpaque(false);
            getCodeTxf().setText("");
            getCodeTxf().setEditable(false);
            getCodeTxf().setEnabled(false);
            getCodeTxf().setOpaque(false);
        } else {
            getIdTxf().setText("");
            getIdTxf().setEditable(true);
            getIdTxf().setEnabled(true);
            getIdTxf().setOpaque(true);
            getCodeTxf().setText("");
            getCodeTxf().setEditable(true);
            getCodeTxf().setEnabled(true);
            getCodeTxf().setOpaque(true);
        }
        loadNamespaces();
        getNmspCbo().setEnabled(true);
        getQualifiesCbo().setEnabled(true);
        getApplyButton().setEnabled(true);
        getDeleteButton().setEnabled(false);
    }

    private void checkForOkayToModify() {
        if (qualTypeEditMode != QUAL_TYPE_NEW) {
            if (currentQualType != null) {
                Namespace nmsp = fNamespaceMgr.findNamespaceById(currentQualType.getNamespaceId());
                if (nmsp.isWritable()) {
                    qualTypeEditMode = QUAL_TYPE_MODIFY;
                    enableEditFieldsforModify();
                } else {
                    qualTypeEditMode = 0;
                    disableEditFields();
                }
            }
        }
    }

    private void enableEditFieldsforModify() {
        getNameTxf().setEditable(true);
        getNameTxf().setEnabled(true);
        getNameTxf().setOpaque(true);
        getApplyButton().setEnabled(false);
        getDeleteButton().setEnabled(true);
    }

    private void disableEditFields() {
        getNameTxf().setEditable(false);
        getNameTxf().setEnabled(false);
        getNameTxf().setOpaque(false);
        getIdTxf().setEditable(false);
        getIdTxf().setEnabled(false);
        getIdTxf().setOpaque(false);
        getCodeTxf().setEditable(false);
        getCodeTxf().setEnabled(false);
        getCodeTxf().setOpaque(false);
        getNmspCbo().setEnabled(false);
        getQualifiesCbo().setEnabled(false);
        getApplyButton().setEnabled(false);
        getDeleteButton().setEnabled(true);
    }

    private void clearFields() {
        getNameTxf().setText("");
        getNameTxf().setEditable(false);
        getNameTxf().setOpaque(false);
        getIdTxf().setText("");
        getIdTxf().setEditable(false);
        getIdTxf().setOpaque(false);
        getCodeTxf().setText("");
        getCodeTxf().setEditable(false);
        getCodeTxf().setOpaque(false);
        getNmspCbo().setEnabled(false);
        getQualifiesCbo().setEnabled(false);
        getApplyButton().setEnabled(false);
    }

    private void updateFields(int index) {
        getNameTxf().setEnabled(true);
        getNameTxf().setOpaque(true);
        getNameTxf().setText((qualTypes.length > index) ? ("" + qualTypes[index].getName()) : "");
        getIdTxf().setEnabled(false);
        getIdTxf().setOpaque(false);
        getIdTxf().setText((qualTypes.length > index) ? ("" + qualTypes[index].getId()) : "");
        getCodeTxf().setEnabled(false);
        getCodeTxf().setOpaque(false);
        getCodeTxf().setText((qualTypes.length > index) ? ("" + qualTypes[index].getCode()) : "");
        if (index >= 0 && index < qualTypes.length) {
            Namespace namesp = fNamespaceMgr.findNamespaceById(qualTypes[index].getNamespaceId());
            if (namesp.isWritable()) {
                if (index >= 0 && index < qualTypes.length) {
                    for (int i = 0; i < getNmspCbo().getItemCount(); i++) {
                        if (qualTypes[index].getNamespaceId() == writableNamespaces[i].getId()) {
                            getNmspCbo().setSelectedIndex(i);
                        }
                    }
                }
                getNmspCbo().setEnabled(false);
            } else {
                String namespName = namesp.getName();
                getNmspCbo().addItem(namespName);
                getNmspCbo().setSelectedItem(namespName);
                getNmspCbo().setEnabled(false);
            }
            QualifiesItemType qualifies = qualTypes[index].getQualifies();
            if (qualifies == QualifiesItemType.CONCEPT_ASSOCIATION) {
                getQualifiesCbo().setSelectedIndex(0);
            } else if (qualifies == QualifiesItemType.CONCEPT_PROPERTY) {
                getQualifiesCbo().setSelectedIndex(1);
            } else if (qualifies == QualifiesItemType.TERM_ASSOCIATION) {
                getQualifiesCbo().setSelectedIndex(2);
            } else if (qualifies == QualifiesItemType.TERM_PROPERTY) {
                getQualifiesCbo().setSelectedIndex(3);
            }
        }
        try {
            if (index >= 0 && index < qualTypes.length) {
                if (hasMatchingConcepts(qualTypes[index])) {
                    getQualifiesCbo().setEnabled(false);
                } else {
                    getQualifiesCbo().setEnabled(true);
                }
            }
        } catch (DTSException ex) {
            getQualifiesCbo().setEnabled(false);
            String s = "DTS Exception in QualTypeEditor.updateFields(). ";
            BeanContext.handleException(s, ex);
        }
    }

    public void connectionOpen(DtsConnectionEvent event) {
    }

    public void connectionClose(DtsConnectionEvent event) {
        this.clear();
    }

    public void namespaceActionOccurred(NamespaceEvent event) {
        loadNamespaces();
    }

    void qualTypeTableSelectionChanged() {
        int sortIndex = getQualTypeTable().getSelectedRow();
        if (sortIndex < 0 || sortIndex > qualTypes.length) {
            return;
        }
        int normIndex = getTableSorter().modelIndex(sortIndex);
        if (normIndex >= 0 && normIndex < qualTypes.length) {
            currentQualType = qualTypes[normIndex];
            loadNamespaces();
            updateFields(normIndex);
            qualTypeEditMode = 0;
        }
        checkForOkayToModify();
    }

    void qualTypeTableChanged(TableModelEvent e) {
    }

    void actionPerformed_bQualTypeNew(ActionEvent e) {
        if (getApplyButton().isEnabled()) {
            handleUnsavedChanges();
        }
        qualTypeEditMode = QUAL_TYPE_NEW;
        enableEditFieldsforNew();
        setDefaultButton(getApplyButton());
    }

    void handleUnsavedChanges() {
        WriteManager writeMgr = WriteManager.getInstance();
        editQualType = makeQualTypeFromUIFields(false);
        if (!writeMgr.doIdCheck(editQualType.getId(), useCodeIdFillIn, false, QUALIFIER_STRING)) {
            qualTypeEditMode = 0;
            clearFields();
            return;
        }
        String s;
        if (qualTypeEditMode == QUAL_TYPE_NEW) {
            s = "Do you want save the new Qualfier Type " + editQualType.getName() + "?";
        } else {
            s = "Do you want save changes to Qualfier Type " + editQualType.getName() + "?";
        }
        int result = JOptionPane.showConfirmDialog(this, s);
        if (result == 0) {
            writeQualTypeToKB(editQualType);
            loadQualTypes();
            loadNamespaces();
            getQualTypeTable().clearSelection();
        }
        qualTypeEditMode = 0;
        clearFields();
    }

    void actionPerformed_bQualTypeDelete(ActionEvent e) {
        QualifierType qualTypeToDelete = currentQualType;
        String strQualTypeToDelete = "";
        strQualTypeToDelete += qualTypeToDelete.getName() + " ( ";
        strQualTypeToDelete += qualTypeToDelete.getId() + ", ";
        strQualTypeToDelete += qualTypeToDelete.getCode() + " )";
        try {
            Namespace targetNmsp = fNamespaceMgr.findNamespaceById(currentQualType.getNamespaceId());
            if (!fQueryMgr.getNamespaceQuery().hasPermission(currentQualType.getNamespaceId())) {
                ApelApp.showWarningMsg("Cannot delete Qualifier Type. \n" + "User does not have permission \n" + "to write to the target Namespace.", this);
                return;
            } else if (!targetNmsp.isWritable()) {
                ApelApp.showWarningMsg("Cannot delete Qualifier Type. \n" + "The target Namespace must be 'Writable'.", this);
                return;
            } else {
                String message = "Are you sure you want to delete the Qualifier Type \n" + strQualTypeToDelete + "? \n";
                int returnVal = JOptionPane.showConfirmDialog(null, message, "Confirm Delete", JOptionPane.WARNING_MESSAGE);
                if (returnVal == JOptionPane.OK_OPTION) {
                    System.out.println("Deleting Qualifier Type : " + qualTypeToDelete);
                    boolean boolDeleted = fQueryMgr.getAssociationQuery().deleteQualifierType(qualTypeToDelete);
                    loadQualTypes();
                    getQualTypeTable().clearSelection();
                    qualTypeEditMode = 0;
                    clearFields();
                }
                setDefaultButton(getNewButton());
            }
        } catch (DTSException ex) {
            String s = "Error while deleting the Qualifier Type. ";
            BeanContext.handleException(s, ex);
        }
    }

    void actionPerformed_bQualTypeApply(ActionEvent e) {
        WriteManager writeMgr = WriteManager.getInstance();
        editQualType = makeQualTypeFromUIFields(true);
        if (editQualType == null) {
            return;
        }
        if (!writeMgr.doIdCheck(editQualType.getId(), useCodeIdFillIn, true, QUALIFIER_STRING)) {
            return;
        }
        QualifierType type = writeQualTypeToKB(editQualType);
        if (type == null || type.getId() == -1) {
            return;
        }
        loadQualTypes();
        loadNamespaces();
        getQualTypeTable().clearSelection();
        qualTypeEditMode = 0;
        clearFields();
        getApplyButton().setEnabled(false);
    }

    void actionPerformed_bQualTypeClose(ActionEvent e) {
        try {
            panelComplete(ApelPanel.OK_CLICKED);
            qualTypeEditMode = 0;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
        return;
    }

    QualifierType makeQualTypeFromUIFields(boolean showWarnings) {
        String qtName = null;
        int qtId = -1;
        String qtCode = null;
        int qtNmspId = -1;
        QualifiesItemType qualfs = QualifiesItemType.CONCEPT_ASSOCIATION;
        if (qualTypeEditMode == QUAL_TYPE_NEW || qualTypeEditMode == QUAL_TYPE_MODIFY) {
            qtName = getNameTxf().getText();
            qtName = qtName.trim();
            if (qtName == null || qtName.equals("")) {
                if (showWarnings) {
                    ApelApp.showWarningMsg("Cannot write Qualifier Type to the server. \n" + "The Qualifier Type Name cannot be blank.", this);
                }
                return null;
            }
            if (qualTypeEditMode == QUAL_TYPE_NEW) {
                if (!useCodeIdFillIn) {
                    String qualIdStr = getIdTxf().getText();
                    if (qualIdStr == null || qualIdStr.equals("")) {
                        if (showWarnings) {
                            ApelApp.showWarningMsg("Cannot write Qualifier Type to the server. \n" + "The Qualifier Type Id cannot be blank.", this);
                        }
                        return null;
                    }
                    try {
                        qtId = Integer.parseInt(qualIdStr);
                    } catch (NumberFormatException nfe) {
                        if (showWarnings) {
                            ApelApp.showWarningMsg("Cannot write Qualifier Type to the server. \n" + "The Qualifier Type ID must be a valid integer \n" + "in the range from 0 to 2,147,483,647.", this);
                        }
                        return null;
                    }
                    qtCode = getCodeTxf().getText();
                    if (qtCode == null || qtCode.equals("")) {
                        if (showWarnings) {
                            ApelApp.showWarningMsg("Cannot write Qualifier Type to the server. \n" + "The Qualifier Type Code cannot be blank.", BeanContext.getInstance().getMainAppFrame());
                        }
                        return null;
                    }
                } else {
                    qtId = -1;
                    qtCode = "null";
                }
            } else {
                qtId = currentQualType.getId();
                qtCode = currentQualType.getCode();
            }
            int selInd = getNmspCbo().getSelectedIndex();
            if (selInd < 0) {
                if (getNmspCbo().getItemCount() == 0) {
                    ApelApp.showWarningMsg("Cannot write Qualifier Type to the Knowledge Base. \n" + "There are no writable Namespaces. Use the Namespace \n " + "Editor to create a local, editable Namespace.", this);
                    return null;
                }
                selInd = 0;
                getNmspCbo().setSelectedIndex(selInd);
            }
            qtNmspId = writableNamespaces[getNmspCbo().getSelectedIndex()].getId();
            qualfs = getQItemType();
        }
        return new QualifierType(qtName, qtId, qtCode, qtNmspId, qualfs);
    }

    QualifierType writeQualTypeToKB(QualifierType qualTypeToWrite) {
        try {
            if (qualTypeEditMode == QUAL_TYPE_NEW) {
                qualTypeToWrite = fQueryMgr.getAssociationQuery().addQualifierType(qualTypeToWrite);
            } else if (qualTypeEditMode == QUAL_TYPE_MODIFY) {
                qualTypeToWrite = fQueryMgr.getAssociationQuery().updateQualifierType(currentQualType, qualTypeToWrite);
            }
            setDefaultButton(getNewButton());
        } catch (DTSException ex) {
            String s = "Error while writing the Qualifier Type. ";
            BeanContext.handleException(s, ex);
        }
        return qualTypeToWrite;
    }

    void actionPerformed_cbxHasValueList(ActionEvent e) {
        getEditValueListBtn().setEnabled(getHasValueListCbx().isSelected());
    }

    void actionPerformed_btnEditValueList(ActionEvent e) {
        EnumValueEditor enumEditor = new EnumValueEditor();
        showPanel(enumEditor, "Qualifier Type Enumerated Value Editor", 450, 300);
    }

    protected int showPanel(ApelPanel panel, String panelName, int width, int height) {
        JFrame appFrame = BeanContext.getInstance().getMainAppFrame();
        ApelDlg dlg = new ApelDlg(panel, appFrame, appFrame);
        dlg.setTitle(panelName);
        dlg.setSize(width, height);
        dlg.centerInWindow(appFrame);
        dlg.setResizable(true);
        dlg.showApelDlg();
        return panel.getResult();
    }

    protected QualifiesItemType getQItemType() {
        if (getQualifiesCbo().getSelectedIndex() == 0) {
            return QualifiesItemType.CONCEPT_ASSOCIATION;
        } else if (getQualifiesCbo().getSelectedIndex() == 1) {
            return QualifiesItemType.CONCEPT_PROPERTY;
        } else if (getQualifiesCbo().getSelectedIndex() == 2) {
            return QualifiesItemType.TERM_ASSOCIATION;
        } else if (getQualifiesCbo().getSelectedIndex() == 3) {
            return QualifiesItemType.TERM_PROPERTY;
        } else {
            return QualifiesItemType.CONCEPT_ASSOCIATION;
        }
    }

    protected boolean hasMatchingConcepts(QualifierType qualType) throws DTSException {
        return true;
    }

    protected int getQualTypeIndex(QualifierType qualType) {
        for (int i = 0; i < qualTypes.length; i++) {
            if (qualTypes[i].equals(qualType)) {
                return i;
            }
        }
        return 0;
    }

    public String arrayToString(int[] intArray) {
        String s = "[";
        for (int i = 0; i < intArray.length; i++) {
            s += intArray[i];
            if (i < (intArray.length - 1)) {
                s += ", ";
            }
        }
        s += "]";
        return s;
    }

    protected void addListeners() {
        DTSAppManager.getQuery().getNamespaceQuery().addNamespaceListener(this);
    }

    protected void removeListeners() {
        DTSAppManager.getQuery().getNamespaceQuery().removeNamespaceListener(this);
    }
}
