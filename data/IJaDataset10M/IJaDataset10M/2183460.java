package com.google.code.p.keytooliui.shared.swing.table;

import java.awt.*;

/**
    not final, in order to inherit method:
        public Component getTableCellRendererComponent(...)
**/
public class TCRData extends TCRAbs {

    public static final Color f_s_colFgDefault = Color.black;

    public static final Color f_s_colBgDefault = new Color(255, 255, 255);

    public TCRData() {
        setForeground(TCRData.f_s_colFgDefault);
        setBackground(TCRData.f_s_colBgDefault);
    }
}
