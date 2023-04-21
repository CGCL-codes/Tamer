package bs;

import java.awt.geom.*;
import java.awt.*;
import java.util.*;
import static java.lang.Math.*;

/**
 *
 * @author  dw3
 */
public class ShotPainter extends Painter {

    public transient java.util.List<Vector> traj;

    public transient Vector oldOutOfScreen;

    public ShotPainter() {
        traj = new LinkedList<Vector>();
    }

    public void tick(double deltaTime) {
        traj.add(location);
    }

    public void simulationStarted() {
        destroy();
        World.instance.viewPort.redraw();
    }

    public void paint(java.awt.Graphics2D g, boolean bUpdateOnly) {
        Shot s = (Shot) attachedTo;
        if (!World.instance.game.isInState(BasicGame.Simulating.class) && World.instance.viewPort.currentControllable != instigator || isDestroyed()) return;
        Rectangle clip = g.getClipBounds();
        double scaleX = clip.width / WORLDSIZE_X;
        double scaleY = clip.height / WORLDSIZE_Y;
        int oX = -1, oY = -1;
        g.setColor(s.color);
        if (bUpdateOnly) {
            int x = scaleX(location.x);
            int y = scaleY(location.y);
            int rx = scaleX(s.SHOTRADIUS);
            int ry = scaleY(s.SHOTRADIUS);
            if (!s.isDestroyed() && bUpdateOnly) {
                if (x < 0 || y < 0 || x > clip.width || y > clip.height) {
                    if (oldOutOfScreen != null) {
                        g.setColor(Color.BLACK);
                        int nrx = Math.max(rx, rx * 2 * Math.abs(scaleX(oldOutOfScreen.x) - clip.width / 2) / clip.width);
                        int nry = Math.max(rx, ry * 2 * Math.abs(scaleY(oldOutOfScreen.y) - clip.height / 2) / clip.height);
                        int nx = Math.min(clip.width - nrx, Math.max(nrx, scaleX(oldOutOfScreen.x)));
                        int ny = Math.min(clip.height - nry, Math.max(nry, scaleY(oldOutOfScreen.y)));
                        g.fillArc(nx - nrx, ny - nry, 2 * nrx, 2 * nry, 0, 360);
                    } else oldOutOfScreen = new Vector(0, 0);
                    g.setColor(Color.LIGHT_GRAY);
                    int nrx = Math.max(rx, rx * 2 * Math.abs(x - clip.width / 2) / clip.width);
                    int nry = Math.max(rx, ry * 2 * Math.abs(y - clip.height / 2) / clip.height);
                    x = Math.min(clip.width - nrx, Math.max(nrx, x));
                    y = Math.min(clip.height - nry, Math.max(nry, y));
                    g.fillArc(x - nrx, y - nry, 2 * nrx, 2 * nry, 0, 360);
                    oldOutOfScreen.x = location.x;
                    oldOutOfScreen.y = location.y;
                } else if (x >= 0 && y >= 0 && x < clip.width && y < clip.height) g.fillArc(x - rx, y - ry, 2 * rx, 2 * ry, 0, 360);
            }
        } else {
            for (Vector p : traj) {
                int x = scaleX(p.x);
                int y = scaleY(p.y);
                int rx = scaleX(s.SHOTRADIUS);
                int ry = scaleY(s.SHOTRADIUS);
                if (x >= 0 && y >= 0 && x <= clip.width && y <= clip.height) {
                    g.fillArc(x - rx, y - ry, 2 * rx, 2 * ry, 0, 360);
                    oX = x;
                    oY = y;
                }
            }
        }
    }
}
