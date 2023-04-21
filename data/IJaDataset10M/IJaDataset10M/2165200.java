package de.devisnik.shifting.impl;

import java.util.Random;
import org.eclipse.swt.widgets.Display;
import de.devisnik.shifting.IGame;
import de.devisnik.shifting.IPuzzle;

public class ReverseAutoGame implements IGame {

    private Random itsRandom = new Random();

    private Puzzle puzzle;

    private IMove[] moves;

    private MoveFactory moveFactory;

    private final Display display;

    private boolean isDone;

    private Runnable runnable;

    private final int gridX;

    private final int gridY;

    private final int scrambleCount;

    private final int thinkingTime;

    public ReverseAutoGame(Display display, int gridX, int gridY, int scrambleCount, int thinkingTime) {
        this.display = display;
        this.gridX = gridX;
        this.gridY = gridY;
        this.scrambleCount = scrambleCount;
        this.thinkingTime = thinkingTime;
    }

    public void init() {
        puzzle = new Puzzle(gridX, gridY);
        moveFactory = new MoveFactory(puzzle);
        moves = scrambleGame(moveFactory, scrambleCount);
        runnable = new Runnable() {

            int index = moves.length - 1;

            public void run() {
                moveFactory.getInverse(moves[index]).execute();
                if (index == 0) {
                    isDone = true;
                } else {
                    index--;
                }
            }
        };
        isDone = false;
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

    private IMove[] scrambleGame(final MoveFactory factory, int numberOfMoves) {
        IMove[] possibleMoves = getPossibleMoves(factory);
        final IMove[] moves = new IMove[numberOfMoves];
        for (int index = 0; index < moves.length; ) {
            IMove holeMover = getRandomMove(possibleMoves);
            if (index > 0) {
                if (holeMover == factory.getInverse(moves[index - 1])) {
                    continue;
                }
            }
            if (holeMover.execute()) {
                moves[index] = holeMover;
                index++;
            }
        }
        return moves;
    }

    private IMove getRandomMove(IMove[] moves) {
        int randInt = itsRandom.nextInt(moves.length);
        return moves[randInt];
    }
}
