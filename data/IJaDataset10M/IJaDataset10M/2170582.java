package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.awt.*;

public abstract class KTLKprOpenSigDetAbs extends KTLKprOpenAbs {

    protected String _strPathAbsFileData_ = null;

    protected String _strPathAbsFileSig_ = null;

    protected String _strPathAbsFileSaveCrt_ = null;

    protected KTLKprOpenSigDetAbs(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, String strPathAbsFileData, String strPathAbsFileSig, String strPathAbsFileCrt, String strProviderKst) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, strProviderKst);
        this._strPathAbsFileData_ = strPathAbsFileData;
        this._strPathAbsFileSig_ = strPathAbsFileSig;
        this._strPathAbsFileSaveCrt_ = strPathAbsFileCrt;
    }
}
