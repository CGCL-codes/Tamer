package org.apache.axis2.jaxws.unitTest.echo;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import javax.xml.ws.Holder;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0-b26-ea3
 * Generated source version: 2.0
 * 
 */
@WebService(name = "EchoPort", targetNamespace = "http://ws.apache.org/axis2/tests", wsdlLocation = "\\work\\apps\\eclipse\\workspace\\axis2-live\\modules\\jaxws\\test-resources\\wsdl\\WSDLTests.wsdl")
public interface EchoPort {

    /**
     * 
     * @param text
     */
    @WebMethod(operationName = "Echo", action = "http://ws.apache.org/axis2/tests/echo")
    @RequestWrapper(localName = "Echo", targetNamespace = "http://ws.apache.org/axis2/tests", className = "org.apache.ws.axis2.tests.Echo")
    @ResponseWrapper(localName = "EchoResponse", targetNamespace = "http://ws.apache.org/axis2/tests", className = "org.apache.ws.axis2.tests.EchoResponse")
    public void echo(@WebParam(name = "text", targetNamespace = "", mode = Mode.INOUT) Holder<String> text);
}
