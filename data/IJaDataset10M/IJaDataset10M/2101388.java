package com.google.code.p.keytooliui.ktl.swing.menubar;

import com.google.code.p.keytooliui.ktl.swing.menu.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.menubar.*;
import com.google.code.p.keytooliui.shared.swing.menu.*;
import javax.swing.Box;

public abstract class MBMainUIAbs extends MBMainAbstract {

    public void destroy() {
        super.destroy();
        if (this._menView_ != null) {
            this._menView_.destroy();
            this._menView_ = null;
        }
    }

    public boolean init() {
        String strMethod = "init()";
        if (!super.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (this._menView_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._menView_");
            return false;
        }
        if (!this._menView_.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (super._mimTools_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._mimTools_");
            return false;
        }
        if (super._menHelp_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._hamHelp_");
            return false;
        }
        add(this._menView_);
        if (super._pamPreference_ != null) add(super._pamPreference_);
        add(super._mimTools_);
        super.add(Box.createHorizontalGlue());
        super.add(Box.createVerticalGlue());
        add(super._menHelp_);
        return true;
    }

    public void setEnabledMenus(boolean bln) {
        super._faaFile_.setEnabled(bln);
        super._mimTools_.setEnabled(bln);
    }

    protected MViewAllMainAbs _menView_ = null;

    protected MBMainUIAbs(java.awt.Component cmpFrameOwner, java.awt.event.ActionListener actListenerParent) {
        super((String) null);
        super._faaFile_ = new MFileAllUI(actListenerParent);
        super._mimTools_ = new MToolAllMainUI((javax.swing.JFrame) cmpFrameOwner, actListenerParent);
    }
}
