package org.aspectbrains.contractj.internal.paramprecondition.eclipse;

import org.aspectbrains.util.log.ILog;
import org.aspectbrains.util.log.LogFactory;

/**
 * Constants for this bundle.
 * 
 * @author Heiko Seeberger
 */
public interface IBundleConstants {

    /**
     * The symbolic name of this bundle:
     * "org.aspectbrains.contractj.paramprecondition.eclipse".
     */
    static final String BUNDLE_SYMBOLIC_NAME = "org.aspectbrains.contractj.paramprecondition.eclipse";

    /**
     * The {@link ILog} for this bundle.
     */
    static final ILog LOG = LogFactory.createLog(BUNDLE_SYMBOLIC_NAME);
}
