package com.google.code.p.keytooliui.ktl.swing.panel;

import java.awt.Frame;
import java.awt.event.ItemListener;
import net.miginfocom.swing.MigLayout;

public abstract class PTabUICmdKtlKstOpenIOShkOutAbs extends PTabUICmdKtlKstOpenIOShkAbs {

    protected PTabUICmdKtlKstOpenIOShkOutAbs(Frame frmOwner, String strHelpID) {
        super(strHelpID, frmOwner, PSelBtnTfdFileSaveShk.f_s_strDocPropVal);
        super._pnlSelectFileData_ = new PSelBtnTfdFileSaveShk((javax.swing.event.DocumentListener) this, frmOwner, (ItemListener) null);
    }

    protected void _fillInPanelInput_() {
    }

    protected void _fillInPanelOutput_() {
        super._pnlOutput_.setLayout(new MigLayout("fill, wrap 1", "[left]"));
        super._pnlOutput_.add(this._pnlSelectFileData_);
    }
}
