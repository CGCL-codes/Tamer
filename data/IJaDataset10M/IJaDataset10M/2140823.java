package com.teletalk.jserver.tcp;

import java.util.ArrayList;
import java.util.List;
import com.teletalk.jserver.property.VectorPropertyItem;

/**
 * This class is used to group a collection of endpoints which are associated with the same remote address. Instances of this class 
 * is used by TcpCommunicationManager.
 * 
 * @see TcpCommunicationManager
 *  
 * @author Tobias L�fstrand
 * 
 * @since Beta 2
 */
public class TcpEndPointGroup implements VectorPropertyItem {

    private final ArrayList endPointSlots;

    /** The address this TcpEndPointGroup represents. */
    protected final TcpEndPointIdentifier address;

    /** Current index used when getting endpoints according to the round robin algorithm (using the method {@link #getEndPoint()}. */
    protected int currentIndex;

    /** Represents the number of endpoints currently contained in this group. */
    protected int size = 0;

    /** The server side address this TcpEndPointGroup was "accepted" on, i.e. the address the client used to connect to 
    * the server. This field is only used for server side endpoint groups. @since 2.0.1 (20041111) */
    protected TcpEndPointIdentifier acceptAddress = null;

    /** 
	 * Flag indicating if this endpoint group is client side (<code>true</code>) or server side (<code>false</code>).
	 * 
	 * @since 1.2 
	 */
    protected boolean clientSide;

    /**
	 * Creates a new TcpEndPointGroup for the specified address.
	 * 
	 * @param address the address of this group.
	 */
    public TcpEndPointGroup(TcpEndPointIdentifier address) {
        this.address = address;
        endPointSlots = new ArrayList();
        currentIndex = 0;
    }

    /**
    * Returns a boolean indicating if there is an error in this endpoint group (possibly an error connecting endpoints). This method is provided 
    * for subclasses that wish to abort waiting for a link to be established.<br>
    * <br>
    * This implementation always returns false.
    * 
    * @since 2.1.2 (20060207)
    */
    protected boolean isError() {
        return false;
    }

    /**
    * Checks if this endpoint group has at least one endpoint that has a working connection (although is may not yet have established a link, i.e finished handshaking).
    * 
    * @return <code>true</code> if at least one endpoint has a working connection, otherwise <code>false</code>. 
    * 
    * @since 2.1.2 (20060224)
    */
    public boolean isConnected() {
        TcpEndPoint endPoint;
        synchronized (this) {
            for (int i = 0; i < endPointSlots.size(); i++) {
                endPoint = (TcpEndPoint) endPointSlots.get(i);
                if ((endPoint != null) && (endPoint.isConnected())) return true;
            }
        }
        return false;
    }

    /**
    * Checks if this endpoint group has at least one endpoint that has an established link.
    * 
    * @return <code>true</code> if at least one endpoint has an established link, otherwise <code>false</code>. 
    * 
    * @since 2.0.1
    */
    public boolean isLinkEstablished() {
        TcpEndPoint endPoint;
        synchronized (this) {
            for (int i = 0; i < endPointSlots.size(); i++) {
                endPoint = (TcpEndPoint) endPointSlots.get(i);
                if ((endPoint != null) && (endPoint.isLinkEstablished())) return true;
            }
        }
        return false;
    }

    /**
    * Blocks the calling thread until this endpoint group has at least one endpoint that has an established link, or until an error occurs.
    * 
    * @return <code>true</code> if at least one endpoint has an established link, otherwise <code>false</code>. 
    * 
    * @since 2.0.1
    * 
    * @exception InterruptedException if the calling thread was interrupted.
    * 
    * @see #isError()
    */
    public boolean waitForLinkEstablished() throws InterruptedException {
        while (!this.isLinkEstablished() && !this.isError()) {
            synchronized (this) {
                this.wait(10000);
            }
        }
        return this.isLinkEstablished();
    }

    /**
    * Blocks the calling thread until this endpoint group has at least one endpoint that has an established link, or until an error occurs, but a maximum of <code>timeout</code> milliseconds.
    * 
    * @param timeout maximum time in milliseconds to wait for an endpoint to establish link.
    * 
    * @return <code>true</code> if at least one endpoint has an established link, otherwise <code>false</code>. 
    * 
    * @since 2.0.1
    * 
    * @exception InterruptedException if the calling thread was interrupted.
    * 
    * @see #isError()
    */
    public boolean waitForLinkEstablished(final long timeout) throws InterruptedException {
        long beginWait = System.currentTimeMillis();
        long waitTime;
        synchronized (this) {
            while (!this.isLinkEstablished() && !this.isError()) {
                waitTime = timeout - (System.currentTimeMillis() - beginWait);
                if (waitTime > 0) {
                    this.wait(waitTime);
                } else break;
            }
        }
        return this.isLinkEstablished();
    }

    /**
	 * Gets the address of this group.
	 * 
	 * @return a TcpEndPointIdentifier object.
	 */
    public TcpEndPointIdentifier getAddress() {
        return address;
    }

    /**
    * Gets the address of the server socket on which the sockets of this group were accepted.
    * 
    * @since 2.0.1 (20041111).
    */
    public TcpEndPointIdentifier getAcceptAddress() {
        if (!this.clientSide) {
            TcpEndPoint endPoint;
            synchronized (this) {
                for (int i = 0; i < endPointSlots.size(); i++) {
                    endPoint = (TcpEndPoint) endPointSlots.get(i);
                    if (endPoint != null) return endPoint.getAcceptAddess();
                }
            }
        }
        return null;
    }

    /**
	 * Gets the number of endpoints in this group.
	 * 
	 * @return the number of endpoints in this group.
	 */
    public final synchronized int size() {
        return size;
    }

    /**
    * Checks if this is a client side (true) or server side (false) group.
	 */
    public boolean isClientSide() {
        return this.clientSide;
    }

    /**
    * Sets the flag indicating if this is a client side (true) or server side (false) group.
	 */
    public void setClientSide(boolean clientSide) {
        this.clientSide = clientSide;
    }

    /**
	 * Adds an endpoint to this group.
	 * 
	 * @param endPoint the endpoint to add.
	 * 
	 * @return the index (sequence number) in this TcpEndPointGroup where the endpoint was added.
	 */
    public synchronized int addEndPoint(final TcpEndPoint endPoint) {
        if (endPoint == null) return -1;
        endPoint.setEndPointGroup(this);
        int firstFreeCellIndex = findFirstFreeCell();
        if (firstFreeCellIndex >= 0) {
            endPointSlots.set(firstFreeCellIndex, endPoint);
            size++;
            return firstFreeCellIndex + 1;
        } else {
            endPointSlots.add(endPoint);
            size++;
            return endPointSlots.size();
        }
    }

    /**
	 * Finds the first free endpoint slot.
	 */
    private int findFirstFreeCell() {
        for (int i = 0; i < endPointSlots.size(); i++) {
            if (endPointSlots.get(i) == null) return i;
        }
        return -1;
    }

    /**
	 * Removes an endpoint from the group.
	 * 
	 * @param endPoint the endpoint to remove.
	 * 
	 * @return <code>true</code> if the specified endpoint was found in this group, otherwise <code>false</code>.
	 */
    public synchronized boolean removeEndPoint(final TcpEndPoint endPoint) {
        if (endPoint == null) return false;
        int index = endPointSlots.indexOf(endPoint);
        if (index >= 0) {
            if (index == (endPointSlots.size() - 1)) {
                endPointSlots.remove(endPoint);
            } else {
                endPointSlots.set(index, null);
            }
            if (size > 0) size--;
            return true;
        } else return false;
    }

    /**
	 * Gets the next endpoint in this group. This implementation gets the next endpoint according to 
	 * the amazing round robin algorithm.
	 * 
	 * @return the next endpoint.
	 */
    public synchronized TcpEndPoint getEndPoint() {
        if (endPointSlots.size() > 0) {
            if (currentIndex >= endPointSlots.size()) currentIndex = 0;
            TcpEndPoint ep = null;
            while ((ep == null) && (currentIndex < endPointSlots.size())) {
                ep = (TcpEndPoint) endPointSlots.get(currentIndex);
                currentIndex++;
            }
            if (currentIndex >= endPointSlots.size()) currentIndex = 0;
            return ep;
        } else return null;
    }

    /**
	 * Gets all the endpoints in this group.
	 * 
	 * @return a list of TcpEndPoint objects.
	 */
    public List getEndPoints() {
        ArrayList endPointsInGroup = new ArrayList();
        TcpEndPoint endPoint;
        synchronized (this) {
            for (int i = 0; i < endPointSlots.size(); i++) {
                endPoint = (TcpEndPoint) endPointSlots.get(i);
                if (endPoint != null) endPointsInGroup.add(endPoint);
            }
        }
        return endPointsInGroup;
    }

    /**
	 * Gets the key uniquely identifying this TcpEndPointGroup.
	 * 
	 * @return the key uniquely identifying this TcpEndPointGroup.
	 */
    public String getKey() {
        return this.address.getAddressAsString();
    }

    /**
	 * Gets a String describing this TcpEndPointGroup.
	 * 
	 * @return a String describing this TcpEndPointGroup.
	 */
    public String getDescription() {
        return this.getDescription(false);
    }

    /**
    * Gets a String describing this TcpEndPointGroup.
    * 
    * @return a String describing this TcpEndPointGroup.
    */
    protected String getDescription(boolean includeAddress) {
        String addressAsString = "";
        if (includeAddress) addressAsString = (this.address != null) ? this.address.getAddressAsString() : "<n/a>";
        if (this.clientSide) return "Client side group " + addressAsString + " - " + this.size() + " endpoints"; else return "Server side group " + addressAsString + " - " + this.size() + " endpoints";
    }

    /**
	 * Gets a String describing this TcpEndPointGroup.
	 * 
	 * @return a String describing this TcpEndPointGroup.
	 */
    public String toString() {
        return this.getDescription(true);
    }

    /**
	 * Test this object for equality with another object.
	 * 
	 * @return true if the object specified by parameter o is a TcpEndPointGroup and represents the same address as this object, otherwise false.
	 * 
	 * @since 1.2
	 */
    public boolean equals(final Object o) {
        if (o instanceof TcpEndPointGroup) {
            return this.address.equals(((TcpEndPointGroup) o).address);
        } else return false;
    }

    /**
    * Called when an endpoint gets connected.<br>
    * <br>
    * <b><i>Note:</i></b> If this method is overridden by a subclass, it is imperative that the super class implementation is called.  
    * 
    * @param endPoint the object representing the endpoint.
    */
    protected void endPointConnected(final TcpEndPoint endPoint) {
    }

    /**
    * Called when an endpoint establishes a link.<br>
    * <br>
    * <b><i>Note:</i></b> If this method is overridden by a subclass, it is imperative that the super class implementation is called.
    * 
    * @param endPoint the object representing the endpoint.
    */
    protected void endPointLinkEstablished(final TcpEndPoint endPoint) {
        synchronized (this) {
            this.notifyAll();
        }
    }

    /**
    * Called when an endpoint gets disconnected.<br>
    * <br>
    * <b><i>Note:</i></b> If this method is overridden by a subclass, it is imperative that the super class implementation is called.
    * 
    * @param endPoint the object representing the endpoint.
    */
    protected void endPointDisconnected(final TcpEndPoint endPoint) {
    }
}
