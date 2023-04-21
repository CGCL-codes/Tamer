package org.eclipse.emf.compare.diff.merge.internal.impl;

import java.util.Iterator;
import org.eclipse.emf.compare.EMFComparePlugin;
import org.eclipse.emf.compare.FactoryException;
import org.eclipse.emf.compare.diff.merge.api.DefaultMerger;
import org.eclipse.emf.compare.diff.metamodel.DiffElement;
import org.eclipse.emf.compare.diff.metamodel.ModelElementChangeRightTarget;
import org.eclipse.emf.compare.diff.metamodel.ReferenceChangeRightTarget;
import org.eclipse.emf.compare.util.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Merger for an {@link ModelElementChangeRightTarget} operation.<br/>
 * <p>
 * Are considered for this merger :
 * <ul>
 * <li>{@link AddModelElement}</li>
 * <li>{@link RemoteRemoveModelElement}</li>
 * </ul>
 * </p>
 * 
 * @author <a href="mailto:laurent.goubet@obeo.fr">Laurent Goubet</a>
 */
public class ModelElementChangeRightTargetMerger extends DefaultMerger {

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#applyInOrigin()
	 */
    @Override
    public void applyInOrigin() {
        final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget) this.diff;
        final EObject origin = theDiff.getLeftParent();
        final EObject element = theDiff.getRightElement();
        final EObject newOne = copy(element);
        final EReference ref = element.eContainmentFeature();
        if (ref != null) {
            try {
                EFactory.eAdd(origin, ref.getName(), newOne);
                setXMIID(newOne, getXMIID(element));
            } catch (FactoryException e) {
                EMFComparePlugin.log(e, true);
            }
        } else {
            findLeftResource().getContents().add(newOne);
        }
        final Iterator<EObject> siblings = getDiffModel().eAllContents();
        while (siblings.hasNext()) {
            final DiffElement op = (DiffElement) siblings.next();
            if (op instanceof ReferenceChangeRightTarget) {
                final ReferenceChangeRightTarget link = (ReferenceChangeRightTarget) op;
                if (link.getRightAddedTarget().equals(element)) {
                    link.setLeftAddedTarget(newOne);
                }
            }
        }
        super.applyInOrigin();
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.compare.diff.merge.api.AbstractMerger#undoInTarget()
	 */
    @Override
    public void undoInTarget() {
        final ModelElementChangeRightTarget theDiff = (ModelElementChangeRightTarget) this.diff;
        final EObject element = theDiff.getRightElement();
        final EObject parent = theDiff.getRightElement().eContainer();
        EcoreUtil.remove(element);
        removeDanglingReferences(parent);
        super.undoInTarget();
    }
}
