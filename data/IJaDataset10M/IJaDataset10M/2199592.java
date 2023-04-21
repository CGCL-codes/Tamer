package org.eucontract.agents.predicates;

import java.rmi.RemoteException;
import org.eucontract.agents.domain.Contract;
import org.eucontract.agents.domain.OntologyConcept;

public class ContractActivated extends OntologyConcept {

    protected Contract ct;

    public ContractActivated() {
        this.ct = new Contract();
    }

    public ContractActivated(Contract ct) {
        this.ct = ct;
    }

    @Override
    public String getXml() throws RemoteException {
        return "<contract-activated>" + ct.getXml() + "</contract-activated>";
    }

    public Contract getContract() {
        return ct;
    }
}
