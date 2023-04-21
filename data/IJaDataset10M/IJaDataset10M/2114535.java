package org.nodal.storage;

import org.nodal.model.Node;
import org.nodal.util.Name;
import org.nodal.util.User;
import org.nodal.model.Txn;
import org.nodal.model.TxnOp;

/**
 * <p>The main interface to a means of storage for NODAL objects.</p>
 * 
 * <p>This is intended to be a framework for implementation of the NODAL persistence mechanisms.  It provides and interface that represents a uri-addressable data store and database manager (transactional).</p>
 * @author Lee Iverson <leei@ece.ubc.ca> 
 */
public interface Storage {

    /**
   * <p>Create a new Storage interface that represents a logged-in data store.</p>
   * 
   * <p>The initial creation of a Storage object represents an anonymous access.  In order to obtain privileged access to objects on a store, it is necessary to login().  This method performs authentication on the named user with the provided key and, if successful, returns a new Storage object that represents that privileged access.</p> 
   */
    Storage login(String user, String pw);

    String uri();

    User user();

    /**
   * <p>Find a Node with the given nid.</p>
   * 
   * <p>This is the primary interface for extracting nodes from the Storage interface.  Given the nid String, the interface will return the Node that has that nid.</p>
   * 
   * <p>The depth parameter represents an access optimization that follows a Node's content tree down depth levels.  A depth of 0 suggests that we are not even requesting a Node's content.  A positive depth value requests that we iterate over a Node's properties and for every property with a non-null Node value, we will retrieve that referenced Node with a depth of depth-1.  This is clearly a recursive process.</p>
   * 
   * <p>A negative depth value implies indefinite recursion.  Since the retrieval process is sensitive to internal references (i.e. multiply-referenced Nodes are only retrieved once), this process will terminate, however care is required.</p> 
   */
    StNode getNode(String nid, int depth);

    boolean addTxnOp(TxnOp op);

    Txn pendingTxn(int seqno);

    int commit();
}
