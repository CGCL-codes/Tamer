package com.google.code.p.keytooliui.ktl.util.jarsigner;

import java.security.cert.CertificateExpiredException;
import com.google.code.p.keytooliui.ktl.swing.dialog.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import java.security.PublicKey;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;
import java.awt.*;
import java.util.*;

public abstract class UtilKstAbs {

    private static final String _f_s_strClass = "com.google.code.p.keytooliui.ktl.util.jarsigner.UtilKstAbs";

    public static PublicKey s_getKeyPublic(Component cmpOwner, KeyStore kseLoaded, String strAlias) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "s_getKeyPublic(...)";
        if (kseLoaded == null || strAlias == null) {
            MySystem.s_printOutExit(strMethod, " nil arg");
        }
        boolean blnAliasOk = false;
        try {
            blnAliasOk = kseLoaded.containsAlias(strAlias);
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutExit(strMethod, excKeyStore.getMessage());
        }
        if (!blnAliasOk) {
            MySystem.s_printOutWarning(strMethod, " ! blnAliasOk");
            String strBody = "keystore does not contain alias named:";
            strBody += "\n";
            strBody += "  ";
            strBody += "\"";
            strBody += "strAlias";
            strBody += "\"";
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
        try {
            java.security.cert.Certificate cert = kseLoaded.getCertificate(strAlias);
            return cert.getPublicKey();
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutWarning(strMethod, " exc caught");
            String strBody = "failed to recovery public key for alias named:";
            strBody += "\n";
            strBody += "  " + strAlias;
            strBody += "\n\n";
            strBody += exc.getMessage();
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
    }

    public static Boolean[] s_getBoosValidDatePKTC(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosValidDatePKTC(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosValidDatePKTC = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnIsTC = false;
            try {
                blnIsTC = kstOpen.isCertificateEntry(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n";
                strBody += excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            boolean blnValidDateTC = true;
            if (blnIsTC) {
                X509Certificate crtX509 = null;
                try {
                    java.security.cert.Certificate crt = kstOpen.getCertificate(strsAlias[i]);
                    if (!(crt instanceof X509Certificate)) {
                        String strBody = "failed to recovery public key for alias named:";
                        strBody += "\n";
                        strBody += "  " + strsAlias[i];
                        strBody += "\n\nNot a certificate of type X509";
                        OPAbstract.s_showDialogError(cmpOwner, strBody);
                        return null;
                    }
                    crtX509 = (X509Certificate) crt;
                } catch (Exception exc) {
                    exc.printStackTrace();
                    MySystem.s_printOutWarning(strMethod, " exc caught");
                    String strBody = "failed to recovery public key for alias named:";
                    strBody += "\n";
                    strBody += "  " + strsAlias[i];
                    strBody += "\n\nException caught:";
                    strBody += "\n  " + exc.getMessage();
                    OPAbstract.s_showDialogError(cmpOwner, strBody);
                    return null;
                }
                try {
                    crtX509.checkValidity();
                } catch (CertificateExpiredException excCertificateExpired) {
                    blnValidDateTC = false;
                } catch (java.security.cert.CertificateNotYetValidException excCertificateNotYetValid) {
                    blnValidDateTC = false;
                    ;
                }
                boosValidDatePKTC[i] = new Boolean(blnValidDateTC);
            } else {
                Certificate[] crts = null;
                try {
                    crts = kstOpen.getCertificateChain(strsAlias[i]);
                } catch (Exception exc) {
                }
                if (crts == null) {
                }
                boolean blnValidDatePK = true;
                for (int j = 0; j < crts.length; j++) {
                    X509Certificate crtX509 = null;
                    if (!(crts[j] instanceof X509Certificate)) {
                        String strBody = "failed to recovery public key for alias named:";
                        strBody += "\n";
                        strBody += "  " + strsAlias[i];
                        strBody += "\n\nNot a certificate of type X509";
                        OPAbstract.s_showDialogError(cmpOwner, strBody);
                        return null;
                    }
                    crtX509 = (X509Certificate) crts[j];
                    try {
                        crtX509.checkValidity();
                    } catch (CertificateExpiredException excCertificateExpired) {
                        blnValidDatePK = false;
                        break;
                    } catch (java.security.cert.CertificateNotYetValidException excCertificateNotYetValid) {
                        blnValidDatePK = false;
                        break;
                    }
                }
                boosValidDatePKTC[i] = new Boolean(blnValidDatePK);
            }
        }
        return boosValidDatePKTC;
    }

    public static boolean s_checkValidDateCert(Component cmpOwner, KeyStore kseLoaded, String strAlias) {
        String strMethod = "s_checkValidDateCert(cmpOwner, kseLoaded, strAlias)";
        if (kseLoaded == null || strAlias == null) {
            MySystem.s_printOutExit(strMethod, " nil arg");
        }
        boolean blnAliasOk = false;
        try {
            blnAliasOk = kseLoaded.containsAlias(strAlias);
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutExit(strMethod, excKeyStore.getMessage());
        }
        if (!blnAliasOk) {
            MySystem.s_printOutWarning(strMethod, " ! blnAliasOk");
            String strBody = "keystore does not contain alias named:";
            strBody += "\n";
            strBody += "  ";
            strBody += "\"";
            strBody += "strAlias";
            strBody += "\"";
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return false;
        }
        X509Certificate crtX509 = null;
        try {
            java.security.cert.Certificate crt = kseLoaded.getCertificate(strAlias);
            if (!(crt instanceof X509Certificate)) {
                String strBody = "failed to recovery public key for alias named:";
                strBody += "\n";
                strBody += "  " + strAlias;
                strBody += "\n\nNot a certificate of type X509";
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return false;
            }
            crtX509 = (X509Certificate) crt;
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutWarning(strMethod, " exc caught");
            String strBody = "failed to recovery public key for alias named:";
            strBody += "\n";
            strBody += "  " + strAlias;
            strBody += "\n\nException caught:";
            strBody += "\n  " + exc.getMessage();
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return false;
        }
        try {
            crtX509.checkValidity();
        } catch (CertificateExpiredException excCertificateExpired) {
            MySystem.s_printOutWarning(strMethod, "excCertificateExpired caught");
            String strBody = "Certificate has expired!";
            strBody += "\n\n" + "Continue anyway?";
            if (!OPAbstract.s_showWarningConfirmDialog(cmpOwner, strBody)) {
                MySystem.s_printOutTrace(strMethod, "aborted by user");
                return false;
            }
        } catch (java.security.cert.CertificateNotYetValidException excCertificateNotYetValid) {
            MySystem.s_printOutWarning(strMethod, "excCertificateNotYetValid caught");
            String strBody = "Certificate not yet valid!";
            strBody += "\n\n" + "Continue anyway?";
            if (!OPAbstract.s_showWarningConfirmDialog(cmpOwner, strBody)) {
                MySystem.s_printOutTrace(strMethod, "aborted by user");
                return false;
            }
        }
        return true;
    }

    public static String s_getCertSigAlgo(Component cmpOwner, KeyStore kseLoaded, String strAlias) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "s_getCertSigAlgo(...)";
        if (kseLoaded == null || strAlias == null) {
            MySystem.s_printOutExit(strMethod, " nil arg");
        }
        boolean blnAliasOk = false;
        try {
            blnAliasOk = kseLoaded.containsAlias(strAlias);
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutExit(strMethod, excKeyStore.getMessage());
        }
        if (!blnAliasOk) {
            MySystem.s_printOutWarning(strMethod, " ! blnAliasOk");
            String strBody = "keystore does not contain alias named:";
            strBody += "\n";
            strBody += "  ";
            strBody += "\"";
            strBody += "strAlias";
            strBody += "\"";
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
        try {
            java.security.cert.Certificate crt = kseLoaded.getCertificate(strAlias);
            if (!(crt instanceof X509Certificate)) {
                String strBody = "failed to recovery public key for alias named:";
                strBody += "\n";
                strBody += "  " + strAlias;
                strBody += "\n\nNot a certificate of type X509";
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            X509Certificate crtX509 = (X509Certificate) crt;
            return crtX509.getSigAlgName();
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutWarning(strMethod, " exc caught");
            String strBody = "failed to recovery public key for alias named:";
            strBody += "\n";
            strBody += "  " + strAlias;
            strBody += "\n\nException caught:";
            strBody += "\n  " + exc.getMessage();
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
    }

    /**
        if any code or config error, exiting
        else if any other type of error, show warning/error dialog, then return null
    **/
    public static Key s_getKey(Component cmpOwner, KeyStore kseLoaded, String strAliasKpr, char[] chrsPasswdKpr) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "s_getKey(...)";
        if (kseLoaded == null || strAliasKpr == null || chrsPasswdKpr == null) {
            MySystem.s_printOutExit(strMethod, " nil arg");
        }
        boolean blnAliasOk = false;
        try {
            blnAliasOk = kseLoaded.containsAlias(strAliasKpr);
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutExit(strMethod, excKeyStore.getMessage());
        }
        if (!blnAliasOk) {
            MySystem.s_printOutWarning(strMethod, " ! blnAliasOk");
            String strBody = "keystore does not contain alias named:";
            strBody += "\n";
            strBody += "  ";
            strBody += "\"";
            strBody += "strAlias";
            strBody += "\"";
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
        Key key = null;
        try {
            key = kseLoaded.getKey(strAliasKpr, chrsPasswdKpr);
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutExit(strMethod, excKeyStore.getMessage());
        } catch (NoSuchAlgorithmException excNoSuchAlgorithm) {
            excNoSuchAlgorithm.printStackTrace();
            MySystem.s_printOutExit(strMethod, excNoSuchAlgorithm.getMessage());
        } catch (UnrecoverableKeyException excUnrecoverableKey) {
            excUnrecoverableKey.printStackTrace();
            MySystem.s_printOutWarning(strMethod, " excUnrecoverableKey caught");
            String strBody = "Cannot recover key!";
            strBody += "\n";
            strBody += "The given password may be wrong.";
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutExit(strMethod, exc.getMessage());
        }
        if (key == null) {
            MySystem.s_printOutExit(strMethod, " nil key");
        }
        return key;
    }

    public static boolean s_setKeyEntry(Frame frmOwner, KeyStore kstOpen, String strAliasKpr, Key key, char[] chrsPasswdKpr, Certificate[] crtsChain) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "s_setKeyEntry(...)";
        if (kstOpen == null || strAliasKpr == null || key == null || chrsPasswdKpr == null) MySystem.s_printOutExit(strMethod, "nil arg");
        try {
            kstOpen.setKeyEntry(strAliasKpr, key, chrsPasswdKpr, crtsChain);
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutError(strMethod, "exc caught");
            String strBody = "Got exception.";
            strBody += "\n" + exc.getMessage();
            strBody += "\n\n";
            strBody += "More: see session.log file";
            OPAbstract.s_showDialogError(frmOwner, strBody);
            return false;
        }
        return true;
    }

    public static Date[] s_getDtesLastModified(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getDtesLastModified(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Date[] dtesLastModified = new Date[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Date dte = null;
            try {
                dte = kstOpen.getCreationDate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            dtesLastModified[i] = dte;
        }
        return dtesLastModified;
    }

    /**
        assuming all certs in certs chain have the same signature algorithm name
    **/
    public static String[] s_getStrsAlgoSigCertPKTC(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getStrsAlgoSigCert(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        String[] strsAlgoSigCert = new String[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Certificate crt = null;
            try {
                crt = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            if (!(crt instanceof X509Certificate)) {
                strsAlgoSigCert[i] = KTLAbs.f_s_strCertSigAlgoUnknown;
                continue;
            }
            X509Certificate crtX509 = (X509Certificate) crt;
            strsAlgoSigCert[i] = new String(crtX509.getSigAlgName());
        }
        return strsAlgoSigCert;
    }

    public static String[] s_getStrsTypeCertificatePKTC(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getStrsTypeCertificatePKTC(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        String[] strsTypeCert = new String[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Certificate cert = null;
            try {
                cert = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            if (cert == null) {
                MySystem.s_printOutWarning(strMethod, "nil cert, assigning _void_");
                strsTypeCert[i] = "_void_";
                continue;
            }
            strsTypeCert[i] = cert.getType();
        }
        return strsTypeCert;
    }

    public static String[] s_getStrsAlgoKeyPubl(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getStrsAlgoKeyPubl(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        String[] strsAlgoKpu = new String[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Certificate cert = null;
            try {
                cert = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            if (cert == null) {
                MySystem.s_printOutWarning(strMethod, "nil cert, assigning _void_");
                strsAlgoKpu[i] = "_void_";
                continue;
            }
            PublicKey pky = cert.getPublicKey();
            strsAlgoKpu[i] = pky.getAlgorithm();
        }
        return strsAlgoKpu;
    }

    public static String[] s_getStrsSizeKeyPubl(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getStrsSizeKeyPubl(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        String[] strsSizeKpu = new String[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Certificate cert = null;
            try {
                cert = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            if (cert == null) {
                MySystem.s_printOutWarning(strMethod, "nil cert, assigning (unknown)");
                strsSizeKpu[i] = "(unknown)";
                continue;
            }
            if (cert instanceof X509Certificate) {
                int intSize = UtilCrtX509.s_getSizeKey((X509Certificate) cert);
                if (intSize == -1) strsSizeKpu[i] = "(unknown)"; else strsSizeKpu[i] = intSize + " bits";
            } else {
                strsSizeKpu[i] = "(unknown)";
            }
        }
        return strsSizeKpu;
    }

    /**
        based on keystore containing default cacerts  
    **/
    public static Boolean[] s_getBoosTrusted(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosTrusted(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        KeyStore kstCertsTrustCASys = UtilKstJks.s_getKstOpenCertsTrustCASys((Frame) cmpOwner, true);
        if (kstCertsTrustCASys == null) {
            MySystem.s_printOutError(strMethod, "nil kstCertsTrustCASys");
            return null;
        }
        String[] strsAliasKstCacertsDefault = UtilKstAbs.s_getStrsAlias(cmpOwner, kstCertsTrustCASys);
        if (strsAliasKstCacertsDefault == null) {
            MySystem.s_printOutError(strMethod, "nil kstCertsTrustCASys");
            return null;
        }
        HashSet<Certificate> hstCrtTrusted = new HashSet<Certificate>();
        try {
            for (int i = 0; i < strsAliasKstCacertsDefault.length; i++) {
                Certificate crtTrusted = kstCertsTrustCASys.getCertificate(strsAliasKstCacertsDefault[i]);
                hstCrtTrusted.add(crtTrusted);
            }
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutError(strMethod, "excKeyStore caught");
            String strBody = "Got keystore Exception.";
            strBody += "\n\n" + excKeyStore.getMessage();
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
        Boolean[] boosTrusted = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnCurTrusted = false;
            Certificate crtCur2check = null;
            try {
                crtCur2check = kstOpen.getCertificate(strsAlias[i]);
                if (crtCur2check != null) blnCurTrusted = UtilKstAbs._s_isTrustedCert(crtCur2check, hstCrtTrusted);
                if (!blnCurTrusted) {
                    Certificate[] crtsChainCur2check = kstOpen.getCertificateChain(strsAlias[i]);
                    if (crtsChainCur2check != null) {
                        blnCurTrusted = UtilKstAbs._s_isTrustedCertsChain(crtsChainCur2check, hstCrtTrusted);
                    }
                }
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            boosTrusted[i] = new Boolean(blnCurTrusted);
        }
        return boosTrusted;
    }

    public static Boolean[] s_getBoosEntryKpr(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosEntryKpr(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosEntryKpr = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnCur = false;
            try {
                blnCur = kstOpen.isKeyEntry(strsAlias[i]);
                if (blnCur) {
                    if (kstOpen.getCertificateChain(strsAlias[i]) == null) blnCur = false;
                }
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            boosEntryKpr[i] = new Boolean(blnCur);
        }
        return boosEntryKpr;
    }

    public static Boolean[] s_getBoosEntryTcr(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosEntryTcr(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosEntryTcr = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnCur = false;
            try {
                blnCur = kstOpen.isCertificateEntry(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            boosEntryTcr[i] = new Boolean(blnCur);
        }
        return boosEntryTcr;
    }

    public static Boolean[] s_getBoosEntryTcrRsa(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosEntryTcrRsa(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosEntryTcrRsa = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnCur = false;
            try {
                blnCur = kstOpen.isCertificateEntry(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            if (!blnCur) {
                boosEntryTcrRsa[i] = new Boolean(false);
                continue;
            }
            Certificate cert = null;
            try {
                cert = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                excKeyStore.printStackTrace();
                MySystem.s_printOutError(strMethod, "Got keystore Exception, strsAlias[i]=" + strsAlias[i]);
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogError(cmpOwner, strBody);
                return null;
            }
            if (cert == null) {
                MySystem.s_printOutWarning(strMethod, "nil cert, strsAlias[i]=" + strsAlias[i]);
                boosEntryTcrRsa[i] = new Boolean(false);
                continue;
            }
            PublicKey pky = cert.getPublicKey();
            String str = pky.getAlgorithm();
            if (str.toLowerCase().compareTo("rsa") == 0) boosEntryTcrRsa[i] = new Boolean(true); else boosEntryTcrRsa[i] = new Boolean(false);
        }
        return boosEntryTcrRsa;
    }

    public static Boolean[] s_getBoosEntryKprRsa(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosEntryKprRsa(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosEntryKprRsa = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnCur = false;
            try {
                blnCur = kstOpen.isKeyEntry(strsAlias[i]);
                if (!blnCur) {
                    boosEntryKprRsa[i] = new Boolean(false);
                    continue;
                }
                if (kstOpen.getCertificateChain(strsAlias[i]) == null) {
                    boosEntryKprRsa[i] = new Boolean(false);
                    continue;
                }
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            Certificate cert = null;
            try {
                cert = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                excKeyStore.printStackTrace();
                MySystem.s_printOutError(strMethod, "Got keystore Exception, strsAlias[i]=" + strsAlias[i]);
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogError(cmpOwner, strBody);
                return null;
            }
            if (cert == null) {
                MySystem.s_printOutWarning(strMethod, "nil cert, strsAlias[i]=" + strsAlias[i]);
                boosEntryKprRsa[i] = new Boolean(false);
                continue;
            }
            PublicKey pky = cert.getPublicKey();
            String str = pky.getAlgorithm();
            if (str.toLowerCase().compareTo("rsa") == 0) boosEntryKprRsa[i] = new Boolean(true); else boosEntryKprRsa[i] = new Boolean(false);
        }
        return boosEntryKprRsa;
    }

    public static Boolean[] s_getBoosEntryKprXmlSign(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosEntryKprXmlSign(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosEntryKprXmlSign = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            boolean blnCur = false;
            try {
                blnCur = kstOpen.isKeyEntry(strsAlias[i]);
                if (!blnCur) {
                    boosEntryKprXmlSign[i] = new Boolean(false);
                    continue;
                }
                if (kstOpen.getCertificateChain(strsAlias[i]) == null) {
                    boosEntryKprXmlSign[i] = new Boolean(false);
                    continue;
                }
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            Certificate cert = null;
            try {
                cert = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                excKeyStore.printStackTrace();
                MySystem.s_printOutError(strMethod, "Got keystore Exception, strsAlias[i]=" + strsAlias[i]);
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogError(cmpOwner, strBody);
                return null;
            }
            if (cert == null) {
                MySystem.s_printOutWarning(strMethod, "nil cert, strsAlias[i]=" + strsAlias[i]);
                boosEntryKprXmlSign[i] = new Boolean(false);
                continue;
            }
            X509Certificate crtX509 = (X509Certificate) cert;
            String str = crtX509.getSigAlgName();
            if (str.toLowerCase().compareTo("sha1withrsa") == 0) boosEntryKprXmlSign[i] = new Boolean(true); else if (str.toLowerCase().compareTo("sha1withdsa") == 0) boosEntryKprXmlSign[i] = new Boolean(true); else if (str.toLowerCase().compareTo("sha1withrsaencryption") == 0) boosEntryKprXmlSign[i] = new Boolean(true); else if (str.toLowerCase().compareTo("dsa") == 0) boosEntryKprXmlSign[i] = new Boolean(true); else boosEntryKprXmlSign[i] = new Boolean(false);
        }
        return boosEntryKprXmlSign;
    }

    public static Boolean[] s_getBoosSelfSigned(Component cmpOwner, KeyStore kstOpen, String[] strsAlias) {
        String strMethod = _f_s_strClass + "." + "s_getBoosSelfSigned(...)";
        if (kstOpen == null || strsAlias == null) MySystem.s_printOutExit(strMethod, "nil arg");
        Boolean[] boosSelfSigned = new Boolean[strsAlias.length];
        for (int i = 0; i < strsAlias.length; i++) {
            Certificate crtCur2check = null;
            try {
                crtCur2check = kstOpen.getCertificate(strsAlias[i]);
            } catch (KeyStoreException excKeyStore) {
                MySystem.s_printOutError(strMethod, "Got keystore Exception");
                String strBody = "Got keystore Exception.";
                strBody += "\n\n" + excKeyStore.getMessage();
                OPAbstract.s_showDialogWarning(cmpOwner, strBody);
                return null;
            }
            boolean bln = false;
            if (crtCur2check != null) bln = com.google.code.p.keytooliui.shared.util.jarsigner.UtilCrt.s_isSelfSigned(crtCur2check);
            boosSelfSigned[i] = new Boolean(bln);
        }
        return boosSelfSigned;
    }

    public static String[] s_getStrsAlias(Component cmpOwner, KeyStore kstOpen) {
        String strMethod = _f_s_strClass + "." + "s_getStrsAlias(...)";
        if (kstOpen == null) MySystem.s_printOutExit(strMethod, "nil kstOpen");
        Enumeration enuAlias = null;
        try {
            enuAlias = kstOpen.aliases();
        } catch (KeyStoreException excKeyStore) {
            MySystem.s_printOutError(strMethod, "Got keystore Exception");
            String strBody = "Got keystore Exception.";
            strBody += "\n\n" + excKeyStore.getMessage();
            OPAbstract.s_showDialogWarning(cmpOwner, strBody);
            return null;
        }
        Vector<String> vecStrAlias = new Vector<String>();
        while (enuAlias.hasMoreElements()) {
            String strAlias = (String) enuAlias.nextElement();
            vecStrAlias.add(strAlias);
        }
        String[] strsAlias = new String[vecStrAlias.size()];
        for (int i = 0; i < vecStrAlias.size(); i++) strsAlias[i] = (String) vecStrAlias.get(i);
        return strsAlias;
    }

    public static String[] s_getStrsAliasPKTC(Component cmpOwner, KeyStore kstOpen) {
        String strMethod = _f_s_strClass + "." + "s_getStrsAliasPKTC(...)";
        if (kstOpen == null) MySystem.s_printOutExit(strMethod, "nil kstOpen");
        Enumeration enuAlias = null;
        try {
            enuAlias = kstOpen.aliases();
        } catch (KeyStoreException excKeyStore) {
            MySystem.s_printOutError(strMethod, "Got keystore Exception");
            String strBody = "Got keystore Exception.";
            strBody += "\n\n" + excKeyStore.getMessage();
            OPAbstract.s_showDialogError(cmpOwner, strBody);
            return null;
        }
        Vector<String> vecStrAlias = new Vector<String>();
        while (enuAlias.hasMoreElements()) {
            String strAlias = (String) enuAlias.nextElement();
            try {
                boolean blnOK = kstOpen.isCertificateEntry(strAlias);
                if (!blnOK) {
                    blnOK = kstOpen.isKeyEntry(strAlias);
                    if (!blnOK) {
                        MySystem.s_printOutError(strMethod, "Uncaught entry aliased: " + strAlias + ", ignoring ...");
                        continue;
                    }
                    if (kstOpen.getCertificateChain(strAlias) == null) continue;
                }
            } catch (Exception exc) {
                MySystem.s_printOutError(strMethod, "Got Exception");
                String strBody = "Got Exception.";
                strBody += "\n\n" + exc.getMessage();
                OPAbstract.s_showDialogError(cmpOwner, strBody);
                return null;
            }
            vecStrAlias.add(strAlias);
        }
        ArrayList altAlias = new ArrayList();
        for (int i = 0; i < vecStrAlias.size(); i++) altAlias.add((String) vecStrAlias.get(i));
        Collections.sort(altAlias);
        String[] strsAlias = new String[altAlias.size()];
        for (int i = 0; i < altAlias.size(); i++) strsAlias[i] = (String) altAlias.get(i);
        return strsAlias;
    }

    public static String[] s_getStrsAliasSK(Component cmpOwner, KeyStore kstOpen) {
        String strMethod = _f_s_strClass + "." + "s_getStrsAliasSK(...)";
        if (kstOpen == null) MySystem.s_printOutExit(strMethod, "nil kstOpen");
        Enumeration enuAlias = null;
        try {
            enuAlias = kstOpen.aliases();
        } catch (KeyStoreException excKeyStore) {
            MySystem.s_printOutError(strMethod, "Got keystore Exception");
            String strBody = "Got keystore Exception.";
            strBody += "\n\n" + excKeyStore.getMessage();
            OPAbstract.s_showDialogError(cmpOwner, strBody);
            return null;
        }
        Vector<String> vecStrAlias = new Vector<String>();
        while (enuAlias.hasMoreElements()) {
            String strAlias = (String) enuAlias.nextElement();
            try {
                if (kstOpen.isCertificateEntry(strAlias)) continue;
                boolean blnOK = kstOpen.isKeyEntry(strAlias);
                if (!blnOK) {
                    MySystem.s_printOutError(strMethod, "Uncaught entry aliased: " + strAlias + ", ignoring ...");
                    continue;
                }
                if (kstOpen.getCertificateChain(strAlias) != null) continue;
            } catch (Exception exc) {
                MySystem.s_printOutError(strMethod, "Got Exception");
                String strBody = "Got Exception.";
                strBody += "\n\n" + exc.getMessage();
                OPAbstract.s_showDialogError(cmpOwner, strBody);
                return null;
            }
            vecStrAlias.add(strAlias);
        }
        ArrayList altAlias = new ArrayList();
        for (int i = 0; i < vecStrAlias.size(); i++) altAlias.add((String) vecStrAlias.get(i));
        Collections.sort(altAlias);
        String[] strsAlias = new String[altAlias.size()];
        for (int i = 0; i < altAlias.size(); i++) strsAlias[i] = (String) altAlias.get(i);
        return strsAlias;
    }

    protected static KeyStore _s_getKeystoreNew_(Frame frmOwner, String strKeystoreType, String strKeystoreProvider) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_getKeystoreNew_(...)";
        if (strKeystoreType == null || strKeystoreProvider == null) {
            MySystem.s_printOutExit(strMethod, "nil arg");
        }
        KeyStore kstNew = UtilKstAbs._s_getInstance_(strKeystoreType, strKeystoreProvider);
        if (kstNew == null) {
            MySystem.s_printOutExit(strMethod, "nil kstNew");
        }
        try {
            kstNew.load((InputStream) null, (char[]) null);
        } catch (Exception exc) {
            exc.printStackTrace();
            MySystem.s_printOutError(strMethod, "exc caught");
            String strBody = "Got Exception.";
            strBody += "\n\n" + exc.getMessage();
            OPAbstract.s_showDialogError(frmOwner, strBody);
            return null;
        }
        MySystem.s_printOutTrace(strMethod, "strKeystoreType=" + strKeystoreType + ", strKeystoreProvider=" + strKeystoreProvider);
        return kstNew;
    }

    /**
        open an existing keystore,
        keystore may be empty (no entries)
        
        if code error, exiting
        else if any error, show warning dialog and return false
    **/
    protected static KeyStore _s_getKeystoreOpen_(Frame frmOwner, File fleOpen, char[] chrsPassword, String strKeystoreType, String strKeystoreProvider) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_getKeystoreOpen_(...)";
        if (fleOpen == null || strKeystoreType == null || strKeystoreProvider == null) {
            MySystem.s_printOutExit(strMethod, "nil arg");
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fleOpen);
        } catch (FileNotFoundException excFileNotFound) {
            excFileNotFound.printStackTrace();
            MySystem.s_printOutError(strMethod, "excFileNotFound caught");
            String strBody = "Got FileNotFound Exception, file location:";
            strBody += "\n  " + fleOpen.getAbsolutePath();
            strBody += "\n\n" + excFileNotFound.getMessage();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        KeyStore kstOpen = UtilKstAbs._s_getInstance_(strKeystoreType, strKeystoreProvider);
        if (kstOpen == null) {
            MySystem.s_printOutExit(strMethod, "nil kstOpen");
        }
        try {
            kstOpen.load(fis, chrsPassword);
        } catch (IOException excIO) {
            excIO.printStackTrace();
            MySystem.s_printOutWarning(strMethod, "excIO caught, strKeystoreType=" + strKeystoreType + ", strKeystoreProvider=" + strKeystoreProvider);
            String strBody = "Got IO Exception, file location:";
            strBody += "\n  " + fleOpen.getAbsolutePath();
            strBody += "\n\n";
            strBody += "... Tampered keystore file, or incorrect ";
            strBody += strKeystoreType;
            strBody += " keystore password!";
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        } catch (NoSuchAlgorithmException excNoSuchAlgorithm) {
            excNoSuchAlgorithm.printStackTrace();
            MySystem.s_printOutError(strMethod, "excNoSuchAlgorithm caught");
            String strBody = "Got NoSuchAlgorithm Exception";
            strBody += "\n\n" + excNoSuchAlgorithm.getMessage();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        } catch (CertificateException excCertificate) {
            excCertificate.printStackTrace();
            MySystem.s_printOutError(strMethod, "excCertificate caught");
            String strBody = "Got Certificate Exception";
            strBody += "\n\n" + excCertificate.getMessage();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        } catch (NoClassDefFoundError errNoClassDefFound) {
            errNoClassDefFound.printStackTrace();
            MySystem.s_printOutError(strMethod, "errNoClassDefFound caught");
            String strBody = "Got No Class Definition Found error.";
            strBody += "\n\n";
            strBody += "See log file to get more info.";
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        } catch (ExceptionInInitializerError errExceptionInInitializer) {
            errExceptionInInitializer.printStackTrace();
            MySystem.s_printOutError(strMethod, "errExceptionInInitializer caught");
            String strBody = "Got Exception In Initializer error.";
            strBody += "\n\n";
            strBody += "See log file to get more info.";
            strBody += "\n\n" + errExceptionInInitializer.getMessage();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        if (kstOpen.getType().toLowerCase().compareTo(strKeystoreType.toLowerCase()) != 0) {
            MySystem.s_printOutError(strMethod, "wrong keystore type, kstOpen.getType()=" + kstOpen.getType());
            String strBody = "Wrong keystore type: ";
            strBody += kstOpen.getType();
            strBody += ".";
            strBody += "\n\n";
            strBody += "Allowed keystore type: ";
            strBody += strKeystoreType;
            strBody += ".";
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        try {
            fis.close();
            fis = null;
        } catch (IOException excIO) {
            excIO.printStackTrace();
            MySystem.s_printOutError(strMethod, "excIO caught, fleOpen.getAbsolutePath()=" + fleOpen.getAbsolutePath());
            String strBody = "Got IO Exception";
            strBody += "\n\n" + excIO.getMessage();
            OPAbstract.s_showDialogWarning(frmOwner, strBody);
            return null;
        }
        return kstOpen;
    }

    /**
        in case of error, return nil, therefore calling method should perform an exit!
    **/
    protected static KeyStore _s_getInstance_(String strType, String strProvider) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_getInstance_(strType, strProvider)";
        if (strType == null || strProvider == null) {
            MySystem.s_printOutError(strMethod, "nil arg");
            return null;
        }
        KeyStore kse = null;
        try {
            kse = KeyStore.getInstance(strType, strProvider);
        } catch (KeyStoreException excKeyStore) {
            excKeyStore.printStackTrace();
            MySystem.s_printOutError(strMethod, "excKeyStore caught, strProvider=" + strProvider);
            return null;
        } catch (NoSuchProviderException excNoSuchProvider) {
            excNoSuchProvider.printStackTrace();
            MySystem.s_printOutError(strMethod, "excNoSuchProvider caught, strType=" + strType);
            return null;
        }
        return kse;
    }

    protected static void _s_manageKstOpen_(Frame frmParent, KeyStore kstOpen, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, String strKeystoreProvider) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_manageKstOpen_(...)";
        if (kstOpen == null || strPathAbsOpenKst == null || chrsPasswdOpenKst == null || strKeystoreProvider == null) MySystem.s_printOutExit(strMethod, "nil arg");
        String[] strsAliasPKTC = UtilKstAbs.s_getStrsAliasPKTC(frmParent, kstOpen);
        if (strsAliasPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAliasPKTC");
        }
        String[] strsAliasSK = UtilKstAbs.s_getStrsAliasSK(frmParent, kstOpen);
        if (strsAliasSK == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAliasPKTC");
        }
        Boolean[] boosIsTCEntryPKTC = UtilKstAbs.s_getBoosEntryTcr(frmParent, kstOpen, strsAliasPKTC);
        if (boosIsTCEntryPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosIsTCEntryPKTC");
        }
        Boolean[] boosValidDatePKTC = UtilKstAbs.s_getBoosValidDatePKTC(frmParent, kstOpen, strsAliasPKTC);
        if (boosValidDatePKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosValidDatePKTC");
        }
        Boolean[] boosSelfSignedCertPKTC = UtilKstAbs.s_getBoosSelfSigned(frmParent, kstOpen, strsAliasPKTC);
        if (boosSelfSignedCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosSelfSignedCertPKTC");
        }
        Boolean[] boosTrustedCertPKTC = UtilKstAbs.s_getBoosTrusted(frmParent, kstOpen, strsAliasPKTC);
        if (boosTrustedCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosTrustedCertPKTC");
        }
        String[] strsSizeKeyPublPKTC = UtilKstAbs.s_getStrsSizeKeyPubl(frmParent, kstOpen, strsAliasPKTC);
        if (strsSizeKeyPublPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsSizeKeyPublPKTC");
        }
        String[] strsTypeCertPKTC = UtilKstAbs.s_getStrsTypeCertificatePKTC(frmParent, kstOpen, strsAliasPKTC);
        if (strsTypeCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsTypeCertPKTC");
        }
        String[] strsAlgoSigCertPKTC = UtilKstAbs.s_getStrsAlgoSigCertPKTC(frmParent, kstOpen, strsAliasPKTC);
        if (strsAlgoSigCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAlgoSigCertPKTC");
        }
        Date[] dtesLastModifiedPKTC = UtilKstAbs.s_getDtesLastModified(frmParent, kstOpen, strsAliasPKTC);
        if (dtesLastModifiedPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil dtesLastModifiedPKTC");
        }
        Date[] dtesLastModifiedSK = UtilKstAbs.s_getDtesLastModified(frmParent, kstOpen, strsAliasSK);
        if (dtesLastModifiedSK == null) {
            MySystem.s_printOutExit(strMethod, "nil dtesLastModifiedPKTC");
        }
        DTblsKstManage dlg = new DTblsKstManage(frmParent, kstOpen, strPathAbsOpenKst, chrsPasswdOpenKst, strKeystoreProvider);
        if (!dlg.init()) MySystem.s_printOutExit(strMethod, "failed");
        if (!dlg.load(strsAliasPKTC, boosIsTCEntryPKTC, boosValidDatePKTC, boosSelfSignedCertPKTC, boosTrustedCertPKTC, strsSizeKeyPublPKTC, strsTypeCertPKTC, strsAlgoSigCertPKTC, dtesLastModifiedPKTC, strsAliasSK, dtesLastModifiedSK)) {
            MySystem.s_printOutExit(strMethod, "failed");
        }
        dlg.setVisible(true);
    }

    protected static void _s_showKstOpen_(Frame frmParent, KeyStore kstOpen, String strPathAbs) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_showKstOpen_(...)";
        if (kstOpen == null) MySystem.s_printOutExit(strMethod, "nil arg");
        String[] strsAliasPKTC = UtilKstAbs.s_getStrsAliasPKTC(frmParent, kstOpen);
        if (strsAliasPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAliasPKTC");
        }
        String[] strsAliasSK = UtilKstAbs.s_getStrsAliasSK(frmParent, kstOpen);
        if (strsAliasSK == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAliasPKTC");
        }
        Boolean[] boosIsTCEntryPKTC = UtilKstAbs.s_getBoosEntryTcr(frmParent, kstOpen, strsAliasPKTC);
        if (boosIsTCEntryPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosIsTCEntryPKTC");
        }
        Boolean[] boosValidDatePKTC = UtilKstAbs.s_getBoosValidDatePKTC(frmParent, kstOpen, strsAliasPKTC);
        if (boosValidDatePKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosValidDatePKTC");
        }
        Boolean[] boosSelfSignedCertPKTC = UtilKstAbs.s_getBoosSelfSigned(frmParent, kstOpen, strsAliasPKTC);
        if (boosSelfSignedCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosSelfSignedCertPKTC");
        }
        Boolean[] boosTrustedCertPKTC = UtilKstAbs.s_getBoosTrusted(frmParent, kstOpen, strsAliasPKTC);
        if (boosTrustedCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil boosTrustedCertPKTC");
        }
        String[] strsSizeKeyPublPKTC = UtilKstAbs.s_getStrsSizeKeyPubl(frmParent, kstOpen, strsAliasPKTC);
        if (strsSizeKeyPublPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsSizeKeyPublPKTC");
        }
        String[] strsTypeCertPKTC = UtilKstAbs.s_getStrsTypeCertificatePKTC(frmParent, kstOpen, strsAliasPKTC);
        if (strsTypeCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsTypeCertPKTC");
        }
        String[] strsAlgoSigCertPKTC = UtilKstAbs.s_getStrsAlgoSigCertPKTC(frmParent, kstOpen, strsAliasPKTC);
        if (strsAlgoSigCertPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil strsAlgoSigCertPKTC");
        }
        Date[] dtesLastModifiedPKTC = UtilKstAbs.s_getDtesLastModified(frmParent, kstOpen, strsAliasPKTC);
        if (dtesLastModifiedPKTC == null) {
            MySystem.s_printOutExit(strMethod, "nil dtesLastModifiedPKTC");
        }
        Date[] dtesLastModifiedSK = UtilKstAbs.s_getDtesLastModified(frmParent, kstOpen, strsAliasSK);
        if (dtesLastModifiedSK == null) {
            MySystem.s_printOutExit(strMethod, "nil dtesLastModifiedPKTC");
        }
        DTblsKstView dlg = new DTblsKstView(frmParent, kstOpen, strPathAbs);
        if (!dlg.init()) MySystem.s_printOutExit(strMethod, "failed");
        if (!dlg.load(strsAliasPKTC, boosIsTCEntryPKTC, boosValidDatePKTC, boosSelfSignedCertPKTC, boosTrustedCertPKTC, strsSizeKeyPublPKTC, strsTypeCertPKTC, strsAlgoSigCertPKTC, dtesLastModifiedPKTC, strsAliasSK, dtesLastModifiedSK)) {
            MySystem.s_printOutExit(strMethod, "failed");
        }
        dlg.setVisible(true);
    }

    private static boolean _s_isTrustedCertsChain(Certificate[] crtsChain2check, HashSet<Certificate> hstCrtTrusted) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_isTrustedCertsChain(...)";
        if (crtsChain2check == null || hstCrtTrusted == null) MySystem.s_printOutExit(strMethod, "nil arg");
        if (crtsChain2check.length < 1) return false;
        for (int i = 0; i < crtsChain2check.length; i++) {
            if (crtsChain2check[i] == null) MySystem.s_printOutExit(strMethod, "nil crtsChain2check[i], i=" + i);
            if (UtilKstAbs._s_isTrustedCert(crtsChain2check[i], hstCrtTrusted)) return true;
        }
        return false;
    }

    private static boolean _s_isTrustedCert(Certificate crt2check, HashSet<Certificate> hstCrtTrusted) {
        String strMethod = UtilKstAbs._f_s_strClass + "." + "_s_isTrustedCert(...)";
        if (crt2check == null || hstCrtTrusted == null) MySystem.s_printOutExit(strMethod, "nil arg");
        for (Iterator itr = hstCrtTrusted.iterator(); itr.hasNext(); ) {
            Certificate crtCurTrusted = (Certificate) itr.next();
            if (crtCurTrusted == null) MySystem.s_printOutExit(strMethod, "nil crtCurTrusted");
            if (UtilCrt.s_isSignedBy(crt2check, crtCurTrusted)) return true;
        }
        return false;
    }

    static void _s_manageKstOpen_(Frame frmParent, KeyStore kstOpen, String string) {
    }
}
