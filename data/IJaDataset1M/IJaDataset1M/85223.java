package net.sourceforge.pyrus.hal.mobject;

import javax.swing.JComponent;
import net.sourceforge.pyrus.hal.MObject;

public interface GuiDevice extends MObject {

    public JComponent getComponent();
}
