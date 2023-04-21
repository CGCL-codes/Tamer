package com.quikj.application.web.talk.client;

/**
 * 
 * @author amit
 */
public class ListGroupElement {

    private String name = null;

    private String fullName = null;

    private int callCount = 0;

    /** Creates a new instance of GroupElement */
    public ListGroupElement() {
    }

    public ListGroupElement(String name, String full_name, int call_count) {
        this.name = name;
        fullName = full_name;
        callCount = call_count;
    }

    /**
	 * Getter for property callCount.
	 * 
	 * @return Value of property callCount.
	 */
    public int getCallCount() {
        return callCount;
    }

    /**
	 * Getter for property fullName.
	 * 
	 * @return Value of property fullName.
	 */
    public java.lang.String getFullName() {
        return fullName;
    }

    /**
	 * Getter for property name.
	 * 
	 * @return Value of property name.
	 */
    public java.lang.String getName() {
        return name;
    }

    /**
	 * Setter for property callCount.
	 * 
	 * @param callCount
	 *            New value of property callCount.
	 */
    public void setCallCount(int callCount) {
        this.callCount = callCount;
    }

    /**
	 * Setter for property fullName.
	 * 
	 * @param fullName
	 *            New value of property fullName.
	 */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }

    /**
	 * Setter for property name.
	 * 
	 * @param name
	 *            New value of property name.
	 */
    public void setName(java.lang.String name) {
        this.name = name;
    }
}
