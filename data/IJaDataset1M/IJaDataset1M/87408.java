package hotgammon.domain.game.move;

import hotgammon.domain.board.Board;
import hotgammon.domain.board.Color;
import hotgammon.domain.board.Location;
import hotgammon.domain.*;
import hotgammon.domain.game.Rules.AvailableDice;
import hotgammon.domain.game.Rules.MoveFromBarOnlyWhenCheckerOnBar;
import hotgammon.domain.game.Rules.MoveRightDirection;
import hotgammon.domain.game.Rules.NotLockedLocationMoveOwnChecker;
import hotgammon.domain.game.Rules.Rule;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author truck
 */
public class BetaMoveStrategy implements MoveStrategy {

    private Collection<Rule> rules;

    public BetaMoveStrategy() {
        rules = new ArrayList<Rule>();
        rules.add(new MoveRightDirection());
        rules.add(new NotLockedLocationMoveOwnChecker());
        rules.add(new AvailableDice());
        rules.add(new MoveFromBarOnlyWhenCheckerOnBar());
    }

    public boolean isValidMove(Board b, Location from, Location to, int[] movesLeft, Color playerInTurn) {
        for (Rule r : rules) {
            if (!r.isValidMove(b, from, to, movesLeft, playerInTurn)) return false;
        }
        return true;
    }

    public int[] removeDice(int[] dice, Location from, Location to) {
        int[] tmp = new int[dice.length - 1];
        boolean diceRemoved = false;
        int g = 0;
        for (int i = 0; i < dice.length; i++) {
            if (Math.abs(Location.distance(from, to)) == dice[i] && !diceRemoved) {
                g++;
                diceRemoved = true;
            } else {
                tmp[i - g] = dice[i];
            }
        }
        return tmp;
    }
}
