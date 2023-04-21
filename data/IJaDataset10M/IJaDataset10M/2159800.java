package atlantik.peer;

import atlantik.game.Atlantik;
import atlantik.game.Game;
import atlantik.game.Game.Option;
import atlantik.ui.GameOptionView;

public class GameOptionPeer extends AbstractPeer<Game.Option, GameOptionView> {

    public GameOptionPeer(Atlantik client, Option item) {
        super(client, item, new GameOptionView(client, item));
        updated(item);
    }
}
