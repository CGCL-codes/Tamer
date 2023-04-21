package rice.past.messaging;

import rice.*;
import rice.past.*;
import rice.pastry.NodeId;
import rice.pastry.messaging.Message;
import rice.pastry.security.Credentials;
import java.util.Random;
import java.io.*;

/**
 * @(#) MessageInsert.java
 *
 * PASTMessage requesting a file be inserted on the local node.
 *
 * @version $Id: MessageInsert.java,v 1.1.1.1 2003/06/17 21:10:39 egs Exp $
 * @author Charles Reis
 */
public class MessageInsert extends PASTMessage {

    /**
   * The file to insert.
   */
    protected Serializable _file;

    /**
   * The credentials of the author of the file.
   */
    protected Credentials _cred;

    /**
   * Whether the insert was successful (on a response).
   */
    protected boolean _success = false;

    /**
   * Builds a new request to insert a file.
   * @param nodeId Source Pastry node's ID
   * @param fileId Pastry key of file
   * @param update File to be stored
   * @param authorCred Credentials of the author of the file
   */
    public MessageInsert(NodeId nodeId, NodeId fileId, Serializable file, Credentials authorCred) {
        super(nodeId, fileId);
        _file = file;
        _cred = authorCred;
    }

    /**
   * Returns whether the insert was successful.
   */
    public boolean getSuccess() {
        return _success;
    }

    /**
   * Inserts this message's file into the service.
   * @param service PASTService on which to act
   */
    public void performAction(final PASTServiceImpl service) {
        debug("  Inserting file " + getFileId() + " at node " + service.getPastryNode().getNodeId());
        final Continuation insert = new Continuation() {

            public void receiveResult(Object o) {
                _success = ((Boolean) o).booleanValue();
                setType(RESPONSE);
                service.sendMessage(MessageInsert.this);
            }

            public void receiveException(Exception e) {
                System.out.println("Exception " + e + " occurred during an insert!");
            }
        };
        Continuation check = new Continuation() {

            public void receiveResult(Object o) {
                if (!((Boolean) o).booleanValue()) {
                    service.getStorage().store(getFileId(), _file, insert);
                } else {
                    _success = false;
                    setType(RESPONSE);
                    service.sendMessage(MessageInsert.this);
                }
            }

            public void receiveException(Exception e) {
                System.out.println("Exception " + e + " occurred during an insert!");
            }
        };
        service.getStorage().exists(getFileId(), check);
    }

    /**
   * Display this message.
   */
    public String toString() {
        String val = "INSERT ";
        if (getType() == REQUEST) {
            val += "Request: ";
        } else {
            val += "Response: ";
        }
        return val + getFileId() + ": " + _file;
    }
}
