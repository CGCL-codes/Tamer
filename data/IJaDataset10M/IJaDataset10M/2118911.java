package propasm.parallax.pseudo;

import propasm.model.Operation;
import propasm.model.Predicate;

/**
 * NOP - pseudo-op
 * Spends four clocks navel-gazing.
 * 
 * More specifically, NOP is a word of zeroes.  With no effect bits set and
 * a predicate of IF_NEVER, it does absolutely nothing.
 * 
 * @author cbiffle
 *
 */
public class NopOp extends Operation {

    @Override
    public boolean generatesResultByDefault() {
        return false;
    }

    @Override
    public int getOpcode() {
        return 0;
    }

    @Override
    public Predicate defaultPredicate() {
        return Predicate.IF_NEVER;
    }
}
