package com.fh.auge.ui.sections.portfolio;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.fh.auge.internal.FontManager;
import com.fh.auge.internal.FormHelper;
import com.fh.auge.model.PositionGroup;
import com.fh.auge.ui.sections.NamedViewSection;

public class GeneralGroupSection extends NamedViewSection {

    private Label invested;

    private Label value;

    private Label gain;

    @Override
    protected void createSectionBody(Composite parent) {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        parent.setLayout(gridLayout1);
        invested = createField(parent, "Invested");
        value = createField(parent, "Value");
        value.setFont(FontManager.getInstance().getValueFont());
        gain = createField(parent, "Profit/Loss");
        gain.setFont(FontManager.getInstance().getGainFont());
    }

    @Override
    protected void inputChanged(Object input) {
        if (input == null || !(input instanceof PositionGroup)) {
            setEmpty();
        } else if (input instanceof PositionGroup) {
            PositionGroup i = (PositionGroup) input;
            setPositionGroup(i);
        }
    }

    private void setPositionGroup(PositionGroup position) {
        if (position.getSnapshot() == null) {
            setEmpty();
        } else {
            FormHelper.setMoney(invested, position.getSnapshot().getInvestedCapital());
            FormHelper.setMoney(value, position.getSnapshot().getValue());
            FormHelper.setGain(gain, position.getSnapshot().getInvestmentGain());
        }
    }

    private void setEmpty() {
        invested.setText("");
        value.setText("");
        gain.setText("");
    }

    @Override
    public String getName() {
        return "General";
    }

    public void dispose() {
    }

    private String toString(Object o) {
        return o == null ? "" : o.toString();
    }
}
