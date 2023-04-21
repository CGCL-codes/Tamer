package com.google.code.p.keytooliui.ktl.swing.panel;

import java.util.Vector;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.KeyUsage;
import com.google.code.p.keytooliui.ktl.util.jarsigner.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.panel.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

public final class PTabUICmdKtlKstOpenCrKprV3CDsa extends PTabUICmdKtlKstOpenCrKprV3CAbs {

    public static final String STR_TITLETASK = "Create DSA private key entry, with vers. #3 cert";

    private static String _s_strHelpID = null;

    static {
        String strWhere = "com.google.code.p.keytooliui.ktl.swing.panel.PTabUICmdKtlKstOpenCrKprV3CDsa";
        String strBundleFileShort = com.google.code.p.keytooliui.ktl.AppMainUIAbs.F_STR_BUNDLE_DIR + ".PTabUICmdKtlKstOpenCrKprV3CDsa";
        String strBundleFileLong = strBundleFileShort + ".properties";
        try {
            java.util.ResourceBundle rbeResources = java.util.ResourceBundle.getBundle(strBundleFileShort, java.util.Locale.getDefault());
            _s_strHelpID = rbeResources.getString("helpID");
        } catch (java.util.MissingResourceException excMissingResource) {
            excMissingResource.printStackTrace();
            MySystem.s_printOutExit(strWhere, "excMissingResource caught, " + strBundleFileLong + " not found");
        }
    }

    public void actionPerformed(ActionEvent evtAction) {
        String strMethod = "actionPerformed(evtAction)";
        String strFormatKst = ((PSelBtnTfdFileOpenKst) super._pnlSelectFileKst_).getSelectedFormatFile();
        if (strFormatKst == null) MySystem.s_printOutExit(this, strMethod, "nil strFormatKst");
        Integer itgSizeKpr = (Integer) ((PSelCmbAbs) super._pnlSelectSizeKeypair_).getSelectedItemCmb();
        int intSizeKpr = itgSizeKpr.intValue();
        String strCertAlgoSignType = (String) ((PSelCmbAbs) super._pnlSelectSigAlgoCert_).getSelectedItemCmb();
        char[] chrsPasswdKstTarget = null;
        if (super._strPasswdKst_ != null) chrsPasswdKstTarget = super._strPasswdKst_.toCharArray(); else chrsPasswdKstTarget = "".toCharArray();
        boolean blnCrtExtKeyUsage = super._cbxCrtExtKUEnabled_.isSelected();
        boolean blnCrtExtKeyUsageCritical = super._cbxCrtExtKUCritical_.isSelected();
        int intCrtExtKeyUsageValue = super._getCrtExtKUValue_();
        boolean blnCrtExtExtKeyUsageCritical = false;
        Vector<DERObjectIdentifier> vecCrtExtExtKeyUsage = null;
        if (super._cbxCrtExtEKUEnabled_.isSelected()) {
            blnCrtExtExtKeyUsageCritical = super._cbxCrtExtEKUCritical_.isSelected();
            vecCrtExtExtKeyUsage = super._getCrtExtEKUValue_();
        }
        KTLKprSaveNewDsaAbs ktl = null;
        if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJks.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprSaveNewDsaJksV3C(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, intSizeKpr, strCertAlgoSignType, super._intValidityKpr_, super._strCN_, super._strOU_, super._strO_, super._strL_, super._strST_, super._strC_, super._strEMAIL_, super._strCrtX500DNM_T_, super._strCrtX500DNM_SN_, super._strCrtX500DNM_STREET_, super._strCrtX500DNM_BUSINESS_CATEGORY_, super._strCrtX500DNM_POSTAL_CODE_, super._strCrtX500DNM_DN_QUALIFIER_, super._strCrtX500DNM_PSEUDONYM_, super._strCrtX500DNM_DATE_OF_BIRTH_, super._strCrtX500DNM_PLACE_OF_BIRTH_, super._strCrtX500DNM_GENDER_, super._strCrtX500DNM_COUNTRY_OF_CITIZENSHIP_, super._strCrtX500DNM_COUNTRY_OF_RESIDENCE_, super._strCrtX500DNM_NAME_AT_BIRTH_, super._strCrtX500DNM_POSTAL_ADDRESS_, super._strCrtX520N_SURNAME_, super._strCrtX520N_GIVENNAME_, super._strCrtX520N_INITIALS_, super._strCrtX520N_GENERATION_, super._strCrtX520N_UNIQUE_IDENTIFIER_, blnCrtExtKeyUsage, blnCrtExtKeyUsageCritical, intCrtExtKeyUsageValue, blnCrtExtExtKeyUsageCritical, vecCrtExtExtKeyUsage);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstJceks.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprSaveNewDsaJceksV3C(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, intSizeKpr, strCertAlgoSignType, super._intValidityKpr_, super._strCN_, super._strOU_, super._strO_, super._strL_, super._strST_, super._strC_, super._strEMAIL_, super._strCrtX500DNM_T_, super._strCrtX500DNM_SN_, super._strCrtX500DNM_STREET_, super._strCrtX500DNM_BUSINESS_CATEGORY_, super._strCrtX500DNM_POSTAL_CODE_, super._strCrtX500DNM_DN_QUALIFIER_, super._strCrtX500DNM_PSEUDONYM_, super._strCrtX500DNM_DATE_OF_BIRTH_, super._strCrtX500DNM_PLACE_OF_BIRTH_, super._strCrtX500DNM_GENDER_, super._strCrtX500DNM_COUNTRY_OF_CITIZENSHIP_, super._strCrtX500DNM_COUNTRY_OF_RESIDENCE_, super._strCrtX500DNM_NAME_AT_BIRTH_, super._strCrtX500DNM_POSTAL_ADDRESS_, super._strCrtX520N_SURNAME_, super._strCrtX520N_GIVENNAME_, super._strCrtX520N_INITIALS_, super._strCrtX520N_GENERATION_, super._strCrtX520N_UNIQUE_IDENTIFIER_, blnCrtExtKeyUsage, blnCrtExtKeyUsageCritical, intCrtExtKeyUsageValue, blnCrtExtExtKeyUsageCritical, vecCrtExtExtKeyUsage);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstPkcs12.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprSaveNewDsaPkcs12V3C(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, intSizeKpr, strCertAlgoSignType, super._intValidityKpr_, super._strCN_, super._strOU_, super._strO_, super._strL_, super._strST_, super._strC_, super._strEMAIL_, super._strCrtX500DNM_T_, super._strCrtX500DNM_SN_, super._strCrtX500DNM_STREET_, super._strCrtX500DNM_BUSINESS_CATEGORY_, super._strCrtX500DNM_POSTAL_CODE_, super._strCrtX500DNM_DN_QUALIFIER_, super._strCrtX500DNM_PSEUDONYM_, super._strCrtX500DNM_DATE_OF_BIRTH_, super._strCrtX500DNM_PLACE_OF_BIRTH_, super._strCrtX500DNM_GENDER_, super._strCrtX500DNM_COUNTRY_OF_CITIZENSHIP_, super._strCrtX500DNM_COUNTRY_OF_RESIDENCE_, super._strCrtX500DNM_NAME_AT_BIRTH_, super._strCrtX500DNM_POSTAL_ADDRESS_, super._strCrtX520N_SURNAME_, super._strCrtX520N_GIVENNAME_, super._strCrtX520N_INITIALS_, super._strCrtX520N_GENERATION_, super._strCrtX520N_UNIQUE_IDENTIFIER_, blnCrtExtKeyUsage, blnCrtExtKeyUsageCritical, intCrtExtKeyUsageValue, blnCrtExtExtKeyUsageCritical, vecCrtExtExtKeyUsage);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstBks.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprSaveNewDsaBksV3C(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, intSizeKpr, strCertAlgoSignType, super._intValidityKpr_, super._strCN_, super._strOU_, super._strO_, super._strL_, super._strST_, super._strC_, super._strEMAIL_, super._strCrtX500DNM_T_, super._strCrtX500DNM_SN_, super._strCrtX500DNM_STREET_, super._strCrtX500DNM_BUSINESS_CATEGORY_, super._strCrtX500DNM_POSTAL_CODE_, super._strCrtX500DNM_DN_QUALIFIER_, super._strCrtX500DNM_PSEUDONYM_, super._strCrtX500DNM_DATE_OF_BIRTH_, super._strCrtX500DNM_PLACE_OF_BIRTH_, super._strCrtX500DNM_GENDER_, super._strCrtX500DNM_COUNTRY_OF_CITIZENSHIP_, super._strCrtX500DNM_COUNTRY_OF_RESIDENCE_, super._strCrtX500DNM_NAME_AT_BIRTH_, super._strCrtX500DNM_POSTAL_ADDRESS_, super._strCrtX520N_SURNAME_, super._strCrtX520N_GIVENNAME_, super._strCrtX520N_INITIALS_, super._strCrtX520N_GENERATION_, super._strCrtX520N_UNIQUE_IDENTIFIER_, blnCrtExtKeyUsage, blnCrtExtKeyUsageCritical, intCrtExtKeyUsageValue, blnCrtExtExtKeyUsageCritical, vecCrtExtExtKeyUsage);
        } else if (strFormatKst.toLowerCase().compareTo(com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstUber.f_s_strKeystoreType.toLowerCase()) == 0) {
            ktl = new KTLKprSaveNewDsaUberV3C(super._frmOwner_, super._strPathAbsKst_, chrsPasswdKstTarget, intSizeKpr, strCertAlgoSignType, super._intValidityKpr_, super._strCN_, super._strOU_, super._strO_, super._strL_, super._strST_, super._strC_, super._strEMAIL_, super._strCrtX500DNM_T_, super._strCrtX500DNM_SN_, super._strCrtX500DNM_STREET_, super._strCrtX500DNM_BUSINESS_CATEGORY_, super._strCrtX500DNM_POSTAL_CODE_, super._strCrtX500DNM_DN_QUALIFIER_, super._strCrtX500DNM_PSEUDONYM_, super._strCrtX500DNM_DATE_OF_BIRTH_, super._strCrtX500DNM_PLACE_OF_BIRTH_, super._strCrtX500DNM_GENDER_, super._strCrtX500DNM_COUNTRY_OF_CITIZENSHIP_, super._strCrtX500DNM_COUNTRY_OF_RESIDENCE_, super._strCrtX500DNM_NAME_AT_BIRTH_, super._strCrtX500DNM_POSTAL_ADDRESS_, super._strCrtX520N_SURNAME_, super._strCrtX520N_GIVENNAME_, super._strCrtX520N_INITIALS_, super._strCrtX520N_GENERATION_, super._strCrtX520N_UNIQUE_IDENTIFIER_, blnCrtExtKeyUsage, blnCrtExtKeyUsageCritical, intCrtExtKeyUsageValue, blnCrtExtExtKeyUsageCritical, vecCrtExtExtKeyUsage);
        } else {
            MySystem.s_printOutExit(this, strMethod, "uncaught value, strFormatKst=" + strFormatKst);
        }
        if (ktl.doJob()) {
            MySystem.s_printOutTrace(this, strMethod, "OK!");
            super._doneJob_(strFormatKst, com.google.code.p.keytooliui.ktl.util.jarsigner.KTLAbs.f_s_strTypeKeypairDsa);
        } else MySystem.s_printOutTrace(this, strMethod, "either aborted by user or failed");
    }

    public PTabUICmdKtlKstOpenCrKprV3CDsa(Frame frmOwner) {
        super(PTabUICmdKtlKstOpenCrKprV3CDsa._s_strHelpID, frmOwner, com.google.code.p.keytooliui.ktl.util.jarsigner.KTLAbs.s_getItgsListSizeKprDsa(), true, true, true);
        super._pnlSelectSizeKeypair_ = new PSelCmbItgSizeKprDsa();
        super._pnlSelectSigAlgoCert_ = new PSelCmbStrCertSigAlgoDsa();
    }
}
