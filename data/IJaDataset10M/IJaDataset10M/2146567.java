package server;

import org.apache.log4j.Logger;
import common.GameLoop;
import common.GameModel;
import common.Player;

public class ServerGameLoop extends GameLoop {

    private static Logger logger = Logger.getLogger(ServerGameLoop.class);

    private ServerGameRound round;

    public ServerGameLoop(GameModel gameModel, ServerGameRound round) {
        super(gameModel);
        this.round = round;
    }

    /**
	 * Determines the winner and triggers post round processing.
	 */
    @Override
    protected void doPostGameProcessing() {
        logger.info("ServerGameLoop#doPostGameProcessing");
        if (winnerId == 0) {
            logger.info("It's a tie!");
        } else {
            logger.info("not a tie");
            Player winner = (Player) gameModel.getGameObjectById(winnerId);
            logger.info("adding 1 to the inner score: " + winner.getName() + " id: " + winner.getId());
            logger.info("Round " + round.toString());
            round.addRoundScore(winner.getId(), 3);
            logger.info("The winner is: " + winner.getName() + " id: " + winner.getId());
        }
        round.doPostRoundProcessing();
    }
}
