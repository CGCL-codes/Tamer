package ac.uk.napier.dfisch.jwurm.gameobjects.actors.Character;

import ac.uk.napier.dfisch.jwurm.gameobjects.EventMessage;
import ac.uk.napier.dfisch.jwurm.gameobjects.actors.Actor;
import ac.uk.napier.dfisch.jwurm.gameobjects.IState;

/**
 *
 * @author kdfisch
 */
public final class AttackedState implements IState {

    private Actor lastAttacker;

    public AttackedState(Actor lastAttacker) {
    }

    public void enterState(Actor actor) {
    }

    public void exitState() {
    }

    public void update(int timeStep) {
    }

    public void onMessage(EventMessage message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
