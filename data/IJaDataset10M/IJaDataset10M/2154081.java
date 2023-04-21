package org.fbc.shogi.movesgen;

import java.util.Vector;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.fbc.shogi.movesgen.gen.MovesGenerator;
import org.fbc.shogi.movesgen.gui.ConsoleBoard;
import org.fbc.shogi.movesgen.model.Board;
import org.fbc.shogi.movesgen.model.Move;
import org.fbc.shogi.spdparser.SPDParser;

public class MovesGeneratorTest extends TestCase {

    private static final Logger log = Logger.getLogger(MovesGeneratorTest.class.getName());

    public void testIsCheckMate1() {
        log.info("testischeckmate1");
        Board b = SPDParser.parse("8k/8G/9/9/8K/9/9/9/9 W -");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        assertEquals(false, mg.isCheckMate(false));
    }

    public void testIsCheckMate2() {
        Board b = SPDParser.parse("8k/8G/8G/9/8K/9/9/9/9 W -");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        assertEquals(true, mg.isCheckMate(false));
    }

    public void testIsCheckMate3() {
        Board b = SPDParser.parse("6G1k/9/8R/9/8K/9/9/9/9 W -");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        assertEquals(false, mg.isCheckMate(false));
    }

    public void testIsCheckMate4() {
        Board b = SPDParser.parse("6G1k/9/8+R/9/8K/9/9/9/9 W -");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        assertEquals(true, mg.isCheckMate(false));
    }

    public void testIsCheckMate5() {
        Board b = SPDParser.parse("6G1k/9/8+R/9/8K/9/9/9/9 W p");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        assertEquals(false, mg.isCheckMate(false));
    }

    public void testPawnDropCannotCheckmate() {
        Board b = SPDParser.parse("6G1k/9/8G/9/8K/9/9/9/9 B P");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        Vector<Move> moves = mg.generateValidMoves();
        System.out.println("moves.count" + moves.size());
        cb.showBoard();
        for (Move m : moves) {
            if (m.isDrop()) {
                System.out.print("(" + m.getPiece() + ")" + m.getASCIImove() + ", ");
            }
        }
        assertEquals(81, moves.size());
    }

    public void testCannotTakeKing() {
        Board b = SPDParser.parse("8k/8G/9/9/8K/9/9/9/9 B -");
        ConsoleBoard cb = new ConsoleBoard(b);
        cb.showBoard();
        MovesGenerator mg = new MovesGenerator(b);
        Vector<Move> moves = mg.generateValidMoves();
        System.out.println("moves.count" + moves.size());
        cb.showBoard();
        for (Move m : moves) {
            System.out.println(m.getASCIImove());
            if (m.isTake()) {
                fail("Cannot take king! (" + m.toString() + ")");
            }
        }
    }
}
