package org.nodal.nav;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leei
 */
public abstract class OpaqueHandler {

    /**
   * A Map from String->OpaqueHandler that takes a scheme and produces a handler
   * for the opaque arguments to that scheme.
   */
    private static Map handlers = new HashMap();

    /**
   * This constructor registers the OpaqueHandler with the handler Map.
   */
    protected OpaqueHandler(String scheme) {
        register(scheme, this);
    }

    /**
   * Remember the String->OpaqueHandler mapping for future reference.
   * @param scheme a String for the handler's recognized scheme
   * @param handler an OpaqueHandler that recognizes arguments to this scheme.
   */
    private static void register(String scheme, OpaqueHandler handler) {
        handlers.put(scheme, handler);
    }

    /**
   * Apply the opaque operator to the given scheme.  If there is an
   * OpaqueHandler for this scheme, then use it to parse the opaque argument
   * and apply them to the scheme's Path.
   * 
   * @param scheme a String for a URI scheme
   * @param opaque a String representing the opaque argument to that scheme
   * @return a Path that includes the scheme and a possibly parsed 
   * @throws Path.Failure if any of these steps fail
   */
    public static Path applyOpaque(String scheme, String opaque) throws Path.Failure {
        Path path = SchemeOp.create(scheme).applyTo(null);
        OpaqueHandler handler = (OpaqueHandler) handlers.get(scheme);
        if (handler != null) {
            path = handler.parseOpaque(path, opaque);
        } else {
            path = OpaqueOp.create(opaque).applyTo(path);
        }
        return path;
    }

    abstract Path parseOpaque(Path path, String opaque) throws Path.Failure;
}
