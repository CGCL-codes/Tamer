package com.cseg674.dataentry.javaparser;

/**
   * RdfExceptionprovides the attributes and methods that define an exception 
   * in the RDF output
   *
   * @author Tian, Juan
   */
public class RdfException extends RdfTreeNode {

    /** 
	   * Default Constructor: Creates a new instance of RdfException 
	   *
	   */
    public RdfException() {
        this("default");
    }

    /** 
	   * Non-Default Constructor 1
	   * Creates a new instance of RdfException using arguments
	   * @param perfix the prefix of the Exception
	   *
	   */
    public RdfException(String perfix) {
        this(perfix, null, null);
    }

    /** 
	   * Non-Default Constructor 2
	   * Creates a new instance of RdfException using arguments
	   * @param name the name of the Exception
	   * @param type the type of the Exception
	   */
    public RdfException(String name, String type) {
        this("default", name, type);
    }

    /** 
	   * Non-Default Constructor 3
	   * Creates a new instance of RdfException using arguments
	   *
	   * @param perfix the prefix of the Exception
	   * @param name	the name of the Exception
	   * @param type the type of the Exception
	   *
	   */
    public RdfException(String perfix, String name, String type) {
        super(perfix, name, type);
    }
}
