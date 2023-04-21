package net.sf.dropboxmq.workflow.persistence.memory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.dropboxmq.workflow.data.State;
import net.sf.dropboxmq.workflow.persistence.StatePersistence;

/**
 * Created: 26 Aug 2010
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision$, $Date$
 */
public class StatePersistenceImpl implements StatePersistence {

    private final ProcessTypePersistenceImpl processTypePersistence;

    private final Map<Integer, State> statesById = new HashMap<Integer, State>();

    private final Map<Integer, Map<String, State>> statesByNameByProcessTypeId = new HashMap<Integer, Map<String, State>>();

    private int nextStateId = 1000;

    public StatePersistenceImpl(final ProcessTypePersistenceImpl processTypePersistence) {
        this.processTypePersistence = processTypePersistence;
    }

    @Override
    public void storeState(final State state) {
        final State newState = state.clone();
        if (statesById.containsKey(nextStateId)) {
            throw new RuntimeException("State id already exists, id = " + nextStateId);
        }
        newState.setId(nextStateId);
        nextStateId++;
        statesById.put(newState.getId(), newState);
        processTypePersistence.getExistingProcessTypeById(newState.getProcessTypeId());
        final Map<String, State> statesByName = getStatesByProcessTypeId(newState.getProcessTypeId());
        if (statesByName.containsKey(newState.getName())) {
            throw new RuntimeException("Process type already contains state of given name," + " state = " + newState.getName());
        }
        statesByName.put(newState.getName(), newState);
        state.setId(newState.getId());
    }

    private Map<String, State> getStatesByProcessTypeId(final int processTypeId) {
        Map<String, State> statesByName = statesByNameByProcessTypeId.get(processTypeId);
        if (statesByName == null) {
            statesByName = new HashMap<String, State>();
            statesByNameByProcessTypeId.put(processTypeId, statesByName);
        }
        return statesByName;
    }

    @Override
    public Collection<State> getStatesByProcessType(final int processTypeId) {
        final Map<String, State> states = getStatesByProcessTypeId(processTypeId);
        final List<State> stateList = new ArrayList<State>();
        for (final State state : states.values()) {
            stateList.add(state.clone());
        }
        return stateList;
    }

    State getExistingStateById(final int id) {
        final State existingState = statesById.get(id);
        if (existingState == null) {
            throw new RuntimeException("Could not find state, id = " + id);
        }
        return existingState;
    }
}
