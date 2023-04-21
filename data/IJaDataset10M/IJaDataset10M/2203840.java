package com.scbeta.net.xiep.eventArgs;

import com.scbeta.net.xiep.packages.RequestPackage;
import com.scbeta.net.xiep.packages.ResponsePackage;
import java.util.EventObject;

/**
 *
 * @author aaa
 */
public class AfterSendResponseArgs extends EventObject {

    private ClientConnectionInfoArgs clientConnectionInfoArgs;

    private RequestPackage requestPackage;

    private ResponsePackage responsePackage;

    public ClientConnectionInfoArgs getClientConnectionInfoArgs() {
        return clientConnectionInfoArgs;
    }

    public RequestPackage getRequestPackage() {
        return requestPackage;
    }

    public ResponsePackage getResponsePackage() {
        return responsePackage;
    }

    public AfterSendResponseArgs(Object source, ClientConnectionInfoArgs clientConnectionInfoArgs, RequestPackage requestPackage, ResponsePackage responsePackage) {
        super(source);
        this.clientConnectionInfoArgs = clientConnectionInfoArgs;
        this.requestPackage = requestPackage;
        this.responsePackage = responsePackage;
    }
}
