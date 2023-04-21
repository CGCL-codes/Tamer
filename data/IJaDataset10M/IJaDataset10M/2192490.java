package org.compiere.model;

/**
 * 	Database Null Indicator 
 *	
 *  @author Jorg Janke
 *  @version $Id: Null.java,v 1.2 2006/07/30 00:58:04 jjanke Exp $
 */
public class Null {

    /** Singleton				*/
    public static final Null NULL = new Null();

    /**
	 * 	NULL Constructor
	 */
    private Null() {
    }

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        return "NULL";
    }
}
