package jbf;

import java.util.Hashtable;

/**
 *  Description of the Class 
 *
 *@author     dennis 
 *@created    March 27, 2000 
 */
class Namedhash extends Hashtable {

    String nm;

    /**
	 *  Constructor for the Namedhash object 
	 *
	 *@param  s  Description of Parameter 
	 */
    public Namedhash(String s) {
        super();
        nm = s;
    }

    /**
	 *  Description of the Method 
	 *
	 *@return    Description of the Returned Value 
	 */
    public String name() {
        return nm;
    }
}
