package org.gridbus.broker.jobdescription.commands;

import org.gridbus.broker.constants.CopyType;
import org.gridbus.broker.constants.LocationType;
import org.gridbus.broker.constants.TaskCommandType;
import org.gridbus.broker.jobdescription.TaskCommand;

/**
 * Represents the copy command of a run file.
 * @author Hussein Gibbins (hag@cs.mu.oz.au)
 */
public class CopyCommand extends TaskCommand {

    private String source = null;

    private String destination = null;

    private int sourceLocation;

    private int destinationLocation;

    /**
	 * Constructs a CopyCommand and sets its command type.
	 */
    public CopyCommand() {
        super();
    }

    /**
	 * 
	 * @param srcLocation (One of the LocationType constants)
	 * @param source
	 */
    public void setSource(int srcLocation, String source) {
        this.sourceLocation = srcLocation;
        this.source = source;
    }

    /**
	 * Gets the source location for this copy command.
	 * 
	 * @return source location.
	 */
    public String getSource() {
        return source;
    }

    /**
	 * @deprecated
	 * @return
	 */
    public boolean isSourceRemote() {
        return (sourceLocation == LocationType.NODE);
    }

    /**
	 * 
	 * @param destLocation (One of the LocationType constants)
	 * @param destination
	 */
    public void setDestination(int destLocation, String destination) {
        this.destinationLocation = destLocation;
        this.destination = destination;
    }

    /**
	 * Gets the destination location for this copy command.
	 * 
	 * @return destination location.
	 */
    public String getDestination() {
        return destination;
    }

    /**
	 * Checks whether the destination location is the compute node.
	 * @deprecated
	 * @return whether the destination location is compute node.
	 */
    public boolean isDestinationRemote() {
        return (destinationLocation == LocationType.NODE);
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CopyCommand :");
        sb.append(" src: ").append(source);
        sb.append(", srcLocation: ").append(LocationType.stringValue(sourceLocation));
        sb.append(", dest: ").append(destination);
        sb.append(", destLocation: ").append(LocationType.stringValue(destinationLocation));
        return sb.toString();
    }

    /**
	 * @return Returns the destinationLocation.
	 */
    public int getDestinationLocation() {
        return destinationLocation;
    }

    /**
	 * @return Returns the sourceLocation.
	 */
    public int getSourceLocation() {
        return sourceLocation;
    }

    /**
	 * Gets the type of copy command.
	 * @return an int which is one of the constants defined in the org.gridbus.broker.constants.CopyType class
	 * @see org.gridbus.broker.constants.CopyType
	 */
    public int getCopyType() {
        int type = CopyType.Unsupported;
        switch(sourceLocation) {
            case LocationType.LOCAL:
                if (destinationLocation == LocationType.NODE) {
                    type = CopyType.Broker_to_ComputeNode;
                }
                break;
            case LocationType.NODE:
                if (destinationLocation == LocationType.LOCAL) {
                    type = CopyType.ComputeNode_to_Broker;
                } else if (destinationLocation == LocationType.REMOTE) {
                    type = CopyType.ComputeNode_to_RemoteDataNode;
                }
                break;
            case LocationType.REMOTE:
                if (destinationLocation == LocationType.NODE) {
                    type = CopyType.RemoteDataNode_to_ComputeNode;
                }
        }
        return type;
    }

    public int getType() {
        return TaskCommandType.COPY_CMD;
    }

    public Object clone() {
        CopyCommand cc = new CopyCommand();
        String dupSrc = new String(this.getSource());
        String dupDest = new String(this.getDestination());
        cc.setDestination(this.getDestinationLocation(), dupDest);
        cc.setSource(this.getSourceLocation(), dupSrc);
        return cc;
    }

    /**
	 * If a url starts with a protocol which is one of:
	 * http:, https:, ftp:, gsiftp:, srb: 
	 * it is considered REMOTE.
	 * 
	 * If no protocol is found or the protocol is not in the above list then, this method returns false.
	 * @return
	 */
    public static boolean isLocationRemote(String url) {
        boolean isRemote = false;
        if (url != null && url.length() != 0) {
            if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp:") || url.startsWith("gsiftp:") || url.startsWith("srb:")) {
                isRemote = true;
            }
        }
        return isRemote;
    }
}
