package com.xebia.jarep.jamon.expose.service;

import java.util.List;
import com.xebia.jarep.CounterDatum;

/**
 * Service that provides access to the Jamon counters the service runs on.
 */
public interface JamonDataExposerService {

    /**
   * Returns all Jamon counters registered in the VM the service runs on.
   * 
   * @param prefix
   *          the prefix to add to the counter names
   * @param reset
   *          if true the jamon counters will be reset. If false nothing happens
   *          to the counters
   * 
   * @return list all CounterDatum objects containing the Jamon counters of the
   *         VM the service runs on
   */
    public List<CounterDatum> getJamonCounters(String prefix, boolean reset);
}
