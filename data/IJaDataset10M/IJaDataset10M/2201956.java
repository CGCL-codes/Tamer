package com.google.code.p.keytooliui.ktl.util.jarsigner;

import java.security.KeyStore;
import java.awt.*;
import java.io.*;

public final class KTLKprOpenKprOutUber extends KTLKprOpenKprOutKPAbs {

    public KTLKprOpenKprOutUber(Frame frmOwner, String strPathAbsOpenKst, char[] chrsPasswdOpenKst, String strPathAbsFileSaveKpr, String strPathAbsFileSaveCrts, String strFormatFileKpr, String strFormatFileCrts) {
        super(frmOwner, strPathAbsOpenKst, chrsPasswdOpenKst, strPathAbsFileSaveKpr, strPathAbsFileSaveCrts, strFormatFileKpr, strFormatFileCrts, KTLAbs.f_s_strProviderKstUber);
    }

    protected KeyStore _getKeystoreOpen_(File fleOpen) {
        return UtilKstUber.s_getKeystoreOpen(super._frmOwner_, fleOpen, super._chrsPasswdKst_);
    }
}
