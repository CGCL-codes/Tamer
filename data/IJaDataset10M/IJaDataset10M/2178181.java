package org.shapelogic.machinelearning;

import java.util.List;
import org.shapelogic.calculation.Calc1;
import org.shapelogic.util.Constants;

/** ConfidenceArraySelector takes a double[] and creates a String.<br />
 *
 * Translates a double[] that could come from a neural network to either the
 * number of the one that is winning walue if any is or to a name for that.<br />
 *
 * @author Sami Badawi
 *
 */
public class ConfidenceArraySelector implements Calc1<double[], String> {

    public static final double DEFAULT_LIMIT = 0.5;

    /** Parallel to the NumberedStream. */
    protected List<String> _ohNames;

    protected double _limit = DEFAULT_LIMIT;

    /** Use the ohName to also be the name of the input stream. <br />
	 * 
	 * @param ohNames
	 * @param limit
	 */
    public ConfidenceArraySelector(List<String> ohNames, double limit) {
        _ohNames = ohNames;
        _limit = limit;
    }

    /** Use the ohName to also be the name of the input stream. <br />
	 *
	 * @param ohNames
	 */
    public ConfidenceArraySelector(List<String> ohNames) {
        this(ohNames, DEFAULT_LIMIT);
    }

    @Override
    public String invoke(double[] input) {
        int countOK = 0;
        int lastFound = -1;
        for (int i = 0; i < input.length; i++) {
            if (_limit < input[i]) {
                countOK++;
                lastFound = i;
            }
        }
        if (countOK == 1) {
            if (_ohNames != null && lastFound < _ohNames.size()) return _ohNames.get(lastFound); else return "" + lastFound;
        }
        return Constants.NO_OH;
    }

    public void setOhNames(List<String> ohNames) {
        _ohNames = ohNames;
    }

    public List<String> getOhNames() {
        return _ohNames;
    }
}
