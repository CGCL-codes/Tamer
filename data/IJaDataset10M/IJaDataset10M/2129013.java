package jdstar.gui;

/**
 * This is an event class.
 */
public final class InputEvent {

    public static final InputEvent SWITCH = new InputEvent();

    public static final InputEvent LEFT = new InputEvent();

    public static final InputEvent DOWN = new InputEvent();

    public static final InputEvent RIGHT = new InputEvent();

    public static final InputEvent UP = new InputEvent();

    public static final InputEvent IDLE = new InputEvent();

    private InputEvent() {
    }
}
