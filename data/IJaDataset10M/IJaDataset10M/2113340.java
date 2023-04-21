package com.google.code.p.keytooliui.shared.swing.menu;

import com.google.code.p.keytooliui.shared.lang.*;

public abstract class MPrefAllMainAbstract extends MAbstract {

    private static String _s_strText = null;

    private static char _s_chrThisMnemo;

    static {
        java.util.ResourceBundle rbeResources;
        final String f_strBundleFileShort = com.google.code.p.keytooliui.shared.Shared.f_s_strBundleDir + ".MPrefAllMainAbstract";
        final String f_strWhere = "com.google.code.p.keytooliui.shared.swing.menuitem.MPrefAllMainAbstract";
        try {
            rbeResources = java.util.ResourceBundle.getBundle(f_strBundleFileShort, java.util.Locale.getDefault());
            _s_strText = rbeResources.getString("text");
            String strThisMnemo = rbeResources.getString("mnemo");
            _s_chrThisMnemo = strThisMnemo.trim().charAt(0);
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(f_strWhere, "excMissingResource caught");
        } catch (IndexOutOfBoundsException excIndexOutOfBounds) {
            excIndexOutOfBounds.printStackTrace();
            MySystem.s_printOutExit(f_strWhere, "excIndexOutOfBounds caught");
        }
    }

    public boolean init() {
        String strMethod = "init()";
        if (this._pamPrefAppli_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._pamPrefAppli_");
            return false;
        }
        if (!this._pamPrefAppli_.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (this._mep_ != null) {
            if (!this._mep_.init()) {
                MySystem.s_printOutError(this, strMethod, "failed");
                return false;
            }
        }
        add(this._pamPrefAppli_);
        if (this._mep_ != null) add(this._mep_);
        return true;
    }

    public void destroy() {
        if (this._pamPrefAppli_ != null) {
            this._pamPrefAppli_.destroy();
            this._pamPrefAppli_ = null;
        }
        if (this._mep_ != null) {
            this._mep_.destroy();
            this._mep_ = null;
        }
    }

    protected MPrefAppliMainAbstract _pamPrefAppli_ = null;

    protected MProjCurAbs _mep_ = null;

    protected MPrefAllMainAbstract() {
        setText(_s_strText);
        setMnemonic(_s_chrThisMnemo);
    }
}
