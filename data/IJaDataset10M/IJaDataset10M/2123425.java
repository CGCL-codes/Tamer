package playground.scnadine.fuzzy;

/**
 * Fuzzy "not" is implemented as a hedge
 */
public class HedgeNot extends Hedge {

    /**
 * getName method for "not".
 */
    @Override
    public java.lang.String getName() {
        return "not";
    }

    /**
 * hedgeIt method for "not"
 */
    @Override
    public double hedgeIt(double value) {
        return 1.0 - value;
    }
}
