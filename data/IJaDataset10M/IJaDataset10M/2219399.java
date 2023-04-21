package org.xmlcml.cml;

import java.util.Vector;

/**
<span class="DTDClass">DTD Class</span><br />
Mainly a syntactic container
*/
public interface CMLFloatArray extends CMLFloatVal, AttributeSize {

    public void setDelimiter(String delimiter);

    public String getDelimiter();

    public int getSize();

    /** actually returns CMLFloatVal */
    public CMLStringVal elementAt(int i);

    public double getFloat(int i);

    public void setElementAt(double f, int i);

    /** add the CMLFloatVal. If it is the first to be added, copy the CMLFloatVal 
attributes as well as the value
*/
    public void addElement(CMLFloatVal f);

    public void addElement(double f);

    public Vector getStringVector();

    public String[] getStringList();
}
