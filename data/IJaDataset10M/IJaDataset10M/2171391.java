package com.jtattoo.plaf;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.MetalToolTipUI;

/**
 * @author Michael Hagen
 */
public class BaseToolTipUI extends MetalToolTipUI {

    public static ComponentUI createUI(JComponent c) {
        return new BaseToolTipUI();
    }
}
