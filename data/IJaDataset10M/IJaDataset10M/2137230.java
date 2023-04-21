package emil.poker.test.ai.handEvaluator;

import net.sourceforge.robotnik.poker.Deck;
import net.sourceforge.robotnik.poker.Player;
import net.sourceforge.robotnik.poker.Stack;
import net.sourceforge.robotnik.poker.Table;
import net.sourceforge.robotnik.poker.players.SimplePlayerImpl;
import net.sourceforge.robotnik.poker.util.CycleVector;
import emil.poker.ai.AiPlayer;
import emil.poker.ai.RobotnikLimitHoldem;
import emil.poker.test.ai.TestBase;

public abstract class TestStartingHandEvaluator extends TestBase {

    Player player = null;

    Table table = null;

    Deck deck;

    CycleVector<Player> players = null;

    @Override
    protected void setUp() {
        game = new RobotnikLimitHoldem();
        table = game.getTable();
        deck = table.getDeck();
        game.setBetSize1(1);
        game.setBetSize2(2);
        game.setMaxNumOfBets(4);
        players = new CycleVector<Player>();
        for (int i = 0; i < 10; i++) {
            Player player = new SimplePlayerImpl();
            player.setName("currentPlayer" + i);
            player.setStack(new Stack(100));
            players.add(player);
        }
        game.setPlayers(players);
        game.init();
        game.nextIsDealer();
        game.payBlinds();
    }
}
