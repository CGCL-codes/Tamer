package org.xmi.gui.eclipse.views.properties.section.element;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.xmi.gui.eclipse.views.adapter.ElementAdaptable;
import org.xmi.infoset.XMIElement;

public class ElementNamespace extends AbstractPropertySection {

    private Text labelText;

    private XMIElement xmiElement;

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        super.createControls(parent, aTabbedPropertySheetPage);
        Composite composite = getWidgetFactory().createFlatFormComposite(parent);
        FormData dataId = null;
        labelText = getWidgetFactory().createText(composite, "");
        dataId = new FormData();
        dataId.left = new FormAttachment(0, STANDARD_LABEL_WIDTH);
        dataId.right = new FormAttachment(100, 0);
        dataId.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
        labelText.setLayoutData(dataId);
        labelText.setEditable(false);
        CLabel labelId = getWidgetFactory().createCLabel(composite, "Namespace");
        dataId = new FormData();
        dataId.left = new FormAttachment(0, 0);
        dataId.right = new FormAttachment(labelText, -ITabbedPropertyConstants.HSPACE);
        dataId.top = new FormAttachment(labelText, 0, SWT.CENTER);
        labelId.setLayoutData(dataId);
    }

    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        Object input = ((IStructuredSelection) selection).getFirstElement();
        this.xmiElement = (XMIElement) ((ElementAdaptable) input).getAdapter(XMIElement.class);
    }

    @Override
    public void refresh() {
        String namespace = "";
        if (xmiElement.getXmiNamespace() != null) {
            namespace = xmiElement.getXmiNamespace();
        }
        labelText.setText(namespace);
    }
}
