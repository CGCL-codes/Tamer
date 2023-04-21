package com.lordjoe.collectiveintelligence;

/**
 * com.lordjoe.collectiveintelligence.ICostFunction
 *
 * @author Steve Lewis
 * @date Mar 24, 2009
 */
public interface ICostFunction<T> {

    public static ICostFunction[] EMPTY_ARRAY = {};

    public static Class THIS_CLASS = ICostFunction.class;

    public double computeCost(T target, Object... otherData);

    public double lowestCost();
}
