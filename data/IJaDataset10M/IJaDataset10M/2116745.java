package de.devisnik.shifting.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.eclipse.swt.widgets.Display;
import de.devisnik.shifting.IGame;
import de.devisnik.shifting.IPuzzle;

public class AutoGame implements IGame {

    private Random itsRandom = new Random();

    private Puzzle puzzle;

    private MoveFactory moveFactory;

    private final Display display;

    private boolean isDone;

    private Runnable runnable;

    private final int gridX;

    private final int gridY;

    private final int thinkingTime;

    public AutoGame(Display display, int gridX, int gridY, int thinkingTime) {
        this.display = display;
        this.gridX = gridX;
        this.gridY = gridY;
        this.thinkingTime = thinkingTime;
    }

    public void init() {
        puzzle = new Puzzle(gridX, gridY);
        moveFactory = new MoveFactory(puzzle);
        runnable = new Runnable() {

            IMove lastMove = null;

            public void run() {
                boolean success = false;
                IMove candidate = null;
                while (!success) {
                    candidate = getNextMoveCandidate(lastMove);
                    success = candidate.execute();
                }
                lastMove = candidate;
            }
        };
    }

    private IMove getNextMoveCandidate(IMove move) {
        IMove[] possibleMoves = getPossibleMoves(moveFactory);
        List possibleList = Arrays.asList(possibleMoves);
        if (move == null) {
            return getRandomMove(possibleMoves);
        }
        List candidates = new ArrayList();
        candidates.addAll(possibleList);
        candidates.remove(moveFactory.getInverse(move));
        return getRandomMove((IMove[]) candidates.toArray(new IMove[candidates.size()]));
    }

    public IPuzzle getPuzzle() {
        return puzzle;
    }

    public boolean isDone() {
        return isDone;
    }

    public void executeNextMove() {
        display.timerExec(thinkingTime, runnable);
    }

    private IMove[] getPossibleMoves(MoveFactory factory) {
        return new IMove[] { factory.getLeft(), factory.getRight(), factory.getUp(), factory.getDown() };
    }

    private IMove getRandomMove(IMove[] moves) {
        int randInt = itsRandom.nextInt(moves.length);
        return moves[randInt];
    }
}
