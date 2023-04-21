package gvs.repository.client.console.parser;

import com.martiansoftware.jsap.JSAPException;
import java.util.logging.Logger;
import gvs.repository.core.service.message.ServiceRequest;
import gvs.repository.core.util.StringArrayUtil;

/**
 * CommandParser.java
 * 
 * <p>
 * Parses the command supported by GVS. It calls the found command's
 * gvs.repository.client.console.parser.
 * </p>
 * 
 * @author <a href="mailto:willy.tiengo@gmail.com">Willy Tiengo</a>.
 * @since 0.1
 */
public class CommandParser {

    public static final String ADD = "add";

    public static final String CHECKOUT = "checkout";

    public static final String COMMIT = "commit";

    public static final String REMOVE = "remove";

    public static final String UPDATE = "update";

    /**
     * The logger.
     */
    private static Logger logger;

    static {
        logger = Logger.getLogger(CommandParser.class.getName());
    }

    /**
     * Parses the command supported by GVS. If it finds a command supported then
     * it call its gvs.repository.client.console.parser. Otherwise throws.
     * 
     * @param _args the command.
     * @return the object returned by specific command
     *         gvs.repository.client.console.parser. If the command array is
     *         empty, a usage is shown in the console and the program fails and
     *         exits. Otherwise throws.
     * @throws com.martiansoftware.jsap.JSAPException if any error occurs trying
     *                 to parse the checkout command array.
     */
    public static Object parse(String[] _args) throws JSAPException {
        if (_args.length == 0) {
            System.err.println("Usage: gvs ");
            System.err.println("\t" + ADD + "\t to add a file or directory");
            System.err.println("\t" + CHECKOUT + "\t to checkout a file or " + "directory");
            System.err.println("\t" + COMMIT + "\t to commit a file or " + "directory");
            System.err.println("\t" + UPDATE + "\t to update a file or " + "directory");
            System.exit(1);
        }
        ServiceRequest _req;
        if (_args[0].equals(ADD)) {
            return _args.length > 1 ? AddParser.parse(StringArrayUtil.subArray(_args, 1)) : AddParser.parse(new String[] {});
        }
        if (_args[0].equals(CHECKOUT)) {
            _req = _args.length > 1 ? (ServiceRequest) CheckoutParser.parse(StringArrayUtil.subArray(_args, 1)) : (ServiceRequest) CheckoutParser.parse(new String[] {});
            _req.setParams(_args);
            return _req;
        }
        if (_args[0].equals(COMMIT)) {
            _req = _args.length > 1 ? (ServiceRequest) CommitParser.parse(StringArrayUtil.subArray(_args, 1)) : (ServiceRequest) CommitParser.parse(new String[] {});
            _req.setParams(StringArrayUtil.concat(new String[] { _args[0] }, _req.getParams()));
            return _req;
        }
        if (_args[0].equals(REMOVE)) {
            return _args.length > 1 ? RemoveParser.parse(StringArrayUtil.subArray(_args, 1)) : RemoveParser.parse(new String[] {});
        }
        if (_args[0].equals(UPDATE)) {
            throw new UnsupportedOperationException("Operation not implemented");
        }
        throw new JSAPException("Operation '" + _args[0] + "' is unknown");
    }

    /**
     * Creates a new instance of CommandParser.
     */
    public CommandParser() {
        logger.info("Creating a new instance of CommandParser");
    }
}
