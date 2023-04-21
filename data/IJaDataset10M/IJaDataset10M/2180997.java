package ca.genovese.briscola;

import java.util.LinkedList;
import java.util.List;
import ca.genovese.briscola.model.Deck;
import ca.genovese.briscola.model.DeckImpl;
import ca.genovese.briscola.model.ItalianCardEnumImpl;
import ca.genovese.briscola.player.ai.SimpleComputerPlayerImpl;
import ca.genovese.briscola.player.ui.swing.SwingGuiPlayerImpl;

public class SwingTestMain {

    public static void main(String[] args) {
        Deck deck = new DeckImpl(ItalianCardEnumImpl.AceClubs);
        List<PlayerState> players = new LinkedList<PlayerState>();
        GameJudge gameJudge = new GameJudgeImpl();
        PlayerStateImpl playerOne = new PlayerStateImpl();
        playerOne.setName("You");
        playerOne.setPlayer(new SwingGuiPlayerImpl());
        players.add(playerOne);
        SimpleComputerPlayerImpl ai = new SimpleComputerPlayerImpl();
        PlayerStateImpl playerTwo = new PlayerStateImpl();
        playerTwo.setName("Bob");
        playerTwo.setPlayer(ai);
        players.add(playerTwo);
        Dealer dealer = new Dealer();
        dealer.setCardDeck(deck);
        dealer.setPlayers(players);
        dealer.setGameJudge(gameJudge);
        dealer.playGame();
    }
}
