package org.icehockeymanager.ihm.game.league.std;

import org.icehockeymanager.ihm.game.league.*;

/**
 * Rules class contains rules for match/scoreboard base class for each rule
 * 
 * @author Arik Dasen
 * @created December, 2001
 */
public class StdRegularSeasonRules extends Rules {

    /**
   * Comment for <code>serialVersionUID</code>
   */
    private static final long serialVersionUID = 3257009869076051768L;

    /**
   * Is winner needed in a match
   * 
   * @return Is winner needed in a match
   */
    public boolean needsWinner() {
        return false;
    }

    /**
   * Returns the number of periods.
   * 
   * @return Number of periods
   */
    public int numberOfPeriods() {
        return 3;
    }

    /**
   * Minutes per period
   * 
   * @return Minutes per period
   */
    public int minutesPerPeriod() {
        return 20;
    }

    /**
   * Will overtime be played
   * 
   * @return Will overtime be played
   */
    public boolean playOvertime() {
        return false;
    }

    /**
   * Minutes per overtime
   * 
   * @return Minutes per overtime
   */
    public int minutesPerOvertime() {
        return 5;
    }

    /**
   * Will suddenDeath be played
   * 
   * @return If suddenDeath will be played
   */
    public boolean suddenDeath() {
        return false;
    }

    /**
   * Play multiple overtimes until we have a winner
   * 
   * @return Play multiple overtimes until we have a winner
   */
    public boolean contignousOvertime() {
        return false;
    }

    /**
   * Players per Overtime
   * 
   * @return Players per Overtime
   */
    public int playersPerOvertime() {
        return 0;
    }

    /**
   * Play shootout
   * 
   * @return If shootout will be played
   */
    public boolean playShootout() {
        return false;
    }

    /**
   * Points for win
   * 
   * @return Points for win
   */
    public int pointsForWin() {
        return 2;
    }

    /**
   * Points for loss
   * 
   * @return Points for loss
   */
    public int pointsForLoss() {
        return 0;
    }

    /**
   * Points for tied
   * 
   * @return Points for tied
   */
    public int pointsForTied() {
        return 1;
    }

    /**
   * Points for overtime win
   * 
   * @return Points for overtime win
   */
    public int pointsForOvertimeWin() {
        return 0;
    }

    /**
   * Points for overtime loss
   * 
   * @return Points for overtime loss
   */
    public int pointsForOvertimeLoss() {
        return 0;
    }

    /** Constructor for the StdRegularSeasonRules object */
    public StdRegularSeasonRules() {
    }
}
