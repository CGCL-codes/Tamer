package com.fh.auge.ui.sections.portfolio;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import com.fh.auge.core.AbstractSnapshot;
import com.fh.auge.internal.FormHelper;
import com.fh.auge.model.Position;
import com.fh.auge.model.PositionGroup;
import com.fh.auge.ui.sections.NamedViewSection;

public class PerformanceSection extends NamedViewSection {

    private Label gain1d;

    private Label gain5d;

    private Label gain1m;

    private Label gain3m;

    private Label gain6m;

    private Label gain1y;

    @Override
    protected void createSectionBody(Composite parent) {
        GridLayout gridLayout1 = new GridLayout();
        gridLayout1.numColumns = 2;
        parent.setLayout(gridLayout1);
        gain1d = createField(parent, "1 day");
        gain5d = createField(parent, "5 days");
        gain1m = createField(parent, "1 month");
        gain3m = createField(parent, "3 months");
        gain6m = createField(parent, "6 months");
        gain1y = createField(parent, "1 year");
    }

    @Override
    protected void inputChanged(Object input) {
        if (input == null) {
            setEmpty();
        } else {
            if (input instanceof PositionGroup) {
                PositionGroup i = (PositionGroup) input;
                setPositionGroup(i.getSnapshot());
            } else if (input instanceof Position) {
                Position i = (Position) input;
                setPositionGroup(i.getInvestmentSnapshot());
            } else {
                setEmpty();
            }
        }
    }

    private void setPositionGroup(AbstractSnapshot snapshot) {
        if (snapshot == null) {
            setEmpty();
        } else {
            FormHelper.setGain(gain1d, snapshot.getGain1d());
            FormHelper.setGain(gain5d, snapshot.getGain5d());
            FormHelper.setGain(gain1m, snapshot.getGain1m());
            FormHelper.setGain(gain3m, snapshot.getGain3m());
            FormHelper.setGain(gain6m, snapshot.getGain6m());
            FormHelper.setGain(gain1y, snapshot.getGain1y());
        }
    }

    private void setEmpty() {
        gain1d.setText("");
        gain5d.setText("");
        gain1m.setText("");
        gain3m.setText("");
        gain6m.setText("");
        gain1y.setText("");
    }

    @Override
    public String getName() {
        return "Profit/Loss";
    }

    public void dispose() {
    }
}
