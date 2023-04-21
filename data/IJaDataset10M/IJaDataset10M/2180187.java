package net.sf.refactorit.refactorings.conflicts;

import net.sf.refactorit.refactorings.conflicts.resolution.ConflictResolution;
import java.util.List;

/**
 *
 * @author vadim
 */
public interface MultipleResolveConflict {

    public void addPossibleResolution(ConflictResolution resolution);

    public List getPossibleResolutions();
}
