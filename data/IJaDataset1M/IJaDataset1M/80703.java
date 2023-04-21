package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.ktl.swing.dialog.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.awt.*;
import java.io.*;
import java.util.*;

public class KTLKprSaveNewDsaUber extends KTLKprSaveNewDsaJAbs {

    /**
        if any error in code, exiting
        in case of trbrl: open up a warning dialog, and return false;
        
        algo:
        . get fileOpen keystore
        . open keystore
        . fill in table KeyPair
        . show dialog KeyPair new Dsa
          . get aliasKpr
          . get passwdKpr
        . create new KeyPair
        . create new certificate of type X.509
        . assign new entry to open keystore
        . save keystore
    **/
    public boolean doJob() {
        String strMethod = "doJob()";
        super._setEnabledCursorWait_(true);
        File fleOpenKst = UtilJsrFile.s_getFileOpen(super._frmOwner_, super._strPathAbsKst_);
        if (fleOpenKst == null) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutError(this, strMethod, "nil fleOpenKst");
            return false;
        }
        if (super._chrsPasswdKst_ == null) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutExit(this, strMethod, "nil super._chrsPasswdKst_");
        }
        KeyStore kstOpen = UtilKstUber.s_getKeystoreOpen(super._frmOwner_, fleOpenKst, super._chrsPasswdKst_);
        if (kstOpen == null) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutError(this, strMethod, "nil kstOpen");
            return false;
        }
        if (!super._doJob_(fleOpenKst, kstOpen)) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        super._setEnabledCursorWait_(false);
        return true;
    }

    public KTLKprSaveNewDsaUber(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, int intSizeKpr, String strCertAlgoSignType, int intValidityKpr, String strKprX500DN_CN, String strKprX500DN_OU, String strKprX500DN_O, String strKprX500DN_L, String strKprX500DN_ST, String strKprX500DN_C, String strKprX500DN_EMAIL, String strKprX500DNM_T, String strKprX500DNM_SN, String strKprX500DNM_STREET, String strKprX500DNM_BUSINESS_CATEGORY, String strKprX500DNM_POSTAL_CODE, String strKprX500DNM_DN_QUALIFIER, String strKprX500DNM_PSEUDONYM, String strKprX500DNM_DATE_OF_BIRTH, String strKprX500DNM_PLACE_OF_BIRTH, String strKprX500DNM_GENDER, String strKprX500DNM_COUNTRY_OF_CITIZENSHIP, String strKprX500DNM_COUNTRY_OF_RESIDENCE, String strKprX500DNM_NAME_AT_BIRTH, String strKprX500DNM_POSTAL_ADDRESS, String strKprX520N_SURNAME, String strKprX520N_GIVENNAME, String strKprX520N_INITIALS, String strKprX520N_GENERATION, String strKprX520N_UNIQUE_IDENTIFIER) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, intSizeKpr, strCertAlgoSignType, intValidityKpr, strKprX500DN_CN, strKprX500DN_OU, strKprX500DN_O, strKprX500DN_L, strKprX500DN_ST, strKprX500DN_C, strKprX500DN_EMAIL, strKprX500DNM_T, strKprX500DNM_SN, strKprX500DNM_STREET, strKprX500DNM_BUSINESS_CATEGORY, strKprX500DNM_POSTAL_CODE, strKprX500DNM_DN_QUALIFIER, strKprX500DNM_PSEUDONYM, strKprX500DNM_DATE_OF_BIRTH, strKprX500DNM_PLACE_OF_BIRTH, strKprX500DNM_GENDER, strKprX500DNM_COUNTRY_OF_CITIZENSHIP, strKprX500DNM_COUNTRY_OF_RESIDENCE, strKprX500DNM_NAME_AT_BIRTH, strKprX500DNM_POSTAL_ADDRESS, strKprX520N_SURNAME, strKprX520N_GIVENNAME, strKprX520N_INITIALS, strKprX520N_GENERATION, strKprX520N_UNIQUE_IDENTIFIER, KTLAbs.f_s_strProviderKstBC);
    }
}
