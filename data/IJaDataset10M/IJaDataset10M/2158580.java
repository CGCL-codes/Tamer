package com.dukesoftware.utils.swing.lookandfeel;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

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
public class MyScrollPaneUI extends BasicScrollPaneUI {

    private static final Object singleton = new MyScrollPaneUI();

    private MyScrollPaneUI() {
        super();
    }

    public static String getCanonicalName() {
        return singleton.getClass().getCanonicalName();
    }

    public static ComponentUI createUI(JComponent c) {
        c.setForeground(OngakuMusouLookAndFeel.FOREGROUND);
        c.setBackground(OngakuMusouLookAndFeel.BACKGROUND);
        return new MyScrollPaneUI();
    }
}
