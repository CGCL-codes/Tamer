package com.iver.gvsig.centerviewpoint;

import java.awt.Color;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.gvsig.centerviewtopoint.gui.InputCoordinatesPanel;

/**
 * The CenterViewToPointExtension class allows to center the View over a
 * concrete point given by its coordinates.
 *
 * @author jmorell
 */
public class CenterViewToPointExtension extends Extension {

    private View vista;

    public static Color COLOR = Color.red;

    public void initialize() {
        PluginServices.getIconTheme().registerDefault("view-center-to-point", this.getClass().getClassLoader().getResource("images/centerviewtopoint.png"));
    }

    public void execute(String actionCommand) {
        vista = (View) PluginServices.getMDIManager().getActiveWindow();
        MapContext mapContext = vista.getModel().getMapContext();
        InputCoordinatesPanel dataSelectionPanel = new InputCoordinatesPanel(mapContext);
        PluginServices.getMDIManager().addWindow(dataSelectionPanel);
    }

    public View getView() {
        return vista;
    }

    public boolean isEnabled() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f.getClass() == View.class) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapa = model.getMapContext();
            FLayers layers = mapa.getLayers();
            for (int i = 0; i < layers.getLayersCount(); i++) {
                if (layers.getLayer(i).isAvailable()) return true;
            }
        }
        return false;
    }

    public boolean isVisible() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f.getClass() == View.class) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapa = model.getMapContext();
            if (mapa.getLayers().getLayersCount() > 0) {
                return true;
            }
            return false;
        }
        return false;
    }
}
