package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.KeyStoreException;
import java.security.Provider;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.awt.*;
import java.util.*;

public abstract class KTLTcrAbs extends KTLAbs {

    protected static boolean _s_isProviderKpgAllowed_(String strTypeTcr, String strProvider) {
        if (strTypeTcr == null || strProvider == null) return false;
        if (strTypeTcr.toLowerCase().compareTo(KTLAbs.f_s_strTypeKeypairDsa.toLowerCase()) == 0) {
            for (int i = 0; i < KTLAbs.f_s_strsProviderKpgDsa.length; i++) {
                if (strProvider.toLowerCase().compareTo(KTLAbs.f_s_strsProviderKpgDsa[i].toLowerCase()) == 0) return true;
            }
            return false;
        }
        if (strTypeTcr.toLowerCase().compareTo(KTLAbs.f_s_strTypeKeypairRsa.toLowerCase()) == 0) {
            for (int i = 0; i < KTLAbs.f_s_strsProviderKpgRsa.length; i++) {
                if (strProvider.toLowerCase().compareTo(KTLAbs.f_s_strsProviderKpgRsa[i].toLowerCase()) == 0) return true;
            }
            return false;
        }
        return false;
    }

    /**
        "Any" means either "DSA" or "RSA"
    **/
    protected Boolean[] _getBoosElligibleAny_(Boolean[] boosEntryTcr, String[] strsAlgoKeyPubl, Boolean[] boosTypeCertX509) {
        String strMethod = "_getBoosElligibleAny_(...)";
        if (boosEntryTcr == null || strsAlgoKeyPubl == null || boosTypeCertX509 == null) MySystem.s_printOutExit(this, strMethod, "nil arg");
        Boolean[] boosElligible = new Boolean[strsAlgoKeyPubl.length];
        for (int i = 0; i < strsAlgoKeyPubl.length; i++) {
            boolean blnOk = true;
            if (boosEntryTcr[i].booleanValue() == false) {
                blnOk = false;
            } else if ((strsAlgoKeyPubl[i].toLowerCase().compareTo(KTLAbs.f_s_strTypeKeypairRsa.toLowerCase()) != 0) && (strsAlgoKeyPubl[i].toLowerCase().compareTo(KTLAbs.f_s_strTypeKeypairDsa.toLowerCase()) != 0)) {
                blnOk = false;
            } else if (boosTypeCertX509[i].booleanValue() == false) {
                blnOk = false;
            }
            boosElligible[i] = new Boolean(blnOk);
        }
        return boosElligible;
    }

    protected Boolean[] _getBoosTypeCertX509_(KeyStore kstOpen, String[] strsAlias) {
        String strMethod = "_getBoosTypeCertX509_(kstOpen, strsAlias)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(this, strMethod, "nil arg");
        Boolean[] boosTypeCertX509 = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Certificate crt = null;
            try {
                crt = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(this, strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                OPAbstract.s_showDialogWarning(super._frmOwner_, strBody);
                return null;
            }
            boolean blnOk = true;
            if (crt == null) blnOk = false; else if (!(crt instanceof X509Certificate)) blnOk = false;
            boosTypeCertX509[i] = new Boolean(blnOk);
        }
        return boosTypeCertX509;
    }

    protected KTLTcrAbs(Frame frmOwner, String strPathAbsKst, char[] chrsPasswdKst, String strProviderKst) {
        super(frmOwner, strPathAbsKst, chrsPasswdKst, strProviderKst);
    }
}
