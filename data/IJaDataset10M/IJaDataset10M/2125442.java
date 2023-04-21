package pcgen.util.testchecker;

import pcgen.util.TestChecker;

/**
 * Compares booleans
 */
public class CompareEqualBoolean extends TestChecker {

    private boolean bool;

    /**
	 * Constructor
	 * @param bool
	 */
    public CompareEqualBoolean(boolean bool) {
        this.bool = bool;
    }

    public boolean check(Object obj) {
        return obj.equals(new Boolean(this.bool));
    }

    public StringBuffer scribe(StringBuffer buf) {
        buf.append("a ");
        buf.append(this.bool);
        buf.append(" value");
        return buf;
    }
}
