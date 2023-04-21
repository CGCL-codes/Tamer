package com.customwars.client.model.game;

import com.customwars.client.action.ActionParser;
import com.customwars.client.action.CWAction;
import com.customwars.client.tools.Args;
import com.customwars.client.ui.state.InGameContext;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A replay of a game containing The initial game and a queue of text actions.
 * These text actions are converted to a CWAction when invoking the execNextReplayAction method.
 * <p/>
 * You can only move 1 step forward. The replay can only be run once.
 * When at the last action hasMoreActions returns false.
 */
public class GameReplay implements Serializable {

    private static final Logger logger = Logger.getLogger(GameReplay.class);

    private Game initialGame;

    private List<String> replayQueue;

    private transient int currentReplayIndex;

    private transient ActionParser actionParser;

    public GameReplay(Game initialGame) {
        this.initialGame = new Game(initialGame);
        this.replayQueue = new ArrayList<String>(40);
        this.actionParser = new ActionParser(initialGame);
        this.currentReplayIndex = 0;
    }

    public void addActions(Iterable<CWAction> actions) {
        for (CWAction action : actions) {
            replayQueue.add(action.getActionCommand());
        }
    }

    /**
   * Executes the next replay action
   * After the reply queue is exhausted hasMoreActions will return false and further
   * invocations are ignored.
   *
   * @param inGameContext The context in which the action should be executed
   */
    public void execNextReplayAction(InGameContext inGameContext) {
        if (hasMoreActions()) {
            String replayActionText = replayQueue.get(currentReplayIndex);
            logger.debug(replayActionText);
            CWAction replayAction = actionParser.parse(replayActionText);
            inGameContext.doAction(replayAction);
            setReplayIndex(currentReplayIndex + 1);
            if (!hasMoreActions()) {
                replayQueue.clear();
            }
        }
    }

    public Game getInitialGame() {
        return initialGame;
    }

    public boolean hasMoreActions() {
        return !replayQueue.isEmpty() && currentReplayIndex < replayQueue.size();
    }

    private void setReplayIndex(int replayIndex) {
        this.currentReplayIndex = Args.getBetweenMinMax(replayIndex, 0, replayQueue.size());
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(initialGame);
        out.writeObject(replayQueue);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws Exception {
        this.initialGame = (Game) in.readObject();
        this.replayQueue = (List<String>) in.readObject();
        this.actionParser = new ActionParser(initialGame);
    }
}
