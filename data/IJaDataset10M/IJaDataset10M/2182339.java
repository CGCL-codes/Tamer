package self.math;

public class QuadraticEquation {

    public double a, b, c;

    public QuadraticEquation() {
    }

    public QuadraticEquation(double a, double b, double c) {
        setCoefficients(a, b, c);
    }

    public void setCoefficients(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public void calcXValues(double[] x1Andx2) {
        double rootSqrBMinus4ac = getRootOfbSqrdMinus4ac();
        double denominator = 2 * a;
        double numerator = -b + rootSqrBMinus4ac;
        x1Andx2[0] = numerator / denominator;
        numerator = -b - rootSqrBMinus4ac;
        x1Andx2[1] = numerator / denominator;
    }

    public double solveForX(double xVal) {
        return (a * (xVal * xVal)) + (b * xVal) + c;
    }

    private double getRootOfbSqrdMinus4ac() {
        double result = 4 * a * c;
        result = b * b - result;
        result = (double) Math.sqrt(result);
        return result;
    }

    public String toString() {
        return "[a=" + a + ", b=" + b + ", c=" + c + "]";
    }
}
