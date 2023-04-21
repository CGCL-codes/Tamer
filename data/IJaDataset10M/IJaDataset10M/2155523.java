package org.parallelj.mda.controlflow.diagram.extension.adapters;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.parallelj.mda.controlflow.diagram.edit.parts.TransitionPredicateInfoEditPart;
import org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage;

/**
 * Refreshment adapter for Predicate Info label, when predicate is bound/unbound
 * 
 * @author Atos Worldline
 */
public class TransitionPredicateRefreshmentAdapter extends AbstractRefreshmentAdapter {

    public TransitionPredicateRefreshmentAdapter(AbstractGraphicalEditPart editPart) {
        super(editPart);
    }

    @Override
    protected EStructuralFeature getFeature() {
        return ControlFlowPackage.eINSTANCE.getTransition_Predicate();
    }

    @Override
    protected int[] getVisualIDsOfEditPartsToRefresh() {
        return new int[] { TransitionPredicateInfoEditPart.VISUAL_ID };
    }
}
