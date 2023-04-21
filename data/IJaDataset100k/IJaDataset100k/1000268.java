package edu.asu.quadriga.quadruple.list;

import java.util.List;
import edu.asu.quadriga.interfaces.elements.IElement;
import edu.asu.quadriga.interfaces.elements.ITermPart;
import edu.asu.quadriga.interfaces.elements.IVocabularyEntry;
import edu.asu.quadriga.manage.HibernateConnector;

public class TermPartLister extends AElementLister {

    public TermPartLister(HibernateConnector connector, ListCoordinator coordinator) {
        super(connector, coordinator);
    }

    @Override
    protected void listInternal(IElement element, List<IElement> list) {
        IVocabularyEntry part = (IVocabularyEntry) connector.loadKeepAlive(((ITermPart) element).getNormalization());
        coordinator.listElements(part, list);
    }
}
