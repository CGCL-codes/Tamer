package com.sf.plctest.testmodel.diagram.providers;

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
import com.sf.plctest.testmodel.diagram.edit.parts.Condition2EditPart;
import com.sf.plctest.testmodel.diagram.edit.parts.ConditionConditionBlock2EditPart;
import com.sf.plctest.testmodel.diagram.edit.parts.ConditionConditionBlockEditPart;
import com.sf.plctest.testmodel.diagram.edit.parts.ConditionEditPart;
import com.sf.plctest.testmodel.diagram.edit.parts.FlowChartEditPart;
import com.sf.plctest.testmodel.diagram.edit.parts.JumpEditPart;
import com.sf.plctest.testmodel.diagram.edit.parts.OtherEditPart;
import com.sf.plctest.testmodel.diagram.part.Messages;
import com.sf.plctest.testmodel.diagram.part.TestDiagramEditorPlugin;

/**
 * @generated
 */
public class TestModelingAssistantProvider extends ModelingAssistantProvider {

    /**
	 * @generated
	 */
    public List getTypesForPopupBar(IAdaptable host) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof ConditionConditionBlockEditPart) {
            ArrayList types = new ArrayList(1);
            types.add(TestElementTypes.Condition_3001);
            return types;
        }
        if (editPart instanceof ConditionConditionBlock2EditPart) {
            ArrayList types = new ArrayList(1);
            types.add(TestElementTypes.Condition_3001);
            return types;
        }
        if (editPart instanceof FlowChartEditPart) {
            ArrayList types = new ArrayList(4);
            types.add(TestElementTypes.Jump_2001);
            types.add(TestElementTypes.Condition_2002);
            types.add(TestElementTypes.Other_2003);
            types.add(TestElementTypes.CoverageCalculator_2004);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSource(IAdaptable source) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof JumpEditPart) {
            return ((JumpEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof OtherEditPart) {
            return ((OtherEditPart) sourceEditPart).getMARelTypesOnSource();
        }
        if (sourceEditPart instanceof Condition2EditPart) {
            return ((Condition2EditPart) sourceEditPart).getMARelTypesOnSource();
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnTarget(IAdaptable target) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof JumpEditPart) {
            return ((JumpEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof OtherEditPart) {
            return ((OtherEditPart) targetEditPart).getMARelTypesOnTarget();
        }
        if (targetEditPart instanceof Condition2EditPart) {
            return ((Condition2EditPart) targetEditPart).getMARelTypesOnTarget();
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof JumpEditPart) {
            return ((JumpEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof OtherEditPart) {
            return ((OtherEditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        if (sourceEditPart instanceof Condition2EditPart) {
            return ((Condition2EditPart) sourceEditPart).getMARelTypesOnSourceAndTarget(targetEditPart);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof JumpEditPart) {
            return ((JumpEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof OtherEditPart) {
            return ((OtherEditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        if (targetEditPart instanceof Condition2EditPart) {
            return ((Condition2EditPart) targetEditPart).getMATypesForSource(relationshipType);
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof JumpEditPart) {
            return ((JumpEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof ConditionEditPart) {
            return ((ConditionEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof OtherEditPart) {
            return ((OtherEditPart) sourceEditPart).getMATypesForTarget(relationshipType);
        }
        if (sourceEditPart instanceof Condition2EditPart) {
            return ((Condition2EditPart) sourceEditPart).getMATypesForTarget(relationshipType);
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
        Collection elements = new HashSet();
        for (Iterator it = diagram.getElement().eAllContents(); it.hasNext(); ) {
            EObject element = (EObject) it.next();
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
        ILabelProvider labelProvider = new AdapterFactoryLabelProvider(TestDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory());
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setMessage(Messages.TestModelingAssistantProviderMessage);
        dialog.setTitle(Messages.TestModelingAssistantProviderTitle);
        dialog.setMultipleSelection(false);
        dialog.setElements(elements);
        EObject selected = null;
        if (dialog.open() == Window.OK) {
            selected = (EObject) dialog.getFirstResult();
        }
        return selected;
    }
}
