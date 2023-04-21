package fr.x9c.cadmium.primitives.unix;

import fr.x9c.cadmium.kernel.CodeRunner;
import fr.x9c.cadmium.kernel.Fail;
import fr.x9c.cadmium.kernel.Primitive;
import fr.x9c.cadmium.kernel.PrimitiveProvider;
import fr.x9c.cadmium.kernel.Value;

/**
 * This class provides implementation for 'unix_putenv' primitive.
 *
 * @author <a href="mailto:cadmium@x9c.fr">Xavier Clerc</a>
 * @version 1.0
 * @since 1.0
 */
@PrimitiveProvider
public final class Putenv {

    /**
     * No instance of this class.
     */
    private Putenv() {
    }

    /**
     * No implementation of this primitive.
     * @param ctxt context
     * @param name ignored
     * @param val ignored
     * @return <i>unit</i>
     * @throws Fail.Exception always
     */
    @Primitive
    public static Value unix_putenv(final CodeRunner ctxt, final Value name, final Value val) throws Fail.Exception {
        Fail.invalidArgument("Unix.putenv not implemented");
        return Value.UNIT;
    }
}
