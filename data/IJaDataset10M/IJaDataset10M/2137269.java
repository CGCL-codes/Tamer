package com.jme3.gde.assetpack.project.wizards;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

public class ImportWizardPanel2 implements WizardDescriptor.Panel {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private ImportVisualPanel2 component;

    public Component getComponent() {
        if (component == null) {
            component = new ImportVisualPanel2();
        }
        return component;
    }

    public HelpCtx getHelp() {
        return new HelpCtx("sdk.asset_packs");
    }

    public boolean isValid() {
        return true;
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }

    public void readSettings(Object settings) {
        component.loadSettings((WizardDescriptor) settings);
    }

    public void storeSettings(Object settings) {
        component.applySettings((WizardDescriptor) settings);
    }
}
