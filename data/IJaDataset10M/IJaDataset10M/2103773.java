package sk.naive.talker.props;

import java.util.Set;

/**
 * Simple abstract class for properties with known valid values set.
 * Class is defining only checkValue(String) method.
 *
 * @author <a href="mailto:richter@bgs.sk">Richard Richter</a>
 * @version $Revision: 1.9 $ $Date: 2004/04/25 08:11:55 $
 */
public abstract class EnumeratedProperty extends SimpleProperty {

    /** Should be initialized in subclass. */
    protected Set validValues;

    /**
	 * Value is valid if it belongs in the validValues() set.
	 * @param o checked value
	 * @return true if validValues().contains(s)
	 */
    public boolean checkValue(Object o) {
        if (validValues == null) {
            return true;
        }
        return validValues().contains(o);
    }

    public Set validValues() {
        return validValues;
    }
}
