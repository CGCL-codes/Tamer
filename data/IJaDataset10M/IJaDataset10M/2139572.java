package genomancer.trellis.das2.model;

import java.net.URI;
import java.util.List;

/**
 * 
 * 
 */
public interface Das2TypesCapabilityI extends Das2CapabilityI {

    public Das2TypesResponseI getTypes();

    public Das2TypeI getType(URI type_uri);

    public Das2FormatI getFormat(String format_name);
}
