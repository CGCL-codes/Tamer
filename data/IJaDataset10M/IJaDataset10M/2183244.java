package com.google.testing.threadtester;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Contains information about a method that has been instrumented.
 *
 * @see ClassInstrumentation
 *
 * @author alasdair.mackintosh@gmail.com (Alasdair Mackintosh)
 */
public interface MethodInstrumentation {

    /**
   * Returns the name of the method.
   */
    String getName();

    /**
   * Returns the Java Method object corresponding to this method.
   */
    Method getUnderlyingMethod();

    /**
   * Returns the lines in this method.
   */
    List<LineInstrumentation> getLines();

    /**
   * Returns the number of executable lines in the method.
   */
    int getNumLines();
}
