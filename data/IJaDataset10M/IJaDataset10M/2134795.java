package org.openscience.jchempaint.io;

/**
 * The interface for JCP file filters
 *
 */
public interface IJCPFileFilter {

    /**
	 *  Gets the type attribute of the JCPFileFilterInterface object
	 *
	 *@return    The type value
	 */
    public String getType();

    /**
	 *  Sets the type attribute of the JCPFileFilterInterface object
	 *
	 *@param  type  The new type value
	 */
    public void setType(String type);
}
