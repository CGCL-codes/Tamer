package eu.redseeds.sc.merging.resolvers;

import org.eclipse.core.runtime.IStatus;
import eu.redseeds.sc.merging.Activator;
import eu.redseeds.sc.merging.MergeConstants;
import eu.redseeds.sc.merging.conflicts.MergeConflictObject;
import eu.redseeds.scl.model.rsl.rsldomainelements.terms.TermDTO;

public class MergeResolverTerm implements IMergeResolver {

    @Override
    public void resolve(MergeConflictObject conflict) {
        if (!validateConflict(conflict)) {
            Activator.log("Invalid merge object", IStatus.WARNING);
            return;
        }
        if (conflict.getConflictType() == MergeConstants.MERGE_CONFLICT_MEANING_TERM_ID) {
            TermDTO currentTerm = (TermDTO) conflict.getCurrentObject();
            TermDTO pastTerm = (TermDTO) conflict.getPastObject();
            if (conflict.getConflictAction() == MergeConstants.MERGE_ACTION_OVERWRITE_ID) {
                currentTerm.setSynonymUid(pastTerm.getSynonymUid());
            }
            if (conflict.getConflictAction() == MergeConstants.MERGE_ACTION_SKIP_ID) {
                pastTerm.setSynonymUid(currentTerm.getSynonymUid());
            }
            if (conflict.getConflictAction() == MergeConstants.MERGE_ACTION_AUTOSOLVE_ID) {
                pastTerm.setSynonymUid(currentTerm.getSynonymUid());
            }
        }
    }

    @Override
    public boolean validateConflict(MergeConflictObject conflict) {
        if (conflict == null) {
            return false;
        }
        if (conflict.getCurrentObject() == null || conflict.getPastObject() == null) {
            return false;
        }
        if (!(conflict.getCurrentObject() instanceof TermDTO)) {
            return false;
        }
        if (!(conflict.getPastObject() instanceof TermDTO)) {
            return false;
        }
        return true;
    }
}
