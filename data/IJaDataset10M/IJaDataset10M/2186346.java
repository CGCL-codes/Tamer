package com.genia.toolbox.web.taglib.head.js;

/**
 * This class represents a javascript function that must be called in the
 * initialization process.
 */
public class JsFunction implements Comparable<JsFunction> {

    /**
   * the name of the javascript function.
   */
    private final transient String name;

    /**
   * the priority of this function.
   */
    private final transient int order;

    /**
   * constructor.
   * 
   * @param name
   *          the name of the javascript function
   * @param order
   *          the priority of this function
   */
    public JsFunction(final String name, final int order) {
        this.name = name;
        this.order = order;
    }

    /**
   * comparaison method.
   * 
   * @param function
   *          the other function
   * @return the value <code>0</code> if the function is equal to this string;
   *         a value less than <code>0</code> if this function has a lesser
   *         priority than the function argument; and a value greater than
   *         <code>0</code> if this function has a greater priority than the
   *         function argument.
   */
    public int compareTo(final JsFunction function) {
        final int res = order - function.order;
        if (res != 0) {
            return res;
        }
        return name.compareTo(function.name);
    }

    /**
   * getter for the name property.
   * 
   * @return the name
   */
    public String getName() {
        return name;
    }
}
