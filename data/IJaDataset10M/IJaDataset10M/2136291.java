package net.sf.mpxj.utility;

/**
 * Trivial class implementing sequence generation.
 */
public class Sequence {

    /**
    * Constructor. Defines the starting value of the seqence.
    * 
    * @param initialValue initial sequence value
    */
    public Sequence(int initialValue) {
        m_sequence = initialValue;
    }

    /**
    * Retrieve the next value in the sequence.
    * 
    * @return next sequence value
    */
    public int next() {
        return (m_sequence++);
    }

    private int m_sequence;
}
