package byggeTegning.husGeometri;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.media.j3d.Group;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import byggeTegning.geometry.Point3D;
import byggeTegning.geometry.PolyShape;
import byggeTegning.geometry.TransformedGroup3D;

public class SalTakEndeBord extends BjelkeLag {

    double tanTakvinkel;

    double takskiHoyde;

    double takskiUtspring;

    double takskiSkjevhet;

    public SalTakEndeBord(String ident, Point3D p0, double tanTakvinkel, double width, double height, double depth, double takskiHoyde, double takskiUtspring, double takskiSkjevhet) {
        super(ident + ":SalTakEndeBord", p0, width, height, depth);
        this.tanTakvinkel = tanTakvinkel;
        this.takskiHoyde = takskiHoyde;
        this.takskiUtspring = takskiUtspring;
        this.takskiSkjevhet = takskiSkjevhet;
        list("new");
    }

    private void list(String id) {
    }

    public Group getModel3D(Color3f c) {
        TransformedGroup3D tg = new TransformedGroup3D();
        Point3D pTop = p0.addY(height).addZ(depth / 2);
        double takVinkel = Math.atan(tanTakvinkel);
        double length = (depth / 2 + (takskiUtspring * 1.05)) * Math.sqrt(1 + tanTakvinkel * tanTakvinkel);
        tg.addChild(getBord3D(c, pTop, width, takskiHoyde, length, takVinkel));
        tg.addChild(getBord3D(c, pTop, width, takskiHoyde, -length, -takVinkel));
        return (tg);
    }

    private Group getBord3DXXX(Color3f c, Point3D pTop, double width, double height, double depth) {
        Block blk = new Block("", pTop, width, height, depth);
        return (blk.getModel3D(c));
    }

    private PolyShape getBord3D(Color3f c, Point3D pTop, double width, double height, double depth, double takVinkel) {
        float bredde = 1, hoyde = 1;
        float px = 0f, py = 0f, pz = 0f - 0.05f;
        float qx = px, qy = py, qz = 1;
        if (depth < 0) {
            depth = -depth;
            pz = -pz;
            qz = -qz;
        }
        Point3f[] front = new Point3f[] { new Point3f(qx, qy, qz), new Point3f(qx, qy + hoyde, qz), new Point3f(qx - bredde, qy + hoyde, qz), new Point3f(qx - bredde, qy, qz), new Point3f(qx, qy, qz) };
        Point3f[] back = new Point3f[] { new Point3f(px, py, pz), new Point3f(px - bredde, py, pz), new Point3f(px - bredde, py + hoyde, pz), new Point3f(px, py + hoyde, pz), new Point3f(px, py, pz) };
        PolyShape tg = new PolyShape(c, front, back);
        tg.setSize(width, height, Math.abs(depth));
        tg.setP0(pTop.x, pTop.y, pTop.z);
        tg.setAngleX(takVinkel);
        return (tg);
    }

    public void drawProjection(int direction, Graphics2D g, Point2D.Double origo2D, double scale, Color c) {
        Rectangle2D.Double r = projection(direction, origo2D, scale);
        switch(direction) {
            case NORTH:
                drawProjectionShortSide(g, r, scale, c);
                break;
            case SOUTH:
                drawProjectionShortSide(g, r, scale, c);
                break;
            case EAST:
                drawProjectionLongSide(g, r, scale, c);
                break;
            case WEST:
                drawProjectionLongSide(g, r, scale, c);
                break;
            case VCUT:
                drawProjectionLongSide(g, r, scale, c);
                break;
            default:
                super.drawProjection(direction, g, origo2D, scale, c);
        }
    }

    private void drawProjectionLongSide(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        double h = takskiHoyde * scale;
        double dxLower = takskiUtspring * scale;
        double dyLower = dxLower * tanTakvinkel;
        double dxUpper = (takskiUtspring + takskiSkjevhet) * scale;
        double dyUppper = dxUpper * tanTakvinkel;
        GeneralPath path = new GeneralPath();
        float xTop = (float) (r.x + r.width / 2);
        float yTopLower = (float) (r.y);
        float yTopUpper = (float) (r.y - h);
        float xLeftUpper = (float) (r.x - dxUpper);
        float xRightUpper = (float) (r.x + dxUpper + r.width);
        float yYtterstUpper = (float) (r.y + dyUppper + r.height - h);
        float xLeftLower = (float) (r.x - dxLower);
        float xRightLower = (float) (r.x + dxLower + r.width);
        float yYtterstLower = (float) (r.y + dyLower + r.height);
        path.moveTo(xTop, yTopUpper);
        path.lineTo(xLeftUpper, yYtterstUpper);
        path.lineTo(xLeftLower, yYtterstLower);
        path.lineTo(xTop, yTopLower);
        path.lineTo(xRightLower, yYtterstLower);
        path.lineTo(xRightUpper, yYtterstUpper);
        path.lineTo(xTop, yTopUpper);
        fill(g, path, c);
        draw(g, path);
    }

    private void drawProjectionShortSide(Graphics2D g, Rectangle2D.Double r, double scale, Color c) {
        double h = takskiHoyde * scale;
        double dxLower = takskiUtspring * scale;
        double dyLower = dxLower * tanTakvinkel;
        double dxUpper = (takskiUtspring + takskiSkjevhet) * scale;
        double dyUppper = dxUpper * tanTakvinkel;
        double yTopUpper = r.y - h;
        double yYtterstLower = r.y + dyLower + r.height;
        double x1 = r.x;
        double w1 = r.width;
        double h1 = yYtterstLower - yTopUpper;
        Rectangle2D.Double ski = new Rectangle2D.Double(x1, yTopUpper, w1, h1);
        fill(g, ski, c);
        draw(g, ski);
    }
}
