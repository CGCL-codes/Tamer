package genomancer.trellis.das2.model;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 */
public interface Das2CapabilityI extends IdentifiableI {

    public void init(Map<String, String> params);

    /**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public String getType();

    /**
 * <p>Does ...</p>
 * 
 */
    public Das2CoordinatesI getCoordinates();

    public Das2VersionI getVersion();

    /**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public List<Das2FormatI> getFormats();

    /**
 * <p>Does ...</p>das
 * 
 * 
 * @return 
 */
    public List<String> getSupportedExtensions();

    /**
 * <p>Does ...</p>
 * 
 * 
 * @return 
 */
    public List<org.jdom.Element> getAdditionalData();

    /**
     *   Support for handling of client-side caching via Last-Modified and If-Modified-Since headers
     *   Basic Trellis framework doesn't know how to figure out if data sources have changed between 
     *   requests, but capability.getLastModified() allows Trellis plugins to implement via 
     *
     *   Capabilities that don't know about modification times should return -1 to indicate unknown 
     *     mod time.
     */
    public long getLastModified();
}
