package org.ikasan.common.xml.transform;

import java.util.HashMap;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import org.apache.log4j.Logger;

/**
 * This calss implements this interface
 * <code>javax.xml.transform.URIResolver</code>
 * that can be called by the processor to turn a URI used in
 * document(), xsl:import, or xsl:include into a Source object.
 *
 * @author Ikasan Development Team
 *
 */
public class DefaultURIResolver implements URIResolver {

    /**
     * The logger instance.
     */
    private static Logger logger = Logger.getLogger(DefaultURIResolver.class);

    /**
     * The mapping table to map URIs to <code>Source</code>.
     */
    private HashMap<String, Source> uriToSourceMap = new HashMap<String, Source>(10);

    /**
     * Creates a new instance of <code>DefaultURIResolver</code>
     * with the default trace level.
     */
    public DefaultURIResolver() {
    }

    /**
     * Maps URIs to <code>StreamSource</code>.
     *
     * @param uri    is the URI (i.e. relative reference).
     * @param source is the <code>StreamSource</code> to be mapped
     *                  to the specified URI.
     */
    public void mapURIToSource(String uri, Source source) {
        if (uri == null) {
            logger.warn("URI string can't null, returning...");
            return;
        }
        if (uri.trim().length() == 0) {
            logger.warn("URI string can't be empty, returning...");
            return;
        }
        if (source == null) {
            logger.warn("Stream source can't be null, returning...");
            return;
        }
        logger.debug("Mapping URI [" + uri + "] to Source...");
        this.uriToSourceMap.put(uri, source);
    }

    /**
     * Called by the processor when it encounters
     * an xsl:include, xsl:import or document() function.
     * @param href 
     * @param base 
     * @return Source
     * @throws TransformerException 
     *
     */
    public Source resolve(String href, String base) throws TransformerException {
        logger.debug("Returning the associated stream source using " + "base:[" + base + "] and href:[" + href + "]...");
        return this.uriToSourceMap.get(href);
    }
}
