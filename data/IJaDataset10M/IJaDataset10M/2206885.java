package edu.upc.lsi.kemlg.aws.communication.performative;

public class ConsentSuggestion extends Performative {

    public ConsentSuggestion(String idDialog, String inReplyTo, String sender, String receiver) {
        this.idDialog = idDialog;
        this.inReplyTo = inReplyTo;
        this.sender = sender;
        this.receiver = receiver;
    }
}
