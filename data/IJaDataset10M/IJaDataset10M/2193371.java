package kursach2.tactics;

import java.util.ArrayList;
import java.util.Map.Entry;
import kursach2.HumanCollision;
import kursach2.barriers.AArchiBot;
import kursach2.barriers.ABarier;

/**
 * @author Vsevolod
 *
 */
public class GradientTactics implements ITactics {

    protected String Name = "GradientTactics";

    protected RoundTargetTactics RTT;

    @Override
    public String getName() {
        return Name;
    }

    public GradientTactics(ITactics R) {
        if (R.getName().compareTo("RoundTargetTactics") != 0) {
            System.out.println("GradientTactics was not taken RoundTargetTactics");
            return;
        }
        RTT = (RoundTargetTactics) R;
    }

    @Override
    public double[] step(AArchiBot Hum, HumanCollision Coll, ArrayList<Entry<ABarier, Double>> Near) {
        double[] r = RTT.step(Hum, Coll, Near);
        double L = Math.sqrt(r[0] * r[0] + r[1] * r[1]);
        double x, y;
        x = (Hum.getX() - Hum.getMyPrem().x[0]) / Hum.getMyPrem().getL();
        y = (Hum.getY() - Hum.getMyPrem().x[1]) / Hum.getMyPrem().getB();
        double grad = f(x, y);
        if (L > grad) {
            Hum.prolong(r, grad - L);
        }
        return r;
    }

    private double f(double x, double y) {
        return sqr(x + sqr(y) - y - 0.5);
    }

    private double[] grad_f(double x, double y) {
        return new double[] { 2 * (x + sqr(y) - y - 0.5), 2 * (x + sqr(y) + y - 1.5) };
    }

    private double multscal(double[] x, double[] y) {
        return x[0] * y[0] + x[1] * y[1];
    }

    private double sqr(double x) {
        return x * x;
    }
}
