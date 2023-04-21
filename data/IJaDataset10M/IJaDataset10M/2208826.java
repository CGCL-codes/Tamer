package chessapp;

import javax.swing.*;
import java.awt.*;
import java.applet.Applet;
import java.io.IOException;
import java.util.*;

/** BoardView class - draws the board

@author Peter Hunter
@version $Revision: 1.7 $ $Date: 2002/01/19 00:19:20 $
*/
final class BoardView extends JPanel {

    private static final int offset = 25;

    private final ChessApp app;

    private Board board;

    private Search searcher;

    private int moves = 0;

    private boolean flip = false;

    private boolean first = true;

    private final JLabel wtm, btm;

    private final JPanel mmPanel;

    private Square[][] square = new Square[8][8];

    private int startCol, startRow;

    private int maxTime = 10000;

    private int maxDepth = 32;

    private boolean moving = false;

    /** Constructs a board view.
     * @param applet The applet we're using the board in.
     * @param b The board of which this is the view
     */
    BoardView(ChessApp applet, Board b) {
        super();
        app = applet;
        board = b;
        searcher = new Search(b);
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(10, 10));
        letterRow(boardPanel);
        for (int i = 0; i < 8; i++) {
            boardPanel.add(new BLabel(convertNum(i, false)));
            for (int j = 0; j < 8; j++) {
                square[i][j] = new Square(i, j, this);
                int color = b.getColor(i, j);
                if (color != Board.EMPTY) {
                    int piece = b.getPiece(i, j);
                    square[i][j].setIcon(new ImageIcon(pieceImage[color][piece]));
                }
                boardPanel.add(square[i][j]);
            }
            boardPanel.add(new BLabel(convertNum(i, false)));
        }
        letterRow(boardPanel);
        mmPanel = new JPanel();
        mmPanel.setLayout(new GridLayout(2, 1));
        Icon whiteMoveIcon = new ImageIcon(app.getImage(getClass().getResource("images/wMove.gif")));
        wtm = new JLabel(whiteMoveIcon);
        Icon blackMoveIcon = new ImageIcon(app.getImage(getClass().getResource("images/bMove.gif")));
        btm = new JLabel(blackMoveIcon);
        wtm.setVerticalAlignment(JLabel.BOTTOM);
        wtm.setHorizontalAlignment(JLabel.CENTER);
        wtm.setPreferredSize(new Dimension(104, 168));
        btm.setVerticalAlignment(JLabel.TOP);
        btm.setHorizontalAlignment(JLabel.CENTER);
        btm.setPreferredSize(new Dimension(104, 168));
        btm.setVisible(false);
        mmPanel.add(btm);
        mmPanel.add(wtm);
        setLayout(new FlowLayout());
        add(boardPanel);
        add(mmPanel);
    }

    public void setBoard(Board b) {
        board = b;
        searcher = new Search(b);
        reset();
    }

    public void setMaxTime(int millis) {
        maxTime = millis;
        maxDepth = 32;
    }

    private void letterRow(JPanel p) {
        p.add(new JPanel());
        for (int i = 0; i < 8; i++) p.add(new BLabel(convertNum(i, true)));
        p.add(new JPanel());
    }

    /** Called when the user selects a square
     *
     * @param row The row of the square clicked
     * @param col The column
     */
    void selected(int row, int col) {
        if (moving) {
            app.showStatus("The computer is moving - please wait!");
            return;
        }
        String status;
        if (first) {
            int piece = board.getPiece(row, col);
            int color = board.getColor(row, col);
            if (piece == Board.EMPTY) {
                status = "That square is empty. Player " + (board.isWhiteToMove() ? "White" : "Black") + " to Move";
                app.showStatus(status);
                return;
            } else if (board.side != color) {
                app.showStatus("That's not your piece!");
                return;
            } else {
                app.showStatus("Select destination for that piece");
                startCol = col;
                startRow = row;
                first = false;
                return;
            }
        }
        TreeSet validMoves = board.gen();
        int from = (startRow << 3) + startCol;
        int to = (row << 3) + col;
        boolean found = false;
        int promote = 0;
        if ((((to < 8) && (board.side == Board.LIGHT)) || ((to > 55) && (board.side == Board.DARK))) && (board.getPiece(startRow, startCol) == Board.PAWN)) {
            ImageIcon[] icons = new ImageIcon[6];
            for (int i = Board.KNIGHT; i <= Board.QUEEN; i++) icons[i] = new ImageIcon(pieceImage[board.side][i]);
            Object[] options = { icons[Board.KNIGHT], icons[Board.BISHOP], icons[Board.ROOK], icons[Board.QUEEN] };
            int choice = JOptionPane.showOptionDialog(this, "Promote to which piece?", "Promotion", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[3]);
            if (choice == JOptionPane.CANCEL_OPTION) choice = 3;
            promote = choice + 1;
        }
        Iterator i = validMoves.iterator();
        Move m = null;
        while (i.hasNext()) {
            m = (Move) i.next();
            if (m.from == from && m.to == to && m.promote == promote) {
                found = true;
                break;
            }
        }
        if (!found || !board.makeMove(m)) {
            System.out.println("Illegal move.\n");
            first = true;
        } else {
            movePieces(m);
            switchMoveMarkers();
            first = true;
            if (isResult()) return;
            if (board.side == app.computerSide) {
                computerMove();
            }
        }
    }

    void computerMove() {
        moving = true;
        (new Thinker()).start();
    }

    void movePieces(Move m) {
        int fromRow = Board.ROW(m.from);
        int fromCol = Board.COL(m.from);
        int toRow = Board.ROW(m.to);
        int toCol = Board.COL(m.to);
        square[toRow][toCol].setIcon(new ImageIcon(pieceImage[board.color[m.to]][board.piece[m.to]]));
        square[fromRow][fromCol].setIcon(null);
        if ((m.bits & 2) != 0) {
            if (m.from == Board.E1 && m.to == Board.G1) movePieces(new Move(Board.H1, Board.F1, (char) 0, (char) 0)); else if (m.from == Board.E1 && m.to == Board.C1) movePieces(new Move(Board.A1, Board.D1, (char) 0, (char) 0)); else if (m.from == Board.E8 && m.to == Board.G8) movePieces(new Move(Board.H8, Board.F8, (char) 0, (char) 0)); else if (m.from == Board.E8 && m.to == Board.C8) movePieces(new Move(Board.A8, Board.D8, (char) 0, (char) 0));
        }
    }

    boolean isResult() {
        TreeSet validMoves = board.gen();
        Iterator i = validMoves.iterator();
        boolean found = false;
        while (i.hasNext()) {
            if (board.makeMove((Move) i.next())) {
                board.takeBack();
                found = true;
                break;
            }
        }
        String message = null;
        if (!found) {
            if (board.inCheck(board.side)) {
                if (board.side == Board.LIGHT) message = "0 - 1 Black mates"; else message = "1 - 0 White mates";
            } else message = "0 - 0 Stalemate";
        } else if (board.reps() == 3) message = "1/2 - 1/2 Draw by repetition"; else if (board.fifty >= 100) message = "1/2 - 1/2 Draw by fifty move rule";
        if (message != null) {
            int choice = JOptionPane.showConfirmDialog(this, message + "\nPlay again?", "Play Again?", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                app.playNewGame();
            }
            return true;
        }
        if (board.inCheck(board.side)) app.showStatus("Check!");
        return false;
    }

    /** Switches the "To Move" marker from white to black or vice versa
    */
    void switchMoveMarkers() {
        if (board.isWhiteToMove()) {
            wtm.setVisible(true);
            btm.setVisible(false);
        } else {
            btm.setVisible(true);
            wtm.setVisible(false);
        }
    }

    /** Resets the view */
    void reset() {
        repaintEverything();
        first = true;
    }

    /** Resets the piece icon in every square */
    void repaintEverything() {
        for (int i = 0; i < 8; i++) for (int j = 0; j < 8; j++) {
            int piece = board.getPiece(i, j);
            int color = board.getColor(i, j);
            if (piece != Board.EMPTY) square[i][j].setIcon(new ImageIcon(pieceImage[color][piece])); else square[i][j].setIcon(null);
        }
    }

    private String convertNum(int temp, boolean first) {
        if (first) return (new Character((char) ('a' + temp))).toString(); else return Integer.toString(8 - temp);
    }

    private static Image pieceImage[][] = new Image[2][6];

    private static String imageFilename[][] = { { "wp.gif", "wn.gif", "wb.gif", "wr.gif", "wq.gif", "wk.gif" }, { "bp.gif", "bn.gif", "bb.gif", "br.gif", "bq.gif", "bk.gif" } };

    public static void bufferImages(Applet app) {
        for (int i = 0; i < 2; i++) for (int j = 0; j < 6; j++) pieceImage[i][j] = app.getImage(app.getClass().getResource("/chessapp/images/" + imageFilename[i][j]));
    }

    class BLabel extends JLabel {

        BLabel(String s) {
            super(s);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }
    }

    class Thinker extends Thread {

        public void run() {
            searcher.think(1, maxTime, maxDepth);
            Move best = searcher.getBest();
            if (best.hashCode() == 0) {
                System.out.println("(no legal moves)");
                app.computerSide = Board.EMPTY;
                return;
            }
            board.makeMove(best);
            movePieces(best);
            switchMoveMarkers();
            isResult();
            moving = false;
        }
    }
}
