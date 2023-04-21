package org.datanucleus.util;

import javax.jdo.datastore.Sequence;

/**
 * Example of a factory class for a sequence.
 */
public class SimpleSequenceFactory {

    public static Sequence newInstance(String name) {
        return new SimpleSequence(name);
    }

    public static Sequence newInstance(String name, String strategy) {
        return new SimpleSequence(name);
    }
}
