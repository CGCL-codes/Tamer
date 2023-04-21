package org.norecess.nolatte.ast;

import org.norecess.nolatte.ast.visitors.DatumVisitor;
import org.norecess.nolatte.types.WhitespaceDatumStripper;

/**
 * Datum with whitespace in front of it.
 * 
 * @author Jeremy D. Frens
 */
public class WhitespaceDatum extends WhitespaceLexpr<Datum> implements IWhitespaceDatum {

    private static final long serialVersionUID = -5171036841512361267L;

    private static final WhitespaceDatumStripper STRIPPER = new WhitespaceDatumStripper();

    public WhitespaceDatum(IText whitespace, Datum datum) {
        super(whitespace, datum.accept(STRIPPER));
    }

    public boolean isTrue() {
        return getLexpr().isTrue();
    }

    public boolean isApplicable() {
        return getLexpr().isApplicable();
    }

    public Datum getDatum() {
        return getLexpr();
    }

    public <T> T accept(DatumVisitor<T> visitor) {
        return visitor.visitWhitespaceDatum(this);
    }
}
