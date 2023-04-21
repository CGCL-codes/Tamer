package fi.hip.gb.portlet.results;

/**
 * When something goes wrong with getting results from the server
 *
 * @author Antti Ahvenlampi
 * @version $Id: ResultException.java 476 2005-08-15 15:20:12Z ahvenlam $
 */
public class ResultException extends Exception {

    /**
	 * @param msg
	 */
    public ResultException(String msg) {
        super(msg);
    }
}
