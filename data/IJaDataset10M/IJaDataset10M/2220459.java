package gossipServices.pss;

import gossipServices.GossipService;
import gossipServices.basic.nodeDescriptors.NodeDescriptor;
import gossipServices.basic.view.PartialView;

/**
 * 
 * Description:
 * It list the interface of a Peer Sampling Service 
 * according to
 * "Gossip-based Peer Sampling", M. Jelasity, S. Voulgaris, 
 * R. Guerraoui, A. Kermarrec, M. Van Steen.
 * 
 *
 */
public interface PeerSamplingServiceAPI extends GossipService {

    /**
	 * The init() method initialize the service.
	 * It's called a the beginning of the computation.
	 * Return true if no error occurs.
	 * @return done : boolean
	 */
    public boolean init();

    /**
	 * The getPeer() method returns a peer picked up 
	 * from the PartialView of the node according to 
	 * a strategy (random or tail).
	 * Return true if no error occurs.
	 * @return peer : NodeDescriptor
	 */
    public NodeDescriptor getPeer();

    /**
	 * Method that gives the access to the local
	 * partial view of the node.
	 * @return
	 */
    public PartialView accessPartialView();
}
