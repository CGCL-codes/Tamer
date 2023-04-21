package object3D;

import context.*;
import geometry3D.Point3D;
import util.Util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.GeneralPath;
import javax.media.j3d.Appearance;
import javax.media.j3d.Group;

public class InngangsTrapp extends Block implements Context.Constants {

    public static final Color murFarge = Color.lightGray;

    public static final double oppTrinn = 0.18;

    public static final double innTrinn = 0.25;

    private Block trinn1;

    private Block trinn2;

    private Block trinn3;

    private Block trinn4;

    private Block trinn5;

    private boolean TEGN_HELE_TRAPPA = false;

    public void setTegnHeleTrappa() {
        TEGN_HELE_TRAPPA = true;
    }

    public InngangsTrapp(String ident, Point3D p0, double width, double height, double depth) {
        super(ident, p0, width, height, depth);
        trinn1 = new Block("Trapp", p0, 0.25, height - oppTrinn * 3, 2.0);
        trinn2 = new Block("Trapp", p0.addX(innTrinn), 0.25, height - oppTrinn * 2, 2.0);
        trinn3 = new Block("Trapp", p0.addX(innTrinn * 2).addZ(0), 0.25, height - oppTrinn, 2.0);
        trinn4 = new Block("Trapp", p0.addX(innTrinn * 3).addZ(0), 0.25, height, 2.0);
        trinn5 = new Block("Trapp", p0.addX(innTrinn * 4).addZ(0.25), 0.85, height + oppTrinn, 1.5);
    }

    public Group getModel3D(Appearance appearance) {
        Group tg = new Group();
        tg.addChild(trinn1.getModel3D(appearance));
        tg.addChild(trinn2.getModel3D(appearance));
        tg.addChild(trinn3.getModel3D(appearance));
        tg.addChild(trinn4.getModel3D(appearance));
        tg.addChild(trinn5.getModel3D(appearance));
        return (tg);
    }

    public void drawProjection(int direction, Graphics2D g, Point2D.Double origo2D, double scale, Color c) {
        Rectangle2D.Double r = projection(direction, origo2D, scale);
        switch(direction) {
            case NORTH:
                tegnTrappSide(direction, g, r, origo2D, scale, c);
                break;
            case SOUTH:
                tegnTrappSide(direction, g, r, origo2D, scale, c);
                break;
            case EAST:
                tegnTrapp(direction, g, r, origo2D, scale, c);
                break;
            case WEST:
                tegnTrapp(direction, g, r, origo2D, scale, c);
                break;
            case HCUT:
                tegnTrappPlan(g, r, origo2D, scale, c);
                break;
            default:
                super.drawProjection(direction, g, origo2D, scale, c);
        }
    }

    private void tegnTrapp(int direction, Graphics2D g, Rectangle2D.Double r, Point2D.Double origo2D, double scale, Color c) {
        if (TEGN_HELE_TRAPPA) {
            trinn5.drawProjection(direction, g, origo2D, scale, murFarge);
            trinn4.drawProjection(direction, g, origo2D, scale, murFarge);
        }
        trinn3.drawProjection(direction, g, origo2D, scale, murFarge);
        trinn2.drawProjection(direction, g, origo2D, scale, murFarge);
        trinn1.drawProjection(direction, g, origo2D, scale, murFarge);
    }

    private void tegnTrappSide(int direction, Graphics2D g, Rectangle2D.Double r, Point2D.Double origo2D, double scale, Color c) {
        double x0 = r.x;
        double dx = innTrinn * scale;
        if (direction == NORTH) {
            x0 = r.x + r.width;
            dx = -dx;
        }
        double y0 = r.y + r.height;
        double dy = oppTrinn * scale;
        double yB = r.y + 3 * dy;
        GeneralPath path = new GeneralPath();
        path.moveTo((float) x0, (float) y0);
        path.lineTo((float) x0, (float) yB);
        path.lineTo((float) (x0 + dx), (float) yB);
        path.lineTo((float) (x0 + dx), (float) (yB - dy));
        path.lineTo((float) (x0 + 2 * dx), (float) (yB - dy));
        path.lineTo((float) (x0 + 2 * dx), (float) (yB - 2 * dy));
        path.lineTo((float) (x0 + 3 * dx), (float) (yB - 2 * dy));
        path.lineTo((float) (x0 + 3 * dx), (float) (yB - 3 * dy));
        path.lineTo((float) (x0 + 3 * dx), (float) (y0));
        path.closePath();
        Util.fill(g, path, murFarge);
        g.draw(path);
    }

    private void tegnTrappPlan(Graphics2D g, Rectangle2D.Double r, Point2D.Double origo2D, double scale, Color c) {
        tegnTrapp(HCUT, g, r, origo2D, scale, c);
        float x0 = (float) r.x;
        float y0 = (float) (r.y + r.height / 2);
        float xT = (float) (r.x + (innTrinn * 3) * scale);
        float dx = (float) (0.20 * scale);
        float dy = (float) (0.15 * scale);
        GeneralPath path = new GeneralPath();
        path.moveTo(x0, y0);
        path.lineTo(xT, y0);
        path.lineTo(xT - dx, y0 + dy);
        path.moveTo(xT - dx, y0 - dy);
        path.lineTo(xT, y0);
        g.draw(path);
    }
}
