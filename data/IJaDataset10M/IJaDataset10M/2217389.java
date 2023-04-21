package com.google.code.p.keytooliui.shared.swing.checkbox;

import com.google.code.p.keytooliui.shared.lang.*;
import javax.swing.*;
import java.awt.event.*;

public abstract class CBILoopAbs extends CBIcon {

    private static String _s_strTip = null;

    static {
        String strWhere = "com.google.code.p.keytooliui.shared.swing.checkbox.CBILoopAbs";
        String strBundleFileShort = com.google.code.p.keytooliui.shared.Shared.f_s_strBundleDir + ".CBILoopAbs";
        try {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            _s_strTip = rbeResources.getString("tip");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught");
        }
    }

    protected CBILoopAbs(ActionListener actListenerParent, ImageIcon iin) {
        super(actListenerParent, iin, _s_strTip);
    }
}
