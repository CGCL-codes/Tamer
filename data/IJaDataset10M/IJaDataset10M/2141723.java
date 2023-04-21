package org.exolab.jms.lease;

/**
 * A lease is used to track message exipration. It contains the object to be
 * leased, the duration and the object to callback if or when the lease expirex
 *
 * @author <a href="mailto:jima@exoffice.com">Jim Alateras</a>
 * @version $Revision: 1.2 $ $Date: 2005/03/18 03:47:30 $
 */
public interface LeaseIfc {

    /**
     * Return the absolute expiry time of this lease.
     *
     * @return the expiry time of the lease, in milliseconds
     */
    public long getExpiryTime();

    /**
     * Returns the duration of the lease.
     *
     * @return the duration of the lease, in milliseconds
     */
    public long getDuration();

    /**
     * Returns the time remaining on the lease.
     *
     * @return the time remaining, in milliseconds
     */
    public long getRemainingTime();

    /**
     * Returns the leased object.
     *
     * @return the leased object
     */
    public Object getLeasedObject();
}
