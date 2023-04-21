package org.vizzini.game.boardgame.chess.standardtoken;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.vizzini.game.IPosition;
import org.vizzini.game.ITeam;
import org.vizzini.game.IToken;
import org.vizzini.game.IntegerPosition;
import org.vizzini.game.boardgame.chess.DefaultChessTokenTest;

/**
 * Provides unit tests for the <code>Mace</code> class.
 *
 * @author   Jeffrey M. Thompson
 * @version  v0.4
 * @since    v0.3
 */
public class MaceTest extends DefaultChessTokenTest {

    /**
     * Test the <code>getPossibleMovePositions()</code> method.
     *
     * @since  v0.3
     */
    public void testGetPossibleMovePositions() {
        Mace token3 = (Mace) _token3;
        System.out.println("position = " + _token3.getPosition());
        BitSet toBits = token3.getPossibleMovePositions(_board0, _adjudicator0);
        assertEquals(14, toBits.cardinality());
        Set<IntegerPosition> set = new TreeSet<IntegerPosition>();
        for (int j = toBits.nextSetBit(0); j >= 0; j = toBits.nextSetBit(j + 1)) {
            IntegerPosition toPosition = _board0.indexToPosition(j);
            set.add(toPosition);
        }
        assertEquals(14, set.size());
        {
            Iterator<IntegerPosition> iter = set.iterator();
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
        }
        Iterator<IntegerPosition> iter = set.iterator();
        assertEquals(IntegerPosition.get(0, 1, 2), iter.next());
        assertEquals(IntegerPosition.get(0, 1, 4), iter.next());
        assertEquals(IntegerPosition.get(0, 3, 2), iter.next());
        assertEquals(IntegerPosition.get(0, 3, 4), iter.next());
        assertEquals(IntegerPosition.get(2, 1, 2), iter.next());
        assertEquals(IntegerPosition.get(2, 1, 4), iter.next());
        assertEquals(IntegerPosition.get(2, 3, 2), iter.next());
        assertEquals(IntegerPosition.get(2, 3, 4), iter.next());
        assertEquals(IntegerPosition.get(3, 0, 1), iter.next());
        assertEquals(IntegerPosition.get(3, 0, 5), iter.next());
        assertEquals(IntegerPosition.get(3, 4, 1), iter.next());
        assertEquals(IntegerPosition.get(3, 4, 5), iter.next());
        assertEquals(IntegerPosition.get(4, 5, 0), iter.next());
        assertEquals(IntegerPosition.get(4, 5, 6), iter.next());
        assertFalse(iter.hasNext());
    }

    /**
     * @see  org.vizzini.game.boardgame.chess.DefaultChessTokenTest#create(org.vizzini.game.IPosition,
     *       java.lang.String, int, org.vizzini.game.ITeam)
     */
    @Override
    protected IToken create(IPosition position, String name, int value, ITeam team) {
        Mace answer = new Mace(position, team);
        return answer;
    }

    /**
     * @return  new test properties.
     *
     * @since   v0.3
     */
    protected Properties createProperties() {
        Properties properties = new Properties();
        properties.setProperty("adjudicator.class", "org.vizzini.game.boardgame.chess.ChessAdjudicator");
        properties.setProperty("environment.tokenCollection.class", "org.vizzini.game.TokenArrayCollection");
        properties.setProperty("environment.dimensions", "5, 6, 7");
        properties.setProperty("environment.pawn.isDoubleFirstMove", "true");
        properties.setProperty("environment.initialPosition.king.black", "2, 3, 3");
        properties.setProperty("environment.initialPosition.king.white", "2, 0, 0");
        properties.setProperty("environment.initialPosition.kingsRook.black", "3, 3, 3");
        properties.setProperty("environment.initialPosition.kingsRook.white", "3, 0, 0");
        properties.setProperty("environment.initialPosition.queensRook.black", "0, 3, 3");
        properties.setProperty("environment.initialPosition.queensRook.white", "0, 0, 0");
        properties.setProperty("team.0.class", "org.vizzini.game.DefaultTeam");
        properties.setProperty("team.0.name", "white");
        return properties;
    }
}
