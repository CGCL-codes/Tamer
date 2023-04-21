package net.sf.mmm.util.io.base;

import net.sf.mmm.util.io.api.spi.DetectorStreamProcessorFactory;

/**
 * This is the abstract base implementation of the
 * {@link DetectorStreamProcessorFactory} interface.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.1.0
 */
public abstract class AbstractDetectorStreamProcessorFactory implements DetectorStreamProcessorFactory {

    /**
   * The constructor.
   */
    public AbstractDetectorStreamProcessorFactory() {
        super();
    }

    /**
   * {@inheritDoc}
   */
    public int getLookaheadCount() {
        return 1024;
    }
}
