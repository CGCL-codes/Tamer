package ac.uk.napier.dfisch.jwurm.gameobjects;

import ac.uk.napier.dfisch.jwurm.engine.IUpdateable;
import ac.uk.napier.dfisch.jwurm.gameobjects.actors.*;

/**
 *
 * @author kdfisch
 */
public interface IState extends IUpdateable {

    public void enterState(Actor actor);

    public void exitState();

    public void onMessage(EventMessage message);
}
