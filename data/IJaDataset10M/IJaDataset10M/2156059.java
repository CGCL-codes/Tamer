package org.jmule.ui.sacli.controller;

import java.util.List;

/** This interface provides methods to execute commands on the app lication context.   	
 * @author StefanOrtmanns
 * @version $Revision: 1.1.1.1 $
 * <br>Last changed by $Author: jmartinc $ on $Date: 2005/04/22 21:45:39 $
 */
public interface Controller {

    /** executes a controller command.
	 * @param command the contoller command
	 */
    public List execute(Command command) throws Exception;
}
