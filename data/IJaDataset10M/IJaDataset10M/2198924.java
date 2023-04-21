package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.shared.lang.*;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.KeyStore;
import java.awt.*;
import java.io.*;
import java.util.*;

public abstract class KTLKprSaveNewDsaAbs extends KTLKprSaveNewAbs {

    protected abstract boolean __doJob__(KeyStore kstOpen, String[] strsAliasPKTC, Boolean[] boosIsTCEntryPKTC, Boolean[] boosValidDatePKTC, Boolean[] boosSelfSignedCertPKTC, Boolean[] boosTrustedCertPKTC, String[] strsSizeKeyPublPKTC, String[] strsTypeCertPKTC, String[] strsAlgoSigCertPKTC, Date[] dtesLastModifiedPKTC, String[] strsAliasSK, Date[] dtesLastModifiedSK);

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
    protected boolean _doJob_(File fleOpenKst, KeyStore kstOpen) {
        String strMethod = "_doJob_(fleOpenKst, kstOpen)";
        if (fleOpenKst == null || kstOpen == null) MySystem.s_printOutExit(this, strMethod, "nil arg");
        String[] strsAliasPKTC = UtilKstAbs.s_getStrsAliasPKTC(super._frmOwner_, kstOpen);
        if (strsAliasPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAliasPKTC");
        }
        String[] strsAliasSK = UtilKstAbs.s_getStrsAliasSK(super._frmOwner_, kstOpen);
        if (strsAliasSK == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAliasPKTC");
        }
        Boolean[] boosIsTCEntryPKTC = UtilKstAbs.s_getBoosEntryTcr(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (boosIsTCEntryPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosIsTCEntryPKTC");
        }
        Boolean[] boosValidDatePKTC = UtilKstAbs.s_getBoosValidDatePKTC(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (boosValidDatePKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosValidDatePKTC");
        }
        Boolean[] boosSelfSignedCertPKTC = UtilKstAbs.s_getBoosSelfSigned(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (boosSelfSignedCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosSelfSignedCertPKTC");
        }
        Boolean[] boosTrustedCertPKTC = UtilKstAbs.s_getBoosTrusted(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (boosTrustedCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosTrustedCertPKTC");
        }
        String[] strsSizeKeyPublPKTC = UtilKstAbs.s_getStrsSizeKeyPubl(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (strsSizeKeyPublPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsSizeKeyPublPKTC");
        }
        String[] strsTypeCertPKTC = UtilKstAbs.s_getStrsTypeCertificatePKTC(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (strsTypeCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsTypeCertPKTC");
        }
        String[] strsAlgoSigCertPKTC = UtilKstAbs.s_getStrsAlgoSigCertPKTC(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (strsAlgoSigCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAlgoSigCertPKTC");
        }
        Date[] dtesLastModifiedPKTC = UtilKstAbs.s_getDtesLastModified(super._frmOwner_, kstOpen, strsAliasPKTC);
        if (dtesLastModifiedPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil dtesLastModifiedPKTC");
        }
        Date[] dtesLastModifiedSK = UtilKstAbs.s_getDtesLastModified(super._frmOwner_, kstOpen, strsAliasSK);
        if (dtesLastModifiedSK == null) {
            MySystem.s_printOutExit(strMethod, "nil dtesLastModifiedPKTC");
        }
        super._setEnabledCursorWait_(false);
        if (!__doJob__(kstOpen, strsAliasPKTC, boosIsTCEntryPKTC, boosValidDatePKTC, boosSelfSignedCertPKTC, boosTrustedCertPKTC, strsSizeKeyPublPKTC, strsTypeCertPKTC, strsAlgoSigCertPKTC, dtesLastModifiedPKTC, strsAliasSK, dtesLastModifiedSK)) {
            super._setEnabledCursorWait_(false);
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (super._chrsPasswdKst_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._chrsPasswdKst_");
            return false;
        }
        if (!super._saveKeyStore_(kstOpen, fleOpenKst, super._chrsPasswdKst_)) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        return true;
    }

    protected KTLKprSaveNewDsaAbs(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, int intSizeKpr, String strCertAlgoSignType, int intValidityKpr, String strKprX500DN_CN, String strKprX500DN_OU, String strKprX500DN_O, String strKprX500DN_L, String strKprX500DN_ST, String strKprX500DN_C, String strKprX500DN_EMAIL, String strKprX500DNM_T, String strKprX500DNM_SN, String strKprX500DNM_STREET, String strKprX500DNM_BUSINESS_CATEGORY, String strKprX500DNM_POSTAL_CODE, String strKprX500DNM_DN_QUALIFIER, String strKprX500DNM_PSEUDONYM, String strKprX500DNM_DATE_OF_BIRTH, String strKprX500DNM_PLACE_OF_BIRTH, String strKprX500DNM_GENDER, String strKprX500DNM_COUNTRY_OF_CITIZENSHIP, String strKprX500DNM_COUNTRY_OF_RESIDENCE, String strKprX500DNM_NAME_AT_BIRTH, String strKprX500DNM_POSTAL_ADDRESS, String strKprX520N_SURNAME, String strKprX520N_GIVENNAME, String strKprX520N_INITIALS, String strKprX520N_GENERATION, String strKprX520N_UNIQUE_IDENTIFIER, String strProviderKst) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, strProviderKst, intSizeKpr, strCertAlgoSignType, intValidityKpr, strKprX500DN_CN, strKprX500DN_OU, strKprX500DN_O, strKprX500DN_L, strKprX500DN_ST, strKprX500DN_C, strKprX500DN_EMAIL, strKprX500DNM_T, strKprX500DNM_SN, strKprX500DNM_STREET, strKprX500DNM_BUSINESS_CATEGORY, strKprX500DNM_POSTAL_CODE, strKprX500DNM_DN_QUALIFIER, strKprX500DNM_PSEUDONYM, strKprX500DNM_DATE_OF_BIRTH, strKprX500DNM_PLACE_OF_BIRTH, strKprX500DNM_GENDER, strKprX500DNM_COUNTRY_OF_CITIZENSHIP, strKprX500DNM_COUNTRY_OF_RESIDENCE, strKprX500DNM_NAME_AT_BIRTH, strKprX500DNM_POSTAL_ADDRESS, strKprX520N_SURNAME, strKprX520N_GIVENNAME, strKprX520N_INITIALS, strKprX520N_GENERATION, strKprX520N_UNIQUE_IDENTIFIER, KTLAbs.f_s_strTypeKeypairDsa);
    }

    protected void _savePublicAsStringArray_(KeyPair kpr, String strAliasKpr) {
        String strMethod = "_savePublicAsStringArray(kpr)";
        if (kpr == null || strAliasKpr == null) MySystem.s_printOutExit(this, strMethod, "nil arg");
        PublicKey keyPublic = kpr.getPublic();
        if (keyPublic == null) MySystem.s_printOutExit(this, strMethod, "nil keyPublic");
        byte[] bytsPublic = keyPublic.getEncoded();
        StringBuffer sbr = new StringBuffer();
        sbr.append("private static final int[] _f_s_intsPubKeyReg =");
        sbr.append("\n");
        sbr.append("{");
        for (int i = 0; i < bytsPublic.length; i++) {
            if (i > 0) sbr.append(",");
            sbr.append("\n");
            sbr.append("  ");
            String strCur = Integer.toString(bytsPublic[i]);
            sbr.append(strCur);
        }
        sbr.append("\n");
        sbr.append("}");
        sbr.append(";");
        if (super._strPathAbsKst_ == null) {
            MySystem.s_printOutExit(this, strMethod, "nil super._strPathAbsKst_");
        }
        try {
            FileOutputStream fos = new FileOutputStream(super._strPathAbsKst_ + "_" + strAliasKpr + ".txt");
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(sbr.toString());
            dos.close();
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutExit(this, strMethod, "exc caught");
        }
    }
}
