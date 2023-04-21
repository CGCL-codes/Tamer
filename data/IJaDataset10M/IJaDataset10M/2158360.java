package edu.upc.lsi.kemlg.aws.communication.performative;

import edu.upc.lsi.kemlg.aws.knowledge.ontology.OntologyConcept;

public class Confirm extends Performative {

    public Confirm(String idDialog, String stInReplyTo, String sender, String receiver, OntologyConcept content) {
        this.idDialog = idDialog;
        this.inReplyTo = stInReplyTo;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
