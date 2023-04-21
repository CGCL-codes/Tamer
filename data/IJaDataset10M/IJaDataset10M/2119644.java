package com.google.code.p.keytooliui.shared.swing.menu;

import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.menuitem.*;
import java.awt.event.*;

public abstract class MFileAllAbstract extends MAbstract {

    private static String _s_strThisText = null;

    private static char _s_chrThisMnemo;

    static {
        String strWhere = "com.google.code.p.keytooliui.shared.swing.menu.MFileAllAbstract";
        String strBundleFileShort = com.google.code.p.keytooliui.shared.Shared.f_s_strBundleDir + ".MFileAllAbstract";
        String strBundleFileLong = strBundleFileShort + ".properties";
        try {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            _s_strThisText = rbeResources.getString("thisText");
            String strThisMnemo = rbeResources.getString("thisMnemo");
            _s_chrThisMnemo = strThisMnemo.trim().charAt(0);
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught");
        } catch (IndexOutOfBoundsException excIndexOutOfBounds) {
            excIndexOutOfBounds.printStackTrace();
            MySystem.s_printOutExit(strWhere, strBundleFileLong + ", excIndexOutOfBounds caught");
        }
        strBundleFileLong = null;
        strBundleFileShort = null;
        strWhere = null;
    }

    public abstract void updateTreeUI();

    public boolean init() {
        String strMethod = "init()";
        if (this._fet_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._fet_");
            return false;
        }
        if (!this._fet_.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        return true;
    }

    public void destroy() {
        if (this._fet_ != null) {
            this._fet_.destroy();
            this._fet_ = null;
        }
    }

    protected MIFileExit _fet_ = null;

    protected MFileAllAbstract(ActionListener actListenerParent) {
        setText(_s_strThisText);
        setMnemonic(_s_chrThisMnemo);
        this._fet_ = new MIFileExit(actListenerParent);
    }
}
