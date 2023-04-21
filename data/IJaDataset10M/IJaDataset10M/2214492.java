package com.google.code.p.keytooliui.ktl.swing.panel;

import com.google.code.p.keytooliui.ktl.util.jarsigner.*;
import com.google.code.p.keytooliui.ktl.io.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.panel.*;
import com.google.code.p.keytooliui.shared.swing.dialog.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public final class PTabUICmdKtlKstOpenKprExpKpr extends PTabUICmdKtlKstOpenKprExpAbs {

    public static final String STR_TITLETASK = "Export private key entry as private key & certificates chain files";

    private static String _s_strHelpID = null;

    private static String _s_strDlgInfoActionBodyBeg = null;

    private static String _s_strDlgInfoActionBodyKpr = null;

    private static String _s_strDlgInfoActionBodyQuery = null;

    static {
        String strWhere = "com.google.code.p.keytooliui.ktl.swing.panel.PTabUICmdKtlKstOpenKprExpKpr";
        String strBundleFileShort = com.google.code.p.keytooliui.ktl.AppMainUIAbs.F_STR_BUNDLE_DIR + ".PTabUICmdKtlKstOpenKprExpKpr";
        String strBundleFileLong = strBundleFileShort + ".properties";
        try {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            _s_strHelpID = "_out_pke_to_files_";
            _s_strDlgInfoActionBodyBeg = rbeResources.getString("dlgInfoActionBodyBeg");
            _s_strDlgInfoActionBodyKpr = rbeResources.getString("dlgInfoActionBodyKpr");
            _s_strDlgInfoActionBodyQuery = rbeResources.getString("dlgInfoActionBodyQuery");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught, " + strBundleFileLong + " not found");
        }
    }

    public void actionPerformed(ActionEvent evtAction) {
        String strMethod = "actionPerformed(evtAction)";
        String strFormatKst = ((PSelBtnTfdFileOpenKst) super._pnlSelectFileKst_).getSelectedFormatFile();
        if (strFormatKst == null) MySystem.s_printOutExit(this, strMethod, "nil strFormatKst");
        String strFormatFileKpr = ((PSelBtnTfdFileSaveKpr) super._pnlSelectFileKpr_).getSelectedFormatFile();
        String strFormatFileCrts = ((PSelBtnTfdFileSaveCrts) super._pnlSelectFileCrts_).getSelectedFormatFile();
        char[] chrsPasswdKst = null;
        if (super._strPasswdKst_ != null) chrsPasswdKst = super._strPasswdKst_.toCharArray(); else chrsPasswdKst = "".toCharArray();
        KTLKprOpenKprOutAbs ktl = null;
        if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprOpenKprOutJks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKst, super._strPathAbsFileKpr_, super._strPathAbsFileCrts_, strFormatFileKpr, strFormatFileCrts);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprOpenKprOutJceks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKst, super._strPathAbsFileKpr_, super._strPathAbsFileCrts_, strFormatFileKpr, strFormatFileCrts);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprOpenKprOutPkcs12(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKst, super._strPathAbsFileKpr_, super._strPathAbsFileCrts_, strFormatFileKpr, strFormatFileCrts);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprOpenKprOutBks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKst, super._strPathAbsFileKpr_, super._strPathAbsFileCrts_, strFormatFileKpr, strFormatFileCrts);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprOpenKprOutUber(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKst, super._strPathAbsFileKpr_, super._strPathAbsFileCrts_, strFormatFileKpr, strFormatFileCrts);
        } else {
            MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKst=" + strFormatKst);
        }
        if (ktl.doJob()) {
            MySystem.s_printOutTrace(this, strMethod, "OK!");
            if (!_queryPreviewResultsKpr(strFormatFileKpr)) MySystem.s_printOutExit(this, strMethod, "failed");
            if (!_queryPreviewResultsCrts(strFormatFileCrts)) MySystem.s_printOutExit(this, strMethod, "failed");
        } else MySystem.s_printOutTrace(this, strMethod, "either aborted by user or failed");
    }

    public PTabUICmdKtlKstOpenKprExpKpr(Frame frmOwner) {
        super(frmOwner, PTabUICmdKtlKstOpenKprExpKpr._s_strHelpID);
    }

    private boolean _queryPreviewResultsKpr(String strFormatFileKpr) {
        if (strFormatFileKpr.toLowerCase().compareTo(KTLAbs.f_s_strFormatFileKprDer.toLowerCase()) == 0) {
            String strDlgBody = "successfully exported private key entry:";
            strDlgBody += "\n" + " . private key file:" + super._strPathAbsFileKpr_;
            strDlgBody += "\n" + " . certificates chain file:" + super._strPathAbsFileCrts_;
            com.google.code.p.keytooliui.shared.swing.optionpane.OPAbstract.s_showDialogInfo(super._frmOwner_, strDlgBody);
            return true;
        }
        String strMethod = "_queryPreviewResults(strFormatFileKpr)";
        String strDlgBody = _s_strDlgInfoActionBodyBeg + "\n" + _s_strDlgInfoActionBodyKpr + "\n    " + super._strPathAbsFileKpr_ + "\n\n" + _s_strDlgInfoActionBodyQuery;
        if (!com.google.code.p.keytooliui.shared.swing.optionpane.OPAbstract.s_showConfirmDialog(super._frmOwner_, strDlgBody)) return true;
        java.io.File fle = new java.io.File(super._strPathAbsFileKpr_);
        if (!fle.exists()) {
            MySystem.s_printOutError(this, strMethod, "! fle.exists(), super._strPathAbsFileKpr_=" + super._strPathAbsFileKpr_);
            return false;
        }
        if (!fle.canRead()) {
            MySystem.s_printOutError(this, strMethod, "! fle.canRead(), super._strPathAbsFileKpr_=" + super._strPathAbsFileKpr_);
            return false;
        }
        if (strFormatFileKpr.toLowerCase().compareTo(KTLAbs.f_s_strFormatFileKprPem.toLowerCase()) == 0) {
            boolean blnGotIt = false;
            for (int i = 0; i < com.google.code.p.keytooliui.ktl.io.S_FileExtensionUI.f_s_strsKprPem.length; i++) {
                if (super._strPathAbsFileKpr_.toLowerCase().endsWith("." + com.google.code.p.keytooliui.ktl.io.S_FileExtensionUI.f_s_strsKprPem[i].toLowerCase())) {
                    blnGotIt = true;
                    break;
                }
            }
            if (!blnGotIt) {
                MySystem.s_printOutError(this, strMethod, "wrong file extension, super._strPathAbsFileKpr_=" + super._strPathAbsFileKpr_);
                return false;
            }
            UtilPemKeyPrivate.s_showFile(super._frmOwner_, fle);
        } else {
            MySystem.s_printOutExit(this, strMethod, "DEV ERROR: uncaught Kpr's file format, strFormatFileKpr=" + strFormatFileKpr);
        }
        return true;
    }

    private boolean _queryPreviewResultsCrts(String strFormatFileCrts) {
        if (strFormatFileCrts.toLowerCase().compareTo(KTLAbs.f_s_strFormatFileCrtDer.toLowerCase()) == 0) {
            return true;
        }
        String strMethod = "_queryPreviewResults(strFormatFileCrts)";
        String strDlgBody = "View saved certificates chain file?";
        if (!com.google.code.p.keytooliui.shared.swing.optionpane.OPAbstract.s_showConfirmDialog(super._frmOwner_, strDlgBody)) return true;
        java.io.File fle = new java.io.File(super._strPathAbsFileCrts_);
        if (!fle.exists()) {
            MySystem.s_printOutError(this, strMethod, "! fle.exists(), super._strPathAbsFileCrts_=" + super._strPathAbsFileCrts_);
            return false;
        }
        if (!fle.canRead()) {
            MySystem.s_printOutError(this, strMethod, "! fle.canRead(), super._strPathAbsFileCrts_=" + super._strPathAbsFileCrts_);
            return false;
        }
        if (strFormatFileCrts.toLowerCase().compareTo(KTLAbs.f_s_strFormatFileCrtPem.toLowerCase()) == 0) {
            boolean blnGotIt = false;
            for (int i = 0; i < com.google.code.p.keytooliui.ktl.io.S_FileExtensionUI.f_s_strsCrtX509Pem.length; i++) {
                if (super._strPathAbsFileCrts_.toLowerCase().endsWith("." + com.google.code.p.keytooliui.ktl.io.S_FileExtensionUI.f_s_strsCrtX509Pem[i].toLowerCase())) {
                    blnGotIt = true;
                    break;
                }
            }
            if (!blnGotIt) {
                MySystem.s_printOutError(this, strMethod, "wrong file extension, super._strPathAbsFileCrts_=" + super._strPathAbsFileCrts_);
                return false;
            }
            UtilCrtX509Pem.s_showFile(super._frmOwner_, fle);
        } else {
            MySystem.s_printOutExit(this, strMethod, "DEV ERROR: uncaught Crts' file format, strFormatFileCrts=" + strFormatFileCrts);
        }
        return true;
    }
}
