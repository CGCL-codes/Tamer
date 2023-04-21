package edu.upc.lsi.kemlg.aws.knowledge.ontology;

import java.rmi.RemoteException;
import edu.upc.lsi.kemlg.aws.knowledge.ontology.OntologyConcept;
import edu.upc.lsi.kemlg.aws.utils.MessageIDGenerator;

public class Contract extends OntologyConcept {

    protected ContractTemplate ct;

    protected String stIdentifier;

    protected String stContents;

    public Contract() {
        this.ct = new ContractTemplate();
        this.stIdentifier = "default";
    }

    public Contract(ContractTemplate ct) {
        this.ct = ct;
        this.stIdentifier = MessageIDGenerator.getMessageID();
    }

    public Contract(String stContents) {
        this.stContents = stContents;
    }

    @Override
    public String getXml() throws RemoteException {
        return stContents;
    }

    public String getIdentifier() {
        return stIdentifier;
    }
}
