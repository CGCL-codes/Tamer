package org.modelversioning.core.diff.engine.impl;

import org.eclipse.emf.compare.diff.metamodel.DiffModel;
import org.eclipse.emf.compare.diff.service.DiffService;
import org.eclipse.emf.compare.match.metamodel.MatchModel;
import org.modelversioning.core.diff.engine.IDiffEngine;

/**
 * Implementation of the {@link IDiffEngine} using the EMF Compare plug-in to
 * calculate and generate the {@link DiffModel}.
 * 
 * <p>
 * This {@link IDiffEngine} will use the {@link DiffService} of EMF Compare, so
 * EMF Compare will additionally select the best DiffEngine to use depending on
 * the models in the specified {@link MatchModel}. Therefore this plug-in also
 * depends on the <i>configuration</i> and locally configured <i>extensions</i>
 * of the EMF Compare Diff plug-in.
 * </p>
 * 
 * @author <a href="mailto:langer@big.tuwien.ac.at">Philip Langer</a>
 * 
 */
public class EMFCompareDiffEngine implements IDiffEngine {

    @Override
    public DiffModel generateDiffModel(MatchModel matchModel) {
        return DiffService.doDiff(matchModel, isThreeWayMatch(matchModel));
    }

    /**
	 * Returns <code>true</code> if the specified <code>matchModel</code>
	 * represents a match of three models (e.g. there is a origin model
	 * provided). Otherwise <code>false</code> is returned.
	 * 
	 * @param matchModel
	 *            {@link MatchModel} to check.
	 * @return <code>true</code> if the specified <code>matchModel</code>
	 *         represents a match of three models. Otherwise <code>false</code>.
	 */
    private boolean isThreeWayMatch(MatchModel matchModel) {
        return (matchModel.getAncestorRoots() != null && matchModel.getAncestorRoots().size() > 0);
    }
}
