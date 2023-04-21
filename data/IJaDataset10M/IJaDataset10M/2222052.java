package org.processmining.mining.fuzzymining.metrics.binary;

import org.processmining.mining.fuzzymining.metrics.MetricsRepository;

/**
 * @author christian
 * 
 */
public abstract class BinaryDerivateMetric extends BinaryMetric {

    protected MetricsRepository repository;

    /**
	 * @param aName
	 * @param aDescription
	 * @param aSize
	 */
    public BinaryDerivateMetric(String aName, String aDescription, MetricsRepository aRepository) {
        super(aName, aDescription, aRepository.getNumberOfLogEvents());
        repository = aRepository;
    }

    public abstract void measure();
}
