package net.community.chest.lang.math;

import java.util.Comparator;

/**
 * Copyright 2007 as per GPLv2
 * 
 * @param <V> Type of compared value
 * @author Lyor G.
 * @since Jun 10, 2007 2:59:29 PM
 */
public interface NumbersComparator<V extends Number & Comparable<V>> extends Comparator<V> {

    /**
	 * @return {@link Class} of {@link Number}-s being compared
	 */
    Class<V> getNumbersClass();
}
