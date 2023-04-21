package de.ios.kontor.cl.order;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.math.*;
import de.ios.framework.gui.*;
import de.ios.framework.basic.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.main.co.*;
import de.ios.kontor.utils.*;

/**
 * View to enter price categories and links for articles - prices.
 * 
 * @author js (Joachim Schaaf)
 * @version $Id: PriceCategoryListView.java,v 1.1.1.1 2004/03/24 23:02:22 nanneb Exp $ 
 */
public class PriceCategoryListView extends KontorView {

    /** The name of this View */
    public static final String VIEW_NAME = "Pricecategory";

    protected PositionLayout PL = null;

    protected static int ATABLE_COLS = 8;

    protected static int ATABLE_ROWS = 10;

    protected static int ATABLE_WIDTH = 300;

    protected static String ATABLE_TITLE;

    protected static int CTABLE_COLS = 1;

    protected static int CTABLE_ROWS = 5;

    protected static int CTABLE_WIDTH = 300;

    protected static String CTABLE_TITLE;

    protected static int PTABLE_COLS = 3;

    protected static int PTABLE_ROWS = 5;

    protected static int PTABLE_WIDTH = 300;

    protected static String PTABLE_TITLE;

    protected Table articleTab = null;

    protected Table categoryTab = null;

    protected Table priceTab = null;

    protected Button articleB;

    protected Button newCatB;

    protected Button modCatB;

    protected Button delCatB;

    protected Button loadCatB;

    protected Button newPriceB;

    protected Button modPriceB;

    protected Button delPriceB;

    protected Button closeB;

    /**
   * constructor.
   *
   * @param session The Kontor session.
   */
    public PriceCategoryListView(KontorSession session) {
        this(session, null);
    }

    /**
   * constructor.
   *
   * @param session The Kontor session.
   */
    public PriceCategoryListView(KontorSession session, Properties p) {
        super(session, p);
    }

    /**
   * "init" Applet emulation
   */
    public void kvInit() {
        super.kvInit();
        createDialog();
    }

    /**
   * "start" Applet emulation
   */
    public void kvStart() {
        super.kvStart();
    }

    /**
   * "stop" Applet emulation
   */
    public void kvStop() {
        super.kvStop();
    }

    /**
   * "destroy" Applet emulation
   */
    public void kvDestroy() {
        super.kvDestroy();
    }

    /**
   * Build the dialog interface 
   */
    public void createDialog() {
        try {
            ATABLE_TITLE = getDesc("art_nr") + ": | " + getDesc("art_name") + ": | " + getDesc("price") + ": | " + getDesc("vat") + ": | " + getDesc("valid") + ": | " + getDesc("description") + ": | " + getDesc("cust_nr") + ": | " + getDesc("unit") + ": ";
            CTABLE_TITLE = getDesc("category") + ": ";
            PTABLE_TITLE = getDesc("category") + ": | " + getDesc("article") + ": | " + getDesc("price") + ": ";
            articleB = new Button(getDesc("update"));
            newCatB = new Button(getDesc("new"));
            modCatB = new Button(getDesc("modify_button"));
            delCatB = new Button(getDesc("delete"));
            loadCatB = new Button(getDesc("update"));
            newPriceB = new Button(getDesc("new"));
            modPriceB = new Button(getDesc("modify_button"));
            delPriceB = new Button(getDesc("delete"));
            closeB = new Button(getDesc("close"));
            articleTab = new Table(ATABLE_COLS, ATABLE_ROWS).setColumnTitles(ATABLE_TITLE).setMaxCharColumns(ATABLE_WIDTH);
            categoryTab = new Table(CTABLE_COLS, CTABLE_ROWS).setColumnTitles(CTABLE_TITLE).setMaxCharColumns(CTABLE_WIDTH);
            priceTab = new Table(PTABLE_COLS, PTABLE_ROWS).setColumnTitles(PTABLE_TITLE).setMaxCharColumns(PTABLE_WIDTH);
            setLayout(new PositionLayout(10, 10, 8, 2, false));
            PL = (PositionLayout) getLayout();
            add("", articleTab);
            add("", categoryTab);
            add("", priceTab);
            add("", articleB);
            add("", newCatB);
            add("", modCatB);
            add("", delCatB);
            add("", loadCatB);
            add("", newPriceB);
            add("", modPriceB);
            add("", delPriceB);
            add("", closeB);
            PL.define(articleTab).setHOrientation(PL.LEFT).setVOrientation(PL.UP).setVAutoResizing(50).fullHAutoExpand(true).nextBelowIs(articleB).nextBelowIs(categoryTab).setVAutoResizing(50).setHAutoResizing(50).setWidth(250).sameHSizeFor(priceTab).nextRightIs(priceTab).setVAutoResizing(50).setHAutoResizing(50).define(newCatB).isBelowOf(categoryTab).nextRightIs(modCatB).nextRightIs(delCatB).nextRightIs(loadCatB).define(newPriceB).isBelowOf(priceTab).nextRightIs(modPriceB).nextRightIs(delPriceB).define(closeB).isBelowOf(newCatB);
            ItemListener ilLoadPrices = new ItemListener() {

                public void itemStateChanged(ItemEvent ev) {
                    setBusy();
                    loadPrices();
                    setReady();
                }
            };
            priceTab.setNoOfSelectedItemsListener(new NoOfSelectedItemsListener() {

                public void noOfSelectedItemsChanged(int nSelected) {
                    modPriceB.setEnabled(nSelected == 1);
                    delPriceB.setEnabled(nSelected == 1);
                }
            });
            categoryTab.setNoOfSelectedItemsListener(new NoOfSelectedItemsListener() {

                public void noOfSelectedItemsChanged(int nSelected) {
                    modCatB.setEnabled(nSelected == 1);
                    delCatB.setEnabled(nSelected == 1);
                }
            });
            articleTab.addItemListener(ilLoadPrices);
            categoryTab.addItemListener(ilLoadPrices);
            articleB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    loadArticles();
                    loadPrices();
                }
            });
            newCatB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    newCategory();
                }
            });
            modCatB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    modCategory();
                }
            });
            delCatB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    delCategory();
                }
            });
            loadCatB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    loadCategories();
                    loadPrices();
                }
            });
            newPriceB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    newPrice();
                }
            });
            modPriceB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    modPrice();
                }
            });
            delPriceB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    delPrice();
                }
            });
            closeB.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    doDestroy();
                }
            });
            BigDecimalFormat formater = new BigDecimalFormat("###,###.00");
            articleTab.setColumnMode(2, Table.COLUMN_RIGHT);
            articleTab.setColumnMode(3, Table.COLUMN_RIGHT);
            priceTab.setColumnMode(2, Table.COLUMN_RIGHT);
            priceTab.setColumnMode(2, formater);
        } catch (Exception e) {
            showExceptionDialog(getDesc("error"), getDesc("err_dialog"), e, false);
        }
    }

    /**
   * Load all Articles.
   */
    public void loadArticles() {
        try {
            setBusy();
            articleTab.clear();
            VectorIterator it = new VectorIterator(getSession().getArticleTypeController().getArticleTypeDC(null, null, null));
            if (it != null) {
                it.setToStart();
                while (it.next()) ((ArticleTypeDC) it.getObject()).addTo(articleTab, 3);
            }
            setReady();
        } catch (Exception e) {
            showExceptionDialog(getDesc("error"), getDesc("err_load_art"), e, false);
            setReady();
        }
    }

    /**
   * New price category.
   */
    public void newCategory() {
        try {
            setBusy();
            new PriceCategoryView(getSession(), this).runWindow(null);
            setReady();
        } catch (Exception e) {
            showExceptionDialog(getDesc("error"), getDesc("err_dialog"), e, false);
        }
    }

    /**
   * Modify price category.
   */
    public void modCategory() {
        setBusy();
        try {
            int res = categoryTab.getSelectedIndex();
            if (res == -1) {
                new MessageDialog().alert1(getDesc("error"), getDesc("err_selected"), getDesc("close"));
            } else {
                PriceCategory pc = getSession().getPriceCategoryController().getPriceCategoryByOId(((Long) categoryTab.getItem(res)).longValue());
                new PriceCategoryView(getSession(), this, pc).runWindow(null);
            }
        } catch (Exception e) {
            setReady();
            showExceptionDialog(getDesc("error"), getDesc("err_access"), e, false);
        }
        setReady();
    }

    /**
   * Delete price category.
   */
    public void delCategory() {
        setBusy();
        int res = categoryTab.getSelectedIndex();
        if (res == -1) {
            new MessageDialog().alert1(getDesc("error"), getDesc("err_selected"), getDesc("close"));
        } else {
            if ((new MessageDialog().alert2(getDesc("del_pricec"), getDesc("del_pricec_msg"), getDesc("yes"), getDesc("no"))) == 1) {
                try {
                    PriceCategory pc = getSession().getPriceCategoryController().getPriceCategoryByOId(((Long) categoryTab.getItem(res)).longValue());
                    pc.delete();
                    loadCategories();
                    loadPrices();
                } catch (Exception e) {
                    showExceptionDialog(getDesc("error"), getDesc("err_del_pricec"), e, false);
                }
            }
        }
        setReady();
    }

    /**
   * Load all price categories.
   */
    public void loadCategories() {
        try {
            setBusy();
            categoryTab.clear();
            VectorIterator it = new VectorIterator(getSession().getPriceCategoryController().getPriceCategoryDC());
            if (it != null) {
                it.setToStart();
                while (it.next()) ((PriceCategoryDC) it.getObject()).addTo(categoryTab);
            }
            setReady();
        } catch (Exception e) {
            setReady();
            showExceptionDialog(getDesc("error"), getDesc("err_load_pricec"), e, false);
        }
    }

    /**
   * Load the special prices for the selected category and article.
   */
    public void loadPrices() {
        setBusy();
        Long categoryOId = (Long) categoryTab.getSelectedItem();
        Long articleOId = (Long) articleTab.getSelectedItem();
        priceTab.clear();
        if ((categoryOId != null) || (articleOId != null)) {
            try {
                VectorIterator it = new VectorIterator(getSession().getSpecialPriceController().getSpecialPriceDC(categoryOId, articleOId));
                if (it != null) {
                    it.setToStart();
                    while (it.next()) ((SpecialPriceDC) it.getObject()).addTo(priceTab);
                }
            } catch (Exception e) {
                setReady();
                showExceptionDialog(getDesc("error"), getDesc("err_load_prices"), e, false);
            }
        }
        setReady();
    }

    /**
   * New price.
   */
    public void newPrice() {
        if (articleTab.getSelectedCount() == 1) {
            try {
                setBusy();
                String article = articleTab.getColumn(articleTab.getSelectedIndex(), 1);
                new SpecialPriceView(getSession(), this, (Long) articleTab.getSelectedItem(), article, (Long) categoryTab.getSelectedItem()).runWindow(null);
                setReady();
            } catch (Exception e) {
                showExceptionDialog(getDesc("error"), getDesc("err_dialog"), e, false);
            }
        } else {
            showInfoDialog(getDesc("no_pricec_or_article"), true);
        }
    }

    /**
   * Modify price.
   */
    public void modPrice() {
        setBusy();
        try {
            SpecialPrice sp = getSession().getSpecialPriceController().getSpecialPriceByOId(((Long) priceTab.getSelectedItem()).longValue());
            new SpecialPriceView(getSession(), this, sp).runWindow(null);
        } catch (Exception e) {
            showExceptionDialog(getDesc("error"), getDesc("err_modify"), e, false);
        }
        setReady();
    }

    /**
   * Delete price.
   */
    public void delPrice() {
        setBusy();
        int row = priceTab.getSelectedIndex();
        String category = priceTab.getColumn(row, 0);
        String article = priceTab.getColumn(row, 1);
        if ((new MessageDialog().alert2(getDesc("del_sp"), getDesc("del_sp_msg1") + article + getDesc("del_sp_msg2") + category + getDesc("del_sp_msg3"), getDesc("yes"), getDesc("no"))) == 1) {
            try {
                getSession().getSpecialPriceController().getSpecialPriceByOId(((Long) priceTab.getSelectedItem()).longValue()).delete();
                loadPrices();
            } catch (Exception e) {
                showExceptionDialog(getDesc("error"), getDesc("err_del_sp"), e, false);
            }
        }
        setReady();
    }

    /**
   * Set the data-object for this view.
   */
    public void setData() {
    }

    /**
   * Get the data-object for this view.
   */
    public void getData() {
    }

    /**
   * @return the name of this View
   */
    public String getViewName() {
        return getViewName(getDesc("pricecategorylist_viewname"));
    }
}
