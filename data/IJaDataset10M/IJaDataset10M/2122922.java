package com.google.code.p.keytooliui.shared.swing.menuitem;

import com.google.code.p.keytooliui.shared.lang.*;
import java.awt.event.*;
import java.net.URI;

public abstract class MIHelpOnlineAbstract extends MIAbstract {

    public static String STR_DLG_ERROR_BODY = null;

    static {
        java.util.ResourceBundle rbeResources;
        final String f_strBundleFileShort = com.google.code.p.keytooliui.shared.Shared.f_s_strBundleDir + ".MIHelpOnlineAbstract";
        final String f_strWhere = "com.google.code.p.keytooliui.shared.swing.menuitem.MIHelpOnlineAbstract";
        try {
            rbeResources = java.util.ResourceBundle.getBundle(f_strBundleFileShort, java.util.Locale.getDefault());
            STR_DLG_ERROR_BODY = rbeResources.getString("dlgErrorBody");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(f_strWhere, "excMissingResource caught");
        }
    }

    protected MIHelpOnlineAbstract(final java.awt.Component f_cmpFrameOwner, String strText, final String f_strUrl) {
        super(strText);
        this.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                String strMethod = "MIHelpOnlineAbstract().addActionListener()... actionPerfomed(evtAction)";
                try {
                    URI uri = new URI(f_strUrl);
                    java.awt.Desktop.getDesktop().browse(uri);
                } catch (Exception exc) {
                    exc.printStackTrace();
                    MySystem.s_printOutError(strMethod, "got exception, exc.get<message()=" + exc.getMessage());
                    String strBody = STR_DLG_ERROR_BODY + "\n" + f_strUrl;
                    com.google.code.p.keytooliui.shared.swing.optionpane.OPAbstract.s_showDialogError(f_cmpFrameOwner, strBody);
                }
            }
        });
    }
}
