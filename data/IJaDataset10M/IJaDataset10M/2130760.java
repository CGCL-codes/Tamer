package peersim.dynamics;

import peersim.config.*;
import peersim.core.*;
import peersim.graph.*;

/**
* This class contains the implementation of the Barabasi-Albert model
* of growing scale free networks. The original model is described in
* <a href="http://arxiv.org/abs/cond-mat/0106096">http://arxiv.org/abs/cond-mat/0106096</a>. It also contains the option of building
* a directed network, in which case the model is a variation of the BA model
* described in <a href="http://arxiv.org/pdf/cond-mat/0408391">
http://arxiv.org/pdf/cond-mat/0408391</a>. In both cases, the number of the
* initial set of nodes is the same as the degree parameter, and no links are
* added. The first added node is connected to all of the initial nodes,
* and after that the BA model is used normally.
* @see GraphFactory#wireScaleFreeBA
*/
public class WireScaleFreeBA extends WireGraph {

    /**
 * The number of edges added to each new node (apart from those forming the 
 * initial network).
 * Passed to {@link GraphFactory#wireScaleFreeBA}.
 * @config
 */
    private static final String PAR_DEGREE = "k";

    /** Parameter of the BA model. */
    private int k;

    /**
 * Standard constructor that reads the configuration parameters.
 * Invoked by the simulation engine.
 * @param prefix the configuration prefix for this class
*/
    public WireScaleFreeBA(String prefix) {
        super(prefix);
        k = Configuration.getInt(prefix + "." + PAR_DEGREE);
    }

    /** calls {@link GraphFactory#wireScaleFreeBA}.*/
    public void wire(Graph g) {
        GraphFactory.wireScaleFreeBA(g, k, CommonState.r);
    }
}
