package org.jwebsocket.config;

/**
 * Marker interface that represents the jWebSocket config element
 * @author puran
 * @version $Id: Config.java 596 2010-06-22 17:09:54Z fivefeetfurther $
 *
 */
public interface Config {

    /**
	 * Validates the configuration
	 */
    public void validate();
}
