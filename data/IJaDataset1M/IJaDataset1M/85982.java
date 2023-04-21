package emast.test.and.treasurehunter;

import java.util.Arrays;
import emast.model.model.ERGMDPModel;
import emast.model.problem.GridERGProblem;
import emast.model.propositional.Expression;

/**
 * Problem that represents a grid with holes and walls.
 * The agent must find the path to treasure and avoid the obstacles.
 *
 * Grid Caption:
 * T: Treasure
 * S: Initial position
 * H: Hole
 * W: Wall
 * 
 * @author anderson
 *
 * ------------
 * Default Grid
 * ------------
 *    1 2 3
 *  1 S H 
 *  2   W
 *  3     T
 * ----------
 */
public class TreasureHunterProblem<M extends ERGMDPModel> extends GridERGProblem<M> {

    public TreasureHunterProblem() {
        this((M) new TreasureHunterModel(3, 3), 3, 3);
    }

    public TreasureHunterProblem(final M pModel, final int pRows, final int pCols) {
        super(pModel, pRows, pCols);
        final Expression goal = new Expression("treasure");
        final Expression preserve = new Expression("!hole & !wall");
        setGoal(goal);
        setPreservationGoal(preserve);
        setError(0.9);
        setInitialStates(Arrays.asList(pModel.getState(1, 1)));
    }
}
