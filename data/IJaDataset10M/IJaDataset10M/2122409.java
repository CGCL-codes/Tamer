package labyrinthe.outils;

/**
 * Definie des methodes statiques permettant de d�tecter des 
 * collisions entre diff�rents �lements
 * 
 * @author picarle.sylvain
 */
public class Collision {

    /** 
	 * Permet de detecter une collision entre les bords de deux cercles
	 * peut �tre utiliser pour que deux billes se repoussent par exemple
	 * @param c1 premier cercle
	 * @param c2 deuxieme cercle
	 * @return coordonnee du point d'impact, null si pas de collision
	 */
    public static Coordonnee collisionCercleCercle(Cercle c1, Cercle c2) {
        double distance;
        double d1 = c1.getDiametre();
        double d2 = c2.getDiametre();
        distance = distanceEntreDeuxPoints(c1.getCentre(), c2.getCentre());
        if (distance <= (d1 / 2f + d2 / 2f)) {
            double rapport = d1 / d2;
            double abs = (c1.getCentre().getX() + c2.getCentre().getX()) / rapport;
            double ord = (c1.getCentre().getY() + c2.getCentre().getY()) / rapport;
            return new Coordonnee(abs, ord, 1);
        }
        return null;
    }

    /**
	 * D�tection de collision entre une coordonnee et un cercle
	 * @param coor la coordonnee
	 * @param c le cercle
	 * @return coordonnee du point d'impact
	 */
    public static Coordonnee collisionCoordonneeCercle(Coordonnee coor, Cercle c) {
        if ((Math.pow(coor.getX() - c.getCentre().getX(), 2) + Math.pow(c.getCentre().getY() - coor.getY(), 2)) < Math.pow(c.getDiametre() / 2, 2)) {
            return coor;
        }
        return null;
    }

    /**
	 * V�rification de collision entre un cercle et une ligne 
	 * @param c1 le cercle
	 * @param l1 la ligne
	 * @return coordonn�e d'impact sur la ligne
	 */
    public static Coordonnee collisionCercleLigne(Cercle c1, Ligne l1) {
        double distance = distanceEntreDeuxPoints(l1.getPointA(), l1.getPointB());
        double lx1 = l1.getPointA().getX();
        double ly1 = l1.getPointA().getY();
        double lx2 = l1.getPointB().getX();
        double ly2 = l1.getPointB().getY();
        double cx1 = c1.getCentre().getX();
        double cy1 = c1.getCentre().getY();
        double r1 = c1.getDiametre() / 2.0;
        double x;
        double y;
        if (lx2 == lx1) {
            x = lx1;
            y = cy1;
        } else if (ly1 == ly2) {
            x = cx1;
            y = ly1;
        } else {
            double m = 0;
            double p1 = 0;
            double p = 0;
            double m1 = 0;
            m = (ly2 - ly1) / (lx2 - lx1);
            p = ly1 - m * lx1;
            m1 = -1 / m;
            p1 = -m1 * cx1 + cy1;
            x = (p - p1) / (m1 - m);
            y = m * x + p;
        }
        Coordonnee intersection = new Coordonnee(x, y, 1);
        if (distanceEntreDeuxPoints(intersection, l1.getPointA()) + distanceEntreDeuxPoints(intersection, l1.getPointB()) - distance > l1.getEpaisseur() / 2) {
            return null;
        }
        r1 += l1.getEpaisseur() / 2;
        if (distanceEntreDeuxPoints(intersection, c1.getCentre()) < r1) {
            return intersection;
        }
        return null;
    }

    /**
	 * Permet de calculer la distance entre deux points
	 * @param a Point de d�part du calcul de distance
	 * @param b Point d'arriv� du calcul de distance
	 */
    public static double distanceEntreDeuxPoints(Coordonnee a, Coordonnee b) {
        double distance = 0;
        double cx1 = a.getX();
        double cy1 = a.getY();
        double cx2 = b.getX();
        double cy2 = b.getY();
        if (cx1 == cx2) {
            distance = Math.abs(cy1 - cy2);
        } else if (cy1 == cy2) {
            distance = Math.abs(cx1 - cx2);
        } else {
            distance = Math.sqrt(Math.pow((cx2 - cx1), 2) + Math.pow((cy2 - cy1), 2));
        }
        return distance;
    }
}
