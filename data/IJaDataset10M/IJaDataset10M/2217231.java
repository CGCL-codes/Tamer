package com.loribel.commons.module.selector.gui;

import javax.swing.JLabel;
import com.loribel.commons.abstraction.GB_BooleanSelector;
import com.loribel.commons.abstraction.GB_BooleanSelectorSet;
import com.loribel.commons.abstraction.GB_Item;
import com.loribel.commons.abstraction.GB_Validable;
import com.loribel.commons.module.selector.GB_BooleanSelectorTools;
import com.loribel.commons.swing.GB_ComboBoxWithItems;
import com.loribel.commons.swing.GB_PanelCols;
import com.loribel.commons.swing.abstraction.GB_LabelRowWithData;
import com.loribel.commons.swing.impl.GB_LabelRowImplWithChange;

/**
 * LabelRow to edit GB_BooleanSelector.
 *
 * @author Gregory Borelli
 */
public class GB_BooleanSelectorRow extends GB_LabelRowImplWithChange implements GB_LabelRowWithData {

    private JLabel label;

    private GB_PanelCols mainComponentLine;

    private GB_BooleanSelectorSet selector;

    private MyCombo combo;

    private boolean editable;

    public GB_BooleanSelectorRow(String a_label, GB_BooleanSelectorSet a_selector, boolean a_editable) {
        super();
        selector = a_selector;
        editable = a_editable;
        buildLabelComponent(a_label);
        buildMainComponent(a_selector);
        setLabelComponent(label);
        setMainComponentLine(mainComponentLine);
        setHorizontalFill(true);
        setVerticalFill(false);
    }

    public GB_BooleanSelectorRow(String a_label, GB_BooleanSelectorSet a_selector) {
        this(a_label, a_selector, true);
    }

    private void buildLabelComponent(String a_label) {
        label = new JLabel(a_label + ": ");
    }

    private void buildMainComponent(GB_BooleanSelector a_selector) {
        mainComponentLine = new GB_PanelCols();
        combo = new MyCombo();
        mainComponentLine.addColFill(combo);
        register();
    }

    /**
     * Add listeners.
     */
    private void register() {
        changeSupport.registerSelectable(combo);
    }

    public String[] getErrorMessages() {
        return null;
    }

    public int getValidStatus() {
        return GB_Validable.VALID;
    }

    public boolean updateData() {
        int l_type = combo.getSelectedItemValueInt();
        selector.setType(l_type);
        return true;
    }

    public void updateFromData() {
        int l_type = selector.getType();
        combo.setSelectedItemValueInt(l_type);
    }

    /**
     * Inner class.
     */
    private class MyCombo extends GB_ComboBoxWithItems {

        MyCombo() {
            GB_Item[] l_items = GB_BooleanSelectorTools.getTypeItems();
            this.addItems(l_items);
            this.setSelectedItemValueInt(selector.getType());
            this.setPreferredSize(AA.DIM_COMBO_TYPE);
            this.setEnabled(editable);
        }
    }
}
