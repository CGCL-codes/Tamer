package com.google.singletondetector.output;

/**
 * The OutputNode representation of a Mingleton. A mingleton is defined as a
 * class which has a static method that returns a non-primitive while taking no
 * arguments. The name is short for "Misko's Singleton," in honor of the man who
 * thought to check for this type of static state.
 * 
 * @author David Rubel
 */
public class MingletonOutputNode extends OutputNode {

    /**
   * Default constructor. Assigns the appropriate values to color and shape to
   * represent a Mingleton.
   * 
   * @param className Name of the class this node represents
   */
    public MingletonOutputNode(String className) {
        super(className);
        fillColor = "FFFF00";
        textColor = "000000";
        shape = "rectangle";
    }
}
