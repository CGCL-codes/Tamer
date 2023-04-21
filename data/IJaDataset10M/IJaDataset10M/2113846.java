package com.duniptech.engine.core.modeling.api;

import java.io.Serializable;

public interface IDevs extends Serializable {

    public static final double INFINITY = Double.POSITIVE_INFINITY;

    /**
	 * <P>DEVS time advance function.</P>
	 */
    double ta();

    /**
	 * <P>DEVS internal transition function.</P>
	 */
    void deltint();

    /**
	 * <P>DEVS external transition function.</P>
	 * @param e Elapsed time.
	 * @param x Input message.
	 */
    void deltext(double e, IMessage x);

    /**
	 * <P>DEVS confluent function.</P>
	 * @param e Elapsed time.
	 * @param x Input message.
	 */
    void deltcon(double e, IMessage x);

    /**
	 * <P>DEVS output function.</P>
	 * @return The new output message.
	 */
    IMessage lambda();
}
