package org.wings;

import org.wings.style.AttributeSet;
import org.wings.style.CSSStyleSheet;
import org.wings.style.SimpleAttributeSet;
import org.wings.style.Style;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * An abstract class, which compounds icon and text. It is the base class for 
 * {@link SAbstractButton} and {@link SClickable}. It supports 7 different icon
 * states. 
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision: 1759 $
 */
public abstract class SAbstractIconTextCompound extends SComponent implements SSelectionComponent, ItemSelectable {

    public static final int ICON_COUNT = 7;

    public static final int DISABLED_ICON = 0;

    public static final int DISABLED_SELECTED_ICON = 1;

    public static final int ENABLED_ICON = 2;

    public static final int SELECTED_ICON = 3;

    public static final int ROLLOVER_ICON = 4;

    public static final int ROLLOVER_SELECTED_ICON = 5;

    public static final int PRESSED_ICON = 6;

    private SButtonModel model;

    /** 
     * The button model's <code>changeListener</code>.
     */
    protected ChangeListener changeListener = null;

    protected transient ChangeEvent changeEvent = null;

    /** the text the button is showing */
    private String text;

    /**
     * TODO: documentation
     */
    private String selectionStyle;

    /** The dynamic attributes of selected cells */
    protected AttributeSet selectionAttributes;

    /**
     * The icon to be displayed
     */
    private SIcon icon;

    /**
     * TODO: documentation
     */
    private SIcon disabledIcon;

    /**
     * TODO: documentation
     */
    private SIcon selectedIcon;

    /**
     * TODO: documentation
     */
    private SIcon pressedIcon;

    /**
     * TODO: documentation
     */
    private SIcon disabledSelectedIcon;

    /**
     * TODO: documentation
     */
    private SIcon rolloverIcon;

    /**
     * TODO: documentation
     */
    private SIcon rolloverSelectedIcon;

    /**
     * TODO: documentation
     */
    private int verticalTextPosition = CENTER;

    /**
     * TODO: documentation
     */
    private int horizontalTextPosition = RIGHT;

    /**
     * TODO: documentation
     */
    private int iconTextGap = 0;

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     */
    protected boolean noBreak = false;

    /**
     *
     */
    private boolean imageAbsBottom = false;

    /**
     *
     */
    private boolean delayEvents = false;

    /**
     * Create a button with given text.
     * @param text the button text
     */
    public SAbstractIconTextCompound(String text) {
        setText(text);
        model = new SDefaultButtonModel();
    }

    /**
     * Creates a new submit button
     */
    public SAbstractIconTextCompound() {
        this("");
    }

    public SButtonModel getModel() {
        return model;
    }

    public void setModel(SButtonModel model) {
        if (model == null) throw new IllegalArgumentException("null not allowed");
        this.model = model;
    }

    /**
     * Returns the selected items or null if no items are selected.
     */
    public Object[] getSelectedObjects() {
        return model.isSelected() ? new Object[] { this } : null;
    }

    /**
     * Adds a ItemListener to the button.
     * @see #removeItemListener(ItemListener)
     */
    public void addItemListener(ItemListener il) {
        addEventListener(ItemListener.class, il);
    }

    /**
     * Remove the given itemListener from list of
     * item listeners.
     * @see #addItemListener(ItemListener)
     */
    public void removeItemListener(ItemListener il) {
        removeEventListener(ItemListener.class, il);
    }

    /**
     * Adds a <code>ChangeListener</code> to the button.
     * @param l the listener to be added
     */
    public void addChangeListener(ChangeListener l) {
        addEventListener(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from the button.
     * @param l the listener to be removed
     */
    public void removeChangeListener(ChangeListener l) {
        removeEventListener(ChangeListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for
     * notification on this event type.  The event instance 
     * is lazily created using the parameters passed into 
     * the fire method.
     * @see EventListenerList
     */
    protected void fireStateChanged() {
        Object[] listeners = getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null) changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setHorizontalTextPosition(int textPosition) {
        horizontalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getHorizontalTextPosition() {
        return horizontalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param textPosition
     */
    public void setVerticalTextPosition(int textPosition) {
        verticalTextPosition = textPosition;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getVerticalTextPosition() {
        return verticalTextPosition;
    }

    /**
     * TODO: documentation
     *
     * @param gap
     */
    public void setIconTextGap(int gap) {
        iconTextGap = gap;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getIconTextGap() {
        return iconTextGap;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, icon, i);
        icon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getIcon() {
        return icon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setPressedIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, pressedIcon, i);
        pressedIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getPressedIcon() {
        return pressedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setRolloverIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, rolloverIcon, i);
        rolloverIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getRolloverIcon() {
        return rolloverIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setRolloverSelectedIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, rolloverSelectedIcon, i);
        rolloverSelectedIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getRolloverSelectedIcon() {
        return rolloverSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setSelectedIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, selectedIcon, i);
        selectedIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getSelectedIcon() {
        return selectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledSelectedIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, disabledSelectedIcon, i);
        disabledSelectedIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getDisabledSelectedIcon() {
        return disabledSelectedIcon;
    }

    /**
     * TODO: documentation
     *
     * @param i
     */
    public void setDisabledIcon(SIcon i) {
        reloadIfChange(ReloadManager.RELOAD_CODE, disabledIcon, i);
        disabledIcon = i;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public SIcon getDisabledIcon() {
        return disabledIcon;
    }

    /**
     * If the text of the button should not be wrapped, set this to true. This
     * inserts a &lt;NOBREAK&gt; Tag around the label
     * @see #isNoBreak()
     * @param b
     */
    public void setNoBreak(boolean b) {
        noBreak = b;
    }

    /**
     * Test, if "noBreak" is set for this button.
     * @see #setNoBreak(boolean)
     * @return true, if nobreak is set, false otherwise.
     */
    public boolean isNoBreak() {
        return noBreak;
    }

    /**
     * @param selectionStyle
     */
    public void setSelectionStyle(String selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    /**
     * @return
     */
    public String getSelectionStyle() {
        return selectionStyle;
    }

    /**
     * Set the selectionAttributes.
     * @param newAttributes the selectionAttributes
     */
    public void setSelectionAttributes(AttributeSet newAttributes) {
        if (newAttributes == null) throw new IllegalArgumentException("null not allowed");
        reloadIfChange(ReloadManager.RELOAD_STYLE, selectionAttributes, newAttributes);
        selectionAttributes = newAttributes;
    }

    /**
     * @return the current selectionAttributes
     */
    public AttributeSet getSelectionAttributes() {
        return selectionAttributes == null ? AttributeSet.EMPTY_ATTRIBUTESET : selectionAttributes;
    }

    /**
     * Set the background color.
     * @param color the new background color
     */
    public void setSelectionBackground(Color color) {
        if (selectionAttributes == null) {
            selectionAttributes = new SimpleAttributeSet();
        }
        boolean changed = selectionAttributes.putAll(CSSStyleSheet.getAttributes(color, Style.BACKGROUND_COLOR));
        if (changed) reload(ReloadManager.RELOAD_STYLE);
    }

    /**
     * Return the background color.
     * @return the background color
     */
    public Color getSelectionBackground() {
        return selectionAttributes == null ? null : CSSStyleSheet.getBackground(selectionAttributes);
    }

    /**
     * Set the foreground color.
     * @param color the foreground color of selected cells
     */
    public void setSelectionForeground(Color color) {
        if (selectionAttributes == null) {
            selectionAttributes = new SimpleAttributeSet();
        }
        boolean changed = selectionAttributes.putAll(CSSStyleSheet.getAttributes(color, Style.COLOR));
        if (changed) reload(ReloadManager.RELOAD_STYLE);
    }

    /**
     * Return the foreground color.
     * @return the foreground color
     */
    public Color getSelectionForeground() {
        return selectionAttributes == null ? null : CSSStyleSheet.getForeground(selectionAttributes);
    }

    /**
     * Sets the label of the button.
     *
     * @param t
     */
    public void setText(String t) {
        reloadIfChange(ReloadManager.RELOAD_CODE, text, t);
        text = t;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public boolean isSelected() {
        return model.isSelected();
    }

    /**
     * Toggle the selection. If the new selection
     * is different to the old selection 
     * an {@link ItemEvent} is raised.
     */
    public void setSelected(boolean selected) {
        if (model.isSelected() != selected) {
            model.setSelected(selected);
            fireStateChanged();
            reload(ReloadManager.RELOAD_CODE | ReloadManager.RELOAD_STYLE);
        }
    }

    public void setImageAbsBottom(boolean t) {
        imageAbsBottom = t;
    }

    public boolean isImageAbsBottom() {
        return imageAbsBottom;
    }

    /**
     * Sets the proper icons for buttonstatus enabled resp. disabled.
     */
    public void setIcons(SIcon[] icons) {
        setIcon(icons[ENABLED_ICON]);
        setDisabledIcon(icons[DISABLED_ICON]);
        setDisabledSelectedIcon(icons[DISABLED_SELECTED_ICON]);
        setRolloverIcon(icons[ROLLOVER_ICON]);
        setRolloverSelectedIcon(icons[ROLLOVER_SELECTED_ICON]);
        setPressedIcon(icons[PRESSED_ICON]);
        setSelectedIcon(icons[SELECTED_ICON]);
    }

    private ItemEvent delayedItemEvent;

    protected final void delayEvents(boolean b) {
        delayEvents = b;
    }

    protected final boolean shouldDelayEvents() {
        return delayEvents;
    }

    /**
     * Reports a selection change.
     * @param ie report this event to all listeners
     * @see java.awt.event.ItemListener
     * @see java.awt.ItemSelectable
     */
    protected void fireItemStateChanged(ItemEvent ie) {
        if (ie == null) return;
        if (delayEvents) {
            delayedItemEvent = ie;
            return;
        }
        Object[] listeners = getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(ie);
            }
        }
    }

    public void fireIntermediateEvents() {
        if (delayEvents && delayedItemEvent != null) {
            delayEvents = false;
            fireItemStateChanged(delayedItemEvent);
            delayEvents = true;
        }
    }

    public void fireFinalEvents() {
        delayEvents = false;
    }
}
