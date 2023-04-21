package de.cbix.tantrixme.Tantrix;

import java.lang.Thread;
import java.util.Enumeration;
import java.util.Vector;

public class Game extends Thread implements TantrixBackend {

    private Space[] demoMoveSpaces;

    private Space[] boardSpaces;

    private TantrixFrontend frontend;

    private boolean paused = false;

    private boolean running = true;

    private boolean active = false;

    public void run() {
        active = true;
        try {
            int i = 0;
            Space s;
            while (running && i < demoMoveSpaces.length) {
                synchronized (this) {
                    s = demoMoveSpaces[i];
                    if (s == null) {
                        continue;
                    }
                    frontend.showMessage(s.toString());
                    boardSpaces[i] = s;
                    frontend.notifyBoardChange(boardSpaces.length);
                    i++;
                }
                synchronized (this) {
                    while (paused) {
                        wait();
                    }
                }
            }
        } catch (InterruptedException e) {
        }
    }

    public Game(int initialBoardCapacity) {
        demoMoveSpaces = new Space[initialBoardCapacity];
        boardSpaces = new Space[initialBoardCapacity];
        demoMoveSpaces[0] = new Space(3, 3, new Tile(7, 0));
        demoMoveSpaces[1] = new Space(2, 2, new Tile(3, 1));
        demoMoveSpaces[2] = new Space(3, 1, new Tile(11, 4));
        demoMoveSpaces[3] = new Space(4, 2, new Tile(13, 3));
        demoMoveSpaces[4] = new Space(2, 0, new Tile(54, 2));
        demoMoveSpaces[5] = new Space(1, 1, new Tile(2, 4));
        demoMoveSpaces[6] = new Space(6, 2, new Tile(16, 0));
        demoMoveSpaces[7] = new Space(5, 3, new Tile(49, 1));
        demoMoveSpaces[8] = new Space(8, 2, new Tile(32, 1));
        demoMoveSpaces[9] = new Space(7, 3, new Tile(24, 2));
        demoMoveSpaces[10] = new Space(10, 2, new Tile(40, 1));
        demoMoveSpaces[11] = new Space(9, 3, new Tile(36, 5));
        demoMoveSpaces[12] = new Space(12, 2, new Tile(46, 0));
        demoMoveSpaces[13] = new Space(11, 3, new Tile(35, 0));
        demoMoveSpaces[14] = new Space(1, -1, new Tile(25, 3));
        demoMoveSpaces[15] = new Space(5, 1, new Tile(8, 3));
        demoMoveSpaces[16] = new Space(4, 0, new Tile(12, 2));
        demoMoveSpaces[17] = new Space(7, 1, new Tile(52, 1));
        demoMoveSpaces[18] = new Space(9, 1, new Tile(17, 5));
        demoMoveSpaces[19] = new Space(11, 1, new Tile(56, 5));
        demoMoveSpaces[20] = new Space(6, 0, new Tile(28, 2));
        demoMoveSpaces[21] = new Space(0, 0, new Tile(51, 0));
        demoMoveSpaces[22] = new Space(-2, 0, new Tile(34, 2));
        demoMoveSpaces[23] = new Space(-1, -1, new Tile(27, 1));
        demoMoveSpaces[24] = new Space(3, -1, new Tile(39, 0));
        demoMoveSpaces[25] = new Space(8, 0, new Tile(15, 2));
        demoMoveSpaces[26] = new Space(-1, 1, new Tile(44, 5));
        demoMoveSpaces[27] = new Space(0, 2, new Tile(42, 3));
        demoMoveSpaces[28] = new Space(10, 0, new Tile(19, 4));
        demoMoveSpaces[29] = new Space(-4, 0, new Tile(23, 4));
        demoMoveSpaces[30] = new Space(-3, 1, new Tile(6, 2));
        demoMoveSpaces[31] = new Space(-3, -1, new Tile(9, 5));
        demoMoveSpaces[32] = new Space(-2, 2, new Tile(4, 2));
        demoMoveSpaces[33] = new Space(1, 3, new Tile(50, 4));
        demoMoveSpaces[34] = new Space(-1, 3, new Tile(37, 5));
        demoMoveSpaces[35] = new Space(-6, 0, new Tile(1, 2));
        demoMoveSpaces[36] = new Space(-2, -2, new Tile(31, 4));
        demoMoveSpaces[37] = new Space(-5, -1, new Tile(26, 0));
        demoMoveSpaces[38] = new Space(0, -2, new Tile(30, 1));
        demoMoveSpaces[39] = new Space(2, -2, new Tile(33, 5));
        demoMoveSpaces[40] = new Space(-8, 0, new Tile(43, 0));
        demoMoveSpaces[41] = new Space(-7, -1, new Tile(47, 0));
        demoMoveSpaces[42] = new Space(-4, -2, new Tile(14, 1));
        demoMoveSpaces[43] = new Space(12, 4, new Tile(41, 5));
        demoMoveSpaces[44] = new Space(13, 3, new Tile(29, 3));
        demoMoveSpaces[45] = new Space(5, -1, new Tile(38, 2));
        demoMoveSpaces[46] = new Space(7, -1, new Tile(20, 0));
        demoMoveSpaces[47] = new Space(-10, 0, new Tile(10, 4));
        demoMoveSpaces[48] = new Space(-9, -1, new Tile(53, 4));
        demoMoveSpaces[49] = new Space(8, -2, new Tile(55, 3));
        demoMoveSpaces[50] = new Space(6, -2, new Tile(45, 1));
        demoMoveSpaces[51] = new Space(3, -3, new Tile(48, 1));
        demoMoveSpaces[52] = new Space(4, -4, new Tile(5, 5));
        demoMoveSpaces[53] = new Space(5, -3, new Tile(18, 1));
        demoMoveSpaces[54] = new Space(11, 5, new Tile(21, 3));
        demoMoveSpaces[55] = new Space(-3, 3, new Tile(22, 4));
    }

    public void stopGame() {
        running = false;
        active = false;
        interrupt();
    }

    public synchronized void pauseGame() {
        frontend.showMessage("pausing game...");
        paused = true;
    }

    public synchronized void resumeGame() {
        frontend.showMessage("resuming game...");
        paused = false;
        notify();
    }

    public void togglePausedGame() {
        if (paused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public void bindFrontend(TantrixFrontend tf) {
        if (frontend == null || !frontend.isActive()) {
            frontend = tf;
        }
    }

    public void getBoardSpaces(Space[] spaceArray) {
        int len;
        if (spaceArray.length < boardSpaces.length) {
            len = spaceArray.length;
        } else {
            len = boardSpaces.length;
        }
        System.arraycopy(boardSpaces, 0, spaceArray, 0, len);
    }

    public Space getSpaceAt(int x, int y) {
        for (int i = 0; i < boardSpaces.length; i++) {
            if (boardSpaces[i] != null && boardSpaces[i].isXY(x, y)) {
                return boardSpaces[i];
            }
        }
        return null;
    }

    public boolean isActive() {
        return active;
    }
}
