package hu.arguscab.ai.game;

import hu.arguscab.ai.FitnessFunction;
import hu.arguscab.ai.Model;
import hu.arguscab.game.GameInfo;
import hu.arguscab.game.WorldState;
import hu.arguscab.physic.Vector;
import hu.arguscab.physic.Vectors;

/**
 *
 * @author sharp
 */
public class FDirection extends FitnessFunction {

    @Override
    public float getMinValue() {
        return -100;
    }

    @Override
    public float getMaxValue() {
        return 100;
    }

    @Override
    public float calcF(Model m) {
        WorldState state = (WorldState) m.getState();
        Vector target = ((Bot) state.getShipState().getShip().getController()).getTarget().getState().getPos();
        Vector myPos = state.getShipState().getPos();
        target = Vectors.sub(target, myPos);
        Vector myDir = state.getShipState().getDirv();
        target.setLength(1);
        myDir.setLength(1);
        return (Vectors.dotProduct(myDir, target) * 100.0f);
    }
}
