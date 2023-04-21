package com.dukesoftware.utils.swing.lookandfeel;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;

/**
 * 
 * 
 * <p></p>
 * 
 * <h5>update history</h5>
 * <p>2007/05/19 File is created. </p>
 * 
 * 
 *
 *
 */
public class MyTableHeaderUI extends BasicTableHeaderUI {

    private static final Object singleton = new MyTableHeaderUI();

    private MyTableHeaderUI() {
        super();
    }

    public static String getCanonicalName() {
        return singleton.getClass().getCanonicalName();
    }

    public static ComponentUI createUI(JComponent c) {
        c.setForeground(OngakuMusouLookAndFeel.FOREGROUND);
        c.setBackground(Color.black);
        return new MyTableHeaderUI();
    }
}
