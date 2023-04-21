package jaron.gui;

import jaron.components.Signal;
import jaron.components.SignalEvent;
import jaron.components.SignalListener;

/**
 * The <code>ActuatorXY</code> class is a generic GUI control that provides the
 * functionality to implement a component with an x- and a y-axis (e.g. a joystick).<br>
 * <br>
 * By default positive x is to the right and positive y is up. Reversing is done
 * by swapping the high and the low signal values.
 * 
 * @author      jarontec gmail com
 * @version     1.2
 * @since       1.0
 */
public class ActuatorXY extends Rect implements SignalListener {

    public Rect control;

    private Axis axisX;

    private Axis axisY;

    private Boolean mousePressed = false;

    private Boolean mouseOver = false;

    private Boolean springX = false;

    private Boolean springY = false;

    private int mouseOffsetX = 0;

    private int mouseOffsetY = 0;

    private Signal power;

    /**
   * Creates a new <code>ActuatorXY</code> object. It provides the basic functionality
   * to implement a two axis GUI element (e.g. a joystick or slider or the like).
   * 
   * @param left          left position
   * @param top           top position
   * @param width         the width of the actuator
   * @param height        the height of the actuator
   * @param controlWidth  the width of the control (e.g. a stick or a lever)
   * @param controlHeight the width of the control (e.g. a stick or a lever)
   */
    public ActuatorXY(int left, int top, int width, int height, int controlWidth, int controlHeight) {
        super(left, top, width, height);
        control = new Rect(0, 0, controlWidth, controlHeight);
        axisX = new Axis(left + (controlWidth / 2), width - controlWidth);
        axisY = new Axis(top + (controlHeight / 2), height - controlHeight);
        axisX.addSignalListener(this);
        axisY.addSignalListener(this);
        setBandwidthX(-1, 1);
        setBandwidthY(-1, 1);
        setValueX(0);
        setValueY(0);
        power = new Signal();
        power.setBandwidth(0, 1);
        power.setValue(power.getHigh());
        power.addSignalListener(axisX.getPowerSignal());
        power.addSignalListener(axisY.getPowerSignal());
    }

    /**
   * Creates a new <code>ActuatorXY</code> object. It provides the basic functionality
   * to implement a two axis GUI element (e.g. a joystick or slider or the like).
   * 
   * @param left          left position
   * @param top           top position
   * @param width         the width of the actuator
   * @param height        the height of the actuator
   */
    public ActuatorXY(int left, int top, int width, int height) {
        this(left, top, width, height, width / 10, height / 10);
    }

    /**
   * Adds a listener to the x-axis. In case of a change of the x-axis' value,
   * all the listeners are informed through the <code>EventListener</code>
   * mechanism.
   * 
   * @param listener    the listener to be added to the x-axis
   * @see SignalListener
   */
    public void addListenerX(SignalListener listener) {
        axisX.addSignalListener(listener);
    }

    /**
   * Adds a listener to the y-axis. In case of a change of the y-axis' value,
   * all the listeners are informed through the <code>EventListener</code>
   * mechanism.
   * 
   * @param listener    the listener to be added to the y-axis
   * @see SignalListener
   */
    public void addListenerY(SignalListener listener) {
        axisY.addSignalListener(listener);
    }

    /**
   * Returns a reference to a <code>Signal</code> object containing a value that
   * represents the current status of the actuator. The status is either on
   * (<code>getSignal</code> equals <code>getHigh</code>) or off
   * (<code>getSignal</code> equals <code>getLow</code>).<br> In its off status
   * the actuator doesn't react on user actions.
   * 
   * @return      a <code>Signal</code> object containing the current status
   * @see         Signal
   */
    public Signal getPowerSignal() {
        return power;
    }

    /**
   * Returns a <code>Signal</code> object containing a value that represents the
   * current value of the x-axis.<br>
   * The default bandwidth of the signal is +-1 and can be changed through the
   * <code>setHigh</code> and <code>setLow</code> methods of the Signal class.
   * 
   * @return      a <code>Signal</code> object containing the current x-axis value
   * @see         Signal
   */
    public Signal getSignalX() {
        return axisX;
    }

    /**
   * Returns a <code>Signal</code> object containing a value that represents the
   * current value of the Y-axis.<br>
   * The default bandwidth of the signal is +-1 and can be changed through the
   * <code>setHigh</code> and <code>setLow</code> methods of the Signal class.
   * 
   * @return      a <code>Signal</code> object containing the current y-axis value
   * @see         Signal
   */
    public Signal getSignalY() {
        return axisY;
    }

    /**
   * Returns the current value of the x-axis.
   * 
   * @return    the value of the x-axis
   * @see       Signal
   */
    public double getValueX() {
        return axisX.getValue();
    }

    /**
   * Returns the current value of the y-axis.
   * 
   * @return    the value of the y-axis
   * @see       Signal
   */
    public double getValueY() {
        return axisY.getValue();
    }

    /**
   * Returns true if the mouse is hovering over the actuator.
   * 
   * @return  true or false
   */
    public Boolean isMouseOver() {
        return mouseOver;
    }

    /**
   * Returns true if the user has clicked into the actuator.
   * 
   * @return  true or false
   */
    public Boolean isMousePressed() {
        return mousePressed;
    }

    /**
   * Handles a <code>mouseDragged</code> event that occurred in the GUI.<br>
   * This method should usually be called from the <code>mouseDragged</code>
   * method of the GUI (e.g. in the Processing Development Environment). This
   * ensures that the user interaction is received and processed by the actuator.
   * 
   * @param x     the current x value of the mouse
   * @param y     the current y value of the mouse
   */
    public void mouseDragged(int x, int y) {
        if (mousePressed) {
            axisX.setPosition(x + mouseOffsetX);
            axisY.setPosition(y + mouseOffsetY);
        }
    }

    /**
   * Handles a <code>mousePressed</code> event that occurred in the GUI.<br>
   * This method should usually be called from the <code>mousePressed</code>
   * method of the GUI (e.g. in the Processing Development Environment). This
   * ensures that the user interaction is received and processed by the actuator.
   * 
   * @param x     the current x value of the mouse
   * @param y     the current y value of the mouse
   */
    public void mousePressed(int x, int y) {
        if (control.contains(x, y) && power.getValue() == power.getHigh()) {
            mousePressed = true;
            mouseOffsetX = (int) axisX.getPosition() - x;
            mouseOffsetY = (int) axisY.getPosition() - y;
            axisX.setPosition(x + mouseOffsetX);
            axisY.setPosition(y + mouseOffsetY);
        }
    }

    /**
   * Handles a <code>mouseReleased</code> event that occurred in the GUI.<br>
   * This method should usually be called from the <code>mouseReleased</code>
   * method of the GUI (e.g. in the Processing Development Environment). This
   * ensures that the user interaction is received and processed by the actuator.
   * 
   * @param x     the current x value of the mouse
   * @param y     the current y value of the mouse
   */
    public void mouseReleased(int x, int y) {
        if (mousePressed && power.getValue() == power.getHigh()) {
            mousePressed = false;
            mouseOffsetX = 0;
            mouseOffsetY = 0;
            if (springY) {
                axisY.setValue((axisY.getBandwidth() / 2) + axisY.getLow());
            }
            if (springX) {
                axisX.setValue((axisX.getBandwidth() / 2) + axisX.getLow());
            }
        }
        if (control.contains(x, y)) {
            mouseOver = false;
        }
    }

    /**
   * Handles a <code>mouseMoved</code> event that occurred in the GUI.<br>
   * This method should usually be called from the <code>mouseMoved</code>
   * method of the GUI (e.g. in the Processing Development Environment). This
   * ensures that the user interaction is received and processed by the actuator.
   * 
   * @param x     the current x value of the mouse
   * @param y     the current y value of the mouse
   */
    public void mouseMoved(int x, int y) {
        if (control.contains(x, y) && power.getValue() == power.getHigh()) {
            mouseOver = true;
        } else {
            mouseOver = false;
        }
    }

    /**
   * Sets the low and the high values of the signal for the x-axis.
   * 
   * @param low   the low end of the bandwidth
   * @param high  the high end of the bandwidth
   */
    public void setBandwidthX(double low, double high) {
        axisX.setLow(low);
        axisX.setHigh(high);
        setValueX(getValueX());
    }

    /**
   * Sets the low and the high values of the signal for the y-axis.
   * 
   * @param low   the low end of the bandwidth
   * @param high  the high end of the bandwidth
   */
    public void setBandwidthY(double low, double high) {
        axisY.setLow(high);
        axisY.setHigh(low);
        setValueY(getValueY());
    }

    /**
   * Sets the size of the control.
   * 
   * @param width   the control's new width
   * @param height  the control's new height
   */
    public void setControlSize(int width, int height) {
        control.setSize(width, height);
        axisX.setStart(getLeft() + (width / 2));
        axisX.setPath(getWidth() - width);
        axisY.setStart(getTop() + (height / 2));
        axisY.setPath(getHeight() - height);
        updateControl();
    }

    @Override
    public void setLocation(int left, int top) {
        axisX.setLocation(left + (control.getWidth() / 2));
        axisY.setLocation(top + (control.getHeight() / 2));
        super.setLocation(left, top);
        updateControl();
    }

    /**
   * Sets the actuator's x-axis locked (<code>true</code>) or unlocked (<code>false</code>).
   * The purpose of this functionality is a one axis actuator whose other, unused axis
   * is locked and therefore can't be moved by the user.
   * 
   * @param state   either <code>true</code> or <code>false</code>
   */
    public void setLockedX(Boolean state) {
        Signal powerX = axisX.getPowerSignal();
        if (state) powerX.setValue(powerX.getLow()); else powerX.setValue(powerX.getHigh());
    }

    /**
   * Sets the actuator's y-axis locked (<code>true</code>) or unlocked (<code>false</code>).
   * The purpose of this functionality is a one axis actuator whose other, unused axis
   * is locked and therefore can't be moved by the user.
   * 
   * @param state   either <code>true</code> or <code>false</code>
   */
    public void setLockedY(Boolean state) {
        Signal powerY = axisY.getPowerSignal();
        if (state) powerY.setValue(powerY.getLow()); else powerY.setValue(powerY.getHigh());
    }

    /**
   * Sets the actuator's x-axis to act like it had a spring that moves the
   * control back to its neutral/middle position after release.
   * 
   * @param state   either <code>true</code> or <code>false</code>
   */
    public void setSpringX(Boolean state) {
        this.springX = state;
    }

    /**
   * Sets the actuator's y-axis to act like it had a spring that moves the
   * control back to its neutral/middle position after release.
   * 
   * @param state   either <code>true</code> or <code>false</code>
   */
    public void setSpringY(Boolean state) {
        this.springY = state;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        axisX.setPath(width - control.getWidth());
        axisY.setPath(height - control.getHeight());
        updateControl();
    }

    /**
   * Sets the value of the actuator's x-axis. If the actuator's power
   * is off then the value won't be changed.
   * 
   * @param value   the new value of the x-axis
   * @see           Signal
   */
    public void setValueX(double value) {
        axisX.setValue(value);
    }

    /**
   * Sets the value of the actuator's y-axis. If the actuator's power
   * is off then the value won't be changed.
   * 
   * @param     value the new value of the y-axis
   * @see       Signal
   */
    public void setValueY(double value) {
        axisY.setValue(value);
    }

    public void setValue(double value) {
    }

    public void signalChanged(SignalEvent event) {
        updateControl();
    }

    private void updateControl() {
        control.setLocation(axisX.getPosition() - (control.getWidth() / 2), axisY.getPosition() - (control.getHeight() / 2));
    }
}
