package org.ldaptive.pool;

/**
 * Thrown when a pool thread is unexpectedly interrupted while blocking.
 *
 * @author  Middleware Services
 * @version  $Revision: 2290 $ $Date: 2012-02-27 17:13:40 -0500 (Mon, 27 Feb 2012) $
 */
public class PoolInterruptedException extends PoolException {

    /** serialVersionUID. */
    private static final long serialVersionUID = -1427225156311025280L;

    /**
   * Creates a new pool interrupted exception.
   *
   * @param  msg  describing this exception
   */
    public PoolInterruptedException(final String msg) {
        super(msg);
    }

    /**
   * Creates a new pool interrupted exception.
   *
   * @param  e  pooling specific exception
   */
    public PoolInterruptedException(final Exception e) {
        super(e);
    }

    /**
   * Creates a new pool interrupted exception.
   *
   * @param  msg  describing this exception
   * @param  e  pooling specific exception
   */
    public PoolInterruptedException(final String msg, final Exception e) {
        super(msg, e);
    }
}
