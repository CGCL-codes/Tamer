package com.google.code.p.keytooliui.ktl.swing.button;

import java.awt.event.*;

public final class RBTypeJarApk extends RBTypeJarAbs {

    public static final String f_s_strDesc = "APK";

    public static final String[] f_s_strsFileExt = { com.google.code.p.keytooliui.shared.io.S_FileExtension.f_s_strAPKDocument };

    public RBTypeJarApk(boolean blnIsEnabled, ItemListener itmListenerParent) {
        super(blnIsEnabled, itmListenerParent, RBTypeJarApk.f_s_strDesc, RBTypeJarApk.f_s_strsFileExt);
    }
}
