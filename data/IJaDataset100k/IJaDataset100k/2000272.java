package org.argouml.uml.diagram.ui;

import java.util.Hashtable;
import java.util.Properties;
import javax.swing.Action;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.SetModeAction;

/**
 * Extends GEF ActionSetMode to add additional metadata such as tooltips.
 *
 * @author Jeremy Jones
 */
public class ActionSetMode extends SetModeAction {

    private static final Logger LOG = Logger.getLogger(ActionSetMode.class);

    /**
     * The constructor.
     *
     * @param args arguments
     */
    public ActionSetMode(Properties args) {
        super(args);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     */
    public ActionSetMode(Class modeClass) {
        super(modeClass);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param name the name of the icon
     */
    public ActionSetMode(Class modeClass, String name) {
        super(modeClass);
        putToolTip(name);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param name the icon name
     * @param tooltipkey The key for the tooltip text.
     */
    public ActionSetMode(Class modeClass, String name, String tooltipkey) {
        super(modeClass, name);
        putToolTip(tooltipkey);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param sticky the global sticky mode boolean allows the user
     *               to place several nodes rapidly (in succession)
     */
    public ActionSetMode(Class modeClass, boolean sticky) {
        super(modeClass, sticky);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param modeArgs arguments for the new mode
     */
    public ActionSetMode(Class modeClass, Hashtable modeArgs) {
        super(modeClass, modeArgs);
    }

    /**
     * The constructor.
     * TODO: The "name" parameter is used for the icon and for the tooltip.
     * This makes i18n of the tooltip impossible.
     *
     * @param modeClass the next global editor mode
     * @param modeArgs arguments for the new mode
     * @param name the name of the command that is the tooltip text.
     */
    public ActionSetMode(Class modeClass, Hashtable modeArgs, String name) {
        super(modeClass);
        this.modeArgs = modeArgs;
        putToolTip(name);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     */
    public ActionSetMode(Class modeClass, String arg, Object value) {
        super(modeClass, arg, value);
    }

    /**
     * The constructor.
     * TODO: The "name" parameter is used for the icon and for the tooltip.
     * This makes i18n of the tooltip impossible.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     * @param name the name of the command that is the tooltip text.
     */
    public ActionSetMode(Class modeClass, String arg, Object value, String name) {
        super(modeClass, arg, value);
        putToolTip(name);
        putIcon(name);
    }

    /**
     * The constructor.
     *
     * @param modeClass the next global editor mode
     * @param arg the name of a new argument for the new mode
     * @param value the value of a new argument for the new mode
     * @param name the name of the command that is the tooltip text.
     * @param icon the SMALL_ICON for the action
     */
    public ActionSetMode(Class modeClass, String arg, Object value, String name, ImageIcon icon) {
        super(modeClass, arg, value, name, icon);
        putToolTip(name);
    }

    /**
     * Adds tooltip text to the Action.
     *
     * @param key The key to be localized to become the tooltip.
     */
    private void putToolTip(String key) {
        putValue(Action.SHORT_DESCRIPTION, Translator.localize(key));
    }

    private void putIcon(String key) {
        ImageIcon icon = ResourceLoaderWrapper.lookupIcon(key);
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
        } else {
            LOG.debug("Failed to find icon for key " + key);
        }
    }
}
