package org.dishevelled.identify;

import java.awt.ComponentOrientation;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;
import org.dishevelled.iconbundle.IconBundle;
import org.dishevelled.iconbundle.IconSize;
import org.dishevelled.iconbundle.IconState;
import org.dishevelled.iconbundle.IconTextDirection;

/**
 * An extension of JButton that displays the name property value
 * and appropriate icon from an icon bundle for an identifiable
 * action.
 *
 * @see IdentifiableAction
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class IdButton extends JButton {

    /** Default icon size. */
    public static final IconSize DEFAULT_ICON_SIZE = IconSize.DEFAULT_16X16;

    /** Default icon text direction. */
    private static final IconTextDirection DEFAULT_ICON_TEXT_DIRECTION = IconTextDirection.LEFT_TO_RIGHT;

    /** Icon size. */
    private IconSize iconSize = DEFAULT_ICON_SIZE;

    /** Icon text direction. */
    private IconTextDirection iconTextDirection = DEFAULT_ICON_TEXT_DIRECTION;

    /**
     * Create a new button with the specified identifiable action.
     *
     * @param action identifiable action, must not be null
     */
    public IdButton(final IdentifiableAction action) {
        super();
        UIManager.put("Button.gradient", null);
        UIManager.getDefaults().put("Button.gradient", null);
        UIManager.getLookAndFeelDefaults().put("Button.gradient", null);
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        setAction(action);
    }

    /**
     * Create a new button with the specified identifiable action
     * and icon size.
     *
     * @param action identifiable action, must not be null
     * @param iconSize icon size, must not be null
     */
    public IdButton(final IdentifiableAction action, final IconSize iconSize) {
        super();
        UIManager.put("Button.gradient", null);
        UIManager.getDefaults().put("Button.gradient", null);
        UIManager.getLookAndFeelDefaults().put("Button.gradient", null);
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        }
        setIconSize(iconSize);
        setAction(action);
    }

    /**
     * Return the icon size for this button.
     *
     * @return the icon size for this button
     */
    public IconSize getIconSize() {
        return iconSize;
    }

    /**
     * Set the icon size for this button to <code>iconSize</code>.
     *
     * <p>This is a bound property.</p>
     *
     * @param iconSize icon size, must not be null
     */
    public void setIconSize(final IconSize iconSize) {
        if (iconSize == null) {
            throw new IllegalArgumentException("iconSize must not be null");
        }
        IconSize oldIconSize = this.iconSize;
        this.iconSize = iconSize;
        if (!this.iconSize.equals(oldIconSize)) {
            firePropertyChange("iconSize", oldIconSize, this.iconSize);
            rebuild();
        }
    }

    /**
     * Return the icon text direction for this button.
     *
     * @return the icon text direction for this button
     */
    IconTextDirection getIconTextDirection() {
        return iconTextDirection;
    }

    /**
     * Display icons only.
     */
    public void displayIcon() {
        setText(null);
        rebuild();
    }

    /**
     * Display text only.
     */
    public void displayText() {
        Action action = getAction();
        if ((action != null) && (action instanceof IdentifiableAction)) {
            IdentifiableAction identifiableAction = (IdentifiableAction) action;
            setText(identifiableAction.getName());
        }
        setIcon(null);
        setPressedIcon(null);
        setSelectedIcon(null);
        setRolloverIcon(null);
        setRolloverSelectedIcon(null);
        setDisabledIcon(null);
    }

    /**
     * Display icon and text.
     */
    public void displayIconAndText() {
        Action action = getAction();
        if ((action != null) && (action instanceof IdentifiableAction)) {
            IdentifiableAction identifiableAction = (IdentifiableAction) action;
            setText(identifiableAction.getName());
        }
        rebuild();
    }

    /** {@inheritDoc} */
    public void setComponentOrientation(final ComponentOrientation orientation) {
        ComponentOrientation oldOrientation = getComponentOrientation();
        if (!oldOrientation.equals(orientation)) {
            if (orientation != null) {
                iconTextDirection = orientation.isLeftToRight() ? IconTextDirection.LEFT_TO_RIGHT : IconTextDirection.RIGHT_TO_LEFT;
                rebuild();
            }
        }
        super.setComponentOrientation(orientation);
    }

    /** {@inheritDoc} */
    public void applyComponentOrientation(final ComponentOrientation orientation) {
        ComponentOrientation oldOrientation = getComponentOrientation();
        if (!oldOrientation.equals(orientation)) {
            if (orientation != null) {
                iconTextDirection = orientation.isLeftToRight() ? IconTextDirection.LEFT_TO_RIGHT : IconTextDirection.RIGHT_TO_LEFT;
                rebuild();
            }
        }
        super.applyComponentOrientation(orientation);
    }

    /** {@inheritDoc} */
    protected void configurePropertiesFromAction(final Action action) {
        super.configurePropertiesFromAction(action);
        rebuild();
    }

    /**
     * Rebuild the icons for this button from the icon bundle
     * provided by the identifiable action for this button.
     */
    private void rebuild() {
        Action action = getAction();
        if ((action != null) && (action instanceof IdentifiableAction)) {
            IdentifiableAction identifiableAction = (IdentifiableAction) action;
            IconBundle bndl = identifiableAction.getIconBundle();
            setIcon(new ImageIcon(bndl.getImage(this, iconTextDirection, IconState.NORMAL, iconSize)));
            setPressedIcon(new ImageIcon(bndl.getImage(this, iconTextDirection, IconState.ACTIVE, iconSize)));
            setSelectedIcon(new ImageIcon(bndl.getImage(this, iconTextDirection, IconState.SELECTED, iconSize)));
            setRolloverIcon(new ImageIcon(bndl.getImage(this, iconTextDirection, IconState.MOUSEOVER, iconSize)));
            setRolloverSelectedIcon(new ImageIcon(bndl.getImage(this, iconTextDirection, IconState.SELECTED, iconSize)));
        }
    }
}
