package tgreiner.amy.chess.book;

import java.util.List;
import java.util.Iterator;
import java.util.Random;
import tgreiner.amy.chess.engine.ChessBoard;
import tgreiner.amy.chess.engine.Move;
import org.apache.log4j.Logger;

/**
 * Selects opening book moves.
 *
 * @author <a href="mailto:thorsten.greiner@googlemail.com">Thorsten Greiner</a>
 */
public class BookMoveSelectorImpl implements BookMoveSelector {

    /** The log. */
    private static Logger log = Logger.getLogger(BookMoveSelectorImpl.class);

    /** The book database. */
    private BookDB db;

    /** Generates random numbers. */
    private Random random = new Random();

    /**
     * Create a BookMoveSelector.
     *
     * @param theDB the book db
     */
    public BookMoveSelectorImpl(final BookDB theDB) {
        this.db = theDB;
    }

    /** @see BookMoveSelector#selectMove */
    public int selectMove(final ChessBoard board) throws Exception {
        List moves = db.get(board.getPosHash());
        if (moves != null) {
            Iterator iter = moves.iterator();
            int count = 0;
            while (iter.hasNext()) {
                BookEntry entry = (BookEntry) iter.next();
                if (log.isDebugEnabled()) {
                    log.debug(Move.toSAN(board, entry.getMove()) + ": " + entry.getCount());
                }
                count += entry.getCount();
            }
            if (count > 0) {
                int r = random.nextInt(count);
                iter = moves.iterator();
                while (iter.hasNext()) {
                    BookEntry entry = (BookEntry) iter.next();
                    r -= entry.getCount();
                    if (r < 0) {
                        return entry.getMove();
                    }
                }
            }
        }
        return 0;
    }
}
