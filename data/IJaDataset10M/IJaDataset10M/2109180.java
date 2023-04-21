package net.obsearch.exception;

/**
 * Occurs when an internal id requested but the id is not available.
 * This Exception is kept as a debugging aid. Normally you should
 * not receive it. If you do, please report the problem to:
 * http://code.google.com/p/obsearch/issues/list
 * @author Arnoldo Jose Muller Molina
 * @since 0.7
 */
public class IllegalIdException extends OBException {

    private long id;

    private byte[] idbytes;

    public String toString() {
        return "Illegal id was received :( : " + id;
    }

    public IllegalIdException(long id) {
        this.id = id;
    }

    public IllegalIdException() {
        this.id = -1;
    }

    public IllegalIdException(byte[] i) {
        this.idbytes = i;
    }
}
