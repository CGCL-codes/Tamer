package net.sf.orcc.tools.normalizer;

import net.sf.orcc.OrccException;
import net.sf.orcc.network.Network;
import net.sf.orcc.network.transforms.INetworkTransformation;

/**
 * This class defines a network transformation that merges actors until a
 * fixpoint is found.
 * 
 * @author Matthieu Wipliez
 * 
 */
public class ActorMerger implements INetworkTransformation {

    /**
	 * Creates a new merger
	 */
    public ActorMerger() {
    }

    /**
	 * Tries to merge actors.
	 * 
	 * @return <code>true</code> if actors were merged, <code>false</code>
	 *         otherwise
	 */
    private boolean mergeActors() {
        return false;
    }

    @Override
    public void transform(Network network) throws OrccException {
        boolean changed;
        do {
            changed = mergeActors();
        } while (changed);
    }
}
