package secolaman.data.entries;

import org.apache.commons.math.complex.Complex;

/**
 * Complex number matcher entry
 * 
 * @author jbgiraudeau
 */
public interface ComplexMatcherEntry {

    /**
	 * @return median value of the matching interval.
	 */
    public double getDelta();

    /**
	 * @return the name of the attribute to check for matching.
	 */
    public String getKey();

    /**
	 * @return deviation allowed from the median value.
	 */
    public Complex getMedian();
}
