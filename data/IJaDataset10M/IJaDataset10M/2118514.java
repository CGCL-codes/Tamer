package org.tockit.cgs.model.events;

import org.tockit.cgs.model.KnowledgeBase;
import org.tockit.cgs.model.Relation;

public class NewRelationCreatedEvent extends KnowledgeBaseChangeEvent {

    private Relation relation;

    public NewRelationCreatedEvent(KnowledgeBase subject, Relation relation) {
        super(subject);
        this.relation = relation;
    }

    public Relation getRelation() {
        return relation;
    }
}
