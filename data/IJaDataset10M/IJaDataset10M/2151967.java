package saf.moves;

import saf.*;

public abstract class StepTowards extends BotMove {

    protected int positionsPerTick;

    public StepTowards(Bot b) {
        super(b);
    }

    public void doMove() {
        if (!isComplete()) {
            stepTowards();
            isComplete();
        }
    }

    private boolean isComplete() {
        if (bot.isOpponentNear()) {
            completed = true;
            return true;
        } else {
            return false;
        }
    }

    protected void stepTowards() {
        int botPosition = bot.getPosition();
        int opponentPosition = bot.getOpponentPosition();
        int difPosition = botPosition - opponentPosition;
        int neededPositions = Math.abs(difPosition);
        int actualPositionsToMove = positionsPerTick;
        if (neededPositions < positionsPerTick) {
            actualPositionsToMove = neededPositions;
        }
        if (difPosition < 0) {
            bot.moveEast(actualPositionsToMove);
        } else {
            bot.moveWest(actualPositionsToMove);
        }
    }
}
