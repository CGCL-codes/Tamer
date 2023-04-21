package org.swinggl;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.RepaintManager;
import org.lwjgl.input.Mouse;

/**
 * <p>GLFrame is top level container that Swing components will be added.</p>
 * @author hakan eryargi (r a f t)
 */
public class GLFrame {

    private static RepaintManager repaintManager = new RepaintManager() {

        public boolean isDoubleBufferingEnabled() {
            return false;
        }
    };

    private final GLGraphics glGraphics = new GLGraphics();

    private final JPanel panel = new JPanel(new BorderLayout());

    private int offsetX, offsetY;

    public GLFrame(JComponent component) throws Exception {
        setDispatcher(panel);
        panel.setOpaque(false);
        panel.add(component, BorderLayout.CENTER);
        panel.setSize(panel.getPreferredSize());
        panel.addNotify();
        panel.validate();
        System.out.println("displayable: " + panel.isDisplayable());
        System.out.println("valid: " + panel.isValid());
    }

    private static void setDispatcher(Container container) throws Exception {
        Field field = Container.class.getDeclaredField("dispatcher");
        field.setAccessible(true);
        System.out.println(field.getType());
        Constructor<?> constructor = field.getType().getDeclaredConstructor(Container.class);
        constructor.setAccessible(true);
        Object dispatcher = constructor.newInstance(container);
        field.set(container, dispatcher);
        System.out.println("created and set dispatcher");
        field = dispatcher.getClass().getDeclaredField("eventMask");
        field.setAccessible(true);
        field.setLong(dispatcher, AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_WHEEL_EVENT_MASK);
        System.out.println("set event mask");
    }

    public void setSize(Dimension d) {
        panel.setSize(d);
    }

    public void setLocation(int x, int y) {
        offsetX = x;
        offsetY = y;
    }

    /** paints this frame to defalt graphics. */
    public void paint() {
        paint(this.glGraphics);
    }

    /** paints this frame to given graphics. */
    public void paint(GLGraphics glGraphics) {
        RepaintManager.setCurrentManager(repaintManager);
        glGraphics.translate(offsetX, offsetY);
        panel.paint(glGraphics);
        glGraphics.translate(-offsetX, -offsetY);
        processMouseEvents();
    }

    private void processMouseEvents() {
        int x = Mouse.getX() - offsetX;
        int y = 600 - Mouse.getY() - offsetY;
        if (Mouse.getDX() != 0 || Mouse.getDY() != 0) {
            MouseEvent event = new MouseEvent(panel, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, 0, 0, false, 0);
            panel.dispatchEvent(event);
        }
        boolean print = false;
        while (Mouse.next()) {
            if (Mouse.getEventButton() != -1) {
                boolean pressed = Mouse.getEventButtonState();
                System.out.println("click:" + Mouse.getEventButton() + (pressed ? " press " : " release "));
                final int button;
                switch(Mouse.getEventButton()) {
                    case 0:
                        button = MouseEvent.BUTTON1;
                        break;
                    case 1:
                        button = MouseEvent.BUTTON2;
                        break;
                    case 2:
                        button = MouseEvent.BUTTON3;
                        break;
                    default:
                        System.out.println("unknown button: " + Mouse.getEventButton() + ", setting to Button1");
                        button = MouseEvent.BUTTON1;
                }
                MouseEvent event = new MouseEvent(panel, pressed ? MouseEvent.MOUSE_PRESSED : MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), 0, x, y, 0, 0, 1, false, button);
                panel.dispatchEvent(event);
            } else {
            }
        }
        if (print) System.out.println();
    }
}
