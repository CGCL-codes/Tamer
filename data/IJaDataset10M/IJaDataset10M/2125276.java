package net.kano.joscar.ratelim;

import net.kano.joscar.snac.ClientSnacProcessor;
import net.kano.joscar.snaccmd.conn.RateClassInfo;

/**
 * An interface for listening for rate-related events on a {@link RateMonitor}.
 */
public interface RateListener {

    /**
     * Called when the given rate monitor receives a new set of rate class
     * information. This callback's invocation indicates that all previously
     * held <code>RateClassMonitor</code>s in the given <code>RateMonitor</code>
     * have been discarded and a new set has been generated.
     *
     * @param monitor the rate monitor that received new rate class information
     */
    void gotRateClasses(RateMonitor monitor);

    /**
     * Called when a single rate class's information has been updated.
     *
     * @param rateMonitor the rate monitor whose rate class was updated
     * @param rateClassMonitor the rate class monitor for the associated
     *        rate class
     * @param rateClassInfo the new rate class information
     */
    void rateClassUpdated(RateMonitor rateMonitor, RateClassMonitor rateClassMonitor, RateClassInfo rateClassInfo);

    /**
     * Called when a single rate class becomes or stops becoming "rate-limited."
     *
     * @param rateMonitor the rate monitor whose rate class's limited status
     *        changed
     * @param rateClassMonitor the rate class monitor for the associated rate
     *        class
     * @param limited whether or not the rate class is limited
     */
    void rateClassLimited(RateMonitor rateMonitor, RateClassMonitor rateClassMonitor, boolean limited);

    /**
     * Called when the given rate monitor was "detached" from the given SNAC
     * processor. See {@link RateMonitor#detach()} for details; in brief, this
     * callback's invocation indicates that no further work should be done
     * involving the given rate monitor, and it is recommended that listeners
     * remove themselves as listeners upon a call to this method.
     *
     * @param rateMonitor the rate monitor that was detached
     * @param processor the SNAC processor from which the given rate monitor was
     *        detached
     */
    void detached(RateMonitor rateMonitor, ClientSnacProcessor processor);

    /**
     * Called when the rate information for the given rate monitor has been
     * reset. See {@link RateMonitor#reset()} for details.
     *
     * @param rateMonitor the rate monitor whose rate information was reset
     */
    void reset(RateMonitor rateMonitor);
}
