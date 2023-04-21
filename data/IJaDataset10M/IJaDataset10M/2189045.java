package au.gov.qld.dnr.dss.v1.ui.pref;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import org.swzoo.log2.core.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.tree.*;

/**
 * Tree Cell Renderer for the Preference tree.
 */
public class PreferenceTreeCellRenderer extends DefaultTreeCellRenderer {

    Icon openIcon;

    Icon closedIcon;

    Icon leafIcon;

    /**
     * Constructor.
     * Initialises icons from our resources.
     */
    public PreferenceTreeCellRenderer() {
        init();
    }

    /**
     * Initialise renderer.
     */
    void init() {
        try {
            openIcon = new ImageIcon(resources.getSystemResource("image/pref/open.gif"));
        } catch (MissingResourceException ex) {
            LogTools.warn(logger, "PreferenceTreeCellRenderer.<init> - \"image/pref/open.gif\" resource not found.");
        }
        try {
            closedIcon = new ImageIcon(resources.getSystemResource("image/pref/closed.gif"));
        } catch (MissingResourceException ex) {
            LogTools.warn(logger, "PreferenceTreeCellRenderer.<init> - \"image/pref/closed.gif\" resource not found.");
        }
        try {
            leafIcon = new ImageIcon(resources.getSystemResource("image/pref/leaf.gif"));
        } catch (MissingResourceException ex) {
            LogTools.warn(logger, "PreferenceTreeCellRenderer.<init> - \"image/pref/leaf.gif\" resource not found.");
        }
        setOpenIcon(openIcon);
        setClosedIcon(closedIcon);
        setLeafIcon(leafIcon);
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource manager. */
    ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}
