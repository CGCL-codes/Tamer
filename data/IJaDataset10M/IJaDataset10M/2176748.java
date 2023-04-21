package edu.asu.vogon.quadriga.ids;

import edu.asu.vogon.digitalHPS.IElement;
import edu.asu.vogon.digitalHPS.IRelationEvent;

public class RelationEventIdRemover extends AIdRemove {

    public RelationEventIdRemover(IDRemover manager) {
        super(manager);
    }

    @Override
    public void processSubelements(IElement element) {
        if (element == null) return;
        IRelationEvent event = (IRelationEvent) element;
        remover.removeIds(event.getRelation());
        remover.removeIds(event.getRelationCreator());
        remover.removeIds(event.getSourceReference());
    }
}
