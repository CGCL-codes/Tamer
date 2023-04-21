package org.apache.axis2.jaxws.samples.client.ping;

import org.apache.axis2.jaxws.samples.ping.PingStringInput;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;

@WebService(name = "PingServicePortType", targetNamespace = "http://org/apache/axis2/jaxws/samples/ping/")
@SOAPBinding(parameterStyle = ParameterStyle.BARE)
public interface PingServicePortTypeClient {

    /**
     * @param parameter
     */
    @WebMethod(action = "pingOperation")
    @Oneway
    public void pingOperation(@WebParam(name = "pingStringInput", targetNamespace = "http://org/apache/axis2/jaxws/samples/ping/", partName = "parameter") PingStringInput parameter);
}
