package org.moyoman.util;

/** This exception is thrown if a full board position repeats itself,
  * as for example if a triple ko occurs.  It is <b>Not</b> thrown
  * for a regular ko, when an IllegalKoException would be thrown.
  */
public class IllegalSuperKoException extends IllegalMoveException {

    /** Create the IllegalSuperKoException object.
	  * @param st The stone that caused the illegal move.
	  */
    public IllegalSuperKoException(Stone st) {
        super(st);
    }
}
