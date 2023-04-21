package com.simpledata.bc.uicomponents.worksheet.workplace.tools;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.apache.log4j.Logger;
import com.simpledata.bc.components.worksheet.workplace.tools.RateBySlice;
import com.simpledata.bc.datamodel.money.Currency;
import com.simpledata.bc.tools.Lang;
import com.simpledata.bc.tools.OrderedMapOfDoublesObject;
import com.simpledata.bc.uicomponents.tools.CurrencyChooserCombo;

/**
 * Panel pour les rate by slice
 *
 * @see RateBySlice
 * @author Simpledata SARL, 2004, all rights reserved. 
 * @version $Id: RateBySlicePanel.java,v 1.2 2007/04/02 17:04:26 perki Exp $
 */
public class RateBySlicePanel extends AbstractBySlicePanel {

    private static final Logger m_log = Logger.getLogger(RateBySlicePanel.class);

    private JPanel jPanel;

    private JLabel currencyLabel;

    private CurrencyChooserCombo currencyChooser;

    /** the type chooser (marginal /effective)**/
    JComboBox typeChooser;

    /** columns **/
    int column[] = { RateBySliceCell.TYPE_KEY, RateBySliceCell.TYPE_MARGINAL_RATE, RateBySliceCell.TYPE_EFFECTIVE_RATE, RateBySliceCell.TYPE_FIXED };

    public final RateBySlice myRbs;

    /**
	 * This is the default constructor
	 * @param rbspl an object implementing RateBySlicePanel.RBSLPListener (can be null)
	 */
    public RateBySlicePanel(RateBySlice rbs, RBSPListener rbspl) {
        super(rbs, rbspl);
        this.myRbs = rbs;
        initialize();
    }

    public RateBySlice getMyRbs() {
        return myRbs;
    }

    /** return the columns type **/
    public int[] getColumnsTypes() {
        return column;
    }

    /** return the column name **/
    public String getTableColumnName(int c) {
        switch(getColumnType(c)) {
            case RateBySliceCell.TYPE_KEY:
                return Lang.translate("From");
            case RateBySliceCell.TYPE_MARGINAL_RATE:
                return Lang.translate("Marginal rate");
            case RateBySliceCell.TYPE_EFFECTIVE_RATE:
                return Lang.translate("Effective rate");
            case RateBySliceCell.TYPE_FIXED:
                return Lang.translate("Min. Fee");
        }
        return "??";
    }

    /** Create a new rateBySlice Cell from this Object **/
    protected final DataBySliceCell createRBSC(OrderedMapOfDoublesObject omodo, int type) {
        return RateBySliceCell.create(this, omodo, type);
    }

    /** update data **/
    public void load() {
        currencyChooser.setSelectedCurrency(myRbs.getCurrency());
    }

    /** save the data **/
    public void save() {
        myRbs.setCurrency(currencyChooser.getSelectedCurrency());
        if (jTable != null) jTable.revalidate();
        rbspl.rbsDataChanged();
    }

    /**
	 * This initialize the marginal / effective rate chooser
	 */
    private javax.swing.JComboBox getTypeChooser() {
        if (typeChooser == null) {
            String[] texts = new String[] { Lang.translate("Marginal rate applies"), Lang.translate("Effective rate applies") };
            typeChooser = new javax.swing.JComboBox(texts);
            int selectIndex = myRbs.getIsMarginalRate() ? 0 : 1;
            typeChooser.setSelectedIndex(selectIndex);
            typeChooser.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    myRbs.setIsMarginalRate(typeChooser.getSelectedIndex() == 0);
                    jTable.repaint();
                    rbspl.rbsDataChanged();
                }
            });
        }
        return typeChooser;
    }

    /**
	 * This method initializes currencyLabel
	 * 
	 * @return javax.swing.JLabel
	 */
    private javax.swing.JLabel getCurrencyLabel() {
        if (currencyLabel == null) {
            currencyLabel = new javax.swing.JLabel();
            currencyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            currencyLabel.setText(Lang.translate("Currency"));
            currencyLabel.setPreferredSize(new java.awt.Dimension(90, 20));
        }
        return currencyLabel;
    }

    /**
	 * This method initializes currencyChooser
	 * 
	 * @return javax.swing.JComboBox
	 */
    private javax.swing.JComboBox getCurrencyChooser() {
        if (currencyChooser == null) {
            final RateBySlicePanel tthis = this;
            currencyChooser = new CurrencyChooserCombo(myRbs.getCurrency()) {

                protected void valueChanged() {
                    tthis.save();
                }
            };
        }
        return currencyChooser;
    }

    /** change the editable properties of my components **/
    protected void _setEditable(boolean b) {
        getTypeChooser().setEnabled(b);
        getCurrencyChooser().setEnabled(b);
    }

    /**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
    protected javax.swing.JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new javax.swing.JPanel();
            java.awt.FlowLayout layFlowLayout1 = new java.awt.FlowLayout();
            layFlowLayout1.setAlignment(java.awt.FlowLayout.LEFT);
            jPanel.setLayout(layFlowLayout1);
            jPanel.add(getTypeChooser(), null);
            jPanel.add(getCurrencyLabel(), null);
            jPanel.add(getCurrencyChooser(), null);
            jPanel.add(getPlusButton(), null);
            jPanel.add(getDeleteButton(), null);
        }
        return jPanel;
    }
}
