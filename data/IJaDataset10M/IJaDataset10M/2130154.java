package jts.moteur.train;

/**D�crit les aspects m�caniques d'un wagon (acc�l�ration, ...)
 * 
 * @author Yannick BISIAUX
 *
 */
public class Mecanique {

    protected double vitesse;

    protected double masse;

    protected Frottement frottement;

    protected double accelerationMax;

    protected double freinageMax;

    public void accelerer(long ms) {
        this.vitesse += ms * accelerationMax / 1000;
    }

    public void freiner(long ms) {
        this.vitesse -= ms * freinageMax / 1000;
        if (this.vitesse < 0) {
            this.vitesse = 0;
        }
    }

    public double getVitesse() {
        return this.vitesse;
    }

    public void appliquerForceMotrice(double force, long ms) {
        this.vitesse += (force / masse) * (ms / 1000.0);
    }

    public void appliquerForceFreinage(long ms) {
        double force = frottement.getFrottement(vitesse);
        double dv = (force / masse) * (ms / 1000.0);
        if (Math.abs(vitesse) > Math.abs(dv)) {
            this.vitesse -= dv;
        } else {
            this.vitesse = 0;
        }
    }
}
