package de.anormalmedia.touchui.component.text;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.TextBox;
import de.anormalmedia.touchui.Dimension;
import de.anormalmedia.touchui.Insets;
import de.anormalmedia.touchui.Point;
import de.anormalmedia.touchui.UIUtils;
import de.anormalmedia.touchui.border.LineBorder;
import de.anormalmedia.touchui.component.Component;
import de.anormalmedia.touchui.component.Container;
import de.anormalmedia.touchui.component.label.Label;
import de.anormalmedia.touchui.interfaces.IPointable;
import de.anormalmedia.touchui.interfaces.IScrollable;
import de.anormalmedia.touchui.layout.GridLayout;
import de.anormalmedia.touchui.paint.Color;
import de.anormalmedia.touchui.paint.GradientPaint;

public abstract class AbstractTextComponent extends Container implements IPointable, IScrollable {

    private Color foreground = new Color(0, 0, 0);

    private String text = "";

    private boolean pressed;

    private Point point;

    public AbstractTextComponent(String text) {
        setBorder(new LineBorder(new Color(100, 100, 100)));
        setBackground(new GradientPaint(new Color(250, 250, 250), new Color(180, 180, 180), 0.8f, GradientPaint.PAINT_VERTICAL));
        setInsets(new Insets(2, 2, 2, 2));
        setText(text);
        setLayout(new GridLayout(1));
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null) {
            text = "";
        }
        this.text = text;
        while (hasChildren()) {
            remove((Component) getChildren().elementAt(0));
        }
        Label label = new Label(text);
        label.setInsets(new Insets(0, 0, 0, 0));
        label.setForeground(getForeground());
        add(label);
    }

    public boolean pointerDragged(int x, int y) {
        pressed = false;
        return false;
    }

    public boolean pointerPressed(int x, int y) {
        boolean containsPoint = getVisibleRect().containsPoint(x, y);
        if (containsPoint) {
            pressed = true;
        }
        return false;
    }

    public boolean pointerReleased(int x, int y) {
        boolean containsPoint = getVisibleRect().containsPoint(x, y);
        if (containsPoint && pressed) {
            Display display = UIUtils.getDisplay(this);
            final Displayable current = display.getCurrent();
            final TextBox tb = new TextBox("Bitte Text eingeben", getText(), 1024, javax.microedition.lcdui.TextField.ANY);
            tb.addCommand(new Command("OK", Command.OK, 1));
            tb.addCommand(new Command("Cancel", Command.CANCEL, 0));
            tb.setCommandListener(new CommandListener() {

                public void commandAction(Command c, Displayable d) {
                    if (c.getCommandType() == Command.OK) {
                        AbstractTextComponent.this.setText(tb.getString());
                    }
                    UIUtils.getDisplay(AbstractTextComponent.this).setCurrent(current);
                }
            });
            display.setCurrent(tb);
        }
        return containsPoint;
    }

    public Point getViewPosition() {
        return point == null ? new Point(0, 0) : point;
    }

    public void setViewPosition(Point point) {
        if (point.getX() < 0) {
            point.setX(0);
        }
        if (point.getY() < 0) {
            point.setY(0);
        }
        this.point = point;
    }

    public Dimension getPreferredSize() {
        return getLayout().getLayoutSize(this);
    }
}
