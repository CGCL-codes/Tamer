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

public final class PSelBtnTfdFileSaveCsr extends PSelBtnTfdFileSaveAbs {

    public static final String f_s_strDocPropVal = "select_file_csr_save";

    public static final String s_strDirNameDefault = "mycsrs";

    public static final String f_s_strLabel = "CSR file:";

    public String getSelectedFormatFile() {
        if (this._btnTypeFileCsrPkcs10 != null) if (this._btnTypeFileCsrPkcs10.isSelected()) return this._btnTypeFileCsrPkcs10.getFormatFile();
        return null;
    }

    public void destroy() {
        super.destroy();
        if (this._btnTypeFileCsrPkcs10 != null) {
            this._btnTypeFileCsrPkcs10.destroy();
            this._btnTypeFileCsrPkcs10 = null;
        }
    }

    public boolean init() {
        String strMethod = "init()";
        if (!super.init()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        if (!this._btnTypeFileCsrPkcs10.init()) return false;
        if (!_addGroup()) {
            MySystem.s_printOutError(this, strMethod, "failed");
            return false;
        }
        return true;
    }

    public PSelBtnTfdFileSaveCsr(javax.swing.event.DocumentListener docListenerParent, Frame frmParent, ItemListener itmListenerParent) {
        super(docListenerParent, frmParent, PSelBtnTfdFileSaveCsr.f_s_strLabel, true);
        super._tfdCurSelection_.getDocument().putProperty(com.google.code.p.keytooliui.shared.swing.textfield.TFAbstract.f_s_strDocPropKey, (Object) PSelBtnTfdFileSaveCsr.f_s_strDocPropVal);
        this._btnTypeFileCsrPkcs10 = new RBTypeCsrPkcs10(false, itmListenerParent);
        this._btnTypeFileCsrPkcs10.addItemListener(this);
    }

    protected void _showDialog_() {
        String strMethod = "_showDialog_()";
        String[] strsTypeFileCsrCur = _getTypeFileCsrCur();
        if (strsTypeFileCsrCur == null) MySystem.s_printOutExit(this, strMethod, "nil strsTypeFileCsrCur");
        String strFileDesc = _getDescFileCsrCur();
        if (strFileDesc == null) MySystem.s_printOutExit(this, strMethod, "nil strFileDesc");
        File fle = null;
        String strButtonTextOk = "Save file";
        fle = S_FileChooserUI.s_getSaveFile(super._frmParent_, strButtonTextOk, strsTypeFileCsrCur, strFileDesc, com.google.code.p.keytooliui.ktl.io.S_FileExtensionUI.f_s_strDirNameDefaultCsr);
        if (fle == null) {
            return;
        }
        if (!_assignValues(fle)) MySystem.s_printOutExit(this, strMethod, "failed, fle.getName()=" + fle.getName());
    }

    private RBTypeCsrAbs _btnTypeFileCsrPkcs10 = null;

    private boolean _assignValues(File fle) {
        String strMethod = "_assignValues(fle)";
        if (fle == null) {
            MySystem.s_printOutError(this, strMethod, "nil fle");
            return false;
        }
        if (super._tfdCurSelection_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._tfdCurSelection_");
            return false;
        }
        super._tfdCurSelection_.setText(fle.getAbsolutePath());
        super._setSelectedValue_(true);
        if (super._btnClearSelection_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._btnClearSelection_");
            return false;
        }
        super._btnClearSelection_.setEnabled(true);
        return true;
    }

    /**
        grouping  PKCS10-[XXX]-[XXX] files
    **/
    private boolean _addGroup() {
        String strMethod = "_addGroup()";
        if (this._btnTypeFileCsrPkcs10 == null) {
            MySystem.s_printOutError(this, strMethod, "nil this._btnTypeFileCsrPkcs10");
            return false;
        }
        ButtonGroup bgp = new ButtonGroup();
        bgp.add(this._btnTypeFileCsrPkcs10);
        this._btnTypeFileCsrPkcs10.setSelected(true);
        JPanel pnlTypeFileCsr = new JPanel();
        pnlTypeFileCsr.setLayout(new BoxLayout(pnlTypeFileCsr, BoxLayout.Y_AXIS));
        pnlTypeFileCsr.add(this._btnTypeFileCsrPkcs10);
        if (super._pnl_ == null) {
            MySystem.s_printOutError(this, strMethod, "nil super._pnl_");
            return false;
        }
        super._pnl_.add(pnlTypeFileCsr);
        return true;
    }

    private String[] _getTypeFileCsrCur() {
        String strMethod = "_getTypeFileCsrCur()";
        if (this._btnTypeFileCsrPkcs10.isSelected()) {
            return this._btnTypeFileCsrPkcs10.getNamesFileExtension();
        }
        MySystem.s_printOutError(this, strMethod, "failed");
        return null;
    }

    private String _getDescFileCsrCur() {
        String strMethod = "_getDescFileCsrCur()";
        if (this._btnTypeFileCsrPkcs10.isSelected()) {
            return this._btnTypeFileCsrPkcs10.getFileDesc();
        }
        MySystem.s_printOutError(this, strMethod, "failed");
        return null;
    }
}
