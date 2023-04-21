package Jogo;

public class Board {

    int sizeBoardX = 14, sizeBoardY = 9;

    boolean spot[][] = new boolean[sizeBoardX][sizeBoardY];

    public Board() {
        for (int i = 0; i < sizeBoardX; i++) {
            for (int j = 0; j < sizeBoardY; j++) {
                spot[i][j] = true;
            }
        }
    }

    public void setSpotBoard(int fromX, int fromY, int toX, int toY) {
        spot[fromX][fromY] = false;
        spot[toX][toY] = true;
    }

    private boolean safetyZone(int lin, int col) {
        boolean safetyZone = false;
        int i;
        if (col != 1 && col != 2 && col != 3 && col != sizeBoardX - 2 && col != sizeBoardX - 3 && col != sizeBoardX - 4) {
            safetyZone = false;
        } else {
            if (col == 1 || col == sizeBoardX - 2) {
                for (i = 3; i > 2 && i < 6; i++) {
                    if (lin != i) {
                        safetyZone = false;
                    } else {
                        safetyZone = true;
                        return safetyZone;
                    }
                }
            } else {
                if (col == 2 || col == sizeBoardX - 3) {
                    for (i = 2; i > 1 && i < 7; i++) {
                        if (lin != i) {
                            safetyZone = false;
                        } else {
                            safetyZone = true;
                            return safetyZone;
                        }
                    }
                } else {
                    if (col == 3 || col == sizeBoardX - 4) {
                        for (i = 1; i > 0 && i < 8; i++) {
                            if (lin != i) {
                                safetyZone = false;
                            } else {
                                safetyZone = true;
                                return safetyZone;
                            }
                        }
                    }
                }
            }
        }
        return safetyZone;
    }

    private boolean scoringZone(int lin, int col) {
        boolean scoringZone = false;
        if (col == 0 || col == sizeBoardX - 1) {
            scoringZone = true;
        } else scoringZone = false;
        return scoringZone;
    }

    public String getZone(int lin, int col) {
        if (scoringZone(lin, col)) {
            return "ScoringZone";
        } else {
            if (safetyZone(lin, col)) {
                return "SafetyZone";
            } else return "RegularZone";
        }
    }
}
