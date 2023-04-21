package org.gvsig.rastertools.geolocation;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.fmap.raster.layers.IRasterLayerActions;
import org.gvsig.raster.gui.IGenericToolBarMenuItem;
import org.gvsig.raster.util.RasterToolsUtil;
import org.gvsig.rastertools.geolocation.behavior.GeoRasterBehavior;
import org.gvsig.rastertools.geolocation.listener.GeorefPanListener;
import org.gvsig.rastertools.geolocation.ui.GeoLocationDialog;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.tools.CompoundBehavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.fmap.tools.Behavior.MouseMovementBehavior;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.toc.AbstractTocContextMenuAction;
import com.iver.cit.gvsig.project.documents.view.toc.ITocItem;
import com.iver.cit.gvsig.project.documents.view.toolListeners.StatusBarListener;

/**
 * Herramienta del men� contextual que carga el raster en el localizador para tener una visi�n general de
 * esta y carga el zoom del cursor para tener una selecci�n de precisi�n.
 *
 * 16-jun-2007
 * @author Nacho Brodin (nachobrodin@gmail.com)
 */
public class GeoLocationTocMenuEntry extends AbstractTocContextMenuAction implements PropertyChangeListener, IGenericToolBarMenuItem {

    private static GeoLocationTocMenuEntry singleton = null;

    private GeoRasterBehavior mb = null;

    /**
	 * Nadie puede crear una instancia a esta clase �nica, hay que usar el
	 * getSingleton()
	 */
    private GeoLocationTocMenuEntry() {
    }

    /**
	 * Devuelve un objeto unico a dicha clase
	 * @return
	 */
    public static GeoLocationTocMenuEntry getSingleton() {
        if (singleton == null) singleton = new GeoLocationTocMenuEntry();
        return singleton;
    }

    public String getGroup() {
        return "GeoRaster";
    }

    public int getGroupOrder() {
        return 55;
    }

    public int getOrder() {
        return 4;
    }

    public String getText() {
        return RasterToolsUtil.getText(this, "geolocation");
    }

    public boolean isEnabled(ITocItem item, FLayer[] selectedItems) {
        return true;
    }

    public boolean isVisible(ITocItem item, FLayer[] selectedItems) {
        if ((selectedItems == null) || (selectedItems.length != 1)) return false;
        if (!(selectedItems[0] instanceof FLyrRasterSE)) return false;
        return ((FLyrRasterSE) selectedItems[0]).isActionEnabled(IRasterLayerActions.GEOLOCATION);
    }

    public void execute(ITocItem item, FLayer[] selectedItems) {
        if (selectedItems == null || selectedItems.length != 1 || !(selectedItems[0] instanceof FLyrRasterSE)) {
            RasterToolsUtil.messageBoxError(PluginServices.getText(this, "layers_not_selected"), null);
            return;
        }
        BaseView theView = null;
        GeoLocationDialog dialog = null;
        IWindow[] win = PluginServices.getMDIManager().getAllWindows();
        for (int i = 0; i < win.length; i++) {
            if (win[i] instanceof BaseView) {
                FLayers lyrs = ((BaseView) win[i]).getMapControl().getMapContext().getLayers();
                for (int j = 0; j < lyrs.getLayersCount(); j++) if (lyrs.getLayer(j).equals(selectedItems[0])) theView = (BaseView) win[i];
            }
            if (win[i] instanceof GeoLocationDialog) RasterToolsUtil.closeWindow(win[i]);
        }
        if (theView == null) {
            RasterToolsUtil.messageBoxError(PluginServices.getText(this, "view_not_found"), null);
            return;
        }
        MapControl mapCtrl = theView.getMapControl();
        StatusBarListener sbl = new StatusBarListener(mapCtrl);
        FLyrRasterSE lyr = (FLyrRasterSE) selectedItems[0];
        dialog = new GeoLocationDialog(lyr, mapCtrl.getViewPort(), theView);
        Point posit = RasterToolsUtil.iwindowPosition((int) dialog.getSizeWindow().getWidth(), (int) dialog.getSizeWindow().getHeight());
        dialog.setPosition((int) posit.getX(), (int) posit.getY());
        RasterToolsUtil.addWindow(dialog);
        dialog.init(mapCtrl);
        loadGeoPanListener(mapCtrl, sbl, dialog, lyr);
        mapCtrl.setTool("geoPan");
    }

    /**
	 * Carga el listener de selecci�n de raster en el MapControl.
	 */
    private void loadGeoPanListener(MapControl mapCtrl, StatusBarListener sbl, GeoLocationDialog gld, FLyrRasterSE lyr) {
        if (mapCtrl.getNamesMapTools().get("geoPan") == null) {
            GeorefPanListener pl = new GeorefPanListener(mapCtrl);
            mb = new GeoRasterBehavior(pl, gld, lyr);
            mapCtrl.addMapTool("geoPan", new Behavior[] { mb, new MouseMovementBehavior(sbl) });
        } else {
            Behavior b = mapCtrl.getMapTool("geoPan");
            if (b instanceof CompoundBehavior && ((CompoundBehavior) b).getBehavior(0) instanceof GeoRasterBehavior) {
                GeoRasterBehavior beh = (GeoRasterBehavior) ((CompoundBehavior) b).getBehavior(0);
                beh.setLayer(lyr);
                beh.setITransformIO(gld);
            }
        }
    }

    public Icon getIcon() {
        return RasterToolsUtil.getIcon("geolocalization-icon");
    }

    public void propertyChange(PropertyChangeEvent evt) {
    }
}
