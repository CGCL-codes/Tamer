package structs;

import interfaces.ICard;
import interfaces.Strategy;

public class StupidStrategy implements Strategy {

    private Game game;

    public StupidStrategy(Game game) {
        this.game = game;
    }

    public ICard nextMove() {
        Player player = (Player) this.game.getCurrentPlayer();
        return player.getCurrentHand().remove(0);
    }
}
