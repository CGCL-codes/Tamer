package com.lordjoe.collectiveintelligence.data;

/**
 * com.lordjoe.collectiveintelligence.data.IPair
 *
 * @author Steve Lewis
 * @date May 12, 2009
 */
public interface IPair<T> {

    public static IPair[] EMPTY_ARRAY = {};

    public static Class THIS_CLASS = IPair.class;

    public T getMinimumValue();

    public T getMaximumValue();
}
