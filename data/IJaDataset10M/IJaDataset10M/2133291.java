package net.innig.macker.example.modularity.player.impl;

import net.innig.macker.example.modularity.player.*;
import net.innig.macker.example.modularity.game.*;
import java.util.*;

public class RandomPlayer implements Player {

    public Move nextMove(Set legalMoves, Move otherPreviousMove) {
        List legalMovesL = new ArrayList(legalMoves);
        return (Move) legalMovesL.get(rand.nextInt(legalMovesL.size()));
    }

    private Random rand = new Random(System.currentTimeMillis() + hashCode());
}
