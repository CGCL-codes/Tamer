package net.sf.kerner.commons.range;

import net.sf.kerner.commons.collection.Factory;

/**
 * 
 * A factory that creates objects of type {@link IntegerRange}.
 * 
 * <p>
 * <b>Example:</b><br>
 * 
 * </p>
 * <p>
 * 
 * <pre>
 * TODO example
 * </pre>
 * 
 * </p>
 * 
 * @author <a href="mailto:alex.kerner.24@googlemail.com">Alexander Kerner</a>
 * @version 2010-11-18
 * 
 */
public interface IntegerRangeFactory extends Factory<IntegerRange> {

    /**
	 * 
	 * Create a {@link IntegerRange} with given start and stop positions.
	 * 
	 * @param start
	 *            start position of created {@code IntegerRange}
	 * @param stop
	 *            stop position of created {@code IntegerRange}
	 * @return newly created {@code IntegerRange}
	 */
    IntegerRange create(int start, int stop);

    /**
	 * 
	 * Create a {@link IntegerRange} with from given template.
	 * 
	 * @param template
	 *            template that is used to create new {@code IntegerRange}
	 * @return newly created {@code IntegerRange}
	 */
    IntegerRange create(IntegerRange template);
}
