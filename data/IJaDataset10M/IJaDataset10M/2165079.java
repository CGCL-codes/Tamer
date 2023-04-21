package jts.moteur.geometrie;

/**Cette classe repr�sente un vecteur.
 * 
 * @author Yannick BISIAUX
 *
 */
public class Vecteur {

    public double x;

    public double y;

    public double z;

    public Vecteur() {
        this(0, 0);
    }

    public Vecteur(double x, double y) {
        this(x, y, 0);
    }

    public Vecteur(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vecteur(Point p1, Point p2) {
        this.x = p2.getX() - p1.getX();
        this.y = p2.getY() - p1.getY();
        this.z = p2.getZ() - p1.getZ();
    }

    public double getNorme() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public void normer() {
        double norme = getNorme();
        if (norme != 0) {
            x = x / norme;
            y = y / norme;
            z = z / norme;
        }
    }

    public double getProduitScalaire(Vecteur vecteur) {
        double ps = x * vecteur.x + y * vecteur.y + z * vecteur.z;
        return ps;
    }

    public Vecteur getProduitVectoriel(Vecteur vecteur) {
        double x3 = y * vecteur.z - vecteur.y * z;
        double y3 = z * vecteur.x - vecteur.z * x;
        double z3 = x * vecteur.y - vecteur.x * y;
        return new Vecteur(x3, y3, z3);
    }

    /**
	 * 
	 * @return un angle en radian
	 */
    public double getAngle() {
        double angle;
        if (x == 0) {
            if (y < 0) {
                angle = -Math.PI / 2;
            } else {
                angle = Math.PI / 2;
            }
        } else {
            if (x > 0) {
                angle = Math.atan(y / x);
            } else {
                if (y > 0) {
                    angle = Math.atan(y / x) + Math.PI;
                } else {
                    angle = Math.atan(y / x) - Math.PI;
                }
            }
        }
        return angle;
    }
}
