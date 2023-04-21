package org.jskat.ai.nn.train;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.NoOpLog;
import org.jskat.ai.nn.AIPlayerNN;
import org.jskat.control.JSkatMaster;
import org.jskat.control.JSkatThread;
import org.jskat.control.SkatGame;
import org.jskat.data.GameAnnouncement;
import org.jskat.data.GameAnnouncement.GameAnnouncementFactory;
import org.jskat.data.GameSummary;
import org.jskat.data.SkatGameData.GameState;
import org.jskat.gui.NullView;
import org.jskat.util.CardDeck;
import org.jskat.util.GameType;
import org.jskat.util.GameVariant;
import org.jskat.util.Player;

/**
 * Trains the neural networks
 */
public class NNTrainer extends JSkatThread {

    private static Log log = LogFactory.getLog(NNTrainer.class);

    private JSkatMaster jskat;

    private Random rand;

    private List<StringBuffer> nullGames;

    private GameType gameType;

    /**
	 * Constructor
	 */
    public NNTrainer() {
        jskat = JSkatMaster.instance();
        rand = new Random();
        nullGames = new ArrayList<StringBuffer>();
        initLearningPatterns();
    }

    private void initLearningPatterns() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CA CT CK ");
        buffer.append("C9 ST SK ");
        buffer.append("H7 DA DT ");
        buffer.append("SA HA ");
        buffer.append("CQ CJ C8 C7 ");
        buffer.append("SQ SJ HK HQ ");
        buffer.append("DK DQ D9 D8 ");
        buffer.append("S9 S8 S7 ");
        buffer.append("HJ H9 H8 ");
        buffer.append("D7 HT DJ");
        nullGames.add(buffer);
        buffer = new StringBuffer();
        buffer.append("CA CT CK ");
        buffer.append("S9 ST SK ");
        buffer.append("H7 DA DT ");
        buffer.append("SA HA ");
        buffer.append("CQ CJ C8 C7 ");
        buffer.append("SQ SJ HK HQ ");
        buffer.append("DK DQ D9 D8 ");
        buffer.append("C9 S8 S7 ");
        buffer.append("HJ H9 H8 ");
        buffer.append("D7 HT DJ");
        nullGames.add(buffer);
        buffer = new StringBuffer();
        buffer.append("C9 C8 C7 ");
        buffer.append("CA CT CK ");
        buffer.append("CQ CJ SA ");
        buffer.append("SJ SK ");
        buffer.append("ST S9 S8 S7 ");
        buffer.append("SQ HA HT HK ");
        buffer.append("HQ DA DT DK ");
        buffer.append("H9 H8 H7 ");
        buffer.append("DQ DJ D9 ");
        buffer.append("D8 D7 HJ");
        nullGames.add(buffer);
        buffer = new StringBuffer();
        buffer.append("SA ST SK ");
        buffer.append("S9 CT CK ");
        buffer.append("H7 DA DT ");
        buffer.append("CA HA ");
        buffer.append("SQ SJ S8 S7 ");
        buffer.append("CQ CJ HK HQ ");
        buffer.append("DK DQ D9 D8 ");
        buffer.append("C9 C8 C7 ");
        buffer.append("HJ H9 H8 ");
        buffer.append("D7 HT DJ");
        nullGames.add(buffer);
        buffer = new StringBuffer();
        buffer.append("HA HT HK ");
        buffer.append("H9 CT CK ");
        buffer.append("S7 DA DT ");
        buffer.append("CA SA ");
        buffer.append("HQ HJ H8 H7 ");
        buffer.append("CQ CJ SK SQ ");
        buffer.append("DK DQ D9 D8 ");
        buffer.append("S9 S8 S7 ");
        buffer.append("SJ S9 S8 ");
        buffer.append("D7 ST DJ");
        nullGames.add(buffer);
    }

    /**
	 * Sets the game type to learn
	 * 
	 * @param newGameType
	 *            Game type
	 */
    public void setGameType(GameType newGameType) {
        gameType = newGameType;
    }

    /**
	 * @see java.lang.Thread#run()
	 */
    @Override
    public void run() {
        trainNets();
    }

    /**
	 * Trains the neural networks
	 */
    private void trainNets() {
        AIPlayerNN nnPlayer1 = new AIPlayerNN();
        nnPlayer1.setIsLearning(true);
        nnPlayer1.setLogger(new NoOpLog());
        AIPlayerNN nnPlayer2 = new AIPlayerNN();
        nnPlayer2.setIsLearning(true);
        nnPlayer2.setLogger(new NoOpLog());
        AIPlayerNN nnPlayer3 = new AIPlayerNN();
        nnPlayer3.setIsLearning(true);
        nnPlayer3.setLogger(new NoOpLog());
        long episodes = 0;
        long episodesWonGames = 0;
        long totalWonGames = 0;
        long totalGames = 0;
        int episodeSteps = 100;
        while (true) {
            if (episodes > 0 && episodes % episodeSteps == 0) {
                log.debug(gameType + ": Episode " + episodes + " won games " + episodesWonGames + " (" + 100 * episodesWonGames / (episodeSteps * 3) + " %)" + " total won games " + totalWonGames + " (" + 100 * totalWonGames / totalGames + " %)");
                jskat.addTrainingResult(gameType, episodes, totalWonGames, episodesWonGames, 0.0);
                episodesWonGames = 0;
            }
            for (Player currPlayer : Player.values()) {
                nnPlayer1.newGame(Player.FOREHAND);
                nnPlayer2.newGame(Player.MIDDLEHAND);
                nnPlayer3.newGame(Player.REARHAND);
                SkatGame game = new SkatGame("table", GameVariant.STANDARD, nnPlayer1, nnPlayer2, nnPlayer3);
                game.setView(new NullView());
                game.setLogger(new NoOpLog());
                CardDeck deck = new CardDeck();
                deck.shuffle();
                log.debug("Card deck: " + deck);
                game.setCardDeck(deck);
                game.dealCards();
                game.setDeclarer(currPlayer);
                GameAnnouncementFactory factory = GameAnnouncement.getFactory();
                factory.setGameType(gameType);
                game.setGameAnnouncement(factory.getAnnouncement());
                game.setGameState(GameState.TRICK_PLAYING);
                game.start();
                try {
                    game.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                game.getGameResult();
                boolean gameWon = false;
                if (gameType.equals(GameType.RAMSCH)) {
                    gameWon = isRamschGameWon(game.getGameSummary(), currPlayer);
                } else {
                    gameWon = game.isGameWon();
                }
                if (gameWon) {
                    episodesWonGames++;
                    totalWonGames++;
                }
                totalGames++;
            }
            episodes += 3;
            checkWaitCondition();
        }
    }

    private static boolean isRamschGameWon(GameSummary gameSummary, Player currPlayer) {
        boolean ramschGameWon = false;
        int playerPoints = gameSummary.getPlayerPoints(currPlayer);
        int highestPlayerPoints = 0;
        for (Player player : Player.values()) {
            int currPlayerPoints = gameSummary.getPlayerPoints(player);
            if (currPlayerPoints > highestPlayerPoints) {
                highestPlayerPoints = currPlayerPoints;
            }
        }
        if (highestPlayerPoints > playerPoints) {
            ramschGameWon = true;
        }
        return ramschGameWon;
    }
}
