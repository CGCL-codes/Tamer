package programme;

/**
 * @author port
 *
 */
public class PortefeuilleStochastique extends ProgStochastique {

    /** Risque de perte maximum que l'on est prêt à subir*/
    private double alpha;

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
}
