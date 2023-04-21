package yore.boardgames.c4;

import yore.ai.*;
import java.util.*;

public class C4Heuristic1 implements Heuristic {

    public double evaluate(Object state) {
        C4GameState c4State = (C4GameState) state;
        LinkedList players = c4State.getPlayerQueue();
        char mySymbol = ((Player) players.getFirst()).getSymbol();
        char yourSymbol = ((Player) players.getLast()).getSymbol();
        char[][] squares = c4State.getSquares();
        int total = 0;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                if (squares[i][j] == '-') {
                    squares[i][j] = mySymbol;
                    if (C4GameState.moveCausedWin(squares, i, j)) total += 10;
                    squares[i][j] = yourSymbol;
                    if (C4GameState.moveCausedWin(squares, i, j)) total -= 10;
                    squares[i][j] = '-';
                }
            }
        }
        double t = total / 100.0;
        if (t > 0.99) return 0.99;
        if (t < -0.99) return -0.99;
        return t;
    }
}
