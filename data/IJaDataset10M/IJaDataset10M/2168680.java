package edu.asu.quadriga.quadruple.list;

import java.util.List;
import edu.asu.quadriga.interfaces.elements.IElement;
import edu.asu.quadriga.manage.HibernateConnector;

public class VocabularyEntryLister extends AElementLister {

    public VocabularyEntryLister(HibernateConnector connector, ListCoordinator coordinator) {
        super(connector, coordinator);
    }

    @Override
    protected void listInternal(IElement element, List<IElement> list) {
    }
}
