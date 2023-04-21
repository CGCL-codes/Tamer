package com.google.code.p.keytooliui.ktl.swing.menuitem;

import java.awt.Component;
import javax.swing.ImageIcon;
import com.google.code.p.keytooliui.ktl.swing.panel.PTabHelpAppliAdvancedUI;
import com.google.code.p.keytooliui.shared.lang.MySystem;
import com.google.code.p.keytooliui.shared.swing.menuitem.MIHelpAboutAppliAbstract;

public final class MIHelpAboutAppliUIKtl extends MIHelpAboutAppliAbstract {

    public static final String f_s_strDlgBodyVersion = "2.5";

    public static String STR_DLG_BODY_SUFFIX = null;

    private static final String _F_STR_NAME_ICON = "aboutappli16.gif";

    static {
        String strWhere = "com.google.code.p.keytooliui.ktl.swing.menuitem.MIHelpAboutAppliUIKtl";
        try {
            String strBundleFileShort = com.google.code.p.keytooliui.ktl.AppMainUIAbs.F_STR_BUNDLE_DIR + ".MIHelpAboutAppliUIKtl";
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            MIHelpAboutAppliUIKtl.STR_DLG_BODY_SUFFIX = rbeResources.getString("dlgBodySuffix");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught");
        }
    }

    public MIHelpAboutAppliUIKtl(Component cmpFrameOwner) {
        super(cmpFrameOwner, MIHelpAboutAppliUIKtl.f_s_strDlgBodyVersion, MIHelpAboutAppliUIKtl.STR_DLG_BODY_SUFFIX, new PTabHelpAppliAdvancedUI());
    }

    @Override
    public boolean init() {
        String strMethod = "init()";
        if (!super.init()) {
            return false;
        }
        ImageIcon iin = com.google.code.p.keytooliui.shared.swing.imageicon.S_IINShared.s_get(MIHelpAboutAppliUIKtl._F_STR_NAME_ICON);
        if (iin == null) {
            MySystem.s_printOutError(this, strMethod, "nil iin");
            return false;
        }
        setIcon(iin);
        return true;
    }
}
