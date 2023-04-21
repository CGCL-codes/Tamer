package org.juddi.error;

import org.juddi.datatype.response.Result;

/**
 * Thrown to indicate that a UDDI Exception was encountered.
 *
 * @author Steve Viens (sviens@users.sourceforge.net)
 */
public class SecretUnknownException extends RegistryException {

    public SecretUnknownException(String msg) {
        super(msg);
        String errMsg = Result.E_SECRET_UNKNOWN_MSG;
        Result result = new Result(Result.E_SECRET_UNKNOWN, Result.E_SECRET_UNKNOWN_CODE, errMsg);
        this.setFaultActor("");
        this.setFaultCode("Client");
        this.setFaultString("Client Error");
        this.addResult(result);
    }
}
