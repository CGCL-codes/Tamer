package gem.util;

/**
 * @author Ozgun Babur
 */
public class Aggregate extends Function {

    protected Function[] f;

    protected Operator op;

    public Aggregate(Operator op, Function... f) {
        this.f = f;
        this.op = op;
    }

    @Override
    public double calc(double x) {
        double[] d = new double[f.length];
        int i = 0;
        for (Function fu : f) {
            d[i++] = fu.calc(x);
        }
        return op.operate(d);
    }
}
