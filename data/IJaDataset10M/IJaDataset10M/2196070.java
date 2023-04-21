package org.fudaa.fudaa.crue.common;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import com.memoire.bu.BuDesktop;
import com.memoire.fu.FuPreferences;
import org.apache.commons.lang.StringUtils;
import org.fudaa.dodico.crue.common.CruePreferences;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;
import org.jdesktop.swingx.table.TableColumnModelExt;

/**
 * Classe charger de sauvegarder l'etat des fenetres internes dans des preferences locales.
 */
public class UserPreferencesSaver {

    /**
   * Permet d'enregistrer dans les preferences du poste client les dimensions des composants utilises
   * 
   * @param main la window dont les dim,la location et les dim de vues vont etre sauvegardées
   */
    public static void saveWindowPreferences(JFrame main, Point lastPoint, Dimension lastDim) {
        boolean val = isWindowMaximized(main);
        FuPreferences createPreferences = createPreferences();
        if (val) {
            if (lastPoint != null) {
                saveIn(lastPoint, main.getName(), createPreferences);
            }
            if (lastDim != null) {
                saveIn(lastDim, main.getName(), createPreferences);
            }
        }
        saveExtendedState(main.getName(), createPreferences, val);
    }

    public static boolean isWindowMaximized(JFrame main) {
        int extendedState = main.getExtendedState();
        return isExtendedStateMaximized(extendedState);
    }

    public static boolean isExtendedStateMaximized(int extendedState) {
        return (extendedState & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
    }

    public static void saveLocationAndDimension(Component main) {
        saveComponentLocationAndDimension(main, createPreferences(), main.getName());
    }

    public static void loadDialogLocationAndDimension(JDialog main) {
        loadComponentLocationAndDimension(main, createPreferences(), main.getName());
        ensureComponentWillBeVisible(main, main.getLocation());
    }

    public static void saveInternalFrameLocationAndDimension(JInternalFrame main) {
        String prefix = main.getName();
        FuPreferences preferences = createPreferences();
        saveExtendedState(prefix, preferences, main.isMaximum());
        Rectangle normalBounds = main.getNormalBounds();
        saveIn(new Dimension(normalBounds.width, normalBounds.height), prefix, preferences);
        saveIn(new Point(normalBounds.x, normalBounds.y), prefix, preferences);
    }

    public static void loadInternalFrameLocationAndDimension(JInternalFrame main, BuDesktop parent) {
        loadComponentLocationAndDimension(main, createPreferences(), main.getName());
        ensureJInternalFrameWillBeVisible(main, parent);
        boolean isMax = loadExtendedState(main.getName(), createPreferences());
        if (isMax) {
            try {
                main.setMaximum(true);
            } catch (PropertyVetoException e) {
            }
        }
    }

    private static void ensureJInternalFrameWillBeVisible(JInternalFrame main, BuDesktop parent) {
        Point init = main.getLocation();
        Dimension initDim = main.getSize();
        int maxW = parent.getWidth();
        int maxH = parent.getHeight();
        int ifW = initDim.width;
        int ifH = initDim.height;
        ifW = Math.min(maxW, ifW);
        ifH = Math.min(maxH, ifH);
        int locX = Math.max(0, Math.min(init.x, maxW - ifW));
        int locY = Math.max(0, Math.min(init.y, maxH - ifH));
        Point newPt = new Point(locX, locY);
        Dimension newDimension = new Dimension(ifW, ifH);
        if (!newPt.equals(init) || newDimension.equals(initDim)) {
            main.setLocation(newPt);
            main.setSize(newDimension);
        }
    }

    private static void saveComponentLocationAndDimension(Component main, final FuPreferences preferences, final String mainWindow) {
        saveIn(main.getSize(), mainWindow, preferences);
        saveIn(main.getLocation(), mainWindow, preferences);
    }

    /**
   * Ne pas utiliser les preferences au prochain redémarrage
   */
    public static void clearPref() {
        createPreferences().putBooleanProperty("prefs.clear", true);
    }

    /**
   * @param window
   */
    public static void loadWindowPreferences(final JFrame window) {
        if (loadExtendedState(window.getName(), createPreferences())) {
            loadComponentLocationAndDimension(window, createPreferences(), window.getName());
            ensureComponentWillBeVisible(window, window.getLocation());
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    window.setExtendedState(Frame.MAXIMIZED_BOTH);
                }
            });
        } else {
            ensureComponentWillBeVisible(window, window.getLocation());
        }
    }

    private static Point loadComponentLocationAndDimension(final Component window, final FuPreferences preferences, final String mainWindow) {
        final Dimension dim = loadDim(mainWindow, preferences);
        if (dim != null) {
            window.setPreferredSize(dim);
            window.setSize(dim);
        }
        final Point loc = loadLocation(mainWindow, preferences);
        if (loc != null) {
            window.setLocation(loc);
        }
        return loc;
    }

    private static GraphicsDevice getDevice(final Component window) {
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        Rectangle boundsOfWindow = window.getBounds();
        if (screenDevices != null && screenDevices.length > 1) {
            for (GraphicsDevice graphicsDevice : screenDevices) {
                Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
                Point p = new Point(boundsOfWindow.x, bounds.y);
                if (bounds.contains(p)) {
                    p.x = boundsOfWindow.x + boundsOfWindow.width;
                    if (bounds.contains(p)) {
                        return graphicsDevice;
                    }
                }
            }
        }
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    }

    private static void ensureComponentWillBeVisible(final Component window, final Point initLocation) {
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ensureComponentWillBeVisible(window, initLocation, screenSize);
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (screenDevices != null && screenDevices.length > 1) {
            GraphicsDevice device = getDevice(window);
            if (device != null) {
                ensureComponentWillBeVisible(window, initLocation, device.getDefaultConfiguration().getBounds().getSize(), false);
            }
        }
    }

    private static void ensureComponentWillBeVisible(final Component window, final Point initLocation, final Dimension screenSize) {
        ensureComponentWillBeVisible(window, initLocation, screenSize, true);
    }

    private static void ensureComponentWillBeVisible(final Component window, final Point initLocation, final Dimension screenSize, boolean modifyX) {
        final Dimension dim = window.getSize();
        boolean size = false;
        if (dim.width > screenSize.width) {
            size = true;
            dim.width = screenSize.width;
        }
        if (dim.height > screenSize.height) {
            size = true;
            dim.height = screenSize.height;
        }
        if (size) {
            window.setSize(dim);
            window.setPreferredSize(dim);
        }
        if (initLocation == null) {
            return;
        }
        if (modifyX) {
            initLocation.x = Math.max(0, Math.min(initLocation.x, screenSize.width - dim.width));
        }
        initLocation.y = Math.max(0, Math.min(initLocation.y, screenSize.height - dim.height));
        window.setLocation(initLocation);
    }

    /**
   * @return
   */
    private static FuPreferences createPreferences() {
        return CruePreferences.Crue;
    }

    public static boolean getPreference(String key, boolean defaultValue) {
        return createPreferences().getBooleanProperty(key, defaultValue);
    }

    public static String getPreference(String key) {
        return createPreferences().getStringProperty(key, null);
    }

    public static void savePreference(String key, String value) {
        createPreferences().putStringProperty(key, value);
    }

    public static String getPreference(String key, String defaultValue) {
        return createPreferences().getStringProperty(key, defaultValue);
    }

    public static void savePreference(String key, boolean value) {
        createPreferences().putBooleanProperty(key, value);
    }

    /**
   * @param location la location a sauvegarder
   * @param classId la classe permettant d'identifier la preferences
   */
    public static void saveLocation(final Point location, String classId) {
        saveIn(location, classId + ".location", createPreferences());
    }

    /**
   * @param d
   * @param classId la classe permettant d'identifier la preferences
   */
    public static void saveDimension(final Dimension d, String classId) {
        saveIn(d, classId + ".dimension", createPreferences());
    }

    /**
   * @param classId la classe permettant d'identifier la preferences
   * @return la dim lue ou null si non trouvee
   */
    public static Dimension loadDimension(String classId) {
        return loadDim(classId + ".dimension", createPreferences());
    }

    /**
   * @param classId la classe permettant d'identifier la preferences
   * @return la position lue ou null si non trouvee
   */
    public static Point loadLocation(String classId) {
        return loadLocation(classId + ".location", createPreferences());
    }

    private static void saveIn(final Dimension d, final String prefix, final FuPreferences pref) {
        pref.putIntegerProperty(prefix + ".w", d.width);
        pref.putIntegerProperty(prefix + ".h", d.height);
    }

    /**
   * @param d les dimension du composant a suaver
   * @param parentDim les dimension parentes utilisees pour le ratio.
   * @param prefix prefix a utiliser pour les preferences
   * @param pref lles preferences a modifier
   */
    public static void saveRatioIn(final Dimension d, final Dimension parentDim, final String prefix, final Preferences pref) {
        pref.putDouble(prefix + ".ratio.w", ((double) d.width) / (double) parentDim.width);
        pref.putDouble(prefix + ".ratio.h", ((double) d.height) / (double) parentDim.height);
    }

    /**
   * @param d le point a sauvegarder
   * @param prefix le prefixe a utilser pour les cles
   * @param pref les preferences a modifier
   */
    public static void saveIn(final Point d, final String prefix, final FuPreferences pref) {
        pref.putIntegerProperty(prefix + ".x", Math.max(0, d.x));
        pref.putIntegerProperty(prefix + ".y", Math.max(0, d.y));
    }

    /**
   * @param prefix le prefixe a utilser pour les cles
   * @param pref les preferences a utiliser
   * @return la dim lue ou null si non trouvee
   */
    public static Dimension loadDim(final String prefix, final FuPreferences pref) {
        final int w = pref.getIntegerProperty(prefix + ".w", -1);
        if (w <= 0) {
            return null;
        }
        final int h = pref.getIntegerProperty(prefix + ".h", -1);
        if (h <= 0) {
            return null;
        }
        return new Dimension(w, h);
    }

    public static boolean loadExtendedState(final String prefix, final FuPreferences pref) {
        return pref.getBooleanProperty(prefix + ".isExtended");
    }

    public static void saveExtendedState(final String prefix, final FuPreferences pref, boolean val) {
        pref.putBooleanProperty(prefix + ".isExtended", val);
    }

    /**
   * @param prefix le prefixe a utilser pour les cles
   * @param parentDim
   * @param pref les preferences a utiliser
   * @return la dim lue ou null si non trouvee
   */
    public static Dimension loadRatioDim(final String prefix, final Dimension parentDim, final FuPreferences pref) {
        final double w = pref.getDoubleProperty(prefix + ".ratio.w", -1d);
        if (w <= 0) {
            return null;
        }
        final double h = pref.getDoubleProperty(prefix + ".ratio.h", -1d);
        if (h <= 0) {
            return null;
        }
        return new Dimension((int) (w * parentDim.width), (int) (h * parentDim.height));
    }

    /**
   * @param prefix le prefixe a utilser pour les cles
   * @param pref les preferences a utiliser
   * @return le point lu ou null si non trouve
   */
    public static Point loadLocation(final String prefix, final FuPreferences pref) {
        final int x = pref.getIntegerProperty(prefix + ".x", -1);
        if (x < 0) {
            return null;
        }
        final int y = pref.getIntegerProperty(prefix + ".y", -1);
        if (y < 0) {
            return null;
        }
        return new Point(x, y);
    }

    public static void saveTablePreferences(final JXTable table) {
        saveTablePreferences(createPreferences(), table);
    }

    public static void saveSplitPaneVerticalPreferences(final JSplitPane splitPane) {
        FuPreferences pref = createPreferences();
        pref.putDoubleProperty(getComponentPreferencesPrefix(splitPane) + "vertical", ((double) splitPane.getDividerLocation()) / splitPane.getHeight());
    }

    public static void restoreSplitPaneVerticalPreferences(final JSplitPane splitPane) {
        final String tablePrefix = getComponentPreferencesPrefix(splitPane);
        FuPreferences pref = createPreferences();
        double ratio = pref.getDoubleProperty(getComponentPreferencesPrefix(splitPane) + "vertical", -1D);
        if (ratio > 0) {
            int height = splitPane.getHeight();
            splitPane.setDividerLocation((int) (ratio * height));
        }
    }

    /**
   * @param pref les pref a modifier
   * @param table la table a sauver: colonne + visibilite
   */
    public static void saveTablePreferences(final FuPreferences pref, final JXTable table) {
        final List<TableColumn> columns = ((TableColumnModelExt) table.getColumnModel()).getColumns(true);
        final String tablePrefix = getComponentPreferencesPrefix(table);
        saveIn(table.getSize(), tablePrefix + "dimension.", pref);
        final String wId = tablePrefix + "column.width.";
        final String visibleId = tablePrefix + "column.visible.";
        for (final TableColumn tableColumn : columns) {
            final String colName = table.getModel().getColumnName(tableColumn.getModelIndex());
            pref.putIntegerProperty(wId + colName, tableColumn.getWidth());
            pref.putBooleanProperty(visibleId + colName, ((TableColumnExt) tableColumn).isVisible());
        }
    }

    /**
   * @param pref les preferences a utiliser
   * @param table la table a modifier
   */
    public static void loadTablePreferences(final JXTable table) {
        loadTablePreferences(createPreferences(), table, null);
    }

    /**
   * @param pref les preferences a utiliser
   * @param table la table a modifier
   * @param defaultWidths les largeurs a utiliser par default
   */
    public static void loadTablePreferences(final FuPreferences pref, final JXTable table, final Map<String, Integer> defaultWidths) {
        final String tablePrefix = getComponentPreferencesPrefix(table);
        loadTableSizePreferences(pref, table, tablePrefix);
        final List<TableColumn> columns = ((TableColumnModelExt) table.getColumnModel()).getColumns(true);
        final String wId = tablePrefix + "column.width.";
        final String visibleId = tablePrefix + "column.visible.";
        for (final TableColumn tableColumn : columns) {
            final String colName = table.getModel().getColumnName(tableColumn.getModelIndex());
            final Integer defaultWidth = defaultWidths == null ? null : defaultWidths.get(colName);
            final int w = pref.getIntegerProperty(wId + colName, defaultWidth == null ? -1 : defaultWidth.intValue());
            if (w > 0) {
                tableColumn.setPreferredWidth(w);
                tableColumn.setWidth(w);
            }
            if (!pref.getBooleanProperty(visibleId + colName, true)) {
                ((TableColumnExt) tableColumn).setVisible(false);
            }
        }
    }

    /**
   * Dimensionne la table selon les preferences sauvegardees.
   * 
   * @param pref les preferences a utiliser
   * @param table la table a modifier
   * @param defaultWidths les largeurs a utiliser par default
   */
    private static void loadTableSizePreferences(final FuPreferences pref, final JXTable table, final String tablePrefix) {
        final Dimension d = loadDim(tablePrefix + "dimension.", pref);
        if (d != null) {
            table.setSize(d);
        }
    }

    /**
   * @param t
   * @return
   */
    private static String getComponentPreferencesPrefix(final JComponent t) {
        return StringUtils.defaultString(t.getName(), "noName") + ".";
    }
}
