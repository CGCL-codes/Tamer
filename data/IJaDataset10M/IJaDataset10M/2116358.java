package hottargui.config;

import hottargui.framework.Game;
import hottargui.framework.GameFactory;
import hottargui.framework.Tile;
import org.junit.Before;
import org.junit.Test;
import java.util.Iterator;

public class TestDeltaGame {

    private DeltaGame game;

    private DeltaGameFactory gameFactory;

    private WinnerStrategy winnerStrategy;

    public TestDeltaGame() {
    }

    @Before
    public void setUp() {
        game = new DeltaGame();
        gameFactory = new DeltaGameFactory(game);
        game.setGameFactory(gameFactory);
        gameFactory.createBoard();
        winnerStrategy = gameFactory.createWinnerStrategy();
    }

    @Test
    public void typeBoard() {
        Iterator iterator = game.getBoardIterator();
        while (iterator.hasNext()) {
            Tile t = (DeltaTile) iterator.next();
            System.out.println(t);
        }
    }
}
