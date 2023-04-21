package org.brainypdm.modules.web.exceptions;

import org.brainypdm.exceptions.BaseException;
import org.brainypdm.main.msg.MessageCode;
import org.brainypdm.modules.exceptions.ModuleException;

/***
 * 
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 *
 */
public class WebModuleException extends ModuleException {

    private static final long serialVersionUID = -2025068607720302886L;

    public WebModuleException(BaseException bex) {
        super(bex);
    }

    public WebModuleException(MessageCode code, String param1, String param2, String param3) {
        super(code, param1, param2, param3);
    }

    public WebModuleException(MessageCode code, String param1, String param2) {
        super(code, param1, param2);
    }

    public WebModuleException(MessageCode code, String param) {
        super(code, param);
    }

    public WebModuleException(MessageCode code, String[] params) {
        super(code, params);
    }

    public WebModuleException(MessageCode code, Throwable exception) {
        super(code, exception);
    }

    public WebModuleException(MessageCode code) {
        super(code);
    }
}
