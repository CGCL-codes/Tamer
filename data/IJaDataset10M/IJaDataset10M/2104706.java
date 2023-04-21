package moduledefault.classify.c45.rafael.jadti;

/**
 * A test on a single numerical attribute.<p>
 * The test checks if an attribute's value is smaller than a fixed threshold.
 **/
public class NumericalTest extends Test {

    private double threshold;

    /**
     * Creates a new test.
     *
     * @param attribute The attribute on which the test is performed.
     * @param threshold The test threshold.
     **/
    public NumericalTest(NumericalAttribute attribute, double threshold) {
        super(attribute);
        this.threshold = threshold;
    }

    /**
     * Applies the test. The test checks if the tested value is smaller
     * than a threshold. 
     *
     * @param value The value to test.
     * @return 1 if the value is smaller than the threshold, 0 otherwise.
     **/
    public int perform(AttributeValue value) {
        if (!(value instanceof KnownNumericalValue)) throw new IllegalArgumentException("Wrong value type");
        return perform((KnownNumericalValue) value);
    }

    /**
     * Applies the test. The test checks if the tested value is smaller
     * than a threshold. 
     *
     * @param value The value to test.
     * @return 1 if the value is smaller than the threshold, 0 otherwise.
     **/
    public int perform(KnownNumericalValue value) {
        return (value.doubleValue < threshold) ? 1 : 0;
    }

    public int nbIssues() {
        return 2;
    }

    public String toString() {
        return attribute.toString() + " < " + String.format("%.4f", threshold);
    }

    public String issueToString(int issueNb) {
        switch(issueNb) {
            case 0:
                return "Não";
            case 1:
                return "Sim";
            default:
                throw new IllegalArgumentException("Invalid issue number");
        }
    }
}
