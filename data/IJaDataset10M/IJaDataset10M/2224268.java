package java.util.regex;

/**
 * Represents node accepting single character from the given char class.
 * 
 * @author Nikolay A. Kuznetsov
 */
class RangeSet extends LeafSet {

    private AbstractCharClass chars;

    private boolean alt = false;

    public RangeSet(AbstractCharClass cs, AbstractSet next) {
        super(next);
        this.chars = cs.getInstance();
        this.alt = cs.alt;
    }

    public RangeSet(AbstractCharClass cc) {
        this.chars = cc.getInstance();
        this.alt = cc.alt;
    }

    public int accepts(int strIndex, CharSequence testString) {
        return chars.contains(testString.charAt(strIndex)) ? 1 : -1;
    }

    protected String getName() {
        return "range:" + (alt ? "^ " : " ") + chars.toString();
    }

    public boolean first(AbstractSet set) {
        if (set instanceof CharSet) {
            return AbstractCharClass.intersects(chars, ((CharSet) set).getChar());
        } else if (set instanceof RangeSet) {
            return AbstractCharClass.intersects(chars, ((RangeSet) set).chars);
        } else if (set instanceof SupplRangeSet) {
            return AbstractCharClass.intersects(chars, ((SupplRangeSet) set).getChars());
        } else if (set instanceof SupplCharSet) {
            return false;
        }
        return true;
    }

    protected AbstractCharClass getChars() {
        return chars;
    }
}
