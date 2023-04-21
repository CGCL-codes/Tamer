package org.xaloon.wicket.component.exception;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas racelis
 */
public class JcrSessionException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public JcrSessionException(String workspace, Exception e) {
        super(workspace, e);
    }
}
