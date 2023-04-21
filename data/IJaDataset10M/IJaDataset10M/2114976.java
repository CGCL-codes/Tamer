package prajna.semantic.endeca;

import java.util.HashMap;
import prajna.semantic.writer.DataGenerator;

/**
 * Implementation of a data generator which accesses Endeca records. The data
 * translator includes an internal EndecaDataAccessor, which generates the
 * semantic content from the Endeca records. It also uses a SemanticWriter,
 * which formats the output. Extensions of this class should call init() with
 * an appropriate SemanticWriter, a configuration file for the initialization
 * of the EndecaDataAccessor, and a default data structure. The config file
 * determines which fields are used to generate the links within a graph, which
 * fields are temporal or geographic, and so forth.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 * @deprecated use the more general prajna.semantic.writer.DataGenerator
 */
@Deprecated
public class EndecaTranslator extends DataGenerator {

    /**
     * Create a new endece translator. This method simply sets the accessor of
     * the data generator to an EndecaAccessor.
     */
    public EndecaTranslator() {
        setAccessor(new EndecaAccessor());
    }

    /**
     * Set the host and port of the Endeca engine that this translator accesses
     * 
     * @param host the host
     * @param port the port
     */
    public void setHostAndPort(String host, int port) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("host", host);
        map.put("port", Integer.toString(port));
        getAccessor().setInitParameters(map);
    }

    /**
     * Set the host and port of the Endeca engine that this translator accesses
     * 
     * @param host the host
     * @param port the port
     */
    public void setHostAndPort(String host, String port) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("host", host);
        map.put("port", port);
        getAccessor().setInitParameters(map);
    }

    /**
     * Set the record filter used by this translator
     * 
     * @param filter the record filter
     */
    public void setRecordFilter(String filter) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("filter", filter);
        getAccessor().setInitParameters(map);
    }
}
