package com.openbravo.pos.panels;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import cn.ekuma.data.dao.I_DataLogic;
import cn.ekuma.data.ui.swing.dnd.AbstractJBeanDropPanel;
import cn.ekuma.epos.db.table.crm.I_CustomerProduct;
import com.openbravo.bean.Customer;
import com.openbravo.bean.Product;
import com.openbravo.bean.crm.CustomerProduct;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.query.QBFParameter;
import com.openbravo.data.loader.query.QBFParameters;
import com.openbravo.pos.base.AppLocal;

public class JDropProductByCustomerPanel extends JPanel {

    I_DataLogic dlSales;

    QBFParameters delQBFParameters;

    QBFParameter productParameter = new QBFParameter(I_CustomerProduct.PRODUCT_ID);

    QBFParameter customerParameter = new QBFParameter(I_CustomerProduct.CUSTOMER_ID);

    /**
	 * Create the panel.
	 */
    public JDropProductByCustomerPanel(final I_DataLogic dlSales2) {
        initComponents();
        this.dlSales = dlSales2;
        delQBFParameters = new QBFParameters();
        delQBFParameters.and(productParameter).and(customerParameter);
        add(new AbstractJBeanDropPanel<Customer, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                CustomerProduct customerProduct;
                for (Product p : beans) {
                    try {
                        customerProduct = new CustomerProduct();
                        customerProduct.setCustomerId(bean.getId());
                        customerProduct.setProductId(p.getID());
                        dlSales.insert(customerProduct);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                customerParameter.equal(bean.getId());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(CustomerProduct.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Customer obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.productCustomerView");
            }
        });
        add(new AbstractJBeanDropPanel<Customer, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                CustomerProduct customerProduct;
                for (Product p : beans) {
                    try {
                        customerProduct = new CustomerProduct();
                        customerProduct.setCustomerId(bean.getId());
                        customerProduct.setProductId(p.getID());
                        dlSales.insert(customerProduct);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                customerParameter.equal(bean.getId());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(CustomerProduct.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Customer obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.productCustomerView");
            }
        });
        add(new AbstractJBeanDropPanel<Customer, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                CustomerProduct customerProduct;
                for (Product p : beans) {
                    try {
                        customerProduct = new CustomerProduct();
                        customerProduct.setCustomerId(bean.getId());
                        customerProduct.setProductId(p.getID());
                        dlSales.insert(customerProduct);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                customerParameter.equal(bean.getId());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(CustomerProduct.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Customer obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.productCustomerView");
            }
        });
        add(new AbstractJBeanDropPanel<Customer, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                CustomerProduct customerProduct;
                for (Product p : beans) {
                    try {
                        customerProduct = new CustomerProduct();
                        customerProduct.setCustomerId(bean.getId());
                        customerProduct.setProductId(p.getID());
                        dlSales.insert(customerProduct);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                customerParameter.equal(bean.getId());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(CustomerProduct.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Customer obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.productCustomerView");
            }
        });
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    public static String getTabName() {
        return AppLocal.getIntString("label.productCustomerView");
    }
}
