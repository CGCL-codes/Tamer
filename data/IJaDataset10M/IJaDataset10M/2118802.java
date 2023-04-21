package com.buschmais.maexo.samples.ds.mbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of the Sample MBean interface.
 */
public class Sample implements SampleMBean {

    /**
	 * The logger instance.
	 */
    private static final Logger logger = LoggerFactory.getLogger(Sample.class);

    /**
	 * The constructor.
	 */
    public Sample() {
        logger.info("created sample MBean instance");
    }

    /**
	 * The attribute.
	 */
    private String attribute;

    /**
	 * {@inheritDoc}
	 */
    public final String getAttribute() {
        return this.attribute;
    }

    /**
	 * {@inheritDoc}
	 */
    public final void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
	 * {@inheritDoc}
	 */
    public final int operation(int a, int b) {
        return a + b;
    }
}
