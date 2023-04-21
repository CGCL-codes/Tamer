package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import java.security.KeyStore;
import java.awt.*;
import java.io.*;

public final class KTLKprSaveFromPkcs12ToKPBks extends KTLKprSaveFromPkcs12ToKPAbs {

    /**
        if any error in code, exiting
        in case of trbrl: open up a warning dialog, and return false;
        
        algo:
        
        . add providers
        
        . open keystoreSource
        . open keystoreKstTarget
        . select aliasSource pointing to valid kprSource
        . get respective keySource & crtsSource
        
        . create new KeyPair
        . create new certificate of type X.509
        . assign new entry to open BKS keystore
        . save BKS keystore
    **/
    public boolean doJob() {
        String strMethod = "doJob()";
        File fleOpenKstSource = UtilJsrFile.s_getFileOpen(super._frmOwner_, super._strPathAbsKstSource_);
        if (fleOpenKstSource == null) {
            MySystem.s_printOutError(this, strMethod, "nil fleOpenKstSource");
            return false;
        }
        if (super._chrsPasswdKstSource_ == null) MySystem.s_printOutExit(this, strMethod, "nil super._chrsPasswdKstSource_");
        KeyStore kstOpenSource = UtilKstPkcs12.s_getKeystoreOpen(super._frmOwner_, fleOpenKstSource, super._chrsPasswdKstSource_);
        if (kstOpenSource == null) {
            MySystem.s_printOutError(this, strMethod, "nil kstOpenSource");
            return false;
        }
        File fleOpenKstTarget = UtilJsrFile.s_getFileOpen(super._frmOwner_, super._strPathAbsKst_);
        if (fleOpenKstTarget == null) {
            MySystem.s_printOutError(this, strMethod, "nil fleOpenKstTarget");
            return false;
        }
        if (super._chrsPasswdKst_ == null) MySystem.s_printOutExit(this, strMethod, "nil super._chrsPasswdKst_");
        KeyStore kstOpenTarget = UtilKstBks.s_getKeystoreOpen(super._frmOwner_, fleOpenKstTarget, super._chrsPasswdKst_);
        if (kstOpenTarget == null) {
            MySystem.s_printOutError(this, strMethod, "nil kstOpenTarget");
            return false;
        }
        super._setEnabledCursorWait_(true);
        if (!super._doJob_(kstOpenSource, kstOpenTarget, fleOpenKstTarget)) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        super._setEnabledCursorWait_(false);
        return true;
    }

    public KTLKprSaveFromPkcs12ToKPBks(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, String strPathAbsKstSource, char[] chrsPasswdKstSource) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, strPathAbsKstSource, chrsPasswdKstSource, KTLAbs.f_s_strProviderKstBC);
    }
}
