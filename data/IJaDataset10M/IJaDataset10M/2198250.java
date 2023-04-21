package org.mitre.rt.client.ui.cchecks.complex.subcomplex;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import org.apache.log4j.Logger;
import org.mitre.rt.client.comparators.StringComparator;
import org.mitre.rt.client.core.MetaManager;
import org.mitre.rt.client.xml.ComplexComplianceCheckHelper;
import org.mitre.rt.rtclient.ApplicationType;
import org.mitre.rt.rtclient.ApplicationType.ComplexComplianceChecks;
import org.mitre.rt.rtclient.ComplexComplianceCheckType;
import org.mitre.rt.rtclient.SharedIdType;

/**
 *
 * @author  JWINSTON
 */
public class SubComplexCheckAddDialog extends javax.swing.JDialog {

    PropertyChangeListener windowClose = null;

    private static final Logger logger = Logger.getLogger(SubComplexCheckAddDialog.class.getPackage().getName());

    private ApplicationType app = null;

    private ComplexComplianceCheckType complexCheck = null;

    private SubComplexCheckAddTableModel model = null;

    private JTable table = null;

    private final ComplexComplianceCheckHelper complexCheckHelper = new ComplexComplianceCheckHelper();

    /**
   * 
   * @param parent    --  GUI parent Frame object
   * @param modal     --  is this window modal?
   * @param app       --  Application element containing this complexCheck
   * @param xmlParent --  XML container element for comments ( ComplexCheckRefs )
   * @param propChange
   */
    public SubComplexCheckAddDialog(java.awt.Frame parent, boolean modal, ApplicationType app, ComplexComplianceCheckType complexCheck) {
        super(parent, modal);
        initComponents();
        setWindowClose();
        this.complexCheck = complexCheck;
        this.app = app;
        createTable(app, complexCheck);
        setWindowLocation();
    }

    private void setWindowLocation() {
        this.setLocationRelativeTo(MetaManager.getMainWindow());
    }

    private void setWindowClose() {
        this.windowClose = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                closeWindow();
            }
        };
    }

    private void closeWindow() {
        this.dispose();
    }

    private void createTable(ApplicationType parent, ComplexComplianceCheckType complexCheck) {
        app = parent;
        this.complexCheck = complexCheck;
        ComplexComplianceChecks filteredComplexChecks = getFilteredComplexChecks();
        model = new SubComplexCheckAddTableModel(parent, filteredComplexChecks);
        table = new JTable(model);
        SubComplexCheckAddTextRenderer renderer = new SubComplexCheckAddTextRenderer(table, app);
        TableColumn titleColumn = table.getColumnModel().getColumn(SubComplexCheckAddTableModel.TITLE);
        titleColumn.setCellRenderer(renderer);
        titleColumn.setPreferredWidth(300);
        TableColumn statusColumn = table.getColumnModel().getColumn(SubComplexCheckAddTableModel.STATUS);
        statusColumn.setCellRenderer(renderer);
        statusColumn.setPreferredWidth(100);
        TableColumn lastModColumn = table.getColumnModel().getColumn(SubComplexCheckAddTableModel.LAST_MODIFIED);
        lastModColumn.setCellRenderer(renderer);
        lastModColumn.setPreferredWidth(100);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setSelectionListener();
        setTableRowSorter();
        table.setVisible(true);
        scrollSubComplexChecks.setViewportView(table);
    }

    private void setTableRowSorter() {
        TableRowSorter<SubComplexCheckAddTableModel> trs = new TableRowSorter<SubComplexCheckAddTableModel>(this.model);
        StringComparator titleCompare = new StringComparator("Title", StringComparator.ELEMENT);
        this.table.setAutoCreateRowSorter(false);
        trs.setComparator(SubComplexCheckAddTableModel.TITLE, titleCompare);
        this.table.setRowSorter(trs);
    }

    private void setSelectionListener() {
        ListSelectionModel selectionListenerModel = this.table.getSelectionModel();
        selectionListenerModel.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                firePropertyChange("row_selected", false, true);
                btnAdd.setEnabled(true);
            }
        });
    }

    /**
     *
     * @return the set of complex checks that are not referenced in other complex checks in the application
     */
    private ComplexComplianceChecks getFilteredComplexChecks() {
        List<ComplexComplianceCheckType> allComplexChecks = null;
        if (app.getComplexComplianceChecks() != null) allComplexChecks = complexCheckHelper.getActiveItems(app.getComplexComplianceChecks().getComplexComplianceCheckList()); else allComplexChecks = new ArrayList<ComplexComplianceCheckType>(0);
        ArrayList<ComplexComplianceCheckType> filteredComplexCheckList = new ArrayList<ComplexComplianceCheckType>(allComplexChecks.size());
        HashSet<String> complexChecksInChecks = new HashSet<String>();
        complexChecksInChecks.add(complexCheck.getId());
        for (ComplexComplianceCheckType tmpccc : allComplexChecks) {
            if (tmpccc.isSetComplexComplianceCheckRefs()) {
                for (SharedIdType complexCheckRef : tmpccc.getComplexComplianceCheckRefs().xgetSharedIdList()) {
                    complexChecksInChecks.add(complexCheckRef.getStringValue());
                }
            }
        }
        for (ComplexComplianceCheckType ccc : allComplexChecks) {
            if (!complexChecksInChecks.contains(ccc.getId())) {
                if (!complexCheckHelper.isDescendantOf(allComplexChecks, ccc, complexCheck)) filteredComplexCheckList.add(ccc);
            }
        }
        ComplexComplianceChecks filteredComplexChecks = ComplexComplianceChecks.Factory.newInstance();
        ComplexComplianceCheckType[] complexCheckArray = new ComplexComplianceCheckType[filteredComplexChecks.sizeOfComplexComplianceCheckArray()];
        complexCheckArray = filteredComplexCheckList.toArray(complexCheckArray);
        filteredComplexChecks.setComplexComplianceCheckArray(complexCheckArray);
        return filteredComplexChecks;
    }

    private void addClicked() {
        int[] rows = table.getSelectedRows();
        if (rows.length == 0) {
            return;
        }
        for (int i = 0; i < rows.length; i++) {
            ComplexComplianceCheckType grp = (ComplexComplianceCheckType) table.getValueAt(rows[i], 0);
            addComplexCheck(grp.getId());
        }
        complexCheckHelper.markModified(complexCheck);
        firePropertyChange("data_changed", null, null);
        firePropertyChange("window_close", false, true);
        closeWindow();
    }

    public SharedIdType addComplexCheck(String complexCheckId) {
        logger.info("Adding subComplexCheck " + complexCheckId + " to ComplexCheck " + complexCheck.getId());
        SharedIdType rr = null;
        rr = complexCheckHelper.addSubComplexCheckReference(app, complexCheck, complexCheckId);
        return rr;
    }

    private void initComponents() {
        panel = new javax.swing.JPanel();
        panelText = new javax.swing.JPanel();
        scrollSubComplexChecks = new javax.swing.JScrollPane();
        panelSouth = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnAdd = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add Sub Compliance Checks");
        setModal(true);
        panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Sub Complex Compliance Checks"));
        panel.setMaximumSize(new java.awt.Dimension(500, 250));
        panel.setName("panelMain");
        panel.setPreferredSize(new java.awt.Dimension(475, 250));
        panel.setLayout(new java.awt.BorderLayout());
        panelText.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        panelText.setMaximumSize(new java.awt.Dimension(450, 200));
        panelText.setMinimumSize(new java.awt.Dimension(200, 50));
        panelText.setPreferredSize(new java.awt.Dimension(400, 150));
        panelText.setLayout(new java.awt.BorderLayout());
        scrollSubComplexChecks.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelText.add(scrollSubComplexChecks, java.awt.BorderLayout.CENTER);
        panel.add(panelText, java.awt.BorderLayout.CENTER);
        panelSouth.setLayout(new java.awt.BorderLayout());
        panelButtons.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 1, 2, 2));
        panelButtons.setMaximumSize(new java.awt.Dimension(150, 50));
        panelButtons.setMinimumSize(new java.awt.Dimension(150, 27));
        panelButtons.setLayout(new javax.swing.BoxLayout(panelButtons, javax.swing.BoxLayout.X_AXIS));
        btnAdd.setText("Add");
        btnAdd.setMaximumSize(new java.awt.Dimension(65, 23));
        btnAdd.setMinimumSize(new java.awt.Dimension(65, 23));
        btnAdd.setPreferredSize(new java.awt.Dimension(65, 23));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        panelButtons.add(btnAdd);
        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });
        panelButtons.add(btnCancel);
        panelSouth.add(panelButtons, java.awt.BorderLayout.EAST);
        panel.add(panelSouth, java.awt.BorderLayout.SOUTH);
        getContentPane().add(panel, java.awt.BorderLayout.CENTER);
        getAccessibleContext().setAccessibleName("Add New Sub Groups");
        pack();
    }

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        addClicked();
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        logger.info("Canceled adding subComplexCheck to ComplexCheck " + complexCheck.getId());
        closeWindow();
    }

    private javax.swing.JButton btnAdd;

    private javax.swing.JButton btnCancel;

    private javax.swing.JPanel panel;

    private javax.swing.JPanel panelButtons;

    private javax.swing.JPanel panelSouth;

    private javax.swing.JPanel panelText;

    private javax.swing.JScrollPane scrollSubComplexChecks;
}
