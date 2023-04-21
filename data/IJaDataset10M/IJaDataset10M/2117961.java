package com.iver.gvsig.datalocator;

import java.util.prefs.Preferences;
import com.hardcode.gdbms.engine.data.DataSource;
import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayersIterator;
import com.iver.cit.gvsig.fmap.layers.layerOperations.LayerCollection;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.gvsig.datalocator.gui.DataSelectionPanel;

/**
 * The DataLocatorExtension class allows to make a quick zoom based on an
 * alphanumeric attribute.
 *
 * @author jmorell
 */
public class DataLocatorExtension extends Extension {

    IWindow iWDataSelection = null;

    IWindow previousView = null;

    public void initialize() {
        registerIcons();
    }

    private void registerIcons() {
        PluginServices.getIconTheme().registerDefault("view-locator", this.getClass().getClassLoader().getResource("images/locator.png"));
    }

    public void execute(String actionCommand) {
        View vista = (View) PluginServices.getMDIManager().getActiveWindow();
        MapContext mapContext = vista.getModel().getMapContext();
        DataSelectionPanel dataSelectionPanel = new DataSelectionPanel(mapContext);
        WindowInfo vi = dataSelectionPanel.getWindowInfo();
        vi.setX(Preferences.userRoot().getInt("gvSIG.DataLocator.x", vi.getX()));
        vi.setY(Preferences.userRoot().getInt("gvSIG.DataLocator.y", vi.getY()));
        PluginServices.getMDIManager().addWindow(dataSelectionPanel);
        iWDataSelection = dataSelectionPanel;
        iWDataSelection.getWindowInfo();
    }

    public boolean isEnabled() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f.getClass() == View.class) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapContext = model.getMapContext();
            if (mapContext.getLayers().getLayersCount() > 0) {
                LayersIterator iter = newValidLayersIterator(mapContext.getLayers());
                if (!iter.hasNext()) {
                    return false;
                }
            } else {
                return false;
            }
            if (iWDataSelection == null || (PluginServices.getMDIManager().getWindowInfo(iWDataSelection).isClosed() && f != previousView)) {
                int userOpen = Preferences.userRoot().getInt("gvSIG.DataLocator.open_first_time", -1);
                if (userOpen == 1) {
                    String layerName = Preferences.userRoot().get("LAYERNAME_FOR_DATA_LOCATION", "");
                    FLayer lyr = mapContext.getLayers().getLayer(layerName);
                    if (lyr != null) {
                        DataSelectionPanel dataSelectionPanel = new DataSelectionPanel(mapContext);
                        WindowInfo vi = dataSelectionPanel.getWindowInfo();
                        vi.setX(Preferences.userRoot().getInt("gvSIG.DataLocator.x", vi.getX()));
                        vi.setY(Preferences.userRoot().getInt("gvSIG.DataLocator.y", vi.getY()));
                        PluginServices.getMDIManager().addWindow(dataSelectionPanel);
                        iWDataSelection = dataSelectionPanel;
                        iWDataSelection.getWindowInfo();
                    }
                }
            }
            previousView = f;
        }
        return true;
    }

    public boolean isVisible() {
        com.iver.andami.ui.mdiManager.IWindow f = PluginServices.getMDIManager().getActiveWindow();
        if (f == null) {
            return false;
        }
        if (f.getClass() == View.class) {
            View vista = (View) f;
            IProjectView model = vista.getModel();
            MapContext mapContext = model.getMapContext();
            return mapContext.getLayers().getLayersCount() > 0;
        } else {
            return false;
        }
    }

    public static LayersIterator newValidLayersIterator(LayerCollection layer) {
        return new LayersIterator((FLayer) layer) {

            public boolean evaluate(FLayer layer) {
                if (!(layer instanceof FLyrVect)) return false;
                DataSource ds;
                try {
                    ds = ((FLyrVect) layer).getRecordset();
                    if (ds.getFieldCount() < 1) return false;
                } catch (Exception e) {
                    return false;
                }
                return true;
            }
        };
    }
}
