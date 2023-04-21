package com.google.code.p.keytooliui.ktl.lang.bool;

import javax.swing.ImageIcon;
import com.google.code.p.keytooliui.ktl.swing.imageicon.S_IINUI;
import com.google.code.p.keytooliui.shared.lang.MySystem;
import com.google.code.p.keytooliui.shared.lang.bool.*;

public class BOEntrySK extends BOAbs {

    public static final String STR_IMAGE = "entry_sk.gif";

    public boolean init() {
        return true;
    }

    public void destroy() {
    }

    public BOEntrySK() {
        this._iin = S_IINUI.s_get(BOEntrySK.STR_IMAGE);
        String strMethod = "BOEntrySK(...)";
        if (this._iin == null) MySystem.s_printOutExit(this, strMethod, "this._iin == null, BOEntrySK.STR_IMAGE=" + BOEntrySK.STR_IMAGE);
    }

    public ImageIcon getIcon() {
        return this._iin;
    }

    public String getText() {
        return null;
    }

    private ImageIcon _iin = null;
}
