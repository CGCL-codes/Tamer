package de.fraunhofer.ipsi.xpathDatatypes;

import javax.xml.namespace.QName;

public interface Numeric extends Comparable<Numeric> {

    public static final QName TYPENAME = new QName("numeric");

    /**
	 * Method add
	 *
	 * @param    a                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric add(Numeric a);

    /**
	 * Method subtract
	 *
	 * @param    a                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric subtract(Numeric a);

    /**
	 * Method multiply
	 *
	 * @param    a                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric multiply(Numeric a);

    /**
	 * Method divide
	 *
	 * @param    a                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric divide(Numeric a);

    /**
	 * Method idivide
	 *
	 * @param    a                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric idivide(Numeric a);

    /**
	 * Method mod
	 *
	 * @param    a                   a  Numeric
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric mod(Numeric a);

    /**
	 * Method negate
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric negate();

    /**
	 * Method abs
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric abs();

    /**
	 * Method floor
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric floor();

    /**
	 * Method ceiling
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric ceiling();

    /**
	 * Method round
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric round();

    /**
	 * Method roundHalfToEven
	 *
	 * @param    precision           an int
	 *
	 * @return   a Numeric
	 *
	 */
    Numeric roundHalfToEven(int precision);
}
