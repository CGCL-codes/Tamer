package org.isurf.dataintegrator;

import java.util.ArrayList;
import java.util.List;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import _1.wsdl.epcis.epcglobal.EPCISServicePortType;
import _1.wsdl.epcis.epcglobal.EPCglobalEPCISService;
import _1.wsdl.epcis.epcglobal.ImplementationExceptionResponse;
import _1.wsdl.epcis.epcglobal.NoSuchNameExceptionResponse;
import _1.wsdl.epcis.epcglobal.QueryParameterExceptionResponse;
import _1.wsdl.epcis.epcglobal.QueryTooComplexExceptionResponse;
import _1.wsdl.epcis.epcglobal.QueryTooLargeExceptionResponse;
import _1.wsdl.epcis.epcglobal.SecurityExceptionResponse;
import _1.wsdl.epcis.epcglobal.ValidationExceptionResponse;
import epcglobal.epcis_query.xsd._1.EmptyParms;
import epcglobal.epcis_query.xsd._1.Poll;
import epcglobal.epcis_query.xsd._1.QueryResults;

public class WSCall {

    EPCISServicePortType port = new EPCglobalEPCISService().getEPCglobalEPCISServicePort();

    EmptyParms parms = new EmptyParms();

    BindingProvider bp = (BindingProvider) port;

    public WSCall() {
        List<Handler> handlerChain = new ArrayList<Handler>();
        handlerChain.add(new MyHandler());
        bp.getBinding().setHandlerChain(handlerChain);
    }

    public QueryResults pollAll(Poll poll) throws ImplementationExceptionResponse, NoSuchNameExceptionResponse, QueryParameterExceptionResponse, QueryTooComplexExceptionResponse, QueryTooLargeExceptionResponse, SecurityExceptionResponse, ValidationExceptionResponse {
        return port.poll(poll);
    }

    public void setURL(String endpointURL) {
        bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);
    }
}
