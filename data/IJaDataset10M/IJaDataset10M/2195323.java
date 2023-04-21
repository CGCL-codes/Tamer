package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.ktl.swing.dialog.*;
import com.google.code.p.keytooliui.shared.lang.*;
import java.security.KeyStore;
import java.awt.*;
import java.io.*;
import java.util.*;

public abstract class KTLKprOpenSigEmbKprDMAbs extends KTLKprOpenSigEmbKprAbs {

    protected KTLKprOpenSigEmbKprDMAbs(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, String strPathAbsFileOpenData, String strPathAbsFileSaveData, String strProviderKst) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, strPathAbsFileOpenData, strPathAbsFileSaveData, strProviderKst);
    }

    protected boolean _doJobSelectKpr_(File fleOpenData, File fleSaveData, KeyStore kstOpen, String[] strsAliasPKTC, Boolean[] boosIsTCEntryPKTC, Boolean[] boosValidDatePKTC, Boolean[] boosSelfSignedCertPKTC, Boolean[] boosTrustedCertPKTC, String[] strsSizeKeyPublPKTC, String[] strsTypeCertPKTC, String[] strsAlgoSigCertPKTC, Date[] dtesLastModifiedPKTC, String[] strsAliasSK, Date[] dtesLastModifiedSK) {
        String strMethod = "_doJobSelectKpr_(...)";
        DTblsKstSelPKOpenXmlSign dlg = new DTblsKstSelPKOpenXmlSign(super._frmOwner_, kstOpen, super._strPathAbsKst_, "Sign XML file with either \"SHA1withRSA\", or \"SHA1withDSA\" private key entry");
        if (!dlg.init()) MySystem.s_printOutExit(this, strMethod, "failed");
        if (!dlg.load(strsAliasPKTC, boosIsTCEntryPKTC, boosValidDatePKTC, boosSelfSignedCertPKTC, boosTrustedCertPKTC, strsSizeKeyPublPKTC, strsTypeCertPKTC, strsAlgoSigCertPKTC, dtesLastModifiedPKTC, strsAliasSK, dtesLastModifiedSK)) {
            MySystem.s_printOutWarning(this, strMethod, "could be either aborted by user, or no valid entry found inside keystore");
            return false;
        }
        dlg.setVisible(true);
        String strAliasKpr = dlg.getAlias();
        if (strAliasKpr == null) {
            MySystem.s_printOutTrace(this, strMethod, "nil strAliasKpr, aborted by user");
            return false;
        }
        char[] chrsPasswdKpr = dlg.getPassword();
        if (chrsPasswdKpr == null) {
            MySystem.s_printOutTrace(this, strMethod, "nil chrsPasswdKpr, aborted by user");
            return false;
        }
        super._setEnabledCursorWait_(true);
        if (!super._doJob_(kstOpen, strAliasKpr, chrsPasswdKpr, fleOpenData, fleSaveData)) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        super._setEnabledCursorWait_(false);
        return true;
    }
}
