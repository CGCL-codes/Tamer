package emast.test.and.message;

import emast.model.action.Action;
import emast.model.function.PropositionFunction;
import emast.model.function.RewardFunction;
import emast.model.function.RewardFunctionItem;
import emast.model.propositional.Proposition;
import emast.model.state.State;
import emast.test.AbstractPPFERGTest;
import emast.test.and.treasurehunter.dec.DecTreasureHunterModel;
import emast.test.and.treasurehunter.dec.DecTreasureHunterProblem;

/**
 *
 * @author anderson
 */
public class MessageTest extends AbstractPPFERGTest<DecTreasureHunterModel, DecTreasureHunterProblem> {

    @Override
    protected DecTreasureHunterProblem createProblem() {
        final int rows = 3;
        final int cols = 4;
        final int agents = 2;
        final DecTreasureHunterModel model = new DecTreasureHunterModel(rows, cols, agents);
        final DecTreasureHunterProblem problem = new DecTreasureHunterProblem(model, rows, cols);
        problem.setInitialStates(model.getStates(1, 0, 2, 1));
        final Proposition hole = model.getProposition("hole");
        final Proposition wall = model.getProposition("wall");
        final Proposition treasure = model.getProposition("treasure");
        final PropositionFunction pf = new PropositionFunction(model);
        pf.add(1, 1, wall);
        pf.add(1, 2, hole);
        pf.add(0, 3, treasure);
        model.setPropositionFunction(pf);
        final RewardFunction rf = new RewardFunction(problem.getModel());
        rf.add(new RewardFunctionItem(-100., model.getState(2, 2), State.ANY, Action.ANY));
        model.setRewardFunction(rf);
        return problem;
    }

    public static void main(final String[] pArgs) {
        new MessageTest().run();
    }
}
