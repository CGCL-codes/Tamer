package eu.popeye.middleware.dataSharing;

import java.io.Serializable;
import org.jdom.Document;

public class SharedSpaceEvent implements Serializable {

    public static final int DIRECTORY_ADDED = 0;

    public static final int DIRECTORY_REMOVED = 1;

    public static final int DATA_ADDED = 2;

    public static final int DATA_REMOVED = 3;

    public int type;

    public String path;

    public String peerID;

    public Document newTreeStructure;

    public SharedSpaceEvent(int type, String path, String peerID, Document newTreeStructure) {
        this.type = type;
        this.path = path;
        this.peerID = peerID;
        this.newTreeStructure = newTreeStructure;
    }

    public String toString() {
        return "type = " + this.type + "\npath =" + this.path + "\npeerID = " + this.peerID;
    }
}
