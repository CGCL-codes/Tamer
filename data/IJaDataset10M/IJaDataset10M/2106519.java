package org.chess.quasimodo.domain.logic;

import java.util.Timer;
import java.util.TimerTask;
import org.apache.commons.lang3.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chess.quasimodo.application.QuasimodoContext;
import org.chess.quasimodo.config.Config;
import org.chess.quasimodo.domain.SetUpGameModel;
import org.chess.quasimodo.domain.TimeControlModel;
import org.chess.quasimodo.domain.logic.Game.Status;
import org.chess.quasimodo.errors.BusinessException;
import org.chess.quasimodo.event.ClockChangedAware;
import org.chess.quasimodo.event.GameStatusAware;
import org.chess.quasimodo.event.MoveTimeChangedAware;
import org.chess.quasimodo.event.PlayerReadyAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Eugen Covaci.
 */
@Component("clock")
@Scope("prototype")
public class Clock implements PlayerReadyAware {

    private Log logger = LogFactory.getLog(Clock.class);

    /**
	 * Start game time.
	 */
    private Long startGameTime;

    /**
	 * End game time
	 */
    private Long endGameTime;

    /**
	 * White elapsed time (seconds).
	 */
    private long whiteElapsedSeconds;

    /**
	 * Black elapsed time (seconds).
	 */
    private long blackElapsedSeconds;

    /**
	 * White remaining time (seconds).
	 */
    private long whiteRemainingSeconds;

    /**
	 * Black remaining time (seconds).
	 */
    private long blackRemainingSeconds;

    /**
     * Current move elapsed time (seconds).
     */
    private long elapsedMoveTime;

    private long startMoveTime;

    private long endMoveTime;

    private long moveTimeGap;

    /**
     * Flag for pause game state.
     */
    private boolean paused;

    /**
     * Indicates the current time control (between 1 and 3).
     */
    private byte timeControlIndex = 1;

    /**
     * Current game model.
     */
    private SetUpGameModel gameModel;

    /**
     * Listener for game status changed.
     */
    private GameStatusAware statusAware;

    /**
	 * The color of the player whose turn is to move.
	 */
    private ChessColor colorToMove = ChessColor.WHITE;

    @Autowired
    private QuasimodoContext context;

    @Autowired
    private Config config;

    @Autowired
    private ClockChangedAware clockView;

    @Autowired
    private MoveTimeChangedAware engineView;

    private Timer timer = new Timer();

    private ClockTimer timerTask;

    private class ClockTimer extends TimerTask {

        @Override
        public void run() {
            onTimeChanged();
        }
    }

    private void onTimeChanged() {
        if (gameModel.isFriendly()) {
            if (colorToMove.isWhite()) {
                whiteElapsedSeconds++;
            } else {
                blackElapsedSeconds++;
            }
            clockView.setClockTime(whiteElapsedSeconds, blackElapsedSeconds);
        } else {
            if (colorToMove.isWhite()) {
                whiteRemainingSeconds--;
            } else {
                blackRemainingSeconds--;
            }
            clockView.setClockTime(whiteRemainingSeconds, blackRemainingSeconds);
        }
        engineView.setMoveTime(++elapsedMoveTime);
        if (gameModel.isThreeTimeControls() || config.isAutoflag()) {
            if (isTimeOver()) {
                statusAware.onGameStatusChanged(Status.TIMEOUT);
                clockView.timeOver(colorToMove);
            }
        }
    }

    private boolean isTimeOver() {
        if (gameModel.isFriendly()) {
            return false;
        }
        return !hasAvailableTime();
    }

    private boolean hasAvailableTime() {
        if (colorToMove.isWhite()) {
            return whiteRemainingSeconds > 0;
        } else {
            return blackRemainingSeconds > 0;
        }
    }

    protected synchronized void setUpForGame() {
        logger.debug("Now setup the clock");
        if (isStarted()) {
            throw new BusinessException("Clock already set up, cannot start it again!");
        }
        statusAware = context.getCurrentGame();
        gameModel = config.getSelectedGameModel();
        if (gameModel.isOneTimeControl()) {
            whiteRemainingSeconds = blackRemainingSeconds = gameModel.getThirdTC().getIncrementedTotalTime();
        } else if (gameModel.isThreeTimeControls()) {
            whiteRemainingSeconds = blackRemainingSeconds = gameModel.getFirstTC().getIncrementedTotalTime();
        }
        startGameTime = startMoveTime = System.currentTimeMillis();
    }

    protected synchronized void pause() {
        if (isPlaying() && !paused) {
            timer.cancel();
            paused = true;
        } else {
            throw new BusinessException("Game already paused");
        }
    }

    protected synchronized void resume() {
        if (isPlaying() && paused) {
            timer.schedule(timerTask, 0, 1000);
            paused = false;
        } else {
            throw new BusinessException("Game not paused or not playing, cannot resume!");
        }
    }

    protected synchronized void stop() {
        if (isPlaying()) {
            endGameTime = System.currentTimeMillis();
            timer.cancel();
            timer.purge();
        } else {
            throw new BusinessException("Game already stopped");
        }
    }

    public long getStartGameTime() {
        return startGameTime;
    }

    public long getEndGameTime() {
        return endGameTime;
    }

    public long getWhiteElapsedSeconds() {
        return whiteElapsedSeconds;
    }

    public long getWhiteRemainingTime() {
        return whiteRemainingSeconds * 1000;
    }

    public long getBlackRemainingTime() {
        return blackRemainingSeconds * 1000;
    }

    public long getBlackElapsedSeconds() {
        return blackElapsedSeconds;
    }

    public boolean isStarted() {
        return startGameTime != null;
    }

    public boolean isEnded() {
        return endGameTime != null;
    }

    public boolean isPlaying() {
        return startGameTime != null && endGameTime == null;
    }

    public ChessColor getColorToMove() {
        return colorToMove;
    }

    public void setColorToMove(ChessColor colorToMove) {
        Validate.notNull(colorToMove, "null colorToMove");
        this.colorToMove = colorToMove;
    }

    /**
	 * Called after a move has been made on board.
	 */
    public synchronized void onMove(int plyNumber) {
        if (isPlaying() && !paused) {
            timerTask.cancel();
            timerTask = null;
            endMoveTime = System.currentTimeMillis();
            moveTimeGap = Math.min(endMoveTime - startMoveTime - elapsedMoveTime * 1000, 0);
            if (gameModel.isOneTimeControl()) {
                incrementRemainingTime(gameModel.getThirdTC());
            } else if (gameModel.isThreeTimeControls()) {
                if (plyNumber <= gameModel.getFirstTC().getMoveNumber()) {
                    incrementRemainingTime(gameModel.getFirstTC());
                } else if (plyNumber <= gameModel.getSecondTC().getMoveNumber()) {
                    if (timeControlIndex == 1) {
                        timeControlIndex++;
                        computeRemainingTime(gameModel.getSecondTC());
                    }
                } else {
                    if (timeControlIndex == 2) {
                        timeControlIndex++;
                        computeRemainingTime(gameModel.getThirdTC());
                    }
                }
            }
            colorToMove = colorToMove.opposite();
            elapsedMoveTime = 0;
        } else {
            logger.warn("Cannot move when inactive game");
        }
    }

    @Override
    public synchronized void onPlayerReady() {
        startMoveTime = System.currentTimeMillis();
        if (timerTask == null) {
            timer.schedule((timerTask = new ClockTimer()), 1000 - moveTimeGap, 1000);
        }
    }

    private void incrementRemainingTime(TimeControlModel timeControl) {
        if (colorToMove.isWhite()) {
            whiteRemainingSeconds += timeControl.getGainPerMove();
        } else {
            blackRemainingSeconds += timeControl.getGainPerMove();
        }
    }

    private void computeRemainingTime(TimeControlModel timeControl) {
        whiteRemainingSeconds += timeControl.getIncrementedTotalTime();
        blackRemainingSeconds += timeControl.getIncrementedTotalTime();
    }

    @Override
    public String toString() {
        return "Clock [startGameTime=" + startGameTime + ", endGameTime=" + endGameTime + ", whiteElapsedSeconds=" + whiteElapsedSeconds + ", blackElapsedSeconds=" + blackElapsedSeconds + ", whiteRemainingSeconds=" + whiteRemainingSeconds + ", blackRemainingSeconds=" + blackRemainingSeconds + ", elapsedMoveTime=" + elapsedMoveTime + ", startMoveTime=" + startMoveTime + ", endMoveTime=" + endMoveTime + ", moveTimeGap=" + moveTimeGap + ", paused=" + paused + ", timeControlIndex=" + timeControlIndex + ", gameModel=" + gameModel + ", colorToMove=" + colorToMove + "]";
    }
}
