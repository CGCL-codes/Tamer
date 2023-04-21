package emil.poker.ai.opponentEvaluators;

import java.util.HashMap;
import java.util.Vector;
import net.sourceforge.robotnik.poker.util.CycleVector;
import org.apache.log4j.Logger;
import emil.poker.ai.AiPlayer;
import emil.poker.ai.Evaluator;
import emil.poker.entities.HandEntity;

/**
 * Anvand PreFlopHandEvaluator istallet
 * 
 * @author emil.sandin
 *
 */
@Deprecated
public class OppsStartHandsProbsEvaluator implements Evaluator {

    CycleVector<AiPlayer> players;

    HashMap<AiPlayer, HashMap<HandEntity, Double>> startHandsProbs;

    Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public OppsStartHandsProbsEvaluator(Vector<AiPlayer> players) {
        super();
        this.players = (CycleVector<AiPlayer>) players;
    }

    /**
	 * For varje spelare raknar denna metoden fram:
	 * Hur stor chans ar det att man far varje hand, 
	 * och om man fatt den handen, hur stor chans ar 
	 * det att man gor det spelet?  
	 */
    @Deprecated
    public void evaluate() {
    }

    public HashMap<AiPlayer, HashMap<HandEntity, Double>> getStartHandsProbs() {
        return startHandsProbs;
    }
}
