package edu.upc.lsi.kemlg.aws.knowledge.predicate;

import java.rmi.RemoteException;
import edu.upc.lsi.kemlg.aws.knowledge.ontology.Contract;
import edu.upc.lsi.kemlg.aws.knowledge.ontology.OntologyConcept;

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
