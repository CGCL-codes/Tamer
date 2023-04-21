package org.cumt.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import javax.swing.UIManager;
import org.cumt.model.ModelObject;

/**
 * 
 * @author <a href="cdescalzi2001@yahoo.com.ar">Carlos Descalzi</a>
 *
 */
public abstract class BaseComponent<T extends ModelObject> extends BaseView<T> implements Draggable {

    private Color focusForeground = Color.BLUE;

    public BaseComponent() {
        setUpUI();
        setOpaque(true);
        setBackground(Color.WHITE);
    }

    private void setUpUI() {
        Font font = UIManager.getFont(getClass().getSimpleName() + ".font");
        if (font != null) {
            setFont(font);
        } else if ((font = UIManager.getFont("BaseComponent.font")) != null) {
            setFont(font);
        } else {
            setFont(UIManager.getFont("Label.font"));
        }
    }

    public Point getNearestPointTo(Component otherComponent) {
        return getNearestPointTo(otherComponent, new Point());
    }

    public Point getNearestPointTo(Component otherComponent, Point point) {
        int otherCenterX = otherComponent.getX() + otherComponent.getWidth() / 2;
        int otherCenterY = otherComponent.getY() + otherComponent.getHeight() / 2;
        int myCenterX = getX() + getWidth() / 2;
        int myCenterY = getY() + getHeight() / 2;
        double angle = -Math.atan2(otherCenterY - myCenterY, otherCenterX - myCenterX);
        if (angle > Math.PI * 2) {
            angle = Math.PI * 2 - angle;
        } else if (angle < 0) {
            angle = Math.PI * 2 + angle;
        }
        final double q = Math.PI / 3;
        if (angle > q && angle <= q * 2) {
            point.x = getWidth() / 2 + (int) (getWidth() / 2 * Math.cos(angle));
            point.y = 0;
        } else if (angle > q * 2 && angle <= q * 4) {
            point.x = 0;
            point.y = getHeight() / 2 - (int) (getHeight() / 2 * Math.sin(angle));
        } else if (angle > q * 4 && angle <= q * 5) {
            point.x = getWidth() / 2 + (int) (getWidth() / 2 * Math.cos(angle));
            point.y = getHeight();
        } else {
            point.x = getWidth();
            point.y = getHeight() / 2 - (int) (getHeight() / 2 * Math.sin(angle));
        }
        point.x += getX();
        point.y += getY();
        return point;
    }

    public Color getFocusForeground() {
        return focusForeground;
    }

    public void setFocusForeground(Color focusForeground) {
        this.focusForeground = focusForeground;
    }
}
