package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import com.google.code.p.keytooliui.shared.swing.dialog.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.awt.*;
import java.io.*;

public final class KTLKstOpenManChgPasswd extends KTLKstOpenAbs {

    public KeyStore getKstOpen() {
        return this._kstOpen;
    }

    public char[] getPasswordNew() {
        return this._chrsPasswdNew;
    }

    /**
        giving a keystore
        change its password
        
    **/
    public boolean doJob() {
        String strMethod = "doJob()";
        if (super._chrsPasswdKst_ == null) {
            MySystem.s_printOutExit(this, strMethod, "nil super._chrsPasswdKst_");
        }
        if (this._kstOpen == null) {
            MySystem.s_printOutExit(this, strMethod, "nil this._kstOpen");
        }
        char[] chrsPasswdNew = _getPasswordKstNew();
        if (chrsPasswdNew == null) {
            MySystem.s_printOutTrace(this, strMethod, "nil chrsPasswdNew, user canceled");
            return false;
        }
        String strOld = new String(super._chrsPasswdKst_);
        String strNew = new String(chrsPasswdNew);
        if (strOld.compareTo(strNew) == 0) {
            MySystem.s_printOutWarning(this, strMethod, "strOld.compareTo(strNew) == 0");
            String strBody = "Same keystore's password.";
            OPAbstract.s_showDialogWarning(super._frmOwner_, strBody);
            return false;
        }
        strOld = null;
        strNew = null;
        File fleOpenKstJks = UtilJsrFile.s_getFileOpen(super._frmOwner_, super._strPathAbsKst_);
        if (fleOpenKstJks == null) {
            MySystem.s_printOutError(this, strMethod, "nil fleOpenKstJks");
            return false;
        }
        if (!super._saveKeyStore_(this._kstOpen, fleOpenKstJks, chrsPasswdNew)) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        this._chrsPasswdNew = chrsPasswdNew;
        return true;
    }

    public KTLKstOpenManChgPasswd(Frame frmOwner, String strPathAbs, char[] chrsPasswdOpenKst, String strProvider, KeyStore kstOpen) {
        super(frmOwner, strPathAbs, chrsPasswdOpenKst, strProvider);
        this._kstOpen = kstOpen;
    }

    private KeyStore _kstOpen = null;

    private char[] _chrsPasswdNew = null;

    private char[] _getPasswordKstNew() {
        String strMethod = "_getPasswordKstNew()";
        DPasswordAbs dlg = new DPasswordConfirmSave(super._frmOwner_);
        if (!dlg.init()) MySystem.s_printOutExit(this, strMethod, "failed");
        dlg.setVisible(true);
        char[] chrsPassword = dlg.getPassword();
        dlg.destroy();
        dlg = null;
        if (chrsPassword == null) {
            MySystem.s_printOutTrace(this, strMethod, "nil chrsPassword");
            return null;
        }
        if (chrsPassword.length < 1) {
            MySystem.s_printOutTrace(this, strMethod, "chrsPassword.length < 1");
            return null;
        }
        if (!_isValidPassword(chrsPassword)) {
            MySystem.s_printOutWarning(this, strMethod, "! _isValidPassword(chrsPassword)");
            return null;
        }
        return chrsPassword;
    }

    private boolean _isValidPassword(char[] chrsPassword) {
        String strMethod = "_isValidPassword(chrsPassword)";
        if (chrsPassword == null) {
            MySystem.s_printOutExit(this, strMethod, "nil chrsPassword");
        }
        String str = new String(chrsPassword);
        if (!com.google.code.p.keytooliui.ktl.util.filter.StringFilterUI.s_isAllowedPassword(str)) {
            MySystem.s_printOutWarning(this, strMethod, "failed");
            String strBody = "Password value not allowed:";
            strBody += "\n";
            strBody += "  ";
            strBody += "\"";
            strBody += str;
            strBody += "\"";
            strBody += "\n\n";
            strBody += com.google.code.p.keytooliui.ktl.util.filter.StringFilterUI.s_getRulePassword();
            OPAbstract.s_showDialogWarning(super._frmOwner_, strBody);
            return false;
        }
        return true;
    }
}
