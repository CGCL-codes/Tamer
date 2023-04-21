package net.sf.istcontract.acs.behaviour;

import net.sf.istcontract.acs.protocol.QueryContractProtocol;

public class QueryContractBehaviour extends CommonContractStoreBehaviour {

    public String getProtocolName() {
        return QueryContractProtocol.class.getName();
    }
}
