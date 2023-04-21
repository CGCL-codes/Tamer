package net.cimd;

/**
 * Description of the Class
 *
 * @author    <a href="mailto:vsuman@gmail.com">Viorel Suman</a>
 * @version   $Id: WrongStateException.java,v 1.1 2007/03/14 14:17:55 viorel_suman Exp $
 */
public class WrongStateException extends Exception {

    /**
	 * Constructor for the WrongStateException object
	 *
	 * @param state    Description of the Parameter
	 * @param message  Description of the Parameter
	 */
    public WrongStateException(State state, String message) {
        super(message);
    }
}
