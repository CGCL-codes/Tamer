package org.szegedi.spring.web.jsflow.support;

import java.util.Random;
import org.mozilla.javascript.NativeContinuation;

/**
 * Default implementation of flow state id generator that uses a random number
 * generator.
 * @author Attila Szegedi
 * @version $Id: $
 */
public class RandomFlowStateIdGenerator implements FlowStateIdGenerator {

    private Random random;

    public void setRandom(Random random) {
        this.random = random;
    }

    public Long generateStateId(NativeContinuation c) {
        return new Long(random.nextLong() & Long.MAX_VALUE);
    }

    public boolean dependsOnContinuation() {
        return false;
    }
}
