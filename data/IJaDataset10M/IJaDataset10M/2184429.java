package com.lucene.index;

import java.io.IOException;

/** Abstract class for enumerating terms.

  <p>Term enumerations are always ordered by Term.compareTo().  Each term in
  the enumeration is greater than all that precede it.  */
public abstract class TermEnum {

    /** Increments the enumeration to the next element.  True if one exists.*/
    public abstract boolean next() throws IOException;

    /** Returns the current Term in the enumeration.
    Initially invalid, valid after next() called for the first time.*/
    public abstract Term term();

    /** Returns the docFreq of the current Term in the enumeration.
    Initially invalid, valid after next() called for the first time.*/
    public abstract int docFreq();

    /** Closes the enumeration to further activity, freeing resources. */
    public abstract void close() throws IOException;
}
