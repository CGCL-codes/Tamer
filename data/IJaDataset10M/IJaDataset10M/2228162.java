package neuralNetwork;

public class StepFunction extends UnitFunction {

    public double low, high, threshold;

    public StepFunction() {
        threshold = 0;
        low = -1;
        high = 1;
    }

    public StepFunction(double threshold) {
        this.threshold = threshold;
        low = 0;
        high = 1;
    }

    public StepFunction(double low, double high, double threshold) {
        this.low = low;
        this.high = high;
        this.threshold = threshold;
    }

    double transfer(double input) {
        return input < threshold ? low : high;
    }
}
