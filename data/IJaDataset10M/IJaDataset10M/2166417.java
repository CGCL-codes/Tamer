package de.tabacha.cgo.strategy;

import de.tabacha.cgo.*;

/** Sample implementation of NoRecursionTemplate.
   <br />$Id: SimpleEngine.java,v 1.2 2006/01/11 23:48:21 ufkub Exp $
   @author michael@tabacha.de
   @author $Author: ufkub $
   @version $Revision: 1.2 $
   @threadsafe false
*/
public class SimpleEngine extends NoRecursionTemplate {

    private int minJumpRow;

    private Jump bestOffensiveJump;

    private int bestOffensiveJumpCount;

    private int maxJumpRow;

    private Jump bestDefensiveJump;

    private int bestDefensiveJumpCount;

    private int maxMinJumpRow;

    private Put bestOffensivePut;

    private int bestOffensivePutCount;

    private int maxPutRow;

    private Put bestDefensivePut;

    private int bestDefensivePutCount;

    private int maxMinPutRow;

    /** Constructor.
     */
    public SimpleEngine() {
    }

    public String getVersion() {
        return "2";
    }

    public String getAuthor() {
        return "michael@tabacha.de";
    }

    protected void initNewMoveSearch() {
        bestOffensiveJump = bestDefensiveJump = null;
        bestOffensiveJumpCount = bestDefensiveJumpCount = 0;
        bestOffensivePut = bestDefensivePut = null;
        bestOffensivePutCount = bestDefensivePutCount = 0;
        maxJumpRow = maxMinJumpRow = maxPutRow = maxMinPutRow = -50;
        minJumpRow = 50;
    }

    protected boolean jumpFound(Jump j, int min, int max) {
        int brow = board().getBall().row();
        if (min < minJumpRow) minJumpRow = min;
        if (brow > maxJumpRow) {
            maxJumpRow = brow;
            bestOffensiveJump = j;
            bestOffensiveJumpCount = 1;
        } else if (brow == maxJumpRow && (random().nextInt(++bestOffensiveJumpCount) == 0)) bestOffensiveJump = j;
        if (min > maxMinJumpRow) {
            maxMinJumpRow = brow;
            bestDefensiveJump = j;
            bestDefensiveJumpCount = 1;
        } else if (min == maxMinJumpRow && (random().nextInt(++bestDefensiveJumpCount) == 0)) bestDefensiveJump = j;
        return false;
    }

    protected boolean putFound(Put p) {
        int[] minMax = getMinMaxRow();
        if (minMax[1] > maxPutRow) {
            maxPutRow = minMax[1];
            bestOffensivePut = p;
            bestOffensivePutCount = 1;
        } else if (minMax[1] == maxPutRow && (random().nextInt(++bestOffensivePutCount) == 0)) bestOffensivePut = p;
        if (minMax[0] > maxMinPutRow) {
            maxMinPutRow = minMax[0];
            bestDefensivePut = p;
        } else if (minMax[0] == maxMinPutRow && (random().nextInt(++bestDefensivePutCount) == 0)) bestDefensivePut = p;
        return false;
    }

    protected Move bestMove() {
        int brow = board().getBall().row();
        if (maxJumpRow >= 18 || maxJumpRow - brow > 4) return bestOffensiveJump;
        if (minJumpRow <= 0 || brow - minJumpRow > 4) {
            if (maxMinJumpRow > maxMinPutRow) return bestDefensiveJump; else if (maxMinPutRow > minJumpRow) return bestDefensivePut; else return bestOffensivePut;
        }
        return bestOffensivePut;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer("Computer (advanced): ");
        buf.append("bestOffensiveJump =").append(bestOffensiveJump).append(", bestDefensiveJump =").append(bestDefensiveJump).append(", bestOffensivePut =").append(bestOffensivePut).append(", bestDefensivePut =").append(bestDefensivePut);
        return buf.toString();
    }
}
