package org.cubictest.ui.sections;

import org.cubictest.model.Test;
import org.cubictest.model.UserActions;
import org.cubictest.ui.gef.controller.UserActionsEditPart;
import org.cubictest.ui.gef.editors.GraphicalTestEditor;
import org.cubictest.ui.gef.wizards.UserActionComponent;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

public class UserActionsSection extends AbstractPropertySection {

    private UserActions actions;

    private UserActionComponent component;

    private Composite parent = null;

    private boolean created = false;

    private Composite composite;

    private Test test;

    public UserActionsSection() {
    }

    @Override
    public void setInput(IWorkbenchPart part, ISelection selection) {
        super.setInput(part, selection);
        Assert.isTrue(selection instanceof IStructuredSelection);
        Object input = ((IStructuredSelection) selection).getFirstElement();
        Assert.isTrue(input instanceof UserActionsEditPart);
        actions = (UserActions) ((UserActionsEditPart) input).getModel();
        Assert.isTrue(part instanceof GraphicalTestEditor);
        test = ((GraphicalTestEditor) part).getTest();
        if (actions != null && !created && parent != null) {
            component = new UserActionComponent(actions, test);
            composite = component.createControl(parent);
            this.parent.setSize(400, 300);
            created = true;
        }
    }

    @Override
    public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
        if (parent != null) {
            super.createControls(parent, aTabbedPropertySheetPage);
            this.parent = parent;
        }
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
