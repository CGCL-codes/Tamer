package com.incors.plaf.kunststoff;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

public class KunststoffCheckBoxUI extends MetalCheckBoxUI {

    private static final KunststoffCheckBoxUI checkBoxUI = new KunststoffCheckBoxUI();

    public KunststoffCheckBoxUI() {
        icon = new KunststoffCheckBoxIcon();
    }

    public static ComponentUI createUI(JComponent b) {
        return checkBoxUI;
    }

    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        icon = new KunststoffCheckBoxIcon();
    }
}
