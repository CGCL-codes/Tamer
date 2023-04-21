package org.openscience.miniJmol;

import java.util.*;
import java.io.*;

/**
 * Collection of AtomTypes. No duplicates are allowed. This class is
 * implemented with Hashtable to allow compatability with Java 1.1.
 */
public class AtomTypeSet extends Hashtable {

    /**
     * Adds the specified AtomType to this set if it is not already
     * present.
     *
     * @param at  AtomType to be added to the set.
     * @returns true if the set did not already contain the AtomType.
     */
    boolean add(BaseAtomType at) {
        if (contains(at)) {
            return false;
        } else {
            put(at.getName(), at);
            return true;
        }
    }

    /**
     * Loads AtomTypes from a Reader.
     */
    public void load(InputStream input) throws IOException {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(input), 1024);
        clear();
        String line = br1.readLine();
        while (line != null) {
            if (!line.startsWith("#")) {
                add(BaseAtomType.parse(line));
            }
            line = br1.readLine();
        }
    }
}
