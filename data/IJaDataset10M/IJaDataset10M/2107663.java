package rkr_gst;

import com.sun.java_cup.internal.runtime.Symbol;
import java.util.*;

/**
 * Stores informations about differences between two compared lexical-element-sequences.
 * @author Ondrej Guth
 */
public final class Difference {

    /**
	 * Store lexical elements of one tile and its starting positions in older and newer version.
	 */
    public static final class MatchingBlock extends Vector<Symbol> {

        /**
		 * starting position of the tile in older version
		 */
        public int p;

        /**
		 * starting position of the tile in newer version
		 */
        public int t;

        /**
		 * Create a new instance of MatchingBlock.
		 * @param match <html>
		 *  <head>
		 *    
		 *  </head>
		 *  <body>
		 *    a sequence of lexical elements of one tile
		 *  </body>
		 * </html>
		 * @param beginInP <html>
		 *  <head>
		 *    
		 *  </head>
		 *  <body>
		 *    position of first element of the tile in older version
		 *  </body>
		 * </html>
		 * @param beginInT <html>
		 *  <head>
		 *    
		 *  </head>
		 *  <body>
		 *    position of first element of the tile in newer version
		 *  </body>
		 * </html>
		 */
        MatchingBlock(final Vector<Symbol> match, final int beginInP, final int beginInT) {
            super(match);
            p = beginInP;
            t = beginInT;
        }
    }

    /**
	 * lexical elements which are placed in older version but not in newer
	 */
    public Vector<Symbol> deleted;

    /**
	 * lexical elements which are placed in newer version but not in older
	 */
    public Vector<Symbol> inserted;

    /**
	 * lexical elements which are parts of blocks (tiles) which are common for both versions
	 */
    public Vector<MatchingBlock> moved;

    /**
	 * number of blocks with distinct move-length (distance between position in older and newer version)
	 */
    public int noOfDistinctMoves;

    /**
	 * Create a new instance of Difference
	 * @param unmarkedP <html>
	 *  <head>
	 *    
	 *  </head>
	 *  <body>
	 *    reference to list of unmarked elements remaining in an older version
	 *  </body>
	 * </html>
	 * @param T <html>
	 *  <head>
	 *    
	 *  </head>
	 *  <body>
	 *    collection containing all elements from newer version
	 *  </body>
	 * </html>
	 */
    public Difference(final LinkedElementItem unmarkedP, final LinkedListOfElements T) {
        initDeleted(unmarkedP);
        initInsertedAndMoved(T);
    }

    /**
	 * Initiate inserted and moved with appropriate elements.
	 * @param T <html>
	 *  <head>
	 * 
	 *  </head>
	 *  <body>
	 *    collection containing elements from newer version
	 *  </body>
	 * </html>
	 */
    protected void initInsertedAndMoved(final LinkedListOfElements T) {
        inserted = new Vector<Symbol>();
        moved = new Vector<MatchingBlock>();
        Iterator<LinkedListOfElementsItem> it = T.iterator();
        Set<Integer> moveSizes = new HashSet<Integer>();
        if (it.hasNext()) {
            LinkedListOfElementsItem e = it.next();
            if (e != null) do {
                if (e.elementOrder() < 0) break;
                if (e.refToUnmarked != null) {
                    inserted.add(e.getSymbol());
                    e = it.next();
                } else {
                    final int t = e.match.t;
                    final int p = e.match.p;
                    Vector<Symbol> subseq = new Vector<Symbol>(e.match.L);
                    while (it.hasNext() && e.match != null && t == e.match.t) {
                        subseq.add(e.getSymbol());
                        e = it.next();
                    }
                    if (t != p) {
                        moveSizes.add(new Integer(t - p));
                        moved.add(new MatchingBlock(subseq, p, t));
                    }
                }
            } while (it.hasNext());
        }
        noOfDistinctMoves = moveSizes.size();
    }

    /**
	 * Initiate deleted with appropriate elements.
	 * @param unmarkedP <html>
	 *  <head>
	 * 
	 *  </head>
	 *  <body>
	 *    reference to a list of unmarked elements from older version
	 *  </body>
	 * </html>
	 */
    protected void initDeleted(final LinkedElementItem unmarkedP) {
        deleted = new Vector<Symbol>();
        LinkedElementItem item = unmarkedP;
        while (item != null) {
            deleted.add(item.refToElement.getSymbol());
            item = item.next;
        }
    }
}
