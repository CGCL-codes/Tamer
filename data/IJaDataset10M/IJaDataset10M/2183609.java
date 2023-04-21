package jdiff.component;

import javax.swing.*;
import jdiff.component.ui.*;
import jdiff.DualDiff;

public class DiffGlobalPhysicalOverview extends DiffOverview {

    private static final String uiClassID = "DiffGlobalPhysicalOverviewUI";

    public DiffGlobalPhysicalOverview(DualDiff dualDiff) {
        super(dualDiff);
        updateUI();
    }

    public void setUI(DiffGlobalPhysicalOverviewUI ui) {
        super.setUI(ui);
    }

    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((DiffGlobalPhysicalOverviewUI) UIManager.getUI(this));
        } else {
            setUI(new BasicDiffGlobalPhysicalOverviewUI());
        }
    }

    public DiffGlobalPhysicalOverviewUI getUI() {
        return (DiffGlobalPhysicalOverviewUI) ui;
    }

    public String getUIClassID() {
        return uiClassID;
    }
}
