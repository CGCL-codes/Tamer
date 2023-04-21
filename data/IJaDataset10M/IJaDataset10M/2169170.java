package fr.x9c.cadmium.primitives.unix;

import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;

/**
 * This class provides implementation for protocol-related primitives.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Getproto {

    /**
     * No instance of this class.
     */
    private Getproto() {
    }

    /**
     * Raises <i>Not_found</i>.
     * @param ctxt context
     * @param name ignored
     * @return <i>unit</i>
     * @throws Fail.Exception always
     */
    @Primitive
    public static Value unix_getprotobyname(final CodeRunner ctxt, final Value name) throws Fail.Exception {
        Fail.raiseNotFound();
        return Value.UNIT;
    }

    /**
     * Raises <i>Not_found</i>.
     * @param ctxt context
     * @param proto ignored
     * @return <i>unit</i>
     * @throws Fail.Exception always
     */
    @Primitive
    public static Value unix_getprotobynumber(final CodeRunner ctxt, final Value proto) throws Fail.Exception {
        Fail.raiseNotFound();
        return Value.UNIT;
    }
}
