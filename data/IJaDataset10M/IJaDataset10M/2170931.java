package shellkk.qiq.jdm.common.kernel;

public class KernelFormerFactor {

    protected double target;

    protected double coefficient;

    protected double weight;

    protected double epsilon;

    public KernelFormerFactor getCopy() {
        KernelFormerFactor copy = new KernelFormerFactor();
        copy.setTarget(target);
        copy.setCoefficient(coefficient);
        copy.setWeight(weight);
        copy.setEpsilon(epsilon);
        return copy;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }
}
