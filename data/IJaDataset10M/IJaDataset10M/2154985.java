package com.ail.core.configure.server;

import com.ail.core.BaseError;
import com.ail.core.BaseException;

/**
 * This error is thrown in the event of a system error during the processing of a car (configuration archive) file.
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/09/03 18:07:56 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/CarProcessingError.java,v $
 */
public class CarProcessingError extends BaseError {

    public CarProcessingError(String description) {
        super(description);
    }

    public CarProcessingError(String description, Throwable target) {
        super(description, target);
    }

    public CarProcessingError(BaseException e) {
        super(e);
    }
}
