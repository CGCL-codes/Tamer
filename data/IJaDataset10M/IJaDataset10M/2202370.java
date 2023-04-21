package org.slasoi.infrastructure.servicemanager.registry;

/**
 * Generic interface to implement a Registry such as Images, Users ,etc.
 * 
 * @author Patrick Cheevers
 * 
 * @param <T>
 * @param <F>
 * 
 */
public interface IMetricRegistry<T, F> {

    String getMetricID(final float memory, final int cores, final float speed);
}
