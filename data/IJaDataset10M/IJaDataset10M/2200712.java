package org.moltools.lib.seq.db.impl;

import java.util.Collection;
import org.moltools.lib.DuplicateIDException;
import org.moltools.lib.IDNotFoundException;
import org.moltools.lib.seq.*;
import org.moltools.lib.seq.db.SequenceDB;
import org.moltools.lib.seq.db.SubSequenceStringResolver;
import org.moltools.lib.seq.utils.NucleotideSequenceHandler;

/**A SequenceDB-based implementation of the SubSequenceStringResolver 
 * interface. This implementation uses a SequenceDB to look up the 
 * sequence in question. 
 * @author Johan Stenberg
 */
public class DefaultSubSequenceStringResolver implements SubSequenceStringResolver {

    /**The sequences*/
    protected SequenceDB sequences;

    /**
   * Create a new resolver.
   * @param sequences The sequenceDB that backs this resolver
   */
    public DefaultSubSequenceStringResolver(SequenceDB sequences) {
        this.sequences = sequences;
    }

    /**
   * Create a new resolver backed up by a database containing the specified sequences.
   * This method will create a HashMapSequenceDB and put the sequences into it.
   * @param seqs A collection of sequences to resolve 
   * @throws DuplicateIDException If any of the specified sequences have equal IDs
   */
    public DefaultSubSequenceStringResolver(Collection seqs) throws DuplicateIDException {
        sequences = new HashMapSequenceDB(seqs);
    }

    public String getSequenceString(SubSequenceDescriptor subseq) throws IDNotFoundException {
        Sequence parent = sequences.getSequenceByID(subseq.getParentID());
        if (parent == null) throw new IDNotFoundException(subseq.getParentID());
        int start = subseq.getStart();
        int end = subseq.getEnd();
        String sequence = parent.subsequence(start, end);
        if (parent instanceof NucleotideSequence) {
            if (subseq instanceof PolarSubSequenceDescriptor) {
                sequence = NucleotideSequenceHandler.getNucleotideSubSequenceString((NucleotideSequence) parent, (PolarSubSequenceDescriptor) subseq);
            } else throw new IllegalArgumentException("A polar sub-sequence descriptor is required for nucleotide sequence substring extraction");
        }
        return sequence;
    }
}
