package gp;

/**
 *
 */
class RandomConstant extends Terminal {

    static final double MIN = -4.0;

    static double MAX = +4.0;

    double value;

    RandomConstant() {
        value = GP.random.nextDouble() * (MAX - MIN) + MIN;
    }

    private RandomConstant(double value) {
        this.value = value;
    }

    public String toString(int level) {
        return indent(level) + value;
    }

    protected Object clone() {
        return new RandomConstant(this.value);
    }

    ;

    String getName() {
        return "Random Constant";
    }

    double eval(double x) {
        return value;
    }
}
