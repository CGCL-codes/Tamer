package shieh.pnn.core;

import java.io.Serializable;

public abstract class BetaFunc implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6796165174315151474L;

    public abstract double beta(int dominantPhase, int phase);
}
