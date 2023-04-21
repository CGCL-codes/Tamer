package jmetest.scene;

import java.util.logging.Logger;
import com.jmex.game.StandardGame;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameState;
import com.jmex.game.state.GameStateManager;
import com.jmex.scene.TimedLifeController;

/**
 * @author Matthew D. Hicks
 */
public class TestTimedLifeController extends TimedLifeController {

    private static final Logger logger = Logger.getLogger(TestTimedLifeController.class.getName());

    private static final long serialVersionUID = 1L;

    private GameState state;

    public TestTimedLifeController(GameState state, float lifeInSeconds) {
        super(lifeInSeconds);
        this.state = state;
    }

    public void updatePercentage(float percentComplete) {
        logger.info("I'm this much complete: " + percentComplete);
        if (percentComplete == 1.0f) {
            logger.info("Guess I'm done!");
            GameStateManager.getInstance().detachChild(state);
            state.setActive(false);
        }
    }

    public static void main(String[] args) throws Exception {
        StandardGame game = new StandardGame("TestTimedLifeGameState");
        game.start();
        BasicGameState timedState = new BasicGameState("TimedLife");
        TestTimedLifeController controller = new TestTimedLifeController(timedState, 10.0f);
        timedState.getRootNode().addController(controller);
        GameStateManager.getInstance().attachChild(timedState);
        timedState.setActive(true);
    }
}
