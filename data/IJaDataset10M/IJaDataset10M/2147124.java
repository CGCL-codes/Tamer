package com.volantis.mcs.eclipse.controls;

import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

/**
 * A button which invokes the system's color dialog for choosing colors
 * and displays the selected color.
 */
public class ColorButton extends Composite {

    /**
     * The button used by the color button.
     */
    private Button button;

    /**
     * The color image drawn on the button.
     */
    private Image colorSquare;

    /**
     * The selected color in RGB format.
     */
    private RGB currentRGB;

    /**
     * The color dialog.
     */
    private ColorDialog dialog;

    /**
     * The color button's list of registered listeners.
     */
    private ListenerList listeners;

    /**
     * The color button's initial background, immediately
     * after creation.
     */
    private Color buttonBackground;

    /**
     * The left and right margin spacings used when drawing the color image.
     */
    private static final int MARGIN_LEFT = ControlsMessages.getInteger("ColorButton.marginLeft").intValue();

    /**
     * The top and bottom margin spacings used when drawing the color image.
     */
    private static final int MARGIN_TOP = ControlsMessages.getInteger("ColorButton.marginTop").intValue();

    /**
     * Constructs a color button.
     * @param parent the parent Composite
     * @param style the style
     */
    public ColorButton(Composite parent, int style) {
        super(parent, style);
        addButton(style);
        listeners = new ListenerList();
        initAccessible();
    }

    /**
     * Creates and adds a button.
     * @param style
     */
    private void addButton(int style) {
        button = new Button(this, style);
        buttonBackground = button.getBackground();
        button.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent se) {
                RGB newRGB = openColorDialog();
                if (newRGB != currentRGB) {
                    currentRGB = newRGB;
                    drawColorSquare();
                    Event e = new Event();
                    e.widget = se.widget;
                    e.data = currentRGB;
                    ModifyEvent me = new ModifyEvent(e);
                    fireModifyEvent(me);
                }
            }
        });
        button.addDisposeListener(new DisposeListener() {

            public void widgetDisposed(DisposeEvent event) {
                if (colorSquare != null) {
                    colorSquare.dispose();
                    colorSquare = null;
                }
                if (buttonBackground != null) {
                    buttonBackground.dispose();
                    buttonBackground = null;
                }
            }
        });
    }

    /**
     * Create an image using the color chosen with the
     * color dialog, and set the button's image to it.
     */
    private void drawColorSquare() {
        int width = button.getSize().x;
        int height = button.getSize().y;
        if (colorSquare != null) {
            colorSquare.dispose();
        }
        colorSquare = new Image(this.getDisplay(), width, height);
        GC colorSquareGC = new GC(colorSquare);
        colorSquareGC.setBackground(buttonBackground);
        colorSquareGC.fillRectangle(0, 0, width, height);
        if (currentRGB != null) {
            Color newColor = new Color(this.getDisplay(), currentRGB);
            colorSquareGC.setBackground(newColor);
            colorSquareGC.fillRectangle(MARGIN_LEFT, MARGIN_TOP, width - 2 * MARGIN_LEFT, height - 2 * MARGIN_TOP);
            newColor.dispose();
        }
        button.setImage(colorSquare);
        colorSquareGC.dispose();
    }

    /**
     * Fires a ModifyText event to all registered listeners.
     * @param modifyEvent the ModifyEvent. modifyEvent.data
     * contains the RGB representation of the selected
     * color.
     */
    private void fireModifyEvent(ModifyEvent modifyEvent) {
        Object[] interested = listeners.getListeners();
        for (int i = 0; i < interested.length; i++) {
            if (interested[i] != null) {
                ((ModifyListener) interested[i]).modifyText(modifyEvent);
            }
        }
    }

    /**
     * Gets the currently selected color for the color button.
     * @return the selected color in RGB format
     */
    public RGB getColor() {
        return currentRGB;
    }

    /**
    * Sets the current color for the color button.
    * @param color the color to set
    */
    public void setColor(RGB color) {
        changeColor(color);
    }

    /**
     * Opens the color dialog and returns the selected color.
     * @return the selected color in RGB format
     */
    private RGB openColorDialog() {
        if (dialog == null) {
            dialog = new ColorDialog(getShell());
        }
        dialog.setRGB(currentRGB);
        RGB rgb = dialog.open();
        return rgb == null ? currentRGB : rgb;
    }

    /**
     * Adds a listener which listens for color selection changes via
     * ModifyText events.
     * @param listener the listener which listens for color selections.
     * The event returns the RGB representation of the selected color in
     * the data field of the ModifyEvent object.
     */
    public void addModifyListener(ModifyListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener
     * @param listener the listener which listens to color modifications
     * via ModifyText events.
     */
    public void removeModifyListener(ModifyListener listener) {
        listeners.remove(listener);
    }

    /**
     * Updates the color button with the selected color
     * from the color dialog.
     * @param rgb the selected color in RGB format
     */
    private void changeColor(RGB rgb) {
        currentRGB = rgb;
        drawColorSquare();
        button.redraw();
    }

    /**
     * Gets the size of the color button.
     * @return a Point describing the size
     */
    public Point getSize() {
        return button.getSize();
    }

    /**
     * Sets the size of the color button
     * @param size a Point describing the size
     */
    public void setSize(Point size) {
        button.setSize(size);
    }

    /**
     * Sets the size of the color button
     * @param width the width of the button
     * @param height the height of the button
     */
    public void setSize(int width, int height) {
        button.setSize(width, height);
    }

    /**
     * Overridden to set the focus to the underlying button.
     */
    public boolean setFocus() {
        return button.setFocus();
    }

    /**
     * Initialise accessibility listeners for this control.
     */
    private void initAccessible() {
        SingleComponentACL acl = new SingleComponentACL() {

            public void getValue(AccessibleControlEvent ae) {
                ae.result = currentRGB.toString();
            }
        };
        acl.setControl(this);
        acl.setRole(ACC.ROLE_PUSHBUTTON);
        getAccessible().addAccessibleControlListener(acl);
        StandardAccessibleListener al = new StandardAccessibleListener(this);
        getAccessible().addAccessibleListener(al);
    }
}
