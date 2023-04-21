package galaxiia.ui.visionneurs.gl;

import static java.lang.Math.*;
import static javax.media.opengl.GL.*;
import javax.media.opengl.GL;

class Affichage {

    private static double EPSILON = 1E-10;

    public static void dessineContour(GL gl, double[] position, double[] vitesse, double rayon) {
        double x = position[0];
        double y = position[1];
        double vx = vitesse[0];
        double vy = vitesse[1];
        double norme = sqrt(vx * vx + vy * vy);
        if (norme < EPSILON) {
            vx = rayon;
            vy = 0;
        } else {
            vx *= rayon / norme;
            vy *= rayon / norme;
        }
        gl.glBegin(GL_LINE_LOOP);
        gl.glTexCoord2d(0, 0);
        gl.glVertex2d(x - vx - vy, y + vx - vy);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(x + vx - vy, y + vx + vy);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(x + vx + vy, y - vx + vy);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(x - vx + vy, y - vx - vy);
        gl.glEnd();
    }

    public static void dessineSupport(GL gl, double[] position, double[] vitesse, double rayon) {
        dessineSupport(gl, position[0], position[1], vitesse[0], vitesse[1], rayon);
    }

    public static void dessineSupport(GL gl, double x, double y, double vx, double vy, double rayon) {
        double norme = sqrt(vx * vx + vy * vy);
        if (norme < EPSILON) {
            vx = rayon;
            vy = 0;
        } else {
            vx *= rayon / norme;
            vy *= rayon / norme;
        }
        gl.glBegin(GL_TRIANGLE_STRIP);
        gl.glTexCoord2d(0, 0);
        gl.glVertex2d(x - vx - vy, y + vx - vy);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(x + vx - vy, y + vx + vy);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(x - vx + vy, y - vx - vy);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(x + vx + vy, y - vx + vy);
        gl.glEnd();
    }

    public static void dessineRectangle(GL gl, double x, double y, double largeur, double hauteur) {
        gl.glBegin(GL_TRIANGLE_STRIP);
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(x, y, 0);
        gl.glTexCoord2d(0, 1);
        gl.glVertex3d(x, y + hauteur, 0);
        gl.glTexCoord2d(1, 0);
        gl.glVertex3d(x + largeur, y, 0);
        gl.glTexCoord2d(1, 1);
        gl.glVertex3d(x + largeur, y + hauteur, 0);
        gl.glEnd();
    }

    public static void dessineRectangle(GL gl, double[] a, double[] b, double hauteur) {
        double x1 = a[0];
        double y1 = a[1];
        double x2 = b[0];
        double y2 = b[1];
        double coefH = 0.5 * hauteur / sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        double hx = (y1 - y2) * coefH;
        double hy = (x2 - x1) * coefH;
        gl.glBegin(GL_TRIANGLE_STRIP);
        gl.glTexCoord2d(0, 0);
        gl.glVertex2d(x1 - hx, y1 - hy);
        gl.glTexCoord2d(1, 0);
        gl.glVertex2d(x1 + hx, y1 + hy);
        gl.glTexCoord2d(0, 1);
        gl.glVertex2d(x2 - hx, y2 - hy);
        gl.glTexCoord2d(1, 1);
        gl.glVertex2d(x2 + hx, y2 + hy);
        gl.glEnd();
    }
}
