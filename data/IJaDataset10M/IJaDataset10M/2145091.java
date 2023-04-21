package loengud.kolmteist;

import java.util.Vector;

/** This class implements the logic (model) for the game of 
    Five-In-A-Row.   
    @author Fred Swartz
    @version 2004-05-02
    @modified Tanel Tammet
    @modification 2005-11-20
    */
class FiveLogic {

    /** Number of board rows. */
    private int maxRows_;

    /** Number of board columns. */
    private int maxCols_;

    /** The board. */
    int[][] board_;

    /** The player who moves next. */
    int nextPlayer_;

    /** Number of moves in the game. */
    int moves_ = 0;

    public static final int EMPTY = 0;

    private static final int PLAYER1 = 1;

    public static final int TIE = -1;

    public static final int INFINITY = 99999999;

    public FiveLogic(int rows, int cols) {
        maxRows_ = rows;
        maxCols_ = cols;
        board_ = new int[maxRows_][maxCols_];
        reset();
    }

    /** Returns the next player. */
    public int getNextPlayer() {
        return nextPlayer_;
    }

    /** Returns player who has played at particular row and column. */
    public int getPlayerAt(int r, int c) {
        return board_[r][c];
    }

    /** Clears board to initial state. Makes first move in center. */
    public void reset() {
        for (int r = 0; r < maxRows_; r++) {
            for (int c = 0; c < maxCols_; c++) {
                board_[r][c] = EMPTY;
            }
        }
        moves_ = 0;
        nextPlayer_ = PLAYER1;
        move(maxCols_ / 2, maxRows_ / 2);
    }

    /** Play a marker on the board, record it, flip players. */
    public void move(int r, int c) {
        board_[r][c] = nextPlayer_;
        nextPlayer_ = 3 - nextPlayer_;
        moves_++;
    }

    /** Play a marker on the board, record it, flip players. */
    public void human_move(int r, int c) {
        int enr;
        move(r, c);
        enr = best_move(nextPlayer_, board_);
        board_[decode_move_x(enr)][decode_move_y(enr)] = nextPlayer_;
        nextPlayer_ = 3 - nextPlayer_;
        moves_++;
    }

    /** The count5_ utility function returns true if there are five in
        a row starting at the specified r,c position and 
        continuing in the dr direcection (+1, -1) and
        similarly for the column c.
        */
    private boolean count5_(int r, int dr, int c, int dc) {
        int player = board_[r][c];
        for (int i = 1; i < 5; i++) {
            if (board_[r + dr * i][c + dc * i] != player) return false;
        }
        return true;
    }

    /** -1 = game is tie, 0 = more to play, 
         1 = player1 wins, 2 = player2 wins */
    public int getGameStatus() {
        int row;
        int col;
        int n_up, n_right, n_up_right, n_up_left;
        boolean at_least_one_move;
        for (row = 0; row < maxRows_; row++) {
            for (col = 0; col < maxCols_; col++) {
                int p = board_[row][col];
                if (p != EMPTY) {
                    if (row < maxRows_ - 4) if (count5_(row, 1, col, 0)) return p;
                    if (col < maxCols_ - 4) {
                        if (count5_(row, 0, col, 1)) return p;
                        if (row < maxRows_ - 4) {
                            if (count5_(row, 1, col, 1)) return p;
                        }
                    }
                    if (col > 3 && row < maxRows_ - 4) {
                        if (count5_(row, 1, col, -1)) return p;
                    }
                }
            }
        }
        if (moves_ == maxRows_ * maxCols_) {
            return TIE;
        } else {
            return 0;
        }
    }

    public int encode_move(int x, int y) {
        return x * 1000 + y;
    }

    public int decode_move_x(int n) {
        return (int) (n / 1000);
    }

    public int decode_move_y(int n) {
        return (n % 1000);
    }

    long startTime;

    public int best_move(int player, int[][] board) {
        Vector<Integer> vec = makemovelist(board);
        if (vec.size() == 0) {
            System.out.println("error in best_move: no move found");
            System.exit(1);
            return 0;
        }
        int bestCost = -INFINITY;
        int bestMove = 0;
        int value = 0;
        for (Integer m : vec) {
            int x = decode_move_x(m);
            int y = decode_move_y(m);
            board[x][y] = player;
            value = minimax(3 - player, board, 3);
            if (value > bestCost) {
                bestCost = value;
                bestMove = m;
            }
            board[x][y] = EMPTY;
        }
        return bestMove;
    }

    /**
     * TODO: vii sisse alfa-beta t�iendus
     * 
     * @param player m�ngija kelle "kontekstis" parasjagu minimaxi k�ivitatakse
     * @param board m�ngulaud (ja sellel olev seis)
     * @param depth	otsingu s�gavus - rekursiooni l�petamise tingimus
     * @return
     */
    public int minimax(int player, int[][] board, int depth) {
        int status = getGameStatus();
        if (status != 0) {
            if (status == PLAYER1) return INFINITY; else if (status == 3 - PLAYER1) return -INFINITY; else return 0;
        }
        if (depth == 0) {
            return 0;
        }
        int bestValue;
        if (player == PLAYER1) bestValue = -INFINITY; else bestValue = INFINITY;
        Vector<Integer> moves = makemovelist(board);
        if (moves.isEmpty()) return 0;
        for (int i = 0; i < moves.size(); i++) {
            int x = decode_move_x(moves.get(i));
            int y = decode_move_y(moves.get(i));
            board[x][y] = player;
            int value = minimax(3 - player, board, depth - 1);
            System.out.println(value);
            board[x][y] = EMPTY;
            if (player == PLAYER1) bestValue = Math.max(value, bestValue); else bestValue = Math.min(value, bestValue);
        }
        return bestValue;
    }

    /**
     * Leiame vabad ruudud kuhu k�ia.
     * 
     * TODO: tegelikult ei ole m�tekas siin k�iki vabasid k�ike v�lja arvutada,
     * piisab kui valida vabad ruudud, mis on t�idetud ruutude k�rval - seda on
     * v�rdlemisi lihtne saavutada count5_() funktsiooni p�hjal
     * 
     * @param board
     * @return
     */
    public Vector<Integer> makemovelist(int[][] board) {
        Vector<Integer> res = new Vector<Integer>();
        int maxrows = board.length;
        int maxcols = board[0].length;
        for (int i = 0; i < maxrows; i++) for (int j = 0; j < maxcols; j++) if (board[i][j] == EMPTY) res.add(encode_move(i, j));
        return res;
    }
}
