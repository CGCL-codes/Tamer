package aie.common;

import java.io.*;
import java.math.BigInteger;

/**
 * Generates IDs given a prefix.  Helps to ensure unique IDs for each object instance.
 */
public class IDGenerator implements Serializable {

    protected String prefix;

    protected BigInteger nextid;

    /**
     * Creates a new IDGenerator that generates unique ids with the given prefix.
     * 
     * @param  prefix the prefix to use
     */
    public IDGenerator(String prefix) {
        this.prefix = prefix;
        nextid = BigInteger.valueOf(0);
    }

    /**
     * Returns the next available ID.
     * 
     * @return the next ID
     */
    public ID getNextID() {
        ID id = new ID(prefix, nextid);
        nextid = nextid.add(BigInteger.valueOf(1));
        return id;
    }
}
