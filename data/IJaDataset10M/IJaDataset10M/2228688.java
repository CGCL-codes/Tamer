package com.simpledata.bc.uicomponents.tools;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.simpledata.bc.BC;
import com.simpledata.bc.datamodel.money.Currency;
import com.simpledata.bc.datamodel.money.Money;

/**
 * A MoneyValue Input Synchronized on BC default Currency
 */
public abstract class MoneyValueInputSlave extends JPanel {

    MoneyValueInput mvi;

    JLabel currency;

    Money money;

    ChangeListener cl;

    public MoneyValueInputSlave(Money m) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        money = m;
        mvi = new MoneyValueInput(money) {

            public void editionStopped() {
                stopEdit();
            }

            public void editionStarted() {
                startEdit();
            }
        };
        currency = new JLabel("XXXX");
        currency.setPreferredSize(new Dimension(30, MoneyValueInput.DEF_H));
        add(mvi);
        add(currency);
        cl = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                refresh();
            }
        };
        BC.getCurrencyManager().addWeakCurrencyChangeListener(cl);
        refresh();
    }

    public void refresh() {
        currency.setText(Currency.getDefaultCurrency().toString());
        money.changeCurrency(Currency.getDefaultCurrency());
        mvi.refresh();
    }

    public void setEditable(boolean b) {
        mvi.setEditable(b);
    }

    public abstract void stopEdit();

    public abstract void startEdit();
}
