package com.fh.auge.views;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.part.ViewPart;
import com.fh.auge.model.DefaultTreeNode;
import com.fh.auge.model.Position;
import com.fh.auge.model.PositionGroup;
import com.fh.auge.ui.sections.IViewSectionFactory;
import com.fh.auge.ui.sections.ViewSectionSupport;
import com.fh.auge.ui.sections.portfolio.PositionGroupSectionFactory;
import com.fh.auge.ui.sections.portfolio.PositionSectionFactory;

public class PropertyPageView extends ViewPart implements ISelectionListener {

    private FormToolkit toolkit;

    private ScrolledForm form;

    private ManagedForm mf;

    private ViewSectionSupport support;

    /**
	 * The constructor.
	 */
    public PropertyPageView() {
    }

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        toolkit = new FormToolkit(parent.getDisplay());
        form = toolkit.createScrolledForm(parent);
        toolkit.decorateFormHeading(form.getForm());
        form.getBody().setLayout(new ColumnLayout());
        mf = new ManagedForm(toolkit, form);
        IViewSectionFactory factory1 = new PositionSectionFactory();
        IViewSectionFactory factory2 = new PositionGroupSectionFactory();
        Map<Class<?>, IViewSectionFactory> mapping = new HashMap<Class<?>, IViewSectionFactory>();
        mapping.put(Position.class, factory1);
        mapping.put(PositionGroup.class, factory2);
        support = new ViewSectionSupport(toolkit, form);
        support.setSectionFactoryMapping(mapping);
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
    }

    /**
	 * Passing the focus request to the form.
	 */
    public void setFocus() {
    }

    /**
	 * Disposes the toolkit
	 */
    public void dispose() {
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
        super.dispose();
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection.isEmpty()) {
            support.setInput(null);
        }
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            Object el = sel.getFirstElement();
            if (el instanceof DefaultTreeNode) {
                DefaultTreeNode node = (DefaultTreeNode) el;
                support.setInput(node.getUserObject());
            } else {
                support.setInput(el);
            }
        }
    }
}
