package uk.org.toot.control;

/**
 * A linear control law.
 * Note that int values are always zero based, as required by the UI and persistence.
 * The user values have the range requested by the user
 */
public class IntegerLaw extends AbstractLaw {

    private int res;

    public IntegerLaw(int min, int max, String units) {
        super(min, max, units);
        assert min >= 0;
        assert max < min + resolution;
        assert min < max;
        res = 1 + max - min;
    }

    public int intValue(float v) {
        return Math.round(v - min);
    }

    public float userValue(int v) {
        return v + min;
    }

    /**
     * We override resolution to force UI to use discrete positions.
     */
    @Override
    public int getResolution() {
        return res;
    }
}
