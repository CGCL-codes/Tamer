package gavator.composite.logic;

import gavator.core.*;

/**
 * @author Sailing
 * 
 */
public class BinaryTruer<F, S> implements BinaryPredicate<F, S> {

    public boolean isTrue(F left, S right) {
        return true;
    }
}
