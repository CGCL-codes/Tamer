package com.google.code.p.keytooliui.ktl.swing.button;

import java.awt.event.*;

public abstract class RBTypeKstAbs extends RBTypeAbs {

    public String getFormatFile() {
        return this._strFormat;
    }

    protected RBTypeKstAbs(boolean blnIsEnabled, ItemListener itmListenerParent, String strFormat, String[] strsExtension) {
        super(blnIsEnabled, itmListenerParent, strFormat, strsExtension);
        this._strFormat = strFormat;
    }

    private String _strFormat = null;
}
