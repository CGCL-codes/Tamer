package de.tabacha.cgo.strategy;

import de.tabacha.cgo.*;

public class MikeGo extends AbstractEngine {

    private static final int MAXSUCHTIEFE = 49;

    /** Koeffizient f�r schlechteste erreichbare Zeile. */
    private static final int A = 1;

    /** Koeffizient f�r beste erreichbare Zeile. */
    private static final int B = 1;

    /** Koeffizient f�r Ball-Zeile. */
    private static final int C = 0;

    /** Ziehen-Malus. Wird bei Stellungsbewertung abgezogen, falls Ziehen-Zug (Initiative-Verlust). */
    private static final int ZM = 5;

    /** Abzug, falls bei Setzen-Zug Sprung in Gegenrichtung m�glich. */
    private static final int SCHIEFER = -3;

    /** Bei Setzen-Z�gen wird das Trapez erst sp�ter gemessen; daher mu� Ergebnis verkleinert werden. */
    private static final double VSB_BOING_FACTOR = 0.25;

    /**  Dieser Wert wird bei bewerte_Stellung() abgezogen, falls der Gegner bis kurz vors Ziel ziehen kann. */
    private static final int NEAR_LOSS = 5;

    private byte[][] board;

    private boolean whoseTurn;

    private byte[] bestZug;

    private int bestZugLaenge;

    private int bestPutCol, bestPutRow;

    private int bestWert, bestVsb;

    /** Anzahl der besten M�glichkeiten, f�r zuf�lliges Ausw�hlen. */
    private int anzBest;

    /** Ist es sinnvoll, auf dieses Feld einen P�ppel zu setzen? */
    private boolean[][] gutzug;

    private int min, max;

    private int ags;

    /** Ball coordinates. */
    private int bcol, brow;

    private byte[] suchZug;

    private int suchZugLaenge;

    public String getVersion() {
        return "1 (Tinmel)";
    }

    public String getAuthor() {
        return "michael@tabacha.de";
    }

    public boolean canPlayBothSides() {
        return true;
    }

    /** Thinking here.
	@param position The actual position
	@return The move the routine makes
    */
    public Move think(Board position) {
        this.whoseTurn = position.whoseTurn();
        if (whoseTurn == DOWN) {
            this.board = position.toArray();
            this.brow = position.getBall().row();
        } else {
            this.board = position.upsideDown().toArray();
            this.brow = 18 - position.getBall().row();
        }
        this.bcol = position.getBall().col();
        bestZug = new byte[MAXSUCHTIEFE];
        bestZugLaenge = 0;
        suchZug = new byte[MAXSUCHTIEFE];
        suchZugLaenge = 0;
        bestPutCol = bestPutRow = -50;
        bestWert = -30000;
        bestVsb = -30000;
        anzBest = 0;
        gutzug = new boolean[23][23];
        int i, j;
        for (i = 0; i < 23; i++) for (j = 0; j < 23; j++) gutzug[i][j] = false;
        int[] minmax = new int[2];
        springe(minmax);
        min = minmax[0];
        max = minmax[1];
        if (bestWert != 20000) probiereBoings();
        if (bestPutCol >= 0) return (whoseTurn == DOWN) ? new Put(bestPutCol, bestPutRow) : new Put(bestPutCol, 18 - bestPutRow); else {
            Jump ju = new Jump();
            for (i = 0; i < bestZugLaenge; i++) ju.push(bestZug[i]);
            return (whoseTurn == DOWN) ? ju : ju.upsideDown();
        }
    }

    private byte on(int col, int row) {
        if (0 <= col && col <= 18 && 0 <= row && row <= 18) return board[row][col]; else return EMPTY;
    }

    private void set(int col, int row, byte type) {
        if (0 <= col && col <= 18 && 0 <= row && row <= 18) board[row][col] = type;
    }

    private void springe(int[] this_minmax) {
        this_minmax[0] = this_minmax[1] = brow;
        int[] sub_minmax = new int[2];
        suchZugLaenge++;
        int testcol, testrow;
        int anz = 0;
        int wert;
        for (byte r = 0; r < 8; r++) {
            testcol = bcol + DCOL[r];
            testrow = brow + DROW[r];
            if (on(testcol, testrow) == EMPTY) gutzug[testcol + 2][testrow + 2] = true; else {
                set(bcol, brow, EMPTY);
                anz = 1;
                bcol += DCOL[r];
                brow += DROW[r];
                do {
                    set(bcol, brow, EMPTY);
                    anz++;
                    bcol += DCOL[r];
                    brow += DROW[r];
                } while (on(bcol, brow) != EMPTY);
                gutzug[bcol + 2][brow + 2] = true;
                if ((0 <= bcol) && (bcol <= 18) || (brow < 0) || (brow > 18)) {
                    set(bcol, brow, BALL);
                    suchZug[suchZugLaenge - 1] = r;
                    if ((suchZugLaenge < MAXSUCHTIEFE) && (0 < brow) && (brow < 18)) springe(sub_minmax); else sub_minmax[0] = sub_minmax[1] = brow;
                    if (sub_minmax[0] < this_minmax[0]) this_minmax[0] = sub_minmax[0];
                    if (sub_minmax[1] > this_minmax[1]) this_minmax[1] = sub_minmax[1];
                    wert = bewerteStellung(sub_minmax[0], sub_minmax[1]) - ZM;
                    if (wert == bestWert) {
                        wert = suchZugLaenge - 1;
                        if (wert > bestVsb) {
                            anzBest = 1;
                            bestVsb = wert;
                            System.arraycopy(suchZug, 0, bestZug, 0, suchZugLaenge);
                            bestZugLaenge = suchZugLaenge;
                        } else if (wert == bestVsb) {
                            anzBest++;
                            if (random().nextInt(anzBest) == 0) {
                                System.arraycopy(suchZug, 0, bestZug, 0, suchZugLaenge);
                                bestZugLaenge = suchZugLaenge;
                            }
                        }
                    } else if (wert > bestWert) {
                        bestWert = wert;
                        bestVsb = suchZugLaenge - 1;
                        anzBest = 1;
                        System.arraycopy(suchZug, 0, bestZug, 0, suchZugLaenge);
                        bestZugLaenge = suchZugLaenge;
                    }
                }
                set(bcol, brow, EMPTY);
                anz--;
                bcol -= DCOL[r];
                brow -= DROW[r];
                while (anz > 0) {
                    set(bcol, brow, PIECE);
                    anz--;
                    bcol -= DCOL[r];
                    brow -= DROW[r];
                }
                set(bcol, brow, BALL);
            }
        }
        suchZugLaenge--;
    }

    private int probiereBoings_schiefer(int x, int y, int bx, int by) {
        int dx = x - bx;
        int dy = y - by;
        if ((-1 <= dy) && (dy <= 1) && (-1 <= dx) && (dx <= 1) && (on(bx - dx, by - dy) == PIECE)) return SCHIEFER; else return 0;
    }

    private int probiereBoings_zurMitte(int x, int y, int bx, int by) {
        return (bx < x && x <= 9 || 9 <= x && x < bx) ? 1 : 0;
    }

    private void probiereBoings() {
        int wert;
        int x, y;
        for (x = 0; x <= 18; x++) for (y = 0; y <= 18; y++) if (gutzug[x + 2][y + 2] && on(x, y) == EMPTY) {
            set(x, y, PIECE);
            wert = bewerteStellung();
            if (wert > bestWert) {
                bestWert = wert;
                bestVsb = vsb() - 2 + probiereBoings_schiefer(x, y, bcol, brow) + probiereBoings_zurMitte(x, y, bcol, brow);
                anzBest = 1;
                bestPutCol = x;
                bestPutRow = y;
            } else if (wert == bestWert) {
                wert = vsb() - 2 + probiereBoings_schiefer(x, y, bcol, brow) + probiereBoings_zurMitte(x, y, bcol, brow);
                if (wert > bestVsb) {
                    bestVsb = wert;
                    anzBest = 1;
                    bestPutCol = x;
                    bestPutRow = y;
                } else if (wert == bestVsb) {
                    anzBest++;
                    if (random().nextInt(anzBest) == 0) {
                        bestPutCol = x;
                        bestPutRow = y;
                    }
                }
            }
            set(x, y, EMPTY);
        }
    }

    private int bewerteStellung(int knownMin, int knownMax) {
        if (brow >= 18) return 20000; else if (brow <= 0) return -20000; else {
            if (knownMin <= 0) return -10000; else if (knownMin <= 2) return A * knownMin + B * knownMax + C * brow - NEAR_LOSS; else return A * knownMin + B * knownMax + C * brow;
        }
    }

    private int bewerteStellung() {
        if (brow >= 18) return 20000; else if (brow <= 0) return -20000; else {
            min = brow;
            max = brow;
            bewerteStellung_springe();
            if (min <= 0) return -10000; else if (min <= 2) return A * min + B * max + C * brow - NEAR_LOSS; else return A * min + B * max + C * brow;
        }
    }

    private void bewerteStellung_springe() {
        int anz;
        for (int r = 0; r < 8; r++) {
            if (on(bcol + DCOL[r], brow + DROW[r]) == PIECE) {
                anz = 1;
                bcol += DCOL[r];
                brow += DROW[r];
                do {
                    set(bcol, brow, EMPTY);
                    anz++;
                    bcol += DCOL[r];
                    brow += DROW[r];
                } while (on(bcol, brow) == PIECE);
                if (brow <= 0) min = 0; else if (brow >= 18) max = 18; else if ((0 <= bcol) && (bcol <= 18)) {
                    bewerteStellung_springe();
                    if (brow > max) max = brow;
                    if (brow < min) min = brow;
                }
                bcol -= DCOL[r];
                brow -= DROW[r];
                for (int d = 0; d < anz - 1; d++) {
                    set(bcol, brow, PIECE);
                    bcol -= DCOL[r];
                    brow -= DROW[r];
                }
            }
        }
    }

    /** Trapezz�hlung.
     */
    private int vsb_zaehlePoeppel() {
        int wert = 0;
        int z = (brow >= 13) ? 18 : (brow + 5);
        for (int y = brow + 2; y <= z; y++) {
            for (int x = (brow - y + bcol <= 0) ? 0 : (brow - y + bcol); (x <= 18) && (x <= y - brow + bcol); x++) if (on(x, y) == PIECE) wert++;
        }
        return wert;
    }

    /**
     */
    private void vsb_springe_vsbWertMax() {
        int wert = 1;
        int x, y, z;
        max = brow;
        while ((wert < suchZugLaenge) && (suchZug[wert] == suchZug[0])) wert++;
        wert = wert * 4;
        z = (brow >= 13) ? 18 : (brow + 5);
        for (y = brow + 2; y <= z; y++) {
            for (x = (brow - y + bcol <= 0) ? 0 : (brow - y + bcol); (x <= 18) && (x <= y - brow + bcol); x++) if (on(x, y) == PIECE) wert++;
        }
        if (wert > ags) ags = wert;
    }

    private void vsb_springe() {
        int d, anz;
        for (byte r = 0; r < 8; r++) if (on(bcol + DCOL[r], brow + DROW[r]) == PIECE) {
            anz = 1;
            bcol += DCOL[r];
            brow += DROW[r];
            do {
                set(bcol, brow, EMPTY);
                anz++;
                bcol += DCOL[r];
                brow += DROW[r];
            } while (on(bcol, brow) == PIECE);
            suchZug[suchZugLaenge] = r;
            suchZugLaenge++;
            if (brow >= 18) vsb_springe_vsbWertMax(); else if ((0 <= bcol) && (bcol <= 18)) {
                if (suchZugLaenge < MAXSUCHTIEFE) vsb_springe();
                if (brow >= max) vsb_springe_vsbWertMax();
            }
            suchZugLaenge--;
            bcol -= DCOL[r];
            brow -= DROW[r];
            for (d = 1; d <= anz - 1; d++) {
                set(bcol, brow, PIECE);
                bcol -= DCOL[r];
                brow -= DROW[r];
            }
        }
    }

    /** Verfeinerte Stellungsbewertung.
	Wird nur bei Setzen-Z�gen aufgerufen.
     */
    private int vsb() {
        suchZugLaenge = 0;
        if ((brow <= 0) || (brow >= 18)) return 0;
        min = brow;
        max = brow;
        ags = vsb_zaehlePoeppel();
        vsb_springe();
        return (int) Math.round(ags * VSB_BOING_FACTOR);
    }
}
