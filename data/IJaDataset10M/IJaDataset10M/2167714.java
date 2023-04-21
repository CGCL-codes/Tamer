package org.vizzini.game.boardgame.action;

import org.vizzini.game.IAgent;
import org.vizzini.game.IToken;
import org.vizzini.game.IntegerPosition;
import org.vizzini.game.action.ActionFactory;
import org.vizzini.game.action.RedoAction;
import org.vizzini.game.action.UndoAction;
import org.vizzini.game.boardgame.IGridBoard;

/**
 * Provides a factory for game actions.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.2
 */
public class BoardGameActionFactory {

    /** Action factory delegate. */
    private ActionFactory _actionFactoryDelegate;

    /**
     * @param   gridBoard  Grid board.
     * @param   agent      Agent.
     * @param   position   Position.
     *
     * @return  an action appropriate for the given parameters.
     *
     * @since   v0.2
     */
    public IGridBoardAction getAction(IGridBoard gridBoard, IAgent agent, IntegerPosition position) {
        RemoveAction answer = RemoveAction.get(gridBoard, agent, position);
        return answer;
    }

    /**
     * @param   gridBoard     Grid board.
     * @param   agent         Agent.
     * @param   fromPosition  From position.
     * @param   toPosition    To position.
     *
     * @return  an action appropriate for the given parameters.
     *
     * @since   v0.2
     */
    public IGridBoardAction getAction(IGridBoard gridBoard, IAgent agent, IntegerPosition fromPosition, IntegerPosition toPosition) {
        IGridBoardAction answer = null;
        boolean isOccupied = gridBoard.getTokenCollection().get(toPosition) != null;
        if (isOccupied) {
            answer = MoveCaptureAction.get(gridBoard, agent, fromPosition, toPosition);
        } else {
            answer = MoveAction.get(gridBoard, agent, fromPosition, toPosition);
        }
        return answer;
    }

    /**
     * @param   gridBoard   Grid board.
     * @param   agent       Agent.
     * @param   position    Position.
     * @param   tokenClass  Token class.
     *
     * @return  an action appropriate for the given parameters.
     *
     * @since   v0.2
     */
    public IGridBoardAction getAction(IGridBoard gridBoard, IAgent agent, IntegerPosition position, Class<? extends IToken> tokenClass) {
        PlaceAction answer = PlaceAction.get(gridBoard, agent, position, tokenClass);
        return answer;
    }

    /**
     * @param   gridBoard  Grid board.
     * @param   agent      Agent.
     * @param   position   Position.
     * @param   token      Token.
     *
     * @return  an action appropriate for the given parameters.
     *
     * @since   v0.4
     */
    public IGridBoardAction getAction(IGridBoard gridBoard, IAgent agent, IntegerPosition position, IToken token) {
        PlaceTokenAction answer = PlaceTokenAction.get(gridBoard, agent, position, token);
        return answer;
    }

    /**
     * @param   agent  Agent.
     *
     * @return  an <code>RedoAction</code> for the given parameters.
     *
     * @since   v0.2
     */
    public RedoAction getRedoAction(IAgent agent) {
        RedoAction answer = getActionFactoryDelegate().getRedoAction(agent);
        return answer;
    }

    /**
     * @param   agent  Agent.
     *
     * @return  an <code>UndoAction</code> for the given parameters.
     *
     * @since   v0.2
     */
    public UndoAction getUndoAction(IAgent agent) {
        UndoAction answer = getActionFactoryDelegate().getUndoAction(agent);
        return answer;
    }

    /**
     * @return  the actionFactoryDelegate
     */
    private ActionFactory getActionFactoryDelegate() {
        return _actionFactoryDelegate;
    }
}
