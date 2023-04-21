package org.xlim.sic.ig.exploitation.impl;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class SavePathToXMLWizardPanel1 implements WizardDescriptor.Panel<WizardDescriptor> {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;

    public Component getComponent() {
        if (component == null) {
            component = new SavePathToXMLVisualPanel1();
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public boolean isValid() {
        return true;
    }

    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }

    public void readSettings(WizardDescriptor settings) {
        final SavePathToXMLVisualPanel1 panel = (SavePathToXMLVisualPanel1) getComponent();
        panel.setFileName((String) settings.getProperty("FileName"));
    }

    public void storeSettings(WizardDescriptor settings) {
        settings.putProperty("FileName", ((SavePathToXMLVisualPanel1) getComponent()).getFileName());
    }
}
