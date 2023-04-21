package org.signserver.server.genericws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.signserver.common.IllegalRequestException;
import org.signserver.common.SignServerException;

public interface IDummyWS {

    @WebMethod
    String test(@WebParam(name = "param1") String param1) throws IllegalRequestException, SignServerException;
}
