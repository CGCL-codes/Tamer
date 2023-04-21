package net.ar.webonswing.own.adapters;

import javax.swing.*;
import net.ar.guia.own.interfaces.*;

public class JCheckBoxAdapter extends JButtonAdapter implements CheckBoxComponent {

    public JCheckBoxAdapter() {
        setSwingComponentClass(JCheckBox.class);
    }

    public JCheckBoxAdapter(JComponent aSwingButton) {
        super(aSwingButton);
        setSwingComponentClass(JCheckBox.class);
    }
}
