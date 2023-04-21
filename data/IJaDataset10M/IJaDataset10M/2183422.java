package org.apache.cxf.demo.complex;

import agentwsresponse.agent.legacysystemservices.schemas.hitum.esb.company.AgentWSResponse;
import com.company.application.CompanyESBApplicationBiztalkAgentDetails4405AgentDetailsPrtSoap;
import com.company.application.GetAgentDetails;
import com.company.application.GetAgentDetailsResponse;

/**
 * This is a trivial implementation of a service contributed in a bug report.  It's useful
 * as an example of a hard case of using dynamic client.
 */
public class ComplexImpl implements CompanyESBApplicationBiztalkAgentDetails4405AgentDetailsPrtSoap {

    /** {@inheritDoc}*/
    public GetAgentDetailsResponse getAgentDetails(GetAgentDetails parameters) {
        GetAgentDetailsResponse r = new GetAgentDetailsResponse();
        AgentWSResponse awr = new AgentWSResponse();
        int number = parameters.getPart().getAgentNumber();
        awr.setAgenceNumber(number);
        awr.setAgentName("Orange");
        r.setAgentWSResponse(awr);
        return r;
    }
}
