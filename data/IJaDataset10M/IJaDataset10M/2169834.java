package org.cumt.view.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import org.cumt.model.ViewAttributes;
import org.cumt.model.ui.ControlObject;
import org.cumt.view.BaseComponent;

/**
 * 
 * @author <a href="cdescalzi2001@yahoo.com.ar">Carlos Descalzi</a>
 *
 */
@SuppressWarnings("serial")
public class ControlView extends BaseComponent<ControlObject> {

    public ControlView() {
        setSize(100, 20);
        setOpaque(true);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(hasFocus() ? getFocusForeground() : getForeground());
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        g.setColor(getForeground());
        g.setFont(getFont());
        g.drawString(getModel().getName(), 5, g.getFontMetrics().getHeight());
    }

    @Override
    public void viewToModel() {
        ViewAttributes viewAttributes = getViewAttributes(ViewAttributes.class);
        viewAttributes.setLocation(getLocation());
        viewAttributes.setSize(getSize());
        viewAttributes.setFont(getFont());
        viewAttributes.setBackgroundColor(getBackground());
        viewAttributes.setForegroundColor(getForeground());
    }

    @Override
    public void modelToView() {
        ViewAttributes viewAttributes = getViewAttributes(null);
        if (viewAttributes != null) {
            this.setLocation(viewAttributes.getLocation() != null ? viewAttributes.getLocation() : new Point(10, 10));
            this.setSize(viewAttributes.getSize() != null ? viewAttributes.getSize() : new Dimension(100, 100));
            if (viewAttributes.getBackgroundColor() != null) {
                setBackground(viewAttributes.getBackgroundColor());
            }
            if (viewAttributes.getForegroundColor() != null) {
                setForeground(viewAttributes.getForegroundColor());
            }
            if (viewAttributes.getFont() != null) {
                setFont(viewAttributes.getFont());
            }
        }
    }
}
