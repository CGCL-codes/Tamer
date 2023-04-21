package dbPhase.hypeerweb;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import dbPhase.states.*;
import dbPhase.hypeerweb.HyPeerWebDatabase;
import distributed.GlobalObjectId;
import distributed.ObjectDB;

/**
 * A node in the HyPeerWeb.
 *
 * <b>Domain:</b>
 *    webId       : WebId
 *    neighbors     : SetOf<Node>
 *    upPointers    : SetOf<Node>
 *    downPointers  : SetOf<Node>
 *    fold          : Node
 *    surrogateFold : Node
 *
 * <b>Instance Invariant:</b>
 *       None for Phase 1
 *
 * @author Scott Cornaby, Robby Canady
 */
public class Node implements Serializable {

    private WebId webID;

    private Set<Node> neighbors;

    private Set<Node> upPointers;

    private Set<Node> downPointers;

    private Node fold;

    private Node surrogateFold;

    private int height;

    private int ourHash;

    private NodeState state;

    private Contents content;

    /**
	 * The NULL_NODE to be used in place of a Node variable or actual
	 * parameter with the value null.
	 */
    public static Node NULL_NODE = new Node(WebId.NULL_WEB_ID);

    /**
	 * Default Constructor
	 *
	 * @pre None
	 * @post webId = new WebId(0), |neighbors| = 0, |upPointers| = 0, |downPointers| = 0, 
	 * fold = NULL_NODE, surrogateFold = NULL_NODE
	 * 
	 * @author Scott Cornaby
	 */
    public Node() {
        state = CapNodeState.getNodeState();
        webID = new WebId(0);
        neighbors = new HashSet<Node>();
        upPointers = new HashSet<Node>();
        downPointers = new HashSet<Node>();
        fold = NULL_NODE;
        surrogateFold = NULL_NODE;
        height = 0;
        ourHash = 0;
        content = new Contents();
    }

    /**
	 * Constructor that takes an int as a parameter to create a new webID to initialize the node.
	 *
	 * @param i  The int representation of the webID of the node to construct.
	 * 
	 * @pre i >= -1, i = -1 means this is the NULL_NODE
	 * @post webId.id = i, |neighbors| = 0, |upPointers| = 0, |downPointers| = 0, 
	 * fold = this, surrogateFold = NULL_NODE
	 *
	 * @author 
	 */
    public Node(int i) {
        Initialize(new WebId(i));
    }

    /**
	 * Constructor that takes a webID as a parameter to initialize the node.
	 *
	 * @param id The webID used to initialize the node.
	 *
	 * @pre id.getID() >= -1, id.getID() = -1 means this is the NULL_NODE
	 * @post webId = id, |neighbors| = 0, |upPointers| = 0, |downPointers| = 0, 
	 * fold = this, surrogateFold = NULL_NODE
	 * 
	 * @author
	 */
    public Node(WebId id) {
        Initialize(id);
    }

    /**
	 * Generates a proxy of this node
	 * 
	 * @pre 
	 * @post
	 * 
	 *  @author Robby Canady
	 */
    public NodeProxy generateProxy() {
        GlobalObjectId goi = new GlobalObjectId();
        NodeProxy proxy = new NodeProxy(goi);
        ObjectDB.getSingleton().store(goi.getLocalObjectId(), this);
        return proxy;
    }

    /**
	 * Initializes the node.  Sets the WebID.  Creates HashSets for neighbors, upPointers, and downPointers.  
	 * Sets the fold to be itself.
	 *
	 * @param WebId  The WebID to be set.
	 *
	 * @pre id.getID() >= -1, id.getID() = -1 means this is the NULL_NODE
	 * @post webId = id, |neighbors| = 0, |upPointers| = 0, |downPointers| = 0, 
	 * fold = this, surrogateFold = NULL_NODE, height = log(2)id.getID(), state = CapNodeState
	 *
	 * @author
	 */
    private void Initialize(WebId id) {
        webID = id;
        neighbors = new HashSet<Node>();
        upPointers = new HashSet<Node>();
        downPointers = new HashSet<Node>();
        fold = this;
        content = new Contents();
        if (id.getId() == -1) {
            surrogateFold = this;
        } else {
            surrogateFold = NULL_NODE;
        }
        height = getHeightFromWebId();
        state = CapNodeState.getNodeState();
        ourHash = 0;
    }

    /**
	 * Constructs and returns a SimplifiedNodeDomain object using the information in the node.
	 *
	 * @return A reference to the new SimplifiedNodeDomain object.
	 *
	 * @pre neighbors != null, upPointers != null, downPointers != null
	 * @post For the returned SimplifiedNodeDomain ourDomain: ourDomain.neighbors contains all nodes in neighbors and 
	 * no nodes not in neighbors, ourDomain.upPointers contains all nodes in upPointers and no nodes not in upPointers,
	 * ourDomain.downPointers contains all nodes in downPointers and no nodes nod in downPointers
	 * 
	 * @author Karen Downs
	 */
    public SimplifiedNodeDomain constructSimplifiedNodeDomain() {
        SimplifiedNodeDomain ourDomain = new SimplifiedNodeDomain(webID.getId(), fold.webID.getId(), surrogateFold.webID.getId(), state.getStateID(), height);
        ourDomain.setNeighbors(new HashSet<Integer>());
        ourDomain.setDownPointers(new HashSet<Integer>());
        ourDomain.setUpPointers(new HashSet<Integer>());
        for (Node n : neighbors) {
            ourDomain.neighbors.add(n.webID.getId());
        }
        for (Node n : upPointers) {
            ourDomain.upPointers.add(n.webID.getId());
        }
        for (Node n : downPointers) {
            ourDomain.downPointers.add(n.webID.getId());
        }
        return ourDomain;
    }

    /**
	 * Gets the WebID of the node.
	 *
	 * @return A reference to the WebID of the node.
	 *
	 * @pre webID != null
	 * @post The webID returned = this.webID
	 *
	 * @author Ken DeCelle
	 */
    public WebId getWebId() {
        return webID;
    }

    /**
	 * Sets the WebID to the given WebID.
	 *
	 * @param newWebId The WebId to be set as the webID.
	 *
	 * @pre newWebID != null
	 * @post webID = newWebId
	 *
	 * @author Ken DeCelle
	 */
    public void setWebId(WebId newWebId) {
        webID = newWebId;
    }

    /**
	 * Gets the state.
	 *
	 * @return The state of the node.
	 *
	 * @pre state != null
	 * @post state returned = state
	 *
	 * @author Ken DeCelle
	 */
    public NodeState getState() {
        return state;
    }

    /**
	 * Sets the state.
	 * 
	 * @param newState The new state to set the node to.
	 *
	 * @pre newState != null
	 * @post state = newState
	 *
	 * @author Ken DeCelle
	 */
    public void setState(NodeState newState) {
        state = newState;
    }

    /**
	 * 2-5-2011
	 * Returns the height of the node
	 *
	 * @pre height > 0
	 * @post height returned = height
	 *
	 * @author Robby Canady
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * 2-5-2011
	 * Sets the height of the Node.
	 *
	 * @param newHeight to set height to
	 *
	 * @pre newHeight > 0
	 * @post height = newHeight
	 *
	 * @author Robby Canady
	 */
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    /**
	 * Gets the set of down pointers.
	 *
	 * @return The set of down pointers for the node.
	 *
	 * @pre downPointers != null
	 * @post return set = downPointers
	 *
	 * @author 
	 */
    public Set<Node> getDownPointers() {
        return downPointers;
    }

    /**
	 * Gets the set of up pointers.
	 *
	 * @return The set of up pointers for the node.
	 *
	 * @pre upPointers != null
	 * @post return set = upPointers
	 *
	 * @author 
	 */
    public Set<Node> getUpPointers() {
        return upPointers;
    }

    /**
	 * 2-5-2011
	 * Returns the neighbors.
	 * 
	 * @return The set of neighbors for the node.
	 *
	 * @pre neighbors != null
	 * @post return set = neighbors
	 *
	 * @author Robby Canady
	 */
    public Set<Node> getNeighbors() {
        return neighbors;
    }

    /**
	 * Gets the fold.
	 * 
	 * @return The fold of the node.
	 *
	 * @pre fold != null
	 * @post return node = fold
	 *
	 * @author 
	 */
    public Node getFold() {
        return fold;
    }

    /**
	 * Gets the surrogate fold.
	 * 
	 * @return The surrogate fold of the node.
	 *
	 * @pre surrogateFold != null
	 * @post return node = surrogateFold
	 *
	 * @author 
	 */
    public Node getSurrogateFold() {
        return surrogateFold;
    }

    /**
	 * Sets the given node as the fold.
	 *
	 * @param upPointer The node to be added as the fold.
	 *
	 * @pre upPointer != null
	 * @post fold = upPointer, the database contains the set of this and upPointer in the folds table
	 *
	 * @author Ken DeCelle
	 */
    public void setFold(Node upPointer) {
        fold = upPointer;
        HyPeerWebDatabase.getHyPeerWebDatabase().setNodeFold(webID.getId(), fold.getWebId().getId());
    }

    /**
	 * Sets the given node as the surrogate fold.
	 *
	 * @param newSurrogateFold  The name of the hyPeerWeb to return.
	 * 
	 * @pre newSurrogateFold != null
	 * @post surrogateFold = newSurrogateFold, the database contains the set of this and newSurrogateFold 
	 * in the surrogateFolds table, all other nodes that were in surrogateFolds are still in surrogateFolds
	 *
	 * @author Ken DeCelle
	 */
    public void setSurrogateFold(Node newSurrogateFold) {
        surrogateFold = newSurrogateFold;
        HyPeerWebDatabase.getHyPeerWebDatabase().setNodeSurrogateFold(webID.getId(), surrogateFold.getWebId().getId());
    }

    /**
	 * Adds the given node as a down pointer.
	 *
	 * @pre newDownPointer != null, newDownPointer exists in the HyPeerWeb, newDownPointer is not an element 
	 * of newDownPointer
	 * @post downPointers is one larger and contains newDownPointer, there is a set of this and newDownPointer 
	 * in the downPointers table, all other nodes that were in downPointers are still in downPointers
	 *
	 * @param newDownPointer The node to be added as a down pointer
	 *
	 * @author Ken DeCelle
	 */
    public void addDownPointer(Node newDownPointer) {
        if (downPointers.add(newDownPointer)) {
            HyPeerWebDatabase.getHyPeerWebDatabase().addDownPointer(webID.getId(), newDownPointer.webID.getId());
        }
    }

    /**
	 * Adds the given node as an up pointer.
	 *
	 * @pre newUpPointer != null, newUpPointer exists in the HyPeerWeb, newUpPointer is not an element 
	 * of newUpPointer
	 * @post upPointers is one larger and contains newUpPointer, there is a set of this and newUpPointer 
	 * in the upPointers table, all other nodes that were in upPointers are still in upPointers
	 *
	 * @param newUpPointer The node to be added as an up pointer.
	 *
	 * @author Ken DeCelle
	 */
    public void addUpPointer(Node newUpPointer) {
        if (upPointers.add(newUpPointer)) {
            HyPeerWebDatabase.getHyPeerWebDatabase().addUpPointer(webID.getId(), newUpPointer.getWebId().getId());
        }
    }

    /**
	 * Adds the given node as a neighbor.
	 *
	 * @pre neighbor != null, neighbor exists in the HyPeerWeb, neighbor is not an element of neighbors
	 * @post neighbors is one larger and contains neighbor, there is a set of this and neighbor 
	 * in the neighbors table, all other nodes that were in neighbors are still in neighbors
	 *
	 * @param neighbor The node to be added as a neighbor.
	 *
	 * @author Ken DeCelle
	 */
    public void addNeighbor(Node neighbor) {
        if (neighbors.add(neighbor)) {
            HyPeerWebDatabase.getHyPeerWebDatabase().addNeighborPair(webID.getId(), neighbor.webID.getId());
        }
    }

    /**
	 * Removes the given node as a neighbor.
	 *
	 * @pre neighbor != null, neighbor exists in the HyPeerWeb, neighbor is an element of neighbors
	 * @post neighbors is one smaller and does not contain neighbor but contains all of the other nodes it 
	 * had before the removal, the database table neighbors contains one less set and does not contain the 
	 * set this and neighbor
	 *
	 * @param neighbor The node to be removed as a neighbor.
	 *
	 * @author Ken DeCelle
	 */
    public void removeNeighbor(Node neighbor) {
        if (neighbors.remove(neighbor)) {
            HyPeerWebDatabase.getHyPeerWebDatabase().removeNeighbor(webID.getId(), neighbor.webID.getId());
        }
    }

    /**
	 * Removes the given node as a down pointer.
	 *
	 * @pre downPointer != null, downPointer exists in the HyPeerWeb, downPointer is an element of downPointers
	 * @post downPointers is one smaller and does not contain downPointer but contains all of the other nodes it 
	 * had before the removal, the database table downPointers contains one less set and does not contain the 
	 * set this and downPointer
	 *
	 * @param downPointer The node to be removed as a down pointer.
	 *
	 * @author
	 */
    public void removeDownPointer(Node downPointer) {
        if (downPointers.remove(downPointer)) {
            HyPeerWebDatabase.getHyPeerWebDatabase().removeDownPointer(webID.getId(), downPointer.webID.getId());
        }
    }

    /**
	 * Removes the given node as an up pointer.
	 *
	 * @pre downPointer != null, downPointer exists in the HyPeerWeb, downPointer is an element of downPointers
	 * @post downPointers is one smaller and does not contain downPointer but contains all of the other nodes it 
	 * had before the removal, the database table downPointers contains one less set and does not contain the 
	 * set this and downPointer
	 *
	 * @param upPointer The node to be removed as an up pointer.
	 *
	 * @author
	 */
    public void removeUpPointer(Node upPointer) {
        if (upPointers.remove(upPointer)) {
            HyPeerWebDatabase.getHyPeerWebDatabase().removeUpPointer(webID.getId(), upPointer.webID.getId());
        }
    }

    /**
	 * Adds the given node as a neighbor, bypassing the database.  This is called when loading from the database.
	 *
	 * @pre n != null, n exists in the HyPeerWeb, n is not an element of neighbors
	 * @post neighbors is one larger and contains n, all other nodes that were in neighbors are still in neighbors
	 *
	 * @return A boolean indicating whether the add was successful.
	 * @param n The node to be added as a neighbor.
	 *
	 * @author Ken DeCelle
	 */
    public boolean addNeighborPointer(Node n) {
        return neighbors.add(n);
    }

    /**
	 * Adds the given node as an upPointer, bypassing the database.  This is called when loading from the database.
	 *
	 * @pre n != null, n exists in the HyPeerWeb, n is not an element of upPointers
	 * @post upPointers is one larger and contains n, all other nodes that were in upPointers are still in upPointers
	 *
	 * @return A boolean indicating whether the add was successful.
	 * @param n The node to be added as an upPointer.
	 *
	 * @author Ken DeCelle
	 */
    public boolean addUpPointerPointer(Node n) {
        return upPointers.add(n);
    }

    /**
	 * Adds the given node as a downPointer, bypassing the database.  This is called when loading from the database.
	 *
	 * @pre n != null, n exists in the HyPeerWeb, n is not an element of downPointers
	 * @post downPointers is one larger and contains n, all other nodes that were in downPointers are still in downPointers
	 *
	 * @return A boolean indicating whether the add was successful.
	 * @param n The node to be added as a downPointer.
	 *
	 * @author Ken DeCelle
	 */
    public boolean addDownPointerPointer(Node n) {
        return downPointers.add(n);
    }

    /**
	 * Adds the given node as a fold, bypassing the database.  This is called when loading from the database.
	 *
	 * @pre n exists in the HyPeerWeb unless n is null
	 * @post fold = n
	 *
	 * @param n The node to be added as a fold.
	 *
	 * @author Ken DeCelle
	 */
    public void addFoldPointer(Node n) {
        if (n != null) {
            fold = n;
        } else {
            fold = NULL_NODE;
        }
    }

    /**
	 * Adds the given node as a surrogateFold, bypassing the database.  This is called when loading from the database.
	 *
	 * @pre n exists in the HyPeerWeb unless n is null
	 * @post surrogateFold = n
	 *
	 * @param n The node to be added as a surrogateFold.
	 *
	 * @author Ken DeCelle
	 */
    public void addSurrogateFoldPointer(Node n) {
        if (n != null) {
            surrogateFold = n;
        } else {
            surrogateFold = NULL_NODE;
        }
    }

    /**
	 * Adds newNode to the HyPeerWeb.
	 *
	 * @pre n != null, n is not an element of the HyPeerWeb
	 * @post newNode is an element of the HyPeerWeb, the HyPeerWeb size has increased by one, all conditions hold as 
	 * outline in the conceptual model
	 *
	 * @param newNode The node to be added to the HyPeerWeb
	 *
	 * @author Scott Cornaby
	 */
    public void addToHyPeerWeb(Node newNode) {
        state.getInsertionDeletionPoints(this).getInsertionPoint().addChild(newNode);
    }

    /**
	 * Gets the node from the HyPeerWeb.
	 *
	 * @pre None
	 * @post Returns a reference to the node.
	 * 
	 * @return A reference to the node that has the given destinationId or NULL_NODE if the node does not exist.
	 * @param destinationId The webID of the node to be returned.
	 *
	 * @author Kevin Murdock
	 */
    public Node getNode(WebId destinationId) {
        Node currNode = this;
        Set<Node> currNeighbors;
        while (destinationId.getId() != currNode.getWebId().getId()) {
            currNeighbors = currNode.neighbors;
            int distance = idDistance(currNode.getWebId(), destinationId);
            int neighborDistance = 0;
            boolean idFound = false;
            for (Node n : currNeighbors) {
                neighborDistance = idDistance(n.getWebId(), destinationId);
                if (neighborDistance < distance) {
                    currNode = n;
                    idFound = true;
                    distance = neighborDistance;
                }
            }
            if (!idFound) {
                return NULL_NODE;
            }
        }
        return currNode;
    }

    /**
	 * Gets the distance between the nodes with the given webIDs.
	 *
	 * @pre There is a node that exists that has the given webId for the first webId and the second webId.
	 * @post None
	 * 
	 * @return An int that represents the distance between the nodes.
	 * @param id1 The webID of the first node.
	 * @param id2 The webID of the second node.
	 *
	 * @author Kevin Murdock
	 */
    public int idDistance(WebId id1, WebId id2) {
        int maxHeight = 0;
        if (id1.height() > id2.height()) {
            maxHeight = id1.height();
        } else {
            maxHeight = id2.height();
        }
        int intId1 = id1.getId();
        int intId2 = id2.getId();
        int difference = 0;
        for (int i = 0; i < maxHeight; i++) {
            if ((intId1 & 1) != (intId2 & 1)) {
                difference++;
            }
            intId1 >>>= 1;
            intId2 >>>= 1;
        }
        return difference;
    }

    /**
	 * Gets whether the node has a neighbor with a webID larger than it's webID.
	 *
	 * @pre neighbors != null
	 * @post neighbors still has all the same nodes in it that it did before the method call
	 * 
	 * @return A boolean indicating whether this node has a neighbor larger than webID.
	 *
	 * @author 
	 */
    public boolean hasLargerNeighbor() {
        for (Node n : neighbors) {
            if (n.getWebId().getId() > webID.getId()) {
                return true;
            }
        }
        return false;
    }

    /**
	 * 2-5-2011
	 * Inserts a Node
	 *
	 * @pre This node is the insertion point.
	 * @post n is a child of this with the associated relations as outlined in the conceptual model.
	 *
	 * @param n Child node to be inserted.
	 *
	 * @author Robby Canady
	 */
    public void addChild(Node n) {
        int childId = (int) (webID.getId() + Math.pow(2, height));
        n.setWebId(new WebId(childId));
        HyPeerWeb.getHyPeerWeb().addNode(n);
        for (Node up : upPointers) {
            n.addNeighbor(up);
            up.addNeighbor(n);
            up.removeDownPointer(this);
        }
        upPointers.clear();
        if (webID.getId() == fold.webID.getId()) {
            n.setFold(fold);
            fold.setFold(n);
        } else if (!fold.equals(NULL_NODE) && surrogateFold.equals(NULL_NODE)) {
            surrogateFold = fold;
            n.setFold(fold);
            fold.setSurrogateFold(fold.getFold());
            fold.setFold(n);
            fold.state.changeState(fold);
            fold = NULL_NODE;
        } else if (!fold.equals(NULL_NODE) && !surrogateFold.equals(NULL_NODE)) {
            n.setFold(surrogateFold);
            surrogateFold.setFold(n);
            surrogateFold.setSurrogateFold(NULL_NODE);
            surrogateFold = NULL_NODE;
        }
        height++;
        n.setHeight(height);
        HyPeerWebDatabase.getHyPeerWebDatabase().setHeight(n.getWebId().getId(), n.getHeight());
        HyPeerWebDatabase.getHyPeerWebDatabase().setHeight(webID.getId(), height);
        addNeighbor(n);
        n.addNeighbor(this);
        for (Node neighbor : neighbors) {
            if (neighbor.webID.getId() > this.webID.getId() && neighbor.webID.getId() != n.webID.getId()) {
                n.addDownPointer(neighbor);
                neighbor.addUpPointer(n);
            }
        }
        for (Node tempNode : neighbors) {
            state.changeState(tempNode);
        }
        for (Node tempNode : n.neighbors) {
            state.changeState(tempNode);
        }
        state.changeState(n);
        state.changeState(this);
    }

    /**
	 * Disconnects this node from the HyPeerWeb and makes all the necessary adjustments.
	 * 
	 * @pre This node is the last element of the HyPeerWeb.
	 * @post This node is not an element of the HyPeerWeb.  All of it's previous connections have been 
	 * reconnected to follow the conditions outlined in the conceptual model.
	 *
	 * @author Robby Canady
	 */
    public void disconnect() {
        for (Node n : downPointers) {
            n.removeUpPointer(this);
            n.state.changeState(n);
        }
        Node parent = getParent();
        parent.setHeight(parent.getHeight() - 1);
        HyPeerWebDatabase.getHyPeerWebDatabase().setHeight(parent.getWebId().getId(), parent.getHeight());
        if (!fold.equals(NULL_NODE)) {
            if (parent.equals(NULL_NODE)) {
                fold.setFold(NULL_NODE);
                fold.setSurrogateFold(NULL_NODE);
            } else if (fold.equals(parent)) {
                parent.setFold(parent);
                parent.setSurrogateFold(NULL_NODE);
            } else if (parent.fold.equals(NULL_NODE)) {
                fold.setFold(parent);
                parent.setFold(fold);
                parent.setSurrogateFold(NULL_NODE);
                fold.setSurrogateFold(NULL_NODE);
                fold.state.changeState(fold);
            } else if (!parent.fold.equals(NULL_NODE)) {
                fold.setFold(NULL_NODE);
                fold.setSurrogateFold(parent);
                parent.setSurrogateFold(fold);
            }
        }
        for (Node n : neighbors) {
            n.neighbors.remove(this);
            if (n != parent) {
                n.addDownPointer(parent);
                parent.addUpPointer(n);
            }
            n.state.changeState(n);
        }
        parent.state.changeState(parent);
        HyPeerWeb.getHyPeerWeb().removeNode(this);
    }

    /**
	 * Swaps this Node with the parameter Node.
	 *
	 * @pre This and toSwap != null, this and toSwap are in the HyPeerWeb.
	 * @post This has the webID and all of the connections of toSwap and vice versa.  All of the
	 * connections follow the conditions as outlined in the conceptual model.
	 *
	 * @author Karen Downs
	 */
    public void swapNode(Node toSwap) {
        WebId tempWebID = new WebId(webID.getId());
        Set<Node> tempNeighbors = new HashSet<Node>();
        for (Node n : neighbors) {
            tempNeighbors.add(getCorrectNode(n, toSwap));
            if (n != toSwap && n != this && !n.neighbors.contains(toSwap)) {
                n.removeNeighbor(this);
                n.addNeighbor(toSwap);
            }
        }
        Set<Node> tempUpPointers = new HashSet<Node>();
        for (Node n : upPointers) {
            tempUpPointers.add(getCorrectNode(n, toSwap));
            if (n != toSwap && n != this && !n.downPointers.contains(toSwap)) {
                n.removeDownPointer(this);
                n.addDownPointer(toSwap);
            }
        }
        Set<Node> tempDownPointers = new HashSet<Node>();
        for (Node n : downPointers) {
            tempDownPointers.add(getCorrectNode(n, toSwap));
            if (n != toSwap && n != this && !n.upPointers.contains(toSwap)) {
                n.removeUpPointer(this);
                n.addUpPointer(toSwap);
            }
        }
        Node tempFold = getCorrectNode(fold, toSwap);
        Node tempSurrogateFold = getCorrectNode(surrogateFold, toSwap);
        int tempHeight = height;
        NodeState tempState = state;
        webID = new WebId(toSwap.webID.getId());
        neighbors = new HashSet<Node>();
        for (Node n : toSwap.neighbors) {
            neighbors.add(getCorrectNode(n, toSwap));
            if (n != toSwap && n != this && !n.neighbors.contains(this)) {
                n.removeNeighbor(toSwap);
                n.addNeighbor(this);
            }
        }
        downPointers = new HashSet<Node>();
        for (Node n : toSwap.downPointers) {
            downPointers.add(getCorrectNode(n, toSwap));
            if (n != toSwap && n != this && !n.upPointers.contains(this)) {
                n.removeUpPointer(toSwap);
                n.addUpPointer(this);
            }
        }
        if (this.fold.webID.getId() != -1) {
            fold.fold = toSwap;
        }
        if (this.surrogateFold.webID.getId() != -1) {
            surrogateFold.surrogateFold = toSwap;
        }
        if (toSwap.fold.webID.getId() != -1) {
            toSwap.fold.fold = this;
        }
        if (toSwap.surrogateFold.webID.getId() != -1) {
            toSwap.surrogateFold.surrogateFold = this;
        }
        fold = getCorrectNode(toSwap.fold, toSwap);
        surrogateFold = getCorrectNode(toSwap.surrogateFold, toSwap);
        height = toSwap.height;
        state = toSwap.state;
        toSwap.webID = tempWebID;
        toSwap.neighbors = tempNeighbors;
        toSwap.upPointers = tempUpPointers;
        toSwap.downPointers = tempDownPointers;
        toSwap.fold = tempFold;
        toSwap.surrogateFold = tempSurrogateFold;
        toSwap.height = tempHeight;
        toSwap.state = tempState;
    }

    private Node getCorrectNode(Node toBeAssigned, Node toBeSwapped) {
        if (toBeAssigned == toBeSwapped) {
            return this;
        }
        if (toBeAssigned == this) {
            return toBeSwapped;
        }
        return toBeAssigned;
    }

    /**
	 * The accept method for the visitor Pattern.
	 * 
	 * @param visitor
	 * @param parameters
	 * 
	 * @pre visitor != null AND parameters != null AND visitor.visit(this, parameters).pre-condition
	 * @post visitor.visit(this, parameters).post-condition
	 */
    public void accept(Visitor visitor, Parameters parameters) {
        visitor.visit(this, parameters);
    }

    /**
	 * Get's this node's contents.
	 * 
	 * @pre None
	 * @post result = contents
	 */
    public Contents getContents() {
        return content;
    }

    /**
	 * Clears this node's contents.
	 * 
	 * @pre None
	 * @post content is empty
	 */
    public void clearContents() {
        content = new Contents();
    }

    /**
	 * 2-5-2011
	 * Calculates the height of the node
	 *
	 * @pre None
	 * @post None
	 * 
	 * @return The height of the node.
	 *
	 * @author Robby Canady
	 */
    public int getHeightFromWebId() {
        int webHeight = 0;
        int currentNumber = webID.getId();
        while ((currentNumber / 2) != 0) {
            webHeight++;
            currentNumber /= 2;
        }
        return webHeight;
    }

    /**
	 * Compares a WebId by the id.
	 *
	 * @pre o != null
	 * @post None
	 *
	 * @param o The Object to compare to.
	 * @return A boolean indicating whether webID and o.webID are equal.
	 *
	 * @author Robby Canady
	 */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Node) {
            return webID.getId() == ((Node) o).getWebId().getId();
        } else {
            return false;
        }
    }

    /**
	 * Prints the Node's information.
	 *
	 * @pre neighbors, upPointers, and downPointers are != null
	 * @post None
	 *
	 * @return A string containing the fold and surrogateFold, all neighbors, upPointers, and downPointer.
	 *
	 * @author Karen Downs
	 */
    @Override
    public String toString() {
        return "Node: " + getWebId().getId();
    }

    @Override
    public int hashCode() {
        if (ourHash == 0) {
            ourHash = 47 * 3 + (this.webID != null ? this.webID.hashCode() : 0);
        }
        return ourHash;
    }

    /**
	 * 2-19-2011
	 * Deletes a node from the hyPeerWeb by swapping it with the last node in the hyperweb and then deleting it
	 * 
	 * 
	 * 
	 * @author Scott Cornaby and Robby Canady
	 */
    public void removeFromHyPeerWeb(Node n) {
        if (n == null) throw new IllegalArgumentException();
        if (!HyPeerWeb.getHyPeerWeb().contains(n)) throw new IllegalArgumentException();
        Node deletePoint = n.state.getInsertionDeletionPoints(n).getDeletionPoint();
        if (n != deletePoint) {
            n.swapNode(deletePoint);
        }
        n.disconnect();
    }

    /**
	 * 2-19-2011
	 * Retrieves this node's child node
	 *
	 * @author Scott Cornaby
	 */
    public Node getChild() {
        int childId = (int) (webID.getId() + Math.pow(2, height - 1));
        for (Node n : neighbors) {
            if (n.getWebId().getId() == childId) {
                return n;
            }
        }
        return Node.NULL_NODE;
    }

    /**
	 * 2-19-2011
	 * Retrieves this node's parent node
	 *
	 * @author Robby Canady, Scott Cornaby
	 */
    public Node getParent() {
        int parentId = (int) (webID.getId() - Math.pow(2, height - 1));
        for (Node n : neighbors) {
            if (n.getWebId().getId() == parentId) {
                return n;
            }
        }
        return Node.NULL_NODE;
    }
}
