package org.geometerplus.zlibrary.ui.swing.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;
import org.geometerplus.zlibrary.core.view.ZLViewWidget;
import org.geometerplus.zlibrary.core.view.ZLView;
import org.geometerplus.zlibrary.core.application.ZLApplication;

public final class ZLSwingViewWidget extends ZLViewWidget implements MouseListener, MouseMotionListener {

    @SuppressWarnings("serial")
    private class Panel extends JPanel {

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            final ZLView view = ZLApplication.Instance().getCurrentView();
            final ZLSwingPaintContext context = (ZLSwingPaintContext) view.Context;
            final Dimension size = getSize();
            final int rotation = getRotation();
            context.setRotation(rotation);
            switch(rotation) {
                case Angle.DEGREES0:
                    context.setSize(size.width, size.height);
                    break;
                case Angle.DEGREES90:
                    g2d.rotate(-Math.PI / 2);
                    g2d.translate(-size.height, 0);
                    context.setSize(size.height, size.width);
                    break;
                case Angle.DEGREES180:
                    g2d.rotate(Math.PI);
                    g2d.translate(-size.width, -size.height);
                    context.setSize(size.width, size.height);
                    break;
                case Angle.DEGREES270:
                    g2d.rotate(Math.PI / 2);
                    g2d.translate(0, -size.width);
                    context.setSize(size.height, size.width);
                    break;
            }
            context.setGraphics(g2d);
            view.paint();
        }
    }

    public ZLSwingViewWidget(int initialAngle) {
        super(initialAngle);
        myPanel.addMouseListener(this);
        myPanel.addMouseMotionListener(this);
    }

    public void repaint() {
        myPanel.repaint();
    }

    public void mouseClicked(MouseEvent e) {
    }

    private boolean myMouseIsPressed;

    private int x(MouseEvent e) {
        switch(getRotation()) {
            case Angle.DEGREES0:
            default:
                return e.getX();
            case Angle.DEGREES90:
                return myPanel.getSize().height - e.getY() - 1;
            case Angle.DEGREES180:
                return myPanel.getSize().height - e.getX() - 1;
            case Angle.DEGREES270:
                return e.getY();
        }
    }

    private int y(MouseEvent e) {
        switch(getRotation()) {
            case Angle.DEGREES0:
            default:
                return e.getY();
            case Angle.DEGREES90:
                return e.getX();
            case Angle.DEGREES180:
                return myPanel.getSize().height - e.getY() - 1;
            case Angle.DEGREES270:
                return myPanel.getSize().height - e.getX() - 1;
        }
    }

    public void mousePressed(MouseEvent e) {
        ZLApplication.Instance().getCurrentView().onStylusPress(x(e), y(e));
    }

    public void mouseReleased(MouseEvent e) {
        ZLApplication.Instance().getCurrentView().onStylusRelease(x(e), y(e));
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        ZLApplication.Instance().getCurrentView().onStylusMovePressed(x(e), y(e));
    }

    public void mouseMoved(MouseEvent e) {
        ZLApplication.Instance().getCurrentView().onStylusMove(x(e), y(e));
    }

    public JPanel getPanel() {
        return myPanel;
    }

    private final Panel myPanel = new Panel();
}
