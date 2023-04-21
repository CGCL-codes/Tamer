package org.icehockeymanager.ihm.game.match.textengine.data;

import java.io.*;
import org.icehockeymanager.ihm.game.match.*;
import org.icehockeymanager.ihm.game.player.*;

/**
 * MatchDataFaceOff based on MatchData contains all informations about a
 * finished faceOff.
 * 
 * @author Bernhard von Gunten
 * @created January 2005
 */
public class MatchDataFaceOff extends MatchData implements Serializable {

    /**
   * Comment for <code>serialVersionUID</code>
   */
    private static final long serialVersionUID = 3257291339757271347L;

    /** Player A */
    private Player playerA;

    /** Player B */
    private Player playerB;

    /** Puckholder after the faceOff */
    private Player puckHolder;

    /** Flag if player A has won the face off */
    private boolean winnerA;

    /** Section of the face off */
    private int section;

    /**
   * Constructs MatchDataFaceOff with the time
   * 
   * @param time
   */
    public MatchDataFaceOff(int time) {
        super(time);
    }

    /** Returns player A 
   * @return Player */
    public Player getPlayerA() {
        return playerA;
    }

    /** Sets player A 
   * @param playerA */
    public void setPlayerA(Player playerA) {
        this.playerA = playerA;
    }

    /** Returns player B 
   * @return Player*/
    public Player getPlayerB() {
        return playerB;
    }

    /** Sets player B 
   * @param playerB */
    public void setPlayerB(Player playerB) {
        this.playerB = playerB;
    }

    /** Returns the puck holder 
   * @return PuckHolder */
    public Player getPuckHolder() {
        return puckHolder;
    }

    /** Sets the puck holder 
   * @param puckHolder */
    public void setPuckHolder(Player puckHolder) {
        this.puckHolder = puckHolder;
    }

    /** Returns the section 
   * @return Section */
    public int getSection() {
        return section;
    }

    /** Sets the section 
   * @param section */
    public void setSection(int section) {
        this.section = section;
    }

    /** Returns if player A is winner 
   * @return True if A is winner */
    public boolean isAWinner() {
        return winnerA;
    }

    /** Sets if player A is winner 
   * @param winnerA */
    public void setWinnerA(boolean winnerA) {
        this.winnerA = winnerA;
    }
}
