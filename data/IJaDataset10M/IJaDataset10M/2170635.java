package com.openbravo.pos.customers;

import com.openbravo.data.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;
import cn.ekuma.data.dao.bean.I_Category;
import cn.ekuma.data.ui.swing.AbstractParentDAOJEditor;
import cn.ekuma.epos.datalogic.I_DataLogicSales;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.bean.Customer;
import com.openbravo.pos.bean.CustomerCategoryExt;
import com.openbravo.bean.TaxCustCategory;
import com.openbravo.pos.panels.JCategoryTreeDialog;
import com.openbravo.pos.util.StringUtils;
import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;
import java.util.ResourceBundle;
import javax.swing.JLabel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author  adrianromero
 */
@SuppressWarnings("serial")
public class CustomersEditor extends AbstractParentDAOJEditor<Customer, String> {

    private ComboBoxValModel m_CategoryModel, m_CategoryModelb;

    private DirtyManager m_Dirty;

    I_DataLogicSales dlSales;

    static SimpleDateFormat sdf = new SimpleDateFormat("ssyyddmm");

    /** Creates new form CustomersView */
    public CustomersEditor(I_DataLogicSales dlSales, DirtyManager dirty) {
        super(dirty);
        this.dlSales = dlSales;
        initComponents();
        m_CategoryModel = new ComboBoxValModel();
        m_CategoryModelb = new ComboBoxValModel();
        m_Dirty = dirty;
        m_jTaxID.getDocument().addDocumentListener(dirty);
        m_jSearchkey.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jCategory.addActionListener(dirty);
        m_jNotes.getDocument().addDocumentListener(dirty);
        txtMaxdebt.getDocument().addDocumentListener(dirty);
        m_jVisible.addActionListener(dirty);
        txtFirstName.getDocument().addDocumentListener(dirty);
        txtLastName.getDocument().addDocumentListener(dirty);
        txtEmail.getDocument().addDocumentListener(dirty);
        txtPhone.getDocument().addDocumentListener(dirty);
        txtPhone2.getDocument().addDocumentListener(dirty);
        txtFax.getDocument().addDocumentListener(dirty);
        txtAddress.getDocument().addDocumentListener(dirty);
        txtAddress2.getDocument().addDocumentListener(dirty);
        txtPostal.getDocument().addDocumentListener(dirty);
        txtCity.getDocument().addDocumentListener(dirty);
        txtRegion.getDocument().addDocumentListener(dirty);
        txtCountry.getDocument().addDocumentListener(dirty);
        m_jCategory1.addActionListener(dirty);
        m_iOpenCredit.getDocument().addDocumentListener(dirty);
        writeValueEOF();
    }

    public void activate() throws BasicException {
        List a = dlSales.list(TaxCustCategory.class);
        a.add(0, null);
        m_CategoryModel = new ComboBoxValModel(a);
        m_jCategory.setModel(m_CategoryModel);
        List b;
        try {
            b = dlSales.list(CustomerCategoryExt.class);
        } catch (BasicException eD) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotloadlists"), eD);
            msg.show(this);
            b = new ArrayList();
        }
        b.add(0, null);
        m_CategoryModelb = new ComboBoxValModel(b);
        m_jCategory1.setModel(m_CategoryModelb);
    }

    public void refresh() {
    }

    public Component getComponent() {
        return this;
    }

    private void initComponents() {
        jLabel7 = new javax.swing.JLabel();
        m_jTaxID = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        m_jSearchkey = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        m_jVisible = new javax.swing.JCheckBox();
        m_jVisible.setText(AppLocal.getIntString("label.visible"));
        jLabel5 = new javax.swing.JLabel();
        jcard = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        m_jCategory = new javax.swing.JComboBox();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMaxdebt = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtFax = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtLastName = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txtPhone2 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txtCountry = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtAddress2 = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txtPostal = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txtRegion = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jNotes = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        txtCurdebt = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCurdate = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        m_jCategory1 = new javax.swing.JComboBox();
        jSearchCategory = new javax.swing.JButton();
        jLabel7.setText(AppLocal.getIntString("label.taxid"));
        jLabel8.setText(AppLocal.getIntString("label.searchkey"));
        jLabel3.setText(AppLocal.getIntString("label.name"));
        jLabel5.setText(AppLocal.getIntString("label.card"));
        jcard.setEditable(false);
        jLabel9.setText(AppLocal.getIntString("label.custtaxcategory"));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png")));
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png")));
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jLabel1.setText(AppLocal.getIntString("label.maxdebt"));
        txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jLabel14.setText(AppLocal.getIntString("label.fax"));
        jLabel15.setText(AppLocal.getIntString("label.lastname"));
        jLabel16.setText(AppLocal.getIntString("label.email"));
        jLabel17.setText(AppLocal.getIntString("label.phone"));
        jLabel18.setText(AppLocal.getIntString("label.phone2"));
        jLabel19.setText(AppLocal.getIntString("label.firstname"));
        javax.swing.GroupLayout gl_jPanel1 = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(gl_jPanel1);
        gl_jPanel1.setHorizontalGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel1.createSequentialGroup().addContainerGap().addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel1.createSequentialGroup().addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtFax, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(259, Short.MAX_VALUE)));
        gl_jPanel1.setVerticalGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel1.createSequentialGroup().addContainerGap().addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel16).addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel17).addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(txtPhone2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel1.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(txtFax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(57, Short.MAX_VALUE)));
        jTabbedPane1.addTab(AppLocal.getIntString("label.contact"), jPanel1);
        jLabel13.setText(AppLocal.getIntString("label.address"));
        jLabel20.setText(AppLocal.getIntString("label.country"));
        jLabel21.setText(AppLocal.getIntString("label.address2"));
        jLabel22.setText(AppLocal.getIntString("label.postal"));
        jLabel23.setText(AppLocal.getIntString("label.city"));
        jLabel24.setText(AppLocal.getIntString("label.region"));
        javax.swing.GroupLayout gl_jPanel2 = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(gl_jPanel2);
        gl_jPanel2.setHorizontalGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel2.createSequentialGroup().addContainerGap().addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtPostal, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtRegion, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(gl_jPanel2.createSequentialGroup().addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtCountry, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(259, Short.MAX_VALUE)));
        gl_jPanel2.setVerticalGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel2.createSequentialGroup().addContainerGap().addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel13).addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel21).addComponent(txtAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel22).addComponent(txtPostal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel23).addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel24).addComponent(txtRegion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(gl_jPanel2.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel20).addComponent(txtCountry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(267, 267, 267)));
        jTabbedPane1.addTab(AppLocal.getIntString("label.location"), jPanel2);
        jScrollPane1.setViewportView(m_jNotes);
        javax.swing.GroupLayout gl_jPanel3 = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(gl_jPanel3);
        gl_jPanel3.setHorizontalGroup(gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel3.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE).addContainerGap()));
        gl_jPanel3.setVerticalGroup(gl_jPanel3.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(gl_jPanel3.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE).addContainerGap()));
        jTabbedPane1.addTab(AppLocal.getIntString("label.notes"), jPanel3);
        jLabel2.setText(AppLocal.getIntString("label.curdebt"));
        txtCurdebt.setEditable(false);
        txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jLabel6.setText(AppLocal.getIntString("label.curdate"));
        txtCurdate.setEditable(false);
        txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jLabel10.setText(AppLocal.getIntString("label.prodcategory"));
        jSearchCategory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/search.png")));
        jSearchCategory.setFocusable(false);
        jSearchCategory.setPreferredSize(new java.awt.Dimension(35, 25));
        jSearchCategory.setRequestFocusEnabled(false);
        jSearchCategory.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSearchCategoryActionPerformed(evt);
            }
        });
        label = new JLabel(AppLocal.getIntString("label.erppassword"));
        passwordField = new JPasswordField();
        JLabel lblOpenCredit = new JLabel(AppLocal.getIntString("label.openCredit"));
        m_iOpenCredit = new JTextField();
        m_iOpenCredit.setColumns(10);
        lblNewLabel = new JLabel(AppLocal.getIntString("label.score"));
        txtScore = new JTextField();
        txtScore.setEditable(false);
        txtScore.setColumns(10);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel7, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(m_jTaxID, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(m_jSearchkey, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE).addGap(28).addComponent(m_jVisible, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(m_jName, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel5, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(jcard, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(m_jCategory, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE))).addGap(14).addComponent(jButton2).addPreferredGap(ComponentPlacement.RELATED).addComponent(jButton3)).addGroup(layout.createSequentialGroup().addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(txtCurdebt, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED).addComponent(txtCurdate, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE).addComponent(jLabel10, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(m_jCategory1, GroupLayout.PREFERRED_SIZE, 290, GroupLayout.PREFERRED_SIZE).addGap(10).addComponent(jSearchCategory, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(txtMaxdebt, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE).addGap(27).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE).addComponent(label, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(lblOpenCredit, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.RELATED))).addGroup(layout.createParallelGroup(Alignment.LEADING, false).addComponent(passwordField).addComponent(m_iOpenCredit, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE).addComponent(txtScore))))).addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 594, GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel7).addComponent(m_jTaxID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel8).addComponent(m_jSearchkey, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(m_jVisible, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel3).addComponent(m_jName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel5).addComponent(jcard, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel9).addComponent(m_jCategory, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))).addComponent(jButton2).addComponent(jButton3)).addGroup(layout.createParallelGroup(Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(11).addComponent(jLabel10)).addGroup(layout.createSequentialGroup().addGap(3).addComponent(m_jCategory1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addGap(3).addComponent(jSearchCategory, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.TRAILING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel1).addComponent(txtMaxdebt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel2).addComponent(txtCurdebt, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel6).addComponent(txtCurdate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel).addComponent(txtScore, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(passwordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(label)).addPreferredGap(ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(lblOpenCredit).addComponent(m_iOpenCredit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))).addPreferredGap(ComponentPlacement.RELATED).addComponent(jTabbedPane1, GroupLayout.PREFERRED_SIZE, 240, GroupLayout.PREFERRED_SIZE).addGap(81)));
        this.setLayout(layout);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText("c" + StringUtils.getCardNumber());
            m_Dirty.setDirty(true);
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardremove"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText(null);
            m_Dirty.setDirty(true);
        }
    }

    private void jSearchCategoryActionPerformed(java.awt.event.ActionEvent evt) {
        JCategoryTreeDialog dailog = JCategoryTreeDialog.newJDialog(this);
        dailog.showDialog(m_CategoryModelb.getDatas(), (I_Category) m_CategoryModelb.getSelectedItem());
        if (dailog.isOK()) m_CategoryModelb.setSelectedItem(dailog.getSelected());
    }

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JButton jSearchCategory;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField jcard;

    private javax.swing.JComboBox m_jCategory;

    private javax.swing.JComboBox m_jCategory1;

    private javax.swing.JTextField m_jName;

    private javax.swing.JTextArea m_jNotes;

    private javax.swing.JTextField m_jSearchkey;

    private javax.swing.JTextField m_jTaxID;

    private javax.swing.JCheckBox m_jVisible;

    private javax.swing.JTextField txtAddress;

    private javax.swing.JTextField txtAddress2;

    private javax.swing.JTextField txtCity;

    private javax.swing.JTextField txtCountry;

    private javax.swing.JTextField txtCurdate;

    private javax.swing.JTextField txtCurdebt;

    private javax.swing.JTextField txtEmail;

    private javax.swing.JTextField txtFax;

    private javax.swing.JTextField txtFirstName;

    private javax.swing.JTextField txtLastName;

    private javax.swing.JTextField txtMaxdebt;

    private javax.swing.JTextField txtPhone;

    private javax.swing.JTextField txtPhone2;

    private javax.swing.JTextField txtPostal;

    private javax.swing.JTextField txtRegion;

    private JLabel label;

    private JPasswordField passwordField;

    private JTextField m_iOpenCredit;

    private JLabel lblNewLabel;

    private JTextField txtScore;

    @Override
    public Customer createNew(Properties prop) {
        Customer newObj = new Customer();
        String code = sdf.format(new Date());
        while (code.startsWith("0")) code = code.substring(1);
        newObj.setTaxid(code);
        newObj.setCreateDate(new Date());
        newObj.setScore(0.0);
        return newObj;
    }

    @Override
    public void rebuild(Customer customer) throws BasicException {
        customer.setTaxid(m_jTaxID.getText());
        customer.setSearchkey(m_jSearchkey.getText());
        customer.setName(m_jName.getText());
        customer.setNotes(m_jNotes.getText());
        customer.setVisible(Boolean.valueOf(m_jVisible.isSelected()));
        customer.setCard((String) Formats.STRING.parseValue(jcard.getText()));
        customer.setMaxdebt((Double) Formats.CURRENCY.parseValue(txtMaxdebt.getText(), new Double(0.0)));
        customer.setCurdate((Date) Formats.TIMESTAMP.parseValue(txtCurdate.getText()));
        customer.setCurdebt((Double) Formats.CURRENCY.parseValue(txtCurdebt.getText()));
        customer.setFirstname((String) Formats.STRING.parseValue(txtFirstName.getText()));
        customer.setLastname((String) Formats.STRING.parseValue(txtLastName.getText()));
        customer.setEmail((String) Formats.STRING.parseValue(txtEmail.getText()));
        customer.setPhone((String) Formats.STRING.parseValue(txtPhone.getText()));
        customer.setPhone2((String) Formats.STRING.parseValue(txtPhone2.getText()));
        customer.setFax((String) Formats.STRING.parseValue(txtFax.getText()));
        customer.setAddress((String) Formats.STRING.parseValue(txtAddress.getText()));
        customer.setAddress2((String) Formats.STRING.parseValue(txtAddress2.getText()));
        customer.setPostal((String) Formats.STRING.parseValue(txtPostal.getText()));
        customer.setCity((String) Formats.STRING.parseValue(txtCity.getText()));
        customer.setRegion((String) Formats.STRING.parseValue(txtRegion.getText()));
        customer.setCountry((String) Formats.STRING.parseValue(txtCountry.getText()));
        customer.setTaxCustCategoryID((String) m_CategoryModel.getSelectedKey());
        customer.setCategoryid((String) m_CategoryModelb.getSelectedKey());
        customer.setOpenCredit((Integer) Formats.INT.parseValue(m_iOpenCredit.getText(), new Integer(0)));
        customer.setScore((Double) Formats.CURRENCY.parseValue(txtScore.getText()));
    }

    @Override
    public void updateUI(boolean editAble, int state) {
        m_jTaxID.setText(obj.getTaxid());
        m_jSearchkey.setText(obj.getSearchkey());
        m_jName.setText(obj.getName());
        m_jNotes.setText(obj.getNotes());
        m_jVisible.setSelected(obj.isVisible());
        jcard.setText(obj.getCard());
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(obj.getMaxdebt()));
        txtCurdate.setText(Formats.DATE.formatValue(obj.getCurdate()));
        txtCurdebt.setText(Formats.CURRENCY.formatValue(obj.getCurdebt()));
        txtFirstName.setText(Formats.STRING.formatValue(obj.getFirstname()));
        txtLastName.setText(Formats.STRING.formatValue(obj.getLastname()));
        txtEmail.setText(Formats.STRING.formatValue(obj.getEmail()));
        txtPhone.setText(Formats.STRING.formatValue(obj.getPhone()));
        txtPhone2.setText(Formats.STRING.formatValue(obj.getPhone2()));
        txtFax.setText(Formats.STRING.formatValue(obj.getFax()));
        txtAddress.setText(Formats.STRING.formatValue(obj.getAddress()));
        txtAddress2.setText(Formats.STRING.formatValue(obj.getAddress2()));
        txtPostal.setText(Formats.STRING.formatValue(obj.getPostal()));
        txtCity.setText(Formats.STRING.formatValue(obj.getCity()));
        txtRegion.setText(Formats.STRING.formatValue(obj.getRegion()));
        txtCountry.setText(Formats.STRING.formatValue(obj.getCountry()));
        m_CategoryModel.setSelectedKey(obj.getTaxCustCategoryID());
        m_CategoryModelb.setSelectedKey(obj.getCategoryid());
        m_iOpenCredit.setText(Formats.INT.formatValue(obj.getOpenCredit()));
        txtScore.setText(Formats.CURRENCY.formatValue(obj.getScore()));
        m_jTaxID.setEnabled(editAble);
        m_jSearchkey.setEnabled(editAble);
        m_jName.setEnabled(editAble);
        m_jNotes.setEnabled(editAble);
        txtMaxdebt.setEnabled(editAble);
        txtCurdebt.setEnabled(editAble);
        txtCurdate.setEnabled(editAble);
        m_jVisible.setEnabled(editAble);
        jcard.setEnabled(editAble);
        txtFirstName.setEnabled(editAble);
        txtLastName.setEnabled(editAble);
        txtEmail.setEnabled(editAble);
        txtPhone.setEnabled(editAble);
        txtPhone2.setEnabled(editAble);
        txtFax.setEnabled(editAble);
        txtAddress.setEnabled(editAble);
        txtAddress2.setEnabled(editAble);
        txtPostal.setEnabled(editAble);
        txtCity.setEnabled(editAble);
        txtRegion.setEnabled(editAble);
        txtCountry.setEnabled(editAble);
        m_jCategory.setEnabled(editAble);
        m_jCategory1.setEnabled(editAble);
        jButton2.setEnabled(editAble);
        jButton3.setEnabled(editAble);
        m_jCategory1.setEnabled(editAble);
        jSearchCategory.setEnabled(editAble);
        m_iOpenCredit.setEditable(editAble);
    }

    @Override
    public Class getEditClass() {
        return Customer.class;
    }
}
