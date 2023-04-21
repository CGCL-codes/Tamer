package free.freechess;

import java.util.StringTokenizer;
import free.util.Struct;

/**
 * A container for delta board information. More information is available by
 * issuing "help iv_compressmoves" on freechess.org
 */
public class DeltaBoardStruct extends Struct {

    /**
   * Creates a new DeltaBoardStruct with the specified arguments.
   *
   * @param gameNumber The game number.
   * @param pliesPlayedCount The amount of half-moves (plies) already played.
   * @param moveAlgebraic The move in algebraic format.
   * @param moveSmith The move in smith-warren format.
   * @param takenTime The amount of time taken to make the move, in
   * milliseconds.
   * @param remainingTime The amount of time remaining, in milliseconds.
   */
    public DeltaBoardStruct(final int gameNumber, final int pliesPlayedCount, final String moveAlgebraic, final String moveSmith, final int takenTime, final int remainingTime) {
        if (pliesPlayedCount < 0) throw new IllegalArgumentException("The amount of plies played (" + pliesPlayedCount + ") may not be negative");
        if (moveAlgebraic == null) throw new IllegalArgumentException("The move in algebraic format may not be null");
        if (moveSmith == null) throw new IllegalArgumentException("The move in Smith-Warren format may not be null");
        if (takenTime < 0) throw new IllegalArgumentException("The time taken to make the move (" + takenTime + ") may not be negative");
        setIntegerProperty("GameNumber", gameNumber);
        setIntegerProperty("PliesPlayedCount", pliesPlayedCount);
        setStringProperty("MoveAlgebraic", moveAlgebraic);
        setStringProperty("MoveSmith", moveSmith);
        setIntegerProperty("TakenTime", takenTime);
        setIntegerProperty("RemainingTime", remainingTime);
    }

    /**
   * Parses the specified delta board line and returns a corresponding
   * DeltaBoardStruct board.
   */
    public static DeltaBoardStruct parseDeltaBoardLine(final String line) {
        final StringTokenizer tokens = new StringTokenizer(line, " ");
        if (!tokens.nextToken().equals("<d1>")) throw new IllegalArgumentException("Missing \"<d1>\" identifier");
        final int gameNumber = Integer.parseInt(tokens.nextToken());
        final int pliesPlayedCount = Integer.parseInt(tokens.nextToken());
        final String moveAlgebraic = tokens.nextToken();
        final String moveSmith = tokens.nextToken();
        final int takenTime = Integer.parseInt(tokens.nextToken());
        final int remainingTime = Integer.parseInt(tokens.nextToken());
        return new DeltaBoardStruct(gameNumber, pliesPlayedCount, moveAlgebraic, moveSmith, takenTime, remainingTime);
    }

    /**
   * Returns the game number.
   */
    public int getGameNumber() {
        return getIntegerProperty("GameNumber");
    }

    /**
   * Returns the amount of half-moves played in the game.
   */
    public int getPliesPlayedCount() {
        return getIntegerProperty("PliesPlayedCount");
    }

    /**
   * Returns the move in algebraic format.
   */
    public String getMoveAlgebraic() {
        return getStringProperty("MoveAlgebraic");
    }

    /**
   * Returns the move in Smith-Warren format.
   */
    public String getMoveSmith() {
        return getStringProperty("MoveSmith");
    }

    /**
   * Returns the amount of time taken to make the move, in milliseconds.
   */
    public int getTakenTime() {
        return getIntegerProperty("TakenTime");
    }

    /**
   * Returns the amount of time remaining, in milliseconds.
   */
    public int getRemainingTime() {
        return getIntegerProperty("RemainingTime");
    }
}
