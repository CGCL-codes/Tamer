package it.chesslab.scoresheet;

import it.chesslab.chessboard.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**  */
public final class ScoresheetMove {

    /**  */
    private final Scoresheet scoresheet;

    /**  */
    private final String notation;

    private final int color;

    /**  */
    private List nags = null;

    /**  */
    ScoresheetMove(Scoresheet scoresheet, String notation, int color) {
        this.scoresheet = scoresheet;
        this.notation = notation;
        this.color = color;
    }

    /**  */
    public final String getNotation() {
        return this.notation;
    }

    /**  */
    public final int getColor() {
        return this.color;
    }

    /**  */
    public final boolean isWhiteMove() {
        return this.color == Color.WHITE;
    }

    /**  */
    public final boolean isBlackMove() {
        return this.color == Color.BLACK;
    }

    /**  */
    public void addNag(int nag) {
        if (nag < 1 || nag > 255) {
            throw new IllegalArgumentException();
        }
        if (this.nags == null) {
            this.nags = new ArrayList();
        }
        this.nags.add(new Integer(nag));
        this.scoresheet.setModified(true);
    }

    /**  */
    public final List getNags() {
        return this.nags;
    }

    /**  */
    public final String getMovePgnFormat() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.notation);
        if (this.nags != null) {
            Iterator nagsIterator = this.nags.iterator();
            while (nagsIterator.hasNext()) {
                Integer nag = (Integer) nagsIterator.next();
                buf.append(' ').append('$').append(nag.intValue());
            }
        }
        return buf.toString();
    }
}
