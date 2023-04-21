package com.google.code.p.keytooliui.ktl.swing.panel;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromAbs;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromBksToKPBks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromBksToKPJceks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromBksToKPJks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromBksToKPUber;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromBksToPkcs12;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJceksToKPBks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJceksToKPJceks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJceksToKPJks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJceksToKPUber;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJceksToPkcs12;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJksToKPBks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJksToKPJceks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJksToKPJks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJksToKPUber;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromJksToPkcs12;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromPkcs12ToKPBks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromPkcs12ToKPJceks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromPkcs12ToKPJks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromPkcs12ToKPUber;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromPkcs12ToPkcs12;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromUberToKPBks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromUberToKPJceks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromUberToKPJks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromUberToKPUber;
import com.google.code.p.keytooliui.ktl.util.jarsigner.KTLKprSaveFromUberToPkcs12;
import com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks;
import com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12;
import com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber;
import com.google.code.p.keytooliui.shared.lang.MySystem;
import com.google.code.p.keytooliui.shared.swing.button.BESPasswordAbs;
import com.google.code.p.keytooliui.shared.swing.panel.PSelAbs;
import com.google.code.p.keytooliui.shared.swing.panel.PSelBtnTfdAbs;
import net.miginfocom.swing.MigLayout;

public final class PTabUICmdKtlKstOpenKprFromKprKst extends PTabUICmdKtlKstOpenAbs {

    public static final String STR_TITLETASK = "Import private key entry from private key entry in other keystore";

    private static final String _f_s_strSuffixDocPropValSource = "_source";

    private static String _s_strHelpID = null;

    static {
        String strWhere = "com.google.code.p.keytooliui.ktl.swing.panel.PTabUICmdKtlKstOpenKprFromKprKst";
        String strBundleFileShort = com.google.code.p.keytooliui.ktl.AppMainUIAbs.F_STR_BUNDLE_DIR + ".PTabUICmdKtlKstOpenKprFromKprKst";
        String strBundleFileLong = strBundleFileShort + ".properties";
        try {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            PTabUICmdKtlKstOpenKprFromKprKst._s_strHelpID = "_in_pke_from_kst_";
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught, " + strBundleFileLong + " not found");
        }
    }

    public void destroy() {
        super.destroy();
        if (this._pnlSelectFileKstSource != null) {
            this._pnlSelectFileKstSource.destroy();
            this._pnlSelectFileKstSource = null;
        }
        if (this._pnlSelectPasswdKstSource != null) {
            this._pnlSelectPasswdKstSource.destroy();
            this._pnlSelectPasswdKstSource = null;
        }
    }

    public boolean init() {
        String strMethod = "init()";
        if (!super.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (this._pnlSelectFileKstSource == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._pnlSelectFileKstSource");
            return false;
        }
        if (!this._pnlSelectFileKstSource.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (this._pnlSelectPasswdKstSource == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._pnlSelectPasswdKstSource");
            return false;
        }
        if (!this._pnlSelectPasswdKstSource.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (!_alignLabelsPanelContents()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        return true;
    }

    public void insertUpdate(DocumentEvent evtDocument) {
        String strMethod = "insertUpdate(evtDocument)";
        Document doc = evtDocument.getDocument();
        if (doc == null) MySystem.s_printOutExit(this, strMethod, "nil doc");
        Object objPropVal = doc.getProperty(com.google.code.p.keytooliui.shared.swing.textfield.TFAbstract.f_s_strDocPropKey);
        if (objPropVal == null) MySystem.s_printOutExit(this, strMethod, "nil objPropVal");
        if (!(objPropVal instanceof String)) MySystem.s_printOutExit(this, strMethod, "! (objPropVal instanceof String)");
        String strPropVal = (String) objPropVal;
        if (strPropVal == null) MySystem.s_printOutExit(this, strMethod, "nil strPropVal");
        int intLength = doc.getLength();
        if (intLength == 0) MySystem.s_printOutExit(this, strMethod, "intLength == 0");
        String strText = null;
        try {
            strText = doc.getText(0, intLength);
        } catch (BadLocationException excBadLocation) {
            excBadLocation.printStackTrace();
            MySystem.s_printOutExit(this, strMethod, "excBadLocation caught");
        }
        if (strPropVal.compareTo(PSelBtnTfdFileOpenKst.f_s_strDocPropVal) == 0) {
            super._strPathAbsKst_ = strText;
            _updateActionButtonDataChanged_(true);
            return;
        }
        if (strPropVal.compareTo(PSelBtnTfdPasswdXlsKstAny.f_s_strDocPropVal) == 0) {
            super._strPasswdKst_ = strText;
            return;
        }
        if (strPropVal.compareTo(PSelBtnTfdFileOpenKst.f_s_strDocPropVal + PTabUICmdKtlKstOpenKprFromKprKst._f_s_strSuffixDocPropValSource) == 0) {
            this._strPathAbsKstSource = strText;
            _updateActionButtonDataChanged_(true);
            return;
        }
        if (strPropVal.compareTo(PSelBtnTfdPasswdXlsKstAny.f_s_strDocPropVal + PTabUICmdKtlKstOpenKprFromKprKst._f_s_strSuffixDocPropValSource) == 0) {
            this._strPasswdKstSource = strText;
            return;
        }
        MySystem.s_printOutExit(this, strMethod, "uncaught strPropVal, strPropVal=" + strPropVal);
    }

    public void removeUpdate(DocumentEvent evtDocument) {
        String strMethod = "removeUpdate(evtDocument)";
        Document doc = evtDocument.getDocument();
        if (doc == null) MySystem.s_printOutExit(this, strMethod, "nil doc");
        Object objPropVal = doc.getProperty(com.google.code.p.keytooliui.shared.swing.textfield.TFAbstract.f_s_strDocPropKey);
        if (objPropVal == null) MySystem.s_printOutExit(this, strMethod, "nil objPropVal");
        if (!(objPropVal instanceof String)) MySystem.s_printOutExit(this, strMethod, "! (objPropVal instanceof String)");
        String strPropVal = (String) objPropVal;
        if (strPropVal == null) MySystem.s_printOutExit(this, strMethod, "nil strPropVal");
        if (strPropVal.compareTo(PSelBtnTfdFileOpenKst.f_s_strDocPropVal) == 0) {
            super._strPathAbsKst_ = null;
            _updateActionButtonDataChanged_(false);
            return;
        }
        if (strPropVal.compareTo(PSelBtnTfdPasswdXlsKstAny.f_s_strDocPropVal) == 0) {
            super._strPasswdKst_ = null;
            return;
        }
        if (strPropVal.compareTo(PSelBtnTfdFileOpenKst.f_s_strDocPropVal + PTabUICmdKtlKstOpenKprFromKprKst._f_s_strSuffixDocPropValSource) == 0) {
            this._strPathAbsKstSource = null;
            _updateActionButtonDataChanged_(false);
            return;
        }
        if (strPropVal.compareTo(PSelBtnTfdPasswdXlsKstAny.f_s_strDocPropVal + PTabUICmdKtlKstOpenKprFromKprKst._f_s_strSuffixDocPropValSource) == 0) {
            this._strPasswdKstSource = null;
            return;
        }
        MySystem.s_printOutExit(this, strMethod, "uncaught strPropVal, strPropVal=" + strPropVal);
    }

    public void actionPerformed(ActionEvent evtAction) {
        String strMethod = "actionPerformed(evtAction)";
        if (super._strPathAbsKst_ == null) {
            MySystem.s_printOutExit(this, strMethod, "nil super._strPathAbsKst_");
            return;
        }
        if (this._strPathAbsKstSource == null) {
            MySystem.s_printOutExit(this, strMethod, "nil this._strPathAbsKstSource");
            return;
        }
        if (super._strPathAbsKst_.compareTo(this._strPathAbsKstSource) == 0) {
            MySystem.s_printOutWarning(this, strMethod, "same keystore files, this._strPathAbsKstSource=" + _strPathAbsKstSource);
            String strBody = "Not allowed: same keystore source and target.";
            strBody += "\n";
            strBody += "  " + _strPathAbsKstSource;
            strBody += "\n\n";
            strBody += "Please change one keystore file.";
            com.google.code.p.keytooliui.shared.swing.optionpane.OPAbstract.s_showDialogWarning(super._frmOwner_, strBody);
            return;
        }
        String strFormatKstSource = ((PSelBtnTfdFileOpenKst) this._pnlSelectFileKstSource).getSelectedFormatFile();
        if (strFormatKstSource == null) MySystem.s_printOutExit(this, strMethod, "nil strFormatKstSource");
        String strFormatKstTarget = ((PSelBtnTfdFileOpenKst) super._pnlSelectFileKst_).getSelectedFormatFile();
        if (strFormatKstTarget == null) MySystem.s_printOutExit(this, strMethod, "nil strFormatKstTarget");
        char[] chrsPasswdKstTarget = null;
        if (super._strPasswdKst_ != null) chrsPasswdKstTarget = super._strPasswdKst_.toCharArray(); else chrsPasswdKstTarget = "".toCharArray();
        char[] chrsPasswdKstSource = null;
        if (this._strPasswdKstSource != null) chrsPasswdKstSource = this._strPasswdKstSource.toCharArray(); else chrsPasswdKstSource = "".toCharArray();
        KTLKprSaveFromAbs ktl = null;
        if (strFormatKstSource.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
            if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJksToKPJks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJksToKPJceks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJksToPkcs12(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJksToKPBks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJksToKPUber(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else {
                MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKstTarget=" + strFormatKstTarget);
            }
        } else if (strFormatKstSource.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
            if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJceksToKPJks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJceksToKPJceks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJceksToPkcs12(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJceksToKPBks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromJceksToKPUber(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else {
                MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKstTarget=" + strFormatKstTarget);
            }
        } else if (strFormatKstSource.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
            if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromBksToKPJks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromBksToKPJceks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromBksToPkcs12(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromBksToKPBks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromBksToKPUber(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else {
                MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKstTarget=" + strFormatKstTarget);
            }
        } else if (strFormatKstSource.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
            if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromUberToKPJks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromUberToKPJceks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromUberToPkcs12(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromUberToKPBks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromUberToKPUber(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else {
                MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKstTarget=" + strFormatKstTarget);
            }
        } else if (strFormatKstSource.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
            if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromPkcs12ToKPJks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromPkcs12ToKPJceks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromPkcs12ToPkcs12(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromPkcs12ToKPBks(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
                ktl = new KTLKprSaveFromPkcs12ToKPUber(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, this._strPathAbsKstSource, chrsPasswdKstSource);
            } else {
                MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKstTarget=" + strFormatKstTarget);
            }
        } else {
            MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKstSource=" + strFormatKstSource);
        }
        if (ktl.doJob()) {
            MySystem.s_printOutTrace(this, strMethod, "OK!");
            this._doneJob(strFormatKstTarget);
        } else MySystem.s_printOutTrace(this, strMethod, "either aborted by user or failed");
    }

    public PTabUICmdKtlKstOpenKprFromKprKst(Frame frmOwner) {
        super(PTabUICmdKtlKstOpenKprFromKprKst._s_strHelpID, frmOwner, true, true, true, true, true);
        this._pnlSelectFileKstSource = new PSelBtnTfdFileOpenKst((javax.swing.event.DocumentListener) this, frmOwner, (java.awt.event.ItemListener) null, true, true, true, true, true, true, PTabUICmdKtlKstOpenKprFromKprKst._f_s_strSuffixDocPropValSource);
        this._pnlSelectPasswdKstSource = new PSelBtnTfdPasswdXlsKstAny((javax.swing.event.DocumentListener) this, frmOwner, BESPasswordAbs.f_s_intModeOpen, PTabUICmdKtlKstOpenKprFromKprKst._f_s_strSuffixDocPropValSource);
    }

    protected void _updateActionButtonDataChanged_(boolean blnFieldInserted) {
        if (blnFieldInserted) {
            if (super._btnAction_.isEnabled()) return;
            if (super._strPathAbsKst_ == null) return;
            if (this._strPathAbsKstSource == null) return;
            super._btnAction_.setEnabled(true);
            return;
        } else {
            if (!super._btnAction_.isEnabled()) return;
            if (super._strPathAbsKst_ == null) {
                super._btnAction_.setEnabled(false);
                return;
            }
            if (this._strPathAbsKstSource == null) {
                super._btnAction_.setEnabled(false);
                return;
            }
            return;
        }
    }

    protected void _fillInPanelInput_() {
        super._pnlInput_.setLayout(new MigLayout("fill, wrap 1", "[left]"));
        super._pnlInput_.add(this._pnlSelectFileKstSource);
        super._pnlInput_.add(this._pnlSelectPasswdKstSource);
    }

    protected void _fillInPanelOutput_() {
        super._fillInPanelKst_(super._pnlOutput_);
    }

    private PSelBtnTfdAbs _pnlSelectFileKstSource = null;

    private PSelBtnTfdAbs _pnlSelectPasswdKstSource = null;

    private String _strPathAbsKstSource = null;

    private String _strPasswdKstSource = null;

    private boolean _alignLabelsPanelContents() {
        String strMethod = "_alignLabelsPanelContents()";
        java.util.Vector<PSelAbs> vecPanel = new java.util.Vector<PSelAbs>();
        vecPanel.add(this._pnlSelectFileKstSource);
        vecPanel.add(this._pnlSelectPasswdKstSource);
        vecPanel.add(super._pnlSelectFileKst_);
        vecPanel.add(super._pnlSelectPasswdKst_);
        if (!PSelAbs.s_alignLabels(vecPanel)) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        vecPanel.clear();
        vecPanel = null;
        return true;
    }

    private void _doneJob(String strFormatKstTarget) {
        String strDlgBody = "Successfully imported keypair." + "\n" + "Imported private key entry stored in keystore:" + "\n    " + super._strPathAbsKst_ + "\n\n" + "View keystore?";
        if (!com.google.code.p.keytooliui.shared.swing.optionpane.OPAbstract.s_showConfirmDialog(super._frmOwner_, strDlgBody)) return;
        if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
            UtilKstJks.s_showFile(super._frmOwner_, super._strPathAbsKst_);
            return;
        }
        if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
            UtilKstJceks.s_showFile(super._frmOwner_, super._strPathAbsKst_);
            return;
        }
        if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
            UtilKstPkcs12.s_showFile(super._frmOwner_, super._strPathAbsKst_, super._strPasswdKst_.toCharArray());
            return;
        }
        if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
            UtilKstBks.s_showFile(super._frmOwner_, super._strPathAbsKst_);
            return;
        }
        if (strFormatKstTarget.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
            UtilKstUber.s_showFile(super._frmOwner_, super._strPathAbsKst_, super._strPasswdKst_.toCharArray());
            return;
        }
    }
}
