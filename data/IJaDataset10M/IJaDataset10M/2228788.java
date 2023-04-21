package edu.princeton.cogsci.wn;

/**
 *  This class models a relation between lexical entries, or word senses.
 *
 */
public class LexicalRelation {

    /** The type of relation. */
    public int type;

    /** The key identifying the destination lexical entry. */
    public WordSenseKey key;

    public LexicalRelation(int type, WordSenseKey key) {
        this.type = type;
        this.key = key;
    }
}
