package eu.redseeds.sc.merging.resolvers;

import org.eclipse.core.runtime.IStatus;
import eu.redseeds.sc.merging.Activator;
import eu.redseeds.sc.merging.MergeConstants;
import eu.redseeds.sc.merging.conflicts.MergeConflictObject;
import eu.redseeds.scl.model.sdsl.PackageDTO;

public class MergeResolverUMLPackage implements IMergeResolver {

    @Override
    public void resolve(MergeConflictObject conflict) {
        if (!validateConflict(conflict)) {
            Activator.log("Invalid merge object", IStatus.WARNING);
            return;
        }
        if (conflict.getConflictType() == MergeConstants.MERGE_CONFLICT_NAMING_PACKAGE_ID) {
            if (conflict.getConflictAction() == MergeConstants.MERGE_ACTION_OVERWRITE_ID) {
                ((PackageDTO) conflict.getCurrentObject()).delete();
            }
            if (conflict.getConflictAction() == MergeConstants.MERGE_ACTION_SKIP_ID) {
                ((PackageDTO) conflict.getPastObject()).delete();
            }
            if (conflict.getConflictAction() == MergeConstants.MERGE_ACTION_AUTOSOLVE_ID) {
                String oldName = ((PackageDTO) conflict.getPastObject()).getName();
                ((PackageDTO) conflict.getPastObject()).setName(oldName + MergeConstants.MERGE_CONFLICT_NAMING_RESOLVER_POSTFIX);
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
        if (!(conflict.getCurrentObject() instanceof PackageDTO)) {
            return false;
        }
        if (!(conflict.getPastObject() instanceof PackageDTO)) {
            return false;
        }
        return true;
    }
}
