package com.nokia.ats4.appmodel.model.domain.impl;

import com.nokia.ats4.appmodel.model.domain.SystemResponse;
import com.nokia.ats4.appmodel.model.domain.ResponseVerificationCommand;

/**
 * A class that implements a container for {@link com.nokia.ats4.appmodel.model.domain.ResponseVerificationCommand commands}
 * that verify system response.
 *
 * @author Timo Sillanp&auml;&auml;
 * @version $Revision: 2 $
 */
public class SystemResponseImpl extends CommandListImpl<ResponseVerificationCommand> implements SystemResponse {

    /**
     * Creates a new instance of SystemResponseImpl
     */
    public SystemResponseImpl() {
    }
}
