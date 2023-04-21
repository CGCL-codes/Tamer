package byggeTegning.husGeometri;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import byggeTegning.geometry.Point3D;

public class EndeVegg extends LangVegg {

    public double tanTakVinkel;

    public EndeVegg(String ident, Point3D p0, double width, double height, double depth, double tanTakVinkel, TimberDimension timber) {
        super(ident, p0, width, height, depth, timber);
        this.tanTakVinkel = tanTakVinkel;
        this.helBunnStokk = true;
        if (timber != null) wall = TimberWall.northSouth(p0, timber, height, depth); else wall = PanelWall.northSouth(p0, height, width, depth);
    }

    public void setVeggType(int veggType, double tanTakVinkel) {
        wall.setVeggType(veggType, tanTakVinkel);
    }

    public void drawProjection(int direction, Graphics2D g, Point2D.Double origo2D, double scale, Color c) {
        Rectangle2D.Double r = projection(direction, origo2D, scale);
        switch(direction) {
            case NORTH:
                drawProjectionNorth(g, r, scale, c);
                break;
            case SOUTH:
                drawProjectionSouth(g, r, scale, c);
                break;
            case EAST:
                drawProjectionEast(g, r, scale, c);
                break;
            case WEST:
                drawProjectionWest(g, r, scale, c);
                break;
            default:
                super.drawProjection(direction, g, origo2D, scale, c);
        }
    }

    private void drawProjectionEast(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        if (timber != null) drawTimberLongSideThis(g, r, scale, c); else drawPanelThis(g, r, scale, c);
    }

    private void drawProjectionWest(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        if (timber != null) drawTimberLongSideThis(g, r, scale, c); else drawPanelThis(g, r, scale, c);
    }

    private void drawProjectionNorth(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        if (timber != null) drawTimberShortSide(g, r, scale, c);
    }

    private void drawProjectionSouth(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        if (timber != null) drawTimberShortSide(g, r, scale, c);
    }

    private void drawTimberLongSideThis(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        double knuteLength = timber.utspring * scale;
        r.x = r.x - knuteLength;
        r.width = knuteLength + r.width + knuteLength;
        GeneralPath path = new GeneralPath();
        float xt = (float) (r.x + r.width / 2);
        float yt = (float) (r.y - (r.width / 2) * tanTakVinkel);
        path.moveTo((float) r.x, (float) r.y);
        path.lineTo(xt, yt);
        path.lineTo((float) (r.x + r.width), (float) r.y);
        path.lineTo((float) (r.x + r.width), (float) (r.y + r.height));
        path.lineTo((float) r.x, (float) (r.y + r.height));
        path.lineTo((float) r.x, (float) r.y);
        fill(g, path, c);
        draw(g, path);
        Shape oldClip = g.getClip();
        g.setClip(path);
        r.y = yt;
        r.height = r.height + (r.width / 2) * tanTakVinkel;
        drawTimberLongSide(g, r, scale, c);
        g.setClip(oldClip);
    }

    private void drawPanelThis(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        GeneralPath path = new GeneralPath();
        float xt = (float) (r.x + r.width / 2);
        float yt = (float) (r.y - (r.width / 2) * tanTakVinkel);
        path.moveTo((float) r.x, (float) r.y);
        path.lineTo(xt, yt);
        path.lineTo((float) (r.x + r.width), (float) r.y);
        path.lineTo((float) (r.x + r.width), (float) (r.y + r.height));
        path.lineTo((float) r.x, (float) (r.y + r.height));
        path.lineTo((float) r.x, (float) r.y);
        fill(g, path, c);
        draw(g, path);
        r.height = r.height + (r.y - yt);
        r.y = yt;
        drawPanel(g, r, path, scale, c);
    }
}
