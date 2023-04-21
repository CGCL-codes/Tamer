package com.openbravo.pos.panels;

import java.util.List;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import com.openbravo.bean.Product;
import com.openbravo.bean.crm.concerns.Concerns;
import com.openbravo.bean.crm.concerns.ProductConcerns;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.query.QBFParameter;
import com.openbravo.data.loader.query.QBFParameters;
import cn.ekuma.data.ui.swing.dnd.AbstractJBeanDropPanel;
import com.openbravo.pos.base.AppLocal;
import cn.ekuma.data.dao.I_DataLogic;
import cn.ekuma.epos.db.table.crm.concerns.I_ProductConcerns;

public class JDropProductByConcernsPanel extends JPanel {

    I_DataLogic dlSales;

    QBFParameters delQBFParameters;

    QBFParameter categoryParameter = new QBFParameter(I_ProductConcerns.CONCERNSID);

    QBFParameter productParameter = new QBFParameter(I_ProductConcerns.PRODUCTID);

    /**
	 * Create the panel.
	 */
    public JDropProductByConcernsPanel(final I_DataLogic dlSales2) {
        initComponents();
        this.dlSales = dlSales2;
        delQBFParameters = new QBFParameters();
        delQBFParameters.and(categoryParameter).and(productParameter);
        add(new AbstractJBeanDropPanel<Concerns, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                for (Product p : beans) {
                    ProductConcerns pConcerns = new ProductConcerns();
                    pConcerns.setConcernsId(bean.getID());
                    try {
                        dlSales2.insert(pConcerns);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                categoryParameter.equal(bean.getID());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(ProductConcerns.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Concerns obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.prodConcerns");
            }
        });
        add(new AbstractJBeanDropPanel<Concerns, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                for (Product p : beans) {
                    ProductConcerns pConcerns = new ProductConcerns();
                    pConcerns.setConcernsId(bean.getID());
                    try {
                        dlSales2.insert(pConcerns);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                categoryParameter.equal(bean.getID());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(ProductConcerns.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Concerns obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.prodConcerns");
            }
        });
        add(new AbstractJBeanDropPanel<Concerns, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                for (Product p : beans) {
                    ProductConcerns pConcerns = new ProductConcerns();
                    pConcerns.setConcernsId(bean.getID());
                    try {
                        dlSales2.insert(pConcerns);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                categoryParameter.equal(bean.getID());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(ProductConcerns.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Concerns obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.prodConcerns");
            }
        });
        add(new AbstractJBeanDropPanel<Concerns, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                for (Product p : beans) {
                    ProductConcerns pConcerns = new ProductConcerns();
                    pConcerns.setConcernsId(bean.getID());
                    try {
                        dlSales2.insert(pConcerns);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                categoryParameter.equal(bean.getID());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(ProductConcerns.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Concerns obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.prodConcerns");
            }
        });
        add(new AbstractJBeanDropPanel<Concerns, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                for (Product p : beans) {
                    ProductConcerns pConcerns = new ProductConcerns();
                    pConcerns.setConcernsId(bean.getID());
                    try {
                        dlSales2.insert(pConcerns);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                categoryParameter.equal(bean.getID());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(ProductConcerns.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Concerns obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.prodConcerns");
            }
        });
        add(new AbstractJBeanDropPanel<Concerns, Product>() {

            @Override
            public void doDropInsert(List<Product> beans) {
                for (Product p : beans) {
                    ProductConcerns pConcerns = new ProductConcerns();
                    pConcerns.setConcernsId(bean.getID());
                    try {
                        dlSales2.insert(pConcerns);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void doDropDelete(List<Product> dropList) {
                categoryParameter.equal(bean.getID());
                for (Product p : dropList) {
                    productParameter.equal(p.getID());
                    try {
                        dlSales.delete(ProductConcerns.class, delQBFParameters);
                    } catch (BasicException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String readerBean(Concerns obj) {
                return obj.getName();
            }

            @Override
            public String getDropTypeTitle() {
                return AppLocal.getIntString("label.prodConcerns");
            }
        });
    }

    private void initComponents() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    }

    public static String getTabName() {
        return AppLocal.getIntString("label.prodcategory");
    }
}
