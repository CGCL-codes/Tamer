package org.martho.game.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import org.martho.game.control.BoardListener;
import org.martho.game.model.Board;
import org.martho.game.model.ChessColor;
import org.martho.game.model.Field;
import org.martho.game.model.Figure;
import org.martho.game.model.Game;
import org.martho.game.model.Player;
import org.martho.game.model.figures.Pawn;
import org.martho.utils.Util;

public class ChessClient implements BoardListener {

    private Game game;

    private Player mrwhite;

    private Player mrblack;

    private boolean has_moved;

    public ChessClient() throws Exception {
        System.out.print("Spieler 1 (Mr. White): ");
        BufferedReader p1 = new BufferedReader(new InputStreamReader(System.in));
        String player1 = p1.readLine();
        mrwhite = new Player(player1, ChessColor.white);
        System.out.print("Spieler 2 (Mr. Black): ");
        BufferedReader p2 = new BufferedReader(new InputStreamReader(System.in));
        String player2 = p2.readLine();
        mrblack = new Player(player2, ChessColor.black);
        game = new Game(mrwhite, mrblack);
        game.getBoard().addListener(this);
        while (true) {
            System.out.println(game.getBoard().toString());
            System.out.print("Player: " + game.getCurrentPlayer().getName() + "\n");
            int[] von = null;
            int[] nach = null;
            String vonInput = null;
            String nachInput = null;
            do {
                System.out.print("Von: ");
                vonInput = Util.consoleInput();
                if (vonInput.equals("EXIT")) break;
                System.out.print("Nach: ");
                nachInput = Util.consoleInput();
                von = Util.convertChessCoordsToArrayCoords(vonInput);
                nach = Util.convertChessCoordsToArrayCoords(nachInput);
            } while (von == null || nach == null);
            System.out.println("\n\n\n\n\n\n\n\n\n");
            has_moved = game.moveTo(von, nach);
            if (has_moved == false) System.out.println("Konnte nicht von " + vonInput.toUpperCase() + " nach " + nachInput.toUpperCase() + " ziehen!!");
        }
    }

    public static void main(String args[]) {
        try {
            new ChessClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pawnAction(Pawn pawn) {
        System.out.println("PAWN on " + pawn.getField().getRow() + " : " + pawn.getField().getRow() + " wants an upgrade!!");
    }

    public void message(String msg) {
        System.out.println(msg);
    }

    public void check(Player p, List<Figure> figures) {
        System.out.println(p.getColor() + " is in check!!!");
    }

    public void checkMate(Player p) {
        System.out.println(p.getColor() + " is check mate");
    }
}
