package com.jme3.gde.terraineditor.sky;

import com.jme3.asset.TextureKey;
import com.jme3.gde.core.sceneexplorer.nodes.actions.AbstractNewSpatialWizardAction;
import com.jme3.gde.core.sceneexplorer.nodes.actions.NewSpatialAction;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.awt.Component;
import java.awt.Dialog;
import java.text.MessageFormat;
import javax.swing.JComponent;
import org.openide.DialogDisplayer;
import org.openide.WizardDescriptor;

/**
 *
 * @author normenhansen
 */
@org.openide.util.lookup.ServiceProvider(service = NewSpatialAction.class)
public class AddSkyboxAction extends AbstractNewSpatialWizardAction {

    private WizardDescriptor.Panel[] panels;

    public AddSkyboxAction() {
        name = "Skybox..";
    }

    @Override
    protected Object showWizard(org.openide.nodes.Node node) {
        WizardDescriptor wizardDescriptor = new WizardDescriptor(getPanels());
        wizardDescriptor.setTitleFormat(new MessageFormat("{0}"));
        wizardDescriptor.setTitle("Skybox Wizard");
        Dialog dialog = DialogDisplayer.getDefault().createDialog(wizardDescriptor);
        dialog.setVisible(true);
        dialog.toFront();
        boolean cancelled = wizardDescriptor.getValue() != WizardDescriptor.FINISH_OPTION;
        if (!cancelled) {
            return wizardDescriptor;
        }
        return null;
    }

    @Override
    protected Spatial doCreateSpatial(Node parent, Object properties) {
        if (properties != null) {
            return generateSkybox((WizardDescriptor) properties);
        }
        return null;
    }

    private Spatial generateSkybox(WizardDescriptor wiz) {
        if ((Boolean) wiz.getProperty("multipleTextures")) {
            Texture south = (Texture) wiz.getProperty("textureSouth");
            Texture north = (Texture) wiz.getProperty("textureNorth");
            Texture east = (Texture) wiz.getProperty("textureEast");
            Texture west = (Texture) wiz.getProperty("textureWest");
            Texture top = (Texture) wiz.getProperty("textureTop");
            Texture bottom = (Texture) wiz.getProperty("textureBottom");
            Vector3f normalScale = (Vector3f) wiz.getProperty("normalScale");
            return SkyFactory.createSky(pm, west, east, north, south, top, bottom, normalScale);
        } else {
            Texture textureSingle = (Texture) wiz.getProperty("textureSingle");
            Vector3f normalScale = (Vector3f) wiz.getProperty("normalScale");
            boolean useSpheremap = (Boolean) wiz.getProperty("useSpheremap");
            boolean flipY = (Boolean) wiz.getProperty("flipY");
            TextureKey key = (TextureKey) textureSingle.getKey();
            TextureKey newKey = new TextureKey(key.getName(), flipY);
            newKey.setGenerateMips(true);
            newKey.setAsCube(!useSpheremap);
            return SkyFactory.createSky(pm, pm.loadTexture(newKey), normalScale, useSpheremap);
        }
    }

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] { new SkyboxWizardPanel1(), new SkyboxWizardPanel2() };
            String[] steps = new String[panels.length];
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                steps[i] = c.getName();
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }
}
