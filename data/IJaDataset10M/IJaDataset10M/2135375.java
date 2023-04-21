package siouxsie.desktop.core;

/**
 * For resource needing application configuration.
 * Ex : Action
 * @author Arnaud Cogolu�gnes
 * @version $Id$
 *
 */
public interface IApplicationConfigurable {

    /**
	 * Configure the resource.
	 * @param application
	 */
    public void configure(Application application);
}
