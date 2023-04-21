package org.databene.mad4db.compare;

import java.util.List;
import org.databene.jdbacl.model.DBSequence;
import org.databene.mad4db.ComparisonConfig;
import org.databene.mad4db.cmd.CompositeStructuralChange;
import org.databene.mad4db.cmd.SequenceCreation;
import org.databene.mad4db.cmd.SequenceDeletion;
import org.databene.mad4db.cmd.StructuralChange;

/**
 * Compares lists of sequences.<br/><br/>
 * Created: 12.07.2011 13:41:08
 * @since 0.2
 * @author Volker Bergmann
 */
public class SequenceListComparator extends UnorderedListComparator<DBSequence> {

    public SequenceListComparator(ComparisonConfig config) {
        super(new SequenceComparator(config));
    }

    public boolean accept(DBSequence candidate) {
        return true;
    }

    @Override
    protected int searchAndProcessObjectSimilarTo(DBSequence object, List<DBSequence> candidates, CompositeStructuralChange<?> ownerChange) {
        return -1;
    }

    public StructuralChange<DBSequence> creation(DBSequence sequence) {
        return new SequenceCreation(sequence);
    }

    public StructuralChange<DBSequence> deletion(DBSequence sequence) {
        return new SequenceDeletion(sequence);
    }
}
