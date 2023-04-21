package org.digitall.common.cashflow.interfaces.costscentre;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import org.digitall.common.cashflow.classes.CostsCentre;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;

public class CCToolBar extends BasicPrimitivePanel {

    private CostsCentre costscentre;

    private TFInput tfAssignedAmount = new TFInput(DataTypes.DOUBLE, "InitialAmount", false);

    private TFInput tfSpentAmount = new TFInput(DataTypes.DOUBLE, "SpentAmount", false);

    private TFInput tfAvailableAmount = new TFInput(DataTypes.DOUBLE, "AvailableAmount", false);

    private TFInput tfSpentAmountP = new TFInput(DataTypes.DOUBLE, "%", false);

    private TFInput tfModifiedAmount = new TFInput(DataTypes.DOUBLE, "ModifiedAmount", false);

    public CCToolBar() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setSize(new Dimension(607, 48));
        this.setLayout(null);
        tfAssignedAmount.setBounds(new Rectangle(10, 5, 115, 35));
        tfAssignedAmount.setSize(new Dimension(115, 35));
        tfSpentAmount.setBounds(new Rectangle(155, 5, 115, 35));
        tfAvailableAmount.setBounds(new Rectangle(480, 5, 115, 35));
        tfSpentAmountP.setBounds(new Rectangle(275, 5, 60, 35));
        tfModifiedAmount.setBounds(new Rectangle(360, 5, 115, 35));
        this.add(tfModifiedAmount, null);
        this.add(tfSpentAmountP, null);
        this.add(tfAvailableAmount, null);
        this.add(tfSpentAmount, null);
        this.add(tfAssignedAmount, null);
        tfSpentAmount.setEnabled(false);
        tfSpentAmountP.setEnabled(false);
        tfAssignedAmount.setEnabled(false);
        tfAvailableAmount.setEnabled(false);
        tfModifiedAmount.setEnabled(false);
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
        super.setParentInternalFrame(_e);
        getParentInternalFrame().setClosable(true);
    }

    public void loadData() {
        if (costscentre != null) {
            tfAssignedAmount.setValue((costscentre.getInitialAmount()));
            tfAssignedAmount.setDisabledTextColor(Color.BLUE.darker().darker().darker());
            tfSpentAmountP.setValue((costscentre.getSpentAmountP()));
            tfSpentAmount.setValue((costscentre.getSpentAmount()));
            tfSpentAmount.setDisabledTextColor(Color.RED.darker().darker().darker());
            tfModifiedAmount.setValue((costscentre.getModifiedAmount()));
            tfAvailableAmount.setValue((costscentre.getAvailableAmount()));
            if (costscentre.getAvailableAmount() > 0) {
                tfAvailableAmount.setDisabledTextColor(new Color(30, 170, 30).darker().darker().darker());
            } else {
                tfAvailableAmount.setDisabledTextColor(Color.RED.darker().darker().darker());
            }
        }
    }

    public void setCostscentre(CostsCentre costscentre) {
        this.costscentre = costscentre;
        loadData();
    }
}
