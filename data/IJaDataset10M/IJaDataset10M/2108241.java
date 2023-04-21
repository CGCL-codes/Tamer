package com.ravana.product;

import com.ravana.purchasing.*;
import com.openbravo.basic.BasicException;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.ravana.gui.datagrid.BeanDataProvider;
import com.ravana.gui.datagrid.ColumnType;
import com.ravana.gui.datagrid.DataGrid;
import com.ravana.gui.datagrid.DataTable;
import com.ravana.gui.datagrid.DefaultColumnType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.KeyStroke;

/**
 *
 * @author  Manjuka Soysa
 */
public class ProductSearcher extends javax.swing.JPanel {

    SearchProductsFunctions functions;

    private DataGrid grid;

    private BeanDataProvider<ProductPurchasingInfo> provider;

    DataLogicProduct dlProduct;

    String supplier;

    public ProductSearcher(SearchProductsFunctions f, DataLogicProduct dlp) {
        initComponents();
        functions = f;
        dlProduct = dlp;
        String[] titles = { "Code", "Name", "Price" };
        ColumnType[] cTypes = { new DefaultColumnType(), new DefaultColumnType(), new DefaultColumnType() };
        int[] widths = { 100, 300, 100 };
        boolean[] editable = { false, false, false };
        grid = new DataGrid(titles, cTypes, widths, editable);
        String[] props = { "code", "name", "priceSell" };
        provider = new BeanDataProvider<ProductPurchasingInfo>(props);
        grid.setProvider(provider);
        DataTable table = grid.getTable();
        resultPane.setViewportView(table);
        KeyStroke enterKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        table.getInputMap().put(enterKeyStroke, "ENTER");
        ActionMap am = table.getActionMap();
        Action enterAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                process();
            }
        };
        am.put("ENTER", enterAction);
        searchField.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                searchField.selectAll();
            }
        });
    }

    public void clear() {
        ArrayList<ProductPurchasingInfo> prods = new ArrayList();
        provider.setValues(prods);
        grid.fireTableDataChanged();
    }

    public void init(String supplier) {
        this.supplier = supplier;
        searchField.requestFocus();
    }

    void process() {
        int selected = grid.getTable().getSelectedRow();
        if (selected < 0) {
            return;
        }
        ProductPurchasingInfo o = provider.getValues().get(selected);
        functions.selected(o);
    }

    void find() {
        String searchText = searchField.getText().trim();
        if (searchText.length() == 0) {
            return;
        }
        try {
            ProductPurchasingInfo prod = null;
            if (supplier == null) {
                prod = dlProduct.getProductPurchasingInfoByCode(searchText);
            } else {
                prod = dlProduct.getProductPurchasingInfoByCode(searchText, supplier);
            }
            if (prod != null) {
                List<ProductPurchasingInfo> prods = new ArrayList(0);
                prods.add(prod);
                provider.setValues(prods);
                functions.selected(prod);
            }
            List<ProductPurchasingInfo> prods = null;
            if (supplier == null) {
                prods = dlProduct.getProductPurchasingInfoByDescription(searchText);
            } else {
                prods = dlProduct.getProductPurchasingInfoByDescription(searchText, supplier);
            }
            provider.setValues(prods);
            if (prods.size() == 1) {
                functions.selected(prods.get(0));
            }
            grid.fireTableDataChanged();
            grid.getTable().selectFirst();
        } catch (BasicException ex) {
            Logger.getLogger(ProductSearcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void cancel() {
        functions.cancelled();
    }

    private void initComponents() {
        statusGroup = new javax.swing.ButtonGroup();
        timeGroup = new javax.swing.ButtonGroup();
        supplierPanel = new javax.swing.JPanel();
        findButton = new javax.swing.JButton();
        resultPane = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        selectButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        findButton.setText("Find");
        findButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout supplierPanelLayout = new javax.swing.GroupLayout(supplierPanel);
        supplierPanel.setLayout(supplierPanelLayout);
        supplierPanelLayout.setHorizontalGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(supplierPanelLayout.createSequentialGroup().addComponent(findButton).addContainerGap(82, Short.MAX_VALUE)));
        supplierPanelLayout.setVerticalGroup(supplierPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, supplierPanelLayout.createSequentialGroup().addContainerGap(21, Short.MAX_VALUE).addComponent(findButton).addContainerGap()));
        jLabel1.setText("Enter Product Name or Barcode");
        searchField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        selectButton.setText("Select");
        selectButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(resultPane, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(selectButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(supplierPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(selectButton).addComponent(cancelButton)))).addGap(18, 18, 18).addComponent(resultPane, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE).addContainerGap()));
    }

    private void findButtonActionPerformed(java.awt.event.ActionEvent evt) {
        find();
    }

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {
        process();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        cancel();
    }

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {
        find();
    }

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton findButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JScrollPane resultPane;

    private javax.swing.JTextField searchField;

    private javax.swing.JButton selectButton;

    private javax.swing.ButtonGroup statusGroup;

    private javax.swing.JPanel supplierPanel;

    private javax.swing.ButtonGroup timeGroup;
}
