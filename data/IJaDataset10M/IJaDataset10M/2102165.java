package org.fest.swing.testing;

import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.MouseButton.*;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import org.fest.assertions.AssertExtension;
import org.fest.swing.core.MouseButton;

/**
 * Understands a mouse listener that records mouse events.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ClickRecorder extends MouseAdapter implements AssertExtension {

    public static ClickRecorder attachTo(Component target) {
        return new ClickRecorder(target);
    }

    private static final Map<Integer, MouseButton> MOUSE_BUTTON_MAP = new HashMap<Integer, MouseButton>();

    static {
        MOUSE_BUTTON_MAP.put(MouseEvent.BUTTON1, LEFT_BUTTON);
        MOUSE_BUTTON_MAP.put(MouseEvent.BUTTON2, MIDDLE_BUTTON);
        MOUSE_BUTTON_MAP.put(MouseEvent.BUTTON3, RIGHT_BUTTON);
    }

    private MouseButton clickedButton;

    private int clickCount;

    private Point pointClicked;

    private ClickRecorder(Component target) {
        attach(this, target);
    }

    private static void attach(ClickRecorder mouseListener, Component target) {
        target.addMouseListener(mouseListener);
        if (!(target instanceof Container)) return;
        for (Component c : ((Container) target).getComponents()) attach(mouseListener, c);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        clickedButton = MOUSE_BUTTON_MAP.get(e.getButton());
        clickCount = e.getClickCount();
        pointClicked = e.getPoint();
    }

    public ClickRecorder wasNotClicked() {
        assertThat(clickedButton).isNull();
        return this;
    }

    public ClickRecorder clicked(MouseButton button) {
        assertThat(clickedButton).isEqualTo(button);
        return this;
    }

    public ClickRecorder timesClicked(int times) {
        assertThat(clickCount).isEqualTo(times);
        return this;
    }

    public ClickRecorder wasClicked() {
        return clicked(LEFT_BUTTON).timesClicked(1);
    }

    public ClickRecorder wasDoubleClicked() {
        return clicked(LEFT_BUTTON).timesClicked(2);
    }

    public ClickRecorder wasRightClicked() {
        return clicked(RIGHT_BUTTON).timesClicked(1);
    }

    public ClickRecorder clickedAt(Point p) {
        assertThat(pointClicked).isEqualTo(p);
        return this;
    }

    public Point pointClicked() {
        return pointClicked;
    }
}
