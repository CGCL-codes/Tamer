package org.vizzini.game.boardgame.chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.vizzini.game.IAgent;
import org.vizzini.game.IEvaluator;
import org.vizzini.game.ITeam;
import org.vizzini.game.IntegerPosition;
import org.vizzini.game.action.IAction;
import org.vizzini.game.action.IActionGenerator;
import org.vizzini.game.boardgame.action.MoveAction;

/**
 * Provides unit tests for the <code>SimpleChessAgent</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public class SimpleChessAgentTest extends DefaultChessComputerAgentTest {

    /**
     * @see  org.vizzini.game.boardgame.chess.DefaultChessComputerAgentTest#getAction()
     */
    @Override
    public void getAction() {
        IAction action = _agent0.getAction(_environment, _adjudicator);
        assertNotNull(action);
        assertEquals(MoveAction.class, action.getClass());
        MoveAction moveAction = (MoveAction) action;
        assertEquals(IntegerPosition.get(0, 0, 0), moveAction.getFromPosition());
        assertEquals(IntegerPosition.get(1, 0, 0), moveAction.getToPosition());
    }

    /**
     * @see  org.vizzini.game.boardgame.chess.DefaultChessComputerAgentTest#testToString()
     */
    @Override
    public void testToString() {
        String expected = "org.vizzini.game.boardgame.chess.SimpleChessAgent [org.vizzini.game.boardgame.chess.DefaultChessComputerAgent [org.vizzini.game.DefaultComputerAgent [org.vizzini.game.DefaultAgent [_name=White,_score=12,_team=org.vizzini.game.DefaultTeam [_name=white]]],_isPawnPrompt=true]]";
        assertEquals(expected, _agent0.toString());
    }

    /**
     * @see  org.vizzini.game.boardgame.chess.DefaultChessComputerAgentTest#create(java.lang.String,
     *       org.vizzini.game.ITeam, org.vizzini.game.action.IActionGenerator,
     *       org.vizzini.game.IEvaluator)
     */
    @Override
    protected IAgent create(String name, ITeam team, IActionGenerator actionGenerator, IEvaluator evaluator) {
        IAgent answer = new SimpleChessAgent(actionGenerator, evaluator);
        answer.setName(name);
        answer.setTeam(team);
        return answer;
    }

    /**
     * @see  org.vizzini.game.boardgame.chess.DefaultChessComputerAgentTest#create1(java.lang.String,
     *       org.vizzini.game.ITeam, org.vizzini.game.action.IActionGenerator,
     *       org.vizzini.game.IEvaluator)
     */
    @Override
    protected IAgent create1(String name, ITeam team, IActionGenerator actionGenerator, IEvaluator evaluator) {
        IAgent answer = new SimpleChessAgent(actionGenerator, evaluator);
        answer.setName(name);
        answer.setTeam(team);
        return answer;
    }
}
