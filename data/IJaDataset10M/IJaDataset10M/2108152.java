package neural;

/**
 * Symmetric sigmoid activation function
 * @author Administrator
 */
public class ActivationFunctionSymmetricSigmoid implements IActivationFunction {

    public double calculateSymmetricSigmoid(double input) {
        return Math.tanh(input);
    }

    public double calculateOutput(double input) {
        return this.calculateSymmetricSigmoid(input);
    }

    public double calculateDerivative(double input) {
        return (1 - this.calculateSymmetricSigmoid(input) * this.calculateSymmetricSigmoid(input));
    }

    public String getType() {
        return "Symmetric sigmoid";
    }
}
