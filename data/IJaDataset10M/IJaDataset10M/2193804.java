package hotgammon.domain.game.Rules;

import hotgammon.domain.board.Board;
import hotgammon.domain.board.Color;
import hotgammon.domain.board.Location;

/**
 *
 * @author jeppewelling
 */
public class AvailableDice implements Rule {

    public boolean isValidMove(Board b, Location from, Location to, int[] movesLeft, Color playerInTurn) {
        boolean validMove = false;
        for (int i : movesLeft) {
            if ((Location.distance(from, to) == i && b.getColor(from) == Color.BLACK) || (Location.distance(from, to) == -i && b.getColor(from) == Color.RED)) {
                validMove = true;
            }
        }
        if (validMove == false) {
            return false;
        }
        return true;
    }
}
