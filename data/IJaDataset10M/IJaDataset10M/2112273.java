package net.sf.juife.swing;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.juife.swing.plaf.DialUI;

/**
 * This class implements a dial knob, which is mainly used in audio applications.
 * @author Grigor Iliev
 */
public class Dial extends JComponent {

    static {
        UIManager.put("DialUI", "net.sf.juife.swing.plaf.basic.BasicDialUI");
    }

    private static final String uiClassID = "DialUI";

    /** Represents how the mouse dragging affects the dial's value. */
    public static enum MouseHandlerMode {

        /**
		 * Indicates that the dial's value will be increased
		 * when dragging from left to right and will be decreased
		 * when dragging from right to left.
		 */
        LEFT_TO_RIGHT, /**
		 * Indicates that the dial's value will be increased
		 * when dragging from right to left and will be decreased
		 * when dragging from left to right.
		 */
        RIGHT_TO_LEFT, /**
		 * Indicates that the dial's value will be increased
		 * when dragging from down to up and will be decreased
		 * when dragging from up to down.
		 */
        DOWN_TO_UP, /**
		 * Indicates that the dial's value will be increased
		 * when dragging from up to down and will be decreased
		 * when dragging from down to up.
		 */
        UP_TO_DOWN, /**
		 * Indicates that both <code>LEFT_TO_RIGHT</code> and
		 * <code>DOWN_TO_UP</code> modes will be handled.
		 */
        LEFT_TO_RIGHT_AND_DOWN_TO_UP, /**
		 * Indicates that both <code>LEFT_TO_RIGHT</code> and
		 * <code>UP_TO_DOWN</code> modes will be handled.
		 */
        LEFT_TO_RIGHT_AND_UP_TO_DOWN, /**
		 * Indicates that both <code>RIGHT_TO_LEFT</code> and
		 * <code>DOWN_TO_UP</code> modes will be handled.
		 */
        RIGHT_TO_LEFT_AND_DOWN_TO_UP, /**
		 * Indicates that both <code>RIGHT_TO_LEFT</code> and
		 * <code>UP_TO_DOWN</code> modes will be handled.
		 */
        RIGHT_TO_LEFT_AND_UP_TO_DOWN, /**
		 * Indicates that, when dragging occurs, the dial's position will
		 * be set to the radial position of the mouse relative to dial's center.
		 */
        RADIAL
    }

    /**
	 * The data model that handles the numeric maximum value,
	 * minimum value, and current-position value for the dial.
	 */
    protected BoundedRangeModel dialModel;

    private ChangeListener changeListener;

    private ImageIcon pixmap = null;

    private ImageIcon disabledPixmap = null;

    private ImageIcon rolloverPixmap = null;

    private ImageIcon pressedPixmap = null;

    private int minAngle = 45;

    private int maxAngle = 315;

    private MouseHandlerMode mouseHandlerMode = MouseHandlerMode.RADIAL;

    /**
	 * Creates a new instance of <code>Dial</code> with a range of 0 to 100
	 * and initial value of 50.
	 */
    public Dial() {
        this(0, 100);
    }

    /**
	 * Creates a new instance of <code>Dial</code> using the specified
	 * <code>min</code> and <code>max</code> with an initial value equal to
	 * the average of the <code>min</code> plus <code>max</code>.
	 * @param min Specifies the minimum value of the dial.
	 * @param max Specifies the maximum value of the dial.
	 * @throws IllegalArgumentException if <code>min</code> is greater then <code>max</code>.
	 */
    public Dial(int min, int max) {
        this(min, max, (min + max) / 2);
    }

    /**
	 * Creates a new instance of <code>Dial</code> using the specified
	 * <code>min</code>, <code>max</code> and <code>value</code>.
	 * @param min Specifies the minimum value of the dial.
	 * @param max Specifies the maximum value of the dial.
	 * @param value Specifies the current value of the dial.
	 * @throws IllegalArgumentException if the following constraints aren't satisfied:
	 * <code>min <= value <= max</code>
	 */
    public Dial(int min, int max, int value) {
        this(new DefaultBoundedRangeModel(value, 0, min, max));
    }

    /**
	 * Creates a new instance of <code>Dial</code> using the specified data model.
	 * @param model Specifies the data model to be used.
	 */
    public Dial(BoundedRangeModel model) {
        changeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                fireStateChanged();
            }
        };
        setModel(model);
        updateUI();
    }

    /**
	 * Gets a string that specifies the name
	 * of the L&F class that renders this component.
	 * @return the string "DialUI"
	 */
    public String getUIClassID() {
        return uiClassID;
    }

    /**
	 * Gets the L&F object that renders this component.
	 * @return The L&F object that renders this component.
	 */
    public DialUI getUI() {
        return (DialUI) ui;
    }

    /**
	 * Sets the L&F object that renders this component.
	 * @param ui The new UI delegate.
	 */
    public void setUI(DialUI ui) {
        super.setUI(ui);
    }

    /**
	 * Resets the UI property to a value from the current look and feel.
	 */
    public void updateUI() {
        setUI((DialUI) UIManager.getUI(this));
    }

    /**
	 * Registers the specified listener to be notified about chages of the data model.
	 * @param listener The <code>ChangeListener</code> to register.
	 */
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    /**
	 * Removes the specified listener.
	 * @param listener The <code>ChangeListener</code> to remove.
	 */
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    /**
	 * Gets the data model that handles the dials' minimum, maximum and value properties.
	 * @return The data model that handles the dials' minimum, maximum and value properties.
	 */
    public BoundedRangeModel getModel() {
        return dialModel;
    }

    /**
	 * Sets the model that handles the dials' minimum, maximum and value properties.
	 * @param model The new non-<code>null</code> model to be set.
	 * @throws IllegalArgumentException if <code>model</code> is <code>null</code>.
	 * @see #getModel
	 */
    public void setModel(BoundedRangeModel model) {
        if (model == null) throw new IllegalArgumentException("model must be non-null");
        BoundedRangeModel oldModel = getModel();
        if (oldModel != null) oldModel.removeChangeListener(changeListener);
        dialModel = model;
        model.addChangeListener(changeListener);
        firePropertyChange("model", oldModel, dialModel);
    }

    /**
	 * Gets the pixmap used for drawing the dial knob.
	 * @return The pixmap used for drawing the dial knob.
	 */
    public ImageIcon getDialPixmap() {
        return pixmap;
    }

    /**
	 * Sets the pixmap to be used for drawing the dial knob
	 * with minimum angle of 45 and maximum angle of 315 degrees.
	 * If <code>pixmap</code> is <code>null</code> the builtin L&F is used.
	 * @param pixmap Specifies the pixmap to be used for drawing the dial knob.
	 * @see #getMinimumAngle
	 * @see #getMaximumAngle
	 */
    public void setDialPixmap(ImageIcon pixmap) {
        setDialPixmap(pixmap, 45, 315);
    }

    /**
	 * Sets the pixmap to be used for drawing the dial knob with the specified
	 * minimum and maximum angle.
	 * @throws IllegalArgumentException if the following constraints aren't satisfied:
	 * <code>0 <= minAngle <= maxAngle <= 360</code>
	 * @see #getMinimumAngle
	 * @see #getMaximumAngle
	 */
    public void setDialPixmap(ImageIcon pixmap, int minAngle, int maxAngle) {
        if (pixmap == getDialPixmap()) return;
        this.pixmap = pixmap;
        if (minAngle < 0 || minAngle > maxAngle || maxAngle > 360) throw new IllegalArgumentException("Invalid angle range");
        this.minAngle = minAngle;
        this.maxAngle = maxAngle;
    }

    /**
	 * Gets the thumb angle (in degrees) of the minimum position of the dial knob.
	 * @return The thumb angle (in degrees) of the minimum position of the dial knob.
	 * @see #setDialPixmap(ImageIcon pixmap, int minAngle, int maxAngle)
	 */
    public int getMinimumAngle() {
        return minAngle;
    }

    /**
	 * Gets the thumb angle (in degrees) of the maximum position of the dial knob.
	 * @return The thumb angle (in degrees) of the maximum position of the dial knob.
	 * @see #setDialPixmap(ImageIcon pixmap, int minAngle, int maxAngle)
	 */
    public int getMaximumAngle() {
        return maxAngle;
    }

    /**
	 * Gets the pixmap used for drawing the dial knob when is disabled.
	 * If <code>null</code>, the pixmap returned by
	 * {@link #getDialPixmap()} is used for drawing the dial knob.
	 * @return The pixmap used for drawing the dial knob when is disabled.
	 */
    public ImageIcon getDisabledDialPixmap() {
        return disabledPixmap;
    }

    /**
	 * Sets the pixmap to be used for drawing the dial knob when is disabled.
	 * If <code>pixmap</code> is <code>null</code> the pixmap returned by
	 * {@link #getDialPixmap()} is used for drawing the dial knob.
	 * @param pixmap Specifies the pixmap to be used for drawing the dial knob when is disabled.
	 */
    public void setDisabledDialPixmap(ImageIcon pixmap) {
        disabledPixmap = pixmap;
    }

    /**
	 * Gets the pixmap used for drawing the dial knob
	 * when dragging is performed or the mouse is over the knob.
	 * If <code>null</code>, the pixmap returned by
	 * {@link #getDialPixmap()} is used for drawing the dial knob.
	 * @return The pixmap used for drawing the dial knob
	 * when dragging is performed or the mouse is over the knob.
	 */
    public ImageIcon getRolloverDialPixmap() {
        return rolloverPixmap;
    }

    /**
	 * Sets the pixmap to be used for drawing the dial knob
	 * when dragging is performed or the mouse is over the knob.
	 * If <code>pixmap</code> is <code>null</code> the pixmap returned by
	 * {@link #getDialPixmap()} is used for drawing the dial knob.
	 * @param pixmap The pixmap to be used for drawing the dial knob
	 * when dragging is performed or the mouse is over the knob.
	 */
    public void setRolloverDialPixmap(ImageIcon pixmap) {
        rolloverPixmap = pixmap;
    }

    /**
	 * Gets the pixmap used for drawing
	 * the dial knob when a mouse button is pressed.
	 * If <code>null</code>, the pixmap returned by
	 * {@link #getDialPixmap()} is used for drawing the dial knob.
	 * @return The pixmap used for drawing
	 * the dial knob when a mouse button is pressed.
	 */
    public ImageIcon getPressedDialPixmap() {
        return pressedPixmap;
    }

    /**
	 * Sets the pixmap to be used for drawing
	 * the dial knob when a mouse button is pressed.
	 * If <code>pixmap</code> is <code>null</code> the pixmap returned by
	 * {@link #getDialPixmap()} is used for drawing the dial knob.
	 * @param pixmap The pixmap to be used for drawing
	 * the dial knob when a mouse button is pressed.
	 */
    public void setPressedDialPixmap(ImageIcon pixmap) {
        pressedPixmap = pixmap;
    }

    /**
	 * Gets the model's maximum acceptable value.
	 * @return The model's maximum acceptable value.
	 */
    public int getMaximum() {
        return getModel().getMaximum();
    }

    /**
	 * Sets the maximum allowed value of this <code>Dial</code>.
	 * @param max the new maximum value of this <code>Dial</code>.
	 */
    public void setMaximum(int max) {
        getModel().setMaximum(max);
    }

    /**
	 * Gets the model's minimum acceptable value.
	 * @return The model's minimum acceptable value.
	 */
    public int getMinimum() {
        return getModel().getMinimum();
    }

    /**
	 * Sets the minimum allowed value of this <code>Dial</code>.
	 * @param min the new minimum value of this <code>Dial</code>.
	 */
    public void setMinimum(int min) {
        getModel().setMinimum(min);
    }

    /**
	 * Gets the current value of the <code>Dial</code>.
	 * @return The current value of the <code>Dial</code>.
	 * @see #setValue
	 */
    public int getValue() {
        return getModel().getValue();
    }

    /**
	 * Sets the current value of the <code>Dial</code>.
	 * @param value Specifies the new value of the <code>Dial</code>.
	 * @see #getValue
	 */
    public void setValue(int value) {
        getModel().setValue(value);
    }

    /**
	 * Determines whether the dial knob is being dragged.
	 * @return <code>true</code> if the dial knob is
	 * being dragged, <code>false</code> otherwise.
	 */
    public boolean getValueIsAdjusting() {
        return getModel().getValueIsAdjusting();
    }

    /**
	 * Sets whether the dial knob is being dragged.
	 * Dial look and feel implementations should set this property to <code>true</code>
	 * when drag begins, and to <code>false</code> when the drag ends.
	 * @param b <code>true</code> to specify that the dial knob is
	 * being dragged, <code>false</code> otherwise.
	 */
    public void setValueIsAdjusting(boolean b) {
        getModel().setValueIsAdjusting(b);
    }

    /**
	 * Gets the mouse handler mode, which determines
	 * how the mouse dragging affects the dial's value.
	 * @return The current mouse handler mode.
	 */
    public MouseHandlerMode getMouseHandlerMode() {
        return mouseHandlerMode;
    }

    /**
	 * Sets the mouse handler mode, which determines
	 * how the mouse dragging affects the dial's value.
	 * @param mouseHandlerMode The new mouse handler mode.
	 */
    public void setMouseHandlerMode(MouseHandlerMode mouseHandlerMode) {
        this.mouseHandlerMode = mouseHandlerMode;
    }

    /**
	 * Gets the value that the dial knob will have if
	 * dragging to point <code>p</code> is made in radial mode.
	 * @param p The point for which the respective dial's value should be obtained.
	 * @return The value that the dial knob will have if
	 * dragging to point <code>p</code> is made in radial mode.
	 * @throws IllegalArgumentException if <code>p</code> is <code>null</code>.
	 */
    public int getValueByPoint(java.awt.Point p) {
        return getUI().getValueByPoint(p);
    }

    private ChangeEvent changeEvent = new ChangeEvent(this);

    /**
	 * Sends a ChangeEvent whose source is this Dial.
	 * This method method is called each time a <code>ChangeEvent</code>
	 * is received from the model.
	 */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}
