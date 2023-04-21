package lo.local.dreamrec.graphtools;

/**
 *
 */
public class LogFunction implements Function {

    public static final double x1 = 50, y1 = 40, x2 = 700, y2 = 150;

    private double k0 = new EquationResolver(new K0LogFunction(x1, y1, x2, y2), 0.001, 1000).getNullValue();

    private double k1 = new EquationResolver(new K1LogFunction(x1, y1, x2, y2), 0.001, 1000).getNullValue();

    public double getValue(double x) {
        return k1 * Math.log(k0 * x + 1);
    }
}
