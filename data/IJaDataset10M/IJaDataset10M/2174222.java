package ch.ethz.mxquery.sms.btree;

import ch.ethz.mxquery.datamodel.StructuralIdentifier;
import ch.ethz.mxquery.datamodel.adm.LinguisticToken;
import ch.ethz.mxquery.util.ObjectObjectPair;

/**
 * Represents a node from the b-tree (either directory node or leaf).
 * 
 * @author jens/marcos extended by christian/julia
 * adapted by jimhof (adapted to be CLDC conform)
 */
public interface BTreeNode {

    /**
	 * for bulk loading
	 */
    public ObjectObjectPair bulkAdd(ObjectObjectPair data, int k, double F);

    /**
	 * To get the root of the btree this node is sitting in
	 * 
	 * @return BTreeNode - the root
	 */
    public BTreeNode getRoot();

    /**
	 * Indicates if this node is a leaf.
	 * 
	 * @return true if a leaf
	 */
    public boolean isLeaf();

    /**
	 * Returns the internal node/ leaf that contains the key "key"
	 * @param key
	 * @return BTreeNode
	 */
    public BTreeNode getBTreeNode(StructuralIdentifier key);

    /**
	 * Creates pointers to the given key or if the key is not in the tree,
	 * to the smallest key larger than the given one that is 
	 * in the tree. 
	 * 
	  * @param key
	 * @param inLeaf points to the leaf where to get the key
	 * @param atPos	points to the position of the key inside the leaf
	 */
    public void getFirstKeyAfter(StructuralIdentifier key, Leaf[] inLeaf, int[] atPos);

    /**
	* Creates pointers to the given key or if the key is not in the tree,
	 * to the largest key smaller than the given one that is 
	 * in the tree. 
	 * 
	 * @param key
	 * @param inLeaf points to the leaf where to get the key
	 * @param atPos	points to the position of the key inside the leaf
	 */
    public void getLastKeyBefore(StructuralIdentifier key, Leaf[] inLeaf, int[] atPos);

    /**
	 * Obtains all values mapped to the given key range (low and high,
	 * inclusive). Values are delivered to the provided push operator.
	 * 
	 * @param lowKey
	 * @param highKey
	 * @param results
	 */
    public void queryRange(StructuralIdentifier lowKey, StructuralIdentifier highKey, BtreePushOperator results);

    /**
	 * Adds the given mapping to the node.
	 * 
	 * @param key
	 * @param value
	 * @return SplitInfo if BtreeNode had to be split to keep k or k_star. else null
	 */
    public SplitInfo add(StructuralIdentifier key, LinguisticToken value, StructuralIdentifier lowKey, StructuralIdentifier highKey, LeafCarrier leafCarrier);

    /**
	 * Removes a single instance of the key-value mapping from the node. If the
	 * value given is equal to BTreeConstants.ALL_MAPPINGS then all mappings
	 * associated to the given key will be removed.
	 * 
	 * @param key
	 * @param lowKey
	 * @param highKey
	 */
    public void remove(StructuralIdentifier key, LinguisticToken value, StructuralIdentifier lowKey, StructuralIdentifier highKey);

    /**
	 * Indicates if this node is empty.
	 * 
	 * @return true if empty 
	 */
    public boolean isEmpty();

    /**
	 * sets the parentNode.
	 * 
	 * @param parentNode
	 */
    public void setParent(InternalNode parentNode);

    /**
	 * gets the lowest key recursively
	 * 
	 * @return the smallest key
	 */
    public StructuralIdentifier getLowestKey();
}
