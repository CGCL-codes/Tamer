package ac.uk.napier.dfisch.jwurm.gameobjects.actors;

import ac.uk.napier.dfisch.jwurm.engine.IUpdateable;
import ac.uk.napier.dfisch.jwurm.gameobjects.FiniteStateMachine;
import ac.uk.napier.dfisch.jwurm.gameobjects.IEventful;
import java.util.Vector;

/**
 *
 * @author kdfisch
 */
public final class Team implements IEventful, IUpdateable {

    private Vector members = new Vector();

    private FiniteStateMachine stateMachine;

    public Vector getMembers() {
        Vector defensiveCopy = new Vector(members.size());
        for (int i = 0; i < members.size(); i++) {
            defensiveCopy.addElement(members.elementAt(i));
        }
        return defensiveCopy;
    }

    public FiniteStateMachine getStateMachine() {
        return (stateMachine);
    }

    public void update(int timeStep) {
        stateMachine.update(timeStep);
    }
}
