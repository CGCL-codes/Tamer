package engine.distribution.master;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import engine.Population;

/**
 * Represents population divided into parts that are intended to be distributed
 * among slaves.
 * @param <T> Type of individual in the population.
 * @author Karol Stosiek (karol.stosiek@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 */
public class DistributedPopulation<T> implements Iterable<Map.Entry<String, Population<T>>> {

    /** Generated serial version UID. */
    private static final long serialVersionUID = 2974766480204792399L;

    /** Divided population to distribute among slaves. */
    private final Map<String, Population<T>> dividedPopulation;

    /**
   * Constructor creating a mapping from slave to population
   * <strong>with state</strong>.
   * @param initialDividedPopulation Mapping from slaves to populations,
   * representing work load for each slave.
   */
    public DistributedPopulation(final Map<String, Population<T>> initialDividedPopulation) {
        this.dividedPopulation = initialDividedPopulation;
    }

    /**
   * Returns part of the population that assigned to a given slave.
   * @param slaveId of the slave that asks for population.
   * @return Part of the population that has to be evaluated by the slave
   * that is asking.
   */
    public Population<T> getPopulation(String slaveId) {
        return dividedPopulation.containsKey(slaveId) ? dividedPopulation.get(slaveId) : null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof DistributedPopulation)) {
            return false;
        }
        DistributedPopulation<T> that = castToDistributedPopulation(object);
        return this.dividedPopulation.equals(that.dividedPopulation);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return dividedPopulation.hashCode();
    }

    /**
   * Casts given object to a DistributedPopulation object. Will throw
   * ClassCastException if the given object is not an instance
   * of DistributedPopulation.
   * 
   * @param object Object to be casted to DistributedPopulation.
   * @return DistributedPopulation object that is the result of a cast.
   * Never null.
   */
    @SuppressWarnings("unchecked")
    private DistributedPopulation<T> castToDistributedPopulation(Object object) {
        DistributedPopulation<T> distributedPopulation = (DistributedPopulation<T>) object;
        for (Entry<String, Population<T>> entry : distributedPopulation) {
            for (@SuppressWarnings("unused") final T individual : entry.getValue().getIndividuals()) {
            }
        }
        return distributedPopulation;
    }

    /** {@inheritDoc} */
    public Iterator<Map.Entry<String, Population<T>>> iterator() {
        return this.dividedPopulation.entrySet().iterator();
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return dividedPopulation.toString();
    }
}
