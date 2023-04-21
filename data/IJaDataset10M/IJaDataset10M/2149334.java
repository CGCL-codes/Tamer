package com.google.code.p.keytooliui.ktl.util.jarsigner;

import com.google.code.p.keytooliui.ktl.swing.dialog.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import com.google.code.p.keytooliui.shared.util.jarsigner.*;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.awt.*;
import java.io.*;
import java.util.*;

public final class KTLShkOpenIOOutUber extends KTLShkOpenIOOutKPAbs {

    public KTLShkOpenIOOutUber(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, String strPathAbsFileSaveIO, String strFormatFileShk) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, strPathAbsFileSaveIO, strFormatFileShk, KTLAbs.f_s_strProviderKstUber);
    }

    protected KeyStore _getKeystoreOpen_(File fleOpen) {
        return UtilKstUber.s_getKeystoreOpen(super._frmOwner_, fleOpen, super._chrsPasswdKst_);
    }
}
