package org.logitest;

import java.util.*;

/** A representation of one part of a Path.

	@see Path
	@author Anthony Eden
*/
public class Part {

    /** Create a new Part with the given path.
	
		@param partPath
	*/
    public Part(String partPath) {
        this.partPath = partPath;
        parse();
    }

    /** Get the part name.
	
		@return The part name
	*/
    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    /** Set the part name.
	
		@param name The new part name
	*/
    public void setName(String name) {
        this.name = name;
    }

    /** Get attribute name that this part represents.  If this part does not
		represent an attribute name, then this method will return null.
	
		@return The attribute name or null
	*/
    public String getAttributeName() {
        return attributeName;
    }

    /** The attribute name that this part represents.  If this part does not
		represent an attribute name, then set this to null.
	
		@param attributeName The attribute name or null
	*/
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /** Get the predicate.
	
		@return The predicate or -1 if the predicate is not set
	*/
    public int getPredicate() {
        return predicate;
    }

    /** Set the predicate.
	
		@param predicate The new predicate value
	*/
    public void setPredicate(int predicate) {
        this.predicate = predicate;
    }

    /** Return a String representation of the Part.
	
		@return The part as a String
	*/
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getName());
        if (predicate >= 0) {
            buffer.append("[");
            buffer.append(predicate);
            buffer.append("]");
        }
        if (attributeName != null) {
            buffer.append("@");
            buffer.append(attributeName);
        }
        return buffer.toString();
    }

    private void parse() {
        String tempPartPath = partPath;
        int atIndex = partPath.indexOf("@");
        if (atIndex >= 0) {
            setAttributeName(partPath.substring(atIndex + 1));
            tempPartPath = partPath.substring(0, atIndex);
        }
        StringTokenizer tk = new StringTokenizer(tempPartPath, "[]");
        if (tk.hasMoreTokens()) {
            setName(tk.nextToken());
        }
        if (tk.hasMoreTokens()) {
            setPredicate(Integer.parseInt(tk.nextToken()));
        }
    }

    private String partPath;

    private String name;

    private String attributeName;

    private int predicate = -1;
}
