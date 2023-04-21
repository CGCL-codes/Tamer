package net.sf.osadm.linedata.table;

/**
 * When input or output values needs some kind of cleaning / enrichment, 
 * a instance of this interface can do that job. 
 */
public interface ValuePolisher {

    /**
     * Returns a cleaned / enriched output value, or just the same, if the 
     * polishing does not apply for the given filter.
     * 
     * @param value - the input value
     * @return A cleaned / enriched output value (or just the same).
     */
    String polishValue(String value);
}
