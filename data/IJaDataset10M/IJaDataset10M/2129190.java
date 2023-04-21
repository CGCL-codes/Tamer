package xGraphique.xCarte.xAlgorithmeTrace;

import java.util.Vector;
import xGraphique.xCarte.XCarte;
import xGraphique.xCarte.XTroncon;
import xGraphique.xCarte.XVille;

/**
 * Azimuth, guide routier (Interface graphique) Projet G.L., IFIPS CI 3
 * 
 * @author Olivier B�tant, St�phane Dautroy, Aur�lien Fontich, C�dric Mora
 * @date Automne 2007
 */
public class AlgorithmeSpring2 {

    public XCarte carte;

    public static final double DISTANCE_FORCE_REPULSIVE_MAX = 200;

    public static final double PARAMETRE_K = 20;

    public static final double PARAMETRE_L = 100;

    public static final double DEPLACEMENT_MAX = 10;

    public double coeff_deplacement = 0.1;

    public AlgorithmeSpring2(XCarte carte) {
        this.carte = carte;
    }

    public void iterer() {
        Vector<XVille> villes = new Vector<XVille>();
        villes.addAll(carte.getVilles());
        for (int i1 = 0; i1 < villes.size(); i1++) {
            XVille v1 = villes.elementAt(i1);
            for (int i2 = i1 + 1; i2 < villes.size(); i2++) {
                XVille v2 = villes.elementAt(i2);
                appliquerForceRepulsive(v1, v2);
            }
        }
        for (XTroncon t : carte.getTroncons()) {
            appliquerForceAttractive(t);
        }
        for (XVille v : carte.getVilles()) {
            double dx = v.forceX * coeff_deplacement;
            if (dx > DEPLACEMENT_MAX) dx = DEPLACEMENT_MAX; else if (dx < -DEPLACEMENT_MAX) dx = -DEPLACEMENT_MAX;
            double dy = v.forceY * coeff_deplacement;
            if (dy > DEPLACEMENT_MAX) dy = DEPLACEMENT_MAX; else if (dy < -DEPLACEMENT_MAX) dy = -DEPLACEMENT_MAX;
            v.x += dx;
            v.y += dy;
        }
        coeff_deplacement *= 0.87;
    }

    public void appliquerForceRepulsive(XVille v1, XVille v2) {
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        double d = dx * dx + dy * dy;
        if (d < 0.01) {
            dx = Math.random() * 0.1 + 0.1;
            dy = Math.random() * 0.1 + 0.1;
            d = dx * dx + dy * dy;
        }
        d = Math.sqrt(d);
        if (d < DISTANCE_FORCE_REPULSIVE_MAX) {
            double force = PARAMETRE_K * PARAMETRE_K / (d * d * d);
            v2.forceX += force * dx;
            v2.forceY += force * dy;
            v1.forceX -= force * dx;
            v1.forceY -= force * dy;
        }
    }

    public void appliquerForceAttractive(XTroncon t) {
        XVille v1 = t.getVille1();
        XVille v2 = t.getVille2();
        double dx = v2.x - v1.x;
        double dy = v2.y - v1.y;
        double d2 = dx * dx + dy * dy;
        if (d2 < 0.01) {
            dx = Math.random() * 0.1 + 0.1;
            dy = Math.random() * 0.1 + 0.1;
            d2 = dx * dx + dy * dy;
        }
        double d = Math.sqrt(d2);
        if (d > DISTANCE_FORCE_REPULSIVE_MAX) {
            d = DISTANCE_FORCE_REPULSIVE_MAX;
            d2 = d * d;
        }
        double force = PARAMETRE_K * (d - PARAMETRE_L) / d;
        v2.forceX -= force * dx;
        v2.forceY -= force * dy;
        v1.forceX += force * dx;
        v1.forceY += force * dy;
    }

    public double getPoids(XTroncon t) {
        return max(0, 1000 - t.longueurMoy());
    }

    protected int max(int a, int b) {
        return a > b ? a : b;
    }
}
