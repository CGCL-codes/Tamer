package org.ceno.communication.cli;

/**
 * @author Andre Albert &lt;andre.albert82@googlemail.com&gt
 * @created 12.04.2010
 * @since 0.0.5
 */
public interface IServerUnavailableObserver {

    /**
	 * Invoked callback after server could not be reached. Not depend if
	 * graceful shutdown or not
	 * 
	 * @since 0.0.5
	 */
    void serverUnavailable();
}
