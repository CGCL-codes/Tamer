package de.tum.in.botl.metamodel.implementation;

import de.tum.in.botl.util.BotlException;

/**
 * @author marschal
 * 08.09.2003
 */
public class MetamodelConsistencyException extends BotlException {

    /**
   * 
   */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public MetamodelConsistencyException() {
        super();
    }

    /**
	 * @param s
	 */
    public MetamodelConsistencyException(String s) {
        super(s);
    }
}
