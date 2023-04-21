package org.eyrene.jgames.core.ai;

import java.util.*;
import org.eyrene.jgames.core.GameRules;
import org.eyrene.jgames.core.Move;

/**
 * <p>Title: MTDf.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: eyrene</p>
 * @author Francesco Vadicamo
 * @version 1.0
 */
public class IterativeMTDf extends MTDf {

    /**
     * Costruttore con passaggio delle regole del gioco
     * 
     * @param gameRules regole del gioco
     */
    public IterativeMTDf(GameRules gameRules) {
        super(gameRules);
    }

    protected void search() throws IllegalSearchStateException {
        currStatus = gameAnalyzer.code(initStatus);
        max = initStatus.getNextPlayer();
        min = gameAnalyzer.nextPlayer(max);
        this.solution = null;
        this.TT = new HashMap(65536, 0.79f);
        float firstguess = 0, bestvalue, g;
        LinkedList new_operators = (LinkedList) operators(currStatus);
        LinkedList old_operators;
        ListIterator iter;
        Move currSolution, op;
        for (int depth = 1; depth <= maxDepth; depth++) {
            TT.clear();
            currSolution = null;
            old_operators = new_operators;
            new_operators = new LinkedList();
            iter = old_operators.listIterator();
            bestvalue = Float.NEGATIVE_INFINITY;
            while (iter.hasNext()) {
                checkState();
                op = (Move) iter.next();
                if (currSolution == null) currSolution = op;
                g = mtdf(apply(op, currStatus), depth - 1, firstguess);
                if (g > bestvalue) {
                    bestvalue = g;
                    firstguess = g;
                    currSolution = op;
                    new_operators.addFirst(op);
                } else new_operators.addLast(op);
            }
            this.solution = currSolution;
        }
    }
}
