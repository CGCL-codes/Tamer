package org.jactr.core.runtime;

import java.util.ArrayList;
import java.util.Collection;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author developer
 *
 */
public class ProductionFiringSequenceTest extends TestCase {

    /**
   logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(ProductionFiringSequenceTest.class);

    private final Collection<String> _productionFiringSequence = new ArrayList<String>();

    protected void setProductionFiringSequence(Collection<String> sequence) {
        _productionFiringSequence.addAll(sequence);
    }

    protected Collection<String> getProductionFiringSequence() {
        return new ArrayList<String>(_productionFiringSequence);
    }

    /** 
   * @see junit.framework.TestCase#setUp()
   */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /** 
   * @see junit.framework.TestCase#tearDown()
   */
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
