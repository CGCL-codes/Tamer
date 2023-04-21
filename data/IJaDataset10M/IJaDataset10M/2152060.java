package es.rediris.searchy;

import es.rediris.searchy.engine.Query;
import es.rediris.searchy.dc.DC;
import es.rediris.searchy.dc.DCResource;

/**
 * It defines the API to access an agent. Any implementation of an agent
 * should implement it
 * 
 * @version $Id: Searchy.java,v 1.6 2005/09/06 07:12:27 dfbarrero Exp $
 * @author David F. Barrero
 */
public interface Searchy {

    /**
	 * It gets the agent's name
	 */
    public String getName();

    /**
	 *
	 */
    public DC search(String element, String query);

    public DC search(Query query);

    /**
     * It returns the agent's version
	 *
	 * @return String Agent's version
	 */
    public String getVersion();
}
