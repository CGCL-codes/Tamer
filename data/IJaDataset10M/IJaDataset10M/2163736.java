package sexpression;

/**
 * An instance of this class represents a list wildcard that constrains slightly
 * more the contents of the list. Each element in the list much match a
 * particular pattern.
 * 
 * @author kyle
 * 
 */
public class ListWildcard extends AWildcard {

    private final ASExpression _pattern;

    public ListWildcard(ASExpression pattern) {
        _pattern = pattern;
    }

    /**
     * @see sexpression.ASExpression#match(sexpression.ASExpression)
     */
    @Override
    public ASExpression match(ASExpression target) {
        if (!(target instanceof ListExpression)) return NoMatch.SINGLETON;
        ListExpression lst = (ListExpression) target;
        for (ASExpression ase : lst) if (_pattern.match(ase) == NoMatch.SINGLETON) return NoMatch.SINGLETON;
        return new ListExpression(target);
    }

    /**
     * @see sexpression.ASExpression#toStringHelp()
     */
    @Override
    public StringBuffer toStringHelp() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("#list:");
        buffer.append(_pattern.toStringHelp());
        buffer.append("");
        return buffer;
    }

    /**
     * @see sexpression.ASExpression#size()
     */
    @Override
    public int size() {
        return 0;
    }

    /**
     * @see sexpression.ASExpression#toVerbatimHelp()
     */
    @Override
    public ByteArrayBuffer toVerbatimHelp() {
        ByteArrayBuffer ba = new ByteArrayBuffer();
        ba.append((byte) '#');
        ba.append((byte) 'l');
        ba.append(_pattern.toVerbatimHelp());
        return ba;
    }

    /**
     * @see sexpression.ASpecialExpression#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ListWildcard)) return false;
        return ((ListWildcard) o)._pattern.equals(_pattern);
    }
}
