package gvs.repository.client.console.parser;

import com.martiansoftware.jsap.JSAPException;
import java.util.logging.Logger;
import gvs.repository.core.service.message.ServiceRequest;
import gvs.repository.core.util.StringArrayUtil;

/**
 * GVSParser.java
 * 
 * <p>
 * Parses a GVS command.
 * </p>
 * 
 * @see SomeRelatedClass.
 * 
 * @author <a href="mailto:willy.tiengo@gmail.com">Willy Tiengo</a>.
 * @since 0.1
 */
public class GVSParser {

    private static String GVS_COMMAND = "gvs";

    /**
     * The logger.
     */
    private static Logger logger;

    static {
        logger = Logger.getLogger(GVSParser.class.getName());
    }

    /**
     * Parses the GVS command. The first command in the gvs command array must
     * always be "<i>gvs</i>".
     * 
     * @param _args the gvs command array.
     * @return the object returned by <code>CommandParser.parse</code> method
     * @throws RuntimeException if any error occurs.
     */
    public static Object parse(String[] _args) throws RuntimeException {
        if (!_args[0].equals(GVS_COMMAND)) {
            throw new RuntimeException("Command '" + _args[0] + "' is unknown");
        }
        try {
            Object _obj;
            _obj = _args.length > 1 ? CommandParser.parse(StringArrayUtil.subArray(_args, 1)) : CommandParser.parse(new String[] {});
            if (_obj instanceof ServiceRequest) {
                ((ServiceRequest) _obj).setParams(StringArrayUtil.concat(new String[] { _args[0] }, ((ServiceRequest) _obj).getParams()));
            }
            return _obj;
        } catch (JSAPException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Creates a new instance of GVSParser.
     */
    public GVSParser() {
        logger.info("Creating a new instance of GVSParser");
    }
}
