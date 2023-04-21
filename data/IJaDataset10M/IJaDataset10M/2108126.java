package com.iver.cit.gvsig;

import java.security.KeyException;
import org.apache.log4j.Logger;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.layers.FLayerVectorialDB;
import com.iver.utiles.extensionPoints.ExtensionPoint;
import com.iver.utiles.extensionPoints.ExtensionPoints;
import com.iver.utiles.extensionPoints.ExtensionPointsSingleton;
import com.prodevelop.cit.gvsig.vectorialdb.wizard.WizardVectorialDB;

/**
 * This extension adds the export-to-oracle button.
 *
 * @author jldominguez
 *
 */
public class ExtDB_Spatial extends Extension {

    private static Logger logger = Logger.getLogger(ExtDB_Spatial.class.getName());

    public void initialize() {
        AddLayer.addWizard(WizardVectorialDB.class);
        ExtensionPoints extensionPoints = ExtensionPointsSingleton.getInstance();
        extensionPoints.add("Layers", FLayerVectorialDB.class.getName(), FLayerVectorialDB.class);
        try {
            ((ExtensionPoint) extensionPoints.get("Layers")).addAlias(FLayerVectorialDB.class.getName(), "VectorialDB");
        } catch (KeyException e) {
            e.printStackTrace();
        }
    }

    public void execute(String actionCommand) {
    }

    public boolean isEnabled() {
        return isVisible();
    }

    /**
     * Is visible when there is one vector layer selected
     */
    public boolean isVisible() {
        return false;
    }
}
