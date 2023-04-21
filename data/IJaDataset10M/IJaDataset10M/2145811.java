package org.eclipse.help.ui.internal.views;

import org.eclipse.help.IContext;
import org.eclipse.help.ui.internal.IHelpUIConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.forms.AbstractFormPart;
import org.eclipse.ui.forms.ManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ILayoutExtension;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class RelatedTopicsPart extends AbstractFormPart implements IHelpPart {

    private ManagedForm mform;

    private ContextHelpPart contextHelpPart;

    private DynamicHelpPart dynamicHelpPart;

    private ReusableHelpPart parent;

    private String id;

    private int VSPACE = 10;

    class RelatedLayout extends Layout implements ILayoutExtension {

        protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
            Point topSize = contextHelpPart.getControl().computeSize(wHint, hHint, flushCache);
            Point botSize = dynamicHelpPart.getControl().computeSize(wHint, hHint, flushCache);
            Point size = new Point(0, 0);
            size.x = Math.max(topSize.x, botSize.x);
            size.y = VSPACE + topSize.y + VSPACE + botSize.y;
            return size;
        }

        protected void layout(Composite composite, boolean flushCache) {
            Rectangle carea = composite.getClientArea();
            Point topSize = contextHelpPart.getControl().computeSize(carea.width, SWT.DEFAULT, flushCache);
            Point botSize = dynamicHelpPart.getControl().computeSize(carea.width, SWT.DEFAULT, flushCache);
            int y = VSPACE;
            contextHelpPart.getControl().setBounds(0, y, carea.width, topSize.y);
            y += topSize.y + VSPACE;
            dynamicHelpPart.getControl().setBounds(0, y, carea.width, botSize.y);
        }

        public int computeMinimumWidth(Composite parent, boolean changed) {
            int top = computeMinimumWidth(contextHelpPart, parent, changed);
            int bot = computeMinimumWidth(dynamicHelpPart, parent, changed);
            return Math.max(top, bot);
        }

        private int computeMinimumWidth(IHelpPart part, Composite parent, boolean changed) {
            ILayoutExtension le = (ILayoutExtension) ((Composite) part.getControl()).getLayout();
            return le.computeMinimumWidth(parent, changed);
        }

        public int computeMaximumWidth(Composite parent, boolean changed) {
            return computeSize(parent, SWT.DEFAULT, SWT.DEFAULT, changed).x;
        }
    }

    public RelatedTopicsPart(Composite parent, FormToolkit toolkit) {
        ScrolledForm form = toolkit.createScrolledForm(parent);
        mform = new ManagedForm(toolkit, form) {

            public void reflow(boolean changed) {
                super.reflow(changed);
                RelatedTopicsPart.this.parent.reflow();
            }
        };
        form.getBody().setLayout(new RelatedLayout());
        contextHelpPart = new ContextHelpPart(form.getBody(), toolkit);
        dynamicHelpPart = new DynamicHelpPart(form.getBody(), toolkit);
    }

    public void init(ReusableHelpPart parent, String id, IMemento memento) {
        this.parent = parent;
        this.id = id;
        contextHelpPart.init(parent, IHelpUIConstants.HV_CONTEXT_HELP, memento);
        dynamicHelpPart.init(parent, IHelpUIConstants.HV_SEARCH_RESULT, memento);
        mform.addPart(contextHelpPart);
        mform.addPart(dynamicHelpPart);
        mform.initialize();
    }

    public void dispose() {
        mform.dispose();
    }

    public void handleActivation(Control c, IWorkbenchPart wpart) {
        contextHelpPart.handleActivation(null, null, c, wpart, false);
    }

    public void setDefaultText(String defaultText) {
        contextHelpPart.setDefaultText(defaultText);
    }

    public Control getControl() {
        return mform.getForm();
    }

    public String getId() {
        return id;
    }

    public void setVisible(boolean visible) {
        mform.getForm().setVisible(visible);
    }

    public boolean hasFocusControl(Control control) {
        return contextHelpPart.hasFocusControl(control) || dynamicHelpPart.hasFocusControl(control);
    }

    public boolean fillContextMenu(IMenuManager manager) {
        Control focusControl = mform.getForm().getDisplay().getFocusControl();
        if (contextHelpPart.hasFocusControl(focusControl)) return contextHelpPart.fillContextMenu(manager);
        return dynamicHelpPart.fillContextMenu(manager);
    }

    public IAction getGlobalAction(String id) {
        return contextHelpPart.getGlobalAction(id);
    }

    public void stop() {
        contextHelpPart.stop();
        dynamicHelpPart.stop();
    }

    public void toggleRoleFilter() {
        contextHelpPart.toggleRoleFilter();
        dynamicHelpPart.toggleRoleFilter();
    }

    public void refilter() {
        contextHelpPart.refilter();
        dynamicHelpPart.refilter();
    }

    public boolean setFormInput(Object input) {
        return mform.setInput(input);
    }

    public void startSearch(String newPhrase, IContext excludeContext) {
        dynamicHelpPart.startSearch(newPhrase, excludeContext);
    }

    public void saveState(IMemento memento) {
    }

    public void setFocus() {
        if (contextHelpPart != null) contextHelpPart.setFocus();
    }
}
