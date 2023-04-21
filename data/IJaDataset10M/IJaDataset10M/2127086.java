package org.parallelj.designer.edit.policies;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.diagram.ui.commands.DeferredLayoutCommand;
import org.eclipse.gmf.runtime.diagram.ui.commands.ICommandProxy;
import org.eclipse.gmf.runtime.diagram.ui.commands.SetViewMutabilityCommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.emf.core.util.EObjectAdapter;
import org.eclipse.gmf.runtime.notation.Node;
import org.eclipse.gmf.runtime.notation.View;
import org.parallelj.designer.edit.parts.ConditionEditPart;
import org.parallelj.designer.edit.parts.DataEditPart;
import org.parallelj.designer.edit.parts.ForEachLoopEditPart;
import org.parallelj.designer.edit.parts.HandlerEditPart;
import org.parallelj.designer.edit.parts.InputConditionEditPart;
import org.parallelj.designer.edit.parts.OutputConditionEditPart;
import org.parallelj.designer.edit.parts.PipelineEditPart;
import org.parallelj.designer.edit.parts.PredicateEditPart;
import org.parallelj.designer.edit.parts.ProcedureEditPart;
import org.parallelj.designer.edit.parts.WhileLoopEditPart;
import org.parallelj.designer.part.ParallelJDiagramUpdater;
import org.parallelj.designer.part.ParallelJNodeDescriptor;
import org.parallelj.designer.part.ParallelJVisualIDRegistry;
import org.parallelj.model.ParallelJPackage;

/**
 * @generated
 */
public class ProgramProgramCompartmentCanonicalEditPolicy extends CanonicalEditPolicy {

    /**
	 * @generated
	 */
    private Set<EStructuralFeature> myFeaturesToSynchronize;

    /**
	 * @generated
	 */
    protected Set getFeaturesToSynchronize() {
        if (myFeaturesToSynchronize == null) {
            myFeaturesToSynchronize = new HashSet<EStructuralFeature>();
            myFeaturesToSynchronize.add(ParallelJPackage.eINSTANCE.getProgram_Elements());
            myFeaturesToSynchronize.add(ParallelJPackage.eINSTANCE.getProgram_Predicates());
            myFeaturesToSynchronize.add(ParallelJPackage.eINSTANCE.getProgram_Data());
        }
        return myFeaturesToSynchronize;
    }

    /**
	 * @generated
	 */
    @SuppressWarnings("rawtypes")
    protected List getSemanticChildrenList() {
        View viewObject = (View) getHost().getModel();
        LinkedList<EObject> result = new LinkedList<EObject>();
        List<ParallelJNodeDescriptor> childDescriptors = ParallelJDiagramUpdater.getProgramProgramCompartment_7001SemanticChildren(viewObject);
        for (ParallelJNodeDescriptor d : childDescriptors) {
            result.add(d.getModelElement());
        }
        return result;
    }

    /**
	 * @generated
	 */
    protected boolean isOrphaned(Collection<EObject> semanticChildren, final View view) {
        return isMyDiagramElement(view) && !semanticChildren.contains(view.getElement());
    }

    /**
	 * @generated
	 */
    private boolean isMyDiagramElement(View view) {
        int visualID = ParallelJVisualIDRegistry.getVisualID(view);
        switch(visualID) {
            case InputConditionEditPart.VISUAL_ID:
            case OutputConditionEditPart.VISUAL_ID:
            case ConditionEditPart.VISUAL_ID:
            case PredicateEditPart.VISUAL_ID:
            case ProcedureEditPart.VISUAL_ID:
            case ForEachLoopEditPart.VISUAL_ID:
            case WhileLoopEditPart.VISUAL_ID:
            case HandlerEditPart.VISUAL_ID:
            case PipelineEditPart.VISUAL_ID:
            case DataEditPart.VISUAL_ID:
                return true;
        }
        return false;
    }

    /**
	 * @generated
	 */
    protected void refreshSemantic() {
        if (resolveSemanticElement() == null) {
            return;
        }
        LinkedList<IAdaptable> createdViews = new LinkedList<IAdaptable>();
        List<ParallelJNodeDescriptor> childDescriptors = ParallelJDiagramUpdater.getProgramProgramCompartment_7001SemanticChildren((View) getHost().getModel());
        LinkedList<View> orphaned = new LinkedList<View>();
        LinkedList<View> knownViewChildren = new LinkedList<View>();
        for (View v : getViewChildren()) {
            if (isMyDiagramElement(v)) {
                knownViewChildren.add(v);
            }
        }
        for (Iterator<ParallelJNodeDescriptor> descriptorsIterator = childDescriptors.iterator(); descriptorsIterator.hasNext(); ) {
            ParallelJNodeDescriptor next = descriptorsIterator.next();
            String hint = ParallelJVisualIDRegistry.getType(next.getVisualID());
            LinkedList<View> perfectMatch = new LinkedList<View>();
            for (View childView : getViewChildren()) {
                EObject semanticElement = childView.getElement();
                if (next.getModelElement().equals(semanticElement)) {
                    if (hint.equals(childView.getType())) {
                        perfectMatch.add(childView);
                    }
                }
            }
            if (perfectMatch.size() > 0) {
                descriptorsIterator.remove();
                knownViewChildren.remove(perfectMatch.getFirst());
            }
        }
        orphaned.addAll(knownViewChildren);
        ArrayList<CreateViewRequest.ViewDescriptor> viewDescriptors = new ArrayList<CreateViewRequest.ViewDescriptor>(childDescriptors.size());
        for (ParallelJNodeDescriptor next : childDescriptors) {
            String hint = ParallelJVisualIDRegistry.getType(next.getVisualID());
            IAdaptable elementAdapter = new CanonicalElementAdapter(next.getModelElement(), hint);
            CreateViewRequest.ViewDescriptor descriptor = new CreateViewRequest.ViewDescriptor(elementAdapter, Node.class, hint, ViewUtil.APPEND, false, host().getDiagramPreferencesHint());
            viewDescriptors.add(descriptor);
        }
        boolean changed = deleteViews(orphaned.iterator());
        CreateViewRequest request = getCreateViewRequest(viewDescriptors);
        Command cmd = getCreateViewCommand(request);
        if (cmd != null && cmd.canExecute()) {
            SetViewMutabilityCommand.makeMutable(new EObjectAdapter(host().getNotationView())).execute();
            executeCommand(cmd);
            @SuppressWarnings("unchecked") List<IAdaptable> nl = (List<IAdaptable>) request.getNewObject();
            createdViews.addAll(nl);
        }
        if (changed || createdViews.size() > 0) {
            postProcessRefreshSemantic(createdViews);
        }
        if (createdViews.size() > 1) {
            DeferredLayoutCommand layoutCmd = new DeferredLayoutCommand(host().getEditingDomain(), createdViews, host());
            executeCommand(new ICommandProxy(layoutCmd));
        }
        makeViewsImmutable(createdViews);
    }
}
