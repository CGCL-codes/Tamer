package santa.jpaint.kernel.tools;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import santa.jpaint.gui.MainFrame;
import santa.jpaint.kernel.Canvas;
import santa.jpaint.resource.Resources;

/**
 * Tool to select a region
 * @author Santa
 *
 */
public class SelectionTool extends Tool {

    /**
	 * Singleton
	 */
    public static final SelectionTool instance = new SelectionTool();

    /**
	 * untranslated point
	 */
    Point startPoint;

    /**
	 * Whether we are dragging the shape 
	 */
    boolean dragged = false;

    /**
	 * Whether this selection is valid
	 */
    boolean valid = false;

    /**
	 * Singleton
	 */
    protected SelectionTool() {
        super.statusInfo = "Drag the image to select, right click to deselect.";
        super.toolTip = "Selection";
        super.icon = Resources.selectToolIcon;
        super.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        dragged = true;
        selectToPoint(e.getPoint());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            valid = false;
            MainFrame.getCurrentEnv().getCanvas().setSelection(null);
        } else {
            valid = true;
        }
        dragged = false;
        startPoint = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        selectToPoint(e.getPoint());
    }

    private void selectToPoint(Point endPoint) {
        if (dragged == false || !valid) return;
        Canvas c = MainFrame.getCurrentEnv().getCanvas();
        BufferedImage image = MainFrame.getCurrentEnv().getImage();
        Point p1 = c.translateZoom(startPoint);
        Point p2 = c.translateZoom(endPoint);
        int x1 = Math.max(0, Math.min(image.getWidth() - 1, p1.x));
        int y1 = Math.max(0, Math.min(image.getHeight() - 1, p1.y));
        int x2 = Math.max(0, Math.min(image.getWidth() - 1, p2.x));
        int y2 = Math.max(0, Math.min(image.getHeight() - 1, p2.y));
        int x = Math.min(x1, x2);
        int width = Math.abs(x1 - x2);
        int y = Math.min(y1, y2);
        int height = Math.abs(y1 - y2);
        if (width > 0 && height > 0) c.setSelection(new Rectangle(x, y, width, height));
    }
}
