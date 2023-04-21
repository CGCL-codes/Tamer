package examples.statistics.v16;

import anima.component.ISupports;

/**
 * This is a simple statistic interface that describe basic functionalities
 * with generics support.
 */
public interface Statistics<E extends Number> extends ISupports {

    /**
	 * Insert an value, some Number
	 * @param number
	 */
    public void insertValue(E number);

    /**
	 * New Method with generic support you can insert an array of numbers that
	 * you desire to calculate
	 * @param numbers
	 */
    public void insertValue(E[] numbers);

    /**
	 * New Method that return a sum of values to see the precisions available
	 * @return the an Precision object, that return the value with some precision
	 * @see examples.statistics.v16.Precision
	 */
    public Precision sum();

    /**
	 * New method with generic support calculate the average of numbers; to see
	 * the precisions available
	 * @return the an Precision object, that return the value with some precision
	 * @see examples.statistics.v16.Precision
	 */
    public Precision average();
}
