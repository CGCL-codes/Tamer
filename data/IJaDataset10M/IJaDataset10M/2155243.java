package ch.sahits.math;

/**
 * This interface describes a function that can be used to integrate over.
 * 
 * @author Andi Hotz (c) by Sahits.ch 2007
 * @version 1.0
 */
public interface IFunction {

    /**
	 * Function over which can be integrated
	 * @param x value
	 * @return value of f(x)
	 */
    public double f(double x);
}
