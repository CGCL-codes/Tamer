package org.signserver.server.validators;

import org.signserver.common.ProcessableConfig;
import org.signserver.common.ValidatorStatus;
import org.signserver.common.WorkerStatus;
import org.signserver.server.BaseProcessable;

/**
 * Base class that all (document) validators can extend to cover basic in common
 * functionality.
 * 
 * @author Markus Kilås
 * @version $Id: BaseValidator.java 1875 2011-09-29 14:50:48Z netmackan $
 */
public abstract class BaseValidator extends BaseProcessable implements IValidator {

    /**
     * @see org.signserver.server.IProcessable#getStatus()
     */
    @Override
    public WorkerStatus getStatus() {
        return new ValidatorStatus(workerId, new ProcessableConfig(config));
    }
}
