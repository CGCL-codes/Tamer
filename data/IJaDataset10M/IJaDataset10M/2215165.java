package com.fh.auge.ui.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import com.fh.auge.core.investment.ApplicationModel;
import com.fh.auge.core.trade.Trade;
import com.fh.auge.ui.properties.TradeComposite;

public class TradePage extends WizardPage {

    private ApplicationModel model;

    private Trade trade;

    private TradeComposite tradeComposite;

    protected TradePage(ApplicationModel model, Trade trade) {
        super("trade");
        this.model = model;
        this.trade = trade;
        setTitle("Trade");
        setDescription("Trade descprition");
    }

    public void createControl(Composite p) {
        tradeComposite = new TradeComposite(p, SWT.NONE, model, trade) {
        };
        setControl(tradeComposite);
    }

    public void updateModel() {
        tradeComposite.updateModel();
    }
}
