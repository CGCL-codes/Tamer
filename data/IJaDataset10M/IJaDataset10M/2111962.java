package it.webscience.kpeople.domain.service.interf;

import java.util.List;
import java.util.Set;
import it.webscience.kpeople.domain.model.KpeopleAction;
import it.webscience.kpeople.domain.model.KpeopleEvent;

public interface ActionStateManager {

    public String addNewState(String name, String description);

    public String addNewAction(String systemId, String actionType, String actionReference);

    public KpeopleEvent addNewEvent(String xMLevent, String numRetries, String lastRetry, KpeopleAction fKEventAction);

    public Set<KpeopleAction> getActionByState(long id);

    public Boolean updateActionState(KpeopleAction action, Long idState);

    public List<KpeopleAction> getActionByState(long id, String systemId);
}
