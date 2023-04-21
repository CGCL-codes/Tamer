package org.gmod.srs;

import org.gmod.exceptions.SrsException;

/**
 * An interface that defines the basic requirements for an
 * SRS interface.
 * 
 * @author Josh Goodman
 * @version CVS $Revision: 1.4 $
 * 
 */
public interface SrsIO {

    /**
	 * Function to run some form of a SRS query and return the results.
	 * 
	 * @return An SrsResultSet returned from the query executed.
	 * @throws SrsException
	 */
    public SrsResultSet runQuery() throws SrsException;
}
