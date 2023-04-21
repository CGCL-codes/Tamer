package org.brainypdm.modules.customgraph.exception;

import org.brainypdm.exceptions.BaseException;
import org.brainypdm.main.msg.MessageCode;
import org.brainypdm.modules.exceptions.ModuleException;

/***
 * 
 * a custom graph exception
 * 
 * @author <a href="mailto:nico@brainypdm.org">Nico Bagari</a>
 *
 */
public class CustomGraphExcepion extends ModuleException {

    public CustomGraphExcepion(BaseException bex) {
        super(bex);
    }

    public CustomGraphExcepion(MessageCode code, String param1, String param2, String param3) {
        super(code, param1, param2, param3);
    }

    public CustomGraphExcepion(MessageCode code, String param1, String param2) {
        super(code, param1, param2);
    }

    public CustomGraphExcepion(MessageCode aCode, String aParam) {
        super(aCode, aParam);
    }

    public CustomGraphExcepion(MessageCode code, String[] params) {
        super(code, params);
    }

    public CustomGraphExcepion(MessageCode aCode, Throwable exception) {
        super(aCode, exception);
    }

    public CustomGraphExcepion(MessageCode aCode) {
        super(aCode);
    }
}
