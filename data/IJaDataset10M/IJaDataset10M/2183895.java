package com.ssd.mdaworks.classdiagram.custom.actions;

import java.util.List;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequest;
import org.eclipse.gmf.runtime.diagram.ui.requests.CreateViewRequestFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import com.ssd.mdaworks.classdiagram.classDiagram.diagram.edit.parts.MClassEditPart;
import com.ssd.mdaworks.classdiagram.classDiagram.diagram.providers.ClassdiagramElementTypes;

public class AddAttributeToClassAction implements IObjectActionDelegate {

    private MClassEditPart selectedElement;

    ;

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    public void run(IAction action) {
        if (selectedElement != null) {
            CompoundCommand cc = new CompoundCommand("Add attribute to class");
            CreateViewRequest attributeRequest = CreateViewRequestFactory.getCreateShapeRequest(ClassdiagramElementTypes.MAttribute_3001, selectedElement.getDiagramPreferencesHint());
            Command createAttributeCommand = selectedElement.getCommand(attributeRequest);
            cc.add(createAttributeCommand);
            selectedElement.getDiagramEditDomain().getDiagramCommandStack().execute(cc);
            IAdaptable attributeViewAdapter = (IAdaptable) ((List) attributeRequest.getNewObject()).get(0);
            final EditPartViewer viewer = selectedElement.getViewer();
            final EditPart elementPart = (EditPart) viewer.getEditPartRegistry().get(attributeViewAdapter.getAdapter(View.class));
            if (elementPart != null) {
                Display.getCurrent().asyncExec(new Runnable() {

                    public void run() {
                        viewer.setSelection(new StructuredSelection(elementPart));
                        Request der = new Request(RequestConstants.REQ_DIRECT_EDIT);
                        elementPart.performRequest(der);
                    }
                });
            }
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof StructuredSelection) {
            Object object = ((StructuredSelection) selection).getFirstElement();
            if (object instanceof MClassEditPart) {
                selectedElement = (MClassEditPart) object;
            }
        }
    }
}
