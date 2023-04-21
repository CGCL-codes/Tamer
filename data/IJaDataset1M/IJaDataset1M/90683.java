package br.pucrio.inf.les.teccomm.oc;

import java.util.Map;

/**
 * <p>Interface State</p>
 *
 *
 * @author
 * @version %I%, %G%
 *
 * @invariant $none
 */
public interface State {

    /**
     * <p>Method getAffectedPins</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    int[] getAffectedPins();

    /**
     * <p>Method getDevice</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    AtomicDevice getDevice();

    /**
     * <p>Method getGlobalTime</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    long getGlobalTime();

    /**
     * <p>Method getLocalTime</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    long getLocalTime();

    /**
     * <p>Method getOldValues</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    Map getOldValues();

    /**
     * <p>Method getOutputs</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    Map getOutputs();

    /**
     * <p>Method getValues</p>
     *
     * @pre $none
     *
     * @post $none
     *
     * @concurrency concurrent
     *
     *
     * @return
     *
     */
    Map getValues();
}
