package fitgoodies.log4j;

import java.util.Map;

/**
 * Parser which extracts a <code>Map</code> from a cell.
 * The parameter format must be &quot;[key1=value1, key2=value2, ...]&quot;
 *
 * @author jwierum
 * @version $Id: CellArgumentParser.java 197 2009-08-21 12:30:26Z jwierum $
 */
public interface CellArgumentParser {

    /**
	 * Gets the extracted parameters.
	 * @return key/value pair of parameters
	 */
    Map<String, String> getExtractedCommandParameters();
}
