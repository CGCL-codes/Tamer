package org.xmlcml.cml;

import java.util.Vector;

/** the size attribute is optional and gives the numebr of elements in
an array. It is subservient to the actual array length and may be used to
check validity and throw an Exception
*/
public interface AttributeSize {

    public static final String SIZE = "size";

    public int getSize();

    /** this will return an element of appropriate subclass */
    public CMLStringVal elementAt(int i);

    /** updates an element, converting to numeric if appropriate; */
    public void setElementAt(int i, Object obj);

    /** adds an element, converting to numeric if appropriate; */
    public void addElement(String s) throws CMLException;

    public Vector getStringVector();

    public void setDelimiter(String delimiter);

    public String getDelimiter();

    /** maintenance methods */
    public boolean updateDOMHasSize();

    public boolean processDOMHasSize();
}
