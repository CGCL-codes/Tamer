package gameEngine.world.shot;

import utilities.MathUtil;
import gameEngine.world.owner.Owner;
import gameEngine.world.unit.Unit;

/**
 * a shot that travels in the conventional manner, once shot it
 * continues along its path until it collides with something or
 * reaches its target position
 * @author Jack
 *
 */
public class StaticVelocityShot extends Shot {

    private double[] sf = new double[2];

    public StaticVelocityShot(double x, double y, double width, double height, Unit target, double movement, double damage, Owner o) {
        super(x, y, width, height, target, movement, damage, o);
        double op = .4;
        double[] s = target.getLocation();
        double[] d = { s[0] - x, s[1] - y };
        sf[0] = d[0] + op * d[0];
        sf[1] = d[1] + op * d[1];
        sf = s;
    }

    /**
	 * moves the shot towards its final position, sets the shot to
	 * dead if it reaches the final position
	 */
    public void updateShot(double tdiff) {
        double m = movement * tdiff;
        if (MathUtil.distance(x, y, sf[0], sf[1]) <= m) {
            dead = true;
        }
        double[] ab = { x - sf[0], y - sf[1] };
        double distance = m * m;
        double lambda = (ab[0] * ab[0]) + (ab[1] * ab[1]);
        lambda = -Math.sqrt(distance / lambda);
        double[] l = { x + ab[0] * lambda, y + ab[1] * lambda };
        setLocation(l[0], l[1]);
    }
}
