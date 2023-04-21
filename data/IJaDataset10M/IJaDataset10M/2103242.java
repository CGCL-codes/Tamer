package com.google.code.p.keytooliui.shared.swing.menu;

import com.google.code.p.keytooliui.shared.lang.*;

public abstract class MEditAllMainAbs extends MAbstract {

    private static String _s_strTextThis;

    private static char _s_chrMnemoThis;

    static {
        String strBundleFileShort = com.google.code.p.keytooliui.shared.Shared.f_s_strBundleDir + ".MEditAllMainAbs";
        String strWhere = "com.google.code.p.keytooliui.shared.swing.menu.MEditAllMainAbs";
        try {
            java.util.ResourceBundle _s_rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            _s_strTextThis = _s_rbeResources.getString("textThis");
            String strMnemoThis = _s_rbeResources.getString("mnemoThis");
            _s_chrMnemoThis = strMnemoThis.trim().charAt(0);
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught");
        } catch (IndexOutOfBoundsException excIndexOutOfBounds) {
            excIndexOutOfBounds.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excIndexOutOfBounds caught");
        }
    }

    public boolean init() {
        String strMethod = "init()";
        if (this._mep_ != null) {
            if (!this._mep_.init()) {
                MySystem.s_printOutError(this, strMethod, "failed");
                return false;
            }
            add(this._mep_);
        }
        return true;
    }

    public void destroy() {
        if (this._mep_ != null) {
            this._mep_.destroy();
            this._mep_ = null;
        }
    }

    protected MProjCurAbs _mep_ = null;

    protected MEditAllMainAbs() {
        setText(_s_strTextThis);
        setMnemonic(_s_chrMnemoThis);
    }
}
