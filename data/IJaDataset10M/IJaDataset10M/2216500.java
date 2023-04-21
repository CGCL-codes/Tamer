package game.gui.structure3d.j3d;

import javax.media.j3d.Group;
import javax.vecmath.Point3d;

public class GOB3DArrow extends GOB3D {

    private double x1;

    private double y1;

    private double z1;

    private double x2;

    private double y2;

    private double z2;

    private double r1;

    private double r2;

    private double length;

    public GOB3DArrow(JKCanvasJ3D canvas3D, double ox1, double oy1, double oz1, double ox2, double oy2, double oz2, double or1, double or2, double olength) {
        super(canvas3D);
        x1 = ox1;
        y1 = oy1;
        z1 = oz1;
        x2 = ox2;
        y2 = oy2;
        z2 = oz2;
        r1 = or1;
        r2 = or2;
        length = olength;
    }

    public void draw(Group obg) {
        Point3d p1 = new Point3d(x1, y1, z1), p2 = new Point3d(x2, y2, z2);
        Point3d pm = new Point3d();
        double len = p1.distance(p2);
        pm.interpolate(p2, p1, length / len);
        tg.addChild(DrawPrimitives.Cylinder(true, ap, p1.x, p1.y, p1.z, pm.x, pm.y, pm.z, r1));
        tg.addChild(DrawPrimitives.Cone(true, ap, pm.x, pm.y, pm.z, p2.x, p2.y, p2.z, r2));
        obg.addChild(tg);
    }
}
