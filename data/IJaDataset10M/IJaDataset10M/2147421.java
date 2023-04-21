package net.sourceforge.java_stratego.stratego;

public abstract class Engine {

    protected Board board = null;

    protected Status status = Status.STOPPED;

    protected int turn = Board.RED;

    public Piece getBoardPiece(int x, int y) {
        return board.getPiece(new Spot(x, y));
    }

    public Piece getTrayPiece(int i) {
        return board.getTrayPiece(i);
    }

    public int getTraySize() {
        return board.getTraySize();
    }

    public int getTurn() {
        return turn;
    }

    private boolean move(Move m) {
        if (status == Status.PLAYING) {
            if (m.getFrom().equals(Board.IN_TRAY)) return false;
            if (m.getTo().equals(Board.IN_TRAY)) return false;
            if (board.attack(m)) return true;
            return board.move(m);
        }
        return false;
    }

    public void newGame() {
        board.clear();
        turn = Board.RED;
        status = Status.SETUP;
        if (Settings.bShowAll) board.showAll();
    }

    public void play() {
        if (status != Status.SETUP) return;
        if (board.getTraySize() == 0) status = Status.PLAYING;
        if (Settings.bShowAll) board.showAll();
    }

    protected boolean requestMove(Move m) {
        if (m == null || !move(m)) return false;
        if (status == Status.PLAYING) {
            int winner;
            if ((winner = board.checkWin()) >= 0) {
                status = Status.STOPPED;
                board.showAll();
                gameOver(winner);
            }
            turn = (turn + 1) % 2;
        }
        update();
        return true;
    }

    public boolean setupPlacePiece(Piece p, Spot s) {
        if (status == Status.SETUP) return board.add(p, s);
        return false;
    }

    public boolean setupRemovePiece(Spot s) {
        if (status == Status.SETUP) return board.remove(s);
        return false;
    }

    protected abstract void gameOver(int winner);

    protected abstract void update();
}
