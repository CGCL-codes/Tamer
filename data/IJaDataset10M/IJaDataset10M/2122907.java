package com.cseg674.dataentry.javaparser;

import java.util.LinkedList;

/**
 * RdfMethod provides the attributes and methods that define a Method in the RDF output.
 */
public class RdfMethod extends RdfTreeNode {

    /**
	 * Contains the list of methods that are called from within the Method
	 */
    LinkedList<RdfMethod> calls;

    /**
     * Contains the list of member variables that are modified by the Method
     */
    LinkedList<RdfField> modifies;

    /**
     * Contains the list of parameters that are passed into the Method
     */
    LinkedList<RdfParam> params;

    /**
     * Cntains the list of exceptions thrown by the Method - not implemented 2009
     */
    LinkedList<RdfException> exceptions;

    /**
     * Contains the list of nest classes - not implemented 2009
     */
    LinkedList<RdfNestClass> nestclasses;

    /**
     * Contains the list of nest interfaces - not implemented 2009
     */
    LinkedList<RdfNestInterface> nestinterfaces;

    /**
     * Dummy flag for the Method
     */
    private boolean flag = false;

    /** 
     * Default Constructor: Creates a new instance of RdfMethod 
     */
    public RdfMethod() {
        this("default");
    }

    /**
     * Constructor: Creates a new instance of RdfMethod  
     * 
     * @param name	the name of the Method
     */
    public RdfMethod(String name) {
        this("default", name, null);
    }

    /**
     * Constructor: Creates a new instance of RdfMethod
     * 
     *  @param name	the name of the Method
     *  @param type	the type of the Method
     */
    public RdfMethod(String name, String type) {
        this("default", name, type);
    }

    /**
     * Constructor: Creates a new instance of RdfMethod 
     * 
     * @param perfix	the prefix of the Method
     * @param name		the name of the Method
     * @param type		the type of the Method
     */
    public RdfMethod(String perfix, String name, String type) {
        super(perfix, name, type);
        calls = new LinkedList<RdfMethod>();
        modifies = new LinkedList<RdfField>();
        params = new LinkedList<RdfParam>();
        exceptions = new LinkedList<RdfException>();
        nestclasses = new LinkedList<RdfNestClass>();
        nestinterfaces = new LinkedList<RdfNestInterface>();
    }

    /**
     * adds the Method 'a' to the list of methods that are called by this Method
     * 
     * @param a	the Method being called by this Method
     * @return	<code>true</code> if the Method was successfully added to the 'calls' list
     */
    public boolean addCalls(RdfMethod a) {
        return calls.add(a);
    }

    /**
     * adds the Member Variable 'a' to the list of fields modified by this Method
     * 
     * @param a	the field modified by the Method
     * @return	<code>true</code> if the Method was successfully added to the 'modifies' list
     */
    public boolean addModifies(RdfField a) {
        return modifies.add(a);
    }

    /**
     * adds the parameter 'a' to the list of parameters of this Method
     * 
     * @param a	the paramter of the Method
     * @return	<code>true</code> if the Parameter was successfully added to the 'params' list
     */
    public boolean addParam(RdfParam a) {
        return params.add(a);
    }

    /**
     * adds the Exception 'a' to the list of fields modified by this Method - not implemented 2009
     * 
     * @param a	the exception thrown by the Method
     * @return	<code>true</code> if the exception was successfully added to the 'exceptions' list
     */
    public boolean addException(RdfException a) {
        return exceptions.add(a);
    }

    /**
     * adds the nest class 'a' to the list of nested classes of this Method - not implemented 2009
     */
    public boolean addNestClass(RdfNestClass a) {
        return nestclasses.add(a);
    }

    /**
     * adds the nested interface 'a' to the list of nested interfaces of Method - not implemented 2009
     */
    public boolean addNestInterfaces(RdfNestInterface a) {
        return nestinterfaces.add(a);
    }

    /**
     * returns the called method in the 'calls' list at index 'i'
     * 
     * @param i	the index of the list that is returned
     * @return	the Method at index 'i'
     */
    public RdfMethod getCalls(int i) {
        return calls.get(i);
    }

    /**
     * returns the modified method in the 'modifies' list at index 'i'
     * 
     * @param i	the index of the list that is returned
     * @return	the Method at index 'i'
     */
    public RdfField getModifies(int i) {
        return modifies.get(i);
    }

    /**
     * returns the parameter in the 'params' list at index 'i'
     * 
     * @param i	the index of the list that is returned
     * @return	the parameter at index 'i'
     */
    public RdfParam getParam(int i) {
        return params.get(i);
    }

    /**
     * returns the number of methods called by this Method
     * 
     * @return	the size of the 'calls' list
     */
    public int sizeOfCalls() {
        return calls.size();
    }

    /**
     * returns the number of methods modified by this Method
     * 
     * @return	the size of the 'modifies' list
     */
    public int sizeOfModifies() {
        return modifies.size();
    }

    /**
     * checks if the method is a dummy method or an actual method
     * 
     * @return	<code>true</code> if the Method is a dummy, else <code>false</code>
     */
    public boolean isDummy() {
        return flag;
    }

    /**
     * Sets the dummy flag
     */
    public void setDummy() {
        flag = true;
    }

    /**
     * returns the number of parameters of this Method
     * 
     * @return	the size of the 'params' list
     */
    public int sizeOfParams() {
        return params.size();
    }
}
