package javax.xml.rpc.holders;

/**
 * Holder for <code>long</code>s.
 * 
 * @version 1.0
 */
public final class LongHolder implements Holder {

    /** The <code>long</code> contained by this holder. */
    public long value;

    /**
	 * Make a new <code>LongHolder</code> with a <code>null</code> value.
	 */
    public LongHolder() {
    }

    /**
	 * Make a new <code>LongHolder</code> with <code>value</code> as the value.
	 * 
	 * @param value
	 *            the <code>long</code> to hold
	 */
    public LongHolder(long value) {
        this.value = value;
    }
}
