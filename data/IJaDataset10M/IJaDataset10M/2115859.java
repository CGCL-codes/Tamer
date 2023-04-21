package com.google.code.p.keytooliui.ktl.swing.menuitem;

import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.Locale;
import com.google.code.p.keytooliui.shared.lang.MySystem;
import com.google.code.p.keytooliui.shared.swing.menuitem.MIAbstract;

public final class MIViewCsrPkcs10 extends MIAbstract {

    public static String STR_TEXT = null;

    static {
        String strWhere = "com.google.code.p.keytooliui.ktl.swing.menuitem.MIViewCsrPkcs10";
        try {
            String strBundleFileShort = com.google.code.p.keytooliui.ktl.AppMainUIAbs.F_STR_BUNDLE_DIR + ".MIViewCsrPkcs10";
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            MIViewCsrPkcs10.STR_TEXT = rbeResources.getString("text");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught");
        }
    }

    public MIViewCsrPkcs10(ActionListener actListenerParent) {
        super(MIViewCsrPkcs10.STR_TEXT, actListenerParent);
    }
}
