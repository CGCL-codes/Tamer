package com.google.code.p.keytooliui.ktl.swing.panel;

import com.google.code.p.keytooliui.ktl.io.*;
import com.google.code.p.keytooliui.ktl.swing.button.*;
import com.google.code.p.keytooliui.shared.swing.button.*;
import com.google.code.p.keytooliui.shared.swing.optionpane.*;
import com.google.code.p.keytooliui.shared.swing.textfield.*;
import com.google.code.p.keytooliui.shared.lang.*;
import com.google.code.p.keytooliui.shared.io.*;
import com.google.code.p.keytooliui.shared.swing.panel.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class PSelBtnTfdFileOpenAnyDir extends PSelBtnTfdFileOpenAbs {

    public static final String f_s_strDocPropVal = "select_file_any_open_dir";

    public PSelBtnTfdFileOpenAnyDir(javax.swing.event.DocumentListener docListenerParent, Frame frmParent, ItemListener itmListenerParent, String strLabel) {
        super(docListenerParent, frmParent, strLabel, true);
        super._tfdCurSelection_.getDocument().putProperty(com.google.code.p.keytooliui.shared.swing.textfield.TFAbstract.f_s_strDocPropKey, (Object) PSelBtnTfdFileOpenAnyDir.f_s_strDocPropVal);
    }

    protected void _showDialog_() {
        String strMethod = "_showDialog_()";
        String strButtonTextOk = "Open directory";
        File fle = S_FileChooserAbs.s_getOpenDir(super._frmParent_, strButtonTextOk);
        if (fle == null) {
            return;
        }
        if (!_assignValues(fle)) MySystem.s_printOutExit(this, strMethod, "failed, fle.getName()=" + fle.getName());
    }

    protected boolean _enableButtonsSelectionDone_() {
        String strMethod = "_enableButtonsSelectionDone_()";
        if (super._btnClearSelection_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._btnClearSelection_");
            return false;
        }
        super._btnClearSelection_.setEnabled(true);
        return true;
    }

    private boolean _assignValues(File fle) {
        String strMethod = "_assignValues(fle)";
        if (fle == null) {
            MySystem.s_printOutError(this, strMethod, "nil fle");
            return false;
        }
        if (!fle.exists()) {
            MySystem.s_printOutWarning(this, strMethod, "! fle.exists(), fle.getAbsolutePath()=" + fle.getAbsolutePath());
            String strBody = fle.getAbsolutePath();
            strBody += ":\nDirectory not found.";
            OPAbstract.s_showDialogWarning(super._frmParent_, strBody);
            return true;
        }
        if (!fle.isDirectory()) {
            MySystem.s_printOutWarning(this, strMethod, "! fle.isDirectory(), fle.getAbsolutePath()=" + fle.getAbsolutePath());
            String strBody = fle.getAbsolutePath();
            strBody += ":\n  not a directory.";
            OPAbstract.s_showDialogWarning(super._frmParent_, strBody);
            return true;
        }
        if (super._tfdCurSelection_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._tfdCurSelection_");
            return false;
        }
        super._tfdCurSelection_.setText(fle.getAbsolutePath());
        super._setSelectedValue_(true);
        if (!_enableButtonsSelectionDone_()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        return true;
    }
}
