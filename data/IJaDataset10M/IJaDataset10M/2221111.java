package com.iparelan.util;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import net.jcip.annotations.Immutable;
import com.iparelan.util.annotations.AutoBoxing;
import com.iparelan.util.annotations.Copyright;
import com.iparelan.util.annotations.idioms.HashCode;

/**
 * A combination of the {@link java.util.concurrent.Delayed} and {@link
 * java.lang.Runnable} interfaces with implementations for {@link
 * java.util.concurrent.Delayed#getDelay(TimeUnit)} and {@link
 * java.lang.Comparable#compareTo(Object)
 * Comparable&lt;Delayed>.compareTo(Delayed)}.
 *
 * @author Greg Mattes
 * @version August 2008
 *
 * @see java.util.concurrent.DelayQueue
 */
@Immutable
@Copyright("Copyright &copy; 2008, Iparelan Solutions, LLC. All rights reserved.")
public abstract class DelayedRunnable implements Delayed, Runnable {

    /**
     * The time (since the epoch) at which this {@code DelayedRunnable} will
     * be executed.
     *
     * @see java.lang.System#nanoTime()
     */
    private final long nanoExpiration;

    /**
     * Creates a {@code DelayedRunnable}.
     *
     * @param delay
     *
     *        The amount of time, expressed in seconds, after the creation
     *        time of this object when it should be {@link
     *        java.lang.Runnable#run() run}.
     */
    public DelayedRunnable(final long delay) {
        this(delay, SECONDS);
    }

    /**
     * Creates a {@code DelayedRunnable}.
     *
     * @param delay
     *
     *        The amount of time, expressed in units of {@code unit}, after
     *        the creation time of this object when it should be {@link
     *        java.lang.Runnable#run() run}.
     *
     * @param unit
     *
     *        The time unit of {@code delay}. May not be {@code null}.
     */
    public DelayedRunnable(final long delay, final TimeUnit unit) {
        final long nanoNow = System.nanoTime();
        final long nanoDelay = NANOSECONDS.convert(delay, unit);
        this.nanoExpiration = (nanoNow + nanoDelay);
    }

    @Override
    public final long getDelay(final TimeUnit unit) {
        final long nanoNow = System.nanoTime();
        final long nanoDelay = (nanoExpiration - nanoNow);
        final long delay = unit.convert(nanoDelay, NANOSECONDS);
        return delay;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Ordering is based solely on expiration time. This means that two
     * distinct objects with the same expiration time are considered to be
     * equal. There is no "tie-breaker" like a unique serial number.
     *
     * @param other {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    @AutoBoxing
    public final int compareTo(final Delayed other) {
        final Long otherDelay = other.getDelay(NANOSECONDS);
        final Long thisDelay = this.getDelay(NANOSECONDS);
        return thisDelay.compareTo(otherDelay);
    }

    @Override
    @HashCode
    public final int hashCode() {
        int result = HashCodeConstants.INITIALIZER;
        final int expirationHash = (int) (nanoExpiration ^ (nanoExpiration >>> (Long.SIZE / 2)));
        result = (HashCodeConstants.MULTIPLIER * result) + expirationHash;
        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Equality is based solely on expiration time. This means that two
     * distinct objects with the same expiration time are considered to be
     * equal. There is no "tie-breaker" like a unique serial number.
     *
     * @param object {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public final boolean equals(final Object object) {
        final boolean isEqual;
        if (object instanceof DelayedRunnable) {
            final Delayed other = Delayed.class.cast(object);
            isEqual = (compareTo(other) == 0);
        } else {
            isEqual = false;
        }
        return isEqual;
    }
}
