package com.thornapple.ebay.manager.ui;

import binding.BindingContext;
import binding.BindingConverter;
import binding.BindingDescription;
import binding.swing.SwingBindingSupport;
import com.thornapple.ebay.manager.ItemSearchCriteria;

/**
 *
 * @author  Bill
 */
public class ItemSearchPanel extends javax.swing.JPanel {

    private ItemSearchCriteria criteria;

    private BindingContext context = new BindingContext();

    /** Creates new form ItemSearchPanel */
    public ItemSearchPanel() {
        initComponents();
        SwingBindingSupport.register();
    }

    /** Creates new form ItemSearchPanel */
    public ItemSearchPanel(ItemSearchCriteria criteria) {
        this.criteria = criteria;
        SwingBindingSupport.register();
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        cbSearchDescription = new javax.swing.JCheckBox();
        txtSearch = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbUseTotalPrice = new javax.swing.JCheckBox();
        txtMinPrice = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtMaxPrice = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMiles = new javax.swing.JTextField();
        txtZip = new javax.swing.JTextField();
        jLabel1.setText("Title");
        cbSearchDescription.setText("Search Description");
        cbSearchDescription.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbSearchDescription.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel2.setText("Price");
        cbUseTotalPrice.setText("Include Shipping");
        cbUseTotalPrice.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        cbUseTotalPrice.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jLabel3.setText("to");
        jLabel4.setText("Location");
        jLabel5.setText("miles from zip");
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(10, 10, 10).add(cbSearchDescription, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(10, 10, 10).add(cbUseTotalPrice)).add(layout.createSequentialGroup().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtMiles, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel5).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtZip, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)).add(layout.createSequentialGroup().add(jLabel2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtMinPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel3).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtMaxPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 63, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(layout.createSequentialGroup().add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(txtSearch, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel1).add(txtSearch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cbSearchDescription).add(16, 16, 16).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel2).add(jLabel3).add(txtMinPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(txtMaxPrice, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cbUseTotalPrice).add(22, 22, 22).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4).add(jLabel5).add(txtMiles, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(txtZip, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private javax.swing.JCheckBox cbSearchDescription;

    private javax.swing.JCheckBox cbUseTotalPrice;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JTextField txtMaxPrice;

    private javax.swing.JTextField txtMiles;

    private javax.swing.JTextField txtMinPrice;

    private javax.swing.JTextField txtSearch;

    private javax.swing.JTextField txtZip;

    public ItemSearchCriteria getCriteria() {
        return criteria;
    }

    public void setCriteria(ItemSearchCriteria criteria) {
        this.criteria = criteria;
        context.unbind();
        bindComponents();
    }

    private void bindComponents() {
        context.addDescription(new BindingDescription(criteria, "query", txtSearch, "text"));
        context.addDescription(new BindingDescription(criteria, "useDescription", cbSearchDescription, "selected"));
        BindingDescription maxDistanceDescription = new BindingDescription(criteria, "maximumDistance", txtMiles, "text");
        maxDistanceDescription.setConverter(new DistanceConverter());
        context.addDescription(maxDistanceDescription);
        context.addDescription(new BindingDescription(criteria, "zipCode", txtZip, "text"));
        BindingDescription minPriceDescription = new BindingDescription(criteria, "minimumPrice", txtMinPrice, "text");
        minPriceDescription.setConverter(new PriceConverter());
        context.addDescription(minPriceDescription);
        BindingDescription maxPriceDescription = new BindingDescription(criteria, "maximumPrice", txtMaxPrice, "text");
        maxPriceDescription.setConverter(new PriceConverter());
        context.addDescription(maxPriceDescription);
        context.bind();
    }

    public void commit() {
        context.commitUncommittedValues();
    }

    private static class PriceConverter extends BindingConverter {

        public Object convertToTarget(BindingDescription description, Object value) {
            if (((Double) value).doubleValue() == 0) return ""; else return value.toString();
        }

        public Object convertToSource(BindingDescription description, Object value) {
            if (value != null && ((String) value).length() > 0) return (new Double((String) value)).doubleValue(); else return 0;
        }
    }

    private static class DistanceConverter extends BindingConverter {

        public Object convertToTarget(BindingDescription description, Object value) {
            if (((Integer) value).intValue() == 0) return ""; else return value.toString();
        }

        public Object convertToSource(BindingDescription description, Object value) {
            if (value != null && ((String) value).length() > 0) return (new Integer((String) value)).intValue(); else return 0;
        }
    }
}
