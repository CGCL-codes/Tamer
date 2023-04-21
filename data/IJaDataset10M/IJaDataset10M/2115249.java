package com.jme3.gde.terraineditor;

import java.awt.Component;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 *
 * @author bowens
 */
public class CreateTerrainWizardPanel3 implements WizardDescriptor.Panel {

    /**
     * The visual component that displays this panel. If you need to access the
     * component from this class, just use getComponent().
     */
    private Component component;

    public Component getComponent() {
        if (component == null) {
            component = new CreateTerrainVisualPanel3();
        }
        return component;
    }

    public HelpCtx getHelp() {
        return HelpCtx.DEFAULT_HELP;
    }

    public boolean isValid() {
        return true;
    }

    public final void addChangeListener(ChangeListener l) {
    }

    public final void removeChangeListener(ChangeListener l) {
    }

    public void readSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;
        int totalSize = (Integer) wiz.getProperty("totalSize");
        CreateTerrainVisualPanel3 comp = (CreateTerrainVisualPanel3) getComponent();
        comp.setDefaultImageSize(totalSize - 1);
    }

    public void storeSettings(Object settings) {
        WizardDescriptor wiz = (WizardDescriptor) settings;
        CreateTerrainVisualPanel3 comp = (CreateTerrainVisualPanel3) getComponent();
        int textureSize = new Integer(comp.getAlphaBlendSize().getText());
        wiz.putProperty("alphaTextureSize", textureSize);
    }
}
