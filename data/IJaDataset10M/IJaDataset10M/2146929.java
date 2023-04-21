package dovetaildb.querynode;

import dovetaildb.bytes.Bytes;
import dovetaildb.bytes.CompoundBytes;

public interface QueryNode {

    /** The document ID of the currently pointed to item.  This is valid at the time of creation. */
    public long doc();

    /** Moves to the next result.  Returns true if there is such a result. */
    public boolean next();

    /** Skips to the first whose document number is greater than or equal to target.  Remains in place is this requirement is already satisfied.  */
    public boolean skipTo(long target);

    public enum NextStatus {

        NEXT_TERM, NEXT_DOC, AT_END
    }

    ;

    /** Next term for current doc, false if no such term.  After a failed call, we are already pointing to the next document. */
    public NextStatus nextTerm();

    /** Position the term pointer back to the earliest term for the current doc id */
    public void resetTerms();

    /** The returned Bytes object is "borrowed" 
	 * Once nextTerm() or next() is called, 
	 * the second member of the CompoundBytes result may change, or 
	 * some the second member may remain the same but with new or changed data.
	 * The first member is guaranteed to never to change. */
    public Bytes term();
}
