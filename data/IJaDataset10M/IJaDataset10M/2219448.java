package net.sourceforge.mandelbroccoli.input;

/**
 *
 * @author Mathias Johansson
 */
public class GamepadEvent {

    public enum EventType {

        AXIS_CHANGED, BUTTON_PRESSED, BUTTON_RELEASED
    }

    protected EventType type = null;

    protected int index = 0;

    protected double value = 0;

    public GamepadEvent(EventType type, int index) {
        this.type = type;
        this.index = index;
    }

    public GamepadEvent(EventType type, int index, double value) {
        this.type = type;
        this.index = index;
        this.value = value;
    }

    public EventType getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }

    public double getValue() {
        return value;
    }
}
