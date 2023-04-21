package de.bioutils.range;

/**
 * <p>
 * Something that has a start point and a stop point.
 * </p>
 * 
 * @deprecated
 * @author Alexander Kerner
 * @lastVisit 2009-12-15
 * 
 * 
 */
public interface RangeFeature {

    /**
	 * <p>
	 * Get begin index of this <code>RangeFeature</code>.
	 * </p>
	 * 
	 * @return start index
	 */
    int getStart();

    /**
	 * <p>
	 * Get end index of this <code>RangeFeature</code>.
	 * </p>
	 * 
	 * @return end index
	 */
    int getStop();

    Range getRange();
}
