package au.edu.diasb.chico.config.mapper;

import java.net.URISyntaxException;

/**
 * An object that implements this interface is capable of rewriting a URI. 
 * 
 * @author scrawley
 */
public interface URIMapper {

    /**
     * Map a URI.  An implementation may do one of the following:
     * <ul>
     * <li>return the URI unchanged,
     * <li>return a different URI,
     * <li>return {@literal null} which should be interpreted as 
     *     meaning "don't allow this URI", or
     * <li>throw an exception.
     * </ul>
     * 
     * @param uri the URI to be mapped
     * @return the mapped exception or {@literal null}.
     * @throws URISyntaxException
     */
    String mapURI(String uri) throws URISyntaxException;
}
