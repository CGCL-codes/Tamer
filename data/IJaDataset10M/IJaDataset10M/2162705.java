package com.openbravo.pos.inventory;

import java.awt.Component;
import com.openbravo.pos.base.AppLocal;
import com.openbravo.format.Formats;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.base.AppView;
import cn.ekuma.data.ui.swing.AbstractDAOJEditor;
import cn.ekuma.epos.datalogic.I_DataLogicSales;
import com.openbravo.bean.Tax;
import com.openbravo.bean.TaxCategory;
import com.openbravo.bean.TaxCustCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TaxEditor extends AbstractDAOJEditor<Tax> {

    private ComboBoxValModel taxcatmodel;

    private ComboBoxValModel taxcustcatmodel;

    private ComboBoxValModel taxparentmodel;

    I_DataLogicSales dlSales;

    /** Creates new form taxEditor */
    public TaxEditor(AppView app, DirtyManager dirty) {
        super(dirty);
        dlSales = (I_DataLogicSales) app.getBean("com.openbravo.pos.forms.I_DataLogicSales");
        initComponents();
        taxcatmodel = new ComboBoxValModel();
        taxcustcatmodel = new ComboBoxValModel();
        taxparentmodel = new ComboBoxValModel();
        m_jName.getDocument().addDocumentListener(dirty);
        m_jTaxCategory.addActionListener(dirty);
        m_jCustTaxCategory.addActionListener(dirty);
        m_jTaxParent.addActionListener(dirty);
        m_jRate.getDocument().addDocumentListener(dirty);
        jCascade.addActionListener(dirty);
        jOrder.getDocument().addDocumentListener(dirty);
        writeValueEOF();
    }

    public void activate() throws BasicException {
        List a = dlSales.list(TaxCategory.class);
        taxcatmodel = new ComboBoxValModel(a);
        m_jTaxCategory.setModel(taxcatmodel);
        a = dlSales.list(TaxCustCategory.class);
        a.add(0, null);
        taxcustcatmodel = new ComboBoxValModel(a);
        m_jCustTaxCategory.setModel(taxcustcatmodel);
    }

    public void refresh() {
        List a;
        try {
            a = dlSales.list(Tax.class);
        } catch (BasicException eD) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.cannotloadlists"), eD);
            msg.show(this);
            a = new ArrayList();
        }
        a.add(0, null);
        taxparentmodel = new ComboBoxValModel(a);
        m_jTaxParent.setModel(taxparentmodel);
    }

    public Component getComponent() {
        return this;
    }

    private void initComponents() {
        m_jName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        m_jRate = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jCascade = new javax.swing.JCheckBox();
        m_jTaxCategory = new javax.swing.JComboBox();
        m_jTaxParent = new javax.swing.JComboBox();
        m_jCustTaxCategory = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jOrder = new javax.swing.JTextField();
        setLayout(null);
        add(m_jName);
        m_jName.setBounds(240, 20, 200, 19);
        jLabel2.setText(AppLocal.getIntString("Label.Name"));
        add(jLabel2);
        jLabel2.setBounds(20, 20, 220, 15);
        jLabel3.setText(AppLocal.getIntString("label.dutyrate"));
        add(jLabel3);
        jLabel3.setBounds(20, 140, 220, 15);
        add(m_jRate);
        m_jRate.setBounds(240, 140, 60, 19);
        jLabel1.setText(AppLocal.getIntString("label.taxcategory"));
        add(jLabel1);
        jLabel1.setBounds(20, 50, 220, 15);
        jLabel4.setText(AppLocal.getIntString("label.custtaxcategory"));
        add(jLabel4);
        jLabel4.setBounds(20, 80, 220, 15);
        jLabel5.setText(AppLocal.getIntString("label.taxparent"));
        add(jLabel5);
        jLabel5.setBounds(20, 110, 220, 15);
        jCascade.setText(AppLocal.getIntString("label.cascade"));
        add(jCascade);
        jCascade.setBounds(320, 140, 110, 23);
        add(m_jTaxCategory);
        m_jTaxCategory.setBounds(240, 50, 200, 24);
        add(m_jTaxParent);
        m_jTaxParent.setBounds(240, 110, 200, 24);
        add(m_jCustTaxCategory);
        m_jCustTaxCategory.setBounds(240, 80, 200, 24);
        jLabel6.setText(AppLocal.getIntString("label.order"));
        add(jLabel6);
        jLabel6.setBounds(20, 170, 220, 15);
        add(jOrder);
        jOrder.setBounds(240, 170, 60, 19);
    }

    private javax.swing.JCheckBox jCascade;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JTextField jOrder;

    private javax.swing.JComboBox m_jCustTaxCategory;

    private javax.swing.JTextField m_jName;

    private javax.swing.JTextField m_jRate;

    private javax.swing.JComboBox m_jTaxCategory;

    private javax.swing.JComboBox m_jTaxParent;

    @Override
    public Tax createNew(Properties prop) {
        return new Tax();
    }

    @Override
    public void rebuild(Tax obj) throws BasicException {
        obj.setName(m_jName.getText());
        obj.setTaxCategoryID((String) taxcatmodel.getSelectedKey());
        obj.setTaxCustCategoryID((String) taxcustcatmodel.getSelectedKey());
        obj.setParentID((String) taxparentmodel.getSelectedKey());
        obj.setRate((Double) Formats.PERCENT.parseValue(m_jRate.getText()));
        obj.setCascade(jCascade.isSelected());
        obj.setOrder((Integer) Formats.INT.parseValue(jOrder.getText()));
    }

    @Override
    public void updateUI(boolean editAble, int state) {
        m_jName.setText(Formats.STRING.formatValue(obj.getName()));
        taxcatmodel.setSelectedKey(obj.getTaxCategoryID());
        taxcustcatmodel.setSelectedKey(obj.getTaxCustCategoryID());
        taxparentmodel.setSelectedKey(obj.getParentID());
        m_jRate.setText(Formats.PERCENT.formatValue(obj.getRate()));
        jCascade.setSelected(obj.isCascade());
        jOrder.setText(Formats.INT.formatValue(obj.getOrder()));
        m_jName.setEnabled(editAble);
        m_jTaxCategory.setEnabled(editAble);
        m_jCustTaxCategory.setEnabled(editAble);
        m_jTaxParent.setEnabled(editAble);
        m_jRate.setEnabled(editAble);
        jCascade.setEnabled(editAble);
        jOrder.setEnabled(editAble);
    }

    @Override
    public Class getEditClass() {
        return Tax.class;
    }
}
