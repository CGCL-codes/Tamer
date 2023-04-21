package org.goodjava.shift.engine;

import org.goodjava.shift.exception.ShiftException;
import org.goodjava.shift.model.State;
import org.goodjava.shift.util.MessageUtils;

/**
 * @author Matt created on Aug 14, 2005
 */
public final class States extends ShiftMap {

    public void add(final State state) throws ShiftException {
        if ((state.getId() == null) || (state.getId().equals(""))) {
            throw new ShiftException(MessageUtils.get("no.state.id"));
        }
        if (getMap().containsKey(state.getId())) {
            throw new ShiftException(MessageUtils.get("duplicate.state.id", new Object[] { state.getId() }));
        }
        getMap().put(state.getId(), state);
    }

    public State get(final String id) throws ShiftException {
        if (!getMap().containsKey(id)) {
            throw new ShiftException(MessageUtils.get("state.id.not.found", new Object[] { id }));
        }
        return (State) getMap().get(id);
    }
}
