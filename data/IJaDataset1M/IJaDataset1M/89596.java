package org.lwjgl.util.jinput;

import java.io.IOException;
import net.java.games.input.AbstractComponent;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import net.java.games.input.Mouse;
import net.java.games.input.Rumbler;

/**
 * @author elias
 */
final class LWJGLMouse extends Mouse {

    private static final int EVENT_X = 1;

    private static final int EVENT_Y = 2;

    private static final int EVENT_WHEEL = 3;

    private static final int EVENT_BUTTON = 4;

    private static final int EVENT_DONE = 5;

    private int event_state = EVENT_DONE;

    LWJGLMouse() {
        super("LWJGLMouse", createComponents(), new Controller[] {}, new Rumbler[] {});
    }

    private static Component[] createComponents() {
        return new Component[] { new Axis(Component.Identifier.Axis.X), new Axis(Component.Identifier.Axis.Y), new Axis(Component.Identifier.Axis.Z), new Button(Component.Identifier.Button.LEFT), new Button(Component.Identifier.Button.MIDDLE), new Button(Component.Identifier.Button.RIGHT) };
    }

    public synchronized void pollDevice() throws IOException {
        if (!org.lwjgl.input.Mouse.isCreated()) return;
        org.lwjgl.input.Mouse.poll();
        for (int i = 0; i < 3; i++) setButtonState(i);
    }

    private Button map(int lwjgl_button) {
        switch(lwjgl_button) {
            case 0:
                return (Button) getLeft();
            case 1:
                return (Button) getRight();
            case 2:
                return (Button) getMiddle();
            default:
                return null;
        }
    }

    private void setButtonState(int lwjgl_button) {
        Button button = map(lwjgl_button);
        if (button != null) button.setValue(org.lwjgl.input.Mouse.isButtonDown(lwjgl_button) ? 1 : 0);
    }

    protected synchronized boolean getNextDeviceEvent(Event event) throws IOException {
        if (!org.lwjgl.input.Mouse.isCreated()) return false;
        while (true) {
            long nanos = org.lwjgl.input.Mouse.getEventNanoseconds();
            switch(event_state) {
                case EVENT_X:
                    event_state = EVENT_Y;
                    int dx = org.lwjgl.input.Mouse.getEventDX();
                    if (dx != 0) {
                        event.set(getX(), dx, nanos);
                        return true;
                    }
                    break;
                case EVENT_Y:
                    event_state = EVENT_WHEEL;
                    int dy = -org.lwjgl.input.Mouse.getEventDY();
                    if (dy != 0) {
                        event.set(getY(), dy, nanos);
                        return true;
                    }
                    break;
                case EVENT_WHEEL:
                    event_state = EVENT_BUTTON;
                    int dwheel = org.lwjgl.input.Mouse.getEventDWheel();
                    if (dwheel != 0) {
                        event.set(getWheel(), dwheel, nanos);
                        return true;
                    }
                    break;
                case EVENT_BUTTON:
                    event_state = EVENT_DONE;
                    int lwjgl_button = org.lwjgl.input.Mouse.getEventButton();
                    if (lwjgl_button != -1) {
                        Button button = map(lwjgl_button);
                        if (button != null) {
                            event.set(button, org.lwjgl.input.Mouse.getEventButtonState() ? 1f : 0f, nanos);
                            return true;
                        }
                    }
                    break;
                case EVENT_DONE:
                    if (!org.lwjgl.input.Mouse.next()) return false;
                    event_state = EVENT_X;
                    break;
                default:
                    break;
            }
        }
    }

    static final class Axis extends AbstractComponent {

        Axis(Component.Identifier.Axis axis_id) {
            super(axis_id.getName(), axis_id);
        }

        public boolean isRelative() {
            return true;
        }

        protected float poll() throws IOException {
            return 0;
        }

        public boolean isAnalog() {
            return true;
        }
    }

    static final class Button extends AbstractComponent {

        private float value;

        Button(Component.Identifier.Button button_id) {
            super(button_id.getName(), button_id);
        }

        void setValue(float value) {
            this.value = value;
        }

        protected float poll() throws IOException {
            return value;
        }

        public boolean isRelative() {
            return false;
        }

        public boolean isAnalog() {
            return false;
        }
    }
}
