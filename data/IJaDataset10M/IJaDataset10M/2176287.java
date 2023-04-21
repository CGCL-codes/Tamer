package org.gvsig.remotesensing;

import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.ILayerState;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.extensionPoints.ExtensionPoint;
import org.gvsig.remotesensing.profiles.gui.ProfileDialog;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;

public class ProfileImageExtension extends AbstractTocContextMenuAction implements IGenericToolBarMenuItem {

    private static ProfileImageExtension singleton = null;

    public static ProfileImageExtension getSingleton() {
        if (singleton == null) singleton = new ProfileImageExtension();
        return singleton;
    }

    public void initialize() {
        ExtensionPoint extensionPoint = ExtensionPoint.getExtensionPoint("GenericToolBarMenu");
        extensionPoint.register("profile", this);
    }

    public void execute(String actionCommand) {
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
            for (int i = 0; i < layers.getLayersCount(); i++) if (layers.getLayer(i) instanceof FLyrRasterSE) {
                return true;
            }
        }
        return false;
    }

    public boolean isVisible() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f instanceof View) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapa = model.getMapContext();
            return mapa.getLayers().getLayersCount() > 0;
        } else {
            return false;
        }
    }

    public void execute(ITocItem item, FLayer[] selectedItems) {
        FLayer fLayer = null;
        com.iver.andami.ui.mdiManager.IWindow activeWindow = PluginServices.getMDIManager().getActiveWindow();
        if (selectedItems.length != 1) return;
        fLayer = selectedItems[0];
        if (!(fLayer instanceof FLyrRasterSE)) return;
        if (((FLyrRasterSE) fLayer).getBandCount() < 2) {
            JOptionPane.showMessageDialog(null, PluginServices.getText(this, "ext_rs_is_not_a_multiband_layer"));
            return;
        }
        ProfileDialog pcPanel = new ProfileDialog(520, 350, ((View) activeWindow), (FLyrRasterSE) fLayer);
        PluginServices.getMDIManager().addWindow(pcPanel);
    }

    public String getGroup() {
        return "RasterLayer";
    }

    public Icon getIcon() {
        return PluginServices.getIconTheme().get("profile-icon");
    }

    public int getOrder() {
        return 0;
    }

    public String getText() {
        return PluginServices.getText(this, "profile");
    }

    public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
        if ((selectedItems == null) || (selectedItems.length != 1)) return false;
        if (!(selectedItems[0] instanceof ILayerState)) return false;
        if (!((ILayerState) selectedItems[0]).isOpen()) return false;
        return true;
    }

    public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
        if ((selectedItems == null) || (selectedItems.length != 1)) return false;
        if (!(selectedItems[0] instanceof FLyrRasterSE)) return false;
        return true;
    }

    public int getGroupOrder() {
        return 55;
    }
}
