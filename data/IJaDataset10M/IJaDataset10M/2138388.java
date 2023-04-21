package mapEditor.interactor;

import mapEditor.map.MapCanvas;
import mapEditor.obj.TextObject;
import mapEditor.utils.MapLocation;
import java.awt.Graphics2D;

public class DrawTextInteractor extends MapInteractorAdapter {

    public String getMouseModeMnemonic() {
        return ("Draw Text");
    }

    private TextObject text;

    private MapLocation p0;

    private MapLocation p1;

    public DrawTextInteractor(MapCanvas mapCanvas) {
        super(mapCanvas, DRAW_TEXT);
    }

    public void paintMouseEffect(Graphics2D g) {
        if (text != null) text.paint((Graphics2D) g);
    }

    protected void mouseDragged() {
        if (text != null) {
            p1.setX(mouseCurrent.getX());
            p1.setY(mouseCurrent.getY());
            mapCanvas.repaint();
        }
    }

    protected void mouseMoved() {
    }

    protected void mouseLeftPressed(MapLocation p) {
        if (text == null) {
            p0 = p;
            text = new TextObject(mapCanvas.getDOMTree(), p0);
            mapCanvas.repaint();
        }
    }

    protected void mouseRightPressed(MapLocation p) {
    }

    protected void mouseLeftReleased(MapLocation p) {
        mapCanvas.drawObjectDone(text, "TEXT");
        text = null;
        mapCanvas.repaint();
    }

    protected void mouseRightReleased(MapLocation p) {
    }
}
