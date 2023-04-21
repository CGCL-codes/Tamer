package org.dinopolis.gpstool.plugin.googlemap;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.dinopolis.gpstool.GpsylonKeyConstants;
import org.dinopolis.gpstool.gui.MouseMode;
import org.dinopolis.gpstool.hook.MapManagerHook;
import org.dinopolis.gpstool.hook.MapNavigationHook;
import org.dinopolis.gpstool.plugin.PluginSupport;
import org.dinopolis.util.Resources;
import com.bbn.openmap.LatLonPoint;

/**
 * The Google Map MouseMode
 *
 * @author Christof Dallermassl
 * @version $Revision: 901 $
 */
public class GoogleMapMouseMode implements MouseMode, AWTEventListener {

    boolean mode_active_ = true;

    Resources resources_;

    GoogleMapLayer layer_;

    MapManagerHook map_manager_;

    JLabel statusMsg_;

    GoogleMapPlugin plugin_;

    public static final String KEY_SWISSGRID_MOUSEMODE_NAME = "swissgrid.mousemode.name";

    public static final String KEY_SWISSGRID_MOUSEMODE_DESCRIPTION = "swissgrid.mousemode.description";

    public static final String KEY_SWISSGRID_MOUSEMODE_MNEMONIC = "swissgrid.mousemode.mnemonic";

    public static final String KEY_SWISSGRID_MOUSEMODE_ACCELERATOR_KEY = "swissgrid.mousemode.accelerator_key";

    public static final String KEY_SWISSGRID_MOUSEMODE_ICON = "swissgrid.mousemode.icon";

    MapNavigationHook map_navigation_hook_;

    Cursor zoom_in_cursor_;

    Cursor zoom_out_cursor_;

    Cursor pan_cursor_;

    int zoom_mode_ = ZOOM_IN_MODE;

    Component component_;

    Point drag_start_;

    boolean mouse_dragged_ = false;

    public static final int ZOOM_IN_MODE = 0;

    public static final int ZOOM_OUT_MODE = 1;

    public static final int COORD_MODE = 2;

    /**
	 * Constructor for SwissGridMouseMode.
	 */
    public GoogleMapMouseMode() {
    }

    /**
	 * Method initialize.
	 * @param plugin_resources the resources of the swiss grid plugin.
	 * @param layer the layer to draw to
	 */
    public void initialize(PluginSupport support, Resources plugin_resources, GoogleMapLayer layer, GoogleMapPlugin plugin, MapManagerHook map_manager, JLabel statusMsg) {
        resources_ = plugin_resources;
        layer_ = layer;
        plugin_ = plugin;
        map_manager_ = map_manager;
        statusMsg_ = statusMsg;
        map_navigation_hook_ = support.getMapNavigationHook();
        component_ = support.getMapComponent();
        Resources resources = support.getResources();
        ImageIcon zoom_in = (ImageIcon) resources.getIcon(GpsylonKeyConstants.KEY_CURSOR_ZOOM_IN_ICON);
        ImageIcon zoom_out = (ImageIcon) resources.getIcon(GpsylonKeyConstants.KEY_CURSOR_ZOOM_OUT_ICON);
        ImageIcon pan = (ImageIcon) resources.getIcon(GpsylonKeyConstants.KEY_CURSOR_PAN_ICON);
        Toolkit toolkit = component_.getToolkit();
        zoom_out_cursor_ = toolkit.createCustomCursor(zoom_out.getImage(), new Point(5, 4), "zoom out");
        zoom_in_cursor_ = toolkit.createCustomCursor(zoom_in.getImage(), new Point(5, 4), "zoom in");
        pan_cursor_ = toolkit.createCustomCursor(pan.getImage(), new Point(5, 4), "pan");
    }

    /**
	   * Invoked when a key event occures.
	   */
    public void eventDispatched(AWTEvent event) {
        if (event instanceof KeyEvent) dispatchKeyEvent((KeyEvent) event);
    }

    /**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 *
	 * @param event the key event.
	 */
    public void dispatchKeyEvent(KeyEvent event) {
        if (event.isShiftDown()) updateZoomCursor(ZOOM_OUT_MODE); else if (event.isControlDown()) updateZoomCursor(COORD_MODE); else updateZoomCursor(ZOOM_IN_MODE);
    }

    protected void updateZoomCursor(int mode) {
        if (!mode_active_) return;
        if (mode == ZOOM_IN_MODE) component_.setCursor(zoom_in_cursor_); else if (mode == COORD_MODE) component_.setCursor(Cursor.getDefaultCursor()); else component_.setCursor(zoom_out_cursor_);
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
        if (!mode_active_) {
            component_.setCursor(Cursor.getDefaultCursor());
            component_.getToolkit().removeAWTEventListener(this);
        } else {
            component_.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
        }
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
        return (resources_.getString(KEY_SWISSGRID_MOUSEMODE_NAME));
    }

    /**
	 * The description returned here is used in the menu and/or the toolbar of
	 * the application to switch the mouse mode on or off.
	 *
	 * @return the description of the mouse mode.
	 */
    public String getMouseModeDescription() {
        return (resources_.getString(KEY_SWISSGRID_MOUSEMODE_DESCRIPTION));
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
        return (resources_.getString(KEY_SWISSGRID_MOUSEMODE_MNEMONIC).charAt(0));
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
        return (resources_.getString(KEY_SWISSGRID_MOUSEMODE_ACCELERATOR_KEY));
    }

    /**
	 * Invoked when the mouse has been clicked on a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseClicked(MouseEvent event) {
        if (!mode_active_) return;
        if (event.getButton() == MouseEvent.BUTTON1) {
            LatLonPoint point = map_navigation_hook_.getMapProjection().inverse(event.getX(), event.getY());
            if (event.isShiftDown()) {
                map_navigation_hook_.setMapCenter(point.getLatitude(), point.getLongitude());
                map_navigation_hook_.reScale(2.0f);
            }
            if (!event.isAltDown() && !event.isShiftDown() && !event.isControlDown()) {
                map_navigation_hook_.setMapCenter(point.getLatitude(), point.getLongitude());
                map_navigation_hook_.reScale(0.5f);
            }
        }
        if (event.getButton() == MouseEvent.BUTTON3) {
            LatLonPoint point = map_navigation_hook_.getMapProjection().inverse(event.getX(), event.getY());
            map_navigation_hook_.setMapCenter(point.getLatitude(), point.getLongitude());
            map_navigation_hook_.reScale(2.0f);
        }
    }

    /**
	 * Invoked when a mouse button has been pressed on a component.
	 *
	 * @param event the mouse event.
	 */
    public void mousePressed(MouseEvent event) {
        if (!mode_active_) return;
        if (event.getButton() == MouseEvent.BUTTON1) {
            drag_start_ = event.getPoint();
        }
    }

    /**
	 * Invoked when a mouse button has been released on a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseReleased(MouseEvent event) {
        if (!mode_active_) return;
        if (event.getButton() == MouseEvent.BUTTON1) {
            if (drag_start_ == null) return;
            Point point = event.getPoint();
            float delta_x = (float) (drag_start_.getX() - point.getX());
            float delta_y = (float) (drag_start_.getY() - point.getY());
            float factor_x = delta_x / component_.getWidth();
            float factor_y = delta_y / component_.getHeight();
            map_navigation_hook_.translateMapCenterRelative(factor_x, factor_y);
            drag_start_ = null;
            updateZoomCursor(ZOOM_IN_MODE);
        }
    }

    /**
	 * Invoked when the mouse enters a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseEntered(MouseEvent event) {
        if (!mode_active_) return;
        Component source = (Component) event.getSource();
        if (event.isShiftDown()) source.setCursor(zoom_out_cursor_); else source.setCursor(zoom_in_cursor_);
    }

    /**
	 * Invoked when the mouse exits a component.
	 *
	 * @param event the mouse event.
	 */
    public void mouseExited(MouseEvent event) {
        if (!mode_active_) return;
        Component source = (Component) event.getSource();
        source.setCursor(Cursor.getDefaultCursor());
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
        if (!mode_active_) return;
        Component source = (Component) event.getSource();
        source.setCursor(pan_cursor_);
    }

    /**
	 * Invoked when the mouse button has been moved on a component
	 * (with no buttons no down).
	 *
	 * @param event the mouse event.
	 */
    public void mouseMoved(MouseEvent event) {
        if (!mode_active_) return;
        LatLonPoint wgs84 = layer_.getProjection().inverse(event.getPoint());
        Point2D swiss = GoogleMapProjection.ll2lv03(wgs84.getLatitude(), wgs84.getLongitude());
        int m25 = GoogleMapProjection.lv03tosn(swiss, 25);
        int m50 = GoogleMapProjection.lv03tosn(swiss, 50);
        int m100 = GoogleMapProjection.lv03tosn(swiss, 100);
        statusMsg_.setText(new String("Swissgrid: " + (int) swiss.getX() + "/" + (int) swiss.getY() + " [" + m25 + "/" + m50 + "/" + m100 + "] WGS84: " + wgs84.getLatitude() + "/" + wgs84.getLongitude() + " "));
    }
}
