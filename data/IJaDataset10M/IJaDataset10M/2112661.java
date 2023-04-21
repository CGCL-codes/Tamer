package org.opt4j.ea;

import org.opt4j.common.random.Rand;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The default selector is the {@link Spea2} selector.
 * 
 * @author lukasiewycz
 *
 */
@Singleton
public class SelectorDefault extends Spea2 {

    /**
	 * Constructs a {@code DefaultSelector}.
	 * 
	 * @param random the random number generator
	 */
    @Inject
    public SelectorDefault(Rand random) {
        super(0, random);
    }
}
