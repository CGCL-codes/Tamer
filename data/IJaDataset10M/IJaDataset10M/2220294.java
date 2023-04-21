package org.mulgara.util;

/**
 * Functor template for a function that takes arguments of three different types
 * and returns a value of a fourth type, possibly throwing an exception.
 * @param <T1> The type of the first parameter of the function.
 * @param <T2> The type of the second parameter of the function.
 * @param <T3> The type of the third parameter of the function.
 * @param <R> The return type of the function.
 * @param <E> The type of exception that may be thrown from the function.
 *
 * @author Paul Gearon
 */
public interface Fn3E<T1, T2, T3, R, E extends Exception> {

    /**
   * Declares a function template that takes three arguments and returns a value of
   * another type.
   * @param arg1 The first argument.
   * @param arg2 The second argument.
   * @param arg3 The third argument.
   * @return A value based on arg1, arg2 and arg3.
   * @throws E An exception that may be thrown from this method.
   */
    R call(T1 arg1, T2 arg2, T3 arg3) throws E;
}
