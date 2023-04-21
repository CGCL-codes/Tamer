package com.memoire.bu;

import java.awt.Component;
import javax.swing.JList;

/**
 * A simple renderer for BuOptionItem.
 */
public final class BuOptionRenderer extends BuAbstractCellRenderer {

    public BuOptionRenderer() {
        super(BuAbstractCellRenderer.COMBOBOX);
    }

    public Component getListCellRendererComponent(JList _list, Object _value, int _index, boolean _selected, boolean _focus) {
        BuLabel r = (BuLabel) super.getListCellRendererComponent(_list, _value, _index, _selected, _focus);
        BuOptionItem option = (BuOptionItem) _value;
        if (option != null) {
            boolean e = (option.isEnabled() && _list.isEnabled());
            r.setText(option.getText());
            r.setEnabled(e);
            r.setIcon(option.getIcon());
            r.setDisabledIcon(option.getIcon());
            if (!e) r.setForeground(_list.getBackground().darker());
        } else {
            r.setText(BuResource.BU.getString("Erreur"));
            r.setEnabled(false);
            r.setIcon(null);
        }
        return r;
    }
}
