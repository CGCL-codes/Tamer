package gossipServices.basic.nodeDescriptors;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Description:
 * A NodeDescriptor is the object that identifies a node in the cloud. 
 * Peers use NodeDescriptors to exchange informations with each other.
 * A NodeDescriptor must contain the:
 *  - name of the node,
 *  - the address of the machine,
 *  - the node ID. 
 * This is the basic interface expected from all peers. 
 * Each peer keeps its own PartialView that's a 
 * finite collection of NodeDescriptor(s).
 * This object generally represents the very basic item for a node.
 * More specific objects with additional informations may be created 
 * using the NodeDescriptor as a base class.
 *  
 */
public interface NodeDescriptor extends Serializable {

    /**
	 * Every NodeDescriptor has to ensure that its ID has a length of
	 * ID_LENGTH. Generally it's a good strategy to make the nodeName and the ID
	 * of the same LENGTH. 
	 */
    public static final int ID_LENGTH = 8;

    /**
	 * The WILD_CHAR is used to fill the node name
	 * that is too short than the fixed length.
	 */
    public static final String WILD_CHAR = "#";

    /**
	 * To a node is associated a name in order to distinguish it if more
	 * than a node is launched on a single machine. 
	 * @return name:String
	 */
    public String getNodeName();

    /**
	 * Returns the address of the host machine on which a node is running.
	 * @return address:InetAddress
	 */
    public InetAddress getNodeAddress();

    /**
	 * Returns the NodeID used of the local.
	 * Management structures such as Map(s) or PrefixTable may use 
	 * the NodeID to build an overlay network upon the nodes. 
	 * @return
	 */
    public Integer getNodeID();
}
