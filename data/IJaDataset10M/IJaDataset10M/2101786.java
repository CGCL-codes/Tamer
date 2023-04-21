package net.suberic.util.swing;

import javax.swing.*;
import java.util.*;
import javax.swing.plaf.metal.*;
import net.suberic.util.*;

/**
 * A class that allows one to apply arbitrary styles to individual 
 * Components.
 */
public class ThemeManager implements ValueChangeListener, ItemCreator, ItemListChangeListener {

    private ItemManager manager;

    private WeakHashMap listenerList = new WeakHashMap();

    private VariableBundle sourceBundle;

    private String resourceString;

    /**
   * Creates a ThemeManager.
   */
    public ThemeManager(String newResourceString, VariableBundle bundle) {
        resourceString = newResourceString;
        sourceBundle = bundle;
        createThemes();
    }

    /**
   * Creates the Theme entries.
   */
    private void createThemes() {
        manager = new ItemManager(resourceString, sourceBundle, this);
        manager.addItemListChangeListener(this);
    }

    /**
   * Applies the given Theme to the component.
   */
    public void applyTheme(MetalTheme theme, java.awt.Component component) throws UnsupportedLookAndFeelException {
        LookAndFeel laf = UIManager.getLookAndFeel();
        if (laf instanceof MetalLookAndFeel) {
            MetalLookAndFeel oldMlaf = ((MetalLookAndFeel) laf);
            MetalTheme oldMt = getDefaultTheme();
            if (theme != null) {
                oldMlaf.setCurrentTheme(theme);
                MetalLookAndFeel newMlaf = new MetalLookAndFeel();
                UIManager.setLookAndFeel(newMlaf);
                SwingUtilities.updateComponentTreeUI(component);
                oldMlaf.setCurrentTheme(oldMt);
                UIManager.setLookAndFeel(oldMlaf);
            } else {
                SwingUtilities.updateComponentTreeUI(component);
            }
        } else {
            throw new UnsupportedLookAndFeelException("Expected MetalLookAndFeel, got " + laf.getClass().getName());
        }
    }

    /**
   * updates the given Component with the configuration from the given
   * ThemeSupporter.
   */
    public void updateUI(ThemeSupporter ui, java.awt.Component component) throws UnsupportedLookAndFeelException {
        updateUI(ui, component, false);
    }

    /**
   * updates the given Component with the configuration from the given
   * ThemeSupporter.
   */
    public void updateUI(ThemeSupporter ui, java.awt.Component component, boolean force) throws UnsupportedLookAndFeelException {
        MetalTheme newTheme = ui.getTheme(this);
        MetalTheme oldTheme = ui.getCurrentTheme();
        if (!force) {
            if (newTheme != oldTheme) {
                applyTheme(newTheme, component);
                ui.setCurrentTheme(newTheme);
            }
        } else {
            applyTheme(newTheme, component);
            if (newTheme != oldTheme) {
                ui.setCurrentTheme(newTheme);
            }
        }
    }

    /**
   * Gets the default configuration for the system.
   */
    public MetalTheme getDefaultTheme() {
        String defaultString = sourceBundle.getProperty(resourceString + "._default", "");
        if (defaultString != null && !defaultString.equals("")) {
            return getTheme(defaultString);
        }
        return null;
    }

    /**
   * Gets the named configuration, or null if no such configuration 
   * exists.
   */
    public MetalTheme getTheme(String configID) {
        if (configID == null) return null;
        Item returnValue = manager.getItem(configID);
        if (returnValue == null) return null; else if (returnValue instanceof MetalTheme) return (MetalTheme) returnValue; else if (returnValue instanceof ThemeWrapperItem) return ((ThemeWrapperItem) returnValue).getWrappedTheme(); else return null;
    }

    /**
   * Called when a ui value changes.
   */
    public void valueChanged(String changedValue) {
    }

    /**
   * As defined in net.suberic.util.ItemListChangeListener.
   * 
   * This listens for ItemListChangeEvents, which result from changes to the 
   * resourceString property.  The result is just that the event is passed 
   * to listeners to this object.
   */
    public void itemListChanged(ItemListChangeEvent e) {
        fireItemListChanged(e);
    }

    /**
   * This notifies all listeners that the Theme list has changed.
   */
    public void fireItemListChanged(ItemListChangeEvent e) {
        Iterator iter = listenerList.keySet().iterator();
        while (iter.hasNext()) ((ItemListChangeListener) iter.next()).itemListChanged(e);
    }

    /**
   * This adds a listener.
   */
    public void addItemListListener(ItemListChangeListener newListener) {
        listenerList.put(newListener, null);
    }

    /**
   * This removes a listener.
   */
    public void removeItemListListener(ItemListChangeListener oldListener) {
        listenerList.remove(oldListener);
    }

    /**
   * Creates an item from the given sourceBundle, resourceString, and itemId.
   *
   * Creates a new Theme object.
   */
    public Item createItem(VariableBundle sourceBundle, String resourceString, String itemId) {
        if (itemId != null && itemId.equals("Ocean")) {
            ThemeWrapperItem wrapper = new ThemeWrapperItem(sourceBundle, resourceString, itemId);
            try {
                Class oceanThemeClass = Class.forName("javax.swing.plaf.metal.OceanTheme");
                MetalTheme oceanTheme = (MetalTheme) oceanThemeClass.newInstance();
                wrapper.setWrappedTheme(oceanTheme);
                return wrapper;
            } catch (Exception e) {
            }
        }
        return new ConfigurableMetalTheme(sourceBundle, resourceString, itemId);
    }
}
