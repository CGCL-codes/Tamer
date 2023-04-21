package org.dinopolis.gpstool.plugin.mapmanager;

import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Vector;
import javax.swing.Icon;
import org.dinopolis.gpstool.gui.MouseMode;
import org.dinopolis.gpstool.hook.MapManagerHook;
import org.dinopolis.gpstool.map.MapInfo;
import org.dinopolis.util.Resources;
import com.bbn.openmap.LatLonPoint;

/**
 * The mouse mode for the map manager plugin allows to interactively
 * select available maps on the map component by clicking and dragging
 * the mouse.
 *
 * @author Christof Dallermassl
 * @version $Revision: 745 $
 */
public class MapManagerMouseMode implements MouseMode {

    boolean mode_active_ = false;

    Resources resources_;

    MapManagerLayer layer_;

    MapManagerHook map_manager_;

    MapManagerPlugin plugin_;

    public static final String KEY_MAPAMANGER_MOUSEMODE_NAME = "mapmanager.mousemode.name";

    public static final String KEY_MAPAMANGER_MOUSEMODE_DESCRIPTION = "mapmanager.mousemode.description";

    public static final String KEY_MAPAMANGER_MOUSEMODE_MNEMONIC = "mapmanager.mousemode.mnemonic";

    public static final String KEY_MAPAMANGER_MOUSEMODE_ACCELERATOR_KEY = "mapmanager.mousemode.accelerator_key";

    public static final String KEY_MAPAMANGER_MOUSEMODE_ICON = "mapmanager.mousemode.icon";

    /**
	 * Constructor for MapManagerMouseMode.
	 */
    public MapManagerMouseMode() {
    }

    /**
	 * Method initialize.
	 * @param plugin_resources the resources of the map manager plugin.
	 * @param layer the layer to draw to
	 */
    public void initialize(Resources plugin_resources, MapManagerLayer layer, MapManagerPlugin plugin, MapManagerHook map_manager) {
        resources_ = plugin_resources;
        layer_ = layer;
        plugin_ = plugin;
        map_manager_ = map_manager;
    }

    /**
	 * Called by the application to switch the mouse mode on or off. If
	 * the mouse mode is switched off, it must not react on mouse events
	 * (although it might register them). This method may be used to
	 * change the mouse cursor, ...
	 *
	 * @param active if <code>true</code> the mouse mode is switched on
	 * and should react on mouse events.
	 */
    public void setActive(boolean active) {
        mode_active_ = active;
    }

    /**
	 * Returns if the mouse mode is active or not.
	 *
	 * @return <code>true</code> if the mouse mode is active and reacts on
	 * mouse events.
	 */
    public boolean isActive() {
        return (mode_active_);
    }

    /**
	 * The name returned here is used in the menu and/or the toolbar of
	 * the application to switch the mouse mode on or off. It should be
	 * localized.
	 *
	 * @return the name of the mouse mode.
	 */
    public String getMouseModeName() {
        return (resources_.getString(KEY_MAPAMANGER_MOUSEMODE_NAME));
    }

    /**
	 * The description returned here is used in the menu and/or the toolbar of
	 * the application to switch the mouse mode on or off. 
	 *
	 * @return the description of the mouse mode.
	 */
    public String getMouseModeDescription() {
        return (resources_.getString(KEY_MAPAMANGER_MOUSEMODE_DESCRIPTION));
    }

    /**
	 * The icon returned here is used in the menu and/or the toolbar of
	 * the application to switch the mouse mode on or off. 
	 *
	 * @return the icon of the mouse mode.
	 */
    public Icon getMouseModeIcon() {
        return (null);
    }

    /**
	 * Returns the mnemonic character that is used for manual (keyboard)
	 * selection in a menu. If possible, it should be the first letter of
	 * the name (default).
	 *
	 * @return a string describing the mnemonic character for this mouse
	 * mode when used in a menu.
	 */
    public char getMouseModeMnemonic() {
        return (resources_.getString(KEY_MAPAMANGER_MOUSEMODE_MNEMONIC).charAt(0));
    }

    /**
	 * Returns the accelerator key that is used for the mouse mode in the
	 * menu or toolbar. The format of the key strings is described in
	 * {@link javax.swing.KeyStroke#getKeyStroke(java.lang.String)}. Some
	 * examples are given: <code>INSERT</code>,<code>controle
	 * DELETE</code>,<code>alt shift X</code>,<code>shift
	 * F</code>.
	 *
	 * @return a string describing the accelerator key.
	 */
    public String getMouseModeAcceleratorKey() {
        return (resources_.getString(KEY_MAPAMANGER_MOUSEMODE_ACCELERATOR_KEY));
    }

    /**
	 * Invoked when the mouse has been clicked on a component. Selects the maps
	 * from the map manager. Shift selects all maps (from smalles to largest
	 * scale), control adds the maps to previous selections.
	 *
	 * @param event the mouse event.
	 */
    public void mouseClicked(MouseEvent event) {
        if (!mode_active_) return;
        LatLonPoint coordinates = layer_.getProjection().inverse(event.getPoint());
        Collection map_infos;
        if (event.isShiftDown()) {
            map_infos = map_manager_.getMapInfos(coordinates.getLatitude(), coordinates.getLongitude());
        } else {
            map_infos = new Vector();
            MapInfo map_info = map_manager_.getBestMatchingMapInfo(coordinates.getLatitude(), coordinates.getLongitude());
            if (map_info != null) map_infos.add(map_info);
        }
        if (event.isControlDown()) plugin_.addMapSelection(map_infos); else plugin_.setMapSelection(map_infos);
    }

    /**
	 * Invoked when a mouse button has been pressed on a component.
	 *
	 * @param event the mouse event.
	 */
    public void mousePressed(MouseEvent event) {
    }

    /**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseReleased(MouseEvent event) {
    }

    /**
	 * Invoked when the mouse enters a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseEntered(MouseEvent event) {
    }

    /**
	 * Invoked when the mouse exits a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseExited(MouseEvent event) {
    }

    /**
	 * Invoked when a mouse button is pressed on a component and then 
	 * dragged.  Mouse drag events will continue to be delivered to
	 * the component where the first originated until the mouse button is
	 * released (regardless of whether the mouse position is within the
	 * bounds of the component).
	 *
	 * @param event the mouse event.
	 */
    public void mouseDragged(MouseEvent event) {
    }

    /**
	 * Invoked when the mouse button has been moved on a component
	 * (with no buttons no down).
	 *
	 * @param event the mouse event.
	 */
    public void mouseMoved(MouseEvent event) {
    }
}
