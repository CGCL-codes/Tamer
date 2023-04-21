package cn.ekuma.epos.analysis.product;

import cn.ekuma.epos.analysis.situation.JFilterListPanel;
import cn.ekuma.epos.datalogic.I_DataLogicCustomers;
import cn.ekuma.epos.datalogic.I_DataLogicERP;
import cn.ekuma.epos.datalogic.I_DataLogicSales;
import com.openbravo.beans.JCalendarDialog;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValEntry;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.query.QBFCompareEnum;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.format.Formats;
import com.openbravo.pos.base.AppLocal;
import com.openbravo.bean.infobean.CustomerInfo;
import com.openbravo.bean.Location;
import com.openbravo.bean.erp.OrderType;
import com.openbravo.bean.Product;
import com.openbravo.bean.ProductCategory;
import com.openbravo.pos.bean.ProductCategoryExt;
import com.openbravo.bean.infobean.UserInfo;
import com.openbravo.pos.panels.JCategoryTreeDialog;
import com.openbravo.pos.panels.JCustomerFinder;
import com.openbravo.pos.panels.JObjectListFinder;
import com.openbravo.pos.panels.JProductFinder;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class JProductFilter extends javax.swing.JPanel implements EditorCreator {

    JFilterListPanel jOrderTypeFilter;

    JFilterListPanel jProdCateoryFilter;

    JFilterListPanel jProductFilter;

    JFilterListPanel jUserFilter;

    JFilterListPanel jCustomerFilter;

    I_DataLogicCustomers dlCustomers;

    I_DataLogicERP dlSales;

    /** Creates new form JERPFilter */
    public JProductFilter(final I_DataLogicCustomers dlCustomers, final I_DataLogicERP dlSales) {
        this.dlCustomers = dlCustomers;
        this.dlSales = dlSales;
        initComponents();
        jOrderTypeFilter = new JFilterListPanel() {

            @Override
            public ComboBoxValEntry add() {
                List allUser;
                try {
                    allUser = dlSales.list(OrderType.class);
                    JObjectListFinder userList = JObjectListFinder.newJDialog(this);
                    Object tempUser = userList.showUserList(allUser);
                    if (tempUser != null) {
                        return new ComboBoxValEntry(((OrderType) tempUser).getId(), ((OrderType) tempUser).getName());
                    }
                } catch (BasicException ex) {
                    new MessageInf(ex).show(this);
                }
                return null;
            }
        };
        jTabbedPane1.addTab(AppLocal.getIntString("label.ordertype"), jOrderTypeFilter);
        jProdCateoryFilter = new JFilterListPanel() {

            @Override
            public ComboBoxValEntry add() {
                try {
                    List prodCateorys = dlSales.list(ProductCategory.class);
                    JCategoryTreeDialog dailog = JCategoryTreeDialog.newJDialog(this);
                    dailog.showDialog(prodCateorys, null);
                    if (dailog.isOK()) {
                        ProductCategory select = (ProductCategory) dailog.getSelected();
                        return new ComboBoxValEntry(select.getID(), select.getName());
                    }
                } catch (BasicException ex) {
                    new MessageInf(ex).show(this);
                }
                return null;
            }
        };
        jTabbedPane1.addTab(AppLocal.getIntString("label.prodcategory"), jProdCateoryFilter);
        jProductFilter = new JFilterListPanel() {

            @Override
            public ComboBoxValEntry add() {
                Product findProduct = JProductFinder.showMessage(this, dlSales);
                if (findProduct != null) {
                    return new ComboBoxValEntry(findProduct.getID(), findProduct.getName());
                }
                return null;
            }
        };
        jTabbedPane1.addTab(AppLocal.getIntString("Menu.Products"), jProductFilter);
        jUserFilter = new JFilterListPanel() {

            @Override
            public ComboBoxValEntry add() {
                List allUser;
                try {
                    allUser = dlSales.listPeopleVisible();
                    JObjectListFinder userList = JObjectListFinder.newJDialog(this);
                    Object tempUser = userList.showUserList(allUser);
                    if (tempUser != null) {
                        return new ComboBoxValEntry(((UserInfo) tempUser).getId(), ((UserInfo) tempUser).getName());
                    }
                } catch (BasicException ex) {
                    new MessageInf(ex).show(this);
                }
                return null;
            }
        };
        jTabbedPane1.addTab(AppLocal.getIntString("Menu.Users"), jUserFilter);
        jCustomerFilter = new JFilterListPanel() {

            @Override
            public ComboBoxValEntry add() {
                JCustomerFinder customerFinder = JCustomerFinder.getCustomerFinder(this, dlCustomers, dlSales);
                customerFinder.setVisible(true);
                CustomerInfo selectCustomer = customerFinder.getSelectedCustomer();
                if (selectCustomer != null) {
                    return new ComboBoxValEntry(selectCustomer.getId(), selectCustomer.getName());
                }
                return null;
            }
        };
        jTabbedPane1.addTab(AppLocal.getIntString("label.customer"), jCustomerFilter);
        List locationList = null;
        try {
            locationList = dlSales.list(Location.class);
        } catch (BasicException ex) {
            new MessageInf(ex).show(this);
        }
        m_jLocation.setModel(new ComboBoxValModel(locationList));
        if (locationList.size() > 0) m_jLocation.setSelectedIndex(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jTxtStartDate = new javax.swing.JTextField();
        btnDateStart = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTxtEndDate = new javax.swing.JTextField();
        btnDateEnd = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        m_jLocation = new javax.swing.JComboBox();
        setLayout(new java.awt.BorderLayout());
        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
        jLabel3.setText(AppLocal.getIntString("Label.StartDate"));
        jTxtStartDate.setPreferredSize(new java.awt.Dimension(200, 25));
        btnDateStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png")));
        btnDateStart.setPreferredSize(new java.awt.Dimension(50, 25));
        btnDateStart.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateStartActionPerformed(evt);
            }
        });
        jLabel4.setText(AppLocal.getIntString("Label.EndDate"));
        jTxtEndDate.setPreferredSize(new java.awt.Dimension(200, 25));
        btnDateEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/date.png")));
        btnDateEnd.setPreferredSize(new java.awt.Dimension(50, 25));
        btnDateEnd.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDateEndActionPerformed(evt);
            }
        });
        jButton1.setText(AppLocal.getIntString("button.reset"));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(14, 14, 14).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addComponent(jTxtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1)).addGroup(jPanel7Layout.createSequentialGroup().addComponent(jTxtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(btnDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(m_jLocation, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))).addGap(86, 86, 86)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGap(22, 22, 22).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jLabel3).addComponent(jTxtStartDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jButton1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER).addComponent(jLabel4).addComponent(jTxtEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(btnDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(m_jLocation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(22, Short.MAX_VALUE)));
        add(jPanel7, java.awt.BorderLayout.SOUTH);
    }

    private void btnDateStartActionPerformed(java.awt.event.ActionEvent evt) {
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtStartDate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }

    private void btnDateEndActionPerformed(java.awt.event.ActionEvent evt) {
        Date date;
        try {
            date = (Date) Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
        } catch (BasicException e) {
            date = null;
        }
        date = JCalendarDialog.showCalendarTimeHours(this, date);
        if (date != null) {
            jTxtEndDate.setText(Formats.TIMESTAMP.formatValue(date));
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        jOrderTypeFilter.removeAll();
        jProdCateoryFilter.removeAll();
        jProductFilter.removeAll();
        jUserFilter.removeAll();
        jCustomerFilter.removeAll();
        jTxtStartDate.setText(null);
        jTxtEndDate.setText(null);
    }

    private javax.swing.JButton btnDateEnd;

    private javax.swing.JButton btnDateStart;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextField jTxtEndDate;

    private javax.swing.JTextField jTxtStartDate;

    private javax.swing.JComboBox m_jLocation;

    public Object createValue() throws BasicException {
        Object[] afilter = new Object[16];
        Object startdate = Formats.TIMESTAMP.parseValue(jTxtStartDate.getText());
        Object enddate = Formats.TIMESTAMP.parseValue(jTxtEndDate.getText());
        afilter[0] = (startdate == null) ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_GREATEROREQUALS;
        afilter[1] = startdate;
        afilter[2] = (enddate == null) ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_LESS;
        afilter[3] = enddate;
        if (jCustomerFilter.getSelectedObjs() != null) {
            afilter[4] = QBFCompareEnum.COMP_IN;
            afilter[5] = jCustomerFilter.getSelectedObjs();
        } else {
            afilter[4] = QBFCompareEnum.COMP_NONE;
            afilter[5] = null;
        }
        if (m_jLocation.getSelectedItem() != null) {
            afilter[6] = QBFCompareEnum.COMP_EQUALS;
            afilter[7] = ((Location) m_jLocation.getSelectedItem()).getID();
        } else {
            afilter[6] = QBFCompareEnum.COMP_NONE;
            afilter[7] = null;
        }
        if (jOrderTypeFilter.getSelectedObjs() != null) {
            afilter[8] = QBFCompareEnum.COMP_IN;
            afilter[9] = jOrderTypeFilter.getSelectedObjs();
        } else {
            afilter[8] = QBFCompareEnum.COMP_NONE;
            afilter[9] = null;
        }
        if (jUserFilter.getSelectedObjs() != null) {
            afilter[10] = QBFCompareEnum.COMP_IN;
            afilter[11] = jUserFilter.getSelectedObjs();
        } else {
            afilter[10] = QBFCompareEnum.COMP_NONE;
            afilter[11] = null;
        }
        if (jProductFilter.getSelectedObjs() != null) {
            afilter[12] = QBFCompareEnum.COMP_IN;
            afilter[13] = jProductFilter.getSelectedObjs();
        } else {
            afilter[12] = QBFCompareEnum.COMP_NONE;
            afilter[13] = null;
        }
        if (jProdCateoryFilter.getSelectedObjs() != null) {
            afilter[14] = QBFCompareEnum.COMP_IN;
            afilter[15] = jProdCateoryFilter.getSelectedObjs();
        } else {
            afilter[14] = QBFCompareEnum.COMP_NONE;
            afilter[15] = null;
        }
        return afilter;
    }
}
