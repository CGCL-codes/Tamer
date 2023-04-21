package de.xlii.bgo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GoView extends BoardView implements GameListener {

    protected static final String TAG = "BGo/GoView";

    private static final int HOSHI = 0;

    private static final int STONE_BLACK = 1;

    private static final int STONE_WHITE = 2;

    private static final int MARK_OFFSET = 2;

    private static final int MARK_BLACK = 3;

    private static final int MARK_WHITE = 4;

    private static final int MAX_TILE = 5;

    private static final int VISITED = 5;

    private Coordinate lastmove = new Coordinate();

    private int deadblack;

    private int deadwhite;

    private ScoreListener scoreListener;

    public GoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        GoRecord.getInstance().addGameListener(this);
    }

    public void update() {
        invalidate();
    }

    public void setScoreListener(ScoreListener scoreListener) {
        this.scoreListener = scoreListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetTiles(MAX_TILE);
        loadTile(STONE_BLACK, new StoneTile(Color.BLACK));
        loadTile(STONE_WHITE, new StoneTile(Color.WHITE));
        loadTile(MARK_BLACK, new StoneMarkTile(Color.argb(255, 80, 80, 80), StoneMarkTile.SQUARE));
        loadTile(MARK_WHITE, new StoneMarkTile(Color.argb(255, 200, 200, 200), StoneMarkTile.SQUARE));
        loadTile(HOSHI, new StoneTile(Color.BLACK, 1));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "touch x:" + event.getX() + ", y:" + event.getY());
                double x = (event.getX() - mXOffset) / mTileSize;
                double y = (event.getY() - mYOffset) / mTileSize;
                int xx = (int) Math.floor(x);
                int yy = (int) Math.floor(y);
                int curMark = GoRecord.getInstance().isNextWhite() ? MARK_WHITE : MARK_BLACK;
                if (lastmove.exists()) {
                    clearTileFlag(curMark, lastmove.getX(), lastmove.getY());
                }
                lastmove = new Coordinate(xx, yy);
                Log.v(TAG, "Touch@ " + lastmove);
                try {
                    setTileFlag(curMark, lastmove.getX(), lastmove.getY());
                } catch (ArrayIndexOutOfBoundsException e) {
                    lastmove = new Coordinate();
                }
                update();
                break;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev) {
        Log.d("Keypress", "key pressed, pos=" + lastmove);
        int dx = 0;
        int dy = 0;
        int cur_color = GoRecord.getInstance().isNextWhite() ? STONE_WHITE : STONE_BLACK;
        switch(keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                dy = -1;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                dy = +1;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                dx = -1;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                dx = +1;
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                if (GtpConnector.getInstance().isThinking()) {
                    return true;
                }
                if (lastmove.exists()) {
                    Log.v(TAG, "Key DPAD CENTER pressed" + cur_color + lastmove);
                    if (isValidMove()) {
                        clearTileFlag(MARK_BLACK, lastmove.getX(), lastmove.getY());
                        clearTileFlag(MARK_WHITE, lastmove.getX(), lastmove.getY());
                        GoRecord.getInstance().addMove(new GameMove(GoRecord.getInstance().isNextWhite(), lastmove.getX(), lastmove.getY()), this);
                        update();
                        lastmove = new Coordinate(lastmove);
                    } else {
                    }
                }
                return true;
            default:
                return false;
        }
        if (dx != 0 || dy != 0) {
            clearTileFlag(cur_color + MARK_OFFSET, lastmove.getX(), lastmove.getY());
            try {
                lastmove.delta(dx, dy);
                setTileFlag(cur_color + MARK_OFFSET, lastmove.getX(), lastmove.getY());
            } catch (ArrayIndexOutOfBoundsException e) {
                lastmove.delta(-dx, -dy);
                setTileFlag(cur_color + MARK_OFFSET, lastmove.getX(), lastmove.getY());
            }
            update();
            return true;
        }
        ;
        throw new IllegalStateException("can't happen");
    }

    private boolean isValidMove() {
        if (getTileFlag(STONE_BLACK, lastmove.getX(), lastmove.getY())) {
            return false;
        } else if (getTileFlag(STONE_WHITE, lastmove.getX(), lastmove.getY())) {
            return false;
        }
        return true;
    }

    private int checkForDeadStones(int color, int tx, int ty) {
        int stones = 0;
        Log.d(TAG, "chkDead@ (" + tx + "/" + ty + "), c=" + intToCol(color));
        if (isDeadStone(color, tx, ty)) {
            Log.d(TAG, "dead!");
            for (int x = 0; x < mTileCount; x++) {
                for (int y = 0; y < mTileCount; y++) {
                    if (getTileFlag(VISITED, x, y)) {
                        stones++;
                        clearTileFlag(color, x, y);
                    }
                    clearTileFlag(VISITED, x, y);
                }
            }
        } else {
            clearTileFlag(VISITED);
        }
        return stones;
    }

    private boolean isDeadStone(int color, int x, int y) {
        if (x < 0 || y < 0 || x >= mTileCount || y >= mTileCount) {
            return true;
        }
        Log.d(TAG, "checking @ " + x + "," + y + ": " + tileToCol(getTile(x, y)));
        if (getTileFlag(VISITED, x, y)) {
            return true;
        }
        if (getTileFlag(color, x, y)) {
            setTileFlag(VISITED, x, y);
            return isDeadStone(color, x + 1, y) && isDeadStone(color, x - 1, y) && isDeadStone(color, x, y + 1) && isDeadStone(color, x, y - 1);
        }
        if (getTileFlag(color == STONE_WHITE ? STONE_BLACK : STONE_WHITE, x, y)) {
            return true;
        }
        return false;
    }

    private String tileToCol(int tile) {
        String s = "(";
        while (tile > 0) {
            s += intToCol(Integer.numberOfTrailingZeros(tile)) + ",";
            tile &= ~Integer.lowestOneBit(tile);
        }
        return s + ")";
    }

    private String intToCol(int color) {
        String[] foo = { "HOSHI", "STONE_BLACK", "STONE_WHITE", "<undef>", "MARK_BLACK", "MARK_WHITE", "VISITED" };
        return foo[color];
    }

    private String boolToCol(boolean isWhite) {
        if (isWhite) {
            return "White";
        } else {
            return "Black";
        }
    }

    public void publishMove(GameMove move, Object source) {
        Log.d(TAG, "Move@" + move + " color:" + boolToCol(move.isWhite()));
        if (!move.isPass()) {
            setTileFlag(move.isWhite() ? STONE_WHITE : STONE_BLACK, move.getX(), move.getY());
            int oppColor = GoRecord.getInstance().isNextWhite() ? STONE_WHITE : STONE_BLACK;
            int killedStones = 0;
            if (move.getX() > 0) if (getTileFlag(oppColor, move.getX() - 1, move.getY())) killedStones += checkForDeadStones(oppColor, move.getX() - 1, move.getY());
            if (move.getX() < mTileCount - 1) if (getTileFlag(oppColor, move.getX() + 1, move.getY())) killedStones += checkForDeadStones(oppColor, move.getX() + 1, move.getY());
            if (move.getY() > 0) if (getTileFlag(oppColor, move.getX(), move.getY() - 1)) killedStones += checkForDeadStones(oppColor, move.getX(), move.getY() - 1);
            if (move.getY() < mTileCount - 1) if (getTileFlag(oppColor, move.getX(), move.getY() + 1)) killedStones += checkForDeadStones(oppColor, move.getX(), move.getY() + 1);
            if (killedStones > 0) {
                Log.d(TAG, "killed " + killedStones + " stones.");
            } else {
                checkForDeadStones(move.isWhite() ? STONE_WHITE : STONE_BLACK, move.getX(), move.getY());
            }
            if (oppColor == STONE_WHITE) {
                deadwhite += killedStones;
                scoreListener.publishWhite(deadwhite);
            } else {
                deadblack += killedStones;
                scoreListener.publishBlack(deadblack);
            }
        }
        if (lastmove.exists()) {
            clearTileFlag(MARK_BLACK, lastmove.getX(), lastmove.getY());
            clearTileFlag(MARK_WHITE, lastmove.getX(), lastmove.getY());
            int curMark = GoRecord.getInstance().isNextWhite() ? MARK_WHITE : MARK_BLACK;
        }
        update();
    }

    public void resetGame(int size, int komi, Object source) {
        createTiles(size);
        int edge = 0;
        if (size >= 7 && size < 13) {
            edge = 3;
        } else if (size >= 13) {
            edge = 4;
        }
        setTileFlag(HOSHI, edge - 1, edge - 1);
        setTileFlag(HOSHI, size - edge, size - edge);
        setTileFlag(HOSHI, edge - 1, size - edge);
        setTileFlag(HOSHI, size - edge, edge - 1);
        if (size % 2 == 1) {
            setTileFlag(HOSHI, (size - 1) / 2, (size - 1) / 2);
            setTileFlag(HOSHI, edge - 1, (size - 1) / 2);
            setTileFlag(HOSHI, (size - 1) / 2, edge - 1);
            setTileFlag(HOSHI, size - edge, (size - 1) / 2);
            setTileFlag(HOSHI, (size - 1) / 2, size - edge);
        }
        update();
    }

    private class Coordinate {

        private int x;

        private int y;

        private boolean drawn;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
            this.drawn = true;
        }

        public Coordinate() {
            drawn = false;
        }

        public Coordinate(Coordinate lastmove) {
            x = lastmove.x;
            y = lastmove.y;
            drawn = false;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return "[" + x + "," + y + "]";
        }

        public boolean exists() {
            return drawn;
        }

        public void delta(int dx, int dy) {
            x += dx;
            y += dy;
            drawn = true;
        }
    }
}
