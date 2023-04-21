package AccuGo.util;

public class Score {

    double blackCount;

    double whiteCount;

    double handicap;

    double difference;

    CellState winner;

    double territoryWhite;

    double territoryBlack;

    public Score(double handicap) {
        winner = CellState.EMPTY;
        blackCount = -handicap;
        whiteCount = 0;
        territoryWhite = 0;
        territoryBlack = 0;
        difference = blackCount - whiteCount;
    }

    public Score(double handicap, CellState state, int newScore) {
        winner = CellState.EMPTY;
        blackCount = -handicap;
        whiteCount = 0;
        territoryWhite = 0;
        territoryBlack = 0;
        difference = blackCount - whiteCount;
        setScore(newScore, state);
    }

    public void increaseScore(CellState stoneType) {
        if (stoneType == CellState.BLACK) {
            blackCount++;
            territoryBlack++;
        } else if (stoneType == CellState.WHITE) {
            whiteCount++;
            territoryWhite++;
        }
    }

    public void decreaseScore(CellState stoneType) {
        if (stoneType == CellState.BLACK) {
            blackCount--;
        } else if (stoneType == CellState.WHITE) {
            whiteCount--;
        }
    }

    public double getBlackPoints() {
        return blackCount;
    }

    public double getWhitePoints() {
        return whiteCount;
    }

    public double getWhiteTerritoy() {
        return territoryWhite;
    }

    public double getBlackTerritoy() {
        return territoryBlack;
    }

    public double getDifference() {
        difference = blackCount - whiteCount;
        difference = Math.abs(difference);
        return difference;
    }

    public double getHandicap() {
        return handicap;
    }

    public void setScore(double newScore, CellState state) {
        if (state == CellState.BLACK) {
            blackCount = newScore;
        } else if (state == CellState.WHITE) {
            whiteCount = newScore;
        }
    }

    public CellState getWinner() {
        if (blackCount > whiteCount) {
            winner = CellState.BLACK;
        } else {
            winner = CellState.WHITE;
        }
        return winner;
    }
}
