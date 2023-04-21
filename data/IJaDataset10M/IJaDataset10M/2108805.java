package fr.x9c.cadmium.primitives.unix;

import java.net.InetAddress;
import java.net.UnknownHostException;
import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;

/**
 * This class provides implementation for 'unix_inet_addr_of_string' primitive.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Addrofstr {

    /**
     * No instance of this class.
     */
    private Addrofstr() {
    }

    /**
     * Converts a <i>a.b.c.d</i> string into an address.
     * @param ctxt context
     * @param s string to convert
     * @return corresponding address
     * @throws Fail.Exception if address conversion fails
     */
    @Primitive
    public static Value unix_inet_addr_of_string(final CodeRunner ctxt, final Value s) throws Fail.Exception {
        try {
            return Unix.createInetAddr(ctxt, InetAddress.getByName(s.asBlock().asString()));
        } catch (final UnknownHostException uhe) {
            Fail.failWith("inet_addr_of_string");
            return Value.UNIT;
        }
    }
}
