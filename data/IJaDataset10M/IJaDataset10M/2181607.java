package siseor.diagram.providers;

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
import siseor.diagram.edit.parts.ContainerEditPart;
import siseor.diagram.edit.parts.InputEditPart;
import siseor.diagram.edit.parts.OperationEditPart;
import siseor.diagram.edit.parts.OutputEditPart;
import siseor.diagram.messages.Messages;
import siseor.diagram.part.SiseorDiagramEditorPlugin;

/**
 * @generated
 */
public class SiseorModelingAssistantProvider extends ModelingAssistantProvider {

    /**
	 * @generated
	 */
    public List getTypesForPopupBar(IAdaptable host) {
        IGraphicalEditPart editPart = (IGraphicalEditPart) host.getAdapter(IGraphicalEditPart.class);
        if (editPart instanceof OperationEditPart) {
            List types = new ArrayList();
            types.add(SiseorElementTypes.Output_2001);
            types.add(SiseorElementTypes.Input_2002);
            return types;
        }
        if (editPart instanceof ContainerEditPart) {
            List types = new ArrayList();
            types.add(SiseorElementTypes.Operation_1001);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSource(IAdaptable source) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof OutputEditPart) {
            List types = new ArrayList();
            types.add(SiseorElementTypes.Link_3001);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnTarget(IAdaptable target) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof InputEditPart) {
            List types = new ArrayList();
            types.add(SiseorElementTypes.Link_3001);
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getRelTypesOnSourceAndTarget(IAdaptable source, IAdaptable target) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof OutputEditPart) {
            List types = new ArrayList();
            if (targetEditPart instanceof InputEditPart) {
                types.add(SiseorElementTypes.Link_3001);
            }
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForSource(IAdaptable target, IElementType relationshipType) {
        IGraphicalEditPart targetEditPart = (IGraphicalEditPart) target.getAdapter(IGraphicalEditPart.class);
        if (targetEditPart instanceof InputEditPart) {
            List types = new ArrayList();
            if (relationshipType == SiseorElementTypes.Link_3001) {
                types.add(SiseorElementTypes.Output_2001);
            }
            return types;
        }
        return Collections.EMPTY_LIST;
    }

    /**
	 * @generated
	 */
    public List getTypesForTarget(IAdaptable source, IElementType relationshipType) {
        IGraphicalEditPart sourceEditPart = (IGraphicalEditPart) source.getAdapter(IGraphicalEditPart.class);
        if (sourceEditPart instanceof OutputEditPart) {
            List types = new ArrayList();
            if (relationshipType == SiseorElementTypes.Link_3001) {
                types.add(SiseorElementTypes.Input_2002);
            }
            return types;
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
        ILabelProvider labelProvider = new AdapterFactoryLabelProvider(SiseorDiagramEditorPlugin.getInstance().getItemProvidersAdapterFactory());
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(shell, labelProvider);
        dialog.setMessage(Messages.SiseorModelingAssistantProviderMessage);
        dialog.setTitle(Messages.SiseorModelingAssistantProviderTitle);
        dialog.setMultipleSelection(false);
        dialog.setElements(elements);
        EObject selected = null;
        if (dialog.open() == Window.OK) {
            selected = (EObject) dialog.getFirstResult();
        }
        return selected;
    }
}
