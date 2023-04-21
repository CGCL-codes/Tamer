package com.google.code.p.keytooliui.shared.swing.panel;

import com.google.code.p.keytooliui.shared.lang.*;
import javax.swing.*;
import java.awt.*;

public abstract class PSelCbxAbs extends PSelAbs {

    public void destroy() {
        super.destroy();
        this._cbx = null;
    }

    public boolean isSelectedItem() {
        if (this._cbx == null) return false;
        return this._cbx.isSelected();
    }

    public boolean init() {
        if (!super.init()) return false;
        _addItem();
        return true;
    }

    protected PSelCbxAbs(String strLabel) {
        super(strLabel, false);
        this._cbx = new JCheckBox();
    }

    private JCheckBox _cbx = null;

    private void _addItem() {
        Dimension dimWidthIcon = new Dimension(16 + 8, 16 + 8);
        super._pnl_.add(Box.createRigidArea(dimWidthIcon));
        super._pnl_.add(Box.createRigidArea(dimWidthIcon));
        super._pnl_.add(this._cbx);
    }
}
