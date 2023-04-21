package org.parallelj.mda.controlflow.properties.zones;

import java.util.List;
import org.parallelj.mda.controlflow.properties.core.Zone;
import org.parallelj.mda.controlflow.diagram.extension.tools.Commands;
import org.parallelj.mda.controlflow.model.controlflow.ControlFlowPackage;
import org.parallelj.mda.controlflow.model.controlflow.Task;
import org.parallelj.mda.controlflow.model.controlflow.TaskVisibility;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;

public class VisibilityZone extends Zone {

    private CLabel gVisibilityLabel;

    private CCombo gVisibilityCombo;

    public VisibilityZone(Composite parent) {
        super(parent, false);
    }

    @Override
    public void addItemsToZone() {
        gVisibilityLabel = getWidgetFactory().createCLabel(getZone(), "Visibility :");
        gVisibilityCombo = getWidgetFactory().createCCombo(getZone(), SWT.READ_ONLY);
        List<TaskVisibility> vVisibilities = TaskVisibility.VALUES;
        for (TaskVisibility vVisibility : vVisibilities) {
            gVisibilityCombo.add(vVisibility.getName());
        }
    }

    @Override
    public void addLayoutsToItems() {
        fData = new FormData();
        fData.left = new FormAttachment(0, 5);
        fData.top = new FormAttachment(0, 3);
        fData.width = 120;
        gVisibilityLabel.setLayoutData(fData);
        fData = new FormData();
        fData.left = new FormAttachment(gVisibilityLabel, 5);
        fData.right = new FormAttachment(30, -5);
        fData.top = new FormAttachment(0, 5);
        gVisibilityCombo.setLayoutData(fData);
    }

    @Override
    public void addListenersToItems() {
        SelectionListener vListener = new SelectionAdapter() {

            public void widgetSelected(SelectionEvent pEvent) {
                CCombo combo = (CCombo) pEvent.getSource();
                Commands.doSetValue(getEditingDomain(), getEObject(), ControlFlowPackage.eINSTANCE.getTask_Visibility(), TaskVisibility.VALUES.get(combo.getSelectionIndex()), getEditPart());
                refreshZoneAndDiagram();
            }
        };
        gVisibilityCombo.addSelectionListener(vListener);
    }

    @Override
    public void updateItemsValues() {
        EObject eObject = getEObject();
        String tooltip = "";
        if (eObject != null && eObject instanceof Task) {
            Task task = (Task) eObject;
            TaskVisibility visibility = task.getVisibility();
            gVisibilityCombo.setText(visibility.getName());
            if (visibility == TaskVisibility.PUBLIC) tooltip = "A public Task is bindable by Elements from external ControlFlow Models"; else if (visibility == TaskVisibility.PRIVATE) tooltip = "A public Task is NOT bindable by Elements from external ControlFlow Models";
        }
        gVisibilityCombo.setToolTipText(tooltip);
        gVisibilityLabel.setToolTipText(tooltip);
    }
}
