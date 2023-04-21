package canvas.view.materials;

import canvas.view.locator.XWidgetLocator;
import canvas.view.widget.XObjectScene;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.WidgetAction.State;
import org.netbeans.api.visual.action.WidgetAction.WidgetMouseEvent;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Isaac
 */
public class LocateWidgetAction extends WidgetAction.Adapter {

    private Point pressedPoint;

    private Point releasedPoint;

    @Override
    public State mousePressed(Widget widget, WidgetMouseEvent evt) {
        XObjectScene objectScene = (XObjectScene) widget.getScene();
        if (objectScene.getXWidgetLocator() != null) {
            pressedPoint = evt.getPoint();
            return State.CONSUMED;
        } else {
            return super.mousePressed(widget, evt);
        }
    }

    @Override
    public State mouseReleased(Widget widget, WidgetMouseEvent evt) {
        XObjectScene objectScene = (XObjectScene) widget.getScene();
        XWidgetLocator xWidgetLocator = objectScene.getXWidgetLocator();
        if (xWidgetLocator != null) {
            releasedPoint = evt.getPoint();
            int x = pressedPoint.x < releasedPoint.x ? pressedPoint.x : releasedPoint.x;
            int y = pressedPoint.y < releasedPoint.y ? pressedPoint.y : releasedPoint.y;
            int width = Math.abs(pressedPoint.x - releasedPoint.x);
            int height = Math.abs(pressedPoint.y - releasedPoint.y);
            Point location = new Point(x, y);
            Rectangle bounds = new Rectangle(x, y, width, height);
            if (xWidgetLocator != null) {
                if (bounds.isEmpty()) {
                    xWidgetLocator.locate(objectScene, location, null);
                } else {
                    xWidgetLocator.locate(objectScene, location, bounds);
                }
            }
            return State.CONSUMED;
        } else {
            return super.mouseReleased(widget, evt);
        }
    }
}
