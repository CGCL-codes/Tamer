package org.parallelj.designer.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.gmf.runtime.diagram.ui.editparts.IGraphicalEditPart;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.gmf.runtime.emf.ui.services.modelingassistant.ModelingAssistantProvider;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.parallelj.designer.edit.parts.ConditionEditPart;
import org.parallelj.designer.edit.parts.ForEachLoopEditPart;
import org.parallelj.designer.edit.parts.HandlerEditPart;
import org.parallelj.designer.edit.parts.InputConditionEditPart;
import org.parallelj.designer.edit.parts.OutputConditionEditPart;
import org.parallelj.designer.edit.parts.PipelineEditPart;
import org.parallelj.designer.edit.parts.PipelinePipelineCompartmentEditPart;
import org.parallelj.designer.edit.parts.PipelineProcedureEditPart;
import org.parallelj.designer.edit.parts.ProcedureEditPart;
import org.parallelj.designer.edit.parts.ProgramProgramCompartmentEditPart;
import org.parallelj.designer.edit.parts.SpecificationEditPart;
import org.parallelj.designer.edit.parts.WhileLoopEditPart;
import org.parallelj.designer.part.Messages;
import org.parallelj.designer.part.ParallelJDiagramEditorPlugin;

/**
 * @generated
 */
public class ParallelJModelingAssistantProvider extends ModelingAssistantProvider {

    /**
	 * @generated
	 */
    public List getTypesForPopupBar(IAdaptable host) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof SpecificationEditPart) {
            ArrayList<IElementType> types = new ArrayList<IElementType>(1);
            types.add(ParallelJElementTypes.Program_2001);
            return types;
        }
        if (editPart instanceof ProgramProgramCompartmentEditPart) {
            ArrayList<IElementType> types = new ArrayList<IElementType>(10);
            types.add(ParallelJElementTypes.InputCondition_3001);
            types.add(ParallelJElementTypes.OutputCondition_3002);
            types.add(ParallelJElementTypes.Condition_3003);
            types.add(ParallelJElementTypes.Predicate_3004);
            types.add(ParallelJElementTypes.Procedure_3005);
            types.add(ParallelJElementTypes.ForEachLoop_3006);
            types.add(ParallelJElementTypes.WhileLoop_3007);
            types.add(ParallelJElementTypes.Handler_3008);
            types.add(ParallelJElementTypes.Pipeline_3009);
            types.add(ParallelJElementTypes.Data_3011);
            return types;
        }
        if (editPart instanceof PipelinePipelineCompartmentEditPart) {
            ArrayList<IElementType> types = new ArrayList<IElementType>(1);
            types.add(ParallelJElementTypes.Procedure_3010);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSource(IAdaptable source) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof InputConditionEditPart) {
            return ((InputConditionEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof OutputConditionEditPart) {
            return ((OutputConditionEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof ProcedureEditPart) {
            return ((ProcedureEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof ForEachLoopEditPart) {
            return ((ForEachLoopEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof WhileLoopEditPart) {
            return ((WhileLoopEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof HandlerEditPart) {
            return ((HandlerEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof PipelineEditPart) {
            return ((PipelineEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof PipelineProcedureEditPart) {
            return ((PipelineProcedureEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnTarget(IAdaptable target) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof InputConditionEditPart) {
            return ((InputConditionEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof OutputConditionEditPart) {
            return ((OutputConditionEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof ProcedureEditPart) {
            return ((ProcedureEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof ForEachLoopEditPart) {
            return ((ForEachLoopEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof WhileLoopEditPart) {
            return ((WhileLoopEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof HandlerEditPart) {
            return ((HandlerEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof PipelineEditPart) {
            return ((PipelineEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof PipelineProcedureEditPart) {
            return ((PipelineProcedureEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof InputConditionEditPart) {
            return ((InputConditionEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof OutputConditionEditPart) {
            return ((OutputConditionEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof ProcedureEditPart) {
            return ((ProcedureEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof ForEachLoopEditPart) {
            return ((ForEachLoopEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof WhileLoopEditPart) {
            return ((WhileLoopEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof HandlerEditPart) {
            return ((HandlerEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof PipelineEditPart) {
            return ((PipelineEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof PipelineProcedureEditPart) {
            return ((PipelineProcedureEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof InputConditionEditPart) {
            return ((InputConditionEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof OutputConditionEditPart) {
            return ((OutputConditionEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof ProcedureEditPart) {
            return ((ProcedureEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof ForEachLoopEditPart) {
            return ((ForEachLoopEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof WhileLoopEditPart) {
            return ((WhileLoopEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof HandlerEditPart) {
            return ((HandlerEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof PipelineEditPart) {
            return ((PipelineEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof PipelineProcedureEditPart) {
            return ((PipelineProcedureEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof InputConditionEditPart) {
            return ((InputConditionEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof OutputConditionEditPart) {
            return ((OutputConditionEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof ProcedureEditPart) {
            return ((ProcedureEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof ForEachLoopEditPart) {
            return ((ForEachLoopEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof WhileLoopEditPart) {
            return ((WhileLoopEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof HandlerEditPart) {
            return ((HandlerEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof PipelineEditPart) {
            return ((PipelineEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof PipelineProcedureEditPart) {
            return ((PipelineProcedureEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public EObject selectExistingElementForSource(IAdaptable target, IElementType relationshipType) {
        return selectExistingElement(target, getTypesForSource(target, relationshipType));
    }

    /**
	 * @generated
	 */
    public EObject selectExistingElementForTarget(IAdaptable source, IElementType relationshipType) {
        return selectExistingElement(source, getTypesForTarget(source, relationshipType));
    }

    /**
	 * @generated
	 */
    protected EObject selectExistingElement(IAdaptable host, Collection types) {
        if (types.isEmpty()) {
            return null;
        }
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart == null) {
            return null;
        }
        Diagram diagram = (Diagram) editPart.getRoot().getContents().getModel();
        HashSet<EObject> elements = new HashSet<EObject>();
        for (Iterator<EObject> it = diagram.getElement().eAllContents(); it.hasNext(); ) {
            EObject element = it.next();
            if (isApplicableElement(element, types)) {
                elements.add(element);
            }
        }
        if (elements.isEmpty()) {
            return null;
        }
        return selectElement((EObject[]) elements.toArray(new EObject[elements.size()]));
    }

    /**
	 * @generated
	 */
    protected boolean isApplicableElement(EObject element, Collection types) {
        IElementType type = ElementTypeRegistry.getInstance().getElementType(element);
        return types.contains(type);
    }

    /**
	 * @generated
	 */
    protected EObject selectElement(EObject[] elements) {
        Shell shell = Display.getCurrent().getActiveShell();
        ILabelProvider labelProvider = new AdapterFactoryLabelProvider(ParallelJDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory());
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setMessage(Messages.ParallelJModelingAssistantProviderMessage);
        dialog.setTitle(Messages.ParallelJModelingAssistantProviderTitle);
        dialog.setMultipleSelection(false);
        dialog.setElements(elements);
        EObject selected = null;
        if (dialog.open() == Window.OK) {
            selected = (EObject) dialog.getFirstResult();
        }
        return selected;
    }
}
