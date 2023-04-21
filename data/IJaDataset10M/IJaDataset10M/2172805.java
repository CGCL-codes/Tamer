package visugraph.gview.animate;

/**
 * Interpolateur qui permet de faire rebondir les objets en fin de mouvement.
 */
public class BounceInterpolator implements Interpolator {

    public static final BounceInterpolator INSTANCE = new BounceInterpolator();

    public double interpolate(double timePos) {
        double t = timePos * 1.1226;
        if (t < 0.3535) {
            return bounce(t);
        } else if (t < 0.7408) {
            return bounce(t - 0.54719) + 0.7;
        } else if (t < 0.9644) {
            return bounce(t - 0.8526) + 0.9;
        } else {
            return bounce(t - 1.0435) + 0.95;
        }
    }

    private double bounce(double t) {
        return t * t * 8.0;
    }
}
