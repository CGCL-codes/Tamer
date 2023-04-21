package com.fh.auge.ui.sections.portfolio;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import com.fh.auge.internal.Age;
import com.fh.auge.internal.FontManager;
import com.fh.auge.internal.FormHelper;
import com.fh.auge.internal.FormatUtils;
import com.fh.auge.model.Position;
import com.fh.auge.ui.sections.NamedViewSection;

public class GeneralPositionSection extends NamedViewSection {

    private Label market;

    private Label shares;

    private Label invested;

    private Label holdSince;

    private Label stopLoss;

    private Label portfolioPercantage;

    private Label value;

    private Label gain;

    @Override
    protected void createSectionBody(Composite parent) {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        parent.setLayout(gridLayout1);
        market = createField(parent, "Market");
        shares = createField(parent, "Shares#");
        holdSince = createField(parent, "Hold");
        stopLoss = createField(parent, "StopLoss");
        portfolioPercantage = createField(parent, "% of Portfolio");
        System.err.println(">>>>>" + section.getForeground());
        invested = createField(parent, "Invested");
        value = createField(parent, "Value");
        value.setFont(FontManager.getInstance().getValueFont());
        gain = createField(parent, "Profit/Loss");
        gain.setFont(FontManager.getInstance().getGainFont());
    }

    @Override
    protected void inputChanged(Object input) {
        if (input == null || !(input instanceof Position)) {
            setEmpty();
        } else if (input instanceof Position) {
            Position i = (Position) input;
            setPosition(i);
        }
    }

    private void setEmpty() {
        market.setText("");
        shares.setText("");
        invested.setText("");
        holdSince.setText("");
        stopLoss.setText("");
        portfolioPercantage.setText("");
        value.setText("");
        gain.setText("");
    }

    protected void setPosition(Position position) {
        market.setText(position.getInvestment().getSecurity().getMarket().getName());
        shares.setText(FormatUtils.getInstance().getDoubleFormat().format(position.getInvestment().getSharesOwned()));
        FormHelper.setMoney(invested, position.getInvestment().getInvestedCapital());
        holdSince.setText(Age.create(position.getInvestment().getPurchaseDate()).toMaxUnits(2, "today"));
        if (position.getInvestmentSnapshot() != null) {
            FormHelper.setMoney(stopLoss, position.getInvestmentSnapshot().getStopLoss());
            FormHelper.setMoney(value, position.getInvestmentSnapshot().getValue());
            FormHelper.setGain(gain, position.getInvestmentSnapshot().getInvestmentGain());
        } else {
            value.setText("");
            gain.setText("");
            stopLoss.setText("");
        }
    }

    @Override
    public String getName() {
        return "General";
    }

    public void dispose() {
    }
}
