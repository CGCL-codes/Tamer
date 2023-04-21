package com.iver.cit.gvsig;

import org.apache.log4j.Logger;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

/**
 * Extensi�n para gestionar los hiperlinks.
 *
 * @author Vicente Caballero Navarro
 */
public class LinkControls extends Extension {

    private static Logger logger = Logger.getLogger(LinkControls.class.getName());

    /**
	 * @see com.iver.andami.plugins.IExtension#execute(java.lang.String)
	 */
    public void execute(String s) {
        View vista = (View) PluginServices.getMDIManager().getActiveWindow();
        MapControl mapCtrl = vista.getMapControl();
        logger.debug("Comand : " + s);
        if (s.compareTo("LINK") == 0) {
            mapCtrl.setTool("link");
        }
    }

    /**
	 * @see com.iver.mdiApp.plugins.IExtension#isVisible()
	 */
    public boolean isVisible() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f instanceof View) {
            MapContext mapa = ((View) f).getModel().getMapContext();
            return mapa.getLayers().getLayersCount() > 0;
        } else {
            return false;
        }
    }

    /**
	 * @see com.iver.andami.plugins.IExtension#isEnabled()
	 */
    public boolean isEnabled() {
        View f = (View) PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f instanceof com.iver.cit.gvsig.project.documents.view.gui.View) {
            com.iver.cit.gvsig.project.documents.view.gui.View view = (com.iver.cit.gvsig.project.documents.view.gui.View) f;
            IProjectView model = view.getModel();
            Boolean p = null;
            FLayer[] activas = model.getMapContext().getLayers().getActives();
            if (activas.length == 0) return false;
            for (int i = 0; i < activas.length; i++) {
                if (!activas[i].isAvailable()) {
                    return false;
                }
                if (!activas[i].allowLinks()) return false;
                if (activas[i].getLinkProperties() == null || activas[i].getLinkProperties().getField() == null) return false;
            }
        }
        return true;
    }

    /**
	 * @see com.iver.andami.plugins.IExtension#initialize()
	 */
    public void initialize() {
        registerIcons();
    }

    private void registerIcons() {
        PluginServices.getIconTheme().registerDefault("view-query-link", this.getClass().getClassLoader().getResource("images/Link.png"));
    }
}
