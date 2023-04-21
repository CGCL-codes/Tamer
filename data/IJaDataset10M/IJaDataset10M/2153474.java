package com.ravana.transfer;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.LocationInfo;
import com.openbravo.pos.reports.JParamsLocation;
import com.ravana.purchasing.DataLogicPurchasing;
import com.ravana.purchasing.JParamsSupplier;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;

/**
 *
 * @author  Manjuka Soysa
 */
public class NewTransferConfigurator extends javax.swing.JPanel {

    NewTransferFunctions functions;

    ComboBoxValModel suppliersModel;

    ComboBoxValModel locationsModel;

    private JComboBox supplierBox;

    private JComboBox locationBox;

    /** Creates new form NewTransferConfigurator */
    public NewTransferConfigurator(NewTransferFunctions f) {
        initComponents();
        functions = f;
    }

    void init() {
        supplierBox = new JComboBox();
        supplierPanel.setLayout(new BorderLayout());
        supplierPanel.add(supplierBox, BorderLayout.CENTER);
        supplierBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                BranchInfo si = (BranchInfo) suppliersModel.getSelectedItem();
                if (si == null) {
                    return;
                }
                salesTaxFreeField.setSelected(si.getSalesTaxExcluded());
            }
        });
        locationBox = new JComboBox();
        locationPanel.setLayout(new BorderLayout());
        locationPanel.add(locationBox, BorderLayout.CENTER);
    }

    void activate(AppView app) throws BasicException {
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        DataLogicPurchasing dlPurch = (DataLogicPurchasing) app.getBean("com.ravana.purchasing.DataLogicPurchasingCreate");
        SentenceList suppliers = dlPurch.getBranchList();
        List a = suppliers.list();
        suppliersModel = new ComboBoxValModel(a);
        supplierBox.setModel(suppliersModel);
        suppliersModel.setSelectedFirst();
        if (a.size() > 0) {
            supplierBox.setSelectedIndex(0);
        }
        SentenceList locations = dlSales.getLocationsList();
        a = locations.list();
        locationsModel = new ComboBoxValModel(a);
        locationBox.setModel(locationsModel);
        locationsModel.setSelectedFirst();
        transferInButton.setSelected(true);
    }

    void createNew() {
        try {
            TransferType tt = TransferType.IN;
            if (transferOutButton.isSelected()) {
                tt = TransferType.OUT;
            }
            functions.editNewTransfer((BranchInfo) suppliersModel.getSelectedItem(), (LocationInfo) locationsModel.getSelectedItem(), !salesTaxFreeField.isSelected(), tt);
        } catch (BasicException ex) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, "Failed to create Transfer", ex);
            msg.show(this);
            Logger.getLogger(NewTransferConfigurator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initComponents() {
        filterOptionsGroup = new javax.swing.ButtonGroup();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        salesTaxFreeField = new javax.swing.JCheckBox();
        createButton = new javax.swing.JButton();
        supplierPanel = new javax.swing.JPanel();
        locationPanel = new javax.swing.JPanel();
        transferInButton = new javax.swing.JRadioButton();
        transferOutButton = new javax.swing.JRadioButton();
        jLabel1.setText("Branch");
        jLabel2.setText("Deliver To/From");
        salesTaxFreeField.setText("Sales Tax Excluded");
        createButton.setText("Create");
        createButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout supplierPanelLayout = new javax.swing.GroupLayout(supplierPanel);
        supplierPanel.setLayout(supplierPanelLayout);
        supplierPanelLayout.setHorizontalGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 135, Short.MAX_VALUE));
        supplierPanelLayout.setVerticalGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 25, Short.MAX_VALUE));
        javax.swing.GroupLayout locationPanelLayout = new javax.swing.GroupLayout(locationPanel);
        locationPanel.setLayout(locationPanelLayout);
        locationPanelLayout.setHorizontalGroup(locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 135, Short.MAX_VALUE));
        locationPanelLayout.setVerticalGroup(locationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 25, Short.MAX_VALUE));
        buttonGroup1.add(transferInButton);
        transferInButton.setText("Transfer In");
        buttonGroup1.add(transferOutButton);
        transferOutButton.setText("Transfer Out");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(transferOutButton).addComponent(transferInButton).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(locationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(salesTaxFreeField).addComponent(createButton)).addContainerGap(62, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2).addComponent(locationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addComponent(transferInButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(transferOutButton).addGap(18, 18, 18).addComponent(salesTaxFreeField).addGap(18, 18, 18).addComponent(createButton).addContainerGap(115, Short.MAX_VALUE)));
    }

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createNew();
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.JButton createButton;

    private javax.swing.ButtonGroup filterOptionsGroup;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel locationPanel;

    private javax.swing.JCheckBox salesTaxFreeField;

    private javax.swing.JPanel supplierPanel;

    private javax.swing.JRadioButton transferInButton;

    private javax.swing.JRadioButton transferOutButton;
}
