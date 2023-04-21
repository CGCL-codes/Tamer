package org.databene.mad4db.compare;

import java.util.Arrays;
import java.util.List;
import org.databene.commons.ArrayUtil;
import org.databene.jdbacl.model.DBIndex;
import org.databene.mad4db.ComparisonConfig;
import org.databene.mad4db.cmd.CompositeStructuralChange;
import org.databene.mad4db.cmd.IndexCreation;
import org.databene.mad4db.cmd.IndexDeletion;
import org.databene.mad4db.cmd.Rename;
import org.databene.mad4db.cmd.StructuralChange;

/**
 * Compares lists of indexes.<br/><br/>
 * Created: 12.07.2011 13:44:56
 * @since 0.2
 * @author Volker Bergmann
 */
public class IndexListComparator extends UnorderedListComparator<DBIndex> {

    public IndexListComparator(ComparisonConfig config) {
        super(new IndexComparator(config));
    }

    public boolean accept(DBIndex candidate) {
        return true;
    }

    @Override
    protected int searchAndProcessObjectSimilarTo(DBIndex originalIndex, List<DBIndex> candidates, CompositeStructuralChange<?> ownerChange) {
        String[] searchedColumnNames = originalIndex.getColumnNames();
        for (int i = 0; i < candidates.size(); i++) {
            DBIndex candidate = candidates.get(i);
            String[] candidateColumnNames = candidate.getColumnNames();
            if (Arrays.equals(searchedColumnNames, candidateColumnNames)) {
                processRenamed(originalIndex, candidate, ownerChange);
                return i;
            }
        }
        for (int i = 0; i < candidates.size(); i++) {
            DBIndex candidate = candidates.get(i);
            String[] candidateColumnNames = candidate.getColumnNames();
            if (ArrayUtil.equalsIgnoreOrder(searchedColumnNames, candidateColumnNames)) {
                processRenamed(originalIndex, candidate, ownerChange);
                if (!originalIndex.isNameDeterministic() || !candidate.isNameDeterministic()) ownerChange.addSubChange(new Rename<DBIndex>(originalIndex, candidate.getName()));
                return i;
            }
        }
        return -1;
    }

    protected void processRenamed(DBIndex originalIndex, DBIndex newIndex, CompositeStructuralChange<?> ownerChange) {
        if (originalIndex.isNameDeterministic() || newIndex.isNameDeterministic()) ownerChange.addSubChange(new Rename<DBIndex>(originalIndex, newIndex.getName()));
    }

    public StructuralChange<DBIndex> creation(DBIndex index) {
        return new IndexCreation(index);
    }

    public StructuralChange<DBIndex> deletion(DBIndex index) {
        return new IndexDeletion(index);
    }
}
