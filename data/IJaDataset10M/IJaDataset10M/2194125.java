package pentago.View;

import pentago.Controller.IPentagoController;
import pentago.Game.IMove;
import pentago.Game.IPlayer;
import pentago.Model.IState;
import pentago.Model.IModel;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ASCIIPentagoView implements IPentagoController {

    private IModel model = null;

    private IPlayer currentPlayer = null;

    public ASCIIPentagoView(IModel model) {
        this.model = model;
    }

    public void changePlayer(IPlayer player) {
        currentPlayer = player;
    }

    public void startGame() {
        System.out.println(model.getBoard());
        System.out.println();
        System.out.println("Player 1: " + currentPlayer.getPlayerNumber());
    }

    public void stopGame() {
        System.exit(0);
    }

    public void allowPointPlacement(boolean allow) {
        if (allow) {
        }
    }

    public void allowQuadrantRotation(boolean allow) {
    }

    public void updateState(IState state) {
    }

    public void queueMove(IMove move) {
    }

    public void submitMove(IMove move) {
    }

    public void submitMoves() {
    }

    public void undoMoves() {
    }
}
