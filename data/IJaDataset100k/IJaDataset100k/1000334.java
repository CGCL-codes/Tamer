package net.hypotenubel.jaicwain.gui.docking;

import javax.swing.*;

/**
 * Classes extending this class can have themselves managed by the
 * {@link WindowManager} class. In general, everything that doesn't want to be
 * a dialog should be derived from this class to get the highest possible amount
 * of UI configurability.
 * 
 * @author Christoph Daniel Schulze
 * @version $Id: DockingPanel.java 111 2006-09-20 16:29:08Z captainnuss $
 */
public class DockingPanel extends JPanel {

    /**
     * The panel's title. Usually a short text displayed by the tabbed container
     * hosting the panel.
     */
    private String panelTitle = "";

    /**
     * A longer version of the panel's title which can be displayed by the window
     * the panel is hosted in.
     */
    private String panelLongTitle = "";

    /**
     * {@code String} containing this panel's description. Could be used
     * for tooltips and - surprise! - panel descriptions.
     */
    private String panelDescription = "";

    /**
     * 16*16 pixel {@code Icon}.
     */
    private Icon panelSmallIcon = null;

    /**
     * 24*24 pixel {@code Icon}.
     */
    private Icon panelLargeIcon = null;

    /**
     * The {@code ExtendedPanelContainer} that owns this panel.
     */
    protected DockingPanelContainer panelContainer = null;

    /**
     * Creates a new {@code ExtendedPanel} object.
     */
    public DockingPanel() {
        super();
    }

    /**
     * Returns this panel's title which isn't {@code null}.
     * 
     * @return {@code String} containing this panel's title.
     */
    public String getTitle() {
        return panelTitle;
    }

    /**
     * Returns this panel's long title which isn't {@code null}.
     * 
     * @return {@code String} containing this panel's long title.
     */
    public String getLongTitle() {
        return panelLongTitle;
    }

    /**
     * Returns this panel's description which isn't {@code null}.
     * 
     * @return {@code String} containing this panel's description.
     */
    public String getDescription() {
        return panelDescription;
    }

    /**
     * Returns this panel's small icon.
     * 
     * @return {@code Icon} containing this panel's small icon or
     *         {@code null} if this panel hasn't got any.
     */
    public Icon getSmallIcon() {
        return panelSmallIcon;
    }

    /**
     * Returns this panel's large icon.
     * 
     * @return {@code Icon} containing this panel's large icon or
     *         {@code null} if this panel hasn't got any.
     */
    public Icon getLargeIcon() {
        return panelLargeIcon;
    }

    /**
     * Returns this panel's container.
     * 
     * @return {@code ExtendedPanelContainer} containing this panel's
     *         container or {@code null} if this panel hasn't got any.
     */
    public DockingPanelContainer getContainer() {
        return panelContainer;
    }

    /**
     * Returns an array of custom menu stuff related to this panel.
     * 
     * @return {@code JMenuItem} array or {@code null} if it doesn't
     *         have any.
     */
    public JMenuItem[] getMenuItems() {
        return null;
    }

    /**
     * Sets this panel's title and updates the container
     * 
     * @param title {@code String} containing this panel's new title.
     */
    protected void setTitle(String title) {
        if (title != null) {
            panelTitle = title;
        }
        if (panelContainer != null) {
            panelContainer.panelInformationUpdated(this);
        }
    }

    /**
     * Sets this panel's long title and updates the container
     * 
     * @param longTitle {@code String} containing this panel's new long title.
     */
    protected void setLongTitle(String longTitle) {
        if (panelLongTitle != null) {
            panelLongTitle = longTitle;
        }
        if (panelContainer != null) {
            panelContainer.panelInformationUpdated(this);
        }
    }

    /**
     * Sets this panel's description and updates the container
     * 
     * @param desc {@code String} containing this panel's new description.
     */
    protected void setDescription(String desc) {
        if (desc != null) {
            panelDescription = desc;
        }
        if (panelContainer != null) {
            panelContainer.panelInformationUpdated(this);
        }
    }

    /**
     * Sets this panel's small icon and updates the container
     * 
     * @param icon {@code Icon} containing this panel's new small icon.
     */
    protected void setSmallIcon(Icon icon) {
        if (icon != null) {
            panelSmallIcon = icon;
        }
        if (panelContainer != null) {
            panelContainer.panelInformationUpdated(this);
        }
    }

    /**
     * Sets this panel's large icon and updates the container
     * 
     * @param icon {@code Icon} containing this panel's new large icon.
     */
    protected void setLargeIcon(Icon icon) {
        if (icon != null) {
            panelLargeIcon = icon;
        }
        if (panelContainer != null) {
            panelContainer.panelInformationUpdated(this);
        }
    }

    /**
     * Sets this panel's container. Should only be called by the specified
     * container itself.
     * 
     * @param container {@code ExtendedPanelContainer} that owns this
     *                  panel.
     */
    public void setContainer(DockingPanelContainer container) {
        panelContainer = container;
    }
}
