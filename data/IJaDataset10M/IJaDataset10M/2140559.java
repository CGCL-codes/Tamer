package org.apache.axis2.jaxws.rpclit.stringarray;

import org.test.rpclit.stringarray.StringArray;
import javax.jws.WebService;

@WebService(serviceName = "RPCLitStringArrayService", endpointInterface = "org.apache.axis2.jaxws.rpclit.stringarray.sei.Echo")
public class EchoImpl {

    public String echoString(String arg0) {
        return arg0;
    }

    public StringArray echoStringArray(StringArray arg0) {
        if (arg0 == null) {
            System.out.println("received null parameter");
        } else {
            System.out.println("received input parameter stringArray =" + arg0.toString());
        }
        return arg0;
    }
}
