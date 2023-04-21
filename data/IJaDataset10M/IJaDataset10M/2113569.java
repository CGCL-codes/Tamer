package twjcalc.gui.whistle;

/**
 *
 */
public class Justify {

    public static final double angle(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle > 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
