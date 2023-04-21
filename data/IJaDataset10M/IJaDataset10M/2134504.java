package org.perfectday.logicengine.model.command.combat.state;

import org.perfectday.logicengine.model.ReferenceObject;
import org.perfectday.logicengine.model.command.Command;
import org.perfectday.logicengine.model.state.State;

/**
 *
 * @author Miguel Angel Lopez Montellano (alakat@gmail.com)
 */
public class RemoveStateCommand extends Command {

    private State state;

    private ReferenceObject mini;

    public ReferenceObject getMini() {
        return mini;
    }

    public void setMini(ReferenceObject mini) {
        this.mini = mini;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String info() {
        return "Se elimina el estado" + state;
    }
}
