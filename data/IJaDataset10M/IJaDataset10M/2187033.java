package de.sambalmueslie.geocache_planer.file_manager.parser.gpx;

import java.util.Collection;
import java.util.Vector;
import de.sambalmueslie.geocache_planer.common.Geocache;
import de.sambalmueslie.geocache_planer.common.Waypoint;
import de.sambalmueslie.geocache_planer.common.parser.GeocacheParser;
import de.sambalmueslie.geocache_planer.file_manager.parser.gpx.groundspeak.GroundspeakCache;
import de.sambalmueslie.geocache_planer.file_manager.parser.gpx.groundspeak.GroundspeakLog;
import de.sambalmueslie.geocache_planer.file_manager.parser.gpx11.GPXWaypoint;
import de.sambalmueslie.geocache_planer.file_manager.reader.XMLFileReader;
import de.sambalmueslie.geocache_planer.file_manager.reader.XMLNode;

/**
 * parse a Groundspeak GPX file.
 * 
 * @author Sambalmueslie
 * 
 * @date 25.05.2009
 * 
 */
public class GroundspeakGPXFileParser extends GPX10FileParser {

    /** the attribute for archived. */
    private static final String ATTRIBUTE_ARCHIVED = "archived";

    /** the attribute for available. */
    private static final String ATTRIBUTE_AVAILABLE = "available";

    /** the attribute for the creator. */
    private static final String ATTRIBUTE_CREATOR = "creator";

    /** the attribute for encoded. */
    private static final String ATTRIBUTE_ENCODED = "encoded";

    /** the attribute for the id . */
    private static final String ATTRIBUTE_ID = "id";

    /** the attribute for the version. */
    private static final String ATTRIBUTE_VERSION = "version";

    /** the attribute for the xmlns. */
    private static final String ATTRIBUTE_XMLNS = "xmlns";

    /** the prefix of the groundspeak tags. */
    private static final String PREFIX_GSP = "groundspeak:";

    /** the tag name for a cache. */
    private static final String TAGNAME_CACHE = "cache";

    /** the tag name for a container. */
    private static final String TAGNAME_CONTAINER = "container";

    /** the tag name for a country. */
    private static final String TAGNAME_COUNTRY = "country";

    /** the tag name for a date. */
    private static final String TAGNAME_DATE = "date";

    /** the tag name for the difficulty. */
    private static final String TAGNAME_DIFFICULTY = "difficulty";

    /** the tag name for tje emcoded hints. */
    private static final String TAGNAME_ENCODED_HINTS = "encoded_hints";

    /** the tag name for the finder. */
    private static final String TAGNAME_FINDER = "finder";

    /** the tag name for a log. */
    private static final String TAGNAME_LOG = "log";

    /** the tag name for logs. */
    private static final String TAGNAME_LOGS = "logs";

    /** the tag name for the long desciption. */
    private static final String TAGNAME_LONG_DESCRIPTION = "long_description";

    /** the tag name for the name. */
    private static final String TAGNAME_NAME = "name";

    /** the tag name for the owner. */
    private static final String TAGNAME_OWNER = "owner";

    /** the tag name for the placed by. */
    private static final String TAGNAME_PLACED_BY = "placed_by";

    /** the tag name for the short description. */
    private static final String TAGNAME_SHORT_DESCRIPTION = "short_description";

    /** the tag name for the state. */
    private static final String TAGNAME_STATE = "state";

    /** the tag name for the terrain. */
    private static final String TAGNAME_TERRAIN = "terrain";

    /** the tag name for the text. */
    private static final String TAGNAME_TEXT = "text";

    /** the tag name for the type. */
    private static final String TAGNAME_TYPE = "type";

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean isFileSuitable(final XMLFileReader reader) {
        final XMLNode rootNode = reader.getRootNode();
        final String nodeName = rootNode.getName();
        if (!nodeName.equals("gpx")) {
            return false;
        }
        final String version = rootNode.getAttribute(ATTRIBUTE_VERSION);
        final String creator = rootNode.getAttribute(ATTRIBUTE_CREATOR);
        final String xmlns = rootNode.getAttribute(ATTRIBUTE_XMLNS);
        if (version.equals("1.0") && xmlns.equals("http://www.topografix.com/GPX/1/0") && creator.contains("Groundspeak")) {
            return true;
        }
        return false;
    }

    /**
	 * parse a {@link Boolean} to a {@link String}.
	 * 
	 * @param value
	 *            the string
	 * @return the result
	 */
    private boolean parseBoolean(final String value) {
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }

    /**
	 * parse a {@link GroundspeakLog} from an {@link XMLNode}.
	 * 
	 * @param node
	 *            the node
	 * @return the value
	 */
    private GroundspeakLog parseGroundspeakLog(final XMLNode node) {
        final String id = node.getAttribute(ATTRIBUTE_ID);
        final String date = node.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_DATE);
        final String type = node.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_TYPE);
        final XMLNode finderNode = node.getSubNode(PREFIX_GSP + TAGNAME_FINDER);
        final String finderId = finderNode.getAttribute(ATTRIBUTE_ID);
        final String finderName = finderNode.getNodeTextValue();
        final XMLNode textNode = node.getSubNode(PREFIX_GSP + TAGNAME_TEXT);
        final String text = textNode.getNodeTextValue();
        final boolean isEncrypted = parseBoolean(textNode.getAttribute(ATTRIBUTE_ENCODED));
        final GroundspeakLog log = new GroundspeakLog(id, date, type, finderId, finderName, text, isEncrypted);
        return log;
    }

    /**
	 * parse some logs from some nodes.
	 * 
	 * @param nodes
	 *            the nodes
	 * @return the values
	 */
    private Collection<GroundspeakLog> parseGroundspeakLogs(final Collection<XMLNode> nodes) {
        final Vector<GroundspeakLog> logs = new Vector<GroundspeakLog>();
        for (final XMLNode node : nodes) {
            final GroundspeakLog log = parseGroundspeakLog(node);
            if (log != null) {
                logs.add(log);
            }
        }
        return logs;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected Waypoint convertToWaypoint(final GPXWaypoint wpt) {
        if (wpt instanceof GroundspeakCache) {
            final Geocache element = new Geocache(wpt + "", ((GroundspeakCache) wpt).getCacheType(), wpt.getLatitude(), wpt.getLongitude());
            return element;
        }
        return super.convertToWaypoint(wpt);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected GPXWaypoint parseWaypoint(final XMLNode node) {
        final GPXWaypoint wpt = super.parseWaypoint(node);
        final XMLNode cacheNode = node.getSubNode(PREFIX_GSP + TAGNAME_CACHE);
        final String cacheId = cacheNode.getAttribute(ATTRIBUTE_ID);
        final boolean available = parseBoolean(cacheNode.getAttribute(ATTRIBUTE_AVAILABLE));
        final boolean archived = parseBoolean(cacheNode.getAttribute(ATTRIBUTE_ARCHIVED));
        final String name = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_NAME);
        final String placedBy = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_PLACED_BY);
        final XMLNode ownerNode = cacheNode.getSubNode(PREFIX_GSP + TAGNAME_OWNER);
        final String ownerId = ownerNode.getAttribute(ATTRIBUTE_ID);
        final String ownerName = ownerNode.getNodeTextValue();
        final Geocache.TYPE type = GeocacheParser.parseType(cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_TYPE));
        final String container = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_CONTAINER);
        final String difficulty = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_DIFFICULTY);
        final String terrain = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_TERRAIN);
        final String country = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_COUNTRY);
        final String state = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_STATE);
        final String shortDesciption = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_SHORT_DESCRIPTION);
        final String longDesciption = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_LONG_DESCRIPTION);
        final String encodedHints = cacheNode.getNodeSubNodeValue(PREFIX_GSP + TAGNAME_ENCODED_HINTS);
        final XMLNode logsNode = cacheNode.getSubNode(PREFIX_GSP + TAGNAME_LOGS);
        final Collection<GroundspeakLog> logs = parseGroundspeakLogs(logsNode.getSubNodes(PREFIX_GSP + TAGNAME_LOG));
        final GroundspeakCache cache = new GroundspeakCache(wpt, cacheId, available, archived, name, placedBy, ownerId, ownerName, type, container, difficulty, terrain, country, state, shortDesciption, longDesciption, encodedHints, logs);
        return cache;
    }
}
