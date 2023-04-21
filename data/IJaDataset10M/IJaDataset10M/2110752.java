package tests.board;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.Test;
import root.JChess;
import root.board.EmptyPlaceException;
import root.board.moviment.Moviment;
import root.board.moviment.Position;
import root.board.pieces.Horse;

/**
 * @author Calebe
 * 
 */
public class GetEnabledMovimentsHorce extends TestCase {

    JChess chess = new JChess();

    private int ammountMoviment(int x, int y) {
        ArrayList<Moviment> moviments = new ArrayList<Moviment>();
        try {
            moviments = chess.getEnabledMoviments(x, y);
        } catch (EmptyPlaceException e) {
            e.printStackTrace();
        }
        return moviments.size();
    }

    @Test
    public void testHorceStartPosition() throws EmptyPlaceException {
        chess = new JChess();
        chess.newGame();
        Position horcePosition = new Position(1, 0);
        if (chess.isPiecePresent(horcePosition)) {
            assertEquals(2, chess.getEnabledMoviments(horcePosition).size());
        }
    }

    @Test
    public void testNewGame() {
        int assertCount = 0;
        chess.newGame();
        try {
            if (chess.getPieceOn(2, 0) instanceof Horse) assertCount++;
            if (chess.getPieceOn(6, 0) != null) assertCount++;
        } catch (EmptyPlaceException e) {
            e.printStackTrace();
        }
    }
}
