package org.icehockeymanager.ihm.game.match.textengine;

import java.util.*;
import org.icehockeymanager.ihm.game.league.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.match.textengine.data.*;
import org.icehockeymanager.ihm.game.match.textengine.plays.*;
import org.icehockeymanager.ihm.game.team.*;

public class TextMatch extends Match {

    private static final long serialVersionUID = -8588151543780188304L;

    /** Internal flag if match has been initialized */
    private boolean initialized = false;

    /** Internal helper variable */
    private static final int TIME_GAME_FINISHED_OT = 3;

    /** Internal helper variable */
    private static final int TIME_GAME_FINISHED = 2;

    /** Internal helper variable */
    private static final int TIME_PERIOD_FINISHED = 1;

    /** Internal helper variable */
    private static final int TIME_NO_BREAK = 0;

    /** How many seconds does one situation last */
    private static final int secondsPerScene = 20;

    /** Internal match snap shot containing current situation layout */
    private MatchSnapshot matchSnapshot = new MatchSnapshot();

    /** Our devoted match listeners :-) */
    protected Vector<TextMatchListener> listener;

    public TextMatch(LeagueElement leagueElement, Team teamHome, Team teamAway, Rules rules) {
        super(leagueElement, teamHome, teamAway, rules);
    }

    /**
   * Returns the current MatchSnapshot
   * 
   * @return Match snapshot
   */
    public MatchSnapshot getCurrentMatchSnapshot() {
        return matchSnapshot;
    }

    public int getSecondsPlayed() {
        return getCurrentMatchSnapshot().currentSeconds;
    }

    public void lateInitGame() {
        if (!initialized) {
            homeTeam.getArena().preGame(this);
            spectators = homeTeam.getArena().getTotalSpectators();
            homeTeam.resetMatchEnergy();
            awayTeam.resetMatchEnergy();
            matchSnapshot.initGame(rules);
            this.initialized = true;
        }
    }

    /** Sets the game to overtime */
    private void goToOvertime() {
        matchSnapshot.goToOvertime(rules);
        overtimePlayed = true;
    }

    /** Finishes the game */
    private void finishGame() {
        homeTeam.getArena().postGame();
        scoreSheet.finish();
        matchFinished = true;
    }

    /** Initializes a new scene */
    private void initScene() {
        matchSnapshot.setCurrentHomeBlock(homeTeam.getTactics().getNextBlock(this));
        matchSnapshot.setCurrentAwayBlock(awayTeam.getTactics().getNextBlock(this));
        matchSnapshot.initScene();
    }

    /** Finishes a scene */
    public void finishScene() {
        matchSnapshot.finishScene();
    }

    /**
   * Starts a new scene and plays it on the ice. Listeners to the match will be
   * informed about what is happening on the ice (n events). And how/why the
   * scene has been fnished (1 event).
   */
    public synchronized void moveOn() {
        lateInitGame();
        initScene();
        if (!matchSnapshot.currentPeriodStarted) {
            matchSnapshot.startCurrentPeriod();
            if (this.matchSnapshot.currentState == MatchSnapshot.MATCH_STATE_REGULAR) {
                fireRegularPeriodStarted();
            } else {
                fireOvertimePeriodStarted();
            }
        }
        boolean breaker = false;
        while (!breaker) {
            breaker = isBreak();
            if (!breaker) {
                if (!matchSnapshot.currentPuckInPlay) {
                    matchSnapshot.currentFaceOffData = new MatchDataFaceOff(matchSnapshot.currentSeconds);
                    Plays.playFaceOff(this);
                    this.fireFaceOff(matchSnapshot.currentFaceOffData);
                }
                matchSnapshot.currentSituationData = new MatchDataSituation();
                breaker = Plays.playNextSituation(this);
                matchSnapshot.currentSeconds += secondsPerScene;
                this.fireSituation(matchSnapshot.currentSituationData);
            }
        }
        finishScene();
        int breakId = checkTimeAndStatus(true);
        switch(breakId) {
            case TIME_GAME_FINISHED_OT:
                {
                    this.fireGoToOvertime();
                    return;
                }
            case TIME_GAME_FINISHED:
                {
                    this.fireGameFinished();
                    return;
                }
            case TIME_PERIOD_FINISHED:
                {
                    if (this.matchSnapshot.currentState == MatchSnapshot.MATCH_STATE_REGULAR) {
                        this.fireRegularPeriodFinished();
                        return;
                    } else {
                        this.fireOvertimePeriodFinished();
                        return;
                    }
                }
            default:
                {
                    if (matchSnapshot.scoreChange) {
                        this.fireScoreChange();
                        return;
                    } else {
                        this.fireSceneFinished();
                        return;
                    }
                }
        }
    }

    /**
   * Checks the time and possible breaks (period, game etc.) and returns it's
   * findings.
   * 
   * @param updateStats
   *          Defines, if the current snapshot shall be updated
   * @return Integer with "TIME_*" status of the current time.
   */
    private int checkTimeAndStatus(boolean updateStats) {
        if (this.isFinished()) {
            return TIME_GAME_FINISHED;
        }
        if (matchSnapshot.currentState == MatchSnapshot.MATCH_STATE_REGULAR) {
            if (matchSnapshot.currentSeconds >= matchSnapshot.currentNextBreak) {
                if (matchSnapshot.currentPeriod == (rules.numberOfPeriods() - 1)) {
                    if (isGameTied()) {
                        if (rules.playOvertime()) {
                            if (updateStats) {
                                this.goToOvertime();
                            }
                            return TIME_GAME_FINISHED_OT;
                        } else {
                            if (updateStats) {
                                this.finishGame();
                            }
                            return TIME_GAME_FINISHED;
                        }
                    } else {
                        if (updateStats) {
                            this.finishGame();
                        }
                        return TIME_GAME_FINISHED;
                    }
                } else {
                    if (updateStats) {
                        matchSnapshot.finishPeriod(rules);
                    }
                    return TIME_PERIOD_FINISHED;
                }
            }
        } else if (matchSnapshot.currentState == MatchSnapshot.MATCH_STATE_OVERTIME) {
            if (matchSnapshot.currentSeconds >= matchSnapshot.currentNextBreak) {
                if (rules.contignousOvertime()) {
                    if (updateStats) {
                        matchSnapshot.finishPeriod(rules);
                    }
                    return TIME_PERIOD_FINISHED;
                } else {
                    if (updateStats) {
                        this.finishGame();
                    }
                    return TIME_GAME_FINISHED;
                }
            }
        }
        return TIME_NO_BREAK;
    }

    /** Checks if the game has to be suspended 
   * @return true if break */
    private boolean isBreak() {
        return checkTimeAndStatus(false) != TIME_NO_BREAK;
    }

    /**
   * A goal has been scored. Create the Goal in the sheet, update snapshot
   */
    public void goalScored() {
        MatchDataGoal goal = new MatchDataGoal(matchSnapshot.currentSeconds, matchSnapshot.currentPuckHoldingBlock.getTeam(), matchSnapshot.getOpponentsBlock().getTeam(), matchSnapshot.currentPuckHolder, matchSnapshot.currentPuckChain[0], matchSnapshot.currentPuckChain[1]);
        scoreSheet.addGoal(goal);
        matchSnapshot.scoreChange = true;
        if (matchSnapshot.currentState == MatchSnapshot.MATCH_STATE_OVERTIME) {
            this.finishGame();
        }
        matchSnapshot.initField();
    }

    /**
   * Adds Matchlistener to the Match
   * 
   * @param m
   *          Listener
   */
    public synchronized void addMatchListener(TextMatchListener m) {
        if (listener == null) {
            listener = new Vector<TextMatchListener>(5, 5);
        }
        if (!listener.contains(m)) {
            listener.addElement((TextMatchListener) m);
        }
    }

    /**
   * Remove all listeners on this match (quite unpopular)
   * 
   */
    public synchronized void removeAllMatchListeners() {
        this.listener = null;
    }

    protected synchronized void fireFaceOff(MatchDataFaceOff ev) {
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).faceOff(ev);
            }
        }
    }

    protected synchronized void fireSituation(MatchDataSituation ev) {
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).situation(ev);
            }
        }
    }

    protected final synchronized void fireRegularPeriodStarted() {
        super.fireRegularPeriodStarted();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).regularPeriodStarted();
            }
        }
    }

    protected final synchronized void fireOvertimePeriodStarted() {
        super.fireOvertimePeriodStarted();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).overtimePeriodStarted();
            }
        }
    }

    protected final synchronized void fireGoToOvertime() {
        super.fireGoToOvertime();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).goToOvertime();
            }
        }
    }

    protected final synchronized void fireGameFinished() {
        super.fireGameFinished();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).gameFinished();
            }
        }
    }

    protected final synchronized void fireRegularPeriodFinished() {
        super.fireRegularPeriodFinished();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).regularPeriodFinished();
            }
        }
    }

    protected final synchronized void fireOvertimePeriodFinished() {
        super.fireOvertimePeriodFinished();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).overtimePeriodFinished();
            }
        }
    }

    protected final synchronized void fireScoreChange() {
        super.fireScoreChange();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).scoreChange();
            }
        }
    }

    protected final synchronized void fireSceneFinished() {
        super.fireSceneFinished();
        if (listener != null) {
            for (Enumeration<TextMatchListener> e = listener.elements(); e.hasMoreElements(); ) {
                (e.nextElement()).sceneFinished();
            }
        }
    }
}
