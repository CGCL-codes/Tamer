package org.modelibra.action;

import java.util.ArrayList;
import java.util.List;
import org.modelibra.DomainModel;
import org.modelibra.Entities;
import org.modelibra.IEntities;
import org.modelibra.ModelSession;
import org.modelibra.exception.ActionRuntimeException;

/**
 * Encapsulates a transaction in a session. A transaction keeps a history of
 * actions on entities. A transaction may be executed or undone. To be
 * successful, all transaction actions must be successful. Domain model
 * observers are informed about the execute or undo events.
 * 
 * @author Dzenan Ridjanovic
 * @version 2009-02-09
 */
public class Transaction extends Action {

    private ModelSession session;

    private History actions = new History();

    /**
	 * Constructs a transaction within a session.
	 * 
	 * @param session
	 *            session
	 */
    public Transaction(ModelSession session) {
        super();
        if (session != null) {
            this.session = session;
        } else {
            String error = "Transaction session is null.";
            throw new ActionRuntimeException(error);
        }
    }

    void add(Action action) {
        actions.add(action);
    }

    /**
	 * Gets a session.
	 * 
	 * @return session
	 */
    public ModelSession getSession() {
        return session;
    }

    /**
	 * Gets the transaction history.
	 * 
	 * @return transaction history of actions
	 */
    public IHistory getHistory() {
        return actions;
    }

    /**
	 * Executes the transaction.
	 * 
	 * @return <code>true</code> if executed
	 */
    public boolean execute() {
        DomainModel model = (DomainModel) getSession().getModel();
        if (!model.isSession()) {
            String error = "The domain model is not configured for transactions.";
            throw new ActionRuntimeException(error);
        } else if (getSession() == null) {
            String error = "Transaction must be within a session.";
            throw new ActionRuntimeException(error);
        }
        boolean executed = actions.executeAll();
        if (executed) {
            setStatus("executed");
            session.getHistory().add(this);
            model.notifyObservers(this);
        } else {
            actions.undoAll();
        }
        return executed;
    }

    /**
	 * Undos the transaction.
	 * 
	 * @return <code>true</code> if undone
	 */
    public boolean undo() {
        if (!isExecuted()) {
            String error = "A transaction must be executed first.";
            throw new ActionRuntimeException(error);
        }
        boolean undone = actions.undoAll();
        if (undone) {
            setStatus("undone");
            DomainModel model = (DomainModel) getSession().getModel();
            model.notifyObservers(this);
        } else {
            actions.executeAll();
        }
        return undone;
    }

    /**
	 * Gets domain model entries used in the transaction.
	 * 
	 * @return list of domain model entries
	 */
    public List<IEntities<?>> getEntries() {
        List<IEntities<?>> entries = new ArrayList<IEntities<?>>();
        List<Action> actionsList = actions.getCopyOfActions();
        for (Action action : actionsList) {
            EntitiesAction entitiesAction = (EntitiesAction) action;
            Entities<?> entities = (Entities<?>) entitiesAction.getEntities();
            if (entities.isPersistent()) {
                DomainModel model = (DomainModel) session.getModel();
                IEntities<?> entry = model.getModelMeta().getEntry(entities);
                if (entry != null && !entries.contains(entry)) {
                    entries.add(entry);
                }
            }
        }
        return entries;
    }

    /**
	 * Gets the actions on entities of the transaction.
	 * 
	 * @return list of actions on entities of the transactio
	 */
    public List<EntitiesAction> getEntitiesActions() {
        List<EntitiesAction> entitiesActions = new ArrayList<EntitiesAction>();
        List<Action> actionsList = actions.getCopyOfActions();
        for (Action action : actionsList) {
            EntitiesAction entitiesAction = (EntitiesAction) action;
            if (entitiesAction != null && !entitiesActions.contains(entitiesAction)) {
                entitiesActions.add(entitiesAction);
            }
        }
        return entitiesActions;
    }
}
