package com.google.code.p.keytooliui.shared.swing.panel;

import com.google.code.p.keytooliui.shared.lang.*;
import javax.swing.*;
import java.awt.*;

public abstract class PBarContainerPagerAbstract extends PBarContainerAbstract {

    public boolean init() {
        String strMethod = "init()";
        if (this._bpr_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._bpr_");
            return false;
        }
        if (!this._bpr_.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(this.getMinimumSize().width, PBarContainerAbstract._f_s_intH_));
        this.setPreferredSize(getMinimumSize());
        return true;
    }

    public void destroy() {
        if (this._bpr_ != null) {
            this._bpr_.destroy();
            this._bpr_ = null;
        }
    }

    public boolean setVisiblePager(boolean bln) {
        String strMethod = "setVisiblePager(bln)";
        if (this._bpr_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._bpr_");
            return false;
        }
        return this._bpr_.setVisibleIcon(bln);
    }

    public boolean setPageId(int intId) {
        String strMethod = "setPageId(intId)";
        if (this._bpr_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._bpr_");
            return false;
        }
        return this._bpr_.setPageId(intId);
    }

    public boolean setPageNb(int intNb) {
        String strMethod = "setPageNb(intNb)";
        if (this._bpr_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._bpr_");
            return false;
        }
        return this._bpr_.setPageNb(intNb);
    }

    protected PBarPagerAbstract _bpr_ = null;

    protected PBarContainerPagerAbstract() {
    }
}
