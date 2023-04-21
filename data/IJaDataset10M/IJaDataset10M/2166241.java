package org.tokaf.normalizer;

import java.util.Comparator;
import org.tokaf.TopKElement;

/**
 * <p> Normalizer transforms given object into a number from [0,1]. </p> <p>
 * Copyright (c) 2006 </p>
 * @author Alan Eckhardt
 * @version 1.0
 */
public interface Normalizer extends Comparator {

    /**
	 * Normalizes given object.
	 * @param te TopKElement The Element, whose attribute we want to normalize.
	 * @param index int Index of attribute.
	 * @return The number between [0,1].
	 */
    public double Normalize(TopKElement te, int index);

    /**
	 * Normalizes given object.
	 * @param o Object to be normalized.
	 * @return The number between [0,1].
	 */
    public double Normalize(Object o);

    /**
	 * Optional funtion. Normalizer should have user name, to allow distinction
	 * among various users.
	 * @return User name associated with this normalizer.
	 */
    String getUserName();

    /**
	 * Optional funtion. This function should sets the user name to normalizer.
	 * @param userName Name of user to be set.
	 */
    void setUserName(String userName);
}
