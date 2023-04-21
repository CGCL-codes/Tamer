package es.rediris.searchy.engine.map;

import java.util.HashMap;
import es.rediris.searchy.dc.DCResource;

/**
 * Interface that declares the access to an implementation
 * of a mapping.
 * 
 * @version $Id: Map.java,v 1.3 2005/09/22 15:05:30 dfbarrero Exp $
 * @author David F. Barrero
 */
public interface Map {

    public void addMapQuery(String key, String filter);

    public void addMapResponse(String key, String filter);

    public String getMapQueryAttribute(String key, String attribute);

    public void addMapQueryAttribute(String key, String attribute, String value);

    /**
	 * It sets a filter to compose the subject of the resource. The
	 * special string $URL$ is reserved to be used by providers to
	 * hard define an URL.
	 */
    public void setUriFilter(String uriFilter);

    /**
	 * It obtains a query string for element given by <code>
	 * key</code> and a query given by <code>query</query>
	 *
	 * @param key Element we query to
	 * @param query The query
	 *
	 * @return query string, <code>null</code> if no filter has been
	 * stored for given key
	 **/
    public String mapQuery(String key, String query);

    /**
	 * It maps a complete response to a DC object.
	 * 
	 * @param uri URI that represents the resource
	 *
	 * @param result The result we want to map. The field name
	 * must be the key in the hash table.
	 *
	 * @return a DCResource object with the mapped response
	 */
    public DCResource mapResponse(String uri, HashMap result);
}
