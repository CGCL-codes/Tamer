package org.digitall.common.cashflow.interfaces.costscentre;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.digitall.common.cashflow.classes.CostsCentre;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.basic.BasicPrimitivePanel;
import org.digitall.lib.components.basic.ExtendedInternalFrame;
import org.digitall.lib.components.buttons.AcceptButton;
import org.digitall.lib.components.buttons.CloseButton;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.data.Format;
import org.digitall.lib.sql.LibSQL;

public class CCGenerateBudgetMgmt extends BasicPrimitivePanel {

    private BasicPanel jPanel1 = new BasicPanel();

    private CloseButton btnClose = new CloseButton();

    private AcceptButton btnAccept = new AcceptButton();

    private TFInput tfName = new TFInput(DataTypes.STRING, "Name", false);

    private TFInput tfStartDate = new TFInput(DataTypes.DATE, "From", false);

    private TFInput tfEndDate = new TFInput(DataTypes.DATE, "To", false);

    private TFInput tfPercentage = new TFInput(DataTypes.PERCENT, "%", false);

    private TFInput tfDescription = new TFInput(DataTypes.STRING, "Description", false);

    private CBInput cbPeriod = new CBInput(CachedCombo.PERIODTYPES_LIST, "PeriodType");

    private CostsCentre costsCentre;

    private CCExpenditureAccountsTree parentTree;

    public CCGenerateBudgetMgmt() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setSize(new Dimension(429, 185));
        this.setPreferredSize(new Dimension(435, 185));
        jPanel1.setBounds(new Rectangle(5, 0, 425, 145));
        jPanel1.setLayout(null);
        btnClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnClose_actionPerformed(e);
            }
        });
        btnAccept.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                btnAccept_actionPerformed(e);
            }
        });
        tfName.setBounds(new Rectangle(15, 10, 325, 35));
        tfStartDate.setBounds(new Rectangle(220, 55, 90, 35));
        tfEndDate.setBounds(new Rectangle(320, 55, 90, 35));
        tfPercentage.setBounds(new Rectangle(350, 10, 60, 35));
        tfDescription.setBounds(new Rectangle(15, 100, 395, 35));
        cbPeriod.setBounds(new Rectangle(15, 55, 195, 35));
        this.addButton(btnClose);
        this.addButton(btnAccept);
        this.add(jPanel1, null);
        jPanel1.add(cbPeriod, null);
        jPanel1.add(tfPercentage, null);
        jPanel1.add(tfEndDate, null);
        jPanel1.add(tfStartDate, null);
        jPanel1.add(tfName, null);
        jPanel1.add(tfDescription, null);
        cbPeriod.autoSize();
    }

    public void setParentInternalFrame(ExtendedInternalFrame _e) {
        super.setParentInternalFrame(_e);
        getParentInternalFrame().setInfo("Se generar� una Nueva Partida cuando presione \"Aceptar\"");
    }

    private void btnClose_actionPerformed(ActionEvent e) {
        getParentInternalFrame().close();
    }

    private void btnAccept_actionPerformed(ActionEvent e) {
        Advisor.messageBox("En construcci�n", "Aviso");
    }

    public void setCostsCentre(CostsCentre costsCentre) {
        this.costsCentre = costsCentre;
    }

    public void setParentTree(CCExpenditureAccountsTree parentTree) {
        this.parentTree = parentTree;
    }
}
