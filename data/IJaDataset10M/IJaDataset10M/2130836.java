package mx.cinvestav.josorio.gosimulator.player;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import mx.cinvestav.josorio.gosimulator.Analyzer;
import mx.cinvestav.josorio.gosimulator.Pair;

/**
 * Bridge to communicate with the GNU Go player.
 * @author Moises Osorio
 *
 */
public class GNUGoPlayer extends Player {

    private Process gnugo;

    private BufferedReader in;

    private PrintWriter out;

    private PlayerType[][] lastBoard;

    public GNUGoPlayer() {
    }

    @Override
    public void init(int n, PlayerType turn) {
        super.init(n, turn);
        try {
            ProcessBuilder builder = new ProcessBuilder("gnugo", "--mode", "gtp");
            builder.redirectErrorStream(true);
            gnugo = builder.start();
            in = new BufferedReader(new InputStreamReader(gnugo.getInputStream()));
            out = new PrintWriter(new BufferedOutputStream(gnugo.getOutputStream()));
            print("boardsize " + n);
            read();
            read();
            print("level 10");
            read();
            read();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void print(String s) {
        out.println(s);
        out.flush();
    }

    private String read() {
        try {
            String s = in.readLine();
            return s;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected Pair doMakeTurn(Analyzer analysis) {
        Pair opponentsMove = getOpponentMoveFromBoard(analysis.getBoard());
        if (opponentsMove != null) setOpponentGNUGoMove(opponentsMove.getA(), opponentsMove.getB());
        Pair move = getGNUGoMove();
        lastBoard = analysis.getBoard();
        return move;
    }

    private void showBoard() {
        print("showboard");
        for (int i = 0; i < n + 4; i++) read();
        analysis.printBoard();
    }

    private void setOpponentGNUGoMove(int x, int y) {
        String m = (char) ('A' + (y >= 8 ? y + 1 : y)) + "" + (n - x);
        String move = "play " + (type == PlayerType.BLACK ? "white" : "black") + " " + m;
        print(move);
        read();
        read();
    }

    private Pair getGNUGoMove() {
        print("kgs-genmove_cleanup " + (type == PlayerType.WHITE ? "white" : "black"));
        String m = read().substring(2);
        read();
        if (m == null || m.equalsIgnoreCase("pass")) {
            System.out.println("Got '" + m + "' from GNU Go");
            return null;
        }
        int y = m.charAt(0) - 'A';
        if (y >= 8) y--;
        int x = n - Integer.parseInt(m.substring(1));
        return new Pair(x, y);
    }

    private Pair getOpponentMoveFromBoard(PlayerType[][] board) {
        if (lastBoard != null) for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) if (board[i][j] != lastBoard[i][j] && board[i][j].ordinal() == 1 - type.ordinal()) return new Pair(i, j);
        return null;
    }

    @Override
    public void stop() {
        super.stop();
        print("quit");
        gnugo.destroy();
    }

    @Override
    protected void finalize() throws Throwable {
        stop();
        super.finalize();
    }
}
