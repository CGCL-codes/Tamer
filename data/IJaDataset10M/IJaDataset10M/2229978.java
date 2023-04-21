package ao.chess.v1.old;

import ao.chess.v1.model.Board;
import ao.chess.v1.model.Move;

/**
 *  class Perft
 *
 *  This class runs a test on different positions to make
 *  sure all moves are generated correctly.
 *  
 *  @author Jonatan Pettersson (mediocrechess@gmail.com)
 */
public class Perft {

    /**
	 * Start the perft search
	 * 
	 * @param board The board to search
	 * @param depth The depth to search to
	 * @param divide Should we divide the first moves or just return the total value
	 */
    public static void perft(Board board, int depth, boolean divide) {
        long time = System.currentTimeMillis();
        long zobrist = board.zobristKey;
        if (divide) {
            divide(board, depth);
        } else {
            System.out.println("Nodes found: " + miniMax(board, depth));
        }
        if (zobrist != board.zobristKey) System.out.println("Error in zobrist update!");
        System.out.println("Time used: " + convertMillis((System.currentTimeMillis() - time)));
    }

    /**
	 *  Keeps track of every starting move and its number of child moves,
	 *  and then prints it on the screen.
	 *
	 *  @param board The position to search
	 *  @param depth The depth to search to
	 */
    private static void divide(Board board, int depth) {
        int[] moves = new int[128];
        int totalMoves = board.generateMoves(false, moves, 0);
        Long[] children = new Long[128];
        for (int i = 0; i < totalMoves; i++) {
            board.makeMove(moves[i]);
            children[i] = miniMax(board, depth - 1);
            board.unmakeMove(moves[i]);
        }
        long nodes = 0;
        for (int i = 0; i < totalMoves; i++) {
            System.out.print(Move.inputNotation(moves[i]) + " ");
            System.out.println(children[i]);
            nodes += children[i];
        }
        System.out.println("Moves: " + totalMoves);
        System.out.println("Nodes: " + nodes);
    }

    /**
	 *  Generates every move from the position on board
	 *  and returns the total number of moves found
	 *  to the depth
	 *
	 *  @param board The board used
	 *  @param depth The depth currently at
	 *  @return int The number of moves found
	 */
    private static long miniMax(Board board, int depth) {
        long nodes = 0;
        if (depth == 0) return 1;
        int[] moves = new int[256];
        int totalMoves = board.generateMoves(false, moves, 0);
        for (int i = 0; i < totalMoves; i++) {
            board.makeMove(moves[i]);
            nodes += miniMax(board, depth - 1);
            board.unmakeMove(moves[i]);
        }
        return nodes;
    }

    /**
	 *  Takes number and converts it to minutes, seconds and fraction of a second
	 *  also includes leading zeros
	 *
	 *  @param millis the Milliseconds to convert
	 *  @return String the conversion
	 */
    public static String convertMillis(long millis) {
        long minutes = millis / 60000;
        long seconds = (millis % 60000) / 1000;
        long fracSec = (millis % 60000) % 1000;
        String timeString = "";
        if (minutes < 10 && minutes != 0) timeString += "0" + Long.toString(minutes) + ":"; else if (minutes >= 10) timeString += Long.toString(minutes) + ":";
        if (seconds == 0) timeString += "0"; else if (minutes != 0 && seconds < 10) timeString += "0" + Long.toString(seconds); else if (seconds < 10) timeString += Long.toString(seconds); else timeString += Long.toString(seconds);
        timeString += ".";
        if (fracSec == 0) timeString += "000"; else if (fracSec < 10) timeString += "00" + Long.toString(fracSec); else if (fracSec < 100) timeString += "0" + Long.toString(fracSec); else timeString += Long.toString(fracSec);
        return timeString;
    }
}
