package com.simpledata.bc.uicomponents.tools;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import com.simpledata.bc.datamodel.money.Currency;
import com.simpledata.bc.datamodel.money.Money;
import com.simpledata.bc.uitools.SNumField;

/**
 * A simple Panel that contains a MoneyValueInput and a CurrencyChooser
 */
public abstract class MoneyEditor extends JPanel {

    public static int DEF_W = 0;

    public static int DEF_H = 20;

    static {
        DEF_W = MoneyValueInput.DEF_W + CurrencyChooserCombo.DEF_W;
    }

    private MoneyValueInput mvi;

    private CurrencyChooserCombo ccc;

    /**
	 * set the dimensions of this component
	 * id one of the value is <= 0 then the default value is taken
	 */
    public void setDim(int height, int widthMoney, int widthCurrency) {
        Dimension d1 = new Dimension(widthMoney <= 0 ? MoneyValueInput.DEF_W : widthMoney, height <= 0 ? DEF_H : height);
        Dimension d2 = new Dimension(widthCurrency <= 0 ? CurrencyChooserCombo.DEF_W : widthCurrency, height <= 0 ? DEF_H : height);
        _setDim(mvi, d1);
        _setDim(ccc, d2);
    }

    private void _setDim(JComponent c, Dimension d) {
        c.setSize(d);
        c.setPreferredSize(d);
        c.setMinimumSize(d);
    }

    public MoneyEditor(Money money) {
        this(money, null);
    }

    public MoneyEditor(Money money, String text) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        mvi = new MoneyValueInput(money) {

            public void editionStopped() {
                stopEdit();
            }

            public void editionStarted() {
                startEdit();
            }
        };
        ccc = new CurrencyChooserCombo(money) {

            protected void valueChanged() {
                startEdit();
                stopEdit();
            }
        };
        setEditable(true);
        if (text != null) add(new JLabel(text));
        add(mvi);
        add(ccc);
    }

    boolean isEditable;

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean b) {
        if (isEditable == b) return;
        isEditable = b;
        if (mvi != null) mvi.setEditable(isEditable);
        if (ccc != null) ccc.setEnabled(isEditable);
    }

    /** reload data from model **/
    public void refresh() {
        mvi.refresh();
        ccc.refresh();
    }

    /** called when edition stopped **/
    public abstract void stopEdit();

    /** called when edition start **/
    public abstract void startEdit();

    /**
	 * @return
	 */
    public CurrencyChooserCombo getCcc() {
        return ccc;
    }

    /**
	 * @return
	 */
    public MoneyValueInput getMvi() {
        return mvi;
    }

    /**
	 * Tool that return a formated string of this money value
	 */
    public static String MoneyToSTring(Money m) {
        return SNumField.formatNumber(m.getValueDouble(), 2, true) + " " + m.getCurrency();
    }
}
