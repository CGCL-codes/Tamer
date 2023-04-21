package playground.scnadine.fuzzy;

/**
 * This hedge is an implementation of fuzzy "very"
 */
public class HedgeVery extends Hedge {

    /**
 * getName method for "very"
 */
    @Override
    public java.lang.String getName() {
        return "very";
    }

    /**
 * hedgeIt method for "very"
 */
    @Override
    public double hedgeIt(double value) {
        return value * value;
    }
}
