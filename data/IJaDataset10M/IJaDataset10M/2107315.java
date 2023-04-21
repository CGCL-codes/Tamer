package com.google.code.p.keytooliui.ktl.swing.internalframe;

import java.awt.Frame;
import com.google.code.p.keytooliui.ktl.swing.panel.*;

public class IFKstOpenIOShkIn extends IFAbs {

    public IFKstOpenIOShkIn(Frame frmParent) {
        super._pnl_ = new PTabUICmdKtlKstOpenIOShkIn(frmParent);
        setTitle(PTabUICmdKtlKstOpenIOShkIn.STR_TITLETASK);
    }
}
