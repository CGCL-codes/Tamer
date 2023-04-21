package draughts;

public class NullITD implements ITD {

    private double[] weights;

    private double a;

    private double b;

    public NullITD(double[] initialWeights, double a, double b) {
        this.weights = initialWeights;
        this.a = a;
        this.b = b;
    }

    public double calculateEvaluationFunction(double[] features) {
        if (features == null || features.length != weights.length) return 0;
        double sum = 0.0;
        for (int i = 0; i < weights.length; i++) {
            sum += features[i] * weights[i];
        }
        return a * Math.tanh(b * sum);
    }

    public double[] getWeigths() {
        return weights;
    }

    public void updateWeights(GameData gameData) {
    }
}
