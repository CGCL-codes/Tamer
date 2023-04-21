package gamerule;

import board.IBoard;
import ship.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines a number of ships of each type (e.g 1x4deck, 2x3deck, 3x2deck, 4x1deck)
 *
 * @author tymoshya
 * @since 8/4/11
 */
public class RuleShipNumber implements IGameRule {

    private IBoard board;

    private Map<Class<? extends Ship>, Integer> shipsNumberMap = new HashMap<Class<? extends Ship>, Integer>();

    public RuleShipNumber(IBoard board) {
        this.board = board;
        fillMap();
    }

    private void fillMap() {
        shipsNumberMap.put(ShipFourDeck.class, 1);
        shipsNumberMap.put(ShipThreeDeck.class, 2);
        shipsNumberMap.put(ShipTwoDeck.class, 3);
        shipsNumberMap.put(ShipOneDeck.class, 4);
    }

    public boolean isSatisfied() {
        return false;
    }
}
