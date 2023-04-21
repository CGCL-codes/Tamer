package org.ldaptive.pool;

import org.ldaptive.AbstractConfig;

/**
 * Contains all the configuration data that the pooling implementations need to
 * control the pool.
 *
 * @author  Middleware Services
 * @version  $Revision: 2352 $ $Date: 2012-04-11 10:00:33 -0400 (Wed, 11 Apr 2012) $
 */
public class PoolConfig extends AbstractConfig {

    /** Default min pool size, value is {@value}. */
    public static final int DEFAULT_MIN_POOL_SIZE = 3;

    /** Default max pool size, value is {@value}. */
    public static final int DEFAULT_MAX_POOL_SIZE = 10;

    /** Default validate on check in, value is {@value}. */
    public static final boolean DEFAULT_VALIDATE_ON_CHECKIN = false;

    /** Default validate on check out, value is {@value}. */
    public static final boolean DEFAULT_VALIDATE_ON_CHECKOUT = false;

    /** Default validate periodically, value is {@value}. */
    public static final boolean DEFAULT_VALIDATE_PERIODICALLY = false;

    /** Default validate period, value is {@value}. */
    public static final long DEFAULT_VALIDATE_PERIOD = 1800;

    /** Default prune period, value is {@value}. */
    public static final long DEFAULT_PRUNE_PERIOD = 300;

    /** Default expiration time, value is {@value}. */
    public static final long DEFAULT_EXPIRATION_TIME = 600;

    /** Min pool size. */
    private int minPoolSize = DEFAULT_MIN_POOL_SIZE;

    /** Max pool size. */
    private int maxPoolSize = DEFAULT_MAX_POOL_SIZE;

    /** Whether the ldap object should be validated when returned to the pool. */
    private boolean validateOnCheckIn = DEFAULT_VALIDATE_ON_CHECKIN;

    /** Whether the ldap object should be validated when given from the pool. */
    private boolean validateOnCheckOut = DEFAULT_VALIDATE_ON_CHECKOUT;

    /** Whether the pool should be validated periodically. */
    private boolean validatePeriodically = DEFAULT_VALIDATE_PERIODICALLY;

    /** Time in seconds that the validate pool should repeat. */
    private long validatePeriod = DEFAULT_VALIDATE_PERIOD;

    /** Time in seconds that the prune pool should repeat. */
    private long prunePeriod = DEFAULT_PRUNE_PERIOD;

    /** Time in seconds that ldap objects should be considered expired. */
    private long expirationTime = DEFAULT_EXPIRATION_TIME;

    /**
   * Returns the min pool size. Default value is {@link #DEFAULT_MIN_POOL_SIZE}.
   * This value represents the size of the pool after a prune has occurred.
   *
   * @return  min pool size
   */
    public int getMinPoolSize() {
        return minPoolSize;
    }

    /**
   * Returns the max pool size. Default value is {@link #DEFAULT_MAX_POOL_SIZE}.
   * This value may or may not be strictly enforced depending on the pooling
   * implementation.
   *
   * @return  max pool size
   */
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    /**
   * Returns the validate on check in flag. Default value is {@link
   * #DEFAULT_VALIDATE_ON_CHECKIN}.
   *
   * @return  validate on check in
   */
    public boolean isValidateOnCheckIn() {
        return validateOnCheckIn;
    }

    /**
   * Returns the validate on check out flag. Default value is {@link
   * #DEFAULT_VALIDATE_ON_CHECKOUT}.
   *
   * @return  validate on check in
   */
    public boolean isValidateOnCheckOut() {
        return validateOnCheckOut;
    }

    /**
   * Returns the validate periodically flag. Default value is {@link
   * #DEFAULT_VALIDATE_PERIODICALLY}.
   *
   * @return  validate periodically
   */
    public boolean isValidatePeriodically() {
        return validatePeriodically;
    }

    /**
   * Returns the prune period. Default value is {@link #DEFAULT_PRUNE_PERIOD}.
   *
   * @return  prune period in seconds
   */
    public long getPrunePeriod() {
        return prunePeriod;
    }

    /**
   * Returns the validate period. Default value is {@link
   * #DEFAULT_VALIDATE_PERIOD}.
   *
   * @return  validate period in seconds
   */
    public long getValidatePeriod() {
        return validatePeriod;
    }

    /**
   * Returns the expiration time. Default value is {@link
   * #DEFAULT_EXPIRATION_TIME}. The expiration time represents the max time a
   * connection should be available before it is considered stale. This value
   * does not apply to connections in the pool if the pool has only the minimum
   * number of connections available.
   *
   * @return  expiration time in seconds
   */
    public long getExpirationTime() {
        return expirationTime;
    }

    /**
   * Sets the min pool size.
   *
   * @param  size  min pool size
   */
    public void setMinPoolSize(final int size) {
        checkImmutable();
        if (size >= 0) {
            logger.trace("setting minPoolSize: {}", size);
            minPoolSize = size;
        }
    }

    /**
   * Sets the max pool size.
   *
   * @param  size  max pool size
   */
    public void setMaxPoolSize(final int size) {
        checkImmutable();
        if (size >= 0) {
            logger.trace("setting maxPoolSize: {}", size);
            maxPoolSize = size;
        }
    }

    /**
   * Sets the validate on check in flag.
   *
   * @param  b  validate on check in
   */
    public void setValidateOnCheckIn(final boolean b) {
        checkImmutable();
        logger.trace("setting validateOnCheckIn: {}", b);
        validateOnCheckIn = b;
    }

    /**
   * Sets the validate on check out flag.
   *
   * @param  b  validate on check out
   */
    public void setValidateOnCheckOut(final boolean b) {
        checkImmutable();
        logger.trace("setting validateOnCheckOut: {}", b);
        validateOnCheckOut = b;
    }

    /**
   * Sets the validate periodically flag.
   *
   * @param  b  validate periodically
   */
    public void setValidatePeriodically(final boolean b) {
        checkImmutable();
        logger.trace("setting validatePeriodically: {}", b);
        validatePeriodically = b;
    }

    /**
   * Sets the period for which the pool will be pruned.
   *
   * @param  time  in seconds
   */
    public void setPrunePeriod(final long time) {
        checkImmutable();
        if (time >= 0) {
            logger.trace("setting prunePeriod: {}", time);
            prunePeriod = time;
        }
    }

    /**
   * Sets the period for which the pool will be validated.
   *
   * @param  time  in seconds
   */
    public void setValidatePeriod(final long time) {
        checkImmutable();
        if (time >= 0) {
            logger.trace("setting validatePeriod: {}", time);
            validatePeriod = time;
        }
    }

    /**
   * Sets the time that an ldap object should be considered stale and ready for
   * removal from the pool.
   *
   * @param  time  in seconds
   */
    public void setExpirationTime(final long time) {
        checkImmutable();
        if (time >= 0) {
            logger.trace("setting expirationTime: {}", time);
            expirationTime = time;
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("[%s@%d::minPoolSize=%s, maxPoolSize=%s, validateOnCheckIn=%s, " + "validateOnCheckOut=%s, validatePeriodically=%s, validatePeriod=%s, " + "prunePeriod=%s, expirationTime=%s]", getClass().getName(), hashCode(), minPoolSize, maxPoolSize, validateOnCheckIn, validateOnCheckOut, validatePeriodically, validatePeriod, prunePeriod, expirationTime);
    }
}
