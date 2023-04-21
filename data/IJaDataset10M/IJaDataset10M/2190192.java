package main.java.chessfootball.rules.checker;

/**
 * User: Igor
 * Date: 28.07.2011
 * Time: 21:03:30
 */
public class KickChecker extends AbstractMoveChecker {

    @Override
    public boolean valid() {
        if (move.getLength() > 1) {
            if (move.getLength() > game.getRule().getGame().getKickLength()) {
                return false;
            }
            if (!put.getCell().equals(game.getField().getBall().getCell())) {
                return false;
            }
            if (game.getRule().getGame().isKick()) {
                if (game.getField().getPlayer(move.getVector().getCell(game.getField().getBall().getCell(), 1)) != null) {
                    return false;
                }
            } else {
                for (int l = 1; l < move.getLength(); l++) {
                    if (game.getField().getPlayer(move.getVector().getCell(game.getField().getBall().getCell(), l)) != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
