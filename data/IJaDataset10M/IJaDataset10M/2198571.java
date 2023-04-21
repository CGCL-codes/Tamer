package ds.moteur.route.cc.elements;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import ds.io.Sauvegardable;
import ds.moteur.geometrie.Angle3D;
import ds.moteur.geometrie.Point;

/**Repr�sente un �l�ment de courbe de conduite.
 * 
 * @author Yannick BISIAUX
 *
 */
public abstract class LigneElementaire implements Sauvegardable {

    /**Origine*/
    protected Point p1;

    /**Fin*/
    protected Point p2;

    protected double longueur;

    public LigneElementaire(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    public double getLongueur() {
        return longueur;
    }

    /**Permet de r�cup�rer la position et l'angle d'une voiture situ�e � une abscisse curviligne sur cet �l�ment.
	 * 
	 * @param point la position de la voiture (maj)
	 * @param angle l'angle de la voiture (maj)
	 * @param abscisse l'abscisse curviligne
	 * @return true si l'abscisse correspond � une position sur cet �l�ment
	 */
    public boolean recupererPosition(Point point, Angle3D angle, double abscisse) {
        boolean dansIntervalle;
        double ratio = abscisse / longueur;
        if (abscisse < longueur) {
            dansIntervalle = true;
            recupererPoint(point, ratio);
            recupererAngle(angle, ratio);
        } else {
            dansIntervalle = false;
        }
        return dansIntervalle;
    }

    /**Permet de calculer la longueur de cet �l�ment.
	 * 
	 */
    public abstract void calculerLongueur();

    /**Projette un point sur cet �l�ment.
	 * 
	 * @param point le <code>Point</code> � projeter
	 * @return le ratio de cet �l�ment auquel la projection se trouve 
	 */
    public abstract double projeter(Point point);

    /**Projette un point sur cet �l�ment.
	 * 
	 * @param point le <code>Point</code> � projeter
	 * @return l'abscisse curviligne de la projection du point
	 */
    public abstract double projeterAbsCurv(Point point);

    /**Projette un point sur cet �l�ment.
	 * 
	 * @param point le <code>Point</code> � projeter
	 * @return la distance entre le point et sa projection
	 */
    public abstract double projeterDistance(Point point);

    /**Permet de r�cup�rer la position d'une voiture situ�e � une abscisse curviligne sur cet �l�ment.
	 * 
	 * @param point la position de la voiture (maj)
	 * @param abscisse l'abscisse curviligne
	 */
    protected abstract void recupererPoint(Point point, double ratio);

    /**Permet de r�cup�rer l'angle d'une voiture situ�e � une abscisse curviligne sur cet �l�ment.
	 * 
	 * @param angle l'angle de la voiture (maj)
	 * @param abscisse l'abscisse curviligne
	 */
    protected abstract void recupererAngle(Angle3D angle, double ratio);

    public void load(DataInputStream dis) throws IOException {
    }

    public void save(DataOutputStream dos) throws IOException {
    }
}
